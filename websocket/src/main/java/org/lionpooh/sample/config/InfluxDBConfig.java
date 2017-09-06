package org.lionpooh.sample.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.lionpooh.sample.properties.InfluxDBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InfluxDBProperties.class)
public class InfluxDBConfig {

	@Autowired
	private InfluxDBProperties influxdbProperties;
	
	@Bean
	public InfluxDB createInfluxdbConnection()	{
		InfluxDB influxdb = InfluxDBFactory.connect(influxdbProperties.getUrl());
		return influxdb;
	}
	
}
