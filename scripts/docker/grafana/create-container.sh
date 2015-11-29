#!/bin/bash

docker run --name grafana --link graphite:graphite -d -p 3000:3000 grafana/grafana
