#!/bin/bash

function checkServiceByNameAndMessage() {
    name=$1
    message=$2
    printf "$name"
    docker-compose logs "$name" &> "logs"
    string=$(cat logs)
    counter=0
    while [[ "$string" != *"$message"* ]]
    do
      printf "%s." "$name"
      docker-compose logs "$name" &> "logs"
      string=$(cat logs)
      sleep 1
      counter=$((counter+1))
      if [ $counter -eq 200 ]; then
          echo "Failed after $counter tries! Cypress tests mail fail!!"
          echo "$string"
          exit
      fi
    done
    counter=$((counter+1))
    echo "succeeded $name Service after $counter tries!"
}

checkServiceByNameAndMessage yucca-db 'database system is ready to accept connections'
