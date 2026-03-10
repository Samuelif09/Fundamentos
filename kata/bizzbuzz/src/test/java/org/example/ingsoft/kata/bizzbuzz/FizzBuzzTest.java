package org.example.ingsoft.kata.bizzbuzz;

import org.example.ingsoft.kata.bizzbuzz.fizzbuzz.FizzBuzz;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FizzBuzzTest {
    @Test
    void TestDomainRange(){
        //Arange
        FizzBuzz fizzBuzz = new FizzBuzz();
        int minValue = -1;
        //Act
        assertThrows(IllegalArgumentException.class, ()-> fizzBuzz.calculate(minValue));

    }
    @Test
    void TestMaxDomainRange(){
        //Arrange
        FizzBuzz fizzBuzz = new FizzBuzz();
        int maxValue = 101;
        //act
        assertThrows(IllegalArgumentException.class, ()-> fizzBuzz.calculate(maxValue));
    }
    @Test
    void testBuzz(){
        //Arange
        FizzBuzz fizzBuzz = new FizzBuzz();
        int value = 10;
        String expected = "Buzz";
        String result = fizzBuzz.calculate(value);
        //act
        assertEquals(expected, result);
    }
    @Test
    void testFizz(){
        //Arange
        FizzBuzz fizzBuzz = new FizzBuzz();
        int value = 27;
        String expected = "Fizz";
        String result = fizzBuzz.calculate(value);
        //act
        assertEquals(expected, result);
    }
    @Test
    void testFizzBuzz(){
        //Arange
        FizzBuzz fizzBuzz = new FizzBuzz();
        int value = 15;
        String expected = "FizzBuzz";
        String result = fizzBuzz.calculate(value);
        //act
        assertEquals(expected, result);
    }
    @Test
    void testNoFizzBuzz(){
        //Arange
        FizzBuzz fizzBuzz = new FizzBuzz();
        int value = 4;
        String expected = "4";
        String result = fizzBuzz.calculate(value);
        //act
        assertEquals(expected, result);
    }

}