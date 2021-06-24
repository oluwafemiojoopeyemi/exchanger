package com.webfactory.exchanger.service;

import com.webfactory.exchanger.dto.CurrentRateResponse;
import com.webfactory.exchanger.dto.HistoryRateResponse;
import org.springframework.http.ResponseEntity;

public interface ExchangeService {


    public ResponseEntity<CurrentRateResponse> getCurrentRateByCurrency(String currency);
    public ResponseEntity<HistoryRateResponse> getCurrentRateByDate(String startDate, String endDate);


    }
