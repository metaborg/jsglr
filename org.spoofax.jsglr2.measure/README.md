# JSGLR2 Measurements

## Build

```
mvn clean install
```

## Execute

```
mvn exec:java
```

## Output

By default, measurements are written to CSV files at the default location `~/jsglr2measurements`.
Alternatively, a different location can be configured by setting the system property `reportPath`, e.g. with:

```
mvn exec:java -DreportPath=/some/other/path
```

## Languages

By default, measurements for interal languages and test sets will be performed.

Alternatively, a custom language and sources can be configured by passing argument `exec.args`, e.g. with:

```
mvn exec:java -Dexec.args="name extensions /path/to/parsetable /path/to/sources"
```
