package org.lionpooh.sample.dao;

import java.util.List;

import org.lionpooh.sample.domain.Metric;

public interface MetricDao {
	public <T extends Metric> List<T> selectMetric(T metric);
}
