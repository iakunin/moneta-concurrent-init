# moneta-concurrent-init

This repo is a minimal reproducible example for a following StackOverflow answer: https://stackoverflow.com/a/61715268/3456163

## Run

### Java 11
A version without concurrent init (completed successfully):
```bash
docker run -it --rm iakunin/moneta-concurrent-init:java11
```

A version with concurrent init (`No MonetaryAmountsSingletonSpi loaded` error):
```bash
docker run -it --rm --env SPRING_PROFILES_ACTIVE=race-condition iakunin/moneta-concurrent-init:java11
```

__Important note__: It may take several runs to reproduce the error. Just try again if there is no error.
However, there could be no error on single-core environment or environment that does not support parallel computations.

Make sure of docker-image really contains Java 11:
```bash
docker run -it --entrypoint '' iakunin/moneta-concurrent-init:java11  java -version
```

### Java 8
A version without concurrent init (completed successfully):
```bash
docker run -it --rm iakunin/moneta-concurrent-init:java8
```

A version with concurrent init (also completed successfully):
```bash
docker run -it --rm --env SPRING_PROFILES_ACTIVE=race-condition iakunin/moneta-concurrent-init:java8
```

Make sure of docker-image really contains Java 8:
```bash
docker run -it --entrypoint '' iakunin/moneta-concurrent-init:java8  java -version
```

## Build

To build a jar from sources use following command (you must have [docker](https://docs.docker.com/get-docker/) installed):
```bash
bash bin/gradle_in_docker.sh clean build
```
