import time 
from confluent_kafka import KafkaError, Producer
import json, datetime, logging
import os

class KafkaProducer:

    def __init__(self,
                kafka_brokers = "", 
                kafka_user = "", 
                kafka_pwd = "", 
                kafka_cacert = "", 
                kafka_sasl_mechanism = "", 
                topic_name = ""):
        self.kafka_brokers = kafka_brokers
        self.kafka_user = kafka_user
        self.kafka_pwd = kafka_pwd
        self.kafka_sasl_mechanism = kafka_sasl_mechanism
        self.kafka_cacert = kafka_cacert
        self.topic_name = topic_name

    def prepare(self,groupID = "pythonproducers"):
        options ={
                'bootstrap.servers':  self.kafka_brokers,
                'group.id': groupID,
                'delivery.timeout.ms': 15000,
                'request.timeout.ms' : 15000
        }
        if (self.kafka_user != ''):
            options['security.protocol'] = 'SASL_SSL'
            options['sasl.mechanisms'] = self.kafka_sasl_mechanism
            options['sasl.username'] = self.kafka_user
            options['sasl.password'] = self.kafka_pwd
        
        if (self.kafka_cacert != '' ):
            options['ssl.ca.location'] = self.kafka_cacert

        logging.info("--- This is the configuration for the producer: ---")
        logging.info('[KafkaProducer] - {}'.format(options))
        logging.info("---------------------------------------------------")
        self.producer = Producer(options)


    def delivery_report(self,err, msg):
        """ Called once for each message produced to indicate delivery result.
            Triggered by poll() or flush(). """
        if err is not None:
            logging.info( str(datetime.datetime.today()) + ' - Message delivery failed: {}'.format(err))
        else:
            logging.info(str(datetime.datetime.today()) + ' - Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

    def publishEvent(self, eventToSend, keyName):
        dataStr = json.dumps(eventToSend)
        logging.info("Send " + dataStr + " with key " + keyName + " to " + self.topic_name)
        
        self.producer.produce(self.topic_name,
                           key=str(eventToSend[keyName]).encode('utf-8'),
                           value=dataStr.encode('utf-8'),
                           callback=self.delivery_report)
        self.producer.flush()
  

   
  