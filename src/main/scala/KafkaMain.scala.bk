import com.typesafe.scalalogging.StrictLogging
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.util.Properties
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.regex.Pattern;

object KafkaMain extends StrictLogging {

  def main(args: Array[String]) = {
    val config = new Properties()
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "grp-k");
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

    val kc = new KafkaConsumer(config, new StringDeserializer(), new StringDeserializer())

    kc.subscribe(Pattern.compile("test"))
    while (true) {
      val recs = kc.poll(10);
      recs.forEach(rec => logger.info(s"${rec.key} ${rec.value}"));
    }
  }

}
