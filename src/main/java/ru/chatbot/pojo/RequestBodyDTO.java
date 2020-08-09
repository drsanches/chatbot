package ru.chatbot.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestBodyDTO {

    @JsonProperty("type")
    private String type;

    @JsonProperty("object")
    private ObjectDTO object;

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("event_id")
    private String eventId;

    public RequestBodyDTO() {}

    public RequestBodyDTO(String type, ObjectDTO object, Integer groupId, String eventId) {
        this.type = type;
        this.object = object;
        this.groupId = groupId;
        this.eventId = eventId;
    }

    public String getType() {
        return type;
    }

    public ObjectDTO getObject() {
        return object;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", object=" + object +
                ", group_id=" + groupId +
                ", event_id='" + eventId + '\'' +
                '}';
    }
}