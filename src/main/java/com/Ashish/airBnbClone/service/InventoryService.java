package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.entity.Room;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

}
