package io.rentalapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rentalapp.api.ApiApi;
import io.rentalapp.common.DateUtility;
import io.rentalapp.model.*;
import io.rentalapp.persist.ToolRepository;
import io.rentalapp.persist.model.RentalAgreementDTO;
import io.rentalapp.service.HolidayService;
import io.rentalapp.service.RentalAgreementService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The REST Controller that implements the OpenApi operations
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T15:29:49.511604531Z[GMT]")
@RestController
public class AbstractController implements ApiApi {

    private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private HolidayService holidayService = new HolidayService();

    private DateUtility dateUtility = new DateUtility();

    @Autowired
    ToolRepository repository;

    @Autowired
    RentalAgreementService rentalAgreementService;

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
                repository.findAll().forEach(tool -> {
                    log.info(tool.toString());
                });
                List<Tool> tools = StreamSupport
                                   .stream(repository.findAll().spliterator(),false)
                                    .map( toolDto -> {
                                        Tool tool = new Tool();
                                        tool.setBrand(toolDto.getBrand());
                                        tool.setCode(toolDto.getCode());
                                        tool.type(toolDto.getType());
                                        return tool;
                                    })
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
     * Get the inventory per tool.
     * @param code
     * @return
     */
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

                RentalAgreementDTO newAgreement  = rentalAgreementService.createRentalAgreement(body);
                RentalAgreement rentalAgreement = new RentalAgreement();

                rentalAgreement.setToolCode(newAgreement.getToolCode());
                rentalAgreement.setToolBrand(newAgreement.getToolBrand());
                rentalAgreement.setToolType(newAgreement.getToolType());
                rentalAgreement.setRentalDays(newAgreement.getRentalDays().toPlainString());
                rentalAgreement.setCheckoutDate(dateUtility.format(newAgreement.getCheckoutDate()));
                rentalAgreement.setDueDate( dateUtility.format(newAgreement.getDueDate()));
                rentalAgreement.setDailyCharge(newAgreement.getDailyCharge());
                rentalAgreement.setChargeDays(newAgreement.getChargeDays());
                rentalAgreement.setPreDiscountCharge(newAgreement.getPreDiscountCharge());
                rentalAgreement.setDiscountPercent(newAgreement.getDiscountPercent());
                rentalAgreement.setDiscountAmount(newAgreement.getDiscountAmount());
                rentalAgreement.setFinalCharge(newAgreement.getFinalCharge());

                ResponseEntity<RentalAgreement> response = ResponseEntity.ok(rentalAgreement);
                return response;

        }

        return new ResponseEntity<RentalAgreement>(HttpStatus.NOT_IMPLEMENTED);
    }

}

