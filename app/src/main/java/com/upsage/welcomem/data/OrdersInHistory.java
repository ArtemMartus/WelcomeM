package com.upsage.welcomem.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.OrderHistoryRetrieveTask;
import com.upsage.welcomem.utils.OrderManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.upsage.welcomem.activities.ShowOrderActivity.getDate;

public class OrdersInHistory extends RecyclerView.Adapter<OrderInHistoryViewHolder> implements
        OnTaskCompleted, ItemTouchHelperAdapter {
    private Integer employeeId;
    private OnTaskCompleted receiver;
    private OnItemClick onItemClick;
    private List<OrderInHistoryEntry> entries = new ArrayList<>();
    private Context context;
    private final static String NAME = "history";
    private final static String DELETED = "deleted";
    private OrderManager orderManager;
    private List<Integer> deletedItems = new LinkedList<>();
    private SharedPreferences preferences;


    public OrdersInHistory(Integer employeeId, OnItemClick onItemClick,
                           Context context) {
        this.employeeId = employeeId;
        this.onItemClick = onItemClick;
        preferences = context.getSharedPreferences("history_order", 0);
        orderManager = new OrderManager(preferences);
        parseDeletedString();
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            List<OrderInHistoryEntry> loaded = ((List<OrderInHistoryEntry>) o);

            for (OrderInHistoryEntry entry : loaded)
                if (!isDeleted(entry.getId()))
                    entries.add(entry);

            orderManager.checkPreferences(NAME, entries);

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
        String deliveryDateString = getDate("dd/MM/yyyy HH:ss",entry.getDeliveryDate());
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
        orderManager.initOrder(entries);
        orderManager.saveOrder(NAME);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        addToDeleted(getItem(position).getId());
        saveDeleted();

        entries.remove(position);
        notifyItemRemoved(position);
        orderManager.initOrder(entries);
        orderManager.saveOrder(NAME);
    }


    /// REMOVED items list section

    void parseDeletedString() {
        String str = preferences.getString(DELETED, "");
        if (str == null || str.isEmpty())
            return;
        for (String part : str.split(" ")) {
            try {
                Integer integer = Integer.parseInt(part);
                addToDeleted(integer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean isDeleted(Integer id) {
        return deletedItems.contains(id);
    }

    void saveDeleted() {
        StringBuilder str = new StringBuilder();
        for (Integer integer : deletedItems) {
            str.append(integer).append(" ");
        }
        preferences.edit().putString(DELETED, str.toString()).apply();
    }

    void addToDeleted(Integer i) {
        if (!deletedItems.contains(i))
            deletedItems.add(i);
    }
}
