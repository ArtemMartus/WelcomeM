package com.upsage.welcomem.data.entries;

import com.upsage.welcomem.interfaces.HasId;

import java.util.Objects;

public class PathwayEntry implements HasId {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathwayEntry entry = (PathwayEntry) o;
        return id.equals(entry.id) &&
                address.equals(entry.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }
}
