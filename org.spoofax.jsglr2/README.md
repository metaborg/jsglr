# JSGLR2

Build project and run tests:

```
mvn clean verify
```

Build project and publish locally while skipping tests:

```
mvn clean install -Dmaven.test.skip=true
```

Test specific class:

```
mvn clean install -Dtest=TestClass
```
