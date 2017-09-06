package org.lionpooh.sample.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "lionpooh.server")
public class ServerProperties {
	private String initialDelayString;
	private String fixedDelayString;
}
