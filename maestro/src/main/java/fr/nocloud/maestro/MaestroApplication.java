package fr.nocloud.maestro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MaestroApplication {

    @Bean
    public Map<String, String> getEnvs() {

        Map<String, String> envs = new HashMap<>();
        envs.put("DOMAIN", "arnaudbouts.fr");

        return envs;
    }

    public static void main(String[] args) {
        SpringApplication.run(MaestroApplication.class, args);
    }
}
