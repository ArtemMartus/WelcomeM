package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.Client;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRetrieveTask extends AsyncTask<Client, Void, Client> {

    private final static String TAG = "Client retriever task";
    private OnTaskCompleted receiver;

    public ClientRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Client doInBackground(Client... args) {
        Client client = null;
        if (args.length > 0 && args[0] != null) {
            Integer clientId = args[0].getId(); // Получаем ID клиента из аргумента
            try {

                // формируем sql запрос используя созданное соединение
                PreparedStatement statement = SQLSingleton.prepareStatement
                        ("SELECT * from clients where id =?");

                // устанавливаем вместо ? ID клиента
                statement.setInt(1, clientId);
                //получаем ответ
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // объект client идет в функцию onPostExecute как аргумент
        return client;
    }

    @Override
    protected void onPostExecute(Client clientData) {
        if (receiver != null) { // а здесь мы уже проверим есть ли получатель-функция (callback)
            Log.i(TAG, "Client we got be like: " + clientData);
            receiver.onTaskCompleted(clientData); // если есть - вызываем
        }
        super.onPostExecute(clientData);
    }
}
