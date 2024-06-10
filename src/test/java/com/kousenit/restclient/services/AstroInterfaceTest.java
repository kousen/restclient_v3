package com.kousenit.restclient.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Isolated
@SpringBootTest
class AstroInterfaceTest {

    @Autowired
    private AstroInterface astroInterface;

    @Test
    void getAstroResponse() {
        var response = astroInterface.getAstroResponse();
        assertNotNull(response);
        assertAll(
                () -> assertEquals("success", response.message()),
                () -> assertTrue(response.number() >= 0),
                () -> assertEquals(response.number(), response.people().size())
        );
        System.out.println(response);
    }
}