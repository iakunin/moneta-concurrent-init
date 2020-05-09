.PHONY: bin build gradle src

VERSION := $(shell git tag -l --sort=v:refname | tail -n 1)

tag:
	bash bin/create_tag.sh

build:
	bash bin/gradle_in_docker.sh clean -Pversion=$(VERSION) build

docker-build:
	docker build -t iakunin/moneta-concurrent-init:$(VERSION) -t iakunin/moneta-concurrent-init:latest .

docker-push:
	docker push iakunin/moneta-concurrent-init:$(VERSION) && \
	docker push iakunin/moneta-concurrent-init:latest
