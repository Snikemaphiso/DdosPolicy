# ddos-policy

A policy to manage reactions/actions to a DDoS attack

To run: 

1. You need jq. run `brew install jq` if you don't have the package already.
2. Start the application, run `sbt clean compile run`. Output will be in this window.
3. To populate the application with some random policies, in another terminal, run `./scripts/createPolicyOnServer.sh` (you might need to run this a few times to create multiple random policies)
4. In another terminal, run `./scripts/fireRandomConcurrentEvents.sh` to start firing random events. 
