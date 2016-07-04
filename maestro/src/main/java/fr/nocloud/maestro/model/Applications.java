package fr.nocloud.maestro.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Applications {

    public Map<String, Application> applications;

    @JsonCreator
    public Applications(@JsonProperty("applications") List<Application> applications) {

        this.applications = applications == null ? null : applications.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
    }

    public Map<String, Application> getApplications() {
        return applications;
    }
}
