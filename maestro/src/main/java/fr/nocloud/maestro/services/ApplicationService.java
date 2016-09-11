package fr.nocloud.maestro.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import fr.nocloud.maestro.model.*;
import fr.nocloud.maestro.utils.Iptables;
import fr.nocloud.maestro.utils.ProcessUtils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApplicationService {

    @Value("${app.install.dir}")
    private String appInstallDir;

    @Value("${catalog.url}")
    private String catalogUrl;

    @Autowired
    private Map<String, String> envs;

    @PostConstruct
    public void refresh() {

        try(InputStream in = new URL(catalogUrl).openStream();
                OutputStream out = Files.newOutputStream(Paths.get(appInstallDir).resolve("catalog.yml"), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            IOUtils.copy(in, out);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Applications getApplications() {

        Applications applications = null;

        try(InputStream in = Files.newInputStream(Paths.get(appInstallDir).resolve("catalog.yml"))) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            applications = mapper.readValue(in, Applications.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return applications;
    }

    public void install(String applicationId, Map<String, String> parameters) {

        Application application = getApplications().getApplications().get(applicationId);

        if(application == null) {
            throw new NoSuchElementException();
        }

        try {
            Files.createDirectories(getAppInstallDir(application));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // définition des paramètres
        writeAppEnvFile(application, parameters);

        // génération du docker-compose.yml
        writeAppDockerComposeFile(application);

        // commande à l'install
        exec(application.getInstallActions());

        if(application.getIptables() != null) {
            application.getIptables().stream().forEach(Iptables::accept);
        }

        if(application.getWriteFiles() != null) {
            application.getWriteFiles().stream().forEach(this::writeFile);
        }

        // démarrage du container
        up(application);
    }

    private void writeAppDockerComposeFile(Application application) {

        String dockerCompose = application.getDockerCompose();

        File appFile = getAppDockerComposeFile(application);

        if(!appFile.getParentFile().exists()) {
            appFile.getParentFile().mkdirs();
        }

        try(PrintWriter writer = new PrintWriter(appFile, "UTF-8")) {

            writer.write(dockerCompose);
        } catch (FileNotFoundException e) {

            throw new RuntimeException("Unable to create file " + appFile);
        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("Unable to encode file using charset UTF-8");
        }
    }

    private void writeAppEnvFile(Application application, Map<String, String> parameters) {

        File envFile = getAppEnvFile(application);

        if(!envFile.getParentFile().exists()) {
            envFile.getParentFile().mkdirs();
        }

        try(PrintWriter writer = new PrintWriter(envFile, "UTF-8")) {

            if(!CollectionUtils.isEmpty(application.getParameters())) {
                for (Parameter parameter : application.getParameters()) {
                    String value = parameters.get(parameter.getName());

                    if (value == null) {
                        value = parameter.getDefaultValue();
                    }

                    writer.println(parameter.getName() + "=" + value);
                }
            }

        } catch (FileNotFoundException e) {

            throw new RuntimeException("Unable to create file " + envFile);
        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("Unable to encode file using charset UTF-8");
        }
    }

    private void writeFile(WriteFile writeFile) {

        try {

            Files.write(Paths.get(writeFile.getPath()), resolveEnvs(writeFile.getContent()).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String resolveEnvs(String stringToResolve) {

        String result = stringToResolve;

        // On cherche le pattern ${...}

        Pattern pattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
        Matcher matcher = pattern.matcher(stringToResolve);

        // Pour chaque ${...} trouvé
        while(matcher.find()) {

            String varName = matcher.group(1);
            if(!envs.containsKey(varName)) {
                throw new RuntimeException(String.format("La variable d'environnement '%s' n'est pas définie", varName));
            }

            String val = envs.get(varName);

            // On le remplace par la valeur de la variable correspondante
            result = result.replace("${" + varName + "}", val);
        }

        return result;
    }

    public void uninstall(String applicationId) {

        Application application = getApplications().getApplications().get(applicationId);

        if(application == null) {
            throw new NoSuchElementException();
        }

        down(application);

        if(application.getIptables() != null) {
            application.getIptables().stream().forEach(Iptables::delete);
        }

        File dockerComposeFile = getAppDockerComposeFile(application);

        File envFile = getAppEnvFile(application);

        dockerComposeFile.delete();
        envFile.delete();
        dockerComposeFile.getParentFile().delete();
    }

    public void start(String applicationId) {

        Application application = getApplications().getApplications().get(applicationId);

        if(application == null) {
            throw new NoSuchElementException();
        }

        start(application);
    }

    public void stop(String applicationId) {

        Application application = getApplications().getApplications().get(applicationId);

        if(application == null) {
            throw new NoSuchElementException();
        }

        stop(application);
    }

    private Path getAppInstallDir(Application application) {
        return Paths.get(appInstallDir, application.getId());
    }

    private File getAppDockerComposeFile(Application application) {

        return getAppInstallDir(application).resolve("docker-compose.yml").toFile();
    }

    private File getAppEnvFile(Application application) {

        return getAppInstallDir(application).resolve(application.getId() + ".env").toFile();
    }

    private void up(Application application) {

        exec(application.getStartActions());

        ProcessUtils.exec("docker-compose", "-f", getAppDockerComposeFile(application).getAbsolutePath(), "up", "-d");
    }

    private void start(Application application) {

        exec(application.getStartActions());

        ProcessUtils.exec("docker-compose", "-f", getAppDockerComposeFile(application).getAbsolutePath(), "start");
    }

    private void stop(Application application) {

        exec(application.getStopActions());

        ProcessUtils.exec("docker-compose", "-f", getAppDockerComposeFile(application).getAbsolutePath(), "stop");
    }

    private void down(Application application) {

        exec(application.getStopActions());

        ProcessUtils.exec("docker-compose", "-f", getAppDockerComposeFile(application).getAbsolutePath(), "down");
    }

    private void exec(List<Action> actions) {

        if(! CollectionUtils.isEmpty(actions)) {

            for (Action action : actions) {

                ProcessUtils.exec(action.getCommand(), action.getParameters());
            }
        }
    }
}
