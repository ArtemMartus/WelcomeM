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

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getOvertiming() {
        return overtiming;
    }

    public void setOvertiming(Integer overtiming) {
        this.overtiming = overtiming;
    }

    public boolean ready() {
        return id != null && id > 0 && finishTime != null && finishTime.getTime() > 0;
    }

//    public int overtimeMinutes() {
//        return workMinutes() - 9 * 60;
//    }

//    public int workMinutes() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(getStartTime());
//        int start_minute = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60;
//        calendar.setTime(getEndTime());
//        int end_minute = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60;
//
//
//        return end_minute - start_minute;
//    }
}

