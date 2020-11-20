import json,os
from confluent_kafka import Consumer, KafkaError


class KafkaConsumer:

    def __init__(self,
                kafka_brokers = "", 
                kafka_user = "", 
                kafka_pwd = "", 
                kafka_cacert = "", 
                kafka_sasl_mechanism = "", 
                topic_name = "", 
                autocommit = True,
                fromWhere = 'earliest'):
        self.kafka_brokers = kafka_brokers
        self.kafka_user = kafka_user
        self.kafka_pwd = kafka_pwd
        self.kafka_cacert = kafka_cacert
        self.kafka_sasl_mechanism = kafka_sasl_mechanism
        self.topic_name = topic_name
        self.fromWhere = fromWhere
        self.kafka_auto_commit = autocommit
        

    def prepare(self, groupID = "pythonconsumers"):
        options ={
                'bootstrap.servers':  self.kafka_brokers,
                'group.id': groupID,
                'auto.offset.reset': self.fromWhere,
                'enable.auto.commit': self.kafka_auto_commit,
        }
        if (self.kafka_user != ''):
            options['security.protocol'] = 'SASL_SSL'
            options['sasl.mechanisms'] = self.kafka_sasl_mechanism
            options['sasl.username'] = self.kafka_user
            options['sasl.password'] = self.kafka_pwd
        
        if (self.kafka_cacert != '' ):
            options['ssl.ca.location'] = self.kafka_cacert
       
        print("[KafkaConsumer] - This is the configuration for the consumer:")
        print('[KafkaConsumer] - {}'.format(options))
        self.consumer = Consumer(options)
        self.consumer.subscribe([self.topic_name])
    
    # Prints out and returns the decoded events received by the consumer
    def traceResponse(self, msg):
        msgStr = msg.value().decode('utf-8')
        print(msgStr)
        print('[KafkaConsumer] - @@@ pollNextRecord {} partition: [{}] at offset {} with key {}:\n\tvalue: {}'
                    .format(msg.topic(), msg.partition(), msg.offset(), str(msg.key()), msgStr ))
        return msgStr

    # Polls for events until it finds an event where keyId=keyname
    def pollNextEvent(self, keyID, keyname):
        gotIt = False
        anEvent = {}
        while not gotIt:
            msg = self.consumer.poll(timeout=10.0)
            # Continue if we have not received a message yet
            if msg is None:
                continue
            if msg.error():
                print("[KafkaConsumer] - Consumer error: {}".format(msg.error()))
                # Stop reading if we find end of partition in the error message
                if ("PARTITION_EOF" in msg.error()):
                    gotIt= True
                continue
            msgStr = self.traceResponse(msg)
            # Create the json event based on message string formed by traceResponse
            anEvent = json.loads(msgStr)
            # If we've found our event based on keyname and keyID, stop reading messages
            if (anEvent["payload"][keyname] == keyID):
                gotIt = True
        return anEvent

    # Polls for events until it finds an event with same key
    def pollNextEventByKey(self, keyID):
        if (str(keyID) == ""):
            print("[KafkaConsumer] - Consumer error: Key is an empty string")
            return None
        gotIt = False
        anEvent = {}
        while not gotIt:
            msg = self.consumer.poll(timeout=10.0)
            # Continue if we have not received a message yet
            if msg is None:
                continue
            if msg.error():
                print("[KafkaConsumer] - Consumer error: {}".format(msg.error()))
                # Stop reading if we find end of partition in the error message
                if ("PARTITION_EOF" in msg.error()):
                    gotIt= True
                continue
            msgStr = self.traceResponse(msg)
            # Create the json event based on message string formed by traceResponse
            anEvent = json.loads(msgStr)
            # If we've found our event based on keyname and keyID, stop reading messages
            if (str(msg.key().decode('utf-8')) == keyID):
                gotIt = True
        return anEvent

    # Polls for events endlessly
    def pollEvents(self):
        gotIt = False
        while not gotIt:
            msg = self.consumer.poll(timeout=10.0)
            if msg is None:
                continue
            if msg.error():
                print("[ERROR] - [KafkaConsumer] - Consumer error: {}".format(msg.error()))
                if ("PARTITION_EOF" in msg.error()):
                    gotIt= True
                continue
            self.traceResponse(msg)
    
    def close(self):
        self.consumer.close()