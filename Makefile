b: build-npm build-maven
build: build-npm
	mvn clean install
build-npm:
	cd buy-odd-yucca-gui && yarn install && yarn build
build-maven:
	mvn clean install -DskipTests
test:
	mvn test
test-maven:
	mvn test
local: no-test
	mkdir -p bin
no-test:
	mvn clean install -DskipTests
docker:
	docker-compose down -v
	docker-compose rm -svf
	mkdir -p kong_prefix kong_tmp kong_data
	docker-compose up -d --build --remove-orphans
	chmod -R 777 kong_tmp
kong-setup:
	cd kong && deck sync
docker-databases: stop local
coverage:
	mvn clean install jacoco:prepare-agent package jacoco:report
	cd buy-odd-yucca-gui && jest --coverage
	mvn omni-coveragereporter:report
build-images:
build-docker: stop no-test build-npm
	docker-compose up -d --build --remove-orphans
show:
	docker ps -a  --format '{{.ID}} - {{.Names}} - {{.Status}}'
docker-delete-idle:
	docker ps --format '{{.ID}}' -q --filter="name=jofisaes_yucca_"| xargs -I {} docker rm {}
docker-delete: stop
	docker ps -a --format '{{.ID}}' -q --filter="name=jofisaes_yucca_"| xargs -I {}  docker stop {}
	docker ps -a --format '{{.ID}}' -q --filter="name=jofisaes_yucca_"| xargs -I {}  docker rm {}
docker-cleanup: docker-delete
	docker images -q | xargs docker rmi
docker-clean:
	docker-compose down -v
	docker-compose rm -svf
docker-clean-build-start: docker-clean b docker
docker-delete-apps: stop
prune-all: docker-delete
	docker network prune
	docker system prune --all
	docker builder prune
	docker system prune --all --volumes
stop:
	docker-compose down --remove-orphans
install:
	nvm install --lts
	nvm use --lts
	brew tap kong/deck
	brew install deck
locust-welcome-start:
	cd locust/welcome && locust --host=localhost
update-snyk:
	npm i -g snyk
update:
	curl --compressed -o- -L https://yarnpkg.com/install.sh | bash
	npm install -g npm-check-updates
	cd buy-odd-yucca-gui && npx browserslist --update-db && ncu -u && yarn
audit:
	cd buy-odd-yucca-gui && npx browserslist --update-db && npm audit fix && yarn
build-integration:
	mvn clean install
	cd buy-oyc-chain-test && mvn clean install -Dintegration
integration:
	cd buy-oyc-chain-test && mvn clean install -Pintegration
