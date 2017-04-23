# Secure


spark-submit --master local[4]  --class DirectKafkaWordCount  target/Secure-1.0-SNAPSHOT-jar-with-dependencies.jar

spark-submit --driver-class-path `hbase classpath`  --files /etc/hbase/conf/core-site.xml --master local[4]  --class DirectKafkaWordCount  target/Secure-1.0-SNAPSHOT-jar-with-dependencies.jar logins:invalids user 

# Hbase stuff

create namespace

      create_namespace 'logins'

create table
      
       create 'logins:invalids','user'
       
disable table
       disable 'logins:invalids'
       
delete table
       drop 'logins:invalids'
# Flume

 tier1.sources  = source1
 tier1.channels = channel1
 tier1.sinks = sink1
 
 tier1.sources.source1.type = exec
 tier1.sources.source1.command = /usr/bin/tail -F /var/log/secure
 tier1.sources.source1.channels = channel1
 
 tier1.channels.channel1.type = memory
 tier1.channels.channel1.capacity = 10000
 tier1.channels.channel1.transactionCapacity = 10000
 
 tier1.sinks.sink1.type = org.apache.flume.sink.kafka.KafkaSink
 tier1.sinks.sink1.topic = general
 tier1.sinks.sink1.brokerList = HOSTNAME:9092
 tier1.sinks.sink1.channel = channel1
 tier1.sinks.sink1.batchSize = 20
       
# Kakfa

kafka-topics --create --zookeeper HOSTNAME:2181--replication-factor 1 --partitions 1 --topic general
