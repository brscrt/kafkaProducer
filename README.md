# KafkaProducer
This project reads sample data from a given .csv file, maps data with selected key and values and produces output via Apache Kafka.
This project is a part of [this project](https://github.com/brscrt/Volume-Weighted-Average-Price).

## Add Apache Kafka library to project
Apache Kafka library should be added as dependency. 

```sh
compile group: 'org.apache.kafka', name: 'kafka_2.11', version: '0.10.0.0' 
```

## Data file
For sample input data, [data.csv](data/data.csv) file was used.

## Apache Kafka properties
A config file must be defined to comminicate kafka service. In this project, myproperties.properties file was used as shown below.

```sh
bootstrap.servers=172.17.0.3:9092
acks=all
retries=0
batch.size=16384
linger.ms=1
buffer.memory=33554432
key.serializer=org.apache.kafka.common.serialization.StringSerializer
value.serializer=org.apache.kafka.common.serialization.StringSerializer
```
### Kafka in Docker
The kafka service running in docker was used in this project. Before run kafka, zookeper should be run. In this project, kafka and zookeeper images were pulled as shown below.

#### Usage of zookeeper
```
docker pull jplock/zookeeper
docker run -d -p 2181:2181 --name zookeeper jplock/zookeeper
```
Zookeeper was started as daemon (-d) and manually assigned port (-p) 2181:2181.

#### Usage of kafka
```
docker pull ches/kafka
docker run -d --name kafka --link zookeeper:zookeeper ches/kafka
```
By this way Kafka service was started and linked to the running zookeeper.

## Producer class
```java
import java.util.ArrayList;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import util.MyCsv;
import util.MyProperties;

public class MyProducer {

	public static void main(String[] args) {
		System.out.println("It's started!");
		MyProperties myProperties = new MyProperties();
		myProperties.loadProperties("conf/myproducer.properties");
		Producer<String, String> producer = new KafkaProducer<>(myProperties);

		Map<String, ArrayList<String>> map = MyCsv.readAndMap("data/data.csv", 1);

		try {
			map.forEach((key, value) -> {				
				value.forEach(sub -> {				
					producer.send(new ProducerRecord<String, String>(key, sub));
				});
			});
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			producer.close();
		}
		System.out.println("It's finished!");
	}
	
}
```
First it loads the properties file that we specify.
```java
myProperties.loadProperties("conf/myproducer.properties");
```
Then we create KafkaProducer object with myProperties.
```java
Producer<String, String> producer = new KafkaProducer<>(myProperties);
```
Through MyCsv class, it reads the csv file and maps it as key and values.
```java
Map<String, ArrayList<String>> map = MyCsv.readAndMap("data/data.csv", 1);
```
The second parameter "1", shows which column index will be key. For example:
```csv
Tid	Code	SerialCode	ShortMarketCode	StockGroup
0	 TEKTU	E	          N	              A
0	 TEKTU	E	          N	              A
481	GUBRF	E	          N	              A
481	GUBRF	E	          N	              A
481	GUBRF	E	          N	              A
```
"Code" column will be key. And we will have 2 different keys as "TEKTU" and "GUBRF"
