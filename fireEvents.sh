#!/bin/bash

for i in {1..10};
do
  echo -e "Firing sample event: $i"

  sleep 5 && \
  curl -X POST localhost:8080/event \
  -H 'Content-Type: application/json' \
  -d '{"event_header":{"report_ID":1000},"event_type":{"attack_class":"bandwith"},"target":{"target_type":"server","target_ID":12},"target_location":{"location":"vm3"},"severity":"low","event_xterics":{"consumption":{"consumption_rate":150}}}';

  echo -e "\n"
done
