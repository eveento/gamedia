package com.gamedia.gamedia.service;

import com.gamedia.gamedia.dto.CurrencyDTO;
import com.gamedia.gamedia.dto.ExchangeDTO;
import com.gamedia.gamedia.dto.ResponseExchangeDTO;
import com.gamedia.gamedia.exceptions.CreateDtoException;
import com.gamedia.gamedia.interfaces.DataConverter;
import com.gamedia.gamedia.utils.HttpUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Log4j2
@Service
public class ExchangeCryptoService implements DataConverter<BigDecimal> {

    @Value("${crypto.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public ExchangeCryptoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseExchangeDTO prepareExchangeListResponse(final ExchangeDTO exchangeDTO) throws InterruptedException {
        final String params = String.join(",", exchangeDTO.getTo());
        final String responseCurrency = restTemplate.exchange(apiUrl + "/price?fsym=" + exchangeDTO.getFrom() + "&tsyms=" + params, HttpMethod.GET, HttpUtil.httpEntity(), String.class).getBody();
        final Map<String, BigDecimal> map = convertStringToMap(responseCurrency);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        final Map<String,CurrencyDTO> currencyDTOS = prepareMapCurrencyDTO(exchangeDTO, map, executorService);
        checkIfAllThreadClosed(executorService);
        return ResponseExchangeDTO.builder()
                .from(exchangeDTO.getFrom())
                .currencyDTO(currencyDTOS)
                .build();
    }

    private void checkIfAllThreadClosed(ExecutorService executorService) throws InterruptedException {
        try {
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS))
                executorService.shutdownNow();
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            throw new InterruptedException();
        }
    }

    private Map<String, CurrencyDTO> prepareMapCurrencyDTO(ExchangeDTO exchangeDTO, Map<String, BigDecimal> map, ExecutorService executorService) throws InterruptedException {
        Map<String, CurrencyDTO> currencyDTOS = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            Future<BigDecimal> exchangedCurrencyValueFuture = executorService.submit(new Calculation(exchangeDTO.getAmount(), entry.getValue()));
            try {
                currencyDTOS.put(entry.getKey(), createCurrencyDTO(exchangeDTO, entry, exchangedCurrencyValueFuture));
            } catch (ExecutionException e) {
                throw new CreateDtoException("Cannot create currencyDTO: {0}", e);
            }
        }
        return currencyDTOS;
    }

    private CurrencyDTO createCurrencyDTO(ExchangeDTO exchangeDTO, Map.Entry<String, BigDecimal> entry, Future<BigDecimal> doubleFeature) throws InterruptedException, ExecutionException {
        BigDecimal res = doubleFeature.get();
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setAmount(exchangeDTO.getAmount().setScale(4, RoundingMode.DOWN));
        currencyDTO.setFee(new BigDecimal(0.0001).setScale(4, RoundingMode.DOWN));
        currencyDTO.setRate(entry.getValue().setScale(4, RoundingMode.DOWN));
        currencyDTO.setResult(res.setScale(4, RoundingMode.DOWN));
        return  currencyDTO;
    }

    class Calculation implements Callable<BigDecimal> {
        private BigDecimal amount;
        private BigDecimal currency;

        Calculation(BigDecimal amount, BigDecimal currency) {
            this.amount = amount;
            this.currency = currency;
        }

        @Override
        public BigDecimal call() {
           return amount.multiply(currency).add(amount.multiply(new BigDecimal(0.01)));
        }
    }
}
