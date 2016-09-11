package fr.nocloud.maestro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fr.nocloud.maestro.model.Maestro;
import fr.nocloud.maestro.model.catalog.Applications;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MaestroApplication {

    @Bean
    public Maestro getConfig() {

        try(InputStream in = this.getClass().getResourceAsStream("/config.yml")) {

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(in, Maestro.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        SpringApplication.run(MaestroApplication.class, args);
    }
}
