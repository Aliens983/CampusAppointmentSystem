package com.laoliu.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author 25516
 */
@MapperScan("com.laoliu.system.mapper")
@SpringBootApplication
@EnableAsync
public class CampusAppointmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusAppointmentSystemApplication.class, args);
    }

}
