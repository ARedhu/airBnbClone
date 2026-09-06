package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.entity.Room;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteFutureInventories(Room room);

}
