# Fetch Rewards Coding Exercise - Backend Software Engineering

## Description
Build Receipt Processor Web Service. Please refer to the https://github.com/fetch-rewards/receipt-processor-challenge for more information

## Instruction
### Requirements
1. JDK 17 or later
2. Gradle 4+ or Maven 3.2+
3. Your favorite IDE (I prefer IntelliJ :P)
4. Postman to test the API (Optional)

### Steps

#### Setup and Run the application

###### IDE steps
1. Clone the repository and import the folder into your editor.
2. Clean and build the project.
3. Start the services by running [ReceiptProcessorApplication.java](src%2Fmain%2Fjava%2Fcom%2Ffetch%2Frewards%2Freceiptprocessor%2FReceiptProcessorApplication.java) through IDE.

###### CLI commands
1. Clone the repository
2. Go to the `receipt-processor` directory
3. Clean and build the project by executing `./gradlew clean build`
4. Run the application by executing `./gradlew bootRun`

#### Test the Receipt Processor API

##### Postman

Open the application

###### Post API

1. Set `Post` and put in the URL as ` http://localhost:8080/receipts/process`
2. Under the `Body` tab select `raw` and set the input type as `JSON` and fill in the input. Example

```
{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },{
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    },{
      "shortDescription": "Knorr Creamy Chicken",
      "price": "1.26"
    },{
      "shortDescription": "Doritos Nacho Cheese",
      "price": "3.35"
    },{
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      "price": "12.00"
    }
  ],
  "total": "35.35"
}
```

3. Hit the send button.

###### Get API
1. Set `Get` and put in the URL as ` http://localhost:8080/receipts/{id}/points`

Example : 
```
 http://localhost:8080/receipts/d3780576-6577-4bbe-b81f-6900ab855871/points
```

2. Hit the send button

##### CLI

###### Post API

You can run the following CURL command for

```
curl --location --request POST 'http://localhost:8080/receipts/process' \
--header 'Content-Type: application/json' \
--data-raw '{
  "retailer": "M&M Corner Market",
  "purchaseDate": "2022-03-20",
  "purchaseTime": "14:33",
  "items": [
    {
      "shortDescription": "Gatorade",
      "price": "2.25"
    },{
      "shortDescription": "Gatorade",
      "price": "2.25"
    },{
      "shortDescription": "Gatorade",
      "price": "2.25"
    },{
      "shortDescription": "Gatorade",
      "price": "2.25"
    }
  ],
  "total": "9.00"
}' 
```

The raw data is the JSON input.

###### Get API

You can run the following CURL command for

```
curl --location --request GET 'http://localhost:8080/receipts/{id}/points'
```











