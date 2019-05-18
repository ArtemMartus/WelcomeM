package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.WorkHoursEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class WorkHoursRetrieveTask extends AsyncTask<Integer, Void, List<WorkHoursEntry>> {

    private final static String host = "jdbc:mysql://remotemysql.com:3306/XN5vmpIBV5";
    private final static String db_user = "XN5vmpIBV5";
    private final static String db_password = "3KW9meu7IL";
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
                Connection connection = DriverManager.getConnection(host, db_user, db_password);
                Log.i(TAG, "connection successful");
                PreparedStatement statement = connection.prepareStatement
                        ("SELECT * from work_reports where employee_id =? order by start_time desc");
                Log.i(TAG, "created statement");
                statement.setInt(1, employeeId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    do {
                        WorkHoursEntry entry = new WorkHoursEntry(
                                resultSet.getInt("id"),
                                resultSet.getTimestamp("start_time"),
                                resultSet.getTimestamp("end_time")
                        );
                        entries.add(entry);
                    } while (resultSet.next());
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entries;
    }


    @Override
    protected void onPostExecute(List<WorkHoursEntry> entries) {
        if (receiver != null) {
            Log.i(TAG, "Client we got be like: " + entries);
            receiver.onTaskCompleted(entries);
        }
        super.onPostExecute(entries);
    }
}
