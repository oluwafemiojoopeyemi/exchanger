package com.webfactory.exchanger.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CurrentRateResponse {

    private String rate;
    private String currency;
    private String updated;
    private String description;



}
