package org.lionpooh.sample.domain;

import java.util.List;

import lombok.Data;

@Data
public class Metric {
	
	private List<String> fields;
	private List<String> measurements;
	private String time;
	
}
