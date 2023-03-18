package com.example.cityevents;

public class Event {
    private int id;
    private String name;
    private String date;
    private String time;
    private Double cost;
    private String length;
    private int user;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Event(int id, String name, String date, String time, Double cost, String length, int user) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.length = length;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", cost=" + cost +
                ", length='" + length + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
