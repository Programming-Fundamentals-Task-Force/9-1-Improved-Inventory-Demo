package org.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.lab.*;

class ClassTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCapture = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCapture));
    }

    @Test void
    output_conforms_to_the_expected_output() {
        Class object = new Class();
        object.printSomething();
        assertEquals(outputStreamCapture.toString().trim(), "expected output");
    }

    @Test void
    object_value_equals_something() {
        Class object = new Class();
        assertEquals(object.getValue(), "something");
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }
}