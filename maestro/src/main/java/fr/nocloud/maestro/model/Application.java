package fr.nocloud.maestro.model;

import java.util.List;

public class Application {

    private String id;

    private String name;

    private String description;

    private List<Parameter> parameters;

    private String dockerCompose;

    private List<WriteFile> writeFiles;

    private List<Action> installActions;

    private List<Action> startActions;

    private List<Action> stopActions;

    private List<Iptable> iptables;

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

    public List<Iptable> getIptables() {
        return iptables;
    }

    public void setIptables(List<Iptable> iptables) {
        this.iptables = iptables;
    }

    public List<WriteFile> getWriteFiles() {
        return writeFiles;
    }

    public void setWriteFiles(List<WriteFile> writeFiles) {
        this.writeFiles = writeFiles;
    }
}
