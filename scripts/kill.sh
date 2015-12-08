#/bin/bash

ps aux | grep ./scripts/curl.sh | awk '{print $2}' | xargs kill
