package ru.ivan.sqlcmd.model.entity;

public final class Description {

    private final String command;
    private final String description;

    public Description(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Description{" +
                "command='" + command + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
