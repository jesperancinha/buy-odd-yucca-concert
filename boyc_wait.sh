#!/bin/bash

function checkServiceByNameAndMessage() {
    name=$1
    message=$2
    printf $name
    docker-compose logs "$name" &> "logs"
    string=$(cat logs)
    counter=0
    while [[ "$string" != *"$message"* ]]
    do
      printf "."
      docker-compose logs "$name" &> "logs"
      string=$(cat logs)
      sleep 1
      counter=$((counter+1))
      if [ $counter -eq 200 ]; then
          echo "Failed after $counter tries! Cypress tests mail fail!!"
          exit
      fi
    done
    counter=$((counter+1))
    echo "Succeeded $name Service after $counter tries!"
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
