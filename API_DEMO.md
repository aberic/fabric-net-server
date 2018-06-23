## API接口部分用例
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