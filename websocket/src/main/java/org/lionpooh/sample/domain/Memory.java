package org.lionpooh.sample.domain;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.Data;

@Data
@Measurement(name = "mem")
public class Memory extends Metric{

	@Column(name = "time")
	private String time;
	@Column(name = "host")
	private String host;
	@Column(name = "active")
	private long active;
	@Column(name = "available")
	private long available;
	@Column(name = "available_percent")
	private double available_percent;
	@Column(name = "buffered")
	private long buffered;
	@Column(name = "cached")
	private long cached;
	@Column(name = "free")
	private long free;
	@Column(name = "total")
	private long total;
	@Column(name = "used")
	private long used;
	@Column(name = "used_percent")
	private double used_percent;
	
}
