package com.ibercode.customer.utils;

import com.ibercode.customer.model.PaymentCustomer;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.UUID;

public class CustomerDDBUtils {

    private final DynamoDbClient ddb = DynamoDbClient.builder()
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
            .build();
    private final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    public String putItem(PaymentCustomer paymentCustomer, String tableName){

        DynamoDbTable<PaymentCustomer> mappedTable = enhancedClient
                .table(tableName, TableSchema.fromBean(PaymentCustomer.class));

        String paymentId = UUID.randomUUID().toString();
        paymentCustomer.setPaymentId(paymentId);

        mappedTable.putItem(paymentCustomer);

        return paymentId;
    }
}
