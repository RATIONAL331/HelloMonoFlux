package com.example.Sec04.helper;

import com.example.util.Util;
import lombok.Data;

@Data
public class PurchaseOrder {
    private String item;
    private String price;
    private int userId;

    public PurchaseOrder(int userId) {
        this.item = Util.faker().commerce().productName();
        this.price = Util.faker().commerce().price();
        this.userId = userId;
    }
}
