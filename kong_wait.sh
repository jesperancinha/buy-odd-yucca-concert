#!/bin/bash

function checkServiceByNameAndMessage() {
    name=$1
    message=$2
    printf $name
    docker-compose logs "$name" &> "logs"
    string=$(cat logs)
    echo "$string"
    counter=0
    while [[ "$string" != *"$message"* ]]
    do
      printf "."
      docker-compose logs "$name"
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

checkServiceByNameAndMessage kong 'init_worker_by_lua'
