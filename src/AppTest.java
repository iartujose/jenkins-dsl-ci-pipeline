package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void greetNullShouldReturnHelloWorld() {
        assertEquals("Hello, World!", App.greet(null));
    }

    @Test
    void greetNameShouldReturnHelloName() {
        assertEquals("Hello, Alice!", App.greet("Alice"));
    }
}

