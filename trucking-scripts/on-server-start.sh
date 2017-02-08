#!/bin/bash

# Start the Schema Registry webserver
/usr/hdp/current/registry/bin/registry stop
/usr/hdp/current/registry/bin/registry clean
/usr/hdp/current/registry/bin/registry start

# nohup /usr/hdp/2.1.0.0-164/registry/bin/registry-server-start.sh /usr/hdp/2.1.0.0-164/registry/conf/registry-dev.yaml &
