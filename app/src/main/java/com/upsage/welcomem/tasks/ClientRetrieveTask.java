package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.Client;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRetrieveTask extends AsyncTask<Client,Void,Client>  {
    private final static String host = "jdbc:mysql://remotemysql.com:3306/XN5vmpIBV5";
    private final static String db_user = "XN5vmpIBV5";
    private final static String db_password = "3KW9meu7IL";
    private final static String TAG = "Client retriever task";
    private OnTaskCompleted receiver;

    public ClientRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Client doInBackground(Client... args) {
        Client client = null;
        if (args.length > 0 && args[0] != null) {
            Client clientData = args[0];
            try {

//                Connection connection = DriverManager.getConnection(host, db_user, db_password);
                Log.i(TAG, "connection successful");
                PreparedStatement statement = SQLSingleton.prepareStatement
                        ("SELECT * from clients where id =?");
                Log.i(TAG, "created statement");
                statement.setInt(1, clientData.getId());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    client = new Client(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("address"),
                            resultSet.getString("email"),
                            resultSet.getString("tel_number"),
                            resultSet.getDouble("balance")
                    );
                }
                resultSet.close();
                statement.close();
//                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    @Override
    protected void onPostExecute(Client clientData) {
        if(receiver!=null){
            Log.i(TAG,"Client we got be like: "+clientData);
            receiver.onTaskCompleted(clientData);
        }
        super.onPostExecute(clientData);
    }
}
