package com.ibercode.sales.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class PaymentSales {

    private String saleId;
    private String customerId;
    private String customerFullName;
    private String customerEmail;
    private String amount;
    private String product;
    private String correlationId;


    public PaymentSales() {
    }

    public PaymentSales(String saleId, String customerId, String customerFullName, String customerEmail, String amount, String product, String correlationId) {
        this.saleId = saleId;
        this.customerId = customerId;
        this.customerFullName = customerFullName;
        this.customerEmail = customerEmail;
        this.amount = amount;
        this.product = product;
        this.correlationId = correlationId;
    }

    @DynamoDbPartitionKey
    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
