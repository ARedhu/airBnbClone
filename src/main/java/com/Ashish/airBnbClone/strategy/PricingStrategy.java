package com.Ashish.airBnbClone.strategy;

import com.Ashish.airBnbClone.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
