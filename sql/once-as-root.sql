DROP DATABASE IF EXISTS spring;
CREATE DATABASE IF NOT EXISTS spring;
USE `spring`;
Drop User 'spring'@'localhost';
Drop User 'spring'@'%';
Create User 'spring'@'localhost' Identified BY 'spring';
Create User 'spring'@'%' Identified BY 'spring';
Grant All on spring.* to 'spring'@'localhost';
Grant All on spring.* to 'spring'@'%';
