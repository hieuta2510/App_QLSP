package edu.ptit.saleapp.model;

public class Act{
    private int id;
    private String username, fullname, act;

    public Act() {
    }

    public Act(int id, String username, String fullname, String act) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.act = act;
    }

    public Act(String username, String fullname, String act) {
        this.username = username;
        this.fullname = fullname;
        this.act = act;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }
}