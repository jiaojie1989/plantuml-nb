#!/bin/bash

mvn install:install-file -Dfile=plantuml-dependency-1.2.0/plantuml-dependency-1.2.0-jar-with-dependencies.jar -DgroupId=net.sourceforge.plantuml-dependency -DartifactId=plantuml-dependency -Dversion=1.2.0 -Dpackaging=jar
