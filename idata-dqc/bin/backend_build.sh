#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"/..

mvn clean package

cp -f target/idata-dqc-1.0.0.jar docker

cd docker
docker login docker-image.cai-inc.com

#清除缓存
#docker system prune --all
curTime=$(date "+%Y%m%d%H%M%S")

docker build -t docker-image.cai-inc.com/bigdata/web-dqc:$curTime .

docker push docker-image.cai-inc.com/bigdata/web-dqc:$curTime

