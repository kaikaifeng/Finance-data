package com.buaa.financedata.sample.scala

import org.apache.spark._ 
import org.apache.spark.SparkContext._

object Count{
	def main(args: Array[String]){
		val conf = new SparkConf().setAppName("Count")
		val sc = new SparkContext(conf)
		val rdd = sc.textFile("test/TAQ")
		println(rdd.count())
	}
}
