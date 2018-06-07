package com.example.utils.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;


public class RedisUtil {
	
	private RedisTemplate<String, ?> redisTemplate;
	
	private RedisSerializer<String> serializer; 
	
	public RedisUtil(RedisTemplate<String, ?> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.serializer = redisTemplate.getStringSerializer();
	}
	/**
	 * 
	* @Title: delete
	* @Description:删除键值
	* @param @param keys
	* @param @return
	* @return Long
	* @throws
	 */
	public Long delete(final String ... keys){
		return redisTemplate.execute(new RedisCallback<Long>(){
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[][] bytes = convert(keys);
				if(null != keys){
					return connection.del(bytes);					
				}else{
					return 0L;
				}

			}
		});
	}
	/**
	 * 
	* @Title: expire
	* @Description:多次时间后过期
	* @param @param key
	* @param @param seconds
	* @return void
	* @throws
	 */
	public void expire(final String key,final Long seconds){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.expire(serializer.serialize(key), seconds);
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: expireAt
	* @Description:在一个时间点过期
	* @param @param key
	* @param @param unixTime
	* @return void
	* @throws
	 */
	public void expireAt(final String key,final Long unixTime){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.expireAt(serializer.serialize(key), unixTime);
				return null;
			}
		});
	}
	
	/**
	 * 
	* @Title: convert
	* @Description:字符串数组转换成byte数组
	* @param @param fields
	* @param @return
	* @return byte[][]
	* @throws
	 */
	private byte[][] convert(String ...fields){
		if(null != fields){
			int length = fields.length;
			byte[][] bytes = new byte[length][];
			for(int i =0;i<length;i++){
				bytes[i] = serializer.serialize(fields[i]);
			}
			return bytes;					
		}else{
			return null;
		}
	}
	/**
	 * 
	* @Title: toByte
	* @Description:对象转换成byte数组
	* @param @param obj
	* @param @return
	* @return byte[]
	* @throws
	 */
	private byte[] toByte(Object obj){
		byte[] bytes = null;   
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)){
		        oos.writeObject(obj);  
		        bytes = baos.toByteArray(); 
		        oos.close(); 
			} catch (IOException e) {
				Logger.getLogger(RedisUtil.class.getName()).log(Level.SEVERE, null, e);
			}        	
        } catch (IOException e) {
        	Logger.getLogger(RedisUtil.class.getName()).log(Level.SEVERE, null, e);
		}
	    return bytes;  
	}
	/**
	 * 
	* @Title: toObject
	* @Description:数组转换成对象
	* @param @param bytes
	* @param @return
	* @return Object
	* @throws
	 */
	private Object toObject(byte[] bytes){
		Object obj = null;  
	    try(ByteArrayInputStream bi = new ByteArrayInputStream(bytes)){	    	
	    	try (ObjectInputStream oi = new ObjectInputStream(bi)){
	    		 obj = oi.readObject();
	    	} catch (IOException e) {
	    		Logger.getLogger(RedisUtil.class.getName()).log(Level.SEVERE, null, e);
	    	}  
	    }catch(Exception e){
	    	Logger.getLogger(RedisUtil.class.getName()).log(Level.SEVERE, null, e);
	    }
		return obj;  
	}
	///////////////////////////////////////////////////////////////////////////////
	//操作字符串
	///////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	* @Title: getString
	* @Description:获取字符串
	* @param @param key
	* @param @return
	* @return String
	* @throws
	 */
	public String getString(final String key){
		return (String) redisTemplate.execute(new RedisCallback<String>(){
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.STRING.compareTo(connection.type(serializer.serialize(key))) == 0){
					return redisTemplate.getStringSerializer().deserialize(connection.get(serializer.serialize(key)));					
				}else{
					return null;
				}
			}
		});
	}
	/**
	 * 
	* @Title: setString
	* @Description:强制设置
	* @param @param key
	* @param @param value
	* @return void
	* @throws
	 */
	public void setString(final String key,final String value){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.STRING.compareTo(connection.type(serializer.serialize(key))) == 0 || !connection.exists(serializer.serialize(key))){
					connection.set(serializer.serialize(key), serializer.serialize(value));
				}
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: isExists
	* @Description:判断字符串是否存在
	* @param @param key
	* @param @return
	* @return Boolean
	* @throws
	 */
	public Boolean isExists(final String key){
		return 	redisTemplate.execute(new RedisCallback<Boolean>(){
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(serializer.serialize(key));
			}
		});
	}
	///////////////////////////////////////////////////////////////////////////////
	//操作map
	///////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	* @Title: setHashMapKeyAndValue
	* @Description:设置map
	* @param @param key
	* @param @param map
	* @return void
	* @throws
	 */
	public void setHashMapKeyAndValue(final String key,final String field,final String value){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0 || !connection.exists(serializer.serialize(key))){
					return connection.hSet(serializer.serialize(key), serializer.serialize(field), serializer.serialize(value));
				}else{
					return null;
				}
			}
		});
	}
	/**
	 * 
	* @Title: getHashMapValue
	* @Description:获取map中的其中一个值
	* @param @param key
	* @param @param field
	* @param @return
	* @return String
	* @throws
	 */
	public String getHashMapValue(final String key,final String field){
		return redisTemplate.execute(new RedisCallback<String>(){
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0){
					return serializer.deserialize(connection.hGet(serializer.serialize(key), serializer.serialize(field)));
				}else{
					return null;
				}
			}
		});
	}
	/**
	 * 
	* @Title: setHashMapKeyAndObjectValue
	* @Description:设置map值是object的map的值
	* @param @param key
	* @param @param field
	* @param @param value
	* @return void
	* @throws
	 */
	public void setHashMapKeyAndObjectValue(final String key,final String field,final Object value){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0 || !connection.exists(serializer.serialize(key))){
					return connection.hSet(serializer.serialize(key), serializer.serialize(field),toByte(value));
				}else{
					return null;
				}
			}
		});
	}
	/**
	 * 
	* @Title: getHashMapObjectValue
	* @Description:获取map值是object的map的值
	* @param @param key
	* @param @param field
	* @param @return
	* @return String
	* @throws
	 */
	public <T> T getHashMapObjectValue(final String key,final String field){
		return redisTemplate.execute(new RedisCallback<T>(){
			@SuppressWarnings("unchecked")
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0){
					byte[] bytes = connection.hGet(serializer.serialize(key), serializer.serialize(field));
					return (T)toObject(bytes);
				}else{
					return null;
				}
			}
		});
	}
	
	/**
	 * 
	* @Title: deleteHashMapValue
	* @Description:删除map中的一个值
	* @param @param key
	* @param @param field
	* @return void
	* @throws
	 */
	public void deleteHashMapValue(final String key,final String ... fields){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0){
					byte[][] bytes = convert(fields);
					if(null != bytes){
						connection.hDel(serializer.serialize(key), bytes);
					}
				}
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: setHashMap
	* @Description:批量设置map,如果键值已经存在则被覆盖
	* @param @param key
	* @param @param map
	* @return void
	* @throws
	 */
	public void setHashMap(final String key,final Map<String,String> map){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0|| !connection.exists(serializer.serialize(key))){					
					BoundHashOperations<String,byte[],byte[]> bho = redisTemplate.boundHashOps(key);
					for(Entry<?, ?> entry :map.entrySet()){
						bho.put(serializer.serialize(entry.getKey().toString()), serializer.serialize(entry.getValue().toString()));
					}
					connection.hMSet(serializer.serialize(key), bho.entries());
				}
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: getHashMap
	* @Description:获得一个map
	* @param @param key
	* @param @return
	* @return Map<String,String>
	* @throws
	 */
	public Map<String,String> getHashMap(final String key){
		return redisTemplate.execute(new RedisCallback<Map<String,String>>(){
			@Override
			public Map<String,String> doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0){
					Map<String,String> map = new HashMap<String,String>();
					Map<byte[],byte[]> _map = connection.hGetAll(serializer.serialize(key));
					for(Entry<byte[], byte[]> entry:_map.entrySet()){
						map.put(serializer.deserialize(entry.getKey()), serializer.deserialize(entry.getValue()));
					}
					return map;
				}else{
					return null;
				}
			}
		});
	}
	/**
	 * 
	* @Title: setObjectHashMap
	* @Description:设置对象map
	* @param @param key
	* @param @param map
	* @return void
	* @throws
	 */
	public <T> void setObjectHashMap(final String key,final Map<String,T> map){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0|| !connection.exists(serializer.serialize(key))){					
					BoundHashOperations<String,byte[],byte[]> bho = redisTemplate.boundHashOps(key);
					for(Entry<String, T> entry :map.entrySet()){
						bho.put(serializer.serialize(entry.getKey().toString()), toByte(entry.getValue()));
					}
					connection.hMSet(serializer.serialize(key), bho.entries());
				}
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: getObjectHashMap
	* @Description:获取对象map
	* @param @param key
	* @param @return
	* @return Map<String,T>
	* @throws
	 */
	public <T> Map<String,T> getObjectHashMap(final String key){
		return redisTemplate.execute(new RedisCallback<Map<String,T>>(){
			@Override
			public Map<String,T> doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0){
					Map<String,T> map = new HashMap<String,T>();
					Map<byte[],byte[]> _map = connection.hGetAll(serializer.serialize(key));
					for(Entry<byte[], byte[]> entry:_map.entrySet()){
						@SuppressWarnings("unchecked")
						T t = (T) toObject(entry.getValue());
						map.put(serializer.deserialize(entry.getKey()), t);
					}
					return map;
				}else{
					return null;
				}
			}
		});
	}
	/**
	 * 
	* @Title: getMapSize
	* @Description:获得map的尺寸
	* @param @param key
	* @param @return
	* @return Long
	* @throws
	 */
	public Long getMapSize(final String key){
		return redisTemplate.execute(new RedisCallback<Long>(){
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0){
					return connection.hLen(serializer.serialize(key));
				}else{
					return null;
				}
			}
		});
	}
	/**
	 * 
	* @Title: isExistsMapKey
	* @Description:判断map中是否包含键
	* @param @param key
	* @param @param field
	* @param @return
	* @return Boolean
	* @throws
	 */
	public Boolean isExistsMapKey(final String key,final String field){
		return redisTemplate.execute(new RedisCallback<Boolean>(){
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.HASH.compareTo(connection.type(serializer.serialize(key))) == 0){
					return connection.hExists(serializer.serialize(key),serializer.serialize(field));
				}else{
					return false;
				}
			}
		});
	}
	///////////////////////////////////////////////////////////////////////////////
	//操作list
	///////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	* @Title: getList
	* @Description:获取一个list
	* @param @param key
	* @param @return
	* @return List<String>
	* @throws
	 */
	public List<String> getList(final String key){
		return redisTemplate.execute(new RedisCallback<List<String>>(){
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.LIST.compareTo(connection.type(serializer.serialize(key))) == 0){
					Long length = connection.lLen(serializer.serialize(key));
					List<byte[]> _list = connection.lRange(serializer.serialize(key), 0L, length);
					List<String> list = new ArrayList<String>();
					for(byte[] _byte:_list){
						list.add(serializer.deserialize(_byte));
					}
					return list;
				}else{
					return null;
				}
			}
		});
	}
	/**
	 * 
	* @Title: setList
	* @Description:设置一个数组
	* @param @param key
	* @param @param list
	* @return void
	* @throws
	 */
	public void setList(final String key,final List<String> list){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.LIST.compareTo(connection.type(serializer.serialize(key))) == 0 || !connection.exists(serializer.serialize(key))){
					for(String string:list){
						connection.rPush(serializer.serialize(key), serializer.serialize(string));
					}
				}
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: setListValue
	* @Description:修改list中的一个值
	* @param @param key
	* @param @param index
	* @param @param value
	* @return void
	* @throws
	 */
	public void setListValue(final String key,final Long index,final String value){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.LIST.compareTo(connection.type(serializer.serialize(key))) == 0 || !connection.exists(serializer.serialize(key))){
					connection.lSet(serializer.serialize(key), index, serializer.serialize(value));
				}
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: getListValue
	* @Description:获得list其中一个的值
	* @param @param key
	* @param @param index
	* @param @return
	* @return String
	* @throws
	 */
	public String getListValue(final String key,final Long index){
		return redisTemplate.execute(new RedisCallback<String>(){
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.LIST.compareTo(connection.type(serializer.serialize(key))) == 0){
					return serializer.deserialize(connection.lIndex(serializer.serialize(key), index));
				}
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: getListSize
	* @Description:获得list的尺寸
	* @param @param key
	* @param @return
	* @return Long
	* @throws
	 */
	public Long getListSize(final String key){
		return redisTemplate.execute(new RedisCallback<Long>(){
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.LIST.compareTo(connection.type(serializer.serialize(key))) == 0){
					return connection.lLen(serializer.serialize(key));
				}
				return null;
			}
		});
	}
	///////////////////////////////////////////////////////////////////////////////
	//操作set
	///////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	* @Title: setSet
	* @Description:插入set
	* @param @param key
	* @param @param set
	* @return void
	* @throws
	 */
	public void setSet(final String key,final Set<String> set){
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.SET.compareTo(connection.type(serializer.serialize(key))) == 0 || !connection.exists(serializer.serialize(key))){
					int length = set.size();
					if(length > 0){
						byte[][] bytes = new byte[length][];
						int i = 0;
						for(String string:set){
							bytes[i] = serializer.serialize(string);
							i++;
						}
						connection.sAdd(serializer.serialize(key), bytes);
					}
				}
				return null;
			}
		});
	}
	/**
	 * 
	* @Title: getSet
	* @Description:获取set
	* @param @param key
	* @param @return
	* @return Set<String>
	* @throws
	 */
	public Set<String> getSet(final String key){
		return redisTemplate.execute(new RedisCallback<Set<String>>(){
			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				if(DataType.SET.compareTo(connection.type(serializer.serialize(key))) == 0){
					Set<String> set = new HashSet<String>();
					BoundSetOperations<String, ?> bso = redisTemplate.boundSetOps(key);
					for(Object b :bso.members()){
						set.add(new String((byte[])b));
					}
					return set;
				}else{
					return null;
				}
			}
		});
	}
}
