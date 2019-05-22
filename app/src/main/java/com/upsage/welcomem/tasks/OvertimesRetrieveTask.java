package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.entries.OvertimeEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class OvertimesRetrieveTask extends AsyncTask<Integer, Void, List<OvertimeEntry>> {
    private final static String TAG = "Overtime retriever task";
    private OnTaskCompleted receiver;

    public OvertimesRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected List<OvertimeEntry> doInBackground(Integer... args) {
        List<OvertimeEntry> overtimeList = new LinkedList<>();
        if (args.length > 0 && args[0] != null) {
            Integer courierId = args[0];
            try {

                PreparedStatement statement = SQLSingleton.prepareStatement
                        ("SELECT * from overtime_history where courier_id =? and overtiming_minutes > 0 order by finish_time asc limit 60");

                statement.setInt(1, courierId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    do {
                        OvertimeEntry entry = new OvertimeEntry(
                                resultSet.getInt("id"),
                                resultSet.getTimestamp("finish_time"),
                                resultSet.getInt("overtiming_minutes")
                        );
                        overtimeList.add(entry);
                    } while (resultSet.next());
                }
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return overtimeList;
    }

    @Override
    protected void onPostExecute(List<OvertimeEntry> overtimes) {
        if (receiver != null) {
            Log.i(TAG, "OvertimeEntry we got be like: " + overtimes);
            receiver.onTaskCompleted(overtimes);
        }
        super.onPostExecute(overtimes);
    }
}
