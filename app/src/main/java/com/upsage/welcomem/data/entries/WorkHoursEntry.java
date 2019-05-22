package com.upsage.welcomem.data.entries;

import java.sql.Timestamp;

public class WorkHoursEntry {
    private Integer id;
    private Timestamp finishTime;
    private Integer overtiming;

    public WorkHoursEntry(Integer id, Timestamp finishTime, Integer overtiming) {
        this.id = id;
        this.finishTime = finishTime;
        this.overtiming = overtiming;
    }

    @Override
    public String toString() {
        return "WorkHoursEntry{" +
                "id=" + id +
                ", finishTime=" + finishTime +
                ", overtiming=" + overtiming +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public Integer getOvertiming() {
        return overtiming;
    }

    public boolean ready() {
        return id != null && id > 0 && finishTime != null && finishTime.getTime() > 0;
    }
}

