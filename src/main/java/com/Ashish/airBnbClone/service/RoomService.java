package com.Ashish.airBnbClone.service;


import com.Ashish.airBnbClone.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);

    List<RoomDto> getAllRoomsInHotel(Long hotelId);

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long hotelId, Long roomId);

    void activateRoomById(Long hotelId, Long roomId);

}
