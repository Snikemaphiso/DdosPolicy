# DDOS-Policy

A system to manage reactions/actions to a DDoS attack

### To run:

Simply run `sbt run`

The application works by accepting **Policy** objects in a predefined JSON format, then consuming **Event**s which are then compared to the **Policy** conditions.

### To simulate:

You'll need jq package. run `brew install jq` if you don't have the package already.

The entire process can be simulated in a few steps:

1. Start the application, run `sbt clean compile run` for a clean start. (Note: Output will be in this window).
2. Populate the application with some random **Policy** objects. In another terminal, run `./scripts/createPolicyOnServer.sh`. (*You might need to run this a few times to create multiple random **Policy** objects*)
3. In another terminal, run `./scripts/fireRandomConcurrentEvents.sh` to start firing random **Event**s. 
