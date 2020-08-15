package com.taxi24.service;


import com.taxi24.enums.ServiceNames;
import org.springframework.stereotype.Service;

@Service
public class RESTResponse {

    int resp_status = 200;
    String resp_message;
    ServiceNames rest_name;
    Object response = null;

    public RESTResponse() {
        super();
    }

    public RESTResponse(int resp_status, String resp_message, ServiceNames rest_name,
                        Object response) {
        this.resp_status = resp_status;
        this.resp_message = resp_message;
        this.rest_name = rest_name;
        this.response = response;
    }

    public int isResp_status() {
        return resp_status;
    }

    public void setResp_status(int resp_status) {
        this.resp_status = resp_status;
    }

    public String getResp_message() {
        return resp_message;
    }

    public void setResp_message(String resp_message) {
        this.resp_message = resp_message;
    }

    public ServiceNames getRest_name() {
        return rest_name;
    }

    public void setRest_name(ServiceNames rest_name) {
        this.rest_name = rest_name;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "RESTResponse [resp_status=" + resp_status + ", resp_message="
                + resp_message + ", rest_name=" + rest_name + ", response=" + response + "]";
    }

}
