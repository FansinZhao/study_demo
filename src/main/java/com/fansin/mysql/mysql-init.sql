--
CREATE TABLE test.replication_table
(
  id   BIGINT(10) not NULL auto_increment,
  name VARCHAR(20),
  demo VARCHAR(30),
  PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
CREATE TABLE test.replication_table
(
  id   BIGINT(10) not NULL auto_increment,
  name VARCHAR(20),
  demo VARCHAR(30),
  PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10001
  DEFAULT CHARSET = utf8;
CREATE TABLE test.replication_table
(
  id   BIGINT(10) not NULL auto_increment,
  name VARCHAR(20),
  demo VARCHAR(30),
  PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20001
  DEFAULT CHARSET = utf8;
