# IPay88 Cambodia
We accept online payments from various methods, such as:
* Credit or Debit cards (Visa, Mastercards, JCB, Diners Club & UnionPay, etc.)
* e-Wallets (KHQR, Wing, PiPay, eMoney, Alipay, WeChat Pay, etc.)
* Online Banking (ACLEDA XPay, CAMPU Direct Debit, Chip Mong Pay, AMK Online Card, Prince Bank QR, etc.)
* [Appendix I (1. PaymentId)](#1-paymentid)
* [Demo App](#4-demo)

# Mobile Payment SDK - Android
[![](https://jitpack.io/v/ipay88-kh/android-sdk.svg)](https://jitpack.io/#ipay88-kh/android-sdk)

## 1. SDK Payment Flow
1. Your application initializes the library.
2. After buyers complete their payments, the library returns a callback to your application with the status of the payment and the transaction id.
3. After the library flow is complete, an activity result will be posted to be received by your application.

## 2. Version Supports
No            |   Date            |    By         |   Version         |   Supports    |   Features
------------- | :---------------: | ------------- | :---------------: | ------------- | -------------               
1             | 2022-Mar-06       | kuntola       |   1.0.0           | minSdk 21 --> targetSdk 31  | - Redirect Payment

# Table of Contents
## 1. Requirements
1. Please share your AppId inside module build.gradle to IPay88 team:
```gradle
android {
    defaultConfig {
        applicationId "xxx.xxx.xxx"
    }
}
```
2. Then IPay88 team will setup your AppId into IPay88 system to allow you to use IPay88 Mobile Payment channel. 
3. And then IPay88 team will share you these credentials:
    + Merchant Code (KHxxxxxx)
    + Merchant Key  (XXxxxxxx)
    + ClientAppSecret (IPAY88-xxxxxxxxxxxxxxxxxxxxxxxxx). Please refer to [2.2 ClientAppSecret inside AndroidManifest.xml](#22-clientappsecret-inside-androidmanifestxml)


## 2. Setup
### 2.1 Permissions inside AndroidManifest.xml
```xml
<manifest>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/> 
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
</manifest>
```

### 2.2 ClientAppSecret inside AndroidManifest.xml
```xml
<manifest>
    <application>
        <meta-data
            android:name="kh.com.ipay88.sdk.ClientAppSecret"
            android:value="IPAY88-xxxxxxxxxxxxxxxxxxxxxxxxx" />
    </application>
</manifest>
```

### 2.3 Dependencies (To get a Git project into your build)
#### Step 1. Add the JitPack repository to your build file
+ Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
+ If the above setting doesn't work, add it in your settings.gradle at the end of repositories:
```gradle
dependencyResolutionManagement {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### Step 2. Add the IPay88 Payment SDK dependency
+ Add it in your module build.gradle inside dependencies:
```gradle
dependencies {
    implementation 'com.github.ipay88-kh:android-payment-sdk:1.0.0'
}
```

## 3. Payment Request & Response
### 3.1 Payment Request
#### 3.1.1 Payment Request Object
```java
public class kh.com.ipay88.sdk.models.IPay88PayRequest
```
#### 3.1.2 Payment Request Methods
Method Name   |  Required         |   Description
------------- | :---------------: | -------------
setEnvironment(Environment value)   |              |   Swtich between SANDBOX (default) & PRODUCION environment. Please refer to [3.1.3 Environment](#313-environment)
setMerchantCode(String value)       |   &#10003;   |   Merchant Code that provided by IPay88, E.g. KH00001
setMerchantKey(String value)        |   &#10003;   |   Merchant Key that provided by IPay88.
setPaymentID(int value)             |   &#10003;   |   PaymentId is the value to request payment method to appear on IPay88 landing page. Please refer to [Appendix I (1. PaymentId)](#1-paymentid)
setRefNo(String value)              |   &#10003;   |   Reference number for merchant reference purposes, should be unique for each transaction.
setAmount(String value)             |   &#10003;   |   Final Amount to pay and in 2 decimal point, E.g. 10.90
setCurrency(Currency currency)      |   &#10003;   |   Currency of transaction. Please refer to [3.1.4 Currency](#314-currency)
setProdDesc(String value)           |   &#10003;   |   Simple Product Description. Note: Special characters is not allowed.
setUserName(String value)           |   &#10003;   |   Customer name in merchant's system. E.g. John Woo
setUserEmail(String value)          |   &#10003;   |   Customer email address in merchant's system with valid email format, E.g. johnwoo@yahoo.com
setUserContact(String value)        |   &#10003;   |   Customer contact number in merchant's system, E.g. 60123436789
setRemark(String value)             |              |   Remark for particular transaction. Note: Special characters is not allowed.
setBackendURL(String value)         |   &#10003;   |   Specify a valid merchant callback URL when payment success. E.g. http://www.myshop.com/backend_page.php
#### 3.1.3 Environment
```java
public enum Environment {
    SANDBOX,
    PRODUCTION
}
```
#### 3.1.4 Currency
```java
public enum Currency {
    KHR,
    USD
}
```

### 3.2 Payment Response
#### 3.2.1 Payment Response Object
```java
public class kh.com.ipay88.sdk.models.IPay88PayResponse
```
#### 3.2.2 Payment Response Methods
Method Name   |  Required         |   Description
------------- | :---------------: | -------------
String getMerchantCode()            |   &#10003;   |   Merchant Code provided by iPay88 and use to uniquely identify the Merchant. E.g. KH00001
int getPaymentID()                  |   &#10003;   |   Please refer to [Appendix I (1. PaymentId)](#1-paymentid) for possible PaymentId value return to BackendPostURL.
String getRefNo()                   |   &#10003;   |   The request Reference number for merchant reference purposes, should be unique for each transaction.
String getAmount()                  |   &#10003;   |   Payment amount with two decimals
String getCurrency()                |   &#10003;   |   Currency code that based on standard ISO (KHR or USD)
String getRemark()                  |              |   Remark for particular transaction.
String getTransID()                 |   &#10003;   |   iPay88 Transaction ID. E.g. T019988877700
String getAuthCode()                |              |   Bank reference number. Note: Sometime bank may not return reference number to gateway.
String getStatus()                  |   &#10003;   |   Use to indicate payment status “1” – Success OR “0” – Fail
String getErrDesc()                 |              |   Payment status description. Please refer to [Appendix I (3. Error Description)](#3-error-description)
String getSignature()               |   &#10003;   |   SHA1 signature. Please refer to [5. Signature Response](#5-signature-response)


## 4. How to Make Payment
### 4.1 Create Payment Request Object
```java
// MARK: - Import SDK to your activity
import kh.com.ipay88.sdk.constants.IPay88Constants;
import kh.com.ipay88.sdk.models.IPay88PayRequest;
import kh.com.ipay88.sdk.models.IPay88PayResponse;
import kh.com.ipay88.sdk.utils.IPay88SDK;

// MARK: - Create 2 static variables to receive IPay88 payment response
static IPay88PayResponse payResponse;
static String errorMsg;

TextView tvStatus;

// MARK: - IPay88 Checkout
IPay88PayRequest payRequest = new IPay88PayRequest();
payRequest.setEnvironment(IPay88PayRequest.Environment.SANDBOX);
payRequest.setMerchantCode("KH00001");
payRequest.setMerchantKey("Apple88KEY");
payRequest.setPaymentID(1);
payRequest.setRefNo("ORD1188");
payRequest.setAmount("1.00");
payRequest.setCurrency(IPay88PayRequest.Currency.USD);
payRequest.setProdDesc("Top Up (SDK)");
payRequest.setUserName("Tola KUN");
payRequest.setUserEmail("tola.kun@ipay88.com.kh");
payRequest.setUserContact("017847800");
payRequest.setRemark("aOS");
payRequest.setBackendURL("http://www.myshop.com/backend_page.php");
```

### 4.2 Create Payment Callback
+ Create a Java class and implement these 2 interfaces:
```java
public interface kh.com.ipay88.sdk.utils.IPay88Callback
public interface java.io.Serializable
```
+ For Example:
```java
public class MainActivityCallback implements IPay88Callback, Serializable {
    @Override
    public void onResponse(IPay88PayResponse payResponse) {
        MainActivity.payResponse = payResponse;
    }

    @Override
    public void onFailure(String message) {
        MainActivity.errorMsg = message;
    }
}
```
### 4.3 Invoke Checkout Function
```java
IPay88SDK.checkout(MainActivity.this, payRequest, new MainActivityCallback());
```

### 4.4 Handle Payment Response Object
+ Need to override 
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
}
```
+ For Example:
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // MARK: - IPay88 Payment Response
    if (requestCode == IPay88Constants.PAY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
            && MainActivity.payResponse != null) {
        
        String status = MainActivity.payResponse.getStatus().equals("1") ? "SUCCESS" : "FAILED";
        String json = IPay88PayResponse.GenerateJSONData(MainActivity.payResponse, true);
        tvStatus.setText(status + "\n" + json);

    } else if (requestCode == IPay88Constants.PAY_REQUEST_CODE
            && resultCode == Activity.RESULT_CANCELED
            && data != null
            && MainActivity.errorMsg != null) {
        
        tvStatus.setText(MainActivity.errorMsg);

    }
}
```

## 5. Signature Response
+ If the Merchant request is successful the response message will contain as SHA1 hashed signature. 
+ The hash signature for the response is a hash of the following fields:
    ```
    1. MerchantKey (Provided by iPay88 and share between iPay88 and merchant only)
    2. MerchantCode
    3. PaymentId
    4. RefNo
    5. Amount
    6. Currency
    7. Status
    ```         
+ The fields must be set in the following order, (MerchantKey & MerchantCode & PaymentId & RefNo & Amount & Currency & Status)
+ For Example:
    ```
    MerchantKey = "Apple88KEY"
    MerchantCode = "KH00001"
    PaymentId = "1"
    RefNo = "ORD1188"
    Amount = "1.00" (Note: Remove the “.” and “,” in the string before hash)
    Currency = "KHR"
    Status = "1"
    ```
         
    - The hash would be calculated on the following string:
    ```
    Apple88KEYKH00001ORD1188100KHR1
    ```
    - The resulting has signature value equals to (using SHA1 algorithm)
    ```
    KiCecyU86ZOFk15jXwOAvHdw/1M=
    ```
+ Test URL: <a href="https://payment.ipay88.com.kh/epayment/testing/TestSignature_response.asp" target="_blank">https://payment.ipay88.com.kh/epayment/testing/TestSignature_response.asp</a>


## Appendix I
### 1. PaymentId
+ Set PaymentId = 0, it will show all registered payment methods to the customer.
#### 1.1 Credit & Debit Card
Payment Method Name |  PaymentId (USD)| Logo
-------------       | :-------------: | :-------------:
Credit Card         |   1             |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/CC.svg" />
UnionPay            |   15            |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/MPGS_UPI.svg" />
#### 1.2 eWallet
Payment Method Name |  PaymentId (USD)|  PaymentId (KHR)| Logo
-------------       | :-------------: | :-------------: | :-------------:
eMoney              |   9             |     10          |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/EW_EMONEY.svg" />
Pi Pay              |   11            |                 |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/EW_PIPAY.svg" />
Alipay              |   233           |                 |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/QR_ALIPAY.svg" />
Wing                |   235           |     236         |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/EW_ONLINEEWING.svg" />
WeChat Pay          |   240           |                 |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/QR_WECHAT.svg" />
KHQR                |   248           |     249         |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/QR_KHQR.svg" />
#### 1.3 Online Banking
Payment Method Name |  PaymentId (USD)|  PaymentId (KHR)| Logo
-------------       | :-------------: | :-------------: | :-------------:
Acleda XPAY         |   3             |     4           |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/OB_ACLEDAXPAY.svg" />
Chip Mong Pay       |   238           |     239         |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/OB_CHIPMONG.svg" />
Campu Direct Debit  |   242           |     243         |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/OB_CAMPUDIRECTDEBIT.svg" />
AMK Online Card     |   246           |     247         |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/OB_AMKCARD.svg" />
Prince Bank QR      |   251           |     252         |   <img width="35" src="https://payment.ipay88.com.kh/PG/assets/images/bank/QR_PRINCE_BANK.svg" />
### 2. Currency
Currency Code   |  Description
-------------   | -------------
KHR | Cambodia Riel
USD | US Dollar
### 3. Error Description
Message         |  Description
-------------   | -------------
Duplicate reference number  |   Reference number must be unique for each transaction.
Invalid merchant            |   The merchant code does not exist.
Invalid parameters          |   Some parameter posted to iPay88 is invalid or empty.
Overlimit per transaction   |   You exceed the amount value per transaction.
Payment not allowed         |   The Payment method you requested is not allowed for this merchant code, please contact iPay88 Support to verify what payment method available for the merchant account.
Permission not allow        |   Your AppId or the shared credentials is not match with the information registered in iPay88 merchant account. Please contact IPay88 team.
### 4. Demo
[IPAY88SDK-Demo.apk](/app/IPAY88SDK-Demo.apk)