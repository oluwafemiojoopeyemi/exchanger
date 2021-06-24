package com.webfactory.exchanger.controllers;

import com.webfactory.exchanger.dto.CurrentRateResponse;
import com.webfactory.exchanger.dto.HistoryRateResponse;
import com.webfactory.exchanger.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
@RequestMapping("/exchanger")
public class ExchangeRateController {

    @Autowired
    private ExchangeService exchangeService;

    @Async
    @GetMapping(value = "getCurrentRateByCurrency", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrentRateResponse> getCurrentRateByCurrency(@RequestParam(name = "currency") String currency) {
        return exchangeService.getCurrentRateByCurrency(currency);
    }

    @Async
    @GetMapping(value = "getCurrentRateByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoryRateResponse> getCurrentRateByDate(@RequestParam(name = "startDate") String startDate,
                                                                        @RequestParam(name = "endDate") String endDate) {
        return exchangeService.getCurrentRateByDate(startDate,endDate);
    }

}
