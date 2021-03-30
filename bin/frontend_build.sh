#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd ./front-end
npm i
npm run build
docker build -t docker-image.cai-inc.com/bigdata/idata-front-end .