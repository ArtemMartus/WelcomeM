package com.upsage.welcomem.data.entries;

import java.sql.Timestamp;
import java.util.Calendar;

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
        return id != null && id > 0 && startTime != null && startTime.getTime() > 0;
    }

    public int overtimeMinutes() {
        return workMinutes() - 9 * 60;
    }

    public int workMinutes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartTime());
        int start_minute = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60;
        calendar.setTime(getEndTime());
        int end_minute = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60;


        return end_minute - start_minute;
    }
}

