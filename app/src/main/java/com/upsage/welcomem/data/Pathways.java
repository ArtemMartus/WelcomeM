package com.upsage.welcomem.data;

import android.content.Context;
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

    public Pathways(Integer courierId, OnItemClick onItemClick, OnStartDragListener dragListener,
                    Context context) {
        this.courierId = courierId;
        this.context = context;
        this.onItemClick = onItemClick;
        mDragStartListener = dragListener;
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            entries.addAll((List<PathwayEntry>) o);
            notifyDataSetChanged();
        }

        if (receiver != null)
            receiver.onTaskCompleted(null);

        if (entries.size() == 0 && context != null) {
            Toast.makeText(context, R.string.seemsYouveDoneYourWorkForTodayString, Toast.LENGTH_SHORT).show();
        }
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
        return true;
    }

}
