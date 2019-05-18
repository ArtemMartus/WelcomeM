package com.upsage.welcomem.data;

import java.sql.Timestamp;

public class WorkHoursEntry {
    private Integer id;
    private Timestamp startTime;
    private Timestamp endTime;

    public WorkHoursEntry(Integer id, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "WorkHoursEntry{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public boolean ready() {
        //todo
        return id != null && id > 0 && startTime != null && startTime.getTime() > 0;
    }
}

