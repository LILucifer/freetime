package com.weixiao.smart.redis.jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;

import java.util.*;
import java.util.Map.Entry;

public class RedisUtils {

	public JedisCluster jedisCluster;

	private static RedisUtils redisUtils;

	/***
	 * 永久缓存
	 */
	public static int foreverTime = 0;

	/***
	 * 长期缓存 缓存， 24<X<34个小时
	 */
	public static int longTime = 3600 * 24 + (int) (Math.random() * 36000);

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	public void init() {
		redisUtils = this;
		redisUtils.jedisCluster = this.jedisCluster;
	}

	/**
	 * 
	 * TODO 基本写入缓存方法
	 * 
	 * @Title 
	 * @author 
	 * @param key
	 * @param value
	 * @param cacheSeconds
	 *            单位：秒(永不超时置为0)
	 */
	public static String set(String key, String value, int cacheSeconds) {
		if (cacheSeconds == 0) {
			return redisUtils.jedisCluster.set(key, value);
		} else {
			return redisUtils.jedisCluster.setex(key, cacheSeconds, value);
		}
	}

	/**
	 * 
	 * TODO 基本的取值方法
	 * 
	 * @Title 
	 * @author 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return redisUtils.jedisCluster.get(key);
	}

	/**
	 * 
	 * TODO redis删除缓存
	 * 
	 * @Title 
	 * @author 
	 * @param key
	 */
	public static void del(String key) {
		redisUtils.jedisCluster.del(key);
	}

	/**
	 * TODO: ****** 天讯修改 ******
	 * 批量删除指定的key
	 * @param keysPattern 要删除的key匹配字符串，可以用正则
	 */
	public static void delKeysPattern(String keysPattern) {
		TreeSet<String> keys = new TreeSet<>();
		
		// 获取所有的节点
		Map<String, JedisPool> clusterNodes = redisUtils.jedisCluster.getClusterNodes();
		
		// 遍历节点 获取所有符合条件的KEY
		for (String k : clusterNodes.keySet()) {
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				ScanParams scanParams = new ScanParams();
				scanParams.count(Integer.MAX_VALUE);
				scanParams.match(keysPattern);
				ScanResult<String> result = connection.scan("0", scanParams);
				
				List<String> scanKeys = result.getResult();
				
//				keys.addAll(scanKeys);
				
				
				
				if (scanKeys.size() > 0) {
                    Map<Integer, List<String>> map = new HashMap<>(6600);
                    for (String key : scanKeys) {
                    	//cluster模式执行多key操作的时候，这些key必须在同一个slot上
                        int slot = JedisClusterCRC16.getSlot(key);
                        
                        //按slot将key分组，相同slot的key一起提交
                        if (map.containsKey(slot)) {
                            map.get(slot).add(key);
                        } else {
                            map.put(slot, Lists.newArrayList(key));
                        }
                    }
                    for (Entry<Integer, List<String>> integerListEntry : map.entrySet()) {
                    	connection.del(integerListEntry.getValue().toArray(new String[integerListEntry.getValue().size()]));
                    }
                }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				connection.close();
			}
		}

		
//		if (keys.size() > 0) {
//			String[] keyArray = keys.toArray(new String[keys.size()]);
//			redisUtils.jedisCluster.del(keyArray);
//		}
	}

	/**
	 * 
	 * TODO 是否存在指定key的缓存
	 * 
	 * @Title 
	 * @author 
	 * @param key
	 * @return
	 */
	public static Boolean exists(String key) {
		return redisUtils.jedisCluster.exists(key);
	}

	/**
	 * 
	 * TODO 缓存redis存入单个对象(任何对象都可以)
	 * 
	 * @Title 
	 * @author 
	 * @param key
	 * @param obj
	 * @param cacheSeconds
	 *            单位：秒(永不超时置为0)
	 */
	public static void setObj(String key, Object obj, int cacheSeconds) {
		if (obj == null)
			return;
		if (cacheSeconds == 0) {
			redisUtils.jedisCluster.set(key, JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect));
		} else {
			redisUtils.jedisCluster.setex(key, cacheSeconds, JSON.toJSONString(obj));
		}
	}

	/**
	 * 
	 * TODO 缓存redis获取单个对象(对象obj或者Map<String,String>)
	 * 
	 * @Title 
	 * @author 
	 * @param key
	 *            键值key
	 * @param class
	 *            泛型类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObj(String key, Class<T> clazz) {
		return JSON.parseObject(redisUtils.jedisCluster.get(key), clazz);
	}

	/**
	 * 缓存redis获取map对象格式为Map<String,Object>
	 * 
	 * @param key
	 * @param clazz
	 *            map值的泛型
	 * @return
	 */
	public static <T> Map<String, T> getMap(String key, Class<T> clazz) {
		Map<String, String> map = redisUtils.getObj(key, Map.class);
		Map<String, T> result = new HashMap<String, T>();
		if (map != null) {
			for (Entry<String, String> entry : map.entrySet()) {
				result.put(entry.getKey(), JSON.parseObject(JSON.toJSONString(entry.getValue()), clazz));
			}
		}
		return result;
	}

	/**
	 * 
	 * TODO 缓存redis存入对象List
	 * 
	 * @Title 
	 * @author 
	 * @param <T>
	 * @param key
	 * @param obj
	 * @param cacheSeconds
	 *            单位：秒(永不超时置为0)
	 */
	public static <T> void setObjList(String key, List<T> objList, int cacheSeconds) {
		if (objList.size() == 0)
			return;
		redisUtils.jedisCluster.del(key);
		for (Object o : objList) {
			redisUtils.jedisCluster.rpush(key, JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect));
		}
		if (cacheSeconds != 0) {
			redisUtils.jedisCluster.expire(key, cacheSeconds);
		}
	}

	/**
	 * 
	 * TODO 缓存redis获取多个对象
	 * 
	 * @Title 
	 * @author 
	 * @param key
	 * @param class
	 *            泛型类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getObjList(String key, Class<T> clazz) {
		List<String> lrange = redisUtils.jedisCluster.lrange(key, 0, -1);
		List<T> list = new ArrayList<T>();
		if (lrange != null) {
			for (String s : lrange) {
				list.add(JSON.parseObject(s, clazz));
			}
		}

		return list;
	}

	/**
	 * 
	 * TODO 批量插入
	 * 
	 * @Title 
	 * @author 
	 * @param map(其中每对键值对应一条缓存)
	 */
	public static void batchSetObject(Map<String, Object> map) {
		JedisClusterPipeline jcp = JedisClusterPipeline.pipelined(redisUtils.jedisCluster);
		jcp.refreshCluster();
		try {
			for (Entry<String, Object> en : map.entrySet()) {
				jcp.set(en.getKey().toString(),
						JSON.toJSONString(en.getValue(), SerializerFeature.DisableCircularReferenceDetect));
			}
			jcp.syncAndReturnAll();
		} finally {
			jcp.close();
		}
	}

	/**
	 * 
	 * TODO 批量插入UC资源专用
	 * 
	 * @Title 
	 * @author 
	 * @param <T>
	 * @param map(其中每对键值对应一条缓存)
	 */
	public static <T> void batchSetCollection(Map<String, Collection<T>> map) {
		JedisClusterPipeline jcp = JedisClusterPipeline.pipelined(redisUtils.jedisCluster);
		jcp.refreshCluster();
		try {
			for (Entry<String, Collection<T>> en : map.entrySet()) {
				jcp.set(en.getKey().toString(),
						JSON.toJSONString(en.getValue(), SerializerFeature.DisableCircularReferenceDetect));
			}
			jcp.syncAndReturnAll();
		} finally {
			jcp.close();
		}
	}

}
