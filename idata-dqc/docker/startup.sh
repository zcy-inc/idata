#!/bin/bash
java -Djava.security.egd=file:/dev/./urandom \
-jar $JAVA_OPTS /app/app.jar