package fr.nocloud.maestro.model.catalog;

import java.util.List;

public class Action {

    private String command;

    private List<String> parameters;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
