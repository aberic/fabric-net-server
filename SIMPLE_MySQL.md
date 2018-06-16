# Simple模块中的sql
在[v0.1](https://github.com/abericyang/fabric-sdk-java-app/tree/v0.1)版本中，并没有使用关联SQL辅助Fabric组织管理，一般情况下，每一个单一组织也无需用到SQL来进行管理或维护。但如果将SDK作为其它应用服务的中间件来使用的话，或许会遇到需要连接多个Fabric网络的情况，这时候就需要用到SQL来管理各个不同的Fabric网络。当然也能用类似Properties配置文件或JSON文件的方式来管理。
<br><br>
本MD说明将依赖于v0.2版本，使用的是MySQL作为其关系型数据库。但对于数据库的选择和用法有很多，各自按照自己的喜好修改设定。
<br><br>
## MySQL操作步骤
* 创建db_fabric库
```mysql
CREATE SCHEMA `fabric` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
```
* 创建org表
```mysql
CREATE TABLE `org` (
  `chainCodeId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `org_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '组织名称',
  `tls` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否开启TLS，0不开启，1开启',
  `ca_tls` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否开启CA TLS，0不开启，1开启',
  `username` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '设置默认用户',
  `crypto_config_dir` varchar(45) COLLATE utf8_bin NOT NULL COMMENT 'CryptoConfig所在目录的目录名称，该目录这里设为统一存放目录。也可以用更灵活的方式，通过上传进行配置',
  `channel_artifacts_dir` varchar(45) COLLATE utf8_bin NOT NULL COMMENT 'ChannleArtifacts所在目录的目录名称，该目录这里设为统一存放目录。也可以用更灵活的方式，通过上传进行配置',
  `ca_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT 'CA名称',
  `ca_location` varchar(45) COLLATE utf8_bin NOT NULL COMMENT 'CA请求URL',
  `org_msp_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '组织唯一标识符',
  `org_domain_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '组织域名',
  `orderer_domain_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '排序服务域名',
  `channel_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '当前组织所访问的通道名称',
  `chaincode_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '智能合约名称',
  `chaincode_source` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '包含智能合约的go环境路径',
  `chaincode_path` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '智能合约路径',
  `chaincode_version` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '智能合约版本',
  `proposal_wait_time` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '单个提案请求的超时时间以毫秒为单位',
  `invoke_wait_time` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '事务等待时间以秒为单位',
  PRIMARY KEY (`chainCodeId`),
  UNIQUE KEY `id_UNIQUE` (`chainCodeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='使用指定用户访问指定通道指定智能合约的组织信息表'
```
* 创建Orderer表
```mysql
CREATE TABLE `fabric`.`orderer` (
  `chainCodeId` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '排序服务器ID',
  `org_id` INT NOT NULL COMMENT '排序服务器所属组织ID',
  `name` VARCHAR(128) NOT NULL COMMENT '排序服务器名称',
  `location` VARCHAR(256) NOT NULL COMMENT '排序服务器地址',
  PRIMARY KEY (`chainCodeId`),
  UNIQUE INDEX `id_UNIQUE` (`chainCodeId` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = '排序服务器表';
```
* 创建Peer表
```mysql
CREATE TABLE `fabric`.`peer` (
  `chainCodeId` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `org_id` INT NOT NULL COMMENT '节点服务器所属组织ID',
  `peer_name` VARCHAR(128) NULL COMMENT '当前指定的组织节点域名',
  `peer_event_hub_name` VARCHAR(128) NULL COMMENT '当前指定的组织节点事件域名',
  `peer_location` VARCHAR(256) NULL COMMENT '当前指定的组织节点访问地址',
  `peer_event_hub_location` VARCHAR(256) NULL COMMENT '当前指定的组织节点事件监听访问地址',
  `event_listener` TINYINT NULL DEFAULT 0 COMMENT '当前peer是否增加Event事件处理，0不监听，1监听',
  PRIMARY KEY (`chainCodeId`),
  UNIQUE INDEX `id_UNIQUE` (`chainCodeId` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = '节点服务器表';
```


