package fr.nocloud.maestro.model;

import java.util.List;

public class Application {

    public String id;

    public String name;

    public String description;

    public List<Parameter> parameters;

    public String dockerCompose;

    public List<Action> installActions;

    public List<Action> startActions;

    public List<Action> stopActions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getDockerCompose() {
        return dockerCompose;
    }

    public void setDockerCompose(String dockerCompose) {
        this.dockerCompose = dockerCompose;
    }

    public List<Action> getInstallActions() {
        return installActions;
    }

    public void setInstallActions(List<Action> installActions) {
        this.installActions = installActions;
    }

    public List<Action> getStartActions() {
        return startActions;
    }

    public void setStartActions(List<Action> startActions) {
        this.startActions = startActions;
    }

    public List<Action> getStopActions() {
        return stopActions;
    }

    public void setStopActions(List<Action> stopActions) {
        this.stopActions = stopActions;
    }
}
