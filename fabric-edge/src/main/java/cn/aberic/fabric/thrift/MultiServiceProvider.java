package cn.aberic.fabric.thrift;

import cn.aberic.fabric.bean.Api;
import cn.aberic.thrift.chaincode.ChaincodeService;
import cn.aberic.thrift.channel.ChannelService;
import cn.aberic.thrift.common.SystemService;
import cn.aberic.thrift.league.LeagueService;
import cn.aberic.thrift.orderer.OrdererService;
import cn.aberic.thrift.org.OrgInfo;
import cn.aberic.thrift.org.OrgService;
import cn.aberic.thrift.peer.PeerService;
import cn.aberic.thrift.state.StateInfo;
import cn.aberic.thrift.state.StateService;
import cn.aberic.thrift.trace.TraceInfo;
import cn.aberic.thrift.trace.TraceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("multiService")
public class MultiServiceProvider {

    @Value("${api.ip}")
    private String apiIp;
    @Value("${api.port}")
    private int apiPort;

    public enum ServiceType {
        SYSTEM,
        LEAGUE,
        ORG,
        ORDERER,
        PEER,
        CHANNEL,
        CHAINCODE,
        STATE,
        TRACE
    }

    public SystemService.Client getSystemService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.SYSTEM);
    }
    public LeagueService.Client getLeagueService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.LEAGUE);
    }
    public OrgService.Client getOrgService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.ORG);
    }
    public OrdererService.Client getOrdererService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.ORDERER);
    }
    public PeerService.Client getPeerService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.PEER);
    }
    public ChannelService.Client getChannelService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.CHANNEL);
    }
    public ChaincodeService.Client getChaincodeService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.CHAINCODE);
    }
    public StateService.Client getStateService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.STATE);
    }
    public TraceService.Client getTraceService() {
        return getService(System.getenv("SERVICE_IP"), Integer.valueOf(System.getenv("SERVICE_PORT")), ServiceType.TRACE);
    }

//    public SystemService.Client getSystemService() {
//        return getService(apiIp, apiPort, ServiceType.SYSTEM);
//    }
//    public LeagueService.Client getLeagueService() {
//        return getService(apiIp, apiPort, ServiceType.LEAGUE);
//    }
//    public OrgService.Client getOrgService() {
//        return getService(apiIp, apiPort, ServiceType.ORG);
//    }
//    public OrdererService.Client getOrdererService() {
//        return getService(apiIp, apiPort, ServiceType.ORDERER);
//    }
//    public PeerService.Client getPeerService() {
//        return getService(apiIp, apiPort, ServiceType.PEER);
//    }
//    public ChannelService.Client getChannelService() {
//        return getService(apiIp, apiPort, ServiceType.CHANNEL);
//    }
//    public ChaincodeService.Client getChaincodeService() {
//        return getService(apiIp, apiPort, ServiceType.CHAINCODE);
//    }
//    public StateService.Client getStateService() {
//        return getService(apiIp, apiPort, ServiceType.STATE);
//    }
//    public TraceService.Client getTraceService() {
//        return getService(apiIp, apiPort, ServiceType.TRACE);
//    }

    private <T> T getService(String host, int port, ServiceType serviceType) {
        // 客户端需要连接到服务端，先新建一个socker的连接
        TSocket socket = new TSocket(host, port, 3000);
        // 客户端需要与服务端的传输方式及协议相同

        // 传输方式
        TTransport transport = new TFastFramedTransport(socket);
        try {
            transport.open(); // 开启连接
        } catch (TTransportException e) {
            e.printStackTrace();
            return null;
        }

        // 传输协议
        TProtocol protocol = new TBinaryProtocol(transport);

        TServiceClient client = null;
        switch (serviceType) {
            case SYSTEM:
                client = new SystemService.Client(new TMultiplexedProtocol(protocol, "system"));
                break;
            case LEAGUE:
                // 构建客户端对象
                client = new LeagueService.Client(new TMultiplexedProtocol(protocol, "league"));
                break;
            case ORG:
                client = new OrgService.Client(new TMultiplexedProtocol(protocol, "org"));
                break;
            case ORDERER:
                client = new OrdererService.Client(new TMultiplexedProtocol(protocol, "orderer"));
                break;
            case PEER:
                client = new PeerService.Client(new TMultiplexedProtocol(protocol, "peer"));
                break;
            case CHANNEL:
                client = new ChannelService.Client(new TMultiplexedProtocol(protocol, "channel"));
                break;
            case CHAINCODE:
                client = new ChaincodeService.Client(new TMultiplexedProtocol(protocol, "chaincode"));
                break;
            case STATE:
                client = new StateService.Client(new TMultiplexedProtocol(protocol, "state"));
                break;
            case TRACE:
                client = new TraceService.Client(new TMultiplexedProtocol(protocol, "trace"));
                break;
        }

        return (T) client;
    }

    public List<OrgInfo> getForPeerAndOrderer() {
        List<OrgInfo> orgs;
        try {
            orgs = getOrgService().listAll();
            for (OrgInfo org : orgs) {
                org.setLeagueName(getLeagueService().get(org.getLeagueId()).getName());
            }
        } catch (TException e) {
            orgs = new ArrayList<>();
            e.printStackTrace();
        }
        return orgs;
    }

    public StateInfo getState(int id, Api api) {
        StateInfo state = new StateInfo();
        state.setId(id);
        state.setStrArray(Arrays.asList(api.exec.trim().split(",")));
        return state;
    }

    public TraceInfo getTrace(int id, Api api) {
        TraceInfo trace = new TraceInfo();
        trace.setId(id);
        trace.setTrace(api.exec.trim());
        return trace;
    }

    public String formatState(StateInfo state) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", state.getId());
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(state.getStrArray()));
        jsonObject.put("strArray", jsonArray);
        return jsonObject.toJSONString();
    }

    public String formatTrace(TraceInfo trace) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", trace.getId());
        jsonObject.put("trace", trace.getTrace());
        return jsonObject.toJSONString();
    }

}
