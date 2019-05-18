package com.upsage.welcomem.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.upsage.welcomem.interfaces.DatabasePojo;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.ClientRetrieveTask;

public class Client implements DatabasePojo {
    private Integer id = -1;
    private String name = "";
    private String surname = "";
    private String address = "";
    private String email = "";
    private String telNumber = "";
    private Double balance = -1.0;
    private OnTaskCompleted receiver;


    public Client(Integer id) {
        this.id = id;
    }

    public Client(Integer id, String name, String surname, String address, String email, String telNumber, Double balance) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.telNumber = telNumber;
        this.balance = balance;
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof Client)) {
            Client client = (Client) o;
            copy(client);
        }

        if (receiver != null)
            if (id != -1) {
                save();
                receiver.onTaskCompleted(o);

            } else
                receiver.onTaskCompleted(null);
    }

    @Override
    public boolean load(Context context) {
        if (context == null) {
            Log.e("Client load()", "Can't load it because context is empty");
            return false;
        }
        if (id == -1) {
            Log.e("Client load()", "Can't load order with invalid id");
            return false;
        }
        /*SharedPreferences orderPreferences = context.getSharedPreferences("client_" + id, 0);
        name = orderPreferences.getString("name", "");
        surname = orderPreferences.getString("surname", "");
        address = orderPreferences.getString("address", "");
        telNumber = orderPreferences.getString("telNumber", "");
        email = orderPreferences.getString("email", "");
        balance = (double) orderPreferences.getFloat("balance", -1.0f);

        Log.d("Client load()", "Downloading client data asynchronously");

        if (orderPreferences.getInt("id", -1) == -1)
            test((OnTaskCompleted) context);
        else
            test(null);
*/
        return ready();
    }

    @Override
    public void save() {
        if (receiver == null) {
            Log.e("Client save()", "Can't save it because receiver is empty so no context");
            return;
        }
        if (id == -1) {
            Log.e("Client save()", "Can't save order with invalid id");
            return;
        }
        Log.d("Client save()", "Saving order to client" + id);
        Context context = (Context) receiver;
        SharedPreferences orderPreferences = context.getSharedPreferences("client_" + id, 0);

        orderPreferences.edit()
                .putInt("id", id)
                .putString("name", name)
                .putString("surname", surname)
                .putString("address", address)
                .putString("telNumber", telNumber)
                .putString("email", email)
                .putFloat("balance", balance.floatValue())
                .apply();
    }

    @Override
    public void copy(Object c) {
        Client o = (Client) c;
        id = o.id;
        name = o.name;
        surname = o.surname;
        address = o.address;
        telNumber = o.telNumber;
        email = o.email;
        balance = o.balance;
    }

    public boolean ready() {
        return !name.isEmpty() && !telNumber.isEmpty();
    }

    @Override
    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        ClientRetrieveTask task = new ClientRetrieveTask(this);
        task.execute(this);

    }

    public String toString() {
        return name + " " + surname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
