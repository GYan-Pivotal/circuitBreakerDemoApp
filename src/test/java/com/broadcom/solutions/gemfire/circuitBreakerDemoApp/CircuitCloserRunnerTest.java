package com.broadcom.solutions.gemfire.circuitBreakerDemoApp;

import org.junit.Test;
import org.mockito.Mockito;

public class CircuitCloserRunnerTest
{

    @Test
    public void run()
    {
        CircuitBreaker cb = Mockito.mock(CircuitBreaker.class);
        new CircuitCloserRunner(cb,2000);


    }
}