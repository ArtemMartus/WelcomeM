package com.upsage.welcomem.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.entries.WorkHoursEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.WorkHoursRetrieveTask;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.upsage.welcomem.activities.ShowOrderActivity.getDate;

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
            TextView finishedHourTV = convertView.findViewById(R.id.finishedHourTextView);
            TextView overtimeTV = convertView.findViewById(R.id.overtimeTextView);

            if (entry.getFinishTime() != null) {
                workDayTV.setText(getContext().getString(R.string.dayString) +
                        getDate("dd/MM/yyyy",entry.getFinishTime()));
                finishedHourTV.setText(getContext().getString(R.string.endString) +
                        getDate("HH:mm",entry.getFinishTime()));
            } else {
                workDayTV.setText(getContext().getString(R.string.dayString) + "-/-/-");
                finishedHourTV.setText(getContext().getString(R.string.endString) + "-:-");
            }


            int minutes = entry.getOvertiming();
            int hours = 0;
            if (minutes > 0) {
                hours = minutes / 60;
                minutes -= hours * 60;
            }
            overtimeTV.setText(hours + ":" + minutes);


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
