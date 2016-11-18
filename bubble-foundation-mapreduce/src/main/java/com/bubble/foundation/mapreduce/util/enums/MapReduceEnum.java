package com.bubble.foundation.mapreduce.util.enums;

public enum MapReduceEnum {

	HBASE, HIVE, HDFS;

	private MapReduceEnum() {
	}

	private MapReduceEnum(String destination) {
		this.destination = destination;
	}

	private String destination;

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}
