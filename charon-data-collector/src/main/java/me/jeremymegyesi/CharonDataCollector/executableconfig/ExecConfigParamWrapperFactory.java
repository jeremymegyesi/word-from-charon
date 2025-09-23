package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecConfigParamWrapperFactory {
    public static <T> ExecConfigParamWrapper<T> fromJson(String json, Class<T> clazz) {
        try {
            T value = new ObjectMapper().readValue(json, clazz);
            return new ExecConfigParamWrapper<>(value);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }

}
