#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"/..
#mvn clean package
cp -f ../idata-dqc/target/idata-dqc-1.0.0.jar ../idata-dqc/docker

cd ../idata-dqc/docker
docker build -t docker-image.cai-inc.com/bigdata/web-dqc .
