CREATE TABLE DB2INST1.VaccineOrderEntity (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY
      (START WITH 101, INCREMENT BY 1) PRIMARY KEY,
  askingOrganization VARCHAR(255) NOT NULL,
  deliveryLocation VARCHAR(255) NOT NULL,
  priority INTEGER,
  quantity BIGINT,
  status INTEGER,
  vaccineType VARCHAR(50), 
  deliveryDate VARCHAR(50),
  creationDate VARCHAR(50)
);
INSERT INTO VaccineOrderEntity(id, askingOrganization, deliveryLocation, priority,quantity, status, vaccineType,deliveryDate,creationDate) VALUES (hibernate_sequence.NEXTVAL, 'French Governement','Paris',1,150,0,'COVID-19', '30-Mar-2021 01:00:00','12-Nov-2020 02:02:40');
