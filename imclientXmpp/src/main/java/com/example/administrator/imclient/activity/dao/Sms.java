package com.example.administrator.imclient.activity.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SMS.
 */
public class Sms {

    private Long id;
    private String from_id;
    private String from_nick;
    private Integer from_avatar;
    private String body;
    private String type;
    private Long time;
    private String status;
    private String unread;
    private String session_id;
    private String session_name;

    public Sms() {
    }

    public Sms(Long id) {
        this.id = id;
    }

    public Sms(Long id, String from_id, String from_nick, Integer from_avatar, String body, String type, Long time, String status, String unread, String session_id, String session_name) {
        this.id = id;
        this.from_id = from_id;
        this.from_nick = from_nick;
        this.from_avatar = from_avatar;
        this.body = body;
        this.type = type;
        this.time = time;
        this.status = status;
        this.unread = unread;
        this.session_id = session_id;
        this.session_name = session_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getFrom_nick() {
        return from_nick;
    }

    public void setFrom_nick(String from_nick) {
        this.from_nick = from_nick;
    }

    public Integer getFrom_avatar() {
        return from_avatar;
    }

    public void setFrom_avatar(Integer from_avatar) {
        this.from_avatar = from_avatar;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

}
