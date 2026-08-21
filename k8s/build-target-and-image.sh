#!/usr/bin/env bash

set -euo pipefail
# set -e
  #Stops execution if any command fails.
# set -u
  #Fails when using an undefined variable.
# set -o pipefail
  #Detects failures in pipelines

path_to_repo="$HOME/sb-projects/ms-2026"

./build-target.sh "$path_to_repo"
./build-docker-image.sh "$path_to_repo"