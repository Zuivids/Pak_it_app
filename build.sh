#!/bin/bash

echo "Building pak-it-app..."
mvn clean package -Dmaven.test.skip=true

if [ $? -eq 0 ]; then
  echo "Build successful."
else
  echo "Build FAILED." >&2
  exit 1
fi
