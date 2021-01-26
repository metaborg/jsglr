# JSGLR2 CLI

## Build

```
mvn clean install
```

## Run

Get usage information:

```
java -jar target/org.spoofax.jsglr2.cli-2.6.0-SNAPSHOT.jar -h
```

## Examples

Given a parse table and input, return an AST:

```
java -jar target/org.spoofax.jsglr2.cli-2.6.0-SNAPSHOT.jar -pt /path/to/parsetable.tbl "foo"
```

Visualize stack and parseforest (requires `dot`), save as PDFs:

```
java -jar target/org.spoofax.jsglr2.cli-2.6.0-SNAPSHOT.jar -pt /path/to/parsetable.tbl --dot=Stack       --dot-format=pdf -o ~/Desktop/stack.pdf        "foo"
java -jar target/org.spoofax.jsglr2.cli-2.6.0-SNAPSHOT.jar -pt /path/to/parsetable.tbl --dot=ParseForest --dot-format=pdf -o ~/Desktop/parseforest.pdf  "foo"
```