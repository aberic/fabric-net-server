# HyperLedger/Fabric JAVA-SDK with 1.1
该项目介绍如何使用fabric-sdk-java框架，基于fabric-sdk-java v1.1正式版开发，可向下兼容1.0版本。<br>
该项目没有对原JAVA-SDK做修改，主要是结合HyperLedger Fabric与fabric-sdk-java中的交互方式做了个人感觉更为清晰的描述，希望能够帮助更多的人尽快熟悉fabric-sdk-java的操作流程和方式。
## sdk-advance
sdk-advance是基于fabric-sdk-java v1.1的服务，其主要目的是为了更简单的使用fabric-sdk-java，对原有的调用方法做了进一步封装，主要提供了各种中转对象，如智能合约、通道、排序服务、节点、用户等等，最终将所有的中转对象交由一个中转组织来负责配置，其对外提供服务的方式则交给FabricManager来掌管。<br>
该项目仅作为学习分享的形式提交维护，关于生产部署方面，sdk-advance与app的交互可自行选择采用thrift或protobuf等数据传输协议实现，这里并没有提供该方案的具体实现，需要自己动手解决。<br>
### sdk-advance-intermediate

