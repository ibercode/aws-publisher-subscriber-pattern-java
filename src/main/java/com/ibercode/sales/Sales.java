package com.ibercode.sales;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import com.ibercode.sales.model.PaymentSales;
import com.ibercode.sales.utils.SalesDDBUtils;

public class Sales implements RequestHandler<SNSEvent, Object> {

    private final Gson GSON = new Gson();
    private final SalesDDBUtils ddbUtils = new SalesDDBUtils();

    @Override
    public Object handleRequest(SNSEvent snsEvent, Context context) {

        String TABLE_NAME = System.getenv("SALES_TABLE");

        snsEvent.getRecords()
                .forEach(record -> {
                    PaymentSales paymentSales = GSON.fromJson(record.getSNS().getMessage(), PaymentSales.class);
                    ddbUtils.putItem(paymentSales, TABLE_NAME);
                });

        return "Done";
    }
}