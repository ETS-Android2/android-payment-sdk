package kh.com.ipay88.sdk.models;

/*
 * IPay88PayRequest
 * IPay88Sdk
 *
 * Created by kunTola on 13/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import static java.lang.String.valueOf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kh.com.ipay88.sdk.utils.IPay88Signature;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * IPay88's Payment Request Parameter
 */
public class IPay88PayRequest implements Serializable {
    private String merchantCode;
    @JsonIgnore
    private String merchantKey = "";
    private int paymentId = 0;
    private String refNo = "";
    private double amount = 0.00;
    private Currency currency = Currency.USD;
    private String prodDesc = "";
    private String userName = "";
    private String userEmail = "";
    private String userContact = "";
    private String remark;
    private String signature;
    private String backendURL;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    @JsonIgnore
    private Environment environment = Environment.SANDBOX;
    private final String paymentUrl = "/ePayment/entry.asp";
    private final String deeplinkListUrl = "/ePayment/WebService/MobileSDK/api/deeplinkList";

    // MARK: - For Cancel Payment
    private String eId;
    private String errDesc;

    /**
     * IPay88's Environment Constant
     */
    public enum Environment {
        DVL("https://dvl-kh.ipay88.co.id"),
        SANDBOX("https://sandbox.ipay88.com.kh"),
        PRODUCTION("https://payment.ipay88.com.kh");

        String rawValue;
        Environment(String rawValue) {
            this.rawValue = rawValue;
        }
    }

    public enum Currency {
        KHR("KHR"),
        USD("USD");

        String rawValue;
        Currency(String rawValue) {
            this.rawValue = rawValue;
        }
    }

    /**
     * Init payment request object
     */
    public IPay88PayRequest() {
    }

    /**
     * Init payment request object with parameter
     *
     * @param environment
     * @param merchantCode
     * @param merchantKey
     * @param paymentId
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
    public IPay88PayRequest(Environment environment, String merchantCode, String merchantKey, int paymentId, String refNo, double amount, Currency currency, String prodDesc, String userName, String userEmail, String userContact, String remark, String backendURL) {
        this.environment = environment;
        this.merchantCode = merchantCode;
        this.merchantKey = merchantKey;
        this.paymentId = paymentId;
        this.refNo = refNo;
        this.amount = amount;
        this.currency = currency;
        this.prodDesc = prodDesc;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.remark = remark;
        this.backendURL = backendURL;
    }

    /**
     * Adjust Data Request Fields
     *
     * @return
     */
    @JsonIgnore
    public IPay88PayRequest adjust() {
        this.backendURL = this.getBackendURL();
        this.generateSignature();
        return this;
    }

    /**
     * Get Payment Environment URL
     *
     * @return
     */
    @JsonIgnore
    public String getPaymentUrl() {
        return this.environment.rawValue + this.paymentUrl;
    }

    /**
     * Get Deeplink WebService Environment URL
     *
     * @return
     */
    @JsonIgnore
    public String getDeeplinkListUrl() {
        return this.environment.rawValue + this.deeplinkListUrl;
    }

    @JsonIgnore
    public String getQueryUrl() {
        return this.environment.rawValue + "/ePayment/WebService/MobileSDK/api/query";
    }

    @JsonIgnore
    public String getCancelUrl() {
        return this.environment.rawValue + "/PG/PaymentResponse/PaymentCancel?" + this.getFormBodyCancel();
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
    public int getPaymentId() {
        return paymentId;
    }

    @JsonProperty("PaymentId")
    public void setPaymentId(int value) {
        this.paymentId = value;
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
    public double getAmount() {
        return amount;
    }

    @JsonProperty("Amount")
    public void setAmount(double value) {
        this.amount = value;
    }

    @JsonIgnore
    public String getAmountFormat() {
        return df.format(this.amount);
    }

    @JsonIgnore
    public String getAmountHash() {
        return df.format(this.amount).replace(".", "");
    }

    @JsonProperty("Currency")
    public Currency getCurrency() {
        return currency;
    }

    @JsonProperty("Currency")
    public void setCurrency(Currency currency) {
        this.currency = currency;
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
    private void setSignature(String value) {
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

    @JsonIgnore
    public void setEId(String eId) {
        this.eId = eId;
    }

    @JsonIgnore
    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    /**
     * Get Default Backend URL in case Merchant doesn't set its value
     *
     * @param value
     * @return
     */
    @JsonIgnore
    private String validateBackendURL(String value) {
        if (value == null || value.equals("")) {
            String defaultUrl = "/ePayment/Mobile/SDKResponsePostback.asp";
            if (this.environment != null) {
                value = this.environment.rawValue + defaultUrl;
            }
        }
        return value;
    }

    /**
     * Generate Payment Request Signature
     */
    @JsonIgnore
    private void generateSignature() {
        String amountHash = this.getAmountHash();
        String key = this.merchantKey + this.merchantCode + this.refNo + amountHash + this.currency.rawValue;
        this.signature = IPay88Signature.SHA1(key);
    }

    public String getSignature(int paymentId, int status)  {
        String amountHash = this.getAmountHash();
        String key = merchantKey + this.merchantCode + paymentId + this.refNo + amountHash + this.currency.rawValue + status;
        return IPay88Signature.SHA1(key);
    }

    /**
     * Generate okhttp3's RequestBody
     *
     * @return
     */
    @JsonIgnore
    public RequestBody getFormBody() {
        RequestBody formBody = new FormBody.Builder()
                .add("MerchantCode", this.merchantCode)
                .add("PaymentId", this.paymentId == 0 ? "" : valueOf(this.paymentId))
                .add("RefNo", this.refNo)
                .add("Amount", this.getAmountFormat())
                .add("Currency", this.currency.rawValue)
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

    @JsonIgnore
    public RequestBody getFormBodyQuery() {
        RequestBody formBody = new FormBody.Builder()
                .add("RefNo", this.refNo)
                .add("Amount", this.getAmountFormat())
                .add("Signature", this.signature)
                .build();
        return formBody;
    }

    @JsonIgnore
    private String getFormBodyCancel() {
        return "EID=" + this.eId + "&ErrDesc=" + this.errDesc;
    }

    /**
     * Safe URL Encode
     *
     * @param value
     * @return
     */
    @JsonIgnore
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
     *
     * @param data
     * @return
     */
    @JsonIgnore
    public static String GenerateJSONData(IPay88PayRequest data, boolean isPretty) {
        String json = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            json = isPretty ? objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data) : objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Generate Payment Request Object from JSON
     *
     * @param json
     * @return
     */
    @JsonIgnore
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

    /**
     * Validate Payment Request
     *
     * @return errorMessage else null
     */
    @JsonIgnore
    public String validate() {
        if (this.merchantCode == null || this.merchantCode.isEmpty())
            return "Invalid field named [MerchantCode].";

        if (this.merchantKey == null || this.merchantKey.isEmpty())
            return "Invalid field named [MerchantKey].";

        if (this.refNo == null || this.refNo.isEmpty())
            return "Invalid field named [RefNo].";

        if (this.currency == null)
            return "Invalid field named [Currency].";

        if (this.prodDesc == null || this.prodDesc.isEmpty())
            return "Invalid field named [ProdDesc].";

        if (this.userName == null || this.userName.isEmpty())
            return "Invalid field named [UserName].";

        if (this.userEmail == null || this.userEmail.isEmpty() || !this.isValidEmail(this.userEmail))
            return "Invalid field named [UserEmail].";

        if (this.userContact == null || this.userContact.isEmpty())
            return "Invalid field named [UserContact].";

        return null;
    }

    @JsonIgnore
    private boolean isValidEmail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        boolean isValid = matcher.matches();
        return isValid;
    }
}