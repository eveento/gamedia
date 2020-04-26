package com.gamedia.gamedia.interfaces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamedia.gamedia.exceptions.ConvertException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface DataConverter<T> {
    default Map<String, T> convertStringToMap(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            TypeReference<HashMap<String, T>> typeRef = new TypeReference<HashMap<String, T>>() {};
            return mapper.readValue(json, typeRef);
        }catch (IOException e) {
            e.printStackTrace();
            throw new ConvertException("Problem with convert String to Map {0}", e);
        }
    }
}
