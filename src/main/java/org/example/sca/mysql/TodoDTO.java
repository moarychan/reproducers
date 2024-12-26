package org.example.sca.mysql;

public class TodoDTO {

    private Integer id;

    private String description;

    private String details;

    private boolean done;

    public TodoDTO(String description, String details, boolean done) {
        this.description = description;
        this.details = details;
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "TodoDTO{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", details='" + details + '\'' +
            ", done=" + done +
            '}';
    }
}
