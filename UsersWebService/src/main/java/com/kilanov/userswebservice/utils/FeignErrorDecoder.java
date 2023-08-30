package com.kilanov.userswebservice.utils;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                //
                break;
            case 404:
                return new ResponseStatusException(HttpStatusCode.valueOf(response.status()));
            default:
                new Exception(response.reason());
        }
        return null;
    }
}
