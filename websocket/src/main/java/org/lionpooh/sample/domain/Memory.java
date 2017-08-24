package org.lionpooh.sample.domain;

import org.influxdb.annotation.Measurement;

import lombok.Data;

@Data
@Measurement(name = "mem")
public class Memory {

	private String host;
	private long active;
	private long avaliable;
	private double available_percent;
	private long buffered;
	private long cached;
	private long free;
	private long total;
	private long used;
	private double used_percent;
	
}
