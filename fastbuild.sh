#!/bin/sh

git pull
git checkout develop

./gradlew build -x test
java -jar -Dspring.profiles.active=prod build/libs/UnderTheC-Backend-0.0.1-SNAPSHOT.jar
