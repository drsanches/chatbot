package ru.chatbot.pojo;

import java.util.List;

public class ObjectDTO {

    private Integer id;
    private Integer date;
    private Integer out;
    private Integer user_id;
    private Integer read_state;
    private String title;
    private String body;
    private List<Integer> owner_ids;

    public ObjectDTO() {}

    public Integer getId() {
        return id;
    }

    public Integer getDate() {
        return date;
    }

    public Integer getOut() {
        return out;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Integer getRead_state() {
        return read_state;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public List<Integer> getOwner_ids() {
        return owner_ids;
    }

    @Override
    public String toString() {
        return "ObjectDTO{" +
                "id=" + id +
                ", date=" + date +
                ", out=" + out +
                ", user_id=" + user_id +
                ", read_state=" + read_state +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", owner_ids=" + owner_ids +
                '}';
    }
}