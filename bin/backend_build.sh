#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"/..
mvn clean package
cp -f ./idata-portal/target/idata-portal-1.0.0.jar ./idata-portal/docker

cd ./idata-portal/docker
docker build -t docker-image.cai-inc.com/bigdata/idata-portal-server .
