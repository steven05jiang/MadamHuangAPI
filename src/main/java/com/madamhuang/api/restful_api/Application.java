package com.madamhuang.api.restful_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Create by Wei Jiang
 */
@EnableAutoConfiguration(
        exclude = { DataSourceAutoConfiguration.class
              , HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class
 /**  **/
        })
@ComponentScan(basePackages = {
		"com.madamhuang.api.restful_api",
		"com.madamhuang.api.restful_api.model",
		"com.madamhuang.api.restful_api.security",
		"com.madamhuang.api.restful_api.controller",
		"com.madamhuang.api.restful_api.util"
})

@EnableTransactionManagement
@SpringBootApplication
public class Application {

    public static void main(String[] args) {SpringApplication.run(Application.class, args); }

}

