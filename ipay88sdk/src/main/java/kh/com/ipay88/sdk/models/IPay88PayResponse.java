package kh.com.ipay88.sdk.models;

/*
 * IPay88PayResponse
 * IPay88Sdk
 *
 * Created by kunTola on 13/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.text.DecimalFormat;

import kh.com.ipay88.sdk.utils.IPay88Signature;

/**
 * IPay88's Payment Response Parameter
 */
public class IPay88PayResponse implements Serializable {
	private String merchantCode;
	private int paymentId;
	private String refNo;
	private double amount;
	private String currency;
	private String remark;
	private String transID;
	private String authCode;
	private int status;
	private String errDesc;
	private String signature;

	private static final DecimalFormat df = new DecimalFormat("0.00");

	@JsonProperty("MerchantCode")
	public String getMerchantCode() { return merchantCode; }
	@JsonProperty("MerchantCode")
	public void setMerchantCode(String value) { this.merchantCode = value; }

	@JsonProperty("PaymentId")
	public int getPaymentId() { return paymentId; }
	@JsonProperty("PaymentId")
	public void setPaymentId(int value) { this.paymentId = value; }

	@JsonProperty("RefNo")
	public String getRefNo() { return refNo; }
	@JsonProperty("RefNo")
	public void setRefNo(String value) { this.refNo = value; }

	@JsonProperty("Amount")
	public double getAmount() { return amount; }
	@JsonProperty("Amount")
	public void setAmount(double value) { this.amount = value; }
	@JsonIgnore
	public String getAmountHash() {
		return df.format(this.amount).replace(".", "");
	}

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
	public int getStatus() { return status; }
	@JsonProperty("Status")
	public void setStatus(int value) { this.status = value; }

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
