package org.example.xmlapidemo.dto;


import jakarta.xml.bind.annotation.*;
import org.example.xmlapidemo.entity.User;

import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersResponse {

    @XmlElement(name = "user")
    private List<User> users;

    @XmlAttribute
    private int count;

    @XmlElement
    private String status;

    public UsersResponse() {
    }

    public UsersResponse(List<User> users) {
        this.users = users;
        this.count = users != null ? users.size() : 0;
        this.status = "success";
    }

    // Getters and Setters
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.count = users != null ? users.size() : 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
