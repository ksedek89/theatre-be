#!/bin/sh
importCaCert () { # alias file
  keytool -delete -cacerts -alias "$1" -storepass changeit -noprompt || true
  keytool -import -cacerts -trustcacerts -alias "$1" -file "$2" -storepass changeit -noprompt
}
set -e
set -x

java -version
exec "$@"
