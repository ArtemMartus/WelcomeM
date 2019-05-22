package com.upsage.welcomem.data;

import com.upsage.welcomem.data.entries.OvertimeEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.OvertimesRetrieveTask;

import java.util.LinkedList;
import java.util.List;

public class OvertimeHistory implements OnTaskCompleted {
    private OnTaskCompleted receiver;
    private Integer courierId;
    private List<OvertimeEntry> overtimeList;

    public OvertimeHistory(Integer courierId) {
        this.courierId = courierId;
    }

    @SuppressWarnings("unchecked")
    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        OvertimesRetrieveTask task = new OvertimesRetrieveTask(this);
        task.execute(courierId);
    }

    public boolean ready() {
        return overtimeList != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            overtimeList = new LinkedList<>();
            overtimeList.addAll((List<OvertimeEntry>) o);
        }

        if (receiver != null)
            receiver.onTaskCompleted(o);
    }


    public String getMonthOvertime() {
        int totalMinutes = 0;
        for (OvertimeEntry entry : overtimeList)
            totalMinutes += entry.getOvertimingMinutes();
        int totalHours = totalMinutes / 60;
        totalMinutes -= totalHours * 60;
        return totalHours + ":" + totalMinutes;
    }
}
