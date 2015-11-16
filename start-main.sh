#!/bin/bash

BASEDIR=$(dirname $0)
java -Djava.security.policy="$BASEDIR"/java.policy -jar "$BASEDIR"/target/adml-1.0-SNAPSHOT.jar --rmi
