package org.example.ingsoft.kata.bizzbuzz.fizzbuzz;


import javax.xml.transform.Result;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;

public class FizzBuzz {
    public static final Integer MIN_VALUE = 0;
    public static final Integer MAX_VALUE = 100;
    public static final Integer FIZZ_NUMBER = 3;
    public static final String FIZZ = "Fizz";
    public static final Integer BUZZ_NUMBER = 5;
    public static final String BUZZ = "Buzz";
    public static final String FIZZBUZZ = "FizzBuzz";

    public String calculate(int number){


        if(number < MIN_VALUE || number > MAX_VALUE){
            throw new IllegalArgumentException("el numero debe ser mayor a 0");
        }

        String result = "";
        if(number % FIZZ_NUMBER !=0 && number % BUZZ_NUMBER !=0){
            return Integer.toString(number);
        }
        if(number % FIZZ_NUMBER == 0){
            result = FIZZ;
        }
        if(number % BUZZ_NUMBER == 0){
            result = BUZZ;
        }
        if(number % FIZZ_NUMBER ==0 && number % BUZZ_NUMBER ==0){
            result = FIZZBUZZ;
        }
        return result;

        //throw new RuntimeException("No Implementar");

    }
    public void print(){
        for (int i = 0; i <= MAX_VALUE ; i++){
            System.out.print(calculate(i));
        }
    }

}
