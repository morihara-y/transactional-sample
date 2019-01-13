create database test;

use test;

CREATE TABLE quantity_trn (
  quantity_code varchar(20) NOT NULL PRIMARY KEY,
  quantity decimal NOT NULL
);

CREATE TABLE quantity_history_trn (
  quantity_history_trn_id nchar(36) NOT NULL PRIMARY KEY,
  quantity_code varchar(20) NOT NULL,
  service_code varchar(20) NOT NULL,
  receipt_issue_trn_id nchar(36) NOT NULL,
  change_quantity decimal NOT NULL
);

CREATE TABLE goods_receipt_trn (
  goods_receipt_trn_id nchar(36) NOT NULL PRIMARY KEY,
  quantity decimal NOT NULL
);

CREATE TABLE goods_issue_trn (
  goods_issue_trn_id nchar(36) NOT NULL PRIMARY KEY,
  quantity decimal NOT NULL
);

show tables from test;
