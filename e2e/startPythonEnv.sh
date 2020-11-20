docker run --network environment_default -v $(pwd):/home -e KAFKA_BROKERS=kafka:9092 \
      -ti ibmcase/python37 bash