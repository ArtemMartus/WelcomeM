package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.entries.OrderInHistoryEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class OrderHistoryRetrieveTask extends AsyncTask<Integer, Void, List<OrderInHistoryEntry>> {

    private final static String host = "jdbc:mysql://remotemysql.com:3306/XN5vmpIBV5";
    private final static String db_user = "XN5vmpIBV5";
    private final static String db_password = "3KW9meu7IL";
    private final static String TAG = "Get Order History Task";
    private OnTaskCompleted receiver;

    public OrderHistoryRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected List<OrderInHistoryEntry> doInBackground(Integer... args) {
        List<OrderInHistoryEntry> orders = new LinkedList<>();
        if (args.length > 0 && args[0] != null) {
            Integer employeeId = args[0];
            try {
                Connection connection = DriverManager.getConnection(host, db_user, db_password);
                Log.i(TAG, "connection successful");
                PreparedStatement statement = connection.prepareStatement
                        ("SELECT id,client_id,delivery_date from orders where courier_id =? and delivery_date is not null order by delivery_date asc");
                Log.i(TAG, "created statement");
                statement.setInt(1, employeeId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    do {
                        int orderId = resultSet.getInt("id");
                        int clientId = resultSet.getInt("client_id");
                        Timestamp delivery = resultSet.getTimestamp("delivery_date");
                        String clientName = "";
                        //get client via another sql request
                        {
                            PreparedStatement clientStatement = connection.prepareStatement(
                                    "select name from clients where id = ?");
                            clientStatement.setInt(1, clientId);
                            ResultSet clientSet = clientStatement.executeQuery();
                            if (clientSet.first()) {
                                clientName = clientSet.getString("name");
                            } else {
                                Log.e(TAG, "Bad client_id in order " + orderId);
                                clientSet.close();
                                clientStatement.close();
                                continue;
                            }
                            clientSet.close();
                            clientStatement.close();
                        }
                        OrderInHistoryEntry order = new OrderInHistoryEntry(
                                orderId,
                                clientId,
                                delivery,
                                employeeId,
                                clientName
                        );
                        orders.add(order);
                    } while (resultSet.next());
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                orders = null;
            }
        }
        return orders;
    }

    @Override
    protected void onPostExecute(List<OrderInHistoryEntry> orderData) {
        if (receiver != null) {
            Log.i(TAG, "Order in history we got be like: " + orderData);
            receiver.onTaskCompleted(orderData);
        }
        super.onPostExecute(orderData);
    }
}
