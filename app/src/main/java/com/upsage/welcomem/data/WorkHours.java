package com.upsage.welcomem.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.upsage.welcomem.R;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.WorkHoursRetrieveTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class WorkHours extends ArrayAdapter<WorkHoursEntry> implements OnTaskCompleted {

    private OnTaskCompleted receiver;
    private Integer employeeId;

    public WorkHours(Integer employeeId, Context context) {
        super(context, R.layout.work_hours_list_item);
        this.employeeId = employeeId;
    }

    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            addAll((List<WorkHoursEntry>) o);
            notifyDataSetChanged();
        }

        if (receiver != null)
            receiver.onTaskCompleted(null);
    }


    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        WorkHoursRetrieveTask task = new WorkHoursRetrieveTask(this);
        task.execute(employeeId);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WorkHoursEntry entry = getItem(position);
        if (entry != null && entry.ready()) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.work_hours_list_item, parent, false);
            }
            TextView workDayTV = convertView.findViewById(R.id.workDayTextView);
            TextView startHourTV = convertView.findViewById(R.id.startHourTextView);
            TextView endHourTV = convertView.findViewById(R.id.endHourTextView);
            TextView overtimeTV = convertView.findViewById(R.id.overtimeTtextView);

            if (entry.getStartTime() != null) {
                workDayTV.setText(getContext().getString(R.string.dayString) +
                        new SimpleDateFormat("dd/MM/yyyy").format(entry.getStartTime()));
                startHourTV.setText(getContext().getString(R.string.startString) +
                        new SimpleDateFormat("HH:mm").format(entry.getStartTime()));
            } else {
                workDayTV.setText(getContext().getString(R.string.dayString) + "-/-/-");
                startHourTV.setText(getContext().getString(R.string.startString) + "-:-");
            }
            if (entry.getEndTime() != null) {
                endHourTV.setText(getContext().getString(R.string.endString) +
                        new SimpleDateFormat("HH:mm").format(entry.getEndTime()));
            } else {
                endHourTV.setText(getContext().getString(R.string.endString) + "-:-");
            }
            if (entry.getEndTime() != null && entry.getStartTime() != null) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(entry.getStartTime());
                int start_hour = calendar.get(Calendar.HOUR_OF_DAY);
                int start_minute = calendar.get(Calendar.MINUTE);
                calendar.setTime(entry.getEndTime());
                int end_hour = calendar.get(Calendar.HOUR_OF_DAY);
                int end_minute = calendar.get(Calendar.MINUTE);

                int hours = end_hour - start_hour;
                int minutes = end_minute - start_minute;
                if (minutes < 0 && hours > 0) {
                    minutes = 60 - minutes;
                    --hours;
                }

                String str = hours + ":" + minutes;
                Log.d("Work hours log", "Difference between " + str);
                if (hours > 9 || (hours == 9 && minutes > 0)) {
                    str = (hours - 9) + ":" + minutes;
                    overtimeTV.setText(getContext().getString(R.string.overtimeString) +
                            str);
                } else {
                    hours = hours - 9;
                    if (hours < 0)
                        hours = -hours;
                    str = hours + ":" + minutes;
                    overtimeTV.setText(getContext().getString(R.string.shorttimeString) +
                            str);
                }
            }

            return convertView;
        } else
            return super.getView(position, convertView, parent);
    }

    public boolean ready() {
        for (int i = 0; i < getCount(); ++i) {
            WorkHoursEntry item = getItem(i);
            if (item != null && !item.ready())
                return false;
        }
        return true;
    }

}
