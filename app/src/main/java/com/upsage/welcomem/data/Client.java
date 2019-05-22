package com.upsage.welcomem.data;

import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.ClientRetrieveTask;

public class Client implements OnTaskCompleted {
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
                receiver.onTaskCompleted(o);

            } else
                receiver.onTaskCompleted(null);
    }

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
