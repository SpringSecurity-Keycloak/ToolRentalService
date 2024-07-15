package io.rentalapp.service;

import io.rentalapp.api.model.Tool;
import io.rentalapp.common.*;
import io.rentalapp.api.model.RentalAgreement;
import io.rentalapp.api.model.RentalRequest;
import io.rentalapp.persist.RentalAgreementRepository;
import io.rentalapp.persist.RentalRequestRepository;
import io.rentalapp.persist.ToolRentalPriceRepositorty;
import io.rentalapp.persist.ToolRepository;
import io.rentalapp.persist.entity.RentalAgreementEntity;
import io.rentalapp.persist.entity.RentalRequestEntity;
import io.rentalapp.persist.entity.ToolEntity;
import io.rentalapp.persist.entity.ToolRentalPriceEntity;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RentalAgreementService {

    private static final Logger log = LoggerFactory.getLogger(RentalAgreementService.class);

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    ToolRepository toolRepository;

    @Autowired
    ToolRentalPriceRepositorty toolRentalPriceRepositorty = null;

    HashMap<String, ToolEntity> availableTools = new HashMap<String, ToolEntity>();

    HashMap<String, ToolRentalPriceEntity> rentalPriceMap = new HashMap<String, ToolRentalPriceEntity>();

    /**
     * Creates a rental agreement based on a rental request
     * @param rentalRequest
     * @return the new rental agreement
     */
    public RentalAgreement createRentalAgreement(RentalRequest rentalRequest) {

        if (!isValidToolCode(rentalRequest.getToolCode())) {
            throw new ValidationException("Invalid Tool Code Entered");
        }

        Date checkoutDate = DataFormat.parseDate(rentalRequest.getCheckoutDate());
        Date rentalDueDate = DateUtils.addDays(checkoutDate,rentalRequest.getRentailDaysCount());

        /*
         * Save the rental request
         */
        RentalRequestEntity rentRequest = rentalRequestRepository
                .save(RentalRequestEntity.builder()
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
        ToolRentalPriceEntity rentalPrice = getRentalPriceForTool(availableTools.get(rentalRequest.getToolCode()).getType());

        /*
         * Calculate the pricing based on the days requested and the pricing rules
         * for the tool and return a rental agreement
         */
        RentalAgreementEntity rentalAgreementEntity = this.calculateRentalPrice(rentRequest,
                dateRangeDetails,
                rentalPrice,
                rentalDueDate);

        /*
         * Save the rental agreement
         */
        rentalAgreementEntity = rentalAgreementRepository.save(rentalAgreementEntity);
        RentalAgreement rentalAgreement = DataFormat.toRentalAgreement(rentalAgreementEntity);
        return rentalAgreement;
    }

    /**
     *
     * @param rentalRequest
     * @param dateRangeDetails
     * @param rentalPrice
     * @return The new Rental Agreement with calculated Rental price
     */
    private RentalAgreementEntity calculateRentalPrice(RentalRequestEntity rentalRequest,
                                                       DateRangeDetails dateRangeDetails,
                                                       ToolRentalPriceEntity rentalPrice,
                                                       Date rentalDueDate) {

        Iterable<RentalAgreementEntity> existingAgreements = rentalAgreementRepository.findAllByToolCode(rentalRequest.getToolCode());

        boolean isToolAvailableForRental = this.isToolAvailableForRent(existingAgreements,dateRangeDetails);
        if (!isToolAvailableForRental) {
            throw new ValidationException("Tool is unavailable to rent for the requested dates");
        }

        double preDiscountCharge = 0;
        int rentalDays = dateRangeDetails.getTotalWeekDays() + dateRangeDetails.getTotalWeekendDays() + dateRangeDetails.getTotalHolidays();
        int chargeDays = rentalDays;

        long weekendCharge = 0;
        if (!rentalPrice.isWeekEndChargeable()) {
            //weekendCharge = dateRangeDetails.getTotalWeekendDays() * rentalPrice.getDailyCharge().longValue();
            chargeDays = chargeDays - dateRangeDetails.getTotalWeekendDays();
        }

        long holidayCharge = 0;
        if (!rentalPrice.isHolidayChargeable()) {
            //holidayCharge = dateRangeDetails.getTotalHolidays() * rentalPrice.getDailyCharge().longValue();
            chargeDays = chargeDays - dateRangeDetails.getTotalHolidays();
        }

        preDiscountCharge = chargeDays * rentalPrice.getDailyCharge().doubleValue();

        double discount = 0;
        if (rentalRequest.getDiscountPercent() > 0) {
            discount = preDiscountCharge * rentalRequest.getDiscountPercent() / 100;
        }

        return RentalAgreementEntity.builder()
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
     * @return true if the tool is not checked out for the requested checkout date, false otherwise
     */
    private boolean isToolAvailableForRent(Iterable<RentalAgreementEntity> existingAgreements, DateRangeDetails requestedRentalDates) {
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
    private ToolRentalPriceEntity getRentalPriceForTool(String toolType) {
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
            RentalAgreement agreement = DataFormat.toRentalAgreement(rentalAgreement);
            allRentalAgreements.add(agreement);
        });

        return allRentalAgreements;
    }

    /**
     * Retrieve all tools available for rent
     * @return List of tools available for rent
     */
    public List<Tool> findAllTools() {
        List<Tool> tools = StreamSupport
                .stream(toolRepository.findAll().spliterator(),false)
                .map( toolDto -> {
                    Tool tool = DataFormat.toTool(toolDto);
                    return tool;
                })
                .collect(Collectors.toList());
        return tools;
    }
}
