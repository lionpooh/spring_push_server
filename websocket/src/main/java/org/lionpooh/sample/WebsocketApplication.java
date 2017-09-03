package org.lionpooh.sample;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.lionpooh.sample.domain.Memory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class WebsocketApplication implements CommandLineRunner {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(WebsocketApplication.class, args);
		final CountDownLatch latch = new CountDownLatch(1);
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		SockJsClient sockJsClient = new SockJsClient(transports);
		
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		stompClient.setInboundMessageSizeLimit(10000);
		
		stompClient.connect("ws://localhost:8080/influxdb_websocket", new WebSocketHttpHeaders(), new StompSessionHandlerAdapter() {

			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				session.subscribe("/influxdb/metrics", new StompFrameHandler() {
					
					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						log.info("subscribing...");
						List<Memory> list = (List) payload;
						log.info(list.toString());
					}
					
					@Override
					public Type getPayloadType(StompHeaders headers) {
						return Object.class;
					}
				});
			}
			
		});
		latch.await();
	}

	@Override
	public void run(String... args) throws Exception {
		
	}

}
