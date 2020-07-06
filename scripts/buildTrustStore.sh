openssl x509 -outform der -in ./certs/mongo.pem -out ./certs/mongo.der
keytool -import -alias mongo -keystore ./certs/cacerts -file ./certs/mongo.der
