package com.buaa.financedata.sample.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;

public class Count{
	public static void main(String[] args){
		SparkConf conf = new SparkConf().setAppName("Count");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> input = sc.textFile("test/TAQ");
		System.out.println(input.count());
	}
}
