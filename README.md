# ddos-policy

A policy to manage reactions/actions to a DDoS attack

To run: 

1. You need jq. run `brew install jq`
2. run `sbt clean compile run`
3. In another terminal, run `./scripts/createPolicyOnServer.sh`
4. In another terminal, run `./scripts/util/createRandomData.sh`