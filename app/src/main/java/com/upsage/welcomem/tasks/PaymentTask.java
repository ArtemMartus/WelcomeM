package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PaymentTask extends AsyncTask<Double, Void, Integer> {
    private final static String TAG = "Paths retriever task";
    private OnTaskCompleted receiver;

    public PaymentTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Integer doInBackground(Double... args) {
        if (args.length >= 3 // должно быть 3 аргумента
                && args[0] != null // проверим id заказа
                && args[1] != null // проверим id клиента
                && args[2] != null // проверим новый баланс
        ) {


            int orderId = args[0].intValue();
            int clientId = args[1].intValue();
            Double newBalance = args[2];


            try {

                PreparedStatement statement = SQLSingleton.prepareStatement
                        ("UPDATE `orders` SET `delivery_date` = now() WHERE (`id` = ?)");

                statement.setInt(1, orderId);
                statement.execute();
                statement.close();

                statement = SQLSingleton.prepareStatement(
                        "UPDATE `clients` SET `balance` = ? WHERE (`id` = ?)");
                statement.setDouble(1, newBalance);
                statement.setInt(2, clientId);
                statement.execute();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return SQLSingleton.ErrorCode;
            }
            return SQLSingleton.SuccessfulPaymentCode;
        }
        return SQLSingleton.ErrorCode; // на выход функция отправляет код
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
