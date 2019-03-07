package com.andy.domain;

import java.util.List;

public class GetM100DataResponse {
    private String service;//接口代码
    private String sessionId;//会话Id
    private String errorCode;//错误码
    private String errorMsg;//错误消息
    private String summary;//摘要

    private List<M100DataObject> dataPoints;    //数据列表

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<M100DataObject> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<M100DataObject> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
