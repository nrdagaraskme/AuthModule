package com.locus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Created by dhruv on 24/06/16.
 */


@SpringBootApplication
@ComponentScan
@Configuration
public class AppLauncher {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
        SpringApplication.run(AppLauncher.class, args);
    }


}
