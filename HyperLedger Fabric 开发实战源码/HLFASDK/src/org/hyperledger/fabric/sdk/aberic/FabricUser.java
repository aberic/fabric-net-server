package org.hyperledger.fabric.sdk.aberic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import io.netty.util.internal.StringUtil;

/**
 * 联盟用户对象
 * 
 * @author 杨毅
 *
 * @date 2017年9月7日 - 下午4:36:53
 * @email abericyang@gmail.com
 */
class FabricUser implements User, Serializable {

	private static final long serialVersionUID = 5695080465408336815L;

	/** 名称 */
	private String name;
	/** 规则 */
	private Set<String> roles;
	/** 账户 */
	private String account;
	/** 从属联盟 */
	private String affiliation;
	/** 组织 */
	private String organization;
	/** 注册操作的密�? */
	private String enrollmentSecret;
	/** 会员id */
	private String mspId;
	/** 注册登记操作 */
	Enrollment enrollment = null; // �?要在测试env中访�?

	/** 存储配置对象 */
	private transient FabricStore keyValStore;
	private String keyValStoreName;

	public FabricUser(String name, String org, FabricStore store) {
		this.name = name;
		this.keyValStore = store;
		this.organization = org;
		this.keyValStoreName = toKeyValStoreName(this.name, org);

		String memberStr = keyValStore.getValue(keyValStoreName);
		if (null != memberStr) {
			saveState();
		} else {
			restoreState();
		}
	}

	/**
	 * 设置账户信息并将用户状�?�更新至存储配置对象
	 * 
	 * @param account
	 *            账户
	 */
	public void setAccount(String account) {
		this.account = account;
		saveState();
	}

	@Override
	public String getAccount() {
		return this.account;
	}

	/**
	 * 设置从属联盟信息并将用户状�?�更新至存储配置对象
	 * 
	 * @param affiliation
	 *            从属联盟
	 */
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
		saveState();
	}

	@Override
	public String getAffiliation() {
		return this.affiliation;
	}

	@Override
	public Enrollment getEnrollment() {
		return this.enrollment;
	}

	/**
	 * 设置会员id信息并将用户状�?�更新至存储配置对象
	 * 
	 * @param mspID
	 *            会员id
	 */
	public void setMspId(String mspID) {
		this.mspId = mspID;
		saveState();
	}

	@Override
	public String getMspId() {
		return this.mspId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 设置规则信息并将用户状�?�更新至存储配置对象
	 * 
	 * @param roles
	 *            规则
	 */
	public void setRoles(Set<String> roles) {
		this.roles = roles;
		saveState();
	}

	@Override
	public Set<String> getRoles() {
		return this.roles;
	}

	public String getEnrollmentSecret() {
		return enrollmentSecret;
	}

	/**
	 * 设置注册操作的密钥信息并将用户状态更新至存储配置对象
	 * 
	 * @param enrollmentSecret
	 *            注册操作的密�?
	 */
	public void setEnrollmentSecret(String enrollmentSecret) {
		this.enrollmentSecret = enrollmentSecret;
		saveState();
	}

	/**
	 * 设置注册登记操作信息并将用户状�?�更新至存储配置对象
	 * 
	 * @param enrollment
	 *            注册登记操作
	 */
	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
		saveState();
	}

	/**
	 * 确定这个名称是否已注�?
	 * 
	 * @return 与否
	 */
	public boolean isRegistered() {
		return !StringUtil.isNullOrEmpty(enrollmentSecret);
	}

	/**
	 * 确定这个名字是否已经注册
	 *
	 * @return 与否
	 */
	public boolean isEnrolled() {
		return this.enrollment != null;
	}

	/** 将用户状态保存至存储配置对象 */
	public void saveState() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.flush();
			keyValStore.setValue(keyValStoreName, Hex.toHexString(bos.toByteArray()));
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从键值存储中恢复该用户的状�??(如果找到的话)。如果找不到，什么也不要做�??
	 * 
	 * @return 返回用户
	 */
	private FabricUser restoreState() {
		String memberStr = keyValStore.getValue(keyValStoreName);
		if (null != memberStr) {
			// 用户在键值存储中被找到，因此恢复状�?��??
			byte[] serialized = Hex.decode(memberStr);
			ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
			try {
				ObjectInputStream ois = new ObjectInputStream(bis);
				FabricUser state = (FabricUser) ois.readObject();
				if (state != null) {
					this.name = state.name;
					this.roles = state.roles;
					this.account = state.account;
					this.affiliation = state.affiliation;
					this.organization = state.organization;
					this.enrollmentSecret = state.enrollmentSecret;
					this.enrollment = state.enrollment;
					this.mspId = state.mspId;
					return this;
				}
			} catch (Exception e) {
				throw new RuntimeException(String.format("Could not restore state of member %s", this.name), e);
			}
		}
		return null;
	}

	public static String toKeyValStoreName(String name, String org) {
		System.out.println("toKeyValStoreName = " + "user." + name + org);
		return "user." + name + org;
	}

}
