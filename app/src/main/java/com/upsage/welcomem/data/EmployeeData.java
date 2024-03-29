package com.upsage.welcomem.data;

import android.content.SharedPreferences;

import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.EmployeeTestTask;

public class EmployeeData implements OnTaskCompleted {
    private String login = "";
    private String password = "";
    private Boolean remember = false;
    private String name = "";
    private String surname = "";
    private Integer id = -1;
    private String address = "";
    private String email = "";
    private String telNumber = "";
    private OnTaskCompleted receiver;

    public EmployeeData(Integer id) {
        this.id = id;
    }

    public EmployeeData(SharedPreferences userPreferences) {
        login = userPreferences.getString("login", "");
        password = userPreferences.getString("password", "");
        name = userPreferences.getString("name", "");
        surname = userPreferences.getString("surname", "");
        address = userPreferences.getString("address", "");
        email = userPreferences.getString("email", "");
        telNumber = userPreferences.getString("telNumber", "");

        remember = userPreferences.getBoolean("remember", true);
        id = userPreferences.getInt("id", -1);
    }

    public EmployeeData(String login, String password, Boolean remember, String name, String surname, Integer id, String address, String email, String telNumber) {
        this.login = login;
        this.password = password;
        this.remember = remember;
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.address = address;
        this.email = email;
        this.telNumber = telNumber;
    }

    public boolean isNotEmpty() {
        return !password.isEmpty()
                && !login.isEmpty();
    }

    public boolean ready() {
        return !name.isEmpty() && !telNumber.isEmpty();
    }

    private void copy(EmployeeData data) {
        login = data.login;
        password = data.password;
        remember = data.remember;
        name = data.name;
        surname = data.surname;
        id = data.id;
        address = data.address;
        email = data.email;
        telNumber = data.telNumber;
    }

    public void save(SharedPreferences userPreferences) {

        userPreferences.edit()
                .putInt("id", id)
                .putBoolean("remember", remember)
                .putString("name", name)
                .putString("login", login)
                .putString("surname", surname)
                .putString("address", address)
                .putString("email", email)
                .putString("telNumber", telNumber)
                .apply();
        if (remember) {
            userPreferences.edit()
                    .putString("password", password)
                    .apply();
        }
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof EmployeeData)) {
            EmployeeData employeeData = (EmployeeData) o;
            copy(employeeData);
        }

        if (receiver != null)
            if (id != -1)
                receiver.onTaskCompleted(o);
            else
                receiver.onTaskCompleted(null);
    }

    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        EmployeeTestTask task = new EmployeeTestTask(this);
        task.execute(this);
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRemember() {
        return remember;
    }

    public void setRemember(Boolean remember) {
        this.remember = remember;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelNumber() {
        return telNumber;
    }

    @Override
    public String toString() {
        return "EmployeeData{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", remember=" + remember +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", id=" + id +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", receiver=" + receiver +
                '}';
    }
}
