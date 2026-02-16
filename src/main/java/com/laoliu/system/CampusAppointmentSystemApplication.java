package com.laoliu.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 25516
 */
@MapperScan("com.laoliu.system.mapper")
@SpringBootApplication
public class CampusAppointmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusAppointmentSystemApplication.class, args);
    }

}
