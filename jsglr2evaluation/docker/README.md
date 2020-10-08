
Build the image with tag `jsglr2evaluation` (not from this directory, but from the parent directory `jsglr2evaluation`):

```
docker build -f docker/Dockerfile -t jsglr2evaluation .
```

Run the evaluation in a container:

```
docker run -d --rm -v ~/jsglr2evaluation:/jsglr2evaluation/data -e "target=evaluation" -e "evaluation_target=all" jsglr2evaluation
```

`-d`: run container in the background
`--rm`: removes the container when it exits
`-v ~/jsglr2evaluation:/jsglr2evaluation/data`: use `~/jsglr2evaluation` as working directory on the host to persist data (Spoofax sources, evaluation corpus, results)
`-e "target=evaluation"`: specify the target of the Make build (e.g. `all`, `spoofax`, `evaluation`)
`-e "evaluation_config=config.yml"`: specify the configuration file to use (in the working directory)
`-e "evaluation_target=all"`: specify the target of the evaluation


## Docker documentattion

- https://docs.docker.com/get-started/part2/
- Dockerfile: https://docs.docker.com/engine/reference/builder/#exec-form-entrypoint-example
- Running: https://docs.docker.com/engine/reference/commandline/run/.
