**注意：** 这是一个依赖于已经部署好的HyperLedger Fabric项目，关于前者搭建部署的方案可以通过参考[从零开始](https://www.cnblogs.com/aberic/category/1148898.html)或购买[开发实战](https://item.jd.com/12381034.html?dist=jd)等途径学习。目前这些都还不在当前项目的支持范围内，未来也许会计划开发一套基于Fabric深度定制的一键部署及管理平台（非K8S）。
<br>
# Fabric Net Server [![fabric-sdk image](https://img.shields.io/badge/made%20by-aberic-orange.svg)](http://www.cnblogs.com/aberic/)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/aberic/fabric-sdk-container/blob/master/LICENSE)
[![fabric-sdk image](https://img.shields.io/docker/build/jrottenberg/ffmpeg.svg)](https://hub.docker.com/r/aberic/)
[![Join the chat at https://gitter.im/fabric-net-server/Lobby](https://badges.gitter.im/fabric-net-server/Lobby.svg)](https://gitter.im/fabric-net-server/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
<br><br>
本项目目前提供一套Fabric网络后台服务，包括对已经在Fabric网络中创建好的org、peer、channel、chaincode等进行网络新增，并基于已经新增成功的内容提供[REST API](https://github.com/aberic/fabric-net-server/blob/master/API_DEMO.md)服务，目前仅支持智能合约的执行、查询以及溯源功能，未来计划支持后台动态加入通道、安装合约、实例化合约等基于[fabric-sdk-java](https://github.com/hyperledger/fabric-sdk-java)已有接口的实现。
<br><br>
本项目开发环境和框架大致有：IntelliJ IDEA、Maven、JDK8+、spring-boot、thrift、thymeleaf以及Docker等。具体实践可使用项目中提供的Docker镜像部署，也可以自定义源码二次开发。
<br><br>
由于个人人力实在有限，后面的工程和工作量会逐步变大变多，在保证个人工作顺利的情况下，会将大部分精力都投入到本项目中。
<br><br>
## 项目预览
后台首页界面预览
<br>
![首页视图](https://raw.githubusercontent.com/aberic/fabric-sdk-container/master/img/index.jpeg "Fabric 网络")
<br><br>
api执行介绍界面预览
<br><br>
![首页视图](https://raw.githubusercontent.com/aberic/fabric-sdk-container/master/img/chaincode1.jpeg "Fabric 网络")
<br><br>
api执行结果及使用方案界面预览
<br>
![首页视图](https://raw.githubusercontent.com/aberic/fabric-sdk-container/master/img/chaincode2.jpeg "Fabric 网络")
## 版本说明
1、如有源码学习需要的，可以参阅[v0.1](https://github.com/abericyang/fabric-net-server/tree/0.1)或含MySQL数据库的[v0.2](https://github.com/abericyang/fabric-net-server/tree/0.2)版本。
<br>
2、从[v1.0-RC](https://github.com/aberic/fabric-net-server/tree/1.0-RC1)开始提供后台视图服务，之前的beta版仅提供接口方案。
<br>
3、如有二次开发需求，建议以[v0.1](https://github.com/abericyang/fabric-net-server/tree/0.1)为蓝本。
<br>
4、下一版计划实现安装、实例化智能合约及加入通道等功能。
<br><br>
## 使用
1、确定Linux内核在`3.10`及以上。
<br>
2、在待部署SDK服务器上安装`Docker`及`docker compose`环境。
<br>
3、执行`docker pull aberic/fabric-service:1.0-RC1`及`docker pull aberic/fabric-edge:1.0-RC1`下载两个镜像。
<br>
4、编辑`docker-fabric-net-server.yaml`，参考。
<br>
5、执行`docker-compose -f docker-fabric-net-server.yaml up`启动SDK镜像服务，如果不需要观察日志，则在命令最后追加`-d`即可。
<br>
6、服务启动完成后，通过http://localhost:port 访问即可。
<br><br>
#### docker-fabric-net-server.yaml说明
```yaml
version: '2'

services:

  service:
    container_name: service
    image: aberic/fabric-service
    command: bash /home/init-service.sh
    ports:
      - 8081:8081

  edge:
    container_name: edge
    image: aberic/fabric-edge
    environment:
      # service地址配置
      - SERVICE_IP=10.163.90.220
      - SERVICE_PORT=8081
    command: bash /home/init-edge.sh
    ports:
      - 8080:8080
    depends_on:
      - service
```
1、上述配置仅为demo，主要关注自己的端口映射，edge的端口号决定了项目访问的最终地址。
<br>
2、edge中SERVICE_IP和SERVICE_PORT两个变量的值取决于service的部署情况，理论上可以在一台物理机中部署即可。
<br>
3、yaml启动需要指定镜像版本号，或tag镜像版本号为latest。
<br><br>
#### 架构（请无视小图标）
![FabricNet](https://raw.githubusercontent.com/aberic/fabric-sdk-container/master/img/FabricNet.png "Fabric 网络")
<br>
如上图，需要对Fabric网络有一个简单的理解一致，并对以下几个点说明一下：
<br>
1、一个Fabric网络即是一个league（联盟链），一个league包含一个或多个org（组织）且含有不少于一个orderer（排序服务），一个org包含一个或多个peer（节点服务），一个peer可以加入一个或多个channel（通道），一个channel可以安装一个或多个chaincode（智能合约）。
<br>
2、根据1所述，仅有league对象集合查询不需要指定id。org集合查询需要指定联盟id，orderer及peer集合查询需要指定orgId，channel集合查询需要指定peerId，chaincode集合查询需要指定channelId。
<br>
3、可以根据某一个chaincode逆向查出该chaincode的整个league网络，因此执行state和trace接口时只需要传入chaincodeId即可。
<br>
4、执行state接口中的`strArray`是调用Fabric网络中部署的chaincode所传入的参数，在用go编写chaincode的时候，chaincode所接收的参数为一个字符串数组，其中字符串数组的第一个参数是chaincode的方法名。chaincode接口中的args所传入的参数就是智能合约所接收的数组参数。如[chaincode_example02](https://github.com/aberic/fabric-net-server/blob/master/chaincode_example02/chaincode_example02.go)中执行invoke时传入的参数为`["invoke", "a", "b", "10"]`，那么在本项目中执行state接口时传入的参数即为`[invoke,a,b,10]`，与Fabric网络中安装的chaincode所需参数保持一致，实现与业务解耦，仅用于维护Fabric项目和做业务与chaincode的桥梁，不参与实际业务当中。
<br>
5、执行trace接口可使用项目中在chaincode管理中提供的测试工具进行测试，上一步中的操作也可以通过该方法测试，测试结果会将正确的请求方式显示出来，以便对外提供服务。
<br>
6、Fabric网络新增操作必须遵循第2点所述，即必须从league开始新增，直到最后新增chaincode。
<br>
7、新增org中需要上传用`zip`压缩的`crypto-config`目录，`crypto-config`是[二进制](https://www.cnblogs.com/aberic/p/7542835.html)生成的证书文件目录。目录下的内容可参考[crypto-config.dir](https://github.com/aberic/fabric-net-server/tree/master/crypto-config)，配置方案可参考[crypto-config](https://github.com/aberic/fabric-sdk-container/blob/master/yaml_config_from/crypto-config.yaml)。
<br><br>
#### 对象新建字段部分释义
league、org、orderer、peer、channel及chaincode中后台创建所需的字段有如下表中释义：
<br>

| Params                       | Description                  | Map                                                                                                                                                                                               |
| :--                          | :--                          | :--                                                                                                                                                                                               |
| ORG_NAME                     | 节点所属组织名称               | 参见[crypto-config](https://github.com/aberic/fabric-sdk-container/blob/master/yaml_config_from/crypto-config.yaml)文件中 -> PeerOrgs-Name                                                          |
| ORG_TLS                      | 节点是否开启TLS                | 根据自身创建网络情况选择true或false                                                                                                                                                                   |
| ORG_USERNAME                 | 节点所属组织用户名称            | 参见[crypto-config](https://github.com/aberic/fabric-sdk-container/tree/master/crypto-config/peerOrganizations/org1.example.com/users)目录下的两个用户，默认配置中选择的Admin                           |
| ORG_CRYPTO_CONFIG_DIR        | 映射到容器中的crypto-config目录 | 即[crypto-config](https://github.com/aberic/fabric-sdk-container/tree/master/crypto-config/peerOrganizations/org1.example.com/users)目录                                                           |
| ORG_MSP_ID                   | 节点所属组织ID                 | 参见[configtx](https://github.com/aberic/fabric-sdk-container/blob/master/yaml_config_from/configtx.yaml)文件中 -> Organizations-&Org1-Name                                                         |
| ORG_DOMAIN_NAME              | 节点所属组织域名名称            | 参见[crypto-config](https://github.com/aberic/fabric-sdk-container/blob/master/yaml_config_from/crypto-config.yaml)文件中 -> PeerOrgs-Domain                                                        |
| ORG_ORDERER_DOMAIN_NAME      | 节点所属排序服务域名名称         | 参见[crypto-config](https://github.com/aberic/fabric-sdk-container/blob/master/yaml_config_from/crypto-config.yaml)文件中 -> OrdererOrgs-Domain                                                     |
| ORDERER_NAME                 | 排序服务名称                   | 参见[configtx](https://github.com/aberic/fabric-sdk-container/blob/master/yaml_config_from/configtx.yaml)文件中 -> Orderer-Addresses                                                                 |
| ORDERER_LOCATION             | 排序服务访问路径                | 根据自身设置实际情况修改，一般为`grpc://host:port`的格式                                                                                                                                                |
| PEER_NAME                    | 节点服务域名名称                | 参见[crypto-config](https://github.com/aberic/fabric-sdk-container/tree/master/crypto-config/peerOrganizations/org1.example.com/peers)目录下的节点域名列表                                            |
| PEER_EVENT_HUB_NAME          | 节点服务事件域名名称            | 同上                                                                                                                                                                                                |
| PEER_LOCATION                | 节点服务路径                   | 根据自身设置实际情况修改，一般为`grpc://host:port`的格式                                                                                                                                                |
| PEER_EVENT_HUB_LOCATION      | 节点服务事件路径                | 根据自身设置实际情况修改，一般为`grpc://host:port`的格式                                                                                                                                                |
| PEER_IS_EVENT_LISTENER       | 节点所属组织名称                | 根据自身需求选择是否监听回调服务                                                                                                                                                                       |
| CHANNEL_NAME                 | 自行创建的通道名称              | 如：`peer channel create -o orderer.example.com:7050 -c mychannel -t 50 -f ./channel-artifacts/mychannel.tx` 命令所创建的mychannel                                                                   |
| CHAINCODE_NAME               | 智能合约名称                   | 如：`peer chaincode install -n testcc -p github.com/hyperledger/fabric/aberic/chaincode/go/chaincode_example02 -v 1.0 `命令所创建的testcc                                                            |
| CHAINCODE_PATH               | 智能合约路径                   | 如：`peer chaincode install -n testcc -p github.com/hyperledger/fabric/aberic/chaincode/go/chaincode_example02 -v 1.0 `命令中的github.com/hyperledger/fabric/aberic/chaincode/go/chaincode_example02 |
| CHAINCODE_VERSION            | 智能合约版本                   | 如：`peer chaincode install -n testcc -p github.com/hyperledger/fabric/aberic/chaincode/go/chaincode_example02 -v 1.0 `命令中的1.0                                                                   |
| CHAINCODE_PROPOSAL_WAIT_TIME | 单个提案请求超时时间以毫秒为单位  | 默认90000                                                                                                                                                                                           |
| CHAINCODE_INVOKE_WAIT_TIME   | 事务等待时间以秒为单位           | 默认120                                                                                                                                                                                            |

<br>

**API参考**

| Method | REST API             | Description                 |
| :--:   | :--                  | :--                         |
| POST   | /state/invoke        | 执行智能合约                  |
| POST   | /state/query         | 查询智能合约                  |
| POST   | /trace/hash          | 根据交易hash查询区块           |
| POST   | /trace/number        | 根据交易区块高度查询区块        |
| POST   | /trace/txid          | 根据交易ID查询区块             |
| GET    | /trace/info/{id}     | 根据当前智能合约id查询当前链信息 |

API文档中未提供安装、实例化及升级操作，但在后续更新中，会支持安装、实例化及升级的功能。如果有SaaS/BaaS服务的紧急需求，可以自行参考[v0.1](https://github.com/abericyang/fabric-net-server/tree/0.1)中的方案来解决。
<br>

##讨论
* [HyperLedger/Aberic](http://www.cnblogs.com/aberic/)
<br>

##社群
* 扫微信订阅号加入：
<br>
![HLFStudy](https://images2017.cnblogs.com/blog/1240530/201802/1240530-20180201103733812-1730907548.jpg "HLFStudy 微信订阅号")
<br>

##入门书籍
[《HyperLedger Fabric开发实战——快速掌握区块链技术》](https://item.jd.com/12381034.html?dist=jd)
<br>

[![HyperLedger Fabric开发实战](https://images2018.cnblogs.com/blog/1240530/201806/1240530-20180614234142771-2017750800.png "HyperLedger Fabric开发实战")](https://item.jd.com/12381034.html?dist=jd)
