package com.andy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json操作帮助类
 * 
 * @author niexiaolong
 * 
 */
public class JsonUtils {

	public static JSONArray toJsonArray(Collection<?> objs) {
		return (JSONArray) JSON.toJSON(objs);
	}

	public static <T> List<T> toBeanList(String data, Class<T> bean) {
		return JSON.parseArray(data, bean);
	}
	
	public static <T> T toBean(String json, Class<T> bean) {
		return JSON.toJavaObject(toJSON(json), bean);
	}
	
	public static <T> Map<String,T> toMap(String json,Class<T> bean) {
		JSONObject jsonMap = toJSON(json);
		Map<String, T> resultMap = new HashMap<>(); 
		for(String key :jsonMap.keySet()){
			T t = toBean(jsonMap.getString(key),bean); 
		     resultMap.put(key, t);  
		}
		return resultMap;
	}
	
	public static <T> T toBean(JSONObject json, Class<T> bean) {
		return JSON.toJavaObject(json, bean);
	}

	public static JSONObject toJSON(String text) {
		return JSON.parseObject(text);
	}

	public static JSONObject toJSON(Object javaObject) {
		return (JSONObject) JSON.toJSON(javaObject);
	}

	public static String toString(Object javaObject) {
		return JSON.toJSONString(javaObject);
	}
	
	public static boolean isJson(Object text){
		boolean result = true;
		try{
			JSON.parse((String)text);
		}catch(Exception e){
			result = false;
		}
		return result;
	}
}
