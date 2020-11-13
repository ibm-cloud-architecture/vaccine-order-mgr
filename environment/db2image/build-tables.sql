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
