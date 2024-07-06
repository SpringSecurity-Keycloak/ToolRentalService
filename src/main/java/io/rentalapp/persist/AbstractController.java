package io.rentalapp.persist;

import io.rentalapp.api.ApiApi;
import io.rentalapp.model.Inventory;
import io.rentalapp.model.RentalAgreement;
import io.rentalapp.model.RentalRequest;
import io.rentalapp.model.Tool;
import io.rentalapp.model.ToolPricingDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T15:29:49.511604531Z[GMT]")
@RestController
public class AbstractController implements ApiApi {

    private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    ToolRepository repository;

    @org.springframework.beans.factory.annotation.Autowired
    public AbstractController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<Tool>> getApiV1Tool() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                repository.findAll().forEach(tool -> {
                    log.info(tool.toString());
                });
                List<Tool> tools = StreamSupport
                                   .stream(repository.findAll().spliterator(),false)
                                    .collect(Collectors.toList());
                ResponseEntity<List<Tool>> response = ResponseEntity.ok(tools);
                return response;
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Tool>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<Tool>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<ToolPricingDetails> getApiV1ToolCode(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("code") String code
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ToolPricingDetails>(objectMapper.readValue("{\n  \"code\" : \"code\",\n  \"daily_charge\" : 0.8008281904610115,\n  \"holiday_charge\" : true,\n  \"weekend_charge\" : true\n}", ToolPricingDetails.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ToolPricingDetails>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ToolPricingDetails>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Inventory> getApiV1ToolInventory(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("code") String code
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Inventory>(objectMapper.readValue("{\n  \"max_available\" : 0.8008281904610115,\n  \"current_count\" : 6.027456183070403,\n  \"tool_code\" : \"tool_code\"\n}", Inventory.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Inventory>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Inventory>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<RentalAgreement> postApiV1ToolCodeCheckout(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("code") String code
            , @Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody RentalRequest body
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<RentalAgreement>(objectMapper.readValue("{\n  \"rental_days\" : \"rental_days\",\n  \"charge_days\" : 6.027456183070403,\n  \"pre_discount_charge\" : 1.4658129805029452,\n  \"tool_type\" : \"tool_type\",\n  \"daily_charge\" : 0.8008281904610115,\n  \"due_date\" : \"due_date\",\n  \"tool_code\" : \"tool_code\",\n  \"final_charge\" : 5.637376656633329,\n  \"id\" : \"id\",\n  \"discount_percent\" : 5.962133916683182,\n  \"tool_brand\" : \"tool_brand\",\n  \"checkout_date\" : \"checkout_date\"\n}", RentalAgreement.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<RentalAgreement>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<RentalAgreement>(HttpStatus.NOT_IMPLEMENTED);
    }

}

