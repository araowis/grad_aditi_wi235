package com.example.calculator_test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorServiceMathTest {

    private CalculatorService calc;

    @BeforeEach
    void setUp() {
        calc = new CalculatorService();
    }

    @Test
    void testAdd() {
        assertEquals(5, calc.add(2, 3));
        assertEquals(-1, calc.add(-2, 1));
        assertEquals(0, calc.add(0, 0));
    }

    @Test
    void testSubt() {
        assertEquals(1, calc.subt(3, 2));
        assertEquals(-3, calc.subt(-2, 1));
        assertEquals(0, calc.subt(0, 0));
    }

    @Test
    void testMul() {
        assertEquals(6, calc.mul(2, 3));
        assertEquals(-2, calc.mul(-2, 1));
        assertEquals(0, calc.mul(0, 10));
    }

    @Test
    void testDivide() {
        assertEquals(2, calc.divide(6, 3));
        assertEquals(-2, calc.divide(-4, 2));
        
        assertThrows(ArithmeticException.class, () -> calc.divide(5, 0));
    }
}