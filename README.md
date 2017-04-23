# Secure


spark-submit --master local[4]  --class DirectKafkaWordCount  target/Secure-1.0-SNAPSHOT-jar-with-dependencies.jar

# Hbase stuff

create namespace

      create_namespace 'logins'

create table
      
       create 'logins:invalids','event user'
