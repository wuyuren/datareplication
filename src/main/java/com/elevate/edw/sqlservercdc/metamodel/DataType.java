package com.elevate.edw.sqlservercdc.metamodel;

public enum DataType {
	IMAGE, SQL_VARIANT, VARBINARY, TEXT, BINARY,NTEXT, XML,			//large binary objects  
	MONEY, DECIMAL, NUMERIC,FLOAT, SMALLMONEY,  				//decimal, float dobule precision
	INT,   SMALLINT, TINYINT, BIGINT,   BIT,   // integer based 
	VARCHAR,    CHAR,  NVARCHAR,	UNIQUEIDENTIFIER,							//character based
	DATETIME, SMALLDATETIME	,DATE	,DATETIME2, TIMESTAMP								//date and timestamp
}
