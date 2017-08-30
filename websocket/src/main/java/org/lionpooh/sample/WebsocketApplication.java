package org.lionpooh.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class WebsocketApplication implements CommandLineRunner {

	@Autowired
	private WebSocketStompClient stompClient;
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(WebsocketApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
	}

}
