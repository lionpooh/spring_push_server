package org.lionpooh.sample.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.lionpooh.sample.domain.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class InfluxDBDao implements MetricDao{

	@Value("${influxdb.database}")
	private String database;
	
	@Autowired
	private InfluxDB influxdb;
	
	/**
	 * select metric from influxdb.
	 * @method name: selectMetric
	 * @param metric
	 * @return result of select query from influxdb
	 */
	@SuppressWarnings("unchecked")
	public <T extends Metric> List<T> selectMetric(T metric)	{
		Assert.notNull(metric, "metric must not null");
		QueryResult queryResult =
				influxdb.query(createQuery(metric));
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		List<T> list = (List<T>) resultMapper.toPOJO(queryResult, Metric.class);
		return list;
	}
	
	/**
	 * create query by metric value.
	 * @method name: createQuery
	 * @param metric
	 * @return created select query for influxdb measurements
	 */
	public <T extends Metric> Query createQuery(T metric)	{
		String query = "";
		String time = metric.getTime();
		String fields = "*";
		String measurements = "";
		List<String> fieldList = metric.getFields();
		List<String> measurementList = metric.getMeasurements();
		
		if(metric.getFields() != null || !metric.getFields().isEmpty())	{
			fields = StringUtils.join(fieldList.toArray(), ",");
		}
		Assert.notNull(measurementList, "measurement must not null");
		Assert.notEmpty(measurementList, "measurement must not empty");
		measurements = StringUtils.join(measurementList.toArray(), ",");
		
		query = "select " + fields + " from " + measurements + " where time > now() - " + time;
		System.out.println(query);
		Query influxdbQuery = new Query(query, this.database);
		return influxdbQuery;
	}
}
