package org.lionpooh.sample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "websocket.influxdb")
public class InfluxDBProperties {
	private String url;
	private String user;
	private String password;
	private String database;
	private String retention_policy;
}
