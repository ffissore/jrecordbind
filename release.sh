#!/usr/bin/env bash

mvn clean install -Prelease
mvn clean install site deploy -Prelease -pl org.fissore.jrecordbind:jrecordbind-parent,org.fissore.jrecordbind:jrecordbind
