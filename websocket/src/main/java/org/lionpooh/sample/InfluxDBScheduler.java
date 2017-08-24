package org.lionpooh.sample;

import javax.annotation.PostConstruct;

import org.influxdb.InfluxDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class InfluxDBScheduler {

	@Autowired
	private InfluxDB influxdb;
	
	@Autowired
	private WebSocketStompClient stompClient;
	
	private StompSessionHandler handler;
	
	@PostConstruct
	public void init()	{
		this.handler = new InfluxdbPushSessionHandler()	{

			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				// TODO Auto-generated method stub
				super.afterConnected(session, connectedHeaders);
			}
			
		};
	}
	
	@Scheduled(initialDelay = 3000, fixedDelay = 10000)
	public void pushMetric()	{
		
	}
	
	private class InfluxdbPushSessionHandler extends StompSessionHandlerAdapter{
		
	}
}

