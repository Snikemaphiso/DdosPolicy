#!/bin/bash
url="http://localhost:8080/policy"
#No of policies that should be created
desired_count=5

# Actual script below
source util/createRandomData.sh

for ((i = 1; i <= desired_count; i++)); do
  random_policy=$(createPolicyFunc)
  # Send the request
#  echo "$random_policy"
  curl -X POST -H "Content-Type: application/json" -d "$random_policy" $url
done
