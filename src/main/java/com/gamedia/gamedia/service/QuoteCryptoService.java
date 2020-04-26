package com.gamedia.gamedia.service;

import com.gamedia.gamedia.dto.CoinBeneDTO;
import com.gamedia.gamedia.interfaces.DataConverter;
import com.gamedia.gamedia.utils.HttpUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Log4j2
@Service
public class QuoteCryptoService implements DataConverter<Object> {

    @Value("${crypto.api.key}")
    private String apiKey;

    @Value("${crypto.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public QuoteCryptoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> prepareQuoteListResponse(String currency, String filter) throws IOException {
        return (!StringUtils.isEmpty(filter))?
                convertStringToMap(getQuoteList(currency, filter)):
                convertStringToMap(getQuoteList(currency, prepareDefaultFilter()));
    }

    private String getQuoteList(String currency, CharSequence filter){
        return restTemplate.exchange(apiUrl+"/pricemulti?fsyms="+currency+"&tsyms="+filter, HttpMethod.GET, HttpUtil.httpEntity(), String.class).getBody();
    }

    private CoinBeneDTO getTopSymbols(){
        return restTemplate.exchange(apiUrl+"exchange/top/volume?e=CoinBene&direction=TO&limit=100", HttpMethod.GET, HttpUtil.httpEntity(), CoinBeneDTO.class).getBody();
    }

    private StringBuilder prepareDefaultFilter() {
        CoinBeneDTO symbols = getTopSymbols();
        StringBuilder defaultFilter = new StringBuilder();
        for (int i =0; i < 20; i++) { //max length filter param
            defaultFilter.append(symbols.getData().get(i).getToSymbol());
            defaultFilter.append(",");
        }
        defaultFilter.setLength(defaultFilter.length()-1);
        return defaultFilter;
    }

}
