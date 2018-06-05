# HyperLedger/Fabric JAVA-SDK with 1.1
该项目介绍如何使用fabric-sdk-java框架，基于fabric-sdk-java v1.1正式版开发，可向下兼容1.0版本。
<br><br>
该项目没有对原JAVA-SDK做修改，主要是结合HyperLedger Fabric与fabric-sdk-java中的交互方式做了个人感觉更为清晰的描述，希望能够帮助更多的人尽快熟悉fabric-sdk-java的操作流程和方式。
<br><br>
## 开发环境
* IntelliJ IDEA
* Maven
* JDK-1.8
* spring-boot
<br><br>
## 版本说明
此版本为无数据库版，适合轻量级的Fabric平台应用。
<br><br>
## sdk-advance
sdk-advance是基于fabric-sdk-java v1.1的服务，其主要目的是为了更简单的使用fabric-sdk-java，对原有的调用方法做了进一步封装，主要提供了各种中转对象，如智能合约、通道、排序服务、节点、用户等等，最终将所有的中转对象交由一个中转组织来负责配置，其对外提供服务的方式则交给FabricManager来掌管。
<br><br>
该项目仅作为学习分享的形式提交维护，关于生产部署方面，sdk-advance与app的交互可自行选择采用thrift或protobuf等数据传输协议实现，这里并没有提供该方案的具体实现，需要自己动手解决。
<br><br>
### sdk-advance-intermediate
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
### sdk-advance-manager
* [OrgManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/OrgManager.java)<br>
组织生成器，该对象已经完全暴露给APP所使用，可以通过[SimpleManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/simple/src/main/java/cn/aberic/simple/module/manager/SimpleManager.java)对象查看其主要用法。通过OrgManager对外暴露的服务生成系列Intermediate对象，并最终使用use方法确定调用的组织具体对象来生成真正提供Fabric区块链网络服务的[FabricManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/FabricManager.java)。<br>
在使用OrgManager的时候，如果有进一步开发SAAS或更深层BAAS服务的想法，建议不要直接使用[SimpleManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/simple/src/main/java/cn/aberic/simple/module/manager/SimpleManager.java)中的方案，而是结合关系型数据库如MySQL等解决方案，将组织中的相关数据持久化，以便后台可以直接获取并调用。
<br><br>
* [FabricManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/sdk-advance/src/main/java/org/hyperledger/fabric/sdk/aberic/FabricManager.java)<br>
区块链网络服务管理器FabricManager，作为APP直接调用Fabric区块链网络的入口对象，该对象提供了Channel和ChaincodeID相关的所有接口。
<br><br>
## simple
simple是一个基于spring-boot的项目，在simple中主要关注[SimpleManager](https://github.com/abericyang/fabric-sdk-java-app/blob/master/simple/src/main/java/cn/aberic/simple/module/manager/SimpleManager.java)对象的使用，该对象的使用建议根据自身业务的实际需求重新包装上线，但直接基于此项目应用也没什么大问题。我的这个simple中的ip的自己申请的服务器，大家可以随便测试，但不保证有效期，建议自行搭建本地服务测试。
<br><br>
### simple-demo
调用示例：<br>
```java
OrgManager orgManager = new OrgManager();
orgManager
    .init("Org1")
    .setUser("Admin", getCryptoConfigPath("aberic"), getChannleArtifactsPath("aberic"))
    .setCA("ca", "http://118.89.243.236:7054")
    .setPeers("Org1MSP", "org1.example.com")
    .addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://118.89.243.236:7051", "grpc://118.89.243.236:7053", true)
    .setOrderers("example.com")
    .addOrderer("orderer.example.com", "grpc://118.89.243.236:7050")
    .setChannel("mychannel")
    .setChainCode("test2cc", "/code", "chaincode/chaincode_example02", "1.2", 90000, 120)
    .openTLS(true)
    .openCATLS(false)
    .setBlockListener(map -> {
            logger.debug(map.get("code"));
            logger.debug(map.get("data"));
        })
    .add();
    FabricManager fabricManager = orgManager.use("Org1");
    fabricManager.install();
    fabricManager.instantiate(argArray);
    fabricManager.upgrade(argArray);
    fabricManager…
```
<br><br>
### api
请求示例：<br>
请求接口：http://localhost:8080/simple/trace<br>
请求JSON：
```JSON
{
   "fcn": "queryBlockByNumber",
   "traceId": "21"
}
```
返回JSON：
```JSON
{
    "data": {
        "dataHash": "5660b39a622b07c11913750f09d6a9926f2440bc974873c99c80863acc0de93b",
        "blockNumber": 21,
        "calculatedBlockHash": "6cf6e766c1a91a6648067d311a124477eca75431fe925308a6773adc624bec6f",
        "envelopeCount": 1,
        "envelopes": [
            {
                "transactionEnvelopeInfo": {
                    "transactionActionInfoArray": [
                        {
                            "chaincodeInputArgsCount": 4,
                            "endorserInfoArray": [
                                {
                                    "mspId": "Org1MSP",
                                    "signature": "30440220713f1b2ad3cdee4b299a2f341d5325b2d8deb32a8f9ae4a7371850fbe72bdcf102206e803cc428b72fb9c8b4fa03bf99210a82efb6d06c217d1bc938d3eaaa7df3ee",
                                    "id": "-----BEGIN CERTIFICATE-----\nMIICGDCCAb+gAwIBAgIQEsKBEKwwwpp/z/Xn5gLApDAKBggqhkjOPQQDAjBzMQsw\nCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZy\nYW5jaXNjbzEZMBcGA1UEChMQb3JnMS5leGFtcGxlLmNvbTEcMBoGA1UEAxMTY2Eu\nb3JnMS5leGFtcGxlLmNvbTAeFw0xODA0MTEwMzU0MTZaFw0yODA0MDgwMzU0MTZa\nMFsxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1T\nYW4gRnJhbmNpc2NvMR8wHQYDVQQDExZwZWVyMC5vcmcxLmV4YW1wbGUuY29tMFkw\nEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAECpresx/Xv/LbmL7P0hPrngE603IoYKOj\n2E22NnW7tVdr0XtQJSXMOaBLHoPU7P0mBv8xcqBirmjBp463axjYAKNNMEswDgYD\nVR0PAQH/BAQDAgeAMAwGA1UdEwEB/wQCMAAwKwYDVR0jBCQwIoAgq33KXl9rHMJM\nICN2TFs00feNhhTSoR50F40tVQm9O+gwCgYIKoZIzj0EAwIDRwAwRAIgLpvW805T\n5IpTQlYSLTs+T7eFPm37rzVu2llABopwOWECIE4clYkh7ILEmxytYE2Vvtun4P4D\naGUb2JbPjKHxUnnL\n-----END CERTIFICATE-----\n"
                                }
                            ],
                            "payload": "",
                            "argArray": [
                                "invoke",
                                "A",
                                "B",
                                "1"
                            ],
                            "endorsementsCount": 1,
                            "rwsetInfo": {
                                "nsRwsetInfoArray": [
                                    {
                                        "writeSet": [],
                                        "readSet": [
                                            {
                                                "readSetIndex": 0,
                                                "readVersionTxNum": 0,
                                                "readVersionBlockNum": 20,
                                                "namespace": "lscc",
                                                "version": "[20 : 0]",
                                                "key": "test2cc"
                                            }
                                        ]
                                    },
                                    {
                                        "writeSet": [
                                            {
                                                "writeSetIndex": 0,
                                                "namespace": "test2cc",
                                                "value": "9",
                                                "key": "A"
                                            },
                                            {
                                                "writeSetIndex": 1,
                                                "namespace": "test2cc",
                                                "value": "11",
                                                "key": "B"
                                            }
                                        ],
                                        "readSet": [
                                            {
                                                "readSetIndex": 0,
                                                "readVersionTxNum": 0,
                                                "readVersionBlockNum": 20,
                                                "namespace": "test2cc",
                                                "version": "[20 : 0]",
                                                "key": "A"
                                            },
                                            {
                                                "readSetIndex": 1,
                                                "readVersionTxNum": 0,
                                                "readVersionBlockNum": 20,
                                                "namespace": "test2cc",
                                                "version": "[20 : 0]",
                                                "key": "B"
                                            }
                                        ]
                                    }
                                ],
                                "nsRWsetCount": 2
                            },
                            "responseStatus": 200,
                            "responseMessageString": "",
                            "status": 200
                        }
                    ],
                    "txCount": 1,
                    "isValid": true,
                    "validationCode": 0
                },
                "createId": "-----BEGIN CERTIFICATE-----\nMIICGDCCAb+gAwIBAgIQVdYyRCqqRvPari/r4GRUmTAKBggqhkjOPQQDAjBzMQsw\nCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZy\nYW5jaXNjbzEZMBcGA1UEChMQb3JnMS5leGFtcGxlLmNvbTEcMBoGA1UEAxMTY2Eu\nb3JnMS5leGFtcGxlLmNvbTAeFw0xODA0MTEwMzU0MTZaFw0yODA0MDgwMzU0MTZa\nMFsxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1T\nYW4gRnJhbmNpc2NvMR8wHQYDVQQDDBZBZG1pbkBvcmcxLmV4YW1wbGUuY29tMFkw\nEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEHaegt0vA9oJvcCSHdZltBz5pRCsXr4x3\nly8vZzt/79gqjCqw4ChL9m93KIng7cHTChuoXQtmsjGmGj+icDIvR6NNMEswDgYD\nVR0PAQH/BAQDAgeAMAwGA1UdEwEB/wQCMAAwKwYDVR0jBCQwIoAgq33KXl9rHMJM\nICN2TFs00feNhhTSoR50F40tVQm9O+gwCgYIKoZIzj0EAwIDRwAwRAIgKMghfQYW\n68PWLs5N/t/RFJ7bfW8ThQHcsm7F26uPoDQCIApRyVSJD41G56b9+ekgSd5uaYzw\niXqcbALvpywjQZgO\n-----END CERTIFICATE-----\n",
                "isValid": true,
                "validationCode": 0,
                "type": "TRANSACTION_ENVELOPE",
                "nonce": "670ba2cc3ee2f2294ee0c822475c671011d9e53bf4860f72",
                "channelId": "mychannel",
                "transactionID": "08b5db91c7723cb61651a4af1034633a2833031a1cdb4415df0d8f6727020a4f",
                "createMSPID": "Org1MSP",
                "timestamp": "2018/05/18 18:14:52"
            }
        ],
        "previousHashID": "8f63d99744752a89a49fcee560a43c271b7f12e37dfaa3489da028b610943595"
    },
    "status": 200
}
```


<br><br>
欢迎与我多多交流：<br>
我的博客：[HyperLedger/Aberic](http://www.cnblogs.com/aberic/)<br>
HyperLedger/Fabric**微信交流群**，扫微信订阅号加入：<br>
![HLFStudy](https://images2017.cnblogs.com/blog/1240530/201802/1240530-20180201103733812-1730907548.jpg "HLFStudy 微信订阅号")
