package com.hhly.lottosplit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
@MapperScan("com.hhly.lottosplit.mapper")
@Configuration
public class SplitApplication{
	public static void main(String[] args) {
        SpringApplication.run(SplitApplication.class, args);
    }
}