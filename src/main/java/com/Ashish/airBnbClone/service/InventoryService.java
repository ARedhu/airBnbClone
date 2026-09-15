package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.dto.HotelDto;
import com.Ashish.airBnbClone.dto.HotelPriceDto;
import com.Ashish.airBnbClone.dto.HotelSearchReqDto;
import com.Ashish.airBnbClone.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchReqDto hotelSearchReqDto);
}
