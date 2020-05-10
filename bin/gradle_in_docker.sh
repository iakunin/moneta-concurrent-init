#!/usr/bin/env bash

docker run \
    --tty \
    --interactive \
    --privileged \
    --rm \
    --volume="${PWD}":/home/gradle/project \
    --volume="${HOME}"/.m2:/root/.m2 \
    --volume=moneta-concurrent-init-gradle-cache:/home/gradle/.gradle \
    --volume=/var/run/docker.sock:/var/run/docker.sock \
    --workdir=/home/gradle/project \
    gradle:6.0.1-jdk8 \
    gradle "$@"

docker run \
    --tty \
    --interactive \
    --privileged \
    --rm \
    --volume="${PWD}":/home/gradle/project \
    --workdir=/home/gradle/project \
    gradle:6.0.1-jdk8 \
    chown -R "$(id -u)":"$(id -g)" .
