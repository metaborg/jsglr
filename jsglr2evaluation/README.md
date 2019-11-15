# JSGLR2 Evaluation

## Prerequisites

 - Java 8
 - Ammonite for Scala scripting: https://ammonite.io/
 - R: https://www.r-project.org/ (in particular, `Rscript`)

## Run

Execute everything with defaults:

```
make all
```

## Config

Specify directory for languages, sources, measurements, etc., with `DIR` (defaults to `~/jsglr2evaluation`):

```
make DIR=~/jsglr2evaluation all
```

Specify number of warmup and benchmark iterations with `ITERATIONS` (defaults to `~/jsglr2evaluation`):

```
make ITERATIONS=10 benchmarks
```

Specify path to generate reports (LaTeX tables and plots with R) to with `REPORTDIR` (defaults to `~/jsglr2evaluation/reports`):

```
make REPORTDIR=~/path/to/paper/generated all
```
