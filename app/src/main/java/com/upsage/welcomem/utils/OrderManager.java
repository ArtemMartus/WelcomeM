package com.upsage.welcomem.utils;

import android.content.SharedPreferences;

import com.upsage.welcomem.interfaces.HasId;

import java.util.ArrayList;
import java.util.List;

public class OrderManager<Entry extends HasId> {
    private final SharedPreferences preferences;
    private List<Integer> rightOrder = new ArrayList<>();

    public OrderManager(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    // we order entries by their IDs
    public void parseOrderString(String str) {
        for (String part : str.split(" ")) {
            try {
                Integer integer = Integer.parseInt(part);
                addToOrder(integer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void checkPreferences(String name, List<Entry> entries) {
        String str = preferences.getString(name, "");
        if (str != null && !str.isEmpty()) {
            parseOrderString(str);
        } else {
            initOrder(entries);
            saveOrder(name);
        }
        makeOrder(entries);
    }

    public void initOrder(List<Entry> entries) {
        rightOrder.clear();
        for (Entry entry : entries)
            addToOrder(entry.getId());
    }

    public void makeOrder(List<Entry> entries) {
        List<Entry> ordered = new ArrayList<>();
        for (Integer id : rightOrder) {
            for (Entry entry : entries) {
                if (entry.getId().equals(id)) {
                    ordered.add(entry);
                    break;
                }
            }
        }

        if (entries.size() > ordered.size()) {
            for (Entry entry : entries) {
                if (rightOrder.contains(entry.getId())
                        || ordered.contains(entry))
                    continue;
                ordered.add(entry);
            }
        }

        entries.clear();
        entries.addAll(ordered);
    }

    public void saveOrder(String name) {
        StringBuilder str = new StringBuilder();
        for (Integer integer : rightOrder) {
            str.append(integer).append(" ");
        }
        preferences.edit().putString(name, str.toString()).apply();
    }

    public void addToOrder(Integer i) {
        if (!rightOrder.contains(i))
            rightOrder.add(i);
    }
}
