create sequence DB2INST1.hibernate_sequence start with 1 increment by 1;

CREATE TABLE DB2INST1.VaccineOrderEntity (
  id INTEGER NOT NULL PRIMARY KEY,
  askingOrganization VARCHAR(255) NOT NULL,
  deliveryLocation VARCHAR(255) NOT NULL,
  priority INTEGER,
  quantity BIGINT,
  status INTEGER,
  vaccineType VARCHAR(50), 
  deliveryDate VARCHAR(50),
  creationDate VARCHAR(50)
);

CREATE TABLE DB2INST1.ORDERCREATEDEVENT (
  id INTEGER NOT NULL PRIMARY KEY,
  order VARCHAR(2046),
  timestamp timestamp
);

CREATE TABLE DB2INST1.ORDERUPDATEDEVENT (
  id INTEGER NOT NULL PRIMARY KEY,
  order VARCHAR(2046),
  timestamp timestamp
);

CREATE TABLE DB2INST1.ORDEREVENTS (
  id varchar(255) NOT NULL PRIMARY KEY,
  aggregatetype VARCHAR(255) NOT NULL,
  aggregateid VARCHAR(255) NOT NULL,
  TYPE VARCHAR(255) NOT NULL,
  timestamp timestamp NOT NULL,
  payload VARCHAR(2046) 
);


INSERT INTO VaccineOrderEntity(id,askingOrganization, deliveryLocation, priority,quantity, status, vaccineType,deliveryDate,creationDate) VALUES (hibernate_sequence.NEXTVAL,'French Governement','Paris',1,150,0,'COVID-19', '30-Mar-2021 01:00:00','12-Nov-2020 02:02:40');
