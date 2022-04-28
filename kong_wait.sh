#!/bin/bash
while [[ "$string" != *"init_worker_by_lua"* ]]
do
  docker logs kong &> "logs"
  string=$(cat logs)
done
