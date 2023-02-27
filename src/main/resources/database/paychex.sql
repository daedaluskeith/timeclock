DROP TABLE IF EXISTS `breaks`;
DROP TABLE IF EXISTS `shifts`;
DROP TABLE IF EXISTS `clockusers`;
DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE USERS (
    USERNAME VARCHAR(128) PRIMARY KEY,
    PASSWORD VARCHAR(128) NOT NULL,
    ENABLED CHAR(1)  NOT NULL
);

CREATE TABLE AUTHORITIES (
    USERNAME VARCHAR(128) NOT NULL,
    AUTHORITY VARCHAR(128) NOT NULL
);
ALTER TABLE AUTHORITIES ADD CONSTRAINT AUTHORITIES_UNIQUE UNIQUE (USERNAME, AUTHORITY);
ALTER TABLE AUTHORITIES ADD CONSTRAINT AUTHORITIES_FK1 FOREIGN KEY (USERNAME) REFERENCES USERS (USERNAME);

CREATE TABLE `clockusers` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `username` varchar(36) NOT NULL,
  `usertype` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
);

CREATE TABLE `shifts` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `userid` int unsigned NOT NULL,
  `starttime` datetime NOT NULL,
  `endtime` datetime DEFAULT NULL,
  `onbreak` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `shifts_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `clockusers` (`id`)
);

CREATE TABLE `breaks` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `shiftid` int unsigned NOT NULL,
  `breaktype` varchar(10) NOT NULL,
  `starttime` datetime NOT NULL,
  `endtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shiftid` (`shiftid`),
  CONSTRAINT `breaks_ibfk_1` FOREIGN KEY (`shiftid`) REFERENCES `shifts` (`id`)
);

