package com.ibercode.payment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.ibercode.payment.model.CustomerPayment;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.util.UUID;

public class Payments implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String TOPIC_ARN = System.getenv("TOPIC_ARN");

    private final SnsClient snsClient = SnsClient.builder()
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
            .build();
    private final Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

        CustomerPayment customerPayment = gson.fromJson(event.getBody(),CustomerPayment.class);
        customerPayment.setCorrelationId(UUID.randomUUID().toString());

        publishMessage(gson.toJson(customerPayment));
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        response.setBody(gson.toJson(customerPayment.getCorrelationId()));

        return response;
    }

    private String publishMessage(String message){

        String response = "";

        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(TOPIC_ARN)
                    .build();
            PublishResponse result = snsClient.publish(request);
            response = result.messageId();

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return response;
    }
}
