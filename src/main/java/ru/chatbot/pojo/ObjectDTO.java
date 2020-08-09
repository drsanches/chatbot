package ru.chatbot.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("date")
    private Integer date;

    @JsonProperty("out")
    private Integer out;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("read_state")
    private Integer readState;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("owner_ids")
    private List<Integer> ownerIds;

    public ObjectDTO() {}

    public ObjectDTO(Integer id, Integer date, Integer out, Integer userId, Integer readState, String title, String body, List<Integer> ownerIds) {
        this.id = id;
        this.date = date;
        this.out = out;
        this.userId = userId;
        this.readState = readState;
        this.title = title;
        this.body = body;
        this.ownerIds = ownerIds;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDate() {
        return date;
    }

    public Integer getOut() {
        return out;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getReadState() {
        return readState;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public List<Integer> getOwnerIds() {
        return ownerIds;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", date=" + date +
                ", out=" + out +
                ", user_id=" + userId +
                ", read_state=" + readState +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", owner_ids=" + ownerIds +
                '}';
    }
}