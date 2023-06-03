#!/bin/bash
# Get servers list:
set — f
# Variables from GitLab server:
# Note: They can’t have spaces!!
string=$DEPLOY_SERVER_IP
array=(${string//,/ })
# Iterate servers for deploy and pull last commit
# Careful with the ; https://stackoverflow.com/a/20666248/1057052
for i in "${!array[@]}"; do
  echo "Deploy project on server ${array[i]}"
sudo ssh ubuntu@${array[i]} 'cd ./Staging/vr && git stash && git checkout $CI_BUILD_REF_NAME && git stash && git pull && sudo yarn install && sudo npm run staging'
done