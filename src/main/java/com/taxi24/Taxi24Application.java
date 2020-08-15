package com.taxi24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class Taxi24Application {

    public static void main(String[] args) {
        SpringApplication.run(Taxi24Application.class, args);
    }

}
