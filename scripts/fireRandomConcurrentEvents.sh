#!/bin/bash
# brew install jq
# max of 1-3 after every 1-10 seconds for desired_time.
# if desired time is empty then forever. then ctrl + c to stop.
#change these for different option
maxNumberOfConcurrentRequest=1
maxDelay=10
#durtaion in seconds for which script should run in seconds
desired_time=30
#change these for different option
url="http://localhost:8080/trigger"

# Actual script below
source util/createRandomData.sh

while true; do
  # Generate a random number between 1 and 3 for the count of curl requests to send
  numberOfConcurrentRequest=$((1 + RANDOM % maxNumberOfConcurrentRequest))
  echo "Will be firing ${numberOfConcurrentRequest} events"

  for ((i = 1; i <= numberOfConcurrentRequest; i++)); do
    # Create the random object and store it in random_event variable
    random_event=$(randomEventFunc)
    # Send the request
#    echo "$random_event"
    curl -X POST -H "Content-Type: application/json" -d "$random_event" $url &
  done
  #waits for all the curl request to sleep
  wait
  # Check if the script has been running for the desired time
  if [[ -n "$desired_time" && "$SECONDS" -ge "$desired_time" ]]; then
    echo "Script has been running for $desired_time seconds. Exiting."
    break
  fi
  # Wait for 0-10 seconds before sending more requests
  randomDelay=$((1 + RANDOM % maxDelay))
  echo "delaying ${randomDelay} seconds for next trigger"
  sleep $randomDelay
done
