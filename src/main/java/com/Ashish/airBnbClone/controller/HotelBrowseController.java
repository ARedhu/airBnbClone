package com.Ashish.airBnbClone.controller;

import com.Ashish.airBnbClone.dto.HotelDto;
import com.Ashish.airBnbClone.dto.HotelInfoDto;
import com.Ashish.airBnbClone.dto.HotelSearchReqDto;
import com.Ashish.airBnbClone.service.HotelService;
import com.Ashish.airBnbClone.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchReqDto hotelSearchReqDto){
        Page<HotelDto> page = inventoryService.searchHotels(hotelSearchReqDto);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
