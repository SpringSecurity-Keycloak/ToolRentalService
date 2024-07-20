package io.rentalapp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rentalapp.api.ApiApi;
import io.rentalapp.api.model.RentalAgreement;
import io.rentalapp.api.model.RentalRequest;
import io.rentalapp.api.model.Tool;
import io.rentalapp.api.model.ToolPricingDetails;
import io.rentalapp.common.ValidationException;
import io.rentalapp.service.IRentalAgreementService;
import io.rentalapp.service.RentalDurationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.Generated;
import java.util.List;

/**
 * The REST Controller that implements the OpenApi operations
 */
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T15:29:49.511604531Z[GMT]")
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
                ToolPricingDetails pricingDetails = rentalAgreementService.findPricingDetailsForTool(code);
                ResponseEntity<ToolPricingDetails> response = ResponseEntity.ok(pricingDetails);
                return response;
            } catch (Exception e) {
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
               if (body.getDiscountPercent() == null) {
                   body.setDiscountPercent(0);
                }

               try {
                   RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(body);
                   ResponseEntity<RentalAgreement> response = ResponseEntity.ok(rentalAgreement);
                   return response;
               }catch (ValidationException ex) {
                       throw ex;
               }


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

