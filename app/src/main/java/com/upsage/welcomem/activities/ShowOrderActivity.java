package com.upsage.welcomem.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.Client;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.data.Order;
import com.upsage.welcomem.data.Products;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.PaymentTask;
import com.upsage.welcomem.utils.ThemeStyle;
import com.upsage.welcomem.utils.ThemeUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ShowOrderActivity extends AppCompatActivity implements OnTaskCompleted {
    private final static String TAG = "Show Order Activity";

    TextView clientNameTextView;
    TextView clientTelephoneTextView;
    TextView productsTextView;
    TextView managerNameTextView;
    TextView managerTelephoneTextView;
    TextView clientBalanceTextView;
    TextView recommendedPriceToPayTextView;
    //todo add editText in which courier can pass amount of money given by customer
    //  so his balance will be corrected according to (balance - (given money - recommended sum) )
    TextView orderPaidTextView;
    TextView orderAddressTextView;
    Button payOrderButton;
    LinearLayout orderLayout;

    Order order;
    Client client;
    EmployeeData manager;
    Products products;
    Double sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);

        clientNameTextView = findViewById(R.id.clientNameTextView);
        clientTelephoneTextView = findViewById(R.id.clientTelephoneTextView);
        productsTextView = findViewById(R.id.productsTextView);
        managerNameTextView = findViewById(R.id.managerNameTextView);
        managerTelephoneTextView = findViewById(R.id.managerTelephoneTextView);
        clientBalanceTextView = findViewById(R.id.clientBalanceTextView);
        recommendedPriceToPayTextView = findViewById(R.id.sumTextView);
        orderPaidTextView = findViewById(R.id.orderPaidTextView);
        orderAddressTextView = findViewById(R.id.orderAddressTextView);
        payOrderButton = findViewById(R.id.payOrderButton);
        orderLayout = findViewById(R.id.orderShowLinearLayout);

        payOrderButton.setOnClickListener(v -> {
            if (sum == null || sum <= 0 || client == null
                    || !client.ready() || order == null
                    || order.getId() == null || order.getClientId() == null
                    || !order.ready()) {
                Toast.makeText(this, R.string.waitUntilItLoadsString, Toast.LENGTH_SHORT).show();
                return;
            }
            payOrderButton.setEnabled(false);
            PaymentTask task = new PaymentTask(this);
            Toast.makeText(this, R.string.payInProcessString, Toast.LENGTH_SHORT).show();
            Double newBalance = client.getBalance() - sum;

            // todo make it also record the end of a day for courier if no more orders left

            task.execute(order.getId().doubleValue()
                    , order.getClientId().doubleValue()
                    , newBalance);
        });

        orderAddressTextView.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(order.getDeliveryAddress()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        });
        if (ThemeUtil.getStyle() == ThemeStyle.BlackNormal
                || ThemeUtil.getStyle() == ThemeStyle.BlackSmall
                || ThemeUtil.getStyle() == ThemeStyle.BlackLarge) {
            orderAddressTextView.setTextColor(Color.YELLOW);
        } else
            orderAddressTextView.setTextColor(Color.BLUE);

        int id = getIntent().getIntExtra("orderId", -1);
        if (id == -1) {
            Log.e(TAG, "No order id as input");
            finish();
            return;
        }
        order = new Order(id);
        if (!order.load(this))
            order.test(this);
        else {
            onTaskCompleted(order);
        }

    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onTaskCompleted(Object o) {
        boolean show_products = true;
        boolean show_manager = true;
        boolean show_client = true;

        if (o instanceof Integer) { /// this magic code is used by payButton to make it reload order payed date
            Integer integer = (Integer) o;
            if (integer == 3) {
                Toast.makeText(this, R.string.paymentMadeString, Toast.LENGTH_SHORT).show();
                order.setClientId(-1);
                clientNameTextView.setText(R.string.loadingString);
                clientBalanceTextView.setText(R.string.loadingString);
                client.setBalance(-1.0);
                client.setTelNumber("");
                client.test(this);
                order.test(this);
            }
        }
        if (order.getClientId().equals(order.getManagerId()) && order.getManagerId() == -1) {
            Toast.makeText(this, R.string.incorrectQRString, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (client == null) {
            client = new Client(order.getClientId());
            if (!client.load(this)) {
                client.test(this);
            }
        }

        if (manager == null) {
            manager = new EmployeeData(order.getManagerId());
            if (!manager.load(this)) {
                manager.test(this);
            }
        }

        if (products == null) {
            products = new Products(order.getProductsId());
            products.test(this);
        }

        if (!products.ready())
            show_products = false;
        if (!manager.ready())
            show_manager = false;
        if (!client.ready())
            show_client = false;


        if (order.getDeliveryDate() != null && payOrderButton != null && orderLayout != null) {
            orderLayout.removeView(payOrderButton);
            orderPaidTextView.setVisibility(View.VISIBLE);
            orderPaidTextView.setText(getString(R.string.deliveredString) +
                    new SimpleDateFormat("dd/MM/yyyy HH:ss").format(order.getDeliveryDate()));
        }


        if (show_client) {
            clientNameTextView.setText(getString(R.string.clientNameString) + client.toString());
            clientTelephoneTextView.setText(getString(R.string.clientTelephoneString) + client.getTelNumber());
            DecimalFormat df = new DecimalFormat("#.00");
            clientBalanceTextView.setText(getString(R.string.clientBalanceString) + df.format(client.getBalance()));
        } else {
            clientNameTextView.setText(R.string.loadingString);
            clientTelephoneTextView.setText(R.string.loadingString);
            clientBalanceTextView.setText(R.string.loadingString);
        }

        if (show_manager) {
            managerNameTextView.setText(getString(R.string.managerNameString) +
                    manager.getName() + " " + manager.getSurname());
            managerTelephoneTextView.setText(getString(R.string.managerTelephoneString) +
                    manager.getTelNumber());
        } else {
            managerNameTextView.setText(R.string.loadingString);
            managerTelephoneTextView.setText(R.string.loadingString);
        }

        if (show_products) {
            String productsInfo = products.getProductsInfo();
            sum = products.getPrice();
            productsTextView.setText(productsInfo);


            DecimalFormat df = new DecimalFormat("#.00");
            recommendedPriceToPayTextView.setText(getString(R.string.sumString) + df.format(sum) + order.getCurrency());

        } else {
            productsTextView.setText(R.string.loadingString);
            recommendedPriceToPayTextView.setText(R.string.loadingString);
        }
        orderAddressTextView.setText(getString(R.string.addressString) + order.getDeliveryAddress());
    }
}
