package io.rentalapp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rentalapp.api.ApiApi;
import io.rentalapp.api.model.*;
import io.rentalapp.common.ValidationException;
import io.rentalapp.service.RentalDurationService;
import io.rentalapp.service.IRentalAgreementService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * The REST Controller that implements the OpenApi operations
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T15:29:49.511604531Z[GMT]")
@RestController
public class AbstractController implements ApiApi {

    private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private RentalDurationService holidayService = new RentalDurationService();

    @Autowired
    IRentalAgreementService rentalAgreementService;

    /**
     *
     * @param objectMapper
     * @param request
     */
    @org.springframework.beans.factory.annotation.Autowired
    public AbstractController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /**
     * Fetches all availble tools for rentals
     * @return List of tools available to rent
     */
    public ResponseEntity<List<Tool>> getApiV1Tool() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                List<Tool> tools = rentalAgreementService.findAllTools();
                ResponseEntity<List<Tool>> response = ResponseEntity.ok(tools);
                return response;
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Tool>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<Tool>>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Gets pricing details by tool code
     * @param code The tool code
     * @return The pricing details for the given tool
     */
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

    /**
     * Checks out a tool for rental and creates a new rental agreement
     * @param code the codeof the tool to check out
     * @param body The rental request details
     * @return the rental agreement
     */
    public ResponseEntity<RentalAgreement> postApiV1ToolCodeCheckout(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("code") String code
            , @Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody RentalRequest body
    ) {

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {

               if (StringUtils.isAllEmpty(body.getToolCode()) ||
                       StringUtils.isAllEmpty(body.getCheckoutDate()) ||
                               body.getRentailDaysCount() == null) {
                   throw new ValidationException("Tool code, checkoutdate and rental days are required for this request");
               }

               if (body.getDiscountPercent() == null) {
                   body.setDiscountPercent(0);
                }

                RentalAgreement rentalAgreement  = rentalAgreementService.createRentalAgreement(body);
                ResponseEntity<RentalAgreement> response = ResponseEntity.ok(rentalAgreement);
                return response;

        }

        return new ResponseEntity<RentalAgreement>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Retrieve all Rental Agreements stored in the system
     * @return
     */
    @Override
    public ResponseEntity<List<RentalAgreement>> getAllRentalAgreements() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                    List<RentalAgreement> allRentalAgreements = rentalAgreementService.findAllRentalAgreements();
                    return ResponseEntity.ok().body(allRentalAgreements);
                } catch (Exception e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<List<RentalAgreement>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<RentalAgreement>>(HttpStatus.NOT_IMPLEMENTED);
    }

}

