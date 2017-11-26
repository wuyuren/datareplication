CREATE TABLE versionstore (
  storekey varchar(30) NOT NULL,
  tablename varchar(300) NOT NULL,
  lastsyncversion bigint(20) NOT NULL,
  last_upd_ts timestamp(6) not null default current_timestamp(6)
  )
  ;
  
  create unique index idx_vstore on versionstore(storekey,tablename);
  