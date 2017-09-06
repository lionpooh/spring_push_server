package org.lionpooh.sample.dao;

import java.util.List;

public interface MetricDao {
	public <T> List<T> selectMetric(T metric);
}
