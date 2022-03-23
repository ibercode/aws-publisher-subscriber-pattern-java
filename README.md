## Integrating microservices by using AWS serverless services
## Publisher/Subscriber pattern - Asynchronous Communication

This is a Java implementation of the Publisher/Subscriber Pattern as described in the official AWS documentation
https://docs.aws.amazon.com/prescriptive-guidance/latest/modernization-integrating-microservices/pub-sub.html

Topology
<img src="topology.png" alt="topology" width="100%"/>

## Description

The communication style in this pattern is asynchronous.
As you can see from the topology, the Lambda function is publishing messages to an SNS topic.
There are three subscriptions, two services and an email subscription, but you can also create other subscriptions using services like SQS or Kinesis Data Firehose.
This pattern is also known as Fanout pattern.
The Lambda functions will process the messages asynchronously and the first Lambda will not expect any response from the downstream processors. 

The SAM template contains all the information to deploy AWS resources and also the permission required by these service to communicate.

You will be able to create and delete the CloudFormation stack using AWS SAM.

This is fully functional example implemented in Java 11.

Important: this application uses various AWS services and there are costs associated with these services after the Free Tier usage - please see the AWS Pricing page for details. You are responsible for any AWS costs incurred.

## Language:
#### This is a Maven project which uses Java 11 and AWS SDK

## Framework
The framework used to deploy the infrastructure is SAM

## Services used

The AWS services used in this pattern are
#### API Gateway - AWS Lambda - DynamoDB - Amazon SNS

## Deployment commands

````
mvn clean package

# create an S3 bucket where the source code will be stored:
aws s3 mb s3://qsmklpcoiw3i20d92idnkqd

# upload the source code to S3:
aws s3 cp target/sourceCode.zip s3://qsmklpcoiw3i20d92idnkqd

# SAM will deploy the CloudFormation stack described in the template.yml file:
sam deploy --s3-bucket qsmklpcoiw3i20d92idnkqd --stack-name pub-sub-pattern --capabilities CAPABILITY_IAM

````

## Testing

```
# Subscribe to the SNS Topic to receive the confirmation email

# use aws cli to show the SNS Topics
aws sns list-topics

# subscribe to the SNS topic with your email address and confirm your subscription
aws sns subscribe \
    --topic-arn arn:aws:sns:eu-central-1:YOUR_AWS_ACCOUNT:Communication \
    --protocol email \
    --notification-endpoint YOUR_EMAIL@DOMAIN.com
    
# Copy the API Gateway URL for the Payments endpoint from the sam deploy outputs
#  i.e. 
# Outputs                                                                                                                                                                                                                             
# -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Key                 PaymentsEndpoint                                                                                                                                                                                                      3
# Description         API Gateway Payments Endpoint                                                                                                                                                 
# Value               https://______________/prod/payments


# Call the API Gateway 

curl -X POST https://API_GATEWAY_URL -H "Content-Type: application/json" -d '{"customerId": "111-222","customerFullName":"John Smith","customerEmail": "john.smith@example.com","amount": "99.99","product": "car insurance"}' 

# you should receive an email with the information
# and also see in the console the correlationId
# i.e. "040ca0f3-0cdb-4c0d-9377-6bb3edd9308f"

# Scan the DynamoDB CustomerPayments 
aws dynamodb scan --table-name CustomerPayments

# Scan the DynamoDB Sales 
aws dynamodb scan --table-name Sales    
```

## Cleanup

Run the given command to delete the resources that were created. It might take some time for the CloudFormation stack to get deleted.
```
aws cloudformation delete-stack --stack-name pub-sub-pattern

aws s3 rm s3://qsmklpcoiw3i20d92idnkqd --recursive

aws s3 rb s3://qsmklpcoiw3i20d92idnkqd
```

## Requirements

* [Create an AWS account](https://portal.aws.amazon.com/gp/aws/developer/registration/index.html) if you do not already have one and log in. The IAM user that you use must have sufficient permissions to make necessary AWS service calls and manage AWS resources.
* [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html) installed and configured
* [Git Installed](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* [AWS Serverless Application Model](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) (AWS SAM) installed


## Author
Razvan Minciuna
https://www.linkedin.com/in/razvanminciuna/


