package com.upsage.welcomem.data.entries;

import java.sql.Timestamp;

public class OvertimeEntry {
    private Integer id;
    private Timestamp finishTime;
    private Integer overtimingMinutes;

    public OvertimeEntry(Integer id, Timestamp finishTime, Integer overtimingMinutes) {
        this.id = id;
        this.finishTime = finishTime;
        this.overtimingMinutes = overtimingMinutes;
    }

    @Override
    public String toString() {
        return "OvertimeEntry{" +
                "id=" + id +
                ", finishTime=" + finishTime +
                ", overtimingMinutes=" + overtimingMinutes +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOvertimingMinutes() {
        return overtimingMinutes;
    }

}
