jfs2013-storm-twitter-sample
============================

The Storm sample shown during my talk at Java Forum Stuttgart 2013

## Features
* GZip support
* OAuth support
* Partitioning support
* Automatic reconnections with appropriate backfill counts
* Access to raw bytes payload
* Proper backoffs/retry schemes
* Relevant statistics/events
* Control stream support for sitestreams

### Quick Start Example

To run the filter stream example, navigate to the storm-java-forum folder and enter

```
mvn package exec:java -Dconsumer.key=XYZ -Dconsumer.secret=SECRET -Daccess.token=ABC -Daccess.token.secret=ABCSECRET
```

Alternatively you can set those properties in storm-java-forum/pom.xml

