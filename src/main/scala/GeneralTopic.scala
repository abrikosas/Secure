/**
  * * @param args(0)        - Hbase table name
  * * @param args(1)        - Hbase table coulumn family name

  */





import java.io.IOException

import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming
import org.apache.log4j.{Level, Logger, PropertyConfigurator}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkException}
import org.apache.hadoop.hbase.spark.HBaseContext
import org.apache.kafka.clients.consumer.ConsumerConfig



object DirectKafkaWordCount {


  def main(args: Array[String]): Unit = {


    val log = Logger.getLogger(getClass.getName)
    PropertyConfigurator.configure("log4j.properties")
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    //val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount")
    val sc = new SparkContext(new SparkConf().setAppName("DirectKafkaWordCount"))
    val tableName = args(0)
    val conf = HBaseConfiguration.create()
    conf.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    val jobConfig: JobConf = new JobConf(conf, this.getClass)
    jobConfig.set("mapreduce.output.fileoutputformat.outputdir", "/tmp/out")
    jobConfig.setOutputFormat(classOf[TableOutputFormat])
    jobConfig.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    val ssc = new StreamingContext(sc, Seconds(2))
    //ssc.checkpoint("/tmp/pipi")
    val pollTimeout = "1000"
    val offsetReset = "earliest"

   val hbaseContext = new HBaseContext(sc,conf )

    try {

      log.info("Connecting to broker list")
      val kafkaParams = Map[String, String]("metadata.broker.list" -> "fmak.lt:9092,94.176.235.138:9092",
                                            "zookeeper.connect" -> "fmak.lt:2181,94.176.235.138:2181"


       )

      val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, Set("general"))

      val messagesLength10: DStream[InvalidUserAttack] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
        ssc, kafkaParams, Set("general")).map(_._2).filter(_.contains("Invalid")).map(_.split(" ")).filter(_.length == 10).map(HbaseRecord.parseEvent)


       messagesLength10.print()

       messagesLength10.foreachRDD{rdd =>
         println("Writing to hbase table "+tableName)
         rdd.foreach(println)
         rdd.map(HbaseRecord.convertToPut(_,args(1))).saveAsHadoopDataset(jobConfig)

       }

      /*val messagesLength16= KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
        ssc, kafkaParams, Set("general")).map(_._2).filter(_.contains("Failed")).map(_.split(" ")).filter(_.length == 16).
        map(HbaseRecordFailed.parseEvent)
*/
      val messagesLength16= KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
        ssc, kafkaParams, Set("general")).map(_._2).map(_.split(" ")).filter(_.length == 16).
        map(HbaseRecordFailed.parseEvent)


      messagesLength16.print()

      messagesLength16.foreachRDD{ rdd =>

        rdd.foreach(println)

        rdd.map(HbaseRecordFailed.convertToPut(_,args(1))).saveAsHadoopDataset(jobConfig)

      }


   /*   val lines = messages.map(_._2)
      val msgLength: DStream[String] = lines.flatMap(_.split(" "))
      println(msgLength)
      val words: DStream[String] = lines.flatMap(_.split(" "))
      val wordCounts: DStream[(String, Long)] = words.map(x => (x, 1L)).reduceByKey(_ + _)
      wordCounts.print()
*/
      ssc.start()
      ssc.awaitTermination()
    } catch {
      case e: IOException => {
        log.error(e.getStackTraceString)
        ssc.stop()
      }
      case other: NumberFormatException => {
        log.error(other.getStackTraceString)
      }
      case e: Exception => {
        log.error(e.getStackTraceString)
        ssc.stop()
      }
      case h: SparkException => {
        log.error(h.getStackTraceString)
        h.getStackTraceString
        ssc.stop()
      }
    }
  }
}