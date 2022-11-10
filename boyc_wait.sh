#!/bin/bash

function checkServiceByNameAndMessage() {
    name=$1
    message=$2
    docker-compose logs "$name" > "logs"
    string=$(cat logs)
    counter=0
    echo "Project $GITHUB_RUN_ID"
    echo -n "Starting service $name "
    while [[ "$string" != *"$message"* ]]
    do
      echo -e -n "\e[93m-\e[39m"
      docker-compose logs "$name" > "logs"
      string=$(cat logs)
      sleep 1
      counter=$((counter+1))
      if [ $counter -eq 200 ]; then
          echo -e "\e[91mFailed after $counter tries! Cypress tests may fail!!\e[39m"
          echo "$string"
          exit 1
      fi
    done
    counter=$((counter+1))
    echo -e "\e[92m Succeeded starting $name Service after $counter tries!\e[39m"
}

checkServiceByNameAndMessage yucca-db 'database system is ready to accept connections'
checkServiceByNameAndMessage buy-oyc-api 'Startup completed in'
checkServiceByNameAndMessage buy-oyc-parking 'Startup completed in'
checkServiceByNameAndMessage buy-oyc-catering 'Startup completed in'
checkServiceByNameAndMessage buy-oyc-ticket 'Startup completed in'
checkServiceByNameAndMessage buy-oyc-concert 'Startup completed in'
checkServiceByNameAndMessage buy-oyc-nginx 'test is successful'
checkServiceByNameAndMessage redis 'Ready to accept connections'
checkServiceByNameAndMessage kong 'finished preloading'
