#!/bin/bash
java -Djava.security.egd=file:/dev/./urandom \
-jar -Dmanagement.port=18080 -Dmanagement.server.port=18080 /app/app.jar