# Preparing environment

wget https://archive.cloudera.com/cm5/installer/latest/cloudera-manager-installer.bin




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

kafka-topics --create --zookeeper HOSTNAME:2181--replication-factor 1 --partitions 12 --topic general

kafka-console-consumer --zookeeper HOSTNAME:2181 --topic general --from-beginning

kafka-topics --delete --zookeeper localhost:2181 --topic general


# Find uniq line length

 cat /var/log/secure | awk -F'[ ]' '{print NF, $0}' | sort  | uniq
 
 [root@8ed7f ~]# grep Invalid /var/log/secure | awk -F'[ ]' '{print NF, $0}' | tail -n 3

10 Apr 23 23:52:04 8ed7f sshd[5750]: Invalid user admin from 37.229.172.66

10 Apr 23 23:54:33 8ed7f sshd[6442]: Invalid user developer from 150.95.143.209

10 Apr 24 00:11:01 8ed7f sshd[10910]: Invalid user 123 from 150.95.143.209

# Output

 62.210.192.216                                column=user:1234, timestamp=1492982635018, value=Apr 23 17:17:36                                                                      
 62.210.192.216                                column=user:admin, timestamp=1492982635018, value=Apr 23 17:18:22                                                                     
 62.210.192.216                                column=user:mobile, timestamp=1492982635018, value=Apr 23 17:18:35                                                                    
 62.210.192.216                                column=user:mother, timestamp=1492982635018, value=Apr 23 17:18:01                                                                    
 62.210.192.216                                column=user:nagios, timestamp=1492982635018, value=Apr 23 17:18:45                                                                    
 62.210.192.216                                column=user:pi, timestamp=1492982635018, value=Apr 23 17:17:08                                                                        
 62.210.192.216                                column=user:support, timestamp=1492982635018, value=Apr 23 17:16:36                                                                   
 62.210.192.216                                column=user:test, timestamp=1492982635018, value=Apr 23 17:19:05                                                                      
 62.210.192.216                                column=user:ubnt, timestamp=1492982635018, value=Apr 23 17:18:12                                                                      
 62.210.192.216                                column=user:user, timestamp=1492982635018, value=Apr 23 17:17:21                                                                      
 62.210.192.216                                column=user:uucp, timestamp=1492982635018, value=Apr 23 17:19:14                                                                      
 89.46.65.148                                  column=user:admin, timestamp=1492982635018, value=Apr 23 23:28:00                                                                     
 91.197.232.109                                column=user:0, timestamp=1492982635018, value=Apr 23 21:21:53                                                                         
 91.197.232.109                                column=user:0000, timestamp=1492982635018, value=Apr 23 21:21:55                                                                      
 91.197.232.109                                column=user:010101, timestamp=1492982635018, value=Apr 23 21:21:58                                                                    
 91.197.232.109                                column=user:1111, timestamp=1492982635018, value=Apr 23 21:22:00                                                                      
 91.197.232.109                                column=user:1234, timestamp=1492982635018, value=Apr 23 21:22:03                                                                      
 91.197.232.109                                column=user:admin, timestamp=1492982635018, value=Apr 23 21:22:57                                                                     
 91.197.232.109                                column=user:api, timestamp=1492982635018, value=Apr 23 21:23:01                                                                       
 91.197.232.109                                column=user:dbadmin, timestamp=1492982635018, value=Apr 23 21:23:04                                                                   
 91.197.232.109                                column=user:default, timestamp=1492982635018, value=Apr 23 21:23:09                                                                   
 91.197.232.109                                column=user:ftpuser, timestamp=1492982635018, value=Apr 23 21:23:18                                                                   
 91.197.232.109                                column=user:git, timestamp=1492982635018, value=Apr 23 21:23:20                                                                       
 91.197.232.109                                column=user:gpadmin, timestamp=1492982635018, value=Apr 23 21:23:24                                                                   
 91.197.232.109                                column=user:guest, timestamp=1492982635018, value=Apr 23 21:23:27                                                                     
 91.197.232.109                                column=user:monitor, timestamp=1492982635018, value=Apr 23 21:23:29                                                                   
 91.197.232.109                                column=user:mysql, timestamp=1492982635018, value=Apr 23 21:23:35                                                                     
 91.197.232.109                                column=user:osmc, timestamp=1492982635018, value=Apr 23 21:23:40                                                                      
 91.197.232.109                                column=user:pi, timestamp=1492982635018, value=Apr 23 21:23:43                                                                        
 91.197.232.109                                column=user:service, timestamp=1492982635018, value=Apr 23 21:24:06                                                                   
 91.197.232.109                                column=user:support, timestamp=1492982635018, value=Apr 23 21:24:10                                                                   
 91.197.232.109                                column=user:sysadmin, timestamp=1492982635018, value=Apr 23 21:24:11                                                                  
 91.197.232.109                                column=user:telecomadmin, timestamp=1492982635018, value=Apr 23 21:24:14                                                              
 91.197.232.109                                column=user:telnet, timestamp=1492982635018, value=Apr 23 21:24:17                                                                    
 91.197.232.109                                column=user:test, timestamp=1492982635018, value=Apr 23 21:24:20                                                                      
 91.197.232.109                                column=user:ubnt, timestamp=1492982635018, value=Apr 23 21:24:22                                                                      
 91.197.232.109                                column=user:user, timestamp=1492982635018, value=Apr 23 21:24:25                                    
