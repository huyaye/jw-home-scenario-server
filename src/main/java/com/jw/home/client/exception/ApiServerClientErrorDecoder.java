package com.jw.home.client.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jw.home.exception.InvalidHomeException;
import com.jw.home.exception.NotFoundDeviceException;
import com.jw.home.rest.dto.ResponseDto;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Component
public class ApiServerClientErrorDecoder implements ErrorDecoder {
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 409:
                Reader reader = response.body().asReader(StandardCharsets.UTF_8);
                String errorResult = IOUtils.toString(reader);
                ObjectMapper objectMapper = new ObjectMapper();
                ResponseDto errorResponse = objectMapper.readValue(errorResult, ResponseDto.class);
                switch (errorResponse.getErrorCode()) {
                    case 301:
                        return InvalidHomeException.INSTANCE;
                    case 401:
                        return NotFoundDeviceException.INSTANCE;
                }
        }
        return new Exception(response.reason());
    }
}
