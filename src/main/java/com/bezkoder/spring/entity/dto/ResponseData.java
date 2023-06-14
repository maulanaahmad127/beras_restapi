package com.bezkoder.spring.entity.dto;
import java.util.ArrayList;
import java.util.List;





public class ResponseData<T> {

    private Boolean status;
    private List<String> message = new ArrayList<>();
    private T payload;
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public List<String> getMessage() {
        return message;
    }
    public void setMessage(List<String> message) {
        this.message = message;
    }
    public T getPayload() {
        return payload;
    }
    public void setPayload(T payload) {
        this.payload = payload;
    }
    
    
}
