#!/bin/sh
echo "start product-service-dev"

export JVM_OPTS="-XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=70.0"
export JVM_ARGS="--spring.profiles.active=dev --server.port=5000"

java -jar product-0.0.1-SNAPSHOT.jar ${JVM_OPTS} ${JVM_ARGS}

