package com.statemachine.demo.service;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class ShippingCalculator {

    public Optional<Double> calculate() {
        Random r = new Random();
        int low = 10;
        int high = 100;
        int result = r.nextInt(high-low) + low;

        return result % 2 == 0 ? Optional.ofNullable((double) result) : Optional.empty();
    }
}
