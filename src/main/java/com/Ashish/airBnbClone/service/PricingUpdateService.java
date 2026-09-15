package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.entity.Hotel;
import com.Ashish.airBnbClone.entity.HotelMinPrice;
import com.Ashish.airBnbClone.entity.Inventory;
import com.Ashish.airBnbClone.repository.HotelMinPriceRepository;
import com.Ashish.airBnbClone.repository.HotelRepository;
import com.Ashish.airBnbClone.repository.InventoryRepository;
import com.Ashish.airBnbClone.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {

    // Scheduler to update the inventory and HotelMinPrice tables every hour

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService; // Took from strategy.

    // cron jobs are those functions which we executes automatically after a certain period of time.
//    @Scheduled(cron = "*/5 * * * * *")
    @Scheduled(cron = "0 0 * * * *")
    public void updatePrices() {
        int page = 0;
        int batchSize = 100;

        while(true) {
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if(hotelPage.isEmpty()) {
                break;
            }
//            hotelPage.getContent().forEach(this::updateHotelPrices);
            hotelPage.getContent().forEach(hotel -> updateHotelPrices(hotel));

            page++;
        }
    }

    private void updateHotelPrices(Hotel hotel) {
        log.info("Updating hotel prices for hotel ID: {}", hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);

        updateInventoryPrices(inventoryList);

        updateHotelMinPrice(hotel, inventoryList, startDate, endDate);
    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {

        Map<LocalDate, BigDecimal> dailyMinPrices = new HashMap<>();

        // Find minimum price for each date
        for (Inventory inventory : inventoryList) {

            LocalDate date = inventory.getDate();
            BigDecimal price = inventory.getPrice();

            if (!dailyMinPrices.containsKey(date)
                    || price.compareTo(dailyMinPrices.get(date)) < 0) {

                dailyMinPrices.put(date, price);
            }
        }

        // Create/update HotelMinPrice
        List<HotelMinPrice> hotelPrices = new ArrayList<>();

        dailyMinPrices.forEach((date, price) -> {

            HotelMinPrice hotelPrice = hotelMinPriceRepository
                                            .findByHotelAndDate(hotel, date)
                                            .orElse(new HotelMinPrice(hotel, date));

            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        hotelMinPriceRepository.saveAll(hotelPrices);
    }

    private void updateInventoryPrices(List<Inventory> inventoryList) {
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }

}

