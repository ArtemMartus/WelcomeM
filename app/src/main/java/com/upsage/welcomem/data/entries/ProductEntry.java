package com.upsage.welcomem.data.entries;
import java.text.DecimalFormat;

public class ProductEntry {

    private Integer id;
    private String name;
    private String model;
    private String color;
    private Double price;
    private Double discount;

    public ProductEntry(Integer id, String name, String model, String color, Double price, Double discount) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.color = color;
        this.price = price;
        this.discount = discount;
    }


    public String asString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return name + "/" + model + "/" + color + " = " + df.format(getFinalPrice());
    }

    public Double getFinalPrice() {
        if (discount > 0 && discount < 100)
            return (price * discount / 100);
        else
            return (price);
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

    @Override
    public String toString() {
        return "ProductEntry{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }


}
