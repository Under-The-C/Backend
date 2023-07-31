#!/bin/sh

git pull
git checkout develop

./gradlew build
java -jar build/libs/UnderTheC-Backend-0.0.1-SNAPSHOT.jar
