package com.upsage.welcomem.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.entries.PathwayEntry;
import com.upsage.welcomem.data.viewholders.PathwayEntryViewHolder;
import com.upsage.welcomem.interfaces.ItemDragHelperAdapter;
import com.upsage.welcomem.interfaces.OnItemClick;
import com.upsage.welcomem.interfaces.OnStartDragListener;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.PathwaysRetrieveTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pathways extends RecyclerView.Adapter<PathwayEntryViewHolder> implements
        OnTaskCompleted, ItemDragHelperAdapter {

    private final OnStartDragListener mDragStartListener;
    private final OnItemClick onItemClick;
    private final Context context;
    private OnTaskCompleted receiver;
    private List<PathwayEntry> entries = new ArrayList<>();
    private Integer courierId;
    private final SharedPreferences preferences;
    private List<Integer> rightOrder = new ArrayList<>();

    public Pathways(Integer courierId, OnItemClick onItemClick, OnStartDragListener dragListener,
                    Context context) {
        this.courierId = courierId;
        this.context = context;
        this.onItemClick = onItemClick;
        mDragStartListener = dragListener;
        preferences = context.getSharedPreferences("path_order", 0);
    }

    // we order entries by their IDs
    void parseOrderString(String str) {
        for (String part : str.split(" ")) {
            try {
                Integer integer = Integer.parseInt(part);
                addToOrder(integer);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    void initOrder() {
        rightOrder.clear();
        for (PathwayEntry entry : entries)
            addToOrder(entry.getId());
    }

    void makeOrder() {
        List<PathwayEntry> ordered = new ArrayList<>();
        for (Integer id : rightOrder) {
            for (PathwayEntry entry : entries) {
                if (entry.getId().equals(id)) {
                    ordered.add(entry);
                    break;
                }
            }
        }

        if (entries.size() > ordered.size()) {
            for (PathwayEntry entry : entries) {
                if (rightOrder.contains(entry.getId())
                        || ordered.contains(entry))
                    continue;
                ordered.add(entry);
            }
        }

        entries = ordered;
    }


    void saveOrder() {
        String str = "";
        for (Integer integer : rightOrder) {
            str += integer + " ";
        }
        preferences.edit().putString("order", str).apply();
    }

    private void addToOrder(Integer i) {
        if (!rightOrder.contains(i))
            rightOrder.add(i);
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            entries.addAll((List<PathwayEntry>) o);

            String str = preferences.getString("order", "");
            if (str != null && !str.isEmpty()) {
                parseOrderString(str);
            } else {
                initOrder();
                saveOrder();
            }
            makeOrder();

            notifyDataSetChanged();
        }

        if (receiver != null)
            receiver.onTaskCompleted(null);

        if (entries.size() == 0 && context != null) {
            Toast.makeText(context, R.string.seemsYouveDoneYourWorkForTodayString, Toast.LENGTH_SHORT).show();
        }
    }

    public void clear() {
        entries.clear();
        notifyDataSetChanged();
    }

    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        PathwaysRetrieveTask task = new PathwaysRetrieveTask(this);
        task.execute(courierId);
    }

    public boolean ready() {
        for (PathwayEntry item : entries)
            if (item != null && !item.ready())
                return false;

        return true;
    }

    @NonNull
    @Override
    public PathwayEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View inflatedView = inflater.inflate(R.layout.pathway_item, parent, false);
        PathwayEntryViewHolder holder = new PathwayEntryViewHolder(inflatedView);
        if (onItemClick != null)
            inflatedView.setOnClickListener(v -> onItemClick.OnItemClick(v, holder.getAdapterPosition()));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PathwayEntryViewHolder holder, int position) {
        PathwayEntry entry = entries.get(position);
        String orderIdString = context.getString(R.string.orderIdString) + entry.getId();
        holder.setData(orderIdString, entry.getAddress());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public PathwayEntry getItem(int position) {
        try {
            return entries.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(entries, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        initOrder();
        saveOrder();
        return true;
    }

}
