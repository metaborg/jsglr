
Build the image with tag `jsglr2evaluation` (not from this directory, but from the parent directory `jsglr2evaluation`):

```
docker build -f docker/Dockerfile -t jsglr2evaluation .
```

Run the evaluation in a container:

```
docker run -d --rm -v ~/jsglr2evaluation:/jsglr2evaluation/data -e "target=evaluation" jsglr2evaluation
```

`-d`: run container in the background
`--rm`: removes the container when it exits
`-v ~/jsglr2evaluation:/jsglr2evaluation/data`: use the `~/jsglr2evaluation` directory on the host to persist data (Spoofax sources, evaluation corpus, results)
`-e "target=evaluation"`: via the `target` environment variable, specify the target of the Make build (e.g. `all`, `spoofax`, `evaluation`)

## Docker documentattion

- https://docs.docker.com/get-started/part2/
- Dockerfile: https://docs.docker.com/engine/reference/builder/#exec-form-entrypoint-example
- Running: https://docs.docker.com/engine/reference/commandline/run/.
