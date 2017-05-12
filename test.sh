#!/usr/bin/env bash

mvnRepoLocation=$(mvn help:evaluate -Dexpression=settings.localRepository | grep )

echo $mvnRepoLocation

IFS=' ' read -r -a array <<< "${mvnRepoLocation}"

echo "mvn repo location is "