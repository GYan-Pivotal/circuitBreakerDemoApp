package com.broadcom.solutions.gemfire.circuitBreakerDemoApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "integration")
public class CircuitBreakerDemoAppApplicationTests {

	@Test
	public void contextLoads() {
	}

}
