# JSGLR2 Measurements

Execute measurements:

```
mvn clean install exec:java
```

Reports are written to CSV files at the default location `~/Desktop/jsglr2reports/`.
An alternative location can be configured by setting the system property `org.spoofax.jsglr2.measure.JSGLR2Measurements.reportPath`, e.g. with:

```
mvn clean install exec:java -Dorg.spoofax.jsglr2.measure.JSGLR2Measurements.reportPath=/some/other/path/
```