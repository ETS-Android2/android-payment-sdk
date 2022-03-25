package kh.com.ipay88.sdk.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * IPay88's Payment Response Parameter
 */
public class IPay88PayResponse implements Serializable {
	private String merchantCode;
	private int paymentID;
	private String refNo;
	private String amount;
	private String currency;
	private String remark;
	private String transID;
	private String authCode;
	private String status;
	private String errDesc;
	private String signature;

	@JsonProperty("MerchantCode")
	public String getMerchantCode() { return merchantCode; }
	@JsonProperty("MerchantCode")
	public void setMerchantCode(String value) { this.merchantCode = value; }

	@JsonProperty("PaymentId")
	public int getPaymentID() { return paymentID; }
	@JsonProperty("PaymentId")
	public void setPaymentID(int value) { this.paymentID = value; }

	@JsonProperty("RefNo")
	public String getRefNo() { return refNo; }
	@JsonProperty("RefNo")
	public void setRefNo(String value) { this.refNo = value; }

	@JsonProperty("Amount")
	public String getAmount() { return amount.replace(",", ""); }
	@JsonProperty("Amount")
	public void setAmount(String value) { this.amount = value; }

	@JsonProperty("Currency")
	public String getCurrency() { return currency; }
	@JsonProperty("Currency")
	public void setCurrency(String value) { this.currency = value; }

	@JsonProperty("Remark")
	public String getRemark() { return remark; }
	@JsonProperty("Remark")
	public void setRemark(String value) { this.remark = value; }

	@JsonProperty("TransId")
	public String getTransID() { return transID; }
	@JsonProperty("TransId")
	public void setTransID(String value) { this.transID = value; }

	@JsonProperty("AuthCode")
	public String getAuthCode() { return authCode; }
	@JsonProperty("AuthCode")
	public void setAuthCode(String value) { this.authCode = value; }

	@JsonProperty("Status")
	public String getStatus() { return status; }
	@JsonProperty("Status")
	public void setStatus(String value) { this.status = value; }

	@JsonProperty("ErrDesc")
	public String getErrDesc() { return errDesc; }
	@JsonProperty("ErrDesc")
	public void setErrDesc(String value) { this.errDesc = value; }

	@JsonProperty("Signature")
	public String getSignature() { return signature; }
	@JsonProperty("Signature")
	public void setSignature(String value) { this.signature = value; }

	/**
	 * Generate JSON String from Payment Response Object
	 * @param data
	 * @param isPretty
	 * @return
	 */
	public static String GenerateJSONData(IPay88PayResponse data, boolean isPretty) {
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
	 * Generate Payment Response Object from JSON
	 * @param json
	 * @return
	 */
	public static IPay88PayResponse GenerateObjectFromJSONData(String json) {
		IPay88PayResponse data = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			data = objectMapper.readValue(json, IPay88PayResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}
