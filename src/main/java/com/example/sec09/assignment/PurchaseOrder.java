package com.example.sec09.assignment;

import com.example.util.Util;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PurchaseOrder {
    private String item;
    private double price;
    private String category;

    public PurchaseOrder() {
        this.item = Util.faker().commerce().productName();
        this.price = Double.parseDouble(Util.faker().commerce().price());
        this.category = Util.faker().commerce().department();
    }
}
