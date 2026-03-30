package com.laoliu.system.vo.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 25516
 */
@Data
public class BookResultResponse {

    private String username;
    private String email;
    private String grade;

    private List<Map<String, Object>> allBookedServices;


}