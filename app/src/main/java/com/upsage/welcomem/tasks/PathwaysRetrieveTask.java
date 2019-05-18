package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.entries.PathwayEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PathwaysRetrieveTask extends AsyncTask<Integer, Void, List<PathwayEntry>> {
    private final static String TAG = "Paths retriever task";
    private OnTaskCompleted receiver;

    public PathwaysRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }


    @Override
    protected List<PathwayEntry> doInBackground(Integer... args) {
        List<PathwayEntry> orders = new LinkedList<>();
        if (args.length > 0 && args[0] != null) {
            Integer courierId = args[0];
            try {
                Log.i(TAG, "connection successful");
                PreparedStatement statement = SQLSingleton.prepareStatement
                        ("SELECT id,delivery_address from orders where courier_id =? and delivery_date is NULL");
                Log.i(TAG, "created statement");
                statement.setInt(1, courierId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    do {
                        PathwayEntry entry = new PathwayEntry(
                                resultSet.getInt("id"),
                                resultSet.getString("delivery_address")
                        );
                        orders.add(entry);
                    } while (resultSet.next());
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orders;
    }

    @Override
    protected void onPostExecute(List<PathwayEntry> paths) {
        if (receiver != null) {
            Log.i(TAG, "Paths we got be like: " + paths);
            receiver.onTaskCompleted(paths);
        }
        super.onPostExecute(paths);
    }

}
