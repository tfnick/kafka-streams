package bbejeck.streams.metrics;

import bbejeck.model.*;
import bbejeck.serializer.JsonDeserializer;
import bbejeck.serializer.JsonSerializer;
import bbejeck.utils.SerdesFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

import java.util.Properties;

public class MetricMonitorStreamsDriver {

    public static void main(String[] args) {

        StreamsConfig streamsConfig = new StreamsConfig(getProperties());
        KStreamBuilder builder = new KStreamBuilder();

        //source
        KStream<String,Metric> metricStream = builder
                .stream(Serdes.String(),SerdesFactory.serdFrom(Metric.class),"src-metric");
        KTable<String, MetricUser> metricUserTable = builder
                .table(Serdes.String(), SerdesFactory.serdFrom(MetricUser.class), "src-metric-user", "metric-user-store");

        //handle
        KStream<String, MetricUserPattern> patternStream = metricStream
                .leftJoin(metricUserTable,(Metric m, MetricUser mu) -> MetricUserStatus.fromMetricAndMetricUser(m,mu),Serdes.String(), SerdesFactory.serdFrom(Metric.class))
                .mapValues((MetricUserStatus mus) -> MetricUserPattern.fromMetricUserStatus(mus))
                .filter((String item,MetricUserPattern mup) -> "crit".equals(mup.getLevel()));

        //sink
        patternStream.to("out-metric-user-pattern");

        System.out.println("Starting MetricStreams Example");
        KafkaStreams kafkaStreams = new KafkaStreams(builder,streamsConfig);
        kafkaStreams.start();
        System.out.println("Now started MetricStreams Example");

    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "Metric-Monitor-Job");
        props.put("group.id", "streams-metrics");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "metric-streams-api");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        return props;
    }
}
