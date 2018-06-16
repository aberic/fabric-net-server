# Simple模块中的sql
在[v1.0-alpha](https://github.com/abericyang/fabric-sdk-java-app/tree/v1.0-alpha)版本中，并没有使用关联SQL辅助Fabric组织管理，一般情况下，每一个单一组织也无需用到SQL来进行管理或维护。但如果将SDK作为其它应用服务的中间件来使用的话，或许会遇到需要连接多个Fabric网络的情况，这时候就需要用到SQL来管理各个不同的Fabric网络。当然也能用类似Properties配置文件或JSON文件的方式来管理。
<br><br>
本说明将依赖于v1.0-alpha版本，使用的是SQLite作为其关系型数据库。
<br><br>
## SQLite操作步骤
* 创建league表
```sqlite
CREATE TABLE "league" (
	 "name" text(256,0) NOT NULL,
	PRIMARY KEY("name")
)
```
* 创建org表
```sqlite
CREATE TABLE "org" (
	 "name" text(45,0) NOT NULL,
	 "tls" integer(1,0) NOT NULL,
	 "username" text(45,0) NOT NULL,
	 "crypto_config_dir" text(500,0) NOT NULL,
	 "msp_id" text(45,0) NOT NULL,
	 "domain_name" text(128,0) NOT NULL,
	 "orderer_domain_name" text(128,0) NOT NULL,
	 "league_id" integer(9,0) NOT NULL,
	PRIMARY KEY("name")
)
```
* 创建orderer表
```sqlite
CREATE TABLE "orderer" (
	 "name" text(128,0) NOT NULL,
	 "location" text(256,0) NOT NULL,
	 "org_id" integer(9,0) NOT NULL,
	PRIMARY KEY("name")
)
```
* 创建peer表
```sqlite
CREATE TABLE "peer" (
	 "name" text(128,0) NOT NULL,
	 "event_hub_name" text(128,0) NOT NULL,
	 "location" text(256,0) NOT NULL,
	 "event_hub_location" text(256,0) NOT NULL,
	 "event_listener" integer(1,0) NOT NULL DEFAULT 0,
	 "org_id" integer(9,0) NOT NULL,
	PRIMARY KEY("name")
)
```
* 创建channel表
```sqlite
CREATE TABLE "channel" (
	 "name" text(128,0) NOT NULL,
	 "peer_id" integer(9,0) NOT NULL,
	PRIMARY KEY("name")
)
```
* 创建chaincode表
```sqlite
CREATE TABLE "chaincode" (
	 "name" text(45,0) NOT NULL,
	 "path" text(128,0) NOT NULL,
	 "version" text(45,0) NOT NULL,
	 "proposal_wait_time" integer(9,0) NOT NULL DEFAULT 90000,
	 "invoke_wait_time" integer(9,0) NOT NULL DEFAULT 120,
	 "channel_id" integer(9,0) NOT NULL,
	PRIMARY KEY("name")
)
```



