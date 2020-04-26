package com.gamedia.gamedia.controller;

import com.gamedia.gamedia.dto.ExchangeDTO;
import com.gamedia.gamedia.exceptions.ConvertException;
import com.gamedia.gamedia.exceptions.CreateDtoException;
import com.gamedia.gamedia.service.ExchangeCryptoService;
import com.gamedia.gamedia.service.QuoteCryptoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@Log4j2
@RequestMapping(value = "currencies")
public class CryptoApiController {

    private final QuoteCryptoService quoteCryptoService;

    private final ExchangeCryptoService exchangeCryptoService;

    public CryptoApiController(QuoteCryptoService quoteCryptoService, ExchangeCryptoService exchangeCryptoService) {
        this.quoteCryptoService = quoteCryptoService;
        this.exchangeCryptoService = exchangeCryptoService;
    }

    @GetMapping(value = "{currency}")
    public ResponseEntity quoteList(@PathVariable("currency") String currency,
                                    @RequestParam(value = "filter", required = false) String filter) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(quoteCryptoService.prepareQuoteListResponse(currency, filter));
        } catch (Exception e) {
            log.error(MessageFormat.format("Cannot get a response{0}", e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "exchange")
    public ResponseEntity exchange(@RequestBody final ExchangeDTO exchangeDTO) {
        try{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(exchangeCryptoService.prepareExchangeListResponse(exchangeDTO));
        }catch (ConvertException e ) {
            log.error(MessageFormat.format("Cannot convert data. ", e));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot convert data.");
        }catch (CreateDtoException e ) {
            log.error(MessageFormat.format("Cannot create DTO. ", e));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            log.error(MessageFormat.format("Cannot get a response{0}", e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
