.PHONY: bin build gradle src

VERSION := $(shell git tag -l --sort=v:refname | tail -n 1)

tag:
	bash bin/create_tag.sh

build:
	bash bin/gradle_in_docker.sh clean -Pversion=$(VERSION) build

docker-build-java11:
	docker build -t iakunin/moneta-concurrent-init:java11 -f Dockerfile-java11 .

docker-push-java11:
	docker push iakunin/moneta-concurrent-init:java11

docker-build-java8:
	docker build -t iakunin/moneta-concurrent-init:java8 -f Dockerfile-java8 .

docker-push-java8:
	docker push iakunin/moneta-concurrent-init:java8
