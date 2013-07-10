package ch.trivadis.sample.storm.trident;

import java.net.InetSocketAddress;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.redis.RedisState;
import storm.trident.state.StateFactory;
import storm.trident.testing.Split;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import ch.trivadis.sample.storm.spout.TwitterStreamingSpout;

public class TridentTopologyRunner {
	
	public static StormTopology createTopology(String consumerKey,
			String consumerSecret, String token, String secret) {

		StateFactory redis = RedisState.nonTransactional(new InetSocketAddress(
				"localhost", 6379));

		TwitterStreamingSpout spout = new TwitterStreamingSpout(consumerKey,consumerSecret, token, secret, 
																new String[] { "#JFS2013" });

		TridentTopology topology = new TridentTopology();
		TridentState wordCounts = topology.newStream("twitter-stream", spout)
				.parallelismHint(1)
				.each(new Fields("tweet"), new HashtagSplitter(), new Fields("hashtag"))
				.each(new Fields("hashtag"), new HashtagNormalizer(), new Fields("hashtagNormalized"))
				.groupBy(new Fields("hashtagNormalized"))
				.persistentAggregate(redis, new Count(), new Fields("count"))
				.parallelismHint(2);

		return topology.build();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StormTopology topology = createTopology("2dYX9qPSedKkB1aDzKXeMg",
				"gEsVlaUxtfhGOfKBDBlwX9X81xqq7LgvINpoQgSp0",
				"852289452-HqMNv1tRivSmbbMC9ZYtWzTkDu7vlEe9rp3NWn1b",
				"4zed17zTNQJmINzkBGQP3EbEtBRPkY9L4U1hcuASshg");
		
		LocalDRPC drpc = new LocalDRPC();
		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("tester", conf, topology);
	}

}
