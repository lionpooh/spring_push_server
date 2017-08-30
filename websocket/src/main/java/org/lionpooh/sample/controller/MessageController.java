package org.lionpooh.sample.controller;

import java.util.List;

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
	public List<Memory> metric(List<Memory> memory)	{
		log.info(memory.toString());
		return memory;
	}
	
	/*@MessageMapping("/metrics")
	@SendTo("/influxdb/metrics")
	public String test(String name)	{
		log.info("name: {}", name);
		return "hello " + name;
	}*/
}
