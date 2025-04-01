#!/bin/bash

container_name=financial-analysis-db

if [ "$(docker ps -aq -f name=$container_name)" ]; then
    if [ "$(docker inspect -f '{{.State.Running}}' $container_name 2>/dev/null)" = "true" ]; then
        echo "Stopping the existing running container..."
        docker stop $container_name
    fi

    echo "Removing existing container..."
    docker rm $container_name
fi

docker build -t financial-analysis-image .
docker run -d --name financial-analysis-container -p 5432:5432 financial-analysis-image