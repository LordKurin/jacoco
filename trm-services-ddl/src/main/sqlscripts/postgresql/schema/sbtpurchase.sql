drop table if exists SBTPURCHASE CASCADE;

create table SBTPURCHASE (
   CUSTOMERID           VARCHAR(128)         not null,
   LASTTXNTIMESTAMP     TIMESTAMP            null,
   constraint PK_SBTPURCHASE primary key (CUSTOMERID)
)
with (fillfactor=100)
tablespace "PCE_TX";