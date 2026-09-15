package com.Ashish.airBnbClone.strategy;

import com.Ashish.airBnbClone.entity.Inventory;

import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy{

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
