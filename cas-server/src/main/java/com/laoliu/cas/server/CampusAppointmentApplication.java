package com.laoliu.cas.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.laoliu.cas.**.mapper"})
public class CampusAppointmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusAppointmentApplication.class, args);
    }
}
