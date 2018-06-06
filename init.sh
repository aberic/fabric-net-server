#!/usr/bin/env bash
echo
echo " ____    _____      _      ____    _____  "
echo "/ ___|  |_   _|    / \    |  _ \  |_   _| "
echo "\___ \    | |     / _ \   | |_) |   | |   "
echo " ___) |   | |    / ___ \  |  _ <    | |   "
echo "|____/    |_|   /_/   \_\ |_| \_\   |_|   "
echo

orgAddUrl=http://localhost:8080/simple/org/add
orderAddUrl=http://localhost:8080/simple/orderer/add
peerAddUrl=http://localhost:8080/simple/peer/add
testNetUrl=http://localhost:8080/simple/trace

orgJson=`jo -p id=1 orgName=ORG_NAME tls@ORG_TLS caTls@ORG_CA_TLS username=ORG_USERNAME cryptoConfigDir=ORG_CRYPTO_CONFIG_DIR channelArtifactsDir=ORG_CHANNEL_ARTIFACTS_DIR caName=ORG_CA_NAME caLocation=ORG_CA_LOCATION orgMSPID=ORG_MSP_ID orgDomainName=ORG_DOMAIN_NAME ordererDomainName=ORG_ORDERER_DOMAIN_NAME channelName=ORG_CHANNEL_NAME chaincodeName=ORG_CHAINCODE_NAME chaincodeSource=ORG_CHAINCODE_SOURCE chaincodePath=ORG_CHAINCODE_PATH chaincodeVersion=ORG_CHAINCODE_VERSION proposalWaitTime=ORG_PROPOSAL_WAIT_TIME invokeWaitTime=ORG_INVOKE_WAIT_TIME`
orderJson=`jo -p id=1 orgId=1 peerName=ORDERER_PEER_NAME peerEventHubName=ORDERER_PEER_EVENT_HUB_NAME peerLocation=ORDERER_PEER_LOCATION peerEventHubLocation=ORDERER_PEER_EVENT_HUB_LOCATION isEventListener@ORDERER_IS_EVENT_LISTENER`
peerJson=`jo -p id=1 orgId=1 name=PEER_NAME location=PEER_LOCATION`
testJson=`jo -p fcn=queryBlockByNumber traceId="0"`

rm -f tpid
nohup java -jar /home/jar/myapp.jar > /dev/null 2>&1 &
echo $! > tpid
echo Start Success!

## 检查添加组织信息是否成功
addOrg () {
	result=`curl -H "Content-type: application/json" -X POST -d ${orgJson} ${orgAddUrl}`
	if [ ${result} -eq 0 ]; then
	    echo "===================== org add success ===================== "
	    echo
	else
	    echo "===================== org add failed ===================== "
	    echo
	    exit 1
	fi
}

## 检查添加排序服务信息是否成功
addOrder () {
	result=`curl -H "Content-type: application/json" -X POST -d ${orderJson} ${orderAddUrl}`
	if [ ${result} -eq 0 ]; then
	    echo "===================== order add success ===================== "
	    echo
	else
	    echo "===================== order add failed ===================== "
	    echo
	    exit 1
	fi
}

## 检查添加节点服务信息是否成功
addPeer () {
	result=`curl -H "Content-type: application/json" -X POST -d ${peerJson} ${peerAddUrl}`
	if [ ${result} -eq 0 ]; then
	    echo "===================== peer add success ===================== "
	    echo
	else
	    echo "===================== peer add failed ===================== "
	    echo
	    exit 1
	fi
}

testNet () {
	result=`curl -H "Content-type: application/json" -X POST -d ${testJson} ${testNetUrl}`
	echo ${result}
}

echo "add Org..."
addOrg

echo "add Order..."
addOrder

echo "add Peer..."
addPeer

echo
echo "===================== All GOOD, start java sdk completed ===================== "
echo

echo
echo " _____   _   _   ____    "
echo "| ____| | \ | | |  _ \   "
echo "|  _|   |  \| | | | | |  "
echo "| |___  | |\  | | |_| |  "
echo "|_____| |_| \_| |____/   "
echo