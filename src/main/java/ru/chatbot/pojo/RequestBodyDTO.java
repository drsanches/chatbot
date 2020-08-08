package ru.chatbot.pojo;

public class RequestBodyDTO {

    private String type;
    private ObjectDTO object;
    private Integer group_id;
    private String event_id;

    public RequestBodyDTO() {}

    public String getType() {
        return type;
    }

    public ObjectDTO getObject() {
        return object;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    @Override
    public String toString() {
        return "RequestBodyDTO{" +
                "type='" + type + '\'' +
                ", object=" + object +
                ", group_id=" + group_id +
                ", event_id='" + event_id + '\'' +
                '}';
    }
}