package org.hyperledger.fabric.sdk.aberic;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeType;
import org.hyperledger.fabric.sdk.BlockInfo.TransactionEnvelopeInfo;
import org.hyperledger.fabric.sdk.aberic.bean.Chaincode;
import org.hyperledger.fabric.sdk.aberic.bean.Orderers;
import org.hyperledger.fabric.sdk.aberic.bean.Peers;
import org.hyperledger.fabric.sdk.BlockListener;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class ChaincodeManager {

	private static Logger log = LoggerFactory.getLogger(ChaincodeManager.class);

	private FabricConfig config;
	private Orderers orderers;
	private Peers peers;
	private Chaincode chaincode;

	private HFClient client;
	private FabricOrg fabricOrg;
	private Channel channel;
	private ChaincodeID chaincodeID;

	public ChaincodeManager(String username, FabricConfig fabricConfig)
			throws CryptoException, InvalidArgumentException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException, TransactionException {
		this.config = fabricConfig;

		orderers = this.config.getOrderers();
		peers = this.config.getPeers();
		chaincode = this.config.getChaincode();

		client = HFClient.createNewInstance();
		log.debug("Create instance of HFClient");
		client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		log.debug("Set Crypto Suite of HFClient");

		fabricOrg = getFabricOrg(username, config.openCATLS());
		channel = getChannel();
		chaincodeID = getChaincodeID();

		client.setUserContext(fabricOrg.getPeerAdmin());
	}

	private FabricOrg getFabricOrg(String username, boolean openCATLS) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {

		// java.io.tmpdir : C:\Users\yangyi47\AppData\Local\Temp\
		File storeFile = new File(System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
		FabricStore fabricStore = new FabricStore(storeFile);

		// Get Org1 from configuration
		FabricOrg fabricOrg = new FabricOrg(username, peers, orderers, fabricStore, config.getCryptoConfigPath(), openCATLS);
		log.debug("Get FabricOrg");
		return fabricOrg;
	}

	private Channel getChannel()
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException, CryptoException, InvalidArgumentException, TransactionException {
		client.setUserContext(fabricOrg.getPeerAdmin());
		return getChannel(fabricOrg, client);
	}

	private Channel getChannel(FabricOrg fabricOrg, HFClient client)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException, CryptoException, InvalidArgumentException, TransactionException {
		Channel channel = client.newChannel(chaincode.getChannelName());
		log.debug("Get Chain " + chaincode.getChannelName());

		channel.setTransactionWaitTime(chaincode.getTransactionWaitTime());
		channel.setDeployWaitTime(chaincode.getDeployWaitTime());

		for (int i = 0; i < peers.get().size(); i++) {
			File peerCert = Paths.get(config.getCryptoConfigPath(), "/peerOrganizations", peers.getOrgDomainName(), "peers", peers.get().get(i).getPeerName(), "tls/server.crt")
					.toFile();
			if (!peerCert.exists()) {
				throw new RuntimeException(
						String.format("Missing cert file for: %s. Could not find at location: %s", peers.get().get(i).getPeerName(), peerCert.getAbsolutePath()));
			}
			Properties peerProperties = new Properties();
			peerProperties.setProperty("pemFile", peerCert.getAbsolutePath());
			// ret.setProperty("trustServerCertificate", "true"); //testing
			// environment only NOT FOR PRODUCTION!
			peerProperties.setProperty("hostnameOverride", peers.getOrgDomainName());
			peerProperties.setProperty("sslProvider", "openSSL");
			peerProperties.setProperty("negotiationType", "TLS");
			// 在grpc的NettyChannelBuilder上设置特定选项
			peerProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
			channel.addPeer(client.newPeer(peers.get().get(i).getPeerName(), fabricOrg.getPeerLocation(peers.get().get(i).getPeerName()), peerProperties));
			if (peers.get().get(i).isAddEventHub()) {
				channel.addEventHub(
						client.newEventHub(peers.get().get(i).getPeerEventHubName(), fabricOrg.getEventHubLocation(peers.get().get(i).getPeerEventHubName()), peerProperties));
			}
		}

		for (int i = 0; i < orderers.get().size(); i++) {
			File ordererCert = Paths.get(config.getCryptoConfigPath(), "/ordererOrganizations", orderers.getOrdererDomainName(), "orderers", orderers.get().get(i).getOrdererName(),
					"tls/server.crt").toFile();
			if (!ordererCert.exists()) {
				throw new RuntimeException(
						String.format("Missing cert file for: %s. Could not find at location: %s", orderers.get().get(i).getOrdererName(), ordererCert.getAbsolutePath()));
			}
			Properties ordererProperties = new Properties();
			ordererProperties.setProperty("pemFile", ordererCert.getAbsolutePath());
			ordererProperties.setProperty("hostnameOverride", orderers.getOrdererDomainName());
			ordererProperties.setProperty("sslProvider", "openSSL");
			ordererProperties.setProperty("negotiationType", "TLS");
			ordererProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
			ordererProperties.setProperty("ordererWaitTimeMilliSecs", "300000");
			channel.addOrderer(
					client.newOrderer(orderers.get().get(i).getOrdererName(), fabricOrg.getOrdererLocation(orderers.get().get(i).getOrdererName()), ordererProperties));
		}

		log.debug("channel.isInitialized() = " + channel.isInitialized());
		if (!channel.isInitialized()) {
			channel.initialize();
		}
		if (config.isRegisterEvent()) {
			log.debug("========================Event事件监听注册========================");
			channel.registerBlockListener(new BlockListener() {

				@Override
				public void received(BlockEvent event) {
					// TODO
					log.debug("========================Event事件监听开始========================");
					try {
						log.debug("event.getChannelId() = " + event.getChannelId());
						log.debug("event.getEvent().getChaincodeEvent().getPayload().toStringUtf8() = " + event.getEvent().getChaincodeEvent().getPayload().toStringUtf8());
						log.debug("event.getBlock().getData().getDataList().size() = " + event.getBlock().getData().getDataList().size());
						ByteString byteString = event.getBlock().getData().getData(0);
						String result = byteString.toStringUtf8();
						log.debug("byteString.toStringUtf8() = " + result);

						String r1[] = result.split("END CERTIFICATE");
						String rr = r1[2];
						log.debug("rr = " + rr);
					} catch (InvalidProtocolBufferException e) {
						// TODO
						e.printStackTrace();
					}
					log.debug("========================Event事件监听结束========================");
				}
			});
		}
		return channel;
	}

	private ChaincodeID getChaincodeID() {
		return ChaincodeID.newBuilder().setName(chaincode.getChaincodeName()).setVersion(chaincode.getChaincodeVersion()).setPath(chaincode.getChaincodePath()).build();
	}

	/**
	 * 执行智能合约
	 * 
	 * @param fcn
	 *            方法名
	 * @param args
	 *            参数数组
	 * @return
	 * @throws InvalidArgumentException
	 * @throws ProposalException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws IOException 
	 * @throws TransactionException 
	 * @throws CryptoException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 */
	public Map<String, String> invoke(String fcn, String[] args)
			throws InvalidArgumentException, ProposalException, InterruptedException, ExecutionException, TimeoutException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, CryptoException, TransactionException, IOException {
		Map<String, String> resultMap = new HashMap<>();

		Collection<ProposalResponse> successful = new LinkedList<>();
		Collection<ProposalResponse> failed = new LinkedList<>();

		/// Send transaction proposal to all peers
		TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
		transactionProposalRequest.setChaincodeID(chaincodeID);
		transactionProposalRequest.setFcn(fcn);
		transactionProposalRequest.setArgs(args);

		Map<String, byte[]> tm2 = new HashMap<>();
		tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
		tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
		tm2.put("result", ":)".getBytes(UTF_8));
		transactionProposalRequest.setTransientMap(tm2);

		long currentStart = System.currentTimeMillis();
		Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
		for (ProposalResponse response : transactionPropResp) {
			if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
				successful.add(response);
			} else {
				failed.add(response);
			}
		}
		log.info("channel send transaction proposal time = " + ( System.currentTimeMillis() - currentStart));

		Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(transactionPropResp);
		if (proposalConsistencySets.size() != 1) {
			log.error("Expected only one set of consistent proposal responses but got " + proposalConsistencySets.size());
		}

		if (failed.size() > 0) {
			ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
			log.error("Not enough endorsers for inspect:" + failed.size() + " endorser error: " + firstTransactionProposalResponse.getMessage() + ". Was verified: "
					+ firstTransactionProposalResponse.isVerified());
			resultMap.put("code", "error");
			resultMap.put("data", firstTransactionProposalResponse.getMessage());
			return resultMap;
		} else {
			log.info("Successfully received transaction proposal responses.");
			ProposalResponse resp = transactionPropResp.iterator().next();
			log.debug("TransactionID: " + resp.getTransactionID());
			byte[] x = resp.getChaincodeActionResponsePayload();
			String resultAsString = null;
			if (x != null) {
				resultAsString = new String(x, "UTF-8");
			}
			log.info("resultAsString = " + resultAsString);
			channel.sendTransaction(successful);
			resultMap.put("code", "success");
			resultMap.put("data", resultAsString);
			resultMap.put("txid", resp.getTransactionID());
			return resultMap;
		}

//		channel.sendTransaction(successful).thenApply(transactionEvent -> {
//			if (transactionEvent.isValid()) {
//				log.info("Successfully send transaction proposal to orderer. Transaction ID: " + transactionEvent.getTransactionID());
//			} else {
//				log.info("Failed to send transaction proposal to orderer");
//			}
//			// chain.shutdown(true);
//			return transactionEvent.getTransactionID();
//		}).get(chaincode.getInvokeWatiTime(), TimeUnit.SECONDS);
	}

	/**
	 * 查询智能合约
	 * 
	 * @param fcn
	 *            方法名
	 * @param args
	 *            参数数组
	 * @return
	 * @throws InvalidArgumentException
	 * @throws ProposalException
	 * @throws IOException 
	 * @throws TransactionException 
	 * @throws CryptoException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 */
	public Map<String, String> query(String fcn, String[] args) throws InvalidArgumentException, ProposalException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, CryptoException, TransactionException, IOException {
		Map<String, String> resultMap = new HashMap<>();
		String payload = "";
		QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
		queryByChaincodeRequest.setArgs(args);
		queryByChaincodeRequest.setFcn(fcn);
		queryByChaincodeRequest.setChaincodeID(chaincodeID);

		Map<String, byte[]> tm2 = new HashMap<>();
		tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
		tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
		queryByChaincodeRequest.setTransientMap(tm2);

		Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest, channel.getPeers());
		for (ProposalResponse proposalResponse : queryProposals) {
			if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
				log.debug("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() + ". Messages: "
						+ proposalResponse.getMessage() + ". Was verified : " + proposalResponse.isVerified());
				resultMap.put("code", "error");
				resultMap.put("data", "Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() + ". Messages: "
						+ proposalResponse.getMessage() + ". Was verified : " + proposalResponse.isVerified());
			} else {
				payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
				log.debug("Query payload from peer: " + proposalResponse.getPeer().getName());
				log.debug("TransactionID: " + proposalResponse.getTransactionID());
				log.debug("" + payload);
				resultMap.put("code", "success");
				resultMap.put("data", payload);
				resultMap.put("txid", proposalResponse.getTransactionID());
			}
		}
		return resultMap;
	}
	
	public Map<String, String> queryBlockByTransactionID(String txID) throws InvalidArgumentException, ProposalException, CertificateException, IOException {
		BlockInfo blockInfo = channel.queryBlockByTransactionID(txID);
		execBlockInfo(blockInfo);
		return null;
	}
	
	public Map<String, String> queryBlockByHash(byte[] blockHash) throws InvalidArgumentException, ProposalException, IOException {
		BlockInfo blockInfo = channel.queryBlockByHash(blockHash);
		execBlockInfo(blockInfo);
		return null;
	}
	
	public Map<String, String> queryBlockByNumber(long blockNumber) throws InvalidArgumentException, ProposalException, IOException {
		BlockInfo blockInfo = channel.queryBlockByNumber(blockNumber);
		execBlockInfo(blockInfo);
		return null;
	}
	
	private void execBlockInfo(BlockInfo blockInfo) throws InvalidArgumentException, IOException {
		final long blockNumber = blockInfo.getBlockNumber();
		log.debug("blockNumber = " + blockNumber);
		log.debug("data hash: " + Hex.encodeHexString(blockInfo.getDataHash()));
		log.debug("previous hash id: " + Hex.encodeHexString(blockInfo.getPreviousHash()));
		log.debug("calculated block hash is " + Hex.encodeHexString(SDKUtils.calculateBlockHash(blockNumber, blockInfo.getPreviousHash(), blockInfo.getDataHash())));
		
		final int envelopeCount = blockInfo.getEnvelopeCount();
		log.debug("block number " + blockNumber + " has " + envelopeCount + " envelope count:");
		
		for(EnvelopeInfo info: blockInfo.getEnvelopeInfos()) {
			final String channelId = info.getChannelId();
			log.debug("ChannelId = " + channelId);
			log.debug("Epoch = " + info.getEpoch());
			log.debug("TransactionID = " + info.getTransactionID());
			log.debug("ValidationCode = " + info.getValidationCode());
			log.debug("Timestamp = " + DateUtil.obtain().parseDateFormat(new Date(info.getTimestamp().getTime()), "yyyy年MM月dd日 HH时mm分ss秒"));
			log.debug("Type = " + info.getType());
			
			if (info.getType() == EnvelopeType.TRANSACTION_ENVELOPE) {
				BlockInfo.TransactionEnvelopeInfo txeInfo = (TransactionEnvelopeInfo) info;
				int txCount = txeInfo.getTransactionActionInfoCount();
				log.debug("Transaction number " + blockNumber + " has actions count = " + txCount);
				log.debug("Transaction number " + blockNumber + " isValid = " + txeInfo.isValid());
				log.debug("Transaction number " + blockNumber + " validation code = " + txeInfo.getValidationCode());
				
				for (int i = 0; i < txCount; i++) {
					BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo txInfo = txeInfo.getTransactionActionInfo(i);
					log.debug("Transaction action " + i + " has response status " + txInfo.getResponseStatus());
                    log.debug("Transaction action " + i + " has response message bytes as string: " + printableString(new String(txInfo.getResponseMessageBytes(), "UTF-8")));
					log.debug("Transaction action " + i + " has endorsements " + txInfo.getEndorsementsCount());
					
					for (int n = 0; n < txInfo.getEndorsementsCount(); ++n) {
                        BlockInfo.EndorserInfo endorserInfo = txInfo.getEndorsementInfo(n);
                        log.debug("Endorser " + n + " signature: " + Hex.encodeHexString(endorserInfo.getSignature()));
                        log.debug("Endorser " + n + " endorser: " + new String(endorserInfo.getEndorser(), "UTF-8"));
                    }
					
                    log.debug("Transaction action " + i + " has " + txInfo.getChaincodeInputArgsCount() + " chaincode input arguments");
                    for (int z = 0; z < txInfo.getChaincodeInputArgsCount(); ++z) {
                        log.debug("Transaction action " + i + " has chaincode input argument " + z + "is: " + printableString(new String(txInfo.getChaincodeInputArgs(z), "UTF-8")));
                    }

                    log.debug("Transaction action " + i + " proposal response status: " + txInfo.getProposalResponseStatus());
                    log.debug("Transaction action " + i + " proposal response payload: " + printableString(new String(txInfo.getProposalResponsePayload())));

                    TxReadWriteSetInfo rwsetInfo = txInfo.getTxReadWriteSet();
                    if (null != rwsetInfo) {
                        log.debug("Transaction action " + i + " has " + rwsetInfo.getNsRwsetCount() +" name space read write sets");

                        for (TxReadWriteSetInfo.NsRwsetInfo nsRwsetInfo : rwsetInfo.getNsRwsetInfos()) {
                        	final String namespace = nsRwsetInfo.getNamespace();
                            KvRwset.KVRWSet rws = nsRwsetInfo.getRwset();

                            int rs = -1;
                            for (KvRwset.KVRead readList : rws.getReadsList()) {
                                rs++;

                                log.debug("Namespace " + namespace + " read set " + rs + " key " + readList.getKey() + " version [" + readList.getVersion().getBlockNum() + " : " + readList.getVersion().getTxNum() + "]");
                            }

                            rs = -1;
                            for (KvRwset.KVWrite writeList : rws.getWritesList()) {
                                rs++;
                                String valAsString = printableString(new String(writeList.getValue().toByteArray(), "UTF-8"));
                                log.debug("Namespace " + namespace + " write set " + rs + " key " + writeList.getKey() + " has value " + valAsString);
                            }
                        }
                    }
				}
			}
		}
	}

    static String printableString(final String string) {
        int maxLogStringLength = 64;
        if (string == null || string.length() == 0) {
            return string;
        }
        String ret = string.replaceAll("[^\\p{Print}]", "?");
        ret = ret.substring(0, Math.min(ret.length(), maxLogStringLength)) + (ret.length() > maxLogStringLength ? "..." : "");
        return ret;
    }

}
