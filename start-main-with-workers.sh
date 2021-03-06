#!/bin/bash

BASEDIR=$(dirname $0)
JAVA=java

if [ -n "$JAVA_HOME" ]; then
    JAVA="$JAVA_HOME/bin/java"
fi

$JAVA -Djava.security.policy="$BASEDIR"/java.policy -jar "$BASEDIR"/target/adml-1.0-SNAPSHOT.jar --rmi -n 12 $*
