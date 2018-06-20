## API接口部分用例
### 新增Fabric联盟
POST：http://ip:port/league/add
```json
{
    "name": "example"
}
```
### 更新Fabric联盟
POST：http://ip:port/league/update
```json
{
    "id": 1,
    "name": "Admin"
}
```
### 新增组织
POST：http://ip:port/org/add
```json
{
    "cryptoConfigDir": "/home/config/crypto-config",
    "ordererDomainName": "example.com",
    "domainName": "org1.example.com",
    "mspId": "Org1MSP",
    "name": "Org1",
    "username": "Admin",
    "tls": true,
    "leagueId": 1
}
```
### 更新组织
POST：http://ip:port/org/update
```json
{
    "id": 1,
    "cryptoConfigDir": "/home/config/crypto-config",
    "ordererDomainName": "example.com",
    "domainName": "org1.example.com",
    "mspId": "Org1MSP",
    "name": "Org1",
    "username": "Admin",
    "tls": true
}
```
### 新增排序服务
POST：http://ip:port/orderer/add
```json
{
    "orgId": "1",
    "name": "orderer.example.com",
    "location": "grpc://118.89.243.236:7050"
}
```
### 更新排序服务
POST：http://ip:port/orderer/update
```json
{
    "id": "1",
    "name": "orderer.example.com",
    "location": "grpc://118.89.243.236:7050"
}
```
### 新增节点
POST：http://ip:port/peer/add
```json
{
    "orgId": "1",
    "name": "peer0.org1.example.com",
    "eventHubName": "peer0.org1.example.com",
    "location": "grpc://118.89.243.236:7051",
    "eventHubLocation": "grpc://118.89.243.236:7053",
    "isEventListener": true
}
```
### 更新节点
POST：http://ip:port/peer/update
```json
{
    "id": 1,
    "orgHash": "9741b01b4f14d11431eb31a795f5c3a4",
    "peerName": "peer0.org1.example.com",
    "peerEventHubName": "peer0.org1.example.com",
    "peerLocation": "grpc://118.89.243.236:7051",
    "peerEventHubLocation": "grpc://118.89.243.236:7053",
    "isEventListener": true
}
```
### 新增通道
POST：http://ip:port/channel/add
```json
{
    "name": "mychannel",
    "peerId": 1
}
```
### 新增智能合约
POST：http://ip:port/chaincode/add
```json
{
    "name": "testcc",
    "path": "github.com/hyperledger/fabric/aberic/chaincode/go/chaincode_example01",
    "version": "1.0",
    "invokeWaitTime": 120,
    "proposalWaitTime": 90000,
    "channelId": 1
}
```
### 执行智能合约
<br>该智能合约以[chaincode_example02.go](https://github.com/aberic/fabric-sdk-container/blob/master/chaincode_example02/chaincode_example02.go)为例。
POST：http://ip:port/state/invoke
```json
{
   "id": "1",
   "args": ["invoke", "a", "b", "1"]
}

```
### 查询智能合约
<br>该智能合约以[chaincode_example02.go](https://github.com/aberic/fabric-sdk-container/blob/master/chaincode_example02/chaincode_example02.go)为例。
POST：http://ip:port/state/query
```json
{
   "id": "1",
   "args": ["query", "a"]
}

```
### 根据交易ID查询区块
POST：http://ip:port/trace/txid
```json
{
   "chainCodeId": "1",
   "trace": "4f1711315d00e84bd76f60bf40af1fa56ff7c5ba398bc264a7d2ca3f882dfe0b"
}
```
### 根据交易hash查询区块
POST：http://ip:port/trace/hash
```json
{
   "chainCodeId": "1",
   "trace": "6ca9ec68a119bdf54b7a400adf09aa178e17f47d397b09eeef1917cd25208dce"
}
```
### 根据交易区块高度查询区块
POST：http://ip:port/trace/number
```json
{
   "chainCodeId": "1",
   "trace": "3"
}
```
### 根据当前智能合约id查询当前链信息
GET：http://ip:port/trace/info/1