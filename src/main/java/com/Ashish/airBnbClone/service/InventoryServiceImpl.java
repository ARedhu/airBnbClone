package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.dto.HotelDto;
import com.Ashish.airBnbClone.dto.HotelPriceDto;
import com.Ashish.airBnbClone.dto.HotelSearchReqDto;
import com.Ashish.airBnbClone.entity.Hotel;
import com.Ashish.airBnbClone.entity.Inventory;
import com.Ashish.airBnbClone.entity.Room;
import com.Ashish.airBnbClone.repository.HotelMinPriceRepository;
import com.Ashish.airBnbClone.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for (; !today.isAfter(endDate); today=today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchReqDto hotelSearchReqDto){
        log.info("Searching hotels for {} city, from {} to {}", hotelSearchReqDto.getCity(), hotelSearchReqDto.getStartDate(), hotelSearchReqDto.getEndDate());

        Pageable pageable = PageRequest.of(hotelSearchReqDto.getPage(), hotelSearchReqDto.getSize());
        long daysCount = ChronoUnit.DAYS.between(hotelSearchReqDto.getStartDate(), hotelSearchReqDto.getEndDate()) + 1; // This line is very imp. See it may happen that in the inventory table, for a particular hotel, b/w these dates, for a particular day we have satisfied number of rooms but another date no room is available. So, we want only those hotels for which these daysCount, rooms are available b/w these dates.

        Page<HotelPriceDto> hotelPage = hotelMinPriceRepository.findHotelWithAvailableInventory(hotelSearchReqDto.getCity(), hotelSearchReqDto.getStartDate(), hotelSearchReqDto.getEndDate(), hotelSearchReqDto.getRoomsCount(), daysCount, pageable);
        return hotelPage;
    }
}
