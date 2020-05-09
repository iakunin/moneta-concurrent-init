# moneta-concurrent-init

This repo is a minimal-reproducible-example for a following StackOverflow question: 
https://stackoverflow.com/q/61465836/3456163

## Run

A version without concurrent init:
```bash
docker run -it --rm iakunin/moneta-concurrent-init
```

A version with concurrent init (`No MonetaryAmountsSingletonSpi loaded` error):
```bash
docker run -it --rm --env SPRING_PROFILES_ACTIVE=race-condition iakunin/moneta-concurrent-init
```

## Build

To build a jar from sources use following command (you must have [docker](https://docs.docker.com/get-docker/) installed):
```bash
bash bin/gradle_in_docker.sh clean build
```
