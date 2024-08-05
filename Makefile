SHELL=/bin/bash

b: build-npm build-maven
build: build-npm
	mvn clean install
build-npm:
	cd buy-odd-yucca-gui; \
	yarn; \
	yarn build
build-maven:
	mvn clean install -DskipTests
build-api:
	docker compose stop buy-oyc-api
	cd buy-oyc-api-service; \
 	rm -r target; \
 	mvn clean install -DskipTests; \
	docker compose rm buy-oyc-api; \
	docker compose build buy-oyc-api; \
	docker compose up -d buy-oyc-api
test:
	mvn test
test-maven:
	mvn test
test-node:
	cd buy-odd-yucca-gui && npm run jest
local: no-test
	mkdir -p bin
no-test:
	mvn clean install -DskipTests
docker: create-folders set-permissions
	docker compose up -d --build --remove-orphans
set-permissions:
	if [[ -d kong_data_vol ]]; then sudo chmod -R 777 kong_data_vol; else mkdir kong_data_vol && sudo chmod -R 777 kong_data_vol; fi
	if [[ -d kong_tmp_vol ]]; then sudo chmod -R 777 kong_tmp_vol; else mkdir kong_tmp_vol && sudo chmod -R 777 kong_tmp_vol; fi
	if [[ -d kong_prefix_vol ]]; then sudo chmod -R 777 kong_prefix_vol; else mkdir kong_prefix_vol && sudo chmod -R 777 kong_prefix_vol; fi
kong-full-action-setup:
	curl -sL https://github.com/kong/deck/releases/download/v1.12.3/deck_1.12.3_linux_amd64.tar.gz -o deck.tar.gz
	tar -xf deck.tar.gz -C /tmp
	sudo cp /tmp/deck /usr/local/bin/
	make set-permissions
	bash kong_wait.sh
	make kong-setup
kong-setup:
	cd kong && deck sync
docker-databases: stop local
docker-stop-all:
	docker ps -a --format '{{.ID}}' | xargs -I {}  docker stop {}
	docker network prune
docker-remove-all: docker-stop-all
	docker network list --format '{{.ID}}' | xargs -I {} docker network rm  {} || echo 'Done!'
	docker ps -a --format '{{.ID}}' | xargs -I {}  docker rm {}
build-report:
	mvn clean install jacoco:prepare-agent package jacoco:report
coverage: build-report
	cd buy-odd-yucca-gui && jest --coverage
	mvn omni-coveragereporter:report
build-images:
build-docker: stop no-test build-npm
	docker compose up -d --build --remove-orphans
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
	docker compose down -v
	docker compose rm -svf
docker-clean-build-start: docker-clean b docker
docker-delete-apps: stop
docker-pull-images:
	docker compose pull
# docker-action is only used for remote pipelines
docker-action: create-folders set-permissions
	#sudo chown -R 1000:1000 ./kong_data_vol
	docker compose build
	docker compose -f docker-compose.yml up -d
	docker compose logs
prune-all: docker-delete
	docker network prune -f
	docker system prune --all -f
	docker builder prune -f
	docker system prune --all --volumes -f
stop:
	docker compose down --remove-orphans
install:
	nvm install --lts
	nvm use --lts
	brew tap kong/deck
	brew install deck
locust-start:
	cd locust/welcome && locust --host=localhost
update-snyk:
	npm i -g snyk
remove-lock-files:
	find . -name "package-lock.json" | xargs -I {} rm {}; \
	find . -name "yarn.lock" | xargs -I {} rm {};
update: remove-lock-files
	curl --compressed -o- -L https://yarnpkg.com/install.sh | bash
	sudo npm install -g npm-check-updates
	cd buy-odd-yucca-gui; \
 	ncu -u; \
 	yarn; \
 	npx browserslist --update-db; \
 	yarn
audit:
	cd buy-odd-yucca-gui && npx browserslist --update-db && npm audit fix && yarn
build-integration: build-npm
	mvn clean install -DskipTests
	cd buy-oyc-commons && mvn clean install -Pintegration
integration:
	cd buy-oyc-commons && mvn clean install -Pintegration
boyc-wait:
	bash boyc_wait.sh
create-folders:
	mkdir -p kong_prefix_vol kong_tmp_vol kong_data_vol
database-wait:
	bash database_wait.sh
dcup-light:
	docker compose --env-file ./.env up -d yucca-db
	make set-permissions
	bash database_wait.sh
dcup-light-action: create-folders
	docker compose --env-file ./.env-pipeline -f docker-compose.yml up -d yucca-db
	bash database_wait.sh
dcup-light-open-action:
	docker compose --env-file ./.env-pipeline up -d yucca-db
	sudo chown -R 1000:1000 ./kong_data_vol
	bash database_wait.sh
dcup: dcd docker-clean docker boyc-wait deck
dcup-full: dcd docker-clean b set-permissions docker boyc-wait
# dcup-full-action is only used for remote pipelines
dcup-full-action: dcd docker-clean docker-pull-images b docker-action boyc-wait
dcd:
	docker compose down
	docker compose down -v
	docker compose rm -svf
	if [[ -d kong_data_vol ]]; then sudo rm -r kong_data_vol; fi
	if [[ -d kong_prefix_vol ]]; then sudo rm -r kong_prefix_vol; fi
	if [[ -d kong_tmp_vol ]]; then sudo rm -r kong_tmp_vol; fi
deck-pipeline:
	docker compose -f docker-compose.yml up -d kong-deck
	docker compose logs kong-deck
	docker compose logs yucca-db
	docker compose logs kong
deck:
	docker compose -f docker-compose.yml up -d kong-deck
cypress-open:
	cd e2e && yarn && npm run cypress:open:electron
cypress-electron:
	cd e2e && make cypress-electron
cypress-chrome:
	cd e2e && make cypress-chrome
cypress-firefox:
	cd e2e && make cypress-firefox
cypress-edge:
	cd e2e && make cypress-edge
coverage-maven:
	mvn jacoco:prepare-agent package jacoco:report
coverage-node:
	cd buy-odd-yucca-gui && npm run coverage
report:
	mvn omni-coveragereporter:report
local-pipeline: build-maven build-npm test-maven test-node report coverage-maven coverage-node
update-node:
	sudo npm install -g n
	sudo n lts
update-browser-list:
	cd buy-odd-yucca-gui; \
	ncu -u; \
	yarn; \
	npx update-browserslist-db@latest
node-update:
	curl https://raw.githubusercontent.com/creationix/nvm/master/install.sh | bash
	source ~/.nvm/nvm.sh
	nvm install --lts
	nvm use --lts
deps-update: update
revert-deps-cypress-update:
	if [ -f  e2e/docker composetmp.yml ]; then rm e2e/docker composetmp.yml; fi
	if [ -f  e2e/packagetmp.json ]; then rm e2e/packagetmp.json; fi
	git checkout e2e/docker compose.yml
	git checkout e2e/package.json
deps-cypress-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/cypressUpdateOne.sh | bash
deps-plugins-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/pluginUpdatesOne.sh | bash
deps-java-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/javaUpdatesOne.sh | bash
deps-node-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/nodeUpdatesOne.sh | bash
deps-quick-update: deps-cypress-update deps-plugins-update deps-java-update deps-node-update
accept-prs:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/acceptPR.sh | bash
dc-migration:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/setupDockerCompose.sh | bash
