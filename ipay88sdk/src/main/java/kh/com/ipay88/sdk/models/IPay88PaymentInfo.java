package kh.com.ipay88.sdk.models;

/*
 * IPay88PaymentInfo
 * IPay88Sdk
 *
 * Created by kunTola on 13/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class IPay88PaymentInfo {
    private String eId;
    private String errDesc;

    @JsonProperty("EID")
    public String getEId() {
        return eId;
    }

    @JsonProperty("EID")
    public void setEId(String eId) {
        this.eId = eId;
    }

    @JsonProperty("ErrDesc")
    public String getErrDesc() {
        return errDesc;
    }

    @JsonProperty("ErrDesc")
    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}
