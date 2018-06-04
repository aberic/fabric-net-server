# HyperLedger/Fabric JAVA-SDK with 1.1
该项目介绍如何使用fabric-sdk-java框架，基于fabric-sdk-java v1.1正式版开发，可向下兼容1.0版本。<br><br>
该项目没有对原JAVA-SDK做修改，主要是结合HyperLedger Fabric与fabric-sdk-java中的交互方式做了个人感觉更为清晰的描述，希望能够帮助更多的人尽快熟悉fabric-sdk-java的操作流程和方式。<br><br>
## sdk-advance
sdk-advance是基于fabric-sdk-java v1.1的服务，其主要目的是为了更简单的使用fabric-sdk-java，对原有的调用方法做了进一步封装，主要提供了各种中转对象，如智能合约、通道、排序服务、节点、用户等等，最终将所有的中转对象交由一个中转组织来负责配置，其对外提供服务的方式则交给FabricManager来掌管。<br><br>
该项目仅作为学习分享的形式提交维护，关于生产部署方面，sdk-advance与app的交互可自行选择采用thrift或protobuf等数据传输协议实现，这里并没有提供该方案的具体实现，需要自己动手解决。<br><br>
### sdk-advance-intermediate
intermediate对象是该项目的主要封装对象，间接屏蔽了真实应用层与fabric-sdk-java之间的直接交互。
* IntermediateOrderer<br>
对于Orderer，我们需要知道所选组织名称，同时还需要知道Orderer的服务器域名及真实访问地址。这里的服务器域名是指由crypto-config.yaml中进行指定。<br>
该对象主要提供了对Orderer的服务器域名及真实访问地址的封装。<br>
* IntermediatePeer<br>
Peer是部署于本地节点的配置对象，主要包括当前指定的组织节点域名、当前指定的组织节点事件域名、当前指定的组织节点访问地址、当前指定的组织节点事件监听访问地址以及当前peer是否增加Event事件处理。这里的主要配置信息也由crypto-config.yaml中进行指定<br>
* IntermediateChannel<br>
每一个Peer所加入的Channel都有独立的通道信息，包括名称或通道tx文件等。IntermediateChannel中定义了通道名称，除此之外，还针对上面两个对象进行了调用，并最终生成了Channel对象。<br>
IntermediateChannel中已经开始提供对外服务了，主要依赖于Channel对象所提供的，包括最基本的Peer加入频道的方法。<br>
另外，IntermediateChannel还提供了一系列基本的溯源方法，包括查询当前频道的链信息，包括链长度、当前最新区块hash以及当前最新区块的上一区块hash、在指定频道内根据transactionID查询区块、在指定频道内根据hash查询区块以及在指定频道内根据区块高度查询区块等方法。<br>
* IntermediateChaincodeID<br>



