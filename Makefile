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
	mkdir -p kong_prefix_vol kong_tmp_vol kong_data_vol
	docker-compose up -d --build --remove-orphans
kong-full-setup:
	chmod -R 777 kong_tmp_vol
	bash kong_wait.sh
	make kong-setup
kong-full-action-setup:
	curl -sL https://github.com/kong/deck/releases/download/v1.12.3/deck_1.12.3_linux_amd64.tar.gz -o deck.tar.gz
	tar -xf deck.tar.gz -C /tmp
	sudo cp /tmp/deck /usr/local/bin/
	sudo chmod -R 777 kong_tmp_vol
	bash kong_wait.sh
	make kong-setup
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
	docker ps -a --format '{{.ID}}' | xargs -I {}  docker stop {}
	docker ps -a --format '{{.ID}}' | xargs -I {}  docker rm {}
docker-cleanup: docker-delete
	docker images -q | xargs docker rmi
docker-clean:
	docker-compose down -v
	docker-compose rm -svf
docker-clean-build-start: docker-clean b docker
docker-delete-apps: stop
docker-action:
	docker-compose -f docker-compose.yml up -d --build --remove-orphans
prune-all: docker-delete
	docker network prune -f
	docker system prune --all -f
	docker builder prune -f
	docker system prune --all --volumes -f
stop:
	docker-compose down --remove-orphans
install:
	nvm install --lts
	nvm use --lts
	brew tap kong/deck
	brew install deck
locust-start:
	cd locust/welcome && locust --host=localhost
update-snyk:
	npm i -g snyk
update:
	curl --compressed -o- -L https://yarnpkg.com/install.sh | bash
	npm install -g npm-check-updates
	cd buy-odd-yucca-gui && npx browserslist --update-db && ncu -u && yarn
audit:
	cd buy-odd-yucca-gui && npx browserslist --update-db && npm audit fix && yarn
build-integration: build-npm
	mvn clean install -DskipTests
	cd buy-oyc-commons && mvn clean install -Pintegration
integration:
	cd buy-oyc-commons && mvn clean install -Pintegration
boyc-wait:
	bash boyc_wait.sh
dcup-light:
	docker-compose up -d fla_postgres
dcup: dcd docker-clean docker kong-full-setup boyc-wait
dcup-full: docker-clean-build-start kong-full-setup boyc-wait
dcup-full-action: docker-clean b docker-action kong-full-action-setup boyc-wait
dcd:
	docker-compose down
cypress-open:
	cd e2e && yarn && npm run cypress
cypress-electron:
	cd e2e && make cypress-electron
cypress-chrome:
	cd e2e && make cypress-chrome
cypress-firefox:
	cd e2e && make cypress-firefox
cypress-edge:
	cd e2e && make cypress-edge
