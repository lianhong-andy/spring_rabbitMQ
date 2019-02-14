package com.andy;


import com.andy.domain.Spittle;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class ProducerMain {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("/amqp-share.xml");
        AmqpTemplate template = (AmqpTemplate) context.getBean("amqpTemplate");
        for (int i = 0; i < 20; i++) {
            System.out.println("Sending message #" + i);
            Spittle spittle = new Spittle((long) i, "Hello world (" + i + ")", new Date());
            template.convertAndSend(spittle);
            Thread.sleep(5000);
        }
        System.out.println("Done!");
    }
}
