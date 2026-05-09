#!/bin/bash

# Try PID file first
if [ -f app.pid ]; then
  PID=$(cat app.pid)
  if kill -0 "$PID" > /dev/null 2>&1; then
    echo "Stopping pak-it-app (PID $PID)..."
    kill "$PID"
    rm -f app.pid
    echo "Stopped."
    exit 0
  else
    echo "PID file found but process not running. Cleaning up."
    rm -f app.pid
  fi
fi

# Fallback: kill by port
if fuser 8443/tcp > /dev/null 2>&1; then
  echo "Stopping process on port 8443..."
  fuser -k 8443/tcp
  echo "Stopped."
else
  echo "No app running on port 8443."
fi
