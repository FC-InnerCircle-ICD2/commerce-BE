package com.emotionalcart;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CommonApplication {

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

}