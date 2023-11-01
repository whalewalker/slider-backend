package com.sliderbackend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sliderbackend.data.model.general.Response;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.sliderbackend.data.model.general.enums.ResponseCode.Successful;

public class Utils {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static <T> Response<T> successfulResponse(List<T> list) {
        Response<T> response = new Response<>();
        response.setResponseCode(Successful.code);
        response.setResponseMessage(Successful.message);
        response.setModelList(list);
        response.setCount(CollectionUtils.isEmpty(list) ? 0 : list.size());
        return response;
    }
}
