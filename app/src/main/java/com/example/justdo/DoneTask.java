package com.example.justdo;

public class DoneTask {
    String id, task , date;

    public DoneTask(String id, String task , String date) {
        this.id = id;
        this.task = task;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate(){
        return date;
    }
}

