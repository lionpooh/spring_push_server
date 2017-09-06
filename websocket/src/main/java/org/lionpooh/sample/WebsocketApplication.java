package org.lionpooh.sample;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import lombok.extern.slf4j.Slf4j;

/*
 * sockJs - withSocketJS()를 쓰면 데이터가 6500? 정도를 stompClient에서 못 받음 ... why?
 * 설정을 빼주면 정상적으로 받음
 */

@Slf4j
@SpringBootApplication
@EnableScheduling
public class WebsocketApplication implements CommandLineRunner {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(WebsocketApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		
		final AtomicReference<Throwable> failure = new AtomicReference<>();
		final CountDownLatch latch = new CountDownLatch(1);
		
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		
		SockJsClient sockJsClient = new SockJsClient(transports);
		
		//WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		StompSessionHandler handler = new TestSessionHandler(failure) {

			@Override
			public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
				StompHeaders stompHeaders = new StompHeaders();
				
				stompHeaders.setDestination("/influxdb/metrics");
				
				session.subscribe(stompHeaders, new StompFrameHandler() {
					@Override
					public Type getPayloadType(StompHeaders headers) {
						return List.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						//Greeting greeting = (Greeting) payload;
						log.info("headers - {}", headers.toString());
						try {
							//String result = greeting.getContent();
							//assertEquals("Hello, Spring!", greeting.getContent());
							//log.info("result - {}", result);
							log.info("result - {}", payload.toString());
						} catch (Throwable t) {
							//failure.set(t);
							t.printStackTrace();
							if(failure.get() != null)	{
								failure.get().printStackTrace();
							}
						} finally {
							//session.disconnect();
							//latch.countDown();
						}
					}
				});
			}
		};
		
		stompClient.connect("ws://localhost:8080/influxdb_websocket", headers, handler);
		//stompClient.getWebSocketClient().do
		latch.await();
	}
	
	private class TestSessionHandler extends StompSessionHandlerAdapter {

		private final AtomicReference<Throwable> failure;


		public TestSessionHandler(AtomicReference<Throwable> failure) {
			this.failure = failure;
		}

		@Override
		public void handleFrame(StompHeaders headers, Object payload) {
			this.failure.set(new Exception(headers.toString()));
		}

		@Override
		public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p,
				Throwable ex) {
			this.failure.set(ex);
		}

		@Override
		public void handleTransportError(StompSession session, Throwable ex) {
			this.failure.set(ex);
		}
	}

}
