package ch.trivadis.sample.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import ch.trivadis.sample.storm.bolt.HashtagCounterRedis;
import ch.trivadis.sample.storm.bolt.HashtagSplitter;
import ch.trivadis.sample.storm.spout.TwitterStreamingSpout;

public class TopologyRunner {

	public static StormTopology createTopology(String consumerKey,
			String consumerSecret, String token, String secret) {
		
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("tweet-stream", new TwitterStreamingSpout(
				consumerKey, consumerSecret, token, secret, new String[] { "#JFS2013" }), 1);
		//builder.setSpout("tweet-stream", new TwitterStreamingSpoutMock(), 1);
		
		
		builder.setBolt("hashtag-splitter", new HashtagSplitter(), 2)
				.shuffleGrouping("tweet-stream");
		
		//builder.setBolt("hashtag-filter", new HashtagWireTap(), 2).shuffleGrouping("hashtag-splitter"); 
		
		//builder.setBolt("hashtag-printer", new ConsoleWriterBolt(), 2)
		//	.fieldsGrouping("hashtag-filter", "filtered", new Fields("hashtag"));
		
		builder.setBolt("hashtag-counter", new HashtagCounterRedis("localhost",6379), 2)
				.fieldsGrouping("hashtag-splitter", new Fields("hashtag"));
		
		return builder.createTopology();
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		StormTopology topology = createTopology("2dYX9qPSedKkB1aDzKXeMg",
				"gEsVlaUxtfhGOfKBDBlwX9X81xqq7LgvINpoQgSp0",
				"852289452-HqMNv1tRivSmbbMC9ZYtWzTkDu7vlEe9rp3NWn1b",
				"4zed17zTNQJmINzkBGQP3EbEtBRPkY9L4U1hcuASshg");

		Config conf = new Config();
		conf.setDebug(true);

		conf.setMaxTaskParallelism(3);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("word-count", conf, topology);

		Thread.sleep(90000000);

		cluster.shutdown();
	}

}
