package com.example.javastudy.serialize;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

public class MapperTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        mapper.findAndRegisterModules();
    }

    @Test
    void jackson_primitive_boolean_test() throws JsonProcessingException {
        String json = "{\"value\":true}";
        var obj = mapper.readValue(json, TestJsonObject.class);
        System.out.println(obj.value);

        String emptyJson = "{}";
        var emptyObj = mapper.readValue(emptyJson, TestJsonObject.class);
        System.out.println(emptyObj.value);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class TestJsonObject {
        private boolean value;
    }
}
