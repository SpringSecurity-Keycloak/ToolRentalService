package io.rentalapp.service;

import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.common.DateUtility;
import io.rentalapp.common.ValidationException;
import io.rentalapp.model.RentalRequest;
import io.rentalapp.persist.RentalAgreementRepository;
import io.rentalapp.persist.RentalRequestRepository;
import io.rentalapp.persist.ToolRentalPriceRepositorty;
import io.rentalapp.persist.ToolRepository;
import io.rentalapp.persist.model.RentalAgreementDTO;
import io.rentalapp.persist.model.RentalRequestDTO;
import io.rentalapp.persist.model.ToolDTO;
import io.rentalapp.persist.model.ToolRentalPriceDTO;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Service
public class RentalAgreementService {

    private static final Logger log = LoggerFactory.getLogger(RentalAgreementService.class);
    DateUtility dateUtility = new DateUtility();

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    ToolRepository toolRepository;

    @Autowired
    ToolRentalPriceRepositorty toolRentalPriceRepositorty = null;

    HashMap<String, ToolDTO> availableTools = new HashMap<String,ToolDTO>();

    HashMap<String,ToolRentalPriceDTO> rentalPriceMap = new HashMap<String,ToolRentalPriceDTO>();

    /**
     * Creates a rental agreement based on a rental request
     * @param rentalRequest
     * @return
     */
    public RentalAgreementDTO createRentalAgreement(RentalRequest rentalRequest) {

        if (!isValidToolCode(rentalRequest.getToolCode())) {
            throw new ValidationException("Invalid Tool Code Entered");
        }

        Date checkoutDate = dateUtility.parseDate(rentalRequest.getCheckoutDate());
        Date rentalDueDate = DateUtils.addDays(checkoutDate,rentalRequest.getRentailDaysCount());

        /*
         * Save the rental request
         */
        RentalRequestDTO rentRequest = rentalRequestRepository
                .save(RentalRequestDTO.builder()
                        .toolCode(rentalRequest.getToolCode())
                        .rentalDaysCount(rentalRequest.getRentailDaysCount())
                        .discountPercent(rentalRequest.getDiscountPercent())
                        .checkoutDate(checkoutDate)
                        .build());

        /*
         * Determine number of weekdays and holidays in the requested rental period
         */
        HolidayService holidayService = new HolidayService();
        DateRangeDetails dateRangeDetails = holidayService.calculateDatesForRental(checkoutDate,rentalRequest.getRentailDaysCount());

        /*
         * Get pricing rules for requested tool
         */
        ToolRentalPriceDTO rentalPrice = getRentalPriceForTool(availableTools.get(rentalRequest.getToolCode()).getType());

        /*
         * Calculate the pricing based on the days requested and the pricing rules
         * for the tool and return a rental agreement
         */
        RentalAgreementDTO rentalAgreement = this.calcuateRentalPrice(rentRequest,
                dateRangeDetails,
                rentalPrice,
                rentalDueDate);

        /*
         * Save the rental agreement
         */
        rentalAgreement = rentalAgreementRepository.save(rentalAgreement);

        // fetch all rental requests
        log.info("All Rental Requests found with findAll():");
        log.info("-------------------------------");
        rentalRequestRepository.findAll().forEach(tool -> {
            log.info(tool.toString());
        });
        log.info("");

        // fetch all rental agreements
        log.info("All Rental Agreements found with findAll():");
        log.info("-------------------------------");
        rentalAgreementRepository.findAll().forEach(agreement -> {
            log.info("Tool Code in agreement " + agreement.getRentalRequest().getToolCode());
            log.info("Agreement Details " + agreement);
        });
        log.info(dateRangeDetails.toString());
        log.info("");

        return rentalAgreement;
    }

    /**
     *
     * @param rentalRequest
     * @param dateRangeDetails
     * @param rentalPrice
     * @return The new Rental Agreement with calculated Rental price
     */
    private RentalAgreementDTO calcuateRentalPrice(RentalRequestDTO rentalRequest,
                                                   DateRangeDetails dateRangeDetails,
                                                   ToolRentalPriceDTO rentalPrice,
                                                   Date rentalDueDate) {
        long preDiscountCharge = 0;
        long rentalDays = dateRangeDetails.getTotalWeekDays() + dateRangeDetails.getTotalWeekendDays() + dateRangeDetails.getTotalHolidays();
        long chargeDays = rentalDays;

        long weekDayCharge = dateRangeDetails.getTotalWeekDays() * rentalPrice.getDailyCharge().longValue();

        long weekendCharge = 0;
        if (rentalPrice.isWeekEndChargeable()) {
            weekendCharge = dateRangeDetails.getTotalWeekendDays() * rentalPrice.getDailyCharge().longValue();
        }
        else {
            chargeDays = chargeDays - dateRangeDetails.getTotalWeekendDays();
        }

        long holidayCharge = 0;
        if (rentalPrice.isHolidayChargeable()) {
            holidayCharge = dateRangeDetails.getTotalHolidays() * rentalPrice.getDailyCharge().longValue();
        }
        else {
            chargeDays = chargeDays - dateRangeDetails.getTotalHolidays();
        }

        preDiscountCharge = weekDayCharge + weekendCharge + holidayCharge;
        long discount = 0;
        if (rentalRequest.getDiscountPercent() > 0) {
            discount = preDiscountCharge * preDiscountCharge / 100;
        }

        return RentalAgreementDTO.builder()
                .toolCode(rentalRequest.getToolCode())
                .toolType(rentalPrice.getToolType())
                .toolBrand(availableTools.get(rentalRequest.getToolCode()).getBrand())
                .checkoutDate(rentalRequest.getCheckoutDate())
                .dueDate(rentalDueDate)
                .rentalRequest(rentalRequest)
                .dailyCharge(rentalPrice.getDailyCharge())
                .chargeDays(BigDecimal.valueOf(chargeDays))
                .rentalDays(BigDecimal.valueOf(rentalDays))
                .preDiscountCharge(BigDecimal.valueOf(preDiscountCharge))
                .discountPercent(BigDecimal.valueOf(rentalRequest.getDiscountPercent().longValue()))
                .discountAmount(BigDecimal.valueOf(discount))
                .finalCharge( BigDecimal.valueOf(preDiscountCharge - discount ))
                .build();
    }

    /**
     * Lookup the pricing details for the given tool type
     * @param toolType
     * @return Pricing Details
     */
    private ToolRentalPriceDTO getRentalPriceForTool(String toolType) {
        if (rentalPriceMap.isEmpty()) {
            toolRentalPriceRepositorty.findAll().forEach( toolPrice -> rentalPriceMap.put(toolPrice.getToolType(),toolPrice));
        }

        return rentalPriceMap.get(toolType);
    }

    /**
     * Check if the given tool code is a valid tool code
     * @param toolCode
     * @return true if the tool code is valid
     */
    boolean isValidToolCode(String toolCode) {

        if (availableTools.isEmpty()) {
            toolRepository.findAll().forEach(tool -> availableTools.put(tool.getCode(),tool));
        }

        return availableTools.containsKey(toolCode);
    }

}
