# HyperLedger/Fabric SDK Docker Image
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/aberic/fabric-sdk-container/blob/master/LICENSE)
[![fabric-sdk image](https://img.shields.io/docker/build/jrottenberg/ffmpeg.svg)](https://hub.docker.com/r/aberic/fabric-sdk/)
[![Swagger Validator](https://img.shields.io/swagger/valid/2.0/https/raw.githubusercontent.com/OAI/OpenAPI-Specification/master/examples/v2.0/json/petstore-expanded.json.svg)](http://140.143.210.217:8090/swagger-ui.html)
<br>
本项目依赖于任意一个已经部署完成的HyperLedger Fabric网络，并对该网络提供REST API服务，包括通道、智能合约及Fabric网络中节点等管理功能。
<br>
这是一个基于[fabric-sdk-java](https://github.com/hyperledger/fabric-sdk-java)的项目，该项目的主要目的是简化HyperLedger/Fabric开发人员在SDK应用层上的工作流程，使得开发和部署更加简单。
<br><br>
该项目使用方便，只需要部署有Docker及docker compose环境即可轻松调用Fabric网络接口，包括执行、查询智能合约，以及trace相关的溯源接口。
<br><br>
## 开发环境
* IntelliJ IDEA
* Maven
* JDK-1.8
* spring-boot
* Docker
* docker compose
<br><br>
## 版本说明
0.x系列的版本主要是非Docker应用方面的项目，即相互交流的源码层项目。<br>
1.x及以上系列的版本均Docker项目，帮助开发人员快速部署SDK应用，减少开发环节，从而实现业务的快速落地。<br>
提供链接的为已发布版本，未提供连接的为待实现版本。<br><br>
目前主要版本如下列表所示：<br>
[v0.1](https://github.com/abericyang/fabric-sdk-java-app/tree/0.1)：无数据库版，适合轻量级的Fabric平台应用。
<br>
[v0.2](https://github.com/abericyang/fabric-sdk-java-app/tree/0.2)：含关系型数据库版，适合单服务管理多Fabric网络。
<br>
[v1.0-alpha](https://github.com/aberic/fabric-sdk-container/tree/v1.0-alpha)：提供Docker容器服务，方便SDK快速部署。此版本为单排序服务及单节点服务配置，符合绝大部分需求。
<br>
[v1.0-beta](https://github.com/aberic/fabric-sdk-container/tree/v1.0-beta)：新增支持多服务节点。
<br>
[v1.0-beta2](https://github.com/aberic/fabric-sdk-container/tree/v1.0-beta2)：修复重新新增组织、排序服务和节点服务的bug；提供更新组织、排序服务和节点服务的接口；新增Swagger2文档支持。
<br>
[v1.0-beta3](https://github.com/aberic/fabric-sdk-container/tree/v1.0-beta3)：删除docker-sdk.yaml环境变量配置，取消hash标识（容易被误会），细化Fabric网络及数据库结构，简化启动脚本。
<br>
v1.0-RC：新增通过SDK加入通道、安装合约、实例化合约以及升级合约等功能。
<br><br>
## 使用sdk-container
1、确定Linux内核在`3.10`及以上。
<br>
2、在待部署SDK服务器上安装`Docker`及`docker compose`环境。
<br>
3、执行`docker pull aberic/fabric-sdk:1.0-beta3`下载镜像。
<br>
4、在`docker-sdk.yaml`文件中将好Fabric网络中的crypto-config挂载到指定目录下。
<br>
5、执行`docker-compose -f docker-sdk.yaml up`启动SDK镜像服务，如果不需要观察日志，则在命令最后追加`-d`即可。
<br>
6、服务启动完成后，参考下面的API介绍以便更快投入使用。
<br><br>
**docker-sdk.yaml说明**
<br>
关于docker-sdk.yaml编排文件中的参数，主要是[二进制](https://www.cnblogs.com/aberic/p/7542835.html)生成的证书文件目录[crypto-config](https://github.com/aberic/fabric-sdk-container/blob/master/yaml_config_from/crypto-config.yaml)（点击链接自行学习二进制文件生成指定证书文件以及参考crypto-config文件配置）。
<br>                                                                                                                                                                   |

docker-sdk.yaml中的`image: aberic/fabric-sdk`，需要指定其版本号。
<br>
docker-sdk.yaml中volumes使用方法请学习docker compose相关知识。
<br>
docker-sdk.yaml中的ports，后一个为容器中端口号，不用修改，冒号前的可以指定为自身服务器未占用的端口号，最终调用sdk接口时通过冒号前指定的端口号即可。
<br><br>
**API入口文档**

| Method | REST API             | Description                 |
| :--:   | :--                  | :--                         |
| POST   | /state/invoke        | 执行智能合约                  |
| POST   | /state/query         | 查询智能合约                  |
| POST   | /trace/hash          | 根据交易hash查询区块           |
| POST   | /trace/number        | 根据交易区块高度查询区块        |
| POST   | /trace/txid          | 根据交易ID查询区块             |
| GET    | /trace/info/{id}     | 根据当前智能合约id查询当前链信息 |
| POST   | /league/add          | 新增联盟对象                  |
| GET    | /league/list         | 获取联盟对象集合               |
| POST   | /league/update       | 更新联盟对象                  |
| POST   | /org/add             | 新增组织对象                  |
| GET    | /org/list/{id}       | 获取组织对象集合               |
| POST   | /org/update          | 更新组织对象                  |
| POST   | /orderer/add         | 新增排序服务对象               |
| GET    | /orderer/list/{id}   | 获取排序服务对象集合           |
| POST   | /orderer/update      | 更新排序服务对象               |
| POST   | /peer/add            | 新增节点服务对象               |
| GET    | /peer/list/{id}      | 获取节点服务对象集合           |
| POST   | /peer/update         | 更新节点服务对象               |
| POST   | /channel/add         | 新增通道对象                  |
| GET    | /channel/list/{id}   | 获取通道对象集合               |
| POST   | /channel/update      | 更新通道对象                  |
| POST   | /chaincode/add       | 新增链码对象                  |
| GET    | /chaincode/list/{id} | 获取链码对象集合               |
| POST   | /chaincode/update    | 更新链码对象                  |

该版本目前为即上即用的版本，仅提供单排序服务及单节点服务，因此API文档中未提供安装、实例化及升级操作，但在后续更新中，会支持安装、实例化及升级的功能。如果有PAAS服务的需要，可以自行参考v0.2中的方案来解决。
<br>
### API要点
由于提供了[swagger2](https://github.com/aberic/fabric-sdk-container/blob/v1.0-beta2/swagger2.json)接口文档，在本文档中就不再赘述接口样例，可自行在[swagger-editor](http://editor.swagger.io/)进行查阅。
<br>
也可以通过当前部署fabric-sdk-container服务器的ip和配置文件中映射的端口号进行访问，如`http://ip:port/swagger-ui.html`。
<br><br>
这里有几个点需要说明一下：
<br>
1、组织hash可以通过/sdk/org/list接口获取，这个接口获取的是一个集合，请选择你所需要的组织hash。
<br>
2、args是调用合约传入的参数，在用go编写智能合约的时候，智能合约所接收的参数为一个字符串数组，其中字符串数组的第一个参数是智能合约的方法名。chaincode接口中的args所传入的参数就是智能合约所接收的数组参数。
<br>
3、新增排序服务和节点服务中的hash来自新增组织的api回调结果，即必须先新增组织，然后在该组织下新增排序服务和节点服务，以此来完成一个Fabric的组网操作。
## 代码简要说明
### sdk-advance
sdk-advance是基于fabric-sdk-java v1.1的服务，其主要目的是为了更简单的使用fabric-sdk-java，对原有的调用方法做了进一步封装，主要提供了各种中转对象，如智能合约、通道、排序服务、节点、用户等等，最终将所有的中转对象交由一个中转组织来负责配置，其对外提供服务的方式则交给FabricManager来掌管。
<br><br>
该项目仅作为学习分享的形式提交维护，关于生产部署方面，sdk-advance与app的交互可自行选择采用thrift或protobuf等数据传输协议实现，这里并没有提供该方案的具体实现，需要自己动手解决。
<br><br>
#### sdk-advance-intermediate
intermediate系列对象是该项目的主要封装对象，间接屏蔽了真实应用层与fabric-sdk-java之间的直接交互。
* [IntermediateOrderer](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/IntermediateOrderer.java)<br>
对于Orderer，我们需要知道所选组织名称，同时还需要知道Orderer的服务器域名及真实访问地址。这里的服务器域名是指由crypto-config.yaml中进行指定。<br>
该对象主要提供了对Orderer的服务器域名及真实访问地址的封装。
<br><br>
* [IntermediatePeer](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/IntermediatePeer.java)<br>
Peer是部署于本地节点的配置对象，主要包括当前指定的组织节点域名、当前指定的组织节点事件域名、当前指定的组织节点访问地址、当前指定的组织节点事件监听访问地址以及当前peer是否增加Event事件处理。这里的主要配置信息也由crypto-config.yaml中进行指定
<br><br>
* [IntermediateChannel](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/IntermediateChannel.java)<br>
每一个Peer所加入的Channel都有独立的通道信息，包括名称或通道tx文件等。IntermediateChannel中定义了通道名称，除此之外，还针对上面两个对象进行了调用，并最终生成了Channel对象。<br>
IntermediateChannel中已经开始提供对外服务了，主要依赖于Channel对象所提供的，包括最基本的Peer加入频道的方法。<br>
另外，IntermediateChannel还提供了一系列基本的溯源方法，包括查询当前频道的链信息，包括链长度、当前最新区块hash以及当前最新区块的上一区块hash、在指定频道内根据transactionID查询区块、在指定频道内根据hash查询区块以及在指定频道内根据区块高度查询区块等方法。
<br><br>
* [IntermediateChaincodeID](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/IntermediateChaincodeID.java)<br>
IntermediateChaincodeID对象与IntermediateChannel相似，除了包含了必须的成员参数外，也提供了一些对外服务。在IntermediateChaincodeID中主要对智能合约名称、包含智能合约的go环境路径、智能合约安装路径、智能合约版本号、指定ID的智能合约以及单个提案请求的超时时间以毫秒为单位和事务等待时间以秒为单位。<br>
同时对外提供了安装智能合约、实例化智能合约、升级智能合约、执行智能合约以及查询智能合约的服务。
<br><br>
* [IntermediateUser](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/IntermediateUser.java)<br>
Fabric中有用户的概念，当然除了用户之外，在1.1中也有组织、节点等基于CA服务的概念。IntermediateUser主要就是用来处理用户CA信息的，目前尚未对此类服务提供更好的封装策略，后续更新中会逐步加入进来，以便提供更简便的实现方案。当前的具体实现可参考[IntermediateOrg](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/IntermediateOrg.java)。
<br><br>
* [IntermediateOrg](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/IntermediateOrg.java)<br>
上述无论是排序服务、节点服务或是通道、合约等内容，都应该被同一个组织所拥有，这里的同一个是指单体组织，而不是只为一个组织服务，整个基于Fabric的区块链网络至少应该为一个组织提供服务。<br>
在IntermediateOrg中集合了已有的对象信息，并从中生成了组织用户以便调用Fabric网络中的接口数据。案例中的方案并非最优方案，关于Fabric CA的实践还没有完成（按照官方的Demo实现无法跑通，如果有能够实现的朋友，欢迎提交代码）。
<br><br>
#### sdk-advance-manager
* [OrgManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/OrgManager.java)<br>
组织生成器，该对象已经完全暴露给APP所使用，可以通过[SimpleManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/simple/src/main/java/cn/aberic/simple/module/manager/SimpleManager.java)对象查看其主要用法。通过OrgManager对外暴露的服务生成系列Intermediate对象，并最终使用use方法确定调用的组织具体对象来生成真正提供Fabric区块链网络服务的[FabricManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/FabricManager.java)。<br>
在使用OrgManager的时候，如果有进一步开发SAAS或更深层BAAS服务的想法，建议不要直接使用[SimpleManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/simple/src/main/java/cn/aberic/simple/module/manager/SimpleManager.java)中的方案，而是结合关系型数据库如MySQL等解决方案，将组织中的相关数据持久化，以便后台可以直接获取并调用。
<br><br>
* [FabricManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/FabricManager.java)<br>
区块链网络服务管理器FabricManager，作为APP直接调用Fabric区块链网络的入口对象，该对象提供了Channel和ChaincodeID相关的所有接口。
<br><br>
### simple
simple是一个基于spring-boot的项目，在simple中主要关注[SimpleManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/simple/src/main/java/cn/aberic/simple/module/manager/SimpleManager.java)对象的使用。<br>
**我的这个simple中的ip的自己申请的服务器，大家可以随便测试，但不保证有效期，建议自行搭建本地服务测试。**
<br><br>
欢迎与我多多交流：<br>
技术博客：[HyperLedger/Aberic](http://www.cnblogs.com/aberic/)<br>
HyperLedger/Fabric**微信交流群**，扫微信订阅号加入：<br>
![HLFStudy](https://images2017.cnblogs.com/blog/1240530/201802/1240530-20180201103733812-1730907548.jpg "HLFStudy 微信订阅号")
