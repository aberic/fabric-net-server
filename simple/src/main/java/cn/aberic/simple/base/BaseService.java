package cn.aberic.simple.base;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 业务层基类
 * 
 * @author 杨毅 【2017年10月24日 - 20:58:52】
 */
public interface BaseService {

	Logger logger = LogManager.getLogger(BaseService.class);

	int SUCCESS = 200;
	int FAIL = 40029;
	int UN_LOGIN = 8;

	default String responseSuccess(String result) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", SUCCESS);
		jsonObject.put("result", result);
		return jsonObject.toString();
	}

	default String responseSuccess(String result, String txid) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", SUCCESS);
		jsonObject.put("result", result);
		jsonObject.put("txid", txid);
		return jsonObject.toString();
	}

	default String responseSuccess(JSONObject json) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", SUCCESS);
		jsonObject.put("data", json);
		return jsonObject.toString();
	}

	default String responseFail(String result) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", FAIL);
		jsonObject.put("error", result);
		return jsonObject.toString();
	}

	default String responseUnLogin() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", UN_LOGIN);
		return jsonObject.toString();
	}

}
