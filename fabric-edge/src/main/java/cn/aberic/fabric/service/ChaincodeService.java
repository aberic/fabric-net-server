package cn.aberic.fabric.service;

import cn.aberic.fabric.bean.Api;
import cn.aberic.fabric.dao.Chaincode;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:14
 * 邮箱：abericyang@gmail.com
 */
public interface ChaincodeService {

    int add(Chaincode chaincode);

    JSONObject install(Chaincode chaincode, MultipartFile file, Api api, boolean init);

    JSONObject instantiate(Chaincode chaincode, List<String> strArray);

    JSONObject upgrade(Chaincode chaincode, MultipartFile file, Api api);

    int update(Chaincode chaincode);

    List<Chaincode> listAll();

    List<Chaincode> listById(int id);

    Chaincode get(int id);

    int countById(int id);

    int count();

    int delete(int id);

    int deleteAll(int channelId);

}
