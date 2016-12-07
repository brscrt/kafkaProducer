package kafka;

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
