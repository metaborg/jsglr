# JSGLR2 Evaluation

## Docker

Checkout the jsglr project, including the evaluation suite, on a server where you want to run the evaluation:

```
git clone https://github.com/metaborg/jsglr.git
```

Optionally, during development, checkout the `develop/jsglr2` branch:

```
git checkout develop/jsglr2
```

Go to the evaluation directory.

```
cd jsglr/jsglr2evaluation
```

Build and run the Docker image:

```
docker build -f docker/Dockerfile -t jsglr2evaluation .
docker run -d --rm -v ~/jsglr2evaluation:/jsglr2evaluation/data -e "target=all" jsglr2evaluation
```

This will use `~/jsglr2evaluation` on the host for persistence.