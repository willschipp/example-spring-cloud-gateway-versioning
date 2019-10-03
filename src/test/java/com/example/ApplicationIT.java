package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationIT {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void main() {
        assertTrue(applicationContext.getBeanDefinitionCount() > 0);
    }
}