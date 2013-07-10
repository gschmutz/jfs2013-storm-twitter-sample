jfs2013-storm-twitter-sample
============================

The Storm sample shown during my talk at Java Forum Stuttgart 2013

## Features
* subscribes to the Twitter Filter Streaming API and filters on a given term (hashtag)
* counts the hashtags in the tweet in a Redis database
* displays the counts through a bar chart using D3.js

### Quick Start Example

To run the filter stream example, navigate to the storm-java-forum folder and enter

```
mvn package exec:java -Dconsumer.key=XYZ -Dconsumer.secret=SECRET -Daccess.token=ABC -Daccess.token.secret=ABCSECRET
```

Alternatively you can set those properties in storm-java-forum/pom.xml

To start the web server providing the visualization, navigate to the storm-java-forum-restapi folder and enter

```
mvn package jetty:run
```

in a web browser, navigate to [http://localhost:8484/storm-java-forum-restapi/][] to show the bar chart with the actual values from Redis. 
