package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class RecordEndOfaDayTask extends AsyncTask<Integer, Void, Integer> {
    private final static String TAG = "End Of a Day task";
    private OnTaskCompleted receiver;

    public RecordEndOfaDayTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Integer doInBackground(Integer... args) {
        if (args.length > 0
                && args[0] != null
        ) {


            int courierId = args[0];


            try {
                Log.i(TAG, "connection successful");
                PreparedStatement statement = SQLSingleton.prepareStatement
                        ("INSERT INTO overtime_history " +
                                "(`courier_id`, `finish_time`, `overtiming_minutes`) " +
                                "VALUES (?, ?, ?)"); // VALUES ( parameterIndex:1, :2, :3)
                Log.i(TAG, "created statement");

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                statement.setTimestamp(2, timestamp); // :2

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timestamp);
                int currentMinutes = calendar.get(Calendar.MINUTE);
                int currentHours = calendar.get(Calendar.HOUR);
                currentHours -= 18; // 18 часов это конец рабочего дня, отняв от времени когда работник закончил 18 - получим время переработки

                if (currentHours > 0 || (currentHours == 0 && currentMinutes > 0)) {

                    currentMinutes += currentHours * 60;
                    statement.setInt(3, currentMinutes); // :3

                } else {
                    statement.setInt(3, 0);  //  :3
                }

                statement.setInt(1, courierId);  // :1
                statement.execute();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return SQLSingleton.ErrorCode;
            }
            return SQLSingleton.FinalRecordCode;
        }
        return SQLSingleton.ErrorCode;
    }

    @Override
    protected void onPostExecute(Integer ret) {
        if (receiver != null) {
            Log.i(TAG, "Integer we got be like: " + ret);
            receiver.onTaskCompleted(ret);
        }
        super.onPostExecute(ret);
    }
}
