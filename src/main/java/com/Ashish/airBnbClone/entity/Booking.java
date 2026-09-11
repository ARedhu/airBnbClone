package com.Ashish.airBnbClone.entity;


import com.Ashish.airBnbClone.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer roomsCount;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "booking_guest",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id")
    ) // We can even skip this thing. But remember if we are using it, then use it only for one-side table/entity. Else it will try to create duplicate entries and will end up with an exception.
    private Set<Guest> guests;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
}

// Note:
//1.  One-to-Many, Many-to-One, Many-to-Many relationships. How they work in Java and how they get stored in DB.
//2.  Many-to-one will own the relationship. Even if we see that here we have one-to-many relationship but the many side will own the relationship and will have @JoinColumn or @JoinTable.
//3.  If a table don't own the relationship then it must have "mappedBy" so that Java code can know how the tables are related. Remember DB don't need it.
//4.  In case of Many-to-Many relationship a new join table gets created with the ids from both the sides.