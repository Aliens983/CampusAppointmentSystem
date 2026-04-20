package com.laoliu.system.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 天气响应VO
 * 
 * @author System
 * @since 2026-04-13
 */
@Data
public class WeatherResponse {

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 国家
     */
    private String guo;

    /**
     * 省份
     */
    private String sheng;

    /**
     * 城市
     */
    private String shi;

    /**
     * 区县
     */
    private String qu;

    /**
     * 天气名称
     */
    private String name;

    /**
     * 当前天气
     */
    private String weather1;

    /**
     * 当前天气图标
     */
    private String weather1img;

    /**
     * 当前温度
     */
    private String wd1;

    /**
     * 当前风向
     */
    private String winddirection1;

    /**
     * 当前风力
     */
    private String windleve1;

    /**
     * 未来天气
     */
    private String weather2;

    /**
     * 未来天气图标
     */
    private String weather2img;

    /**
     * 未来温度
     */
    private String wd2;

    /**
     * 未来风向
     */
    private String winddirection2;

    /**
     * 未来风力
     */
    private String windleve2;

    /**
     * 经度
     */
    private String lon;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 更新时间
     */
    private String uptime;

    /**
     * 实时天气信息
     */
    @JsonProperty("nowinfo")
    private NowInfo nowInfo;

    /**
     * 预警信息
     */
    @JsonProperty("alarm")
    private Alarm[] alarms;

    /**
     * 消息描述
     */
    private String message;

    /**
     * 实时天气信息内部类
     */
    @Data
    public static class NowInfo {
        /**
         * 天气
         */
        private String weather;

        /**
         * 温度
         */
        private String temp;

        /**
         * 体感温度
         */
        private String feel_temp;

        /**
         * 湿度
         */
        private String humidity;

        /**
         * 风向
         */
        private String wind_direction;

        /**
         * 风力
         */
        private String wind_speed;

        /**
         * 降水
         */
        private String precipitation;

        /**
         * 能见度
         */
        private String visibility;

        /**
         * 气压
         */
        private String pressure;

        /**
         * 露点温度
         */
        private String dew_point;
    }

    /**
     * 预警信息内部类
     */
    @Data
    public static class Alarm {
        /**
         * 预警类型
         */
        private String alarm_type;

        /**
         * 预警级别
         */
        private String alarm_level;

        /**
         * 预警标题
         */
        private String title;

        /**
         * 预警内容
         */
        private String content;

        /**
         * 发布时间
         */
        private String issue_time;

        /**
         * 解除时间
         */
        private String cancel_time;
    }
}