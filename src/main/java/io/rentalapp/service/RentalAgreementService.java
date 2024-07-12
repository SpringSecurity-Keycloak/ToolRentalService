package io.rentalapp.service;

import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.common.DateUtility;
import io.rentalapp.common.DecimalNumber;
import io.rentalapp.common.ValidationException;
import io.rentalapp.model.RentalAgreement;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

        log.info("Search for rental agreements with due date < "+ rentalRequest.getCheckoutDate());
        /*
        Iterable<RentalAgreementDTO> existingAgreements = this.rentalAgreementRepository.findRentalAgreementBetween(
                rentalRequest.getCheckoutDate(),rentalDueDate,rentalRequest.getToolCode());

         */
        Iterable<RentalAgreementDTO> existingAgreements = rentalAgreementRepository.findAll();
        existingAgreements.forEach(agreement -> log.info("Existing agreements : "+agreement.toString()));

        boolean isToolAvailableForRental = this.isToolAvailableForRent(existingAgreements,dateRangeDetails);
        if (!isToolAvailableForRental) {
            throw new ValidationException("Tool is unavailable to rent for the requested dates");
        }

        double preDiscountCharge = 0;
        int rentalDays = dateRangeDetails.getTotalWeekDays() + dateRangeDetails.getTotalWeekendDays() + dateRangeDetails.getTotalHolidays();
        int chargeDays = rentalDays;

        long weekDayCharge = dateRangeDetails.getTotalWeekDays() * rentalPrice.getDailyCharge().longValue();

        long weekendCharge = 0;
        if (rentalPrice.isWeekEndChargeable()) {
            //weekendCharge = dateRangeDetails.getTotalWeekendDays() * rentalPrice.getDailyCharge().longValue();
            chargeDays = chargeDays - dateRangeDetails.getTotalWeekendDays();
        }
        else {
            chargeDays = chargeDays - dateRangeDetails.getTotalWeekendDays();
        }

        long holidayCharge = 0;
        if (rentalPrice.isHolidayChargeable()) {
            //holidayCharge = dateRangeDetails.getTotalHolidays() * rentalPrice.getDailyCharge().longValue();
            chargeDays = chargeDays - dateRangeDetails.getTotalHolidays();
        }
        else {
            chargeDays = chargeDays - dateRangeDetails.getTotalHolidays();
        }

        preDiscountCharge = chargeDays * rentalPrice.getDailyCharge().doubleValue();

        double discount = 0;
        if (rentalRequest.getDiscountPercent() > 0) {
            discount = preDiscountCharge * rentalRequest.getDiscountPercent() / 100;
        }

        return RentalAgreementDTO.builder()
                .toolCode(rentalRequest.getToolCode())
                .toolType(rentalPrice.getToolType())
                .toolBrand(availableTools.get(rentalRequest.getToolCode()).getBrand())
                .checkoutDate(rentalRequest.getCheckoutDate())
                .dueDate(rentalDueDate)
                .rentalRequest(rentalRequest)
                .dailyCharge(rentalPrice.getDailyCharge())
                .chargeDays(Integer.valueOf(chargeDays))
                .rentalDays(Integer.valueOf(rentalDays))
                .preDiscountCharge(DecimalNumber.valueOf(preDiscountCharge))
                .discountPercent(DecimalNumber.valueOf(rentalRequest.getDiscountPercent().longValue()))
                .discountAmount(DecimalNumber.valueOf(discount))
                .finalCharge(DecimalNumber.valueOf(preDiscountCharge - discount ))
                .build();
    }

    /**
     * Check if the tool is available for the requested dates
     * @param existingAgreements
     * @param requestedRentalDates
     * @return
     */
    private boolean isToolAvailableForRent(Iterable<RentalAgreementDTO> existingAgreements, DateRangeDetails requestedRentalDates) {
        boolean toolIsAvailableForRental = false;
        final List<LocalDate> checkedOutDates = new ArrayList<LocalDate>();
        existingAgreements.forEach(rentalAgreement -> checkedOutDates.addAll(HolidayService.getDateRange(rentalAgreement)));

        List<LocalDate> requestedDates = requestedRentalDates.getDateRange();

        log.info("CheckedOutDates = "+ checkedOutDates);
        log.info("RequestedDates = "+ requestedDates);

        Set<LocalDate> datesUnavailableForRental =requestedDates.stream()
                .distinct()
                .filter(checkedOutDates::contains)
                .collect(Collectors.toSet());

        if (datesUnavailableForRental.isEmpty()) {
            toolIsAvailableForRental = true;
        }

        return toolIsAvailableForRental;
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

    /**
     *
     * @return
     */
    public List<RentalAgreement> findAllRentalAgreements() {
        List<RentalAgreement> allRentalAgreements = new ArrayList<RentalAgreement>();
        rentalAgreementRepository.findAll().forEach(rentalAgreement -> {
            RentalAgreement agreement = new RentalAgreement();
            agreement.setDiscountAmount(rentalAgreement.getDiscountAmount());
            agreement.setDiscountPercent(rentalAgreement.getDiscountPercent());
            agreement.setPreDiscountCharge(rentalAgreement.getPreDiscountCharge());
            agreement.setDailyCharge(rentalAgreement.getDailyCharge());
            agreement.setRentalDays(String.valueOf(rentalAgreement.getRentalDays()));
            agreement.setFinalCharge(rentalAgreement.getFinalCharge());
            agreement.setToolType(rentalAgreement.getToolType());
            agreement.setChargeDays(BigDecimal.valueOf(rentalAgreement.getChargeDays()));
            agreement.setDueDate(dateUtility.format(rentalAgreement.getDueDate()));
            agreement.setToolBrand(rentalAgreement.getToolBrand());
            agreement.setToolType(rentalAgreement.getToolType());
            agreement.setToolCode(rentalAgreement.getToolCode());
            agreement.setCheckoutDate(dateUtility.format(rentalAgreement.getCheckoutDate()));


            allRentalAgreements.add(agreement);
        });

        return allRentalAgreements;
    }
}
