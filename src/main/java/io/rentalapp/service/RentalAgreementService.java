package io.rentalapp.service;

import io.rentalapp.api.model.Tool;
import io.rentalapp.api.model.ToolPricingDetails;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RentalAgreementService implements IRentalAgreementService {

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

        /*
         * Determine number of weekdays and holidays in the requested rental period
         */
        RentalDurationService rentalDurationService = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDurationService.calculateDatesForRental(checkoutDate,rentalRequest.getRentailDaysCount());

        /*
         * Verify that the tool is available on the requested dates
         */
        Iterable<RentalAgreementEntity> existingAgreements = rentalAgreementRepository.findAllByToolCode(rentalRequest.getToolCode());
        boolean isToolAvailableForRental = verifyToolIsAvailable(existingAgreements,dateRangeDetails);
        if (!isToolAvailableForRental) {
            throw new ValidationException("Tool is unavailable to rent for the requested dates");
        }

        /*
         * Get pricing rules for requested tool
         */
        ToolRentalPriceEntity rentalPrice = getRentalPriceForTool(availableTools.get(rentalRequest.getToolCode()).getType());

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
         * Calculate the pricing based on the days requested and the pricing rules
         * for the tool and return a rental agreement
         */
        RentalAgreementEntity rentalAgreementEntity = calculateRentalPrice(rentRequest,
                dateRangeDetails,rentalPrice);

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
                                                       ToolRentalPriceEntity rentalPrice) {

        double preDiscountCharge = 0;
        int rentalDays = dateRangeDetails.getTotalWeekDays() + dateRangeDetails.getTotalWeekendDays() + dateRangeDetails.getTotalHolidays();
        int chargeDays = rentalDays;

        if (!rentalPrice.isWeekEndChargeable()) {
            chargeDays = chargeDays - dateRangeDetails.getTotalWeekendDays();
        }

        if (!rentalPrice.isHolidayChargeable()) {
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
                .dueDate(dateRangeDetails.getDueDate())
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
    private boolean verifyToolIsAvailable(Iterable<RentalAgreementEntity> existingAgreements, DateRangeDetails requestedRentalDates) {
        boolean toolIsAvailableForRental = false;
        final List<LocalDate> checkedOutDates = new ArrayList<LocalDate>();
        existingAgreements.forEach(rentalAgreement -> checkedOutDates.addAll(RentalDurationService.getDateRange(rentalAgreement)));

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
     * Find Pricing Details for a tool
     *
     * @param code
     * @return
     */
    @Override
    public ToolPricingDetails findPricingDetailsForTool(String toolType) {
        ToolRentalPriceEntity toolRentalPriceEntity = toolRentalPriceRepositorty.findByCode(toolType);

        ToolPricingDetails pricingDetails = new ToolPricingDetails();
        pricingDetails.setCode(toolRentalPriceEntity.getToolType());
        pricingDetails.setDailyCharge(toolRentalPriceEntity.getDailyCharge());
        pricingDetails.setHolidayCharge(toolRentalPriceEntity.isHolidayChargeable());
        pricingDetails.setWeekendCharge(toolRentalPriceEntity.isWeekEndChargeable());

        return pricingDetails;
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
