docker exec -ti  kafka  bash -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --create  --replication-factor 1 --partitions 1 --topic db_history_vaccine_orders"

docker exec -ti  kafka bash -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --create  --replication-factor 1 --partitions 1 --topic vaccine_shipments"

docker exec -ti  kafka bash -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --create  --replication-factor 1 --partitions 1 --topic transportation"

docker exec -ti  kafka bash -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --create  --replication-factor 1 --partitions 1 --topic reefer_telemetries"

docker exec -ti  kafka bash -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --create  --replication-factor 1 --partitions 1 --topic vaccine_lots"

docker exec -ti  kafka bash -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --create  --replication-factor 1 --partitions 1 --topic vaccine_shipment_plans"