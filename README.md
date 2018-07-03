**注意：** 这是一个依赖于已经部署好的HyperLedger Fabric项目，关于前者搭建部署的方案可以通过参考[从零开始](https://www.cnblogs.com/aberic/category/1148898.html)或购买[开发实战](https://item.jd.com/12381034.html?dist=jd)等途径学习。Fabric环境部署等目前都不在当前项目的支持范围内。
<br>
# Fabric Net Server [![fabric-sdk image](https://img.shields.io/badge/made%20by-aberic-orange.svg)](http://www.cnblogs.com/aberic/)
[![version](https://img.shields.io/badge/version-1.0RC3-green.svg)](https://github.com/aberic/fabric-net-server/tree/1.0-RC3)
[![apache 2](https://img.shields.io/hexpm/l/plug.svg?longCache=true)](https://github.com/aberic/fabric-sdk-container/blob/master/LICENSE)
[![codebeat badge](https://codebeat.co/badges/92aa24de-8a82-432b-83fb-ec7413615952)](https://codebeat.co/projects/github-com-aberic-fabric-net-server-master)
[![Join the chat at https://gitter.im/fabric-net-server/Lobby](https://badges.gitter.im/fabric-net-server/Lobby.svg)](https://gitter.im/fabric-net-server/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
<br><br>
本项目目前提供一套Fabric网络后台服务，包括对已经在Fabric网络中创建好的org、peer、channel、chaincode等进行网络新增，并基于已经新增成功的内容提供[REST API](https://github.com/aberic/fabric-net-server/blob/master/API_DEMO.md)服务，目前支持链码安装、实例化、升级、调用、查询以及溯源等功能，未来计划支持后台动态加入通道等基于[fabric-sdk-java](https://github.com/hyperledger/fabric-sdk-java)已有接口的实现。
<br><br>
本项目开发环境和框架大致有：IntelliJ IDEA、Maven、JDK8+、spring-boot、thymeleaf以及Docker等。具体实践可使用项目中提供的Docker镜像部署，也可以自定义源码二次开发。
<br><br>
由于个人精力有限，后面的工程和工作量会逐步变大变多，在保证个人工作顺利的情况下，会将大部分精力都投入到本项目中。
<br><br>
## 项目简览
后台界面综览
<br>
![项目简览](https://raw.githubusercontent.com/aberic/fabric-net-server/master/img/indexDemo.png "Fabric Net Server")
<br><br>

## 资源
#### [F.N.S Documentation Wiki](https://github.com/aberic/fabric-net-server/wiki)
#### [Fabric Net Server开发计划](https://github.com/aberic/fabric-net-server/wiki/Fabric-Net-Server%E5%BC%80%E5%8F%91%E8%AE%A1%E5%88%92)
#### [HyperLedger/Aberic Blog](http://www.cnblogs.com/aberic/)
#### [HyperLedger Fabric开发实战](https://item.jd.com/12381034.html?dist=jd)
#### [HLFStudy 微信订阅号](https://camo.githubusercontent.com/bbde569d4617068fe0188d51b1ef8e47561d62ea/68747470733a2f2f696d61676573323031372e636e626c6f67732e636f6d2f626c6f672f313234303533302f3230313830322f313234303533302d32303138303230313130333733333831322d313733303930373534382e6a7067)