package com.ibercode.customer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import com.ibercode.customer.model.PaymentCustomer;
import com.ibercode.customer.utils.CustomerDDBUtils;

public class Customer implements RequestHandler<SNSEvent, String> {

    private final Gson GSON = new Gson();
    private final CustomerDDBUtils ddbUtils = new CustomerDDBUtils();
    @Override
    public String handleRequest(SNSEvent snsEvent, Context context) {

        String TABLE_NAME = System.getenv("CUSTOMER_TABLE");

        snsEvent.getRecords()
                        .forEach(record -> {
                            PaymentCustomer paymentCustomer = GSON.fromJson(record.getSNS().getMessage(), PaymentCustomer.class);
                            ddbUtils.putItem(paymentCustomer, TABLE_NAME);
                        });

        return "Done";
    }
}
