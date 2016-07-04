package fr.nocloud.maestro.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import fr.nocloud.maestro.model.Action;
import fr.nocloud.maestro.model.Application;
import fr.nocloud.maestro.model.Applications;
import fr.nocloud.maestro.model.Parameter;
import fr.nocloud.maestro.utils.ProcessUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ApplicationService {

    @Value("${app.install.dir}")
    private String appInstallDir;


    public Applications getApplications() {

        Applications applications = null;

        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("catalog.yml");
            applications = mapper.readValue(resource, Applications.class);
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

        // définition des paramètres
        writeAppEnvFile(application, parameters);

        // génération du docker-compose.yml
        writeAppDockerComposeFile(application);

        // commande à l'install
        exec(application.getInstallActions());

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

        if(!CollectionUtils.isEmpty(application.getParameters())) {
            File envFile = getAppEnvFile(application);

            try(PrintWriter writer = new PrintWriter(envFile, "UTF-8")) {

                for(Parameter parameter : application.getParameters()) {
                    String value = parameters.get(parameter.getName());

                    if(value == null) {
                        value = parameter.getDefaultValue();
                    }

                    writer.println(parameter.getName() + "=" + value);
                }

            } catch (FileNotFoundException e) {

                throw new RuntimeException("Unable to create file " + envFile);
            } catch (UnsupportedEncodingException e) {

                throw new RuntimeException("Unable to encode file using charset UTF-8");
            }
        }
    }

    public void uninstall(String applicationId) {

        Application application = getApplications().getApplications().get(applicationId);

        if(application == null) {
            throw new NoSuchElementException();
        }

        stop(application);

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

    private File getAppInstallDir(Application application) {
        return new File(appInstallDir, application.getId());
    }

    private File getAppDockerComposeFile(Application application) {

        return new File(getAppInstallDir(application), "docker-compose.yml");
    }

    private File getAppEnvFile(Application application) {

        return new File(getAppInstallDir(application), application.getId() + ".env");
    }

    private void up(Application application) {

        exec(application.getStartActions());

        ProcessUtils.exec("docker-compose", "-f", getAppDockerComposeFile(application).getAbsolutePath(), "up");
    }

    private void start(Application application) {

        exec(application.getStartActions());

        ProcessUtils.exec("docker-compose", "-f", getAppDockerComposeFile(application).getAbsolutePath(), "start");
    }

    private void stop(Application application) {

        exec(application.getStopActions());

        ProcessUtils.exec("docker-compose", "-f", getAppDockerComposeFile(application).getAbsolutePath(), "stop");
    }

    private void exec(List<Action> actions) {

        if(! CollectionUtils.isEmpty(actions)) {

            for (Action action : actions) {

                ProcessUtils.exec(action.getCommand(), action.getParameters());
            }
        }
    }
}
