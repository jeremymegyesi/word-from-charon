package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ExecConfigParamWrapperConverter
implements AttributeConverter<ExecConfigParamWrapper<?>, String> {
        private static final ObjectMapper mapper = new ObjectMapper();

        static {
            // Enable default typing to support @class-based polymorphism
            mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
            );
        }


    @Override
    public String convertToDatabaseColumn(ExecConfigParamWrapper<?> attribute) {
        try {
            return mapper.writeValueAsString(attribute.getValue());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    @Override
    public ExecConfigParamWrapper<?> convertToEntityAttribute(String dbData) {
        try {
            Object value = mapper.readValue(dbData, Object.class); // Jackson will use @class to resolve type
            return new ExecConfigParamWrapper<>(value);
        } catch (IOException e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }
}
