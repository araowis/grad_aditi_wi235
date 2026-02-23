package com.example.calculator_test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    @Mock
    CalculatorInterface mockOps;

    @Test
    void testVoidCallNTimes() {
        System.out.println("Starting testVoidCallNTimes");

        System.out.println("Calling logSomethingNTimes(2)");
        mockOps.logSomethingNTimes();
        mockOps.logSomethingNTimes();

        System.out.println("Verifying logOperation called 2 times");
        verify(mockOps, times(2)).logSomethingNTimes();
        System.out.println("testVoidCallNTimes done ");
    }

    @Test
    void testVoidCalled() {
        System.out.println("Starting testVoidCalled");
        // mockOps.logSomethingNTimes(1);        

        // verify(mockOps, times(1)).logSomethingNTimes();
        verify(mockOps, never()).logSomethingNTimes();

        System.out.println("testVoidCalled done");
    }
}