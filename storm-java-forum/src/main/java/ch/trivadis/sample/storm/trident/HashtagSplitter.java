package ch.trivadis.sample.storm.trident;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import backtype.storm.tuple.Values;

public class HashtagSplitter extends BaseFunction {

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Status tweet = (Status)tuple.getValueByField("tweet");
		for (HashtagEntity hashtagEntity : tweet.getHashtagEntities()) {
			collector.emit(new Values(hashtagEntity.getText()));
		}
	}
}
