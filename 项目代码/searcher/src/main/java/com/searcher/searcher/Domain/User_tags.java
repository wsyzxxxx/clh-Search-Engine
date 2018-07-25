package com.searcher.searcher.Domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User_tags {
    @EmbeddedId
    private user_tag_id user_tag;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public user_tag_id getUser_tag() {
        return user_tag;
    }

    public void setUser_tag(user_tag_id user_tag) {
        this.user_tag = user_tag;
    }
}