include ../Makefile

integration:
	mvn clean install -Pintegration

b: build

build:
	cd .. && mvn clean install

# Makes complete build and runs integration tests
build-integration: build integration
