## KAFKA常用SHELL命令

ZK_IP = 172.18.0.2
BOOT_IP = 127.0.0.1

> 注意：Kafka 从 2.2 版本开始将 kafka-topic.sh 脚本中的 −−zookeeper 参数标注为 “过时”，推荐使用 **−−bootstrap-server** 参数。若读者依旧使用的是 2.1 及以下版本，请将下述的 --bootstrap-server 参数及其值手动替换为 --zookeeper zk1:2181,zk2:2181,zk:2181。一定要注意两者参数值所指向的集群地址是不同的。

> 使用 −−bootstrap-server 的好处是，不必关心kafka之外的配置信息，更方便开发阶段调适，特别是容器化部署时，不需要关心容器化的ZK的IP，非常方便

进入 $KAFKA_HOME,执行相关命令

list topics

```shell

./bin/kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --list

```


create topic 

```shell

./bin/kafka-topics.sh --create --bootstrap-server 127.0.0.1:9092 --replication-factor 1 --partitions 1 --topic src-metric

```


topic detail

```shell

./bin/kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --topic test1 --describe

```




# kafka-streams
This is the repository for the examples of using Kafka streams covered in the blog posts: 

 *   [Kafka Streams - The Processor API](http://codingjunkie.net/kafka-processor-part1/)
 *   [Kafka Streams - The KStreams API](http://codingjunkie.net/kafka-streams-part2/)
 *   [Machine Learning with Kafka Streams](http://codingjunkie.net/kafka-streams-machine-learning/)


## Requirements to build this project

1.    Java 8
2.    Gradle

## Requirements to run the examples

1.    [kafka](https://github.com/apache/kafka) version kafka_2.11-0.10.1.0 see the section marked "Running a task on a particular version of Scala"
2.    The [json-data-generator](https://github.com/acesinc/json-data-generator) from [ACES,Inc](http://acesinc.net/) 


## Setup Instructions

### 容器化进行部署kafka

- 创建 docker-compose.yml

```yml
version: "3"
services:
  zookeeper:
    image: 'bitnami/zookeeper:3.4.10-r4'
    ports:
      - '2181:2181'
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    image: 'bitnami/kafka:0.10.2.1'
    ports:
      - '9092:9092'
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_LISTENERS=PLAINTEXT://:9092
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092
      - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
    depends_on:
      - zookeeper
```

- 拉取镜像并启动容器(需在docker-compose.yml所在目录下执行)

```shell
docker-compose up
```

- 停止容器(需在docker-compose.yml所在目录下执行)

```shell
docker-compose stop
```

- 启动容器(需在docker-compose.yml所在目录下执行)

```shell
docker-compose start
```

- 查看kafka默认配置

/opt/bitnami/kafka/config/server.properties

```properties
# important on cluster mode  #
broker.id=1
listeners=PLAINTEXT://1.2.1.173:9093
num.network.threads=32
num.io.threads=8
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
socket.request.max.bytes=104857600
log.dirs=/opt/appdata/kafka-data
# default partitions for new topic #
num.partitions=1
num.recovery.threads.per.data.dir=1
log.retention.hours=120
log.retention.bytes=128849018880
log.segment.bytes=1073741824
log.retention.check.interval.ms=300000
zookeeper.connect=zk1:2181,zk2:2181,zk3:2181/kafka
zookeeper.connection.timeout.ms=6000
delete.topic.enable=true
# allow to create topic if not exist #
auto.create.topics.enable=false
```

> 查看容器化ZK的IP

显示运行的容器

```shell
docker ps
```

获取ZK容器id

```shell
docker inspect ZK容器ID
```

进入Kafka容器

```shell
docker exec -it Kafka容器ID bash
```


### 非容器化部署kafka


#### Extact the kafka_2.11-0.10.1.0.tgz file ####
    tar -xvzf kafka_2.11-0.10.1.0.tgz


#### Start zookeeper and kafka
```
      kafka-install-dir/bin/zookeeper-server-start.sh kafka-install-dir/conf/zookeeper.properties
      kafka-install-dir/bin/kafka-server-start.sh kafka-install-dir/conf/server.properties
```

#### Install the Json-Data-Generator  
Download the latest [json-data-generator release](https://github.com/acesinc/json-data-generator/releases) and follow the install instructions [here](http://acesinc.net/introducing-a-streaming-json-data-generator/)

#### Setup the kafka-streams repo
Clone or fork the repo
```
     git clone git@github.com:bbejeck/kafka-streams    
     cd kafka-streams
```     
Then copy the json config files to json generator conf directory
```
    cp streaming-workflows/* <dir>/json-data-generator-1.2.0/conf
```    
     

### Running the Purchase Processor API KStreams API Examples ###
     cd <dir>/json-data-generator-1.2.0/
     java -jar json-data-generator-1.2.0 purchases-config.json
     cd kafka-streams
    
     # Low Level API Processor
     ./gradlew runPurchaseProcessor 
     # Streams API
     ./gradlew runPurchaseStreams 
     

### Running the Stock Trades Processor API or KStreams API Examples ###
     cd <dir>/json-data-generator-1.2.0/
     java -jar json-data-generator-1.2.0 stock-transactions-config.json
     cd kafka-streams
     ./gradlew runStockProcessor 
     
### Running the Twitter KStreams Language Classification Example ###
    rename src/main/resources/twitter-app.properties.template to twitter-app.properties 
    fill out the properties file with all the required values
    
    cd kafka-streams
    ./gradlew runTwitterKstreamNLP 

### Viewing the results of the purchase streaming examples ###
    cd kafka_install-dir/bin
    ./kafka-console-consumer.sh --topic [patterns|rewards|purchases] --bootstrap-server 127.0.0.1:9092

    [] ： 表示多个Topic选择其一,比如 patterns

    ./kafka-console-consumer.sh --topic patterns --bootstrap-server 127.0.0.1:9092

     
### Viewing the results of the stock-trading streaming examples ###
    cd kafka_install-dir/bin
    ./kafka-console-consumer.sh --topic [stocks-out|transaction-summary] --bootstrap-server 127.0.0.1:9092

    [] ： 表示多个Topic选择其一
    
### Viewing the results of the Twitter KStreams Language Classification Example ###
    cd kafka_install-dir/bin
    ./kafka-console-consumer.sh --topic [english|french|spanish] --bootstrap-server 127.0.0.1:9092

    [] ： 表示多个Topic选择其一
          
