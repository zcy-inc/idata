#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd ./front-end
npm i --registry=http://mirrors-front.cai-inc.com
npm run build
docker build -t docker-image.cai-inc.com/bigdata/idata-portal-front .