package kz.narxoz.rabbit.middle02rabbitreceiver;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class Middle02rabbitreceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(Middle02rabbitreceiverApplication.class, args);
	}

}
