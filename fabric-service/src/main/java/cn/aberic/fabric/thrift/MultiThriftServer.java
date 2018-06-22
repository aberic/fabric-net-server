package cn.aberic.fabric.thrift;

import cn.aberic.thrift.chaincode.ChaincodeService;
import cn.aberic.thrift.channel.ChannelService;
import cn.aberic.thrift.common.SystemService;
import cn.aberic.thrift.league.LeagueService;
import cn.aberic.thrift.orderer.OrdererService;
import cn.aberic.thrift.org.OrgService;
import cn.aberic.thrift.peer.PeerService;
import cn.aberic.thrift.state.StateService;
import cn.aberic.thrift.trace.TraceService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 描述：完成Thrift入口的配置和启动
 *
 * @author : Aberic 【2018/5/30 11:00】
 */
@Configuration
public class MultiThriftServer {

    @Value("${server.port}")
    private int servicePort;

    @Resource
    private SystemService.Iface systemService;
    @Resource
    private LeagueService.Iface leagueService;
    @Resource
    private OrgService.Iface orgService;
    @Resource
    private OrdererService.Iface ordererService;
    @Resource
    private PeerService.Iface peerService;
    @Resource
    private ChannelService.Iface channelService;
    @Resource
    private ChaincodeService.Iface chaincodeService;
    @Resource
    private StateService.Iface stateService;
    @Resource
    private TraceService.Iface traceService;

    /** 该方法在构造完实例后启动 */
    @PostConstruct
    public void startThriftServer(){
        // 把Service中的服务注册到Thrift中的执行器中
        TProcessor systemProcessor = new SystemService.Processor<>(systemService);
        TProcessor leagueProcessor = new LeagueService.Processor<>(leagueService);
        TProcessor orgProcessor = new OrgService.Processor<>(orgService);
        TProcessor ordererProcessor = new OrdererService.Processor<>(ordererService);
        TProcessor peerProcessor = new PeerService.Processor<>(peerService);
        TProcessor channelProcessor = new ChannelService.Processor<>(channelService);
        TProcessor chaincodeProcessor = new ChaincodeService.Processor<>(chaincodeService);
        TProcessor stateProcessor = new StateService.Processor<>(stateService);
        TProcessor traceProcessor = new TraceService.Processor<>(traceService);
        // 注册多个TMultiplexedProcessor
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
        multiplexedProcessor.registerProcessor("system", systemProcessor);
        multiplexedProcessor.registerProcessor("league", leagueProcessor);
        multiplexedProcessor.registerProcessor("org", orgProcessor);
        multiplexedProcessor.registerProcessor("orderer", ordererProcessor);
        multiplexedProcessor.registerProcessor("peer", peerProcessor);
        multiplexedProcessor.registerProcessor("channel", channelProcessor);
        multiplexedProcessor.registerProcessor("chaincode", chaincodeProcessor);
        multiplexedProcessor.registerProcessor("state", stateProcessor);
        multiplexedProcessor.registerProcessor("trace", traceProcessor);
        // Thrift有三种网络模式，单线程、线程池及NIO（非阻塞）
        // 这里使用非阻塞的NIO网络模式
        TNonblockingServerSocket socket = null;
        try {
            socket = new TNonblockingServerSocket(servicePort);
        } catch (TTransportException e) {
            e.printStackTrace();
        }

        // 构建Server启动所需参数
        TNonblockingServer.Args args = new TNonblockingServer.Args(socket);
        args.processor(multiplexedProcessor);
        args.transportFactory(new TFramedTransport.Factory()); // 定义传输方式为帧传输
        args.protocolFactory(new TBinaryProtocol.Factory()); // 定义传输协议为二进制，调试时可选用Json

        TServer server = new TNonblockingServer(args);
        server.serve();
    }

}
