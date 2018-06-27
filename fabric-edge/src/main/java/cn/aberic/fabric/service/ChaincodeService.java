package cn.aberic.fabric.service;

import cn.aberic.fabric.dao.Chaincode;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:14
 * 邮箱：abericyang@gmail.com
 */
public interface ChaincodeService {

     int add(Chaincode chaincode);

     String install(Chaincode chaincode, ByteBuffer sourceBuff, String sourceFileName);

     String instantiate(Chaincode chaincode, List<String> strArray);

     int update(Chaincode chaincode);

     List<Chaincode> listAll();

     List<Chaincode> listById(int id);

    Chaincode get(int id);

     int countById(int id);

     int count();
    
}
