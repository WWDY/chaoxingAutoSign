package com.daiju.cloudsign.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.Calendar;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @Author WDY
 * @Date 2021-01-27 10:09
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronAndCalendar implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cron;
    private Calendar calendar;
    private ScheduledTask scheduledTask;
}
