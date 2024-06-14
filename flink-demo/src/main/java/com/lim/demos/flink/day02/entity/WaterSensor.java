package com.lim.demos.flink.day02.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WaterSensor
 *
 * <p>水位传感器：用于接收水位数据</p>
 * id:传感器编号
 * ts:时间戳
 * vc:水位
 *
 * @author lzc
 * @since 2020/12/8 22:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterSensor {

    /** 传感器编号 */
    private String id;

    /** 时间戳 */
    private Long ts;

    /** 水位 */
    private Integer vc;

}