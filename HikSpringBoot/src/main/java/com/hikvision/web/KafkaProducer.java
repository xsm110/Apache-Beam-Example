package com.hikvision.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.google.gson.Gson;
import com.hikvision.entity.AlarmTable;
import com.hikvision.entity.DateUtil;

import java.util.Date;
import java.util.UUID;

/**
 * 生产者
 * 使用@EnableScheduling注解开启定时任务
 */
@Component
@EnableScheduling
public class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    

    /**
     * 定时任务
     */
    @Scheduled(cron = "00/1 * * * * ?")
    public void send(){
        Gson gon=new Gson();
        
        String message = UUID.randomUUID().toString();
       
        AlarmTable moAlarmTable=new AlarmTable();
        moAlarmTable.setAlarmid(message);
        moAlarmTable.setAlarmTitle("0005防区检测设备出现异常");
        moAlarmTable.setAlarmSource(371424005);
        moAlarmTable.setAlarmMsg("您好！收到一条设备异常报警信息请进行处理。");
        moAlarmTable.setDeviceModel("后端NVR");
       
        String jsonString=gon.toJson(moAlarmTable);
        ListenableFuture future = kafkaTemplate.send("TopicAlarm", jsonString);
        future.addCallback(o -> System.out.println("send-消息发送成功：" + message), throwable -> System.out.println("消息发送失败：" + message));
    }

}