package org.lionpooh.sample.controller;

import java.util.HashMap;
import java.util.Map;

import org.lionpooh.sample.domain.Memory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MessageController {

	/*@MessageMapping("/hello")
	@SendTo("/topic/roomId")
	public ClientMessage broadcasting(ClientMessage message) throws Exception	{
		//return new Message(message.getContents());
		//connect
		//System.out.println(message.getContents());
		
		return message;
	}*/
	
	/*@MessageMapping("bye")
	@SendTo("/topic/bye")
	public ClientMessage bye(ClientMessage message) throws Exception	{
		//disconnect
		//System.out.println(message.getContents());
		return message;
	}*/
	
	@MessageMapping("/metrics/memory")
	@SendTo("/influxdb/metrics")
	public Map<String, Object> metric(Memory memory)	{
		Map<String, Object> metricSet = new HashMap<>();
		metricSet.put("memory", memory);
		
		log.info(metricSet.toString());
		
		return metricSet;
	}
	
}
