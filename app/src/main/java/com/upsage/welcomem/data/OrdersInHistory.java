package com.upsage.welcomem.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.entries.OrderInHistoryEntry;
import com.upsage.welcomem.data.viewholders.OrderInHistoryViewHolder;
import com.upsage.welcomem.interfaces.ItemTouchHelperAdapter;
import com.upsage.welcomem.interfaces.OnItemClick;
import com.upsage.welcomem.interfaces.OnStartDragListener;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.OrderHistoryRetrieveTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersInHistory extends RecyclerView.Adapter<OrderInHistoryViewHolder> implements
        OnTaskCompleted, ItemTouchHelperAdapter {
    private final OnStartDragListener mDragStartListener;
    private Integer employeeId;
    private OnTaskCompleted receiver;
    private OnItemClick onItemClick;
    private List<OrderInHistoryEntry> entries = new ArrayList<>();
    private Context context;


    public OrdersInHistory(Integer employeeId, OnItemClick onItemClick, OnStartDragListener dragListener) {
        this.employeeId = employeeId;
        this.onItemClick = onItemClick;
        mDragStartListener = dragListener;
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            entries.addAll((List<OrderInHistoryEntry>) o);
            notifyDataSetChanged();
        }

        if (receiver != null)
            receiver.onTaskCompleted(null);
    }

    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        OrderHistoryRetrieveTask task = new OrderHistoryRetrieveTask(this);
        task.execute(employeeId);
    }


    public boolean ready() {
        for (OrderInHistoryEntry item :
                entries)
            if (item != null && !item.ready())
                return false;

        return true;
    }


    @NonNull
    @Override
    public OrderInHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null)
            context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflatedView = inflater.inflate(R.layout.order_item_in_history_list, parent, false);
        OrderInHistoryViewHolder holder = new OrderInHistoryViewHolder(inflatedView);
        if (onItemClick != null)
            inflatedView.setOnClickListener(v -> onItemClick.OnItemClick(v, holder.getAdapterPosition()));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderInHistoryViewHolder holder, int position) {
        OrderInHistoryEntry entry = entries.get(position);
        String orderIdString = context.getString(R.string.orderIdString) + entry.getId();
        @SuppressLint("SimpleDateFormat")
        String deliveryDateString = new SimpleDateFormat("dd/MM/yyyy HH:ss").format(entry.getDeliveryDate());
        holder.setData(orderIdString, entry.getClientName(), deliveryDateString);

    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public OrderInHistoryEntry getItem(int position) {
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

    @Override
    public void onItemDismiss(int position) {
        entries.remove(position);
        notifyItemRemoved(position);
    }
}
