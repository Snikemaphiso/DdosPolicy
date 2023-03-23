#!/bin/bash

# Predefined arrays
report_ID_values=($(seq 1 20000))
attack_class_values=(
  "Flooding attack"
  "Amplified attack"
  "Protocol Exploits"
  "Malformed Packets"
)
# following 2 are  associative values
#target_type_values=("Application server" "Network infrastructure" "Cloud-based service" "Web server" "DNS infrastructure" "VoIP server" "Database server" "VPN concentrator" "Firewall" "SDN controller" "VNF" "Security agent")
target_type_values=(
  "Data Center"
  "Cloud-based service"
  "Database Server"
  "Security Agent"
  "VNF"
  "Application Server"
)
#target_id_values=($(seq 1 20))
target_id_values=($(seq 1 100))

target_location_values=("vm1" "vm2" "vm3" "vm4" "vm5" "vm6")
severity_values=("low" "moderate" "high")
# seq -s " " 100 50 1000
consumption_rate_values=(100 150 200 250 300 350 400 450 500 550 600 650 700 750 800 850 900 950 1000)

function randomEventFunc() {
  # Generate random keys
  report_ID="${report_ID_values[RANDOM % ${#report_ID_values[@]}]}"
  attack_class="${attack_class_values[RANDOM % ${#attack_class_values[@]}]}"
  # target values start
  target_idx=$((RANDOM % ${#target_type_values[@]}))
  target_type="${target_type_values[target_idx]}"
  target_id="${target_id_values[target_idx]}"
  # target values end
  target_location="${target_location_values[RANDOM % ${#target_location_values[@]}]}"
  severity="${severity_values[RANDOM % ${#severity_values[@]}]}"
  consumption_rate="${consumption_rate_values[RANDOM % ${#consumption_rate_values[@]}]}"
  time_stamp=$(date +%s%3)

  # Build JSON object using jq
  json_object=$(jq -n \
    --argjson key1 "$report_ID" \
    --arg key2 "$attack_class" \
    --arg key3 "$target_type" \
    --argjson key4 "$target_id" \
    --arg key5 "$target_location" \
    --arg key6 "$severity" \
    --argjson key7 "$consumption_rate" \
    --argjson key8 "$time_stamp" \
    '{
      "event_header": {
        "report_ID": $key1,
        "time_stamp": $key8
      },
      "event_type": {
        "attack_class": $key2
      },
      "target": {
        "target_type": $key3,
        "target_ID": $key4
      },
      "target_location": {
        "location": $key5
      },
      "severity": $key6,
      "event_xterics": {
        "consumption": {
          "consumption_rate": $key7
        }
      }
    }')
  #  echo "generateRandomEvent done func"
  echo "$json_object"
}

function createPolicyFunc() {

  action_values=("ALLOW" "DEPLOY_DPI" "MIGRATE" "DENY_SOURCE" "CLOSE_APP" "NO_ACTION")
  policy_type_values=("pType")
  flow_condition_values=(">" "<" ">=" "<=" "==")

  name="Policy - ${report_ID_values[RANDOM % ${#report_ID_values[@]}]}"
  policyType="${policy_type_values[RANDOM % ${#policy_type_values[@]}]}"
  target_type="${target_type_values[$((RANDOM % ${#target_type_values[@]}))]}"
  severity="${severity_values[RANDOM % ${#severity_values[@]}]}"
  flow_condition="${flow_condition_values[RANDOM % ${#flow_condition_values[@]}]}"
  flow_rate="${consumption_rate_values[RANDOM % ${#consumption_rate_values[@]}]}"
  action="${action_values[RANDOM % ${#action_values[@]}]}"

  # Build JSON object using jq
  json_object=$(
    jq -n \
      --arg key1 "$name" \
      --arg key2 "$policyType" \
      --arg key3 "$target_type" \
      --arg key4 "$severity" \
      --arg key5 "$flow_condition$flow_rate" \
      --arg key6 "$action" \
      '{
        "name": $key1,
        "policyType": $key2,
        "target": $key3,
        "severity": $key4,
        "condition": {
            "flow": $key5
        },
        "action": $key6
    }'
  )
  echo "$json_object"
}
