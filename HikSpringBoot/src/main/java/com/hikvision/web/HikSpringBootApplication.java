package com.hikvision.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@Controller
@SpringBootApplication
@MapperScan("com.hikvision.dao")
@ComponentScan(basePackages = { "com.hikvision.service", "com.hikvision.web" })
public class HikSpringBootApplication extends SpringBootServletInitializer {

	@RequestMapping("/")
	public String index() {
		System.out.println("启动了");
		return "socket.html";
	}

	public static void main(String[] args) {
		SpringApplication.run(HikSpringBootApplication.class, args);
	}
}
