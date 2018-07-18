/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeType;
import org.hyperledger.fabric.sdk.BlockInfo.TransactionEnvelopeInfo;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 描述：中继频道对象
 *
 * @author : Aberic 【2018/5/17 18:25】
 */
class IntermediateChannel {

    private Logger log = LogManager.getLogger(IntermediateChannel.class);

    /** 当前将要访问的智能合约所属频道名称 */
    private String channelName; // myChannel
//    /** 事务等待时间以秒为单位 */
//    private int transactionWaitTime = 100000;
//    /** 部署等待时间以秒为单位 */
//    private int deployWatiTime = 120000;
    /** 中继组织节点 */
    private IntermediateOrg org;
    private Channel channel;

    void init(IntermediateOrg org) throws TransactionException, InvalidArgumentException {
        this.org = org;
        setChannel(org.getClient());
    }

    private void setChannel(HFClient client) throws InvalidArgumentException, TransactionException {
//        client.setUserContext(org.getUser(org.getUsername()));
        client.setUserContext(org.getUser());
        channel = client.newChannel(channelName);
        log.debug("Get Chain " + channelName);

        int sizeOrderers = org.getOrderers().size();
        for (int i = 0; i < sizeOrderers; i++) {
            Properties ordererProperties = new Properties();
            if (org.openTLS()) {
                File ordererCert = new File(org.getOrderers().get(i).getServerCrtPath());
                if (!ordererCert.exists()) {
                    throw new RuntimeException(
                            String.format("Missing cert file for: %s. Could not find at location: %s", org.getOrderers().get(i).getOrdererName(), ordererCert.getAbsolutePath()));
                }
                ordererProperties.setProperty("pemFile", ordererCert.getAbsolutePath());
            }
            ordererProperties.setProperty("hostnameOverride", org.getOrderers().get(i).getOrdererName());
            ordererProperties.setProperty("sslProvider", "openSSL");
            ordererProperties.setProperty("negotiationType", "TLS");
            ordererProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
            // 设置keepAlive以避免在不活跃的http2连接上超时的例子。在5分钟内，需要对服务器端进行更改，以接受更快的ping速率。
            ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[]{5L, TimeUnit.MINUTES});
            ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[]{8L, TimeUnit.SECONDS});
            ordererProperties.setProperty("ordererWaitTimeMilliSecs", "300000");
            channel.addOrderer(
                    client.newOrderer(org.getOrderers().get(i).getOrdererName(), org.getOrderers().get(i).getOrdererLocation(), ordererProperties));
        }

        int sizePeer = org.getPeers().size();
        for (int i = 0; i < sizePeer; i++) {
            Properties peerProperties = new Properties();
            if (org.openTLS()) {
                File peerCert = new File(org.getPeers().get(i).getServerCrtPath());
                if (!peerCert.exists()) {
                    throw new RuntimeException(
                            String.format("Missing cert file for: %s. Could not find at location: %s", org.getPeers().get(i).getPeerName(), peerCert.getAbsolutePath()));
                }
                peerProperties.setProperty("pemFile", peerCert.getAbsolutePath());
            }
            // ret.setProperty("trustServerCertificate", "true"); //testing
            // environment only NOT FOR PRODUCTION!
            peerProperties.setProperty("hostnameOverride", org.getPeers().get(i).getPeerName());
            peerProperties.setProperty("sslProvider", "openSSL");
            peerProperties.setProperty("negotiationType", "TLS");
            // 在grpc的NettyChannelBuilder上设置特定选项
            peerProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
            // 如果未加入频道，该方法执行加入。如果已加入频道，则执行下一行方面新增Peer
            // channel.joinPeer(client.newPeer(peers.get().get(i).getPeerName(), fabricOrg.getPeerLocation(peers.get().get(i).getPeerName()), peerProperties));
            channel.addPeer(client.newPeer(org.getPeers().get(i).getPeerName(), org.getPeers().get(i).getPeerLocation(), peerProperties));
            if (org.getPeers().get(i).isAddEventHub()) {
                channel.addEventHub(client.newEventHub(org.getPeers().get(i).getPeerEventHubName(), org.getPeers().get(i).getPeerEventHubLocation(), peerProperties));
            }
        }

        log.debug("channel.isInitialized() = " + channel.isInitialized());
        if (!channel.isInitialized()) {
            channel.initialize();
        }
        if (null != org.getBlockListener()) {
            // channel.registerBlockListener(org.getBlockListener());
            channel.registerBlockListener(blockEvent -> {
                try {
                    org.getBlockListener().received(execBlockInfo(blockEvent));
                } catch (Exception e) {
                    e.printStackTrace();
                    org.getBlockListener().received(getFailFromString(e.getMessage()));
                }
            });
        }
    }

    /** 获取Fabric Channel */
    Channel get() {
        return channel;
    }

    void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * Peer加入频道
     *
     * @param peer 中继节点信息
     */
    JSONObject joinPeer(IntermediatePeer peer) throws InvalidArgumentException, ProposalException {
        Properties peerProperties = new Properties();
        if (org.openTLS()) {
            File peerCert = new File(org.getPeers().get(0).getServerCrtPath());
            if (!peerCert.exists()) {
                throw new RuntimeException(
                        String.format("Missing cert file for: %s. Could not find at location: %s", peer.getPeerName(), peerCert.getAbsolutePath()));
            }
            peerProperties.setProperty("pemFile", peerCert.getAbsolutePath());
        }
        // ret.setProperty("trustServerCertificate", "true"); //testing
        // environment only NOT FOR PRODUCTION!
        peerProperties.setProperty("hostnameOverride", peer.getPeerName());
        peerProperties.setProperty("sslProvider", "openSSL");
        peerProperties.setProperty("negotiationType", "TLS");
        // 在grpc的NettyChannelBuilder上设置特定选项
        peerProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
        // 如果未加入频道，该方法执行加入。如果已加入频道，则执行下一行方面新增Peer
        // channel.joinPeer(client.newPeer(peers.get().get(i).getPeerName(), fabricOrg.getPeerLocation(peers.get().get(i).getPeerName()), peerProperties));
        Peer fabricPeer = org.getClient().newPeer(peer.getPeerName(), peer.getPeerLocation(), peerProperties);
        for (Peer peerNow : channel.getPeers()) {
            if (peerNow.getUrl().equals(fabricPeer.getUrl())) {
                return getFailFromString("peer has already in channel");
            }
        }
        channel.joinPeer(fabricPeer);
        if (peer.isAddEventHub()) {
            channel.addEventHub(org.getClient().newEventHub(peer.getPeerEventHubName(), peer.getPeerEventHubLocation(), peerProperties));
        }
        return getSuccessFromString();
    }

    private JSONObject getSuccess(JSON json) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", BlockListener.SUCCESS);
        jsonObject.put("data", json);
        return jsonObject;
    }

    private JSONObject getSuccessFromString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", BlockListener.SUCCESS);
        jsonObject.put("data", JSON.parse("peer join channel success"));
        return jsonObject;
    }

    private JSONObject getFailFromString(String data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", BlockListener.ERROR);
        jsonObject.put("data", data);
        return jsonObject;
    }

    /** 查询当前频道的链信息，包括链长度、当前最新区块hash以及当前最新区块的上一区块hash */
    JSONObject getBlockchainInfo() throws InvalidArgumentException, ProposalException {
        JSONObject blockchainInfo = new JSONObject();
        blockchainInfo.put("height", channel.queryBlockchainInfo().getHeight());
        blockchainInfo.put("currentBlockHash", Hex.encodeHexString(channel.queryBlockchainInfo().getCurrentBlockHash()));
        blockchainInfo.put("previousBlockHash", Hex.encodeHexString(channel.queryBlockchainInfo().getPreviousBlockHash()));
        return getSuccess(blockchainInfo);
    }

    /**
     * 在指定频道内根据transactionID查询区块
     *
     * @param txID transactionID
     */
    JSONObject queryBlockByTransactionID(String txID) throws InvalidArgumentException, ProposalException, IOException {
        return execBlockInfo(channel.queryBlockByTransactionID(txID));
    }

    /**
     * 在指定频道内根据hash查询区块
     *
     * @param blockHash hash
     */
    JSONObject queryBlockByHash(byte[] blockHash) throws InvalidArgumentException, ProposalException, IOException {
        return execBlockInfo(channel.queryBlockByHash(blockHash));
    }

    /**
     * 在指定频道内根据区块高度查询区块
     *
     * @param blockNumber 区块高度
     */
    JSONObject queryBlockByNumber(long blockNumber) throws InvalidArgumentException, ProposalException, IOException {
        return execBlockInfo(channel.queryBlockByNumber(blockNumber));
    }

    /**
     * 解析区块信息对象
     *
     * @param blockInfo 区块信息对象
     */
    private JSONObject execBlockInfo(BlockInfo blockInfo) throws IOException, InvalidArgumentException {
        final long blockNumber = blockInfo.getBlockNumber();
        JSONObject blockJson = new JSONObject();
        blockJson.put("blockNumber", blockNumber);
        blockJson.put("dataHash", Hex.encodeHexString(blockInfo.getDataHash()));
        blockJson.put("previousHashID", Hex.encodeHexString(blockInfo.getPreviousHash()));
        blockJson.put("calculatedBlockHash", Hex.encodeHexString(SDKUtils.calculateBlockHash(org.getClient(), blockNumber, blockInfo.getPreviousHash(), blockInfo.getDataHash())));
        blockJson.put("envelopeCount", blockInfo.getEnvelopeCount());

        log.debug("blockNumber = " + blockNumber);
        log.debug("data hash: " + Hex.encodeHexString(blockInfo.getDataHash()));
        log.debug("previous hash id: " + Hex.encodeHexString(blockInfo.getPreviousHash()));
        log.debug("calculated block hash is " + Hex.encodeHexString(SDKUtils.calculateBlockHash(org.getClient(), blockNumber, blockInfo.getPreviousHash(), blockInfo.getDataHash())));
        log.debug("block number " + blockNumber + " has " + blockInfo.getEnvelopeCount() + " envelope count:");

        blockJson.put("envelopes", getEnvelopeJsonArray(blockInfo, blockNumber));
        return getSuccess(blockJson);
    }

    /** 解析区块包 */
    private JSONArray getEnvelopeJsonArray(BlockInfo blockInfo, long blockNumber) throws UnsupportedEncodingException, InvalidProtocolBufferException {
        JSONArray envelopeJsonArray = new JSONArray();
        for (EnvelopeInfo info : blockInfo.getEnvelopeInfos()) {
            JSONObject envelopeJson = new JSONObject();
            envelopeJson.put("channelId", info.getChannelId());
            envelopeJson.put("transactionID", info.getTransactionID());
            envelopeJson.put("validationCode", info.getValidationCode());
            envelopeJson.put("timestamp", Utils.parseDateFormat(new Date(info.getTimestamp().getTime())));
            envelopeJson.put("type", info.getType());
            envelopeJson.put("createId", info.getCreator().getId());
            envelopeJson.put("createMSPID", info.getCreator().getMspid());
            envelopeJson.put("isValid", info.isValid());
            envelopeJson.put("nonce", Hex.encodeHexString(info.getNonce()));

            log.debug("channelId = " + info.getChannelId());
            log.debug("nonce = " + Hex.encodeHexString(info.getNonce()));
            log.debug("createId = " + info.getCreator().getId());
            log.debug("createMSPID = " + info.getCreator().getMspid());
            log.debug("isValid = " + info.isValid());
            log.debug("transactionID = " + info.getTransactionID());
            log.debug("validationCode = " + info.getValidationCode());
            log.debug("timestamp = " + Utils.parseDateFormat(new Date(info.getTimestamp().getTime())));
            log.debug("type = " + info.getType());

            if (info.getType() == EnvelopeType.TRANSACTION_ENVELOPE) {
                TransactionEnvelopeInfo txeInfo = (TransactionEnvelopeInfo) info;
                JSONObject transactionEnvelopeInfoJson = new JSONObject();
                int txCount = txeInfo.getTransactionActionInfoCount();
                transactionEnvelopeInfoJson.put("txCount", txCount);
                transactionEnvelopeInfoJson.put("isValid", txeInfo.isValid());
                transactionEnvelopeInfoJson.put("validationCode", txeInfo.getValidationCode());

                log.debug("Transaction number " + blockNumber + " has actions count = " + txCount);
                log.debug("Transaction number " + blockNumber + " isValid = " + txeInfo.isValid());
                log.debug("Transaction number " + blockNumber + " validation code = " + txeInfo.getValidationCode());

                transactionEnvelopeInfoJson.put("transactionActionInfoArray", getTransactionActionInfoJsonArray(txeInfo, txCount));
                envelopeJson.put("transactionEnvelopeInfo", transactionEnvelopeInfoJson);
            }
            envelopeJsonArray.add(envelopeJson);
        }
        return envelopeJsonArray;
    }

    /** 解析交易请求集合 */
    private JSONArray getTransactionActionInfoJsonArray(TransactionEnvelopeInfo txeInfo, int txCount) throws UnsupportedEncodingException, InvalidProtocolBufferException {
        JSONArray transactionActionInfoJsonArray = new JSONArray();
        for (int i = 0; i < txCount; i++) {
            TransactionEnvelopeInfo.TransactionActionInfo txInfo = txeInfo.getTransactionActionInfo(i);
            int endorsementsCount = txInfo.getEndorsementsCount();
            int chaincodeInputArgsCount = txInfo.getChaincodeInputArgsCount();
            JSONObject transactionActionInfoJson = new JSONObject();
            transactionActionInfoJson.put("responseStatus", txInfo.getResponseStatus());
            transactionActionInfoJson.put("responseMessageString", printableString(new String(txInfo.getResponseMessageBytes(), "UTF-8")));
            transactionActionInfoJson.put("endorsementsCount", endorsementsCount);
            transactionActionInfoJson.put("chaincodeInputArgsCount", chaincodeInputArgsCount);
            transactionActionInfoJson.put("status", txInfo.getProposalResponseStatus());
            transactionActionInfoJson.put("payload", printableString(new String(txInfo.getProposalResponsePayload(), "UTF-8")));

            log.debug("Transaction action " + i + " has response status " + txInfo.getResponseStatus());
            log.debug("Transaction action " + i + " has response message bytes as string: " + printableString(new String(txInfo.getResponseMessageBytes(), "UTF-8")));
            log.debug("Transaction action " + i + " has endorsements " + endorsementsCount);

            transactionActionInfoJson.put("endorserInfoArray", getEndorserInfoJsonArray(txInfo, endorsementsCount));

            log.debug("Transaction action " + i + " has " + chaincodeInputArgsCount + " chaincode input arguments");

            transactionActionInfoJson.put("argArray", getArgJSONArray(i, txInfo, chaincodeInputArgsCount));

            log.debug("Transaction action " + i + " proposal response status: " + txInfo.getProposalResponseStatus());
            log.debug("Transaction action " + i + " proposal response payload: " + printableString(new String(txInfo.getProposalResponsePayload())));

            TxReadWriteSetInfo rwsetInfo = txInfo.getTxReadWriteSet();
            JSONObject rwsetInfoJson = new JSONObject();
            if (null != rwsetInfo) {
                int nsRWsetCount = rwsetInfo.getNsRwsetCount();
                rwsetInfoJson.put("nsRWsetCount", nsRWsetCount);
                log.debug("Transaction action " + i + " has " + nsRWsetCount + " name space read write sets");
                rwsetInfoJson.put("nsRwsetInfoArray", getNsRwsetInfoJsonArray(rwsetInfo));
            }
            transactionActionInfoJson.put("rwsetInfo", rwsetInfoJson);
            transactionActionInfoJsonArray.add(transactionActionInfoJson);
        }
        return transactionActionInfoJsonArray;
    }

    /** 解析参数 */
    private JSONArray getArgJSONArray(int i, TransactionEnvelopeInfo.TransactionActionInfo txInfo, int chaincodeInputArgsCount) throws UnsupportedEncodingException {
        JSONArray argJsonArray = new JSONArray();
        for (int z = 0; z < chaincodeInputArgsCount; ++z) {
            argJsonArray.add(printableString(new String(txInfo.getChaincodeInputArgs(z), "UTF-8")));
            log.debug("Transaction action " + i + " has chaincode input argument " + z + "is: " + printableString(new String(txInfo.getChaincodeInputArgs(z), "UTF-8")));
        }
        return argJsonArray;
    }

    /** 解析背书信息 */
    private JSONArray getEndorserInfoJsonArray(TransactionEnvelopeInfo.TransactionActionInfo txInfo, int endorsementsCount) {
        JSONArray endorserInfoJsonArray = new JSONArray();
        for (int n = 0; n < endorsementsCount; ++n) {
            BlockInfo.EndorserInfo endorserInfo = txInfo.getEndorsementInfo(n);
            String signature = Hex.encodeHexString(endorserInfo.getSignature());
            String id = endorserInfo.getId();
            String mspId = endorserInfo.getMspid();
            JSONObject endorserInfoJson = new JSONObject();
            endorserInfoJson.put("signature", signature);
            endorserInfoJson.put("id", id);
            endorserInfoJson.put("mspId", mspId);

            log.debug("Endorser " + n + " signature: " + signature);
            log.debug("Endorser " + n + " id: " + id);
            log.debug("Endorser " + n + " mspId: " + mspId);
            endorserInfoJsonArray.add(endorserInfoJson);
        }
        return endorserInfoJsonArray;
    }

    /** 解析读写集集合 */
    private JSONArray getNsRwsetInfoJsonArray(TxReadWriteSetInfo rwsetInfo) throws InvalidProtocolBufferException, UnsupportedEncodingException {
        JSONArray nsRwsetInfoJsonArray = new JSONArray();
        for (TxReadWriteSetInfo.NsRwsetInfo nsRwsetInfo : rwsetInfo.getNsRwsetInfos()) {
            final String namespace = nsRwsetInfo.getNamespace();
            KvRwset.KVRWSet rws = nsRwsetInfo.getRwset();
            JSONObject nsRwsetInfoJson = new JSONObject();

            nsRwsetInfoJson.put("readSet", getReadSetJSONArray(rws, namespace));
            nsRwsetInfoJson.put("writeSet", getWriteSetJSONArray(rws, namespace));
            nsRwsetInfoJsonArray.add(nsRwsetInfoJson);
        }
        return nsRwsetInfoJsonArray;
    }

    /** 解析读集 */
    private JSONArray getReadSetJSONArray(KvRwset.KVRWSet rws, String namespace) {
        JSONArray readJsonArray = new JSONArray();
        int rs = -1;
        for (KvRwset.KVRead readList : rws.getReadsList()) {
            rs++;
            String key = readList.getKey();
            long readVersionBlockNum = readList.getVersion().getBlockNum();
            long readVersionTxNum = readList.getVersion().getTxNum();
            JSONObject readInfoJson = new JSONObject();
            readInfoJson.put("namespace", namespace);
            readInfoJson.put("readSetIndex", rs);
            readInfoJson.put("key", key);
            readInfoJson.put("readVersionBlockNum", readVersionBlockNum);
            readInfoJson.put("readVersionTxNum", readVersionTxNum);
            readInfoJson.put("chaincode_version", String.format("[%s : %s]", readVersionBlockNum, readVersionTxNum));
            readJsonArray.add(readInfoJson);
            log.debug("Namespace " + namespace + " read set " + rs + " key " + key + " version [" + readVersionBlockNum + " : " + readVersionTxNum + "]");
        }
        return readJsonArray;
    }

    /** 解析写集 */
    private JSONArray getWriteSetJSONArray(KvRwset.KVRWSet rws, String namespace) throws UnsupportedEncodingException {
        JSONArray writeJsonArray = new JSONArray();
        int rs = -1;
        for (KvRwset.KVWrite writeList : rws.getWritesList()) {
            rs++;
            String key = writeList.getKey();
            String valAsString = printableString(new String(writeList.getValue().toByteArray(), "UTF-8"));
            JSONObject writeInfoJson = new JSONObject();
            writeInfoJson.put("namespace", namespace);
            writeInfoJson.put("writeSetIndex", rs);
            writeInfoJson.put("key", key);
            writeInfoJson.put("value", valAsString);
            log.debug("Namespace " + namespace + " write set " + rs + " key " + key + " has value " + valAsString);
            writeJsonArray.add(writeInfoJson);
        }
        return writeJsonArray;
    }

    private String printableString(final String string) {
        int maxLogStringLength = 64;
        if (string == null || string.length() == 0) {
            return string;
        }
        String ret = string.replaceAll("[^\\p{Print}]", "?");
        ret = ret.substring(0, Math.min(ret.length(), maxLogStringLength)) + (ret.length() > maxLogStringLength ? "..." : "");
        return ret;
    }
}
