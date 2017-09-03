package org.lionpooh.sample.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.GenericMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Bean
	public SockJsClient sockJsClient() {
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		SockJsClient sockJsClient = new SockJsClient(transports);
		return sockJsClient;
	}

	@Bean
	public WebSocketStompClient stompClient() {
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient());
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		//stompClient.setMessageConverter(new GenericMessageConverter());
		//stompClient.setMessageConverter(new StringMessageConverter());
		return stompClient;
	}

	@Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(30000);
        container.setMaxBinaryMessageBufferSize(30000);
        return container;
    }
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes("/test");
		config.enableSimpleBroker("/influxdb");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/influxdb_websocket").withSockJS();
	}

}
