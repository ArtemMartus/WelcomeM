package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.entries.WorkHoursEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class WorkHoursRetrieveTask extends AsyncTask<Integer, Void, List<WorkHoursEntry>> {

    private final static String TAG = "WorkHours Retrieve Task";
    private OnTaskCompleted receiver;

    public WorkHoursRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected List<WorkHoursEntry> doInBackground(Integer... args) {
        List<WorkHoursEntry> entries = new LinkedList<>();

        if (args.length > 0 && args[0] != null) {
            Integer employeeId = args[0];
            try {

                PreparedStatement statement = SQLSingleton.prepareStatement
                        ("SELECT * from overtime_history where courier_id =? order by finish_time desc");

                statement.setInt(1, employeeId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    do {
                        WorkHoursEntry entry = new WorkHoursEntry(
                                resultSet.getInt("id"),
                                resultSet.getTimestamp("finish_time"),
                                resultSet.getInt("overtiming_minutes")
                        );
                        entries.add(entry);
                    } while (resultSet.next());
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entries;
    }


    @Override
    protected void onPostExecute(List<WorkHoursEntry> entries) {
        if (receiver != null) {
            Log.i(TAG, "Work hours we got be like: " + entries);
            receiver.onTaskCompleted(entries);
        }
        super.onPostExecute(entries);
    }
}
