package cn.aberic.simple.base;

/**
 * 数据库操作基础类
 * 
 * @author 杨毅 【2017年10月24日 - 20:58:23】
 */
public interface BaseDao {

	/**
	 * 新增对象
	 *
	 * @param object
	 *            对象
	 */
	int insert(Object object);

	/**
	 * 根据 id 删除对象
	 *
	 * @param id
	 *            对象所在表对应 id
	 */
	int delByID(String id);

	/**
	 * 根据 id 修改对象
	 *
	 * @param object
	 *            对象
	 */
	int update(Object object);

	/**
	 * 根据 id 获取
	 *
	 * @param id
	 *            对象所在表对应 id
	 *
	 * @return 对象
	 */
	<T> T getByID(Integer id);

}
