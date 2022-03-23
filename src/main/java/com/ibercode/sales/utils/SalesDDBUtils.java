package com.ibercode.sales.utils;

import com.ibercode.sales.model.PaymentSales;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.UUID;

public class SalesDDBUtils {

    private final DynamoDbClient ddb = DynamoDbClient.builder()
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
            .build();
    private final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    public String putItem(PaymentSales paymentSales, String tableName){

        DynamoDbTable<PaymentSales> mappedTable = enhancedClient
                .table(tableName, TableSchema.fromBean(PaymentSales.class));

        String saleId = UUID.randomUUID().toString();
        paymentSales.setSaleId(saleId);

        mappedTable.putItem(paymentSales);

        return saleId;
    }
}
