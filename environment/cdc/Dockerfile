ARG DEBEZIUM_VERSION
FROM debezium/connect:1.3

USER root
RUN yum -y install libaio curl && yum clean all

USER kafka
ENV CONNECT_TOPIC_CREATION_ENABLE=true
# Deploy db2 client and drivers
RUN curl https://repo1.maven.org/maven2/com/ibm/db2/jcc/11.5.0.0/jcc-11.5.0.0.jar --output /kafka/connect/debezium-connector-db2/jcc-11.5.0.0.jar
