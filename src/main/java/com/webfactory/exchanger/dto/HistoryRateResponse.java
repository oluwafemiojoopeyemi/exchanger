package com.webfactory.exchanger.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HistoryRateResponse {

    private Map<String,String> rate;
    private String updated;
    private String description;



}
