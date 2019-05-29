package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.Home;
import cn.aberic.fabric.dao.entity.User;
import cn.aberic.fabric.service.AppService;
import cn.aberic.fabric.service.BlockService;
import cn.aberic.fabric.service.CAService;
import cn.aberic.fabric.service.ChaincodeService;
import cn.aberic.fabric.service.ChannelService;
import cn.aberic.fabric.service.LeagueService;
import cn.aberic.fabric.service.OrdererService;
import cn.aberic.fabric.service.OrgService;
import cn.aberic.fabric.service.PeerService;
import cn.aberic.fabric.service.UserService;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DataUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {

    @Resource
    private LeagueService leagueService;
    @Resource
    private OrgService orgService;
    @Resource
    private OrdererService ordererService;
    @Resource
    private PeerService peerService;
    @Resource
    private CAService caService;
    @Resource
    private ChannelService channelService;
    @Resource
    private ChaincodeService chaincodeService;
    @Resource
    private AppService appService;
    @Resource
    private BlockService blockService;
    @Resource
    private UserService userService;

    @GetMapping(value = "get")
    public JSONObject get(@RequestHeader String username, @RequestHeader String token) {
        JSONObject jsonObject = new JSONObject();
        User user = CacheUtil.getUser(token);
        if (null == user || !username.equals(user.getUsername())) {
            jsonObject.put("code", "88");
        } else {
            Home home = CacheUtil.getHome();
            if (null == home) {
                home = DataUtil.obtain().home(leagueService, orgService, ordererService,
                    peerService, caService, channelService, chaincodeService,
                    appService, blockService);
                CacheUtil.putHome(home);
            }
            //联盟
            jsonObject.put("leagueCount", home.getLeagueCount());
            //组织
            jsonObject.put("orgCount", home.getOrgCount());
            //排序
            jsonObject.put("ordererCount", home.getOrdererCount());
            //节点
            jsonObject.put("peerCount", home.getPeerCount());
            //CA
            jsonObject.put("caCount", home.getCaCount());
            //通道
            jsonObject.put("channelCount", home.getChannelCount());
            //链码
            jsonObject.put("chaincodeCount", home.getChaincodeCount());
            //应用
            jsonObject.put("appCount", home.getAppCount());
            //中间统计模块开始

            //通道区块交易曲线
            jsonObject.put("channelBlockLists", JSONArray.toJSON(home.getChannelBlockLists()));
            jsonObject.put("blockDaos", home.getBlockDaos());
            //通道区块比 通道交易比
            jsonObject.put("channelPercents", JSONArray.toJSON(home.getChannelPercents()));
            //平台今日区块数 平台今日交易量
            jsonObject.put("dayStatistics", home.getDayStatistics());
            //平台区块总数 平台总交易量 平台区块读写集总数
            jsonObject.put("platform", home.getPlatform());

            //平台日总区块曲线
            jsonObject.put("dayBlocks", home.getDayBlocks());
            jsonObject.put("dayBlocksJson", JSONObject.toJSON(home.getDayBlocks()));
            //平台日总交易量曲线
            jsonObject.put("dayTxs", home.getDayTxs());
            jsonObject.put("dayTxsJson", JSONObject.toJSON(home.getDayTxs()));
            //平台日总读写集曲线
            jsonObject.put("dayRWs", home.getDayRWs());
            jsonObject.put("dayRWsJson", JSONObject.toJSON(home.getDayRWs()));

            //区块记录
            jsonObject.put("blocks", home.getBlocks());

            //中间统计模块结束
        }
        return jsonObject;
    }

    @PostMapping(value = "auth")
    public JSONObject auth(@RequestBody User user) {
        return new JSONObject() {{
            put("token", userService.login(user));
        }};
    }

    @GetMapping(value = "invalid/{token}")
    public JSONObject invalid(@PathVariable String token) {
        CacheUtil.removeUser(token);
        return new JSONObject();
    }
}
