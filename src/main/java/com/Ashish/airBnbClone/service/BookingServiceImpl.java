package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.dto.BookingDto;
import com.Ashish.airBnbClone.dto.BookingInitRequest;
import com.Ashish.airBnbClone.dto.GuestDto;
import com.Ashish.airBnbClone.entity.*;
import com.Ashish.airBnbClone.entity.enums.BookingStatus;
import com.Ashish.airBnbClone.exception.ResourceNotFoundException;
import com.Ashish.airBnbClone.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingInitRequest bookingInitRequest) {
        log.info("Initialising booking for hotel : {}, room: {}, date {}-{}", bookingInitRequest,
                bookingInitRequest.getRoomId(), bookingInitRequest.getCheckInDate(), bookingInitRequest.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingInitRequest.getHotelId()).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with id: "+bookingInitRequest.getHotelId()));
        Room room = roomRepository.findById(bookingInitRequest.getRoomId()).orElseThrow(() ->
                new ResourceNotFoundException("Room not found with id: "+bookingInitRequest.getRoomId()));
        if(!room.getHotel().getId().equals(bookingInitRequest.getHotelId())) {
            throw new RuntimeException("Room does not belong to this hotel");
        }

        // lock
        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingInitRequest.getCheckInDate(), bookingInitRequest.getCheckOutDate(), bookingInitRequest.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingInitRequest.getCheckInDate(), bookingInitRequest.getCheckOutDate()) + 1;

        if(inventoryList.size() != daysCount){
            throw new IllegalStateException("Room is not available anymore");
        }

        // Reserve the room.
        for(Inventory inventory : inventoryList){
            inventory.setReservedCount(inventory.getReservedCount() + bookingInitRequest.getRoomsCount());
        }
        inventoryRepository.saveAll(inventoryList);

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingInitRequest.getCheckInDate())
                .checkOutDate(bookingInitRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingInitRequest.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {

        log.info("Adding guests for booking with id: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResourceNotFoundException("Booking not found with id: "+bookingId));

        if (hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }

        for (GuestDto guestDto: guestDtoList) {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(getCurrentUser());
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }


    public User getCurrentUser(){
        User user = new User();
        user.setId(1L);
        return user;
    }

    public boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

}
