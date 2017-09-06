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
	
	@MessageMapping("/metrics/memory")
	@SendTo("/influxdb/metrics")
	public List<Memory> metric(List<Memory> memory)	{
		log.info(memory.toString());
		return memory;
	}

}
