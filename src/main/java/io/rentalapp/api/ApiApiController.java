package io.rentalapp.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.rentalapp.api.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;

public class ApiApiController extends AbstractController {

    public ApiApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        super(objectMapper, request);
    }
}
