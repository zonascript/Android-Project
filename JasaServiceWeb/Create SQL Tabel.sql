CREATE TABLE TBL_USER
(
	ID_USER VARCHAR(50) NOT NULL PRIMARY KEY,
	[USER_NAME] VARCHAR(150) NULL,
	NO_TELFON VARCHAR(15) NULL,
	EMAIL VARCHAR(50) NULL,
	PASSWORD VARCHAR(MAX) NULL,
	JENIS_USER VARCHAR(10) NULL,
	FOTO_PROFIL VARCHAR(50) NULL,
	CURRENT_LONGITUDE VARCHAR(MAX) NULL,
	CURRENT_LATITUDE VARCHAR(MAX) NULL,
	[STATUS] VARCHAR(10) NULL,
	TOKEN VARCHAR(MAX) NULL
)

CREATE TABLE TBL_SERVICE
(
	ID_SERVICE VARCHAR(10) NOT NULL PRIMARY KEY,
	[SERVICE_NAME] VARCHAR(100) NULL,
	SERVICE_ICON VARCHAR(100) NULL
)

--DROP TABLE TBL_SERVICE_ITEM
CREATE TABLE TBL_SERVICE_ITEM
(
	ID_SERVICE_ITEM INT NOT NULL PRIMARY KEY,
	FID_SERVICE VARCHAR(10) NULL,
	ITEM_NAME VARCHAR(100) NULL,
	JENIS_INPUT VARCHAR(100) NULL
)

CREATE TABLE TBL_SERVICE_PROVIDE
(
	ID_SERVICE_PROVIDE VARCHAR(100) NOT NULL PRIMARY KEY,
	FID_SERVICE VARCHAR(10) NULL,
	[SERVICE_NAME] VARCHAR(100) NULL,
	[STATUS] VARCHAR(10) NULL,
	[DESCRIPTION] VARCHAR(100) NULL,
	[PENGALAMAN] VARCHAR(250) NULL
)

--DROP TABLE TBL_DROPDOWN_LIST
CREATE TABLE TBL_DROPDOWN_LIST
(
	ID_DROPDOWN VARCHAR(100) NOT NULL PRIMARY KEY,
	FID_SERVICE_ITEM INT NULL,
	DESCRIPTION VARCHAR(150),
	ALIAS VARCHAR(50)		
)

CREATE TABLE TBL_REASON_LIST
(
	ID_REASON INT NOT NULL PRIMARY KEY,
	PARAMETER VARCHAR(50),
	DESCRIPTION VARCHAR(150)
)

CREATE TABLE TBL_REQUEST
(
	ID_REQUEST VARCHAR(100) NOT NULL PRIMARY KEY,
	FID_SERVICE VARCHAR(10) NULL,
	FID_SERVICE_PROVIDE VARCHAR(100) NULL,
	FID_USER_CREATE VARCHAR(50) NULL,
	FID_USER_ACCEPT VARCHAR(50) NULL,
	[STATUS] VARCHAR(50) NULL,
	GPS_IMAGE VARCHAR(150),
	LONGITUDE VARCHAR(MAX),
	LATITUDE VARCHAR(MAX),
	[ADDRES] VARCHAR(MAX),
	ADDRES_DESCRIPTION VARCHAR(250),
	START_TIME VARCHAR(50),
	FINISH_TIME VARCHAR(50),
	CREATE_DATE DATETIME NULL,
	FINISH_COMMNET_USER VARCHAR(150),
	HASIL_SERVICE VARCHAR(50)
)

--DROP TABLE TBL_REQUEST_DETAIL
CREATE TABLE TBL_REQUEST_DETAIL
(
	ID_REQUEST_DETAIL VARCHAR(100) NOT NULL PRIMARY KEY,
	FID_REQUEST VARCHAR(100) NULL,
	FID_SERVICE_ITEM INT,
	SERVICE_ITEM_NAME VARCHAR(100) NULL,
	SERVICE_ITEM_VALUE VARCHAR(MAX) NULL,
	JENIS_INPUT VARCHAR(50) NULL,
	SATUAN VARCHAR(10) NULL
)

CREATE TABLE TBL_REQUEST_HISTORY
(
	ID_HISTORY VARCHAR(150) NOT NULL PRIMARY KEY,
	FID_REQUEST VARCHAR(100) NULL,
	FID_REASON INT NULL,
	[STATUS] VARCHAR(50) NULL,
	REASON VARCHAR(150) NULL,
	CREATE_BY VARCHAR(50) NULL,
	CREATE_DATE DATETIME NULL
)