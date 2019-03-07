package com.andy.domain;

public class M100DataObject {
    private String dataType;    //数据类型    String
    private String sendDateTime;    //发送时间    String
//    private M100DataObjectKV dataKV;    //数值对象    Object
    private String serviceNo;    //用户服务号    String
    private Integer userSeq;    //用户序号    Integer
    private String eqmtNo;    //设备号    String

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(String sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public Integer getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public String getEqmtNo() {
        return eqmtNo;
    }

    public void setEqmtNo(String eqmtNo) {
        this.eqmtNo = eqmtNo;
    }
}
