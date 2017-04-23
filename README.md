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
       
scan table

      scan 'logins:invalids'
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


# Find uniq line length

 cat /var/log/secure | awk -F'[ ]' '{print NF, $0}' | sort  | uniq
 
 [root@8ed7f ~]# grep Invalid /var/log/secure | awk -F'[ ]' '{print NF, $0}' | tail -n 3

10 Apr 23 23:52:04 8ed7f sshd[5750]: Invalid user admin from 37.229.172.66

10 Apr 23 23:54:33 8ed7f sshd[6442]: Invalid user developer from 150.95.143.209

10 Apr 24 00:11:01 8ed7f sshd[10910]: Invalid user 123 from 150.95.143.209

