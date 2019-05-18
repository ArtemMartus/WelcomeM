package com.upsage.welcomem.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.data.WorkHours;
import com.upsage.welcomem.data.entries.WorkHoursEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.ThemeUtil;

public class WorkHoursActivity extends AppCompatActivity implements OnTaskCompleted {

    WorkHours workHours;
    ListView listView;
    TextView titleTextView;

    //todo add overall month overtime textview
    //todo remake workHours system to one that counts overtime $(last_order_delivered) - 18:00

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_hours);

        listView = findViewById(R.id.workHoursListView);
        titleTextView = findViewById(R.id.workHoursTitleTextView);

        SharedPreferences userPreferences = getSharedPreferences("user", 0);
        EmployeeData employee = new EmployeeData(userPreferences);
        workHours = new WorkHours(employee.getId(), this);
        workHours.test(this);
        titleTextView.setText(R.string.loadingString);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            WorkHoursEntry item = workHours.getItem(position);
            if (item != null) {
                int minutes = item.workMinutes();
                int hours = minutes / 60;
                minutes -= hours * 60;
                Toast.makeText(this, getString(R.string.overallOvertimeForGivenDayString) +
                        hours + ":" + minutes, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onTaskCompleted(Object o) {
        if (workHours.ready()) {
            titleTextView.setText(R.string.workHoursString);
            listView.setAdapter(workHours);
        } else {
            titleTextView.setText(R.string.loadingString);//never called
        }
    }
}
