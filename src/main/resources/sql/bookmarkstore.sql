DROP TABLE bookmarkstore;
CREATE TABLE bookmarkstore (
consumergroup VARCHAR(100) NOT NULL,
topic VARCHAR(100) NOT NULL,
partnum int NOT NULL,
offset bigint NOT NULL,
metastr varchar(200)
);

create unique index udi_bookmarkstore ON bookmarkstore(consumergroup ,topic,partnum );


CREATE TABLE bookmarkhistory (
bookmarkid bigint not null auto_increment primary key,
consumergroup VARCHAR(100) NOT NULL,
topic VARCHAR(100) NOT NULL,
partnum int NOT NULL,
offset bigint NOT NULL,
metastr varchar(200),
inst_ts timestamp(6) default current_timestamp(6)
);
