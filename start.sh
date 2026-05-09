#!/bin/bash

# Check if already running
if fuser 8443/tcp > /dev/null 2>&1; then
  echo "App is already running on port 8443."
  exit 1
fi

# Warn if no swap is configured
SWAP=$(free -m | awk '/^Swap:/ {print $2}')
if [ "$SWAP" -eq 0 ]; then
  echo "WARNING: No swap configured. Risk of OOM kill. Run setup-swap.sh first."
fi

echo "Starting pak-it-app..."
nohup java -Xmx400m -Xms200m \
  -jar ./target/pak-it-app-0.0.1-SNAPSHOT.jar \
  > output.log 2>&1 &

echo $! > app.pid
echo "Started with PID $(cat app.pid). Logs: output.log"
