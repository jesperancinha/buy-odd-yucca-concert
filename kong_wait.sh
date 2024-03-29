#!/bin/bash

function checkServiceByNameAndMessage() {
    name=$1
    message=$2
    printf "%s." "$name"
    docker-compose logs "$name" &> "logs"
    string=$(cat logs)
    echo "$string"
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
          echo "$string"
          exit 1
      fi
      if [[ "$string" = *"[PostgreSQL error] failed to retrieve PostgreSQL"* ]]; then
          echo "Failed PostgreSQL connection after $counter tries! Cypress tests mail fail!!"
          echo "$string"
          exit 1
      fi
    done
    counter=$((counter+1))
    echo "succeeded $name Service after $counter tries!"
}

checkServiceByNameAndMessage kong 'init_worker_by_lua'
