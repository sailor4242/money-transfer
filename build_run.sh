#!/usr/bin/env bash
mvn package -DskipTests; java -jar target/transfer-0.0.1-SNAPSHOT.jar
