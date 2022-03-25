package kh.com.ipay88.sdk.models;

import static java.lang.String.valueOf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import kh.com.ipay88.sdk.utils.IPay88Signature;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * IPay88's Payment Request Parameter
 */
public class IPay88PayRequest implements Serializable {
    private String merchantCode;
    @JsonIgnore
    private String merchantKey;
    private int paymentID;
    private String refNo;
    private String amount;
    private String currency;
    private String prodDesc;
    private String userName;
    private String userEmail;
    private String userContact;
    private String remark;
    private String signature;
    private String backendURL;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    @JsonIgnore
    private Environment environment = Environment.SANDBOX;
    private final String paymentUrl = "/ePayment/entry.asp";
    private final String deeplinkListUrl = "/ePayment/WebService/MobileSDK/api/deeplinkList";

    /**
     * IPay88's Environment Constant
     */
    public enum Environment {
        DVL("https://dvl-kh.ipay88.co.id"),
        SANDBOX("https://sandbox.ipay88.com.kh"),
        PRODUCTION("https://payment.ipay88.com.kh");

        String baseUrl;
        Environment(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    public enum Currency {
        KHR("KHR"),
        USD("USD");

        String label;
        Currency(String label) {
            this.label = label;
        }
    }

    /**
     * Init payment request object
     */
    public IPay88PayRequest() {
    }

    /**
     * Init payment request object with parameter
     * @param environment
     * @param merchantCode
     * @param merchantKey
     * @param paymentID
     * @param refNo
     * @param amount
     * @param currency
     * @param prodDesc
     * @param userName
     * @param userEmail
     * @param userContact
     * @param remark
     * @param backendURL
     */
    public IPay88PayRequest(Environment environment, String merchantCode, String merchantKey, int paymentID, String refNo, String amount, Currency currency, String prodDesc, String userName, String userEmail, String userContact, String remark, String backendURL) {
        this.environment = environment;
        this.merchantCode = merchantCode;
        this.merchantKey = merchantKey;
        this.paymentID = paymentID;
        this.refNo = refNo;
        this.amount = amount;
        this.currency = currency.label;
        this.prodDesc = prodDesc;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.remark = remark;
        this.backendURL = backendURL;
    }

    /**
     * Adjust Data Request Fields
     * @return
     */
    public IPay88PayRequest adjust() {
        this.backendURL = this.getBackendURL();
        this.generateSignature();
        return this;
    }

    /**
     * Get Payment Environment URL
     * @return
     */
    @JsonIgnore
    public String getPaymentUrl() {
        String baseUrl = null;
        if (this.environment != null) {
            baseUrl = this.environment.baseUrl + this.paymentUrl;
        }
        return baseUrl;
    }

    /**
     * Get Deeplink WebService Environment URL
     * @return
     */
    @JsonIgnore
    public String getDeeplinkListUrl() {
        String baseUrl = null;
        if (this.environment != null) {
            baseUrl = this.environment.baseUrl + this.deeplinkListUrl;
        }
        return baseUrl;
    }

    @JsonIgnore
    public Environment getEnvironment() {
        return environment;
    }

    @JsonIgnore
    public void setEnvironment(Environment value) {
        this.environment = value;
    }

    @JsonProperty("MerchantCode")
    public String getMerchantCode() {
        return merchantCode;
    }

    @JsonProperty("MerchantCode")
    public void setMerchantCode(String value) {
        this.merchantCode = value;
    }

    @JsonIgnore
    public String getMerchantKey() {
        return merchantKey;
    }

    @JsonIgnore
    public void setMerchantKey(String value) {
        this.merchantKey = value;
    }

    @JsonProperty("PaymentId")
    public int getPaymentID() {
        return paymentID;
    }

    @JsonProperty("PaymentId")
    public void setPaymentID(int value) {
        this.paymentID = value;
    }

    @JsonProperty("RefNo")
    public String getRefNo() {
        return refNo;
    }

    @JsonProperty("RefNo")
    public void setRefNo(String value) {
        this.refNo = value;
    }

    @JsonProperty("Amount")
    public String getAmount() {
        return amount.replace(",", "");
    }

    @JsonProperty("Amount")
    public void setAmount(String value) {
        this.amount = value;
    }

    @JsonIgnore
    public String getAmountFormat() {
        return df.format(Double.parseDouble(this.getAmount()));
    }

    @JsonIgnore
    public String getAmountHash() {
        return df.format(Double.parseDouble(this.getAmount())).replace(".", "");
    }

    @JsonProperty("Currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("Currency")
    public void setCurrency(Currency currency) {
        this.currency = currency.label;
    }

    @JsonProperty("ProdDesc")
    public String getProdDesc() {
        return prodDesc;
    }

    @JsonProperty("ProdDesc")
    public void setProdDesc(String value) {
        this.prodDesc = value;
    }

    @JsonProperty("UserName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("UserName")
    public void setUserName(String value) {
        this.userName = value;
    }

    @JsonProperty("UserEmail")
    public String getUserEmail() {
        return userEmail;
    }

    @JsonProperty("UserEmail")
    public void setUserEmail(String value) {
        this.userEmail = value;
    }

    @JsonProperty("UserContact")
    public String getUserContact() {
        return userContact;
    }

    @JsonProperty("UserContact")
    public void setUserContact(String value) {
        this.userContact = value;
    }

    @JsonProperty("Remark")
    public String getRemark() {
        return remark;
    }

    @JsonProperty("Remark")
    public void setRemark(String value) {
        this.remark = value;
    }

    @JsonProperty("Signature")
    public String getSignature() {
        return signature;
    }

    @JsonProperty("Signature")
    public void setSignature(String value) {
        this.signature = value;
    }

    @JsonProperty("BackendURL")
    public void setBackendURL(String value) {
        this.backendURL = validateBackendURL(value);
    }

    @JsonProperty("BackendURL")
    public String getBackendURL() {
        return validateBackendURL(this.backendURL);
    }

    /**
     * Get Default Backend URL in case Merchant doesn't set its value
     * @param value
     * @return
     */
    private String validateBackendURL(String value) {
        if (value == null || value.equals("")) {
            String defaultUrl = "/ePayment/Mobile/SDKResponsePostback.asp";
            if (this.environment != null) {
                value = this.environment.baseUrl + defaultUrl;
            }
        }
        return value;
    }

    /**
     * Generate Payment Request Signature
     */
    private void generateSignature() {
        String amountHash = this.getAmountHash();
        String key = this.merchantKey + this.merchantCode + this.refNo + amountHash + this.currency;
        String hash = IPay88Signature.SHA1((key));
        this.signature = hash;
    }

    /**
     * Generate okhttp3's RequestBody
     * @return
     */
    public RequestBody generateFormBody() {
        RequestBody formBody = new FormBody.Builder()
                .add("MerchantCode", this.merchantCode)
                .add("PaymentId", this.paymentID == 0 ? "" : valueOf(this.paymentID))
                .add("RefNo", this.refNo)
                .add("Amount", this.getAmountFormat())
                .add("Currency", this.currency)
                .add("ProdDesc", this.prodDesc)
                .add("UserName", this.userName)
                .add("UserEmail", this.userEmail)
                .add("UserContact", this.userContact)
                .add("Remark", this.remark)
                .add("Signature", this.signature)
                .add("BackendURL", this.getBackendURL())
                .build();
        return formBody;
    }

    /**
     * Safe URL Encode
     * @param value
     * @return
     */
    private String urlEncode(String value) {
        String result = "";
        try {
            result = value != null ? URLEncoder.encode(value, "iso-8859-1") : "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Generate JSON String from Payment Request Object
     * @param data
     * @return
     */
    public static String GenerateJSONData(IPay88PayRequest data) {
        String json = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Generate Payment Request Object from JSON
     * @param json
     * @return
     */
    public static IPay88PayRequest GenerateObjectFromJSONData(String json) {
        IPay88PayRequest data = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            data = objectMapper.readValue(json, IPay88PayRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}