#!/bin/bash
set -o errexit

parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"/../front-end

if [ ! -d "./node_modules" ]
then
  npm i --registry=http://mirrors-front.cai-inc.com --force
fi

npm run build
cp -f -r ./dist ./docker/
cd ./docker
docker build -t docker-image.cai-inc.com/bigdata/idata-portal-front .