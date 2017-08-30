package org.lionpooh.sample;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.lionpooh.sample.domain.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@DependsOn("simpleBrokerMessageHandler")
public class InfluxDBScheduler {

	@Autowired
	private InfluxDB influxdb;

	@Autowired
	private WebSocketStompClient stompClient;

	private StompSessionHandler handler;

	private StompSession session;

	@PostConstruct
	public void init() {
		// System.out.println("influxdbscheduler");
		/*
		 * stompClient.connect("ws://localhost:8080/influxdb_websocket", new
		 * StompSessionHandlerAdapter() {
		 * 
		 * });
		 */
	}

	@Scheduled(initialDelay = 3000, fixedDelay = 10000)
	public void pushMetric() throws InterruptedException, ExecutionException {
		if (this.session == null || !this.session.isConnected()) {
			this.session = stompClient
					.connect("ws://localhost:8080/influxdb_websocket", new StompSessionHandlerAdapter() {

						/*@Override
						public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
							session.subscribe("", new StompFrameHandler() {
								
								@Override
								public void handleFrame(StompHeaders arg0, Object arg1) {
									
								}
								
								@Override
								public Type getPayloadType(StompHeaders arg0) {
									return null;
								}
							});
						}*/
						
					}).get();
		}
		QueryResult queryResult = influxdb.query(new Query("select * from mem where time > now() - 15m", "telegraf"));
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		List<Memory> list = resultMapper.toPOJO(queryResult, Memory.class);
		//list.forEach(System.out::println);
		this.session.send("/test/metrics/memory", list);
		log.info("send...");
	}

	private class InfluxdbPushSessionHandler extends StompSessionHandlerAdapter {

	}
}
