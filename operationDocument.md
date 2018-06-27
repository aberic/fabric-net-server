**注意：** 本文档是以fabric官方1.0版本中e2e_cli单机版作为启动环境，没有做其他个人更改。同时为了调试和测试方便，关闭了tls。
<br/>
# Fabric Net Server 详细说明文档 [![fabric-sdk image](https://img.shields.io/badge/made%20by-aberic-orange.svg)](http://www.cnblogs.com/aberic/)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/aberic/fabric-sdk-container/blob/master/LICENSE)
[![fabric-sdk image](https://img.shields.io/docker/build/jrottenberg/ffmpeg.svg)](https://hub.docker.com/r/aberic/)
[![Join the chat at https://gitter.im/fabric-net-server/Lobby](https://badges.gitter.im/fabric-net-server/Lobby.svg)](https://gitter.im/fabric-net-server/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
<br><br>
  由于aberic大大一个人精力和人力有限，作为追随者，根据自身的搭建调试经验，编写此文档，贡献一些力量。
<br><br>

### 功能说明
可能有部分刚刚接触的人对这个本sdk中的联盟，组织等概念有一些混乱，这里再次根据作者的思路进行一个详细的说明。
#### 联盟(league)
fabric是联盟链的杰出代表，所谓联盟，就是由多个相同的组织或者不同的组织所组成的一个相互信任的盟友关系，一个fabric网络便是一个联盟链。
#### 组织和服务节点(org&orderer)
在一个联盟链中存在多个组织以及至少存在一个orderer(排序服务节点)，对于初次接触fabric或者不是很熟悉的人，建议使用[fabric1.0(fabric/examples/e2e_cli/
)](https://github.com/hyperledger/fabric/tree/release-1.0)版本进行单机版的项目搭建，也就是1orderer+4pper+1cli的项目结构，这样会避免很多坑，也能尽快的体会fabric的整体构成，减少学习成本。
#### 节点(peer)
对于每个组织成员org来说，由于需要避免服务器宕机，每个组织通常会启动多个peer节点保证服务的正常进行，在正常情况下，在本案例[fabric-net-server](https://github.com/aberic/fabric-net-server/tree/1.0-RC1)中，正确添加一个节点即可完成链码交互。
#### 通道(channel)
一个peer可以加入一个或多个channel（通道），通道之间是无法相互感知的，做到了数据隔离
#### 链码(chaincode)
一个channel可以安装一个或多个chaincode（智能合约）

## 使用
1、确定Linux内核在`3.10`及以上。
<br>
2、在待部署SDK服务器上安装`Docker`及`docker compose`环境。
<br>
3、执行`docker pull aberic/fabric-service:1.0-RC1`及`docker pull aberic/fabric-edge:1.0-RC1`下载两个镜像。
<br>
4、编辑`docker-fabric-net-server.yaml`。
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
    image: aberic/fabric-service:1.0-RC1
    command: bash /home/init-service.sh
    ports:
      - 8081:8081

  edge:
    container_name: edge
    image: aberic/fabric-edge:1.0-RC1
    environment:
      # service地址配置
      - SERVICE_IP=10.0.38.95
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
<br/>
4、SERVICE_IP 配置的时候避免使用127.0.0.1,博主说可能会导致项目无法启动。
<br><br>
*********************************************
## 添加流程截图说明
#### 后台首页界面预览
![首页视图](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E9%A6%96%E9%A1%B5%E9%A2%84%E8%A7%88.png "Fabric 网络")

#### 联盟列表界面预览
![联盟列表视图](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E8%81%94%E7%9B%9F%E5%88%97%E8%A1%A82.png "Fabric 联盟列表")
##### 添加联盟
![添加联盟](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E6%B7%BB%E5%8A%A0%E8%81%94%E7%9B%9F.png "添加联盟")
##### 添加联盟说明
联盟名称没有限制，可以根据实际场景和需求设定，可以设定多个联盟
<br><br>

#### 组织列表界面预览
![组织列表视图](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E8%81%94%E7%9B%9F%E5%88%97%E8%A1%A82.png "Fabric 组织列表视图")
##### 添加组织
![添加组织](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E6%B7%BB%E5%8A%A0%E7%BB%84%E7%BB%87%E9%A2%84%E8%A7%88.png "添加组织")
##### 选取配置文件
![选取配置文件](https://raw.githubusercontent.com/Larryleo5/LarryProject/cea70c81d42a965887f5310a3fafc6b59c7b48a5/%E9%80%89%E6%8B%A9%E5%8E%8B%E7%BC%A9%E5%8C%85.png "选取配置文件")
##### 选择联盟
![选择联盟](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E6%B7%BB%E5%8A%A0%E7%BB%84%E7%BB%87%E6%97%B6%E9%80%89%E6%8B%A9%E8%81%94%E7%9B%9F.png "选择联盟")
###### 添加组织说明
在存在多个联盟的情况下，可以选择对应的组织，从而形成不同组织多联盟链
<br><br>

#### 排序节点列表界面预览
![排序节点列表视图](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E6%8E%92%E5%BA%8F%E6%9C%8D%E5%8A%A1%E5%88%97%E8%A1%A8.png "Fabric 排序节点列表视图")
##### 添加排序节点
![添加排序节点](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E6%B7%BB%E5%8A%A0%E6%8E%92%E5%BA%8F%E9%A2%84%E8%A7%88.png "添加排序节点")
##### 选择组织
![选择组织](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E6%B7%BB%E5%8A%A0%E6%8E%92%E5%BA%8F%E9%80%89%E6%8B%A9%E7%BB%84%E7%BB%87.png "选择组织")
###### 添加排序节点说明
在一个联盟链中，必须有一个排序服务节点，负责对交易进行打包和广播
<br><br>

#### 节点列表界面预览
![节点列表视图](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E8%8A%82%E7%82%B9%E5%88%97%E8%A1%A8.png "Fabric 节点列表视图")
##### 添加节点
![添加节点](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E8%8A%82%E7%82%B9%E6%B7%BB%E5%8A%A0%E9%A2%84%E8%A7%88.png "添加节点")
###### 添加节点说明
在同一组织下，可以添加多个节点，防止节点宕机造成的服务不可用。本教程中，添加一个节点即可完成链码交互。
<br><br>

#### 通道列表界面预览
![通道列表视图](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E9%80%9A%E9%81%93%E5%88%97%E8%A1%A8.png "Fabric 通道列表视图")
##### 添加通道
![添加通道](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E9%80%9A%E9%81%93%E6%B7%BB%E5%8A%A0%E9%A2%84%E8%A7%88.png "添加联盟")
###### 添加通道说明
通道是由peer节点创建的，并且一个节点可以创建或者加入多个通道，所以在通道创建时需要选中peer节点
<br><br>

#### 链码列表界面预览
![链码列表视图](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E5%90%88%E7%BA%A6%E5%88%97%E8%A1%A8%E9%A2%84%E8%A7%88.png "Fabric 链码列表视图")
##### 添加链码
![添加链码](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E5%90%88%E7%BA%A6%E6%B7%BB%E5%8A%A0%E9%A2%84%E8%A7%88.png "添加链码")
###### 添加链码说明
以官方e2e_cli项目为例，启动脚本中 安装链码的指令为例
<br/>
`peer chaincode install -n mycc -v 1.0 -p github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02 >&log.txt`
<br/>
-n mycc (指定链码名称)
<br/>
-v 1.0 (指定链码版本)
<br/>
-p github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02 (指定链码路径)
<br/>
智能合约添加时，需要对以上信息进行匹配(如图中所示)
<br><br>
*********************************************

#### 所有资源添加完成
![资源添加完成](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E6%B7%BB%E5%8A%A0%E5%AE%8C%E6%88%90%E9%A6%96%E9%A1%B5.png "资源添加完成")

*********************************************
#### 智能合约交互测试
###### 合约交互说明
![合约交互说明](https://raw.githubusercontent.com/Larryleo5/LarryProject/cea70c81d42a965887f5310a3fafc6b59c7b48a5/%E9%80%89%E5%8C%BA_063.png "Fabric 合约交互说明")
** 注意 ** 务必仔细看此交互说明，否则参数错误，会一直报code:9999
<br/>
和之前beta3中的交互方式略有不同<br/>
`执行合约，如：{"id": "1", "strArray": ["invoke", "a", "b", "2"]}`
<br/>
目前版本中，由于可视化提供了列表展示，点击测试按钮时会自动针对当前选中的只能合约进行交互，故不用再进行chaincodeId的传递，直接输入参数进行交互即可
<br/> 
查询命令  `query,a`
<br/> 
如下图所示
![测试合约查询](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E5%90%88%E7%BA%A6%E6%9F%A5%E8%AF%A2.png "Fabric 测试合约查询")
##### 返回正确结果
![正确结果返回](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E6%9F%A5%E8%AF%A2%E7%BB%93%E6%9E%9C.png "正确结果返回")

###### 其他交互用例
| Args             | REST API             | Description                 |
| :--:             | :--                  | :--                         |
| `invoke,a,b,10`  | /state/invoke        | 执行智能合约                  |
| `query,a`        | /state/query         | 查询智能合约                  |
| `blockhash`      | /trace/hash          | 根据交易hash查询区块           |
| `blockNum`       | /trace/number        | 根据交易区块高度查询区块        |
| `txId`           | /trace/txid          | 根据交易ID查询区块             |
| ` `              | /trace/info/{id}     | 根据当前智能合约id查询当前链信息 |
<br/>

*********************************************
### 可能出现的异常情况
#### Read Time Out
![测试合约执行](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E5%90%88%E7%BA%A6%E6%9F%A5%E8%AF%A2.png "Fabric 联盟列表")
##### 返超时异常
![添加链码](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E8%B6%85%E6%97%B6%E5%BC%82%E5%B8%B8.png "添加联盟")
##### 查询容器日志
在宿主机执行命令查看容器日志 `docker logs edge -f`
如果不需要动态查看日志，去掉-f
![超时日志](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/thrift%E8%B6%85%E6%97%B6%E6%97%A5%E5%BF%97.png "添加联盟")
##### 合约能够正常执行
![合约成功执行](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E5%90%88%E7%BA%A6%E6%AD%A3%E5%B8%B8%E6%89%A7%E8%A1%8C.png "添加联盟")
当我们将所有资源添加完成时，block高度是5（*也可能是4,看e2e启动时是否进行过其他操作*），但是执行完
`invoke,a,b,10`后，虽然返回超时异常，确产生了额外的区块（此时区块高度为6），说明执行是成功了的。
###### 异常说明
和博主沟通过，应该是thrift框架的问题，需要再跟进查找。后续博主找到问题，再进行分析和更新
<br/>
目前不影响测试使用。

#### SystemInfo 异常
有时查看容器日志 `docker logs edge -f`
![系统状态读取异常](https://raw.githubusercontent.com/Larryleo5/LarryProject/a82205392eedcd0800cbce5072693b42927b481d/%E7%B3%BB%E7%BB%9F%E7%8A%B6%E6%80%81%E8%AF%BB%E5%8F%96%E5%BC%82%E5%B8%B8.png "Fabric 联盟列表")
###### 异常说明
此bug会导致首页的系统监测异常，容器无法正常读取系统信息，但是不影响使用，这部分内容会在下一版本去掉。

