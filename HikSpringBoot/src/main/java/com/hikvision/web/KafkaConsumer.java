package com.hikvision.web;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * 消费者 使用@KafkaListener注解,可以指定:主题,分区,消费组
 */
@Component
public class KafkaConsumer {
	private static Logger log = LoggerFactory.getLogger(UserController.class);

//	@KafkaListener(topics = { "testmsg" })
//	public void receive(String message) {
//		System.out.println("消费消息:" + message + "\n");
//			try {
//
//		
//			WebSocket.sendMessage(message);
//			
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		  log.error("接收kafka消息出现异常：--------------------------\r\n"+e.getMessage()+"--------------------\r\n");
//		}
//
//	}
}