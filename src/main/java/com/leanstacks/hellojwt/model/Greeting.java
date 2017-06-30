package com.leanstacks.hellojwt.model;

import java.util.UUID;

public class Greeting {

    private String id;
    private String text;

    public Greeting() {
        this.id = UUID.randomUUID().toString();
    }

    public Greeting(final String text) {
        this();
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
