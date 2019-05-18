package com.upsage.welcomem.data.entries;

public class PathwayEntry {
    private Integer id = -1;
    private String address;

    public PathwayEntry(Integer id, String address) {
        this.id = id;
        this.address = address;
    }

    public boolean ready() {
        return id != -1 && !address.isEmpty();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
