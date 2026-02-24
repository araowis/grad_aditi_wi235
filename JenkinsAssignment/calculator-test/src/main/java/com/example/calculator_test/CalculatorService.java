package com.example.calculator_test;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService implements CalculatorInterface {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("Division by zero!");
        return a / b;
    }

    @Override
    public int mul(int a, int b) {
        return a * b;
    }

    @Override
    public int subt(int a, int b) {
        return a - b;
    }

    @Override
    public void logSomethingNTimes() {}
}
