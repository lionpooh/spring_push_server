package org.lionpooh.sample;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.lionpooh.sample.domain.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InfluxDBScheduler {

	@Autowired
	private InfluxDB influxdb;

	@Autowired
	private WebSocketStompClient stompClient;

	private StompSession session;

	private WebSocketHttpHeaders headers;

	@PostConstruct
	public void init() {
		headers = new WebSocketHttpHeaders();
	}

	@Scheduled(initialDelayString = "3000", fixedDelayString = "3000")
	public void pushMetric() throws InterruptedException, ExecutionException {
		if (this.session == null || !this.session.isConnected()) {
			this.session = stompClient.connect("ws://localhost:8080/influxdb_websocket",
					this.headers, new StompSessionHandlerAdapter() {}).get();
		}
		// size limit 7000 with sockJs
		QueryResult queryResult =
				influxdb.query(new Query("select * from mem where time > now() - 15m", "telegraf"));
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		List<Memory> list = resultMapper.toPOJO(queryResult, Memory.class);
		
		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.setDestination("/test/metrics/memory");
		// stompHeaders.setContentLength(1500);
		session.send(stompHeaders, list);
		log.info("send...");
	}
	
	@PreDestroy
	public void dest()	{
		if(this.session.isConnected())	{
			session.disconnect();
		}
	}
}
