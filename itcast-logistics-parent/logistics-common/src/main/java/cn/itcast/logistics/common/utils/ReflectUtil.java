package cn.itcast.logistics.common.utils;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@SuppressWarnings("all")
public class ReflectUtil {

	/** 跳过的字段 **/
	public static final List<String> SKIP_OTHER_FIELDS = Arrays.asList("serialVersionUID");
	/** 跳过Object类的方法 **/
	public static final List<String> SKIP_OBJECT_METHODS = Arrays.asList("registerNatives","getClass","hashCode","equals","clone","toString","notify","notifyAll","wait","finalize");
	/** 8个基本类型 **/
	public static final List<String> BASE_TYPES = Arrays.asList("boolean","byte","char","short","int","long","float","double");
	/** 基本类型对应的包装类型 **/
	public static final List<String> PACK_TYPES = Arrays.asList("Boolean","Byte","Character","Short","Integer","Long","Float","Double");
	/** 日期类型 **/
	public static final List<String> DATE_TYPES = Arrays.asList("Date","Calendar");
	/** SQL中的日期类型 **/
	public static final List<String> SQL_DATE_TYPES = Arrays.asList("Date","Time","Timestamp");
	/** set方法前缀 **/
	public static final String PREFIX_SET_METHOD = "set";
	/** get方法前缀 **/
	public static final String PREFIX_GET_METHOD = "get";

	
	/**
	 * 把JavaBean转换为List<Pair<String, String>>(fieldName,fieldType)
	 * @param obj
	 * @return	The return value is multiple Pair<String, String>(fieldName,fieldType)
	 */
	public static List<Pair<String, String>> objToPair(Object obj) {
		List<Pair<String, String>> pairs = new LinkedList<Pair<String,String>>();
		Pair<String, String> pair = null;
		List<String> skipOtherFields = ReflectUtil.SKIP_OTHER_FIELDS;
		Class<?> caz = obj.getClass();
		Field[] fields = caz.getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				String name = field.getName();
				String type = field.getType().getSimpleName();
				if (skipOtherFields.contains(name)) {
					continue;
				}
				pair = new Pair<String, String>(name, type);
				pairs.add(pair);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return pairs;
	}
	
	/**
	 * 把JavaBean转换为三元组集合，格式为Triplet<String, String, Object>(fieldName,fieldType,fieldValue)
	 * @param obj
	 * @param includeEmptyField	是否包含JavaBean的空字段，true是，false不是
	 * @return	The return value is multiple Triplet<String, String, Object>(fieldName,fieldType,fieldValue)
	 */
	public static List<Triplet<String, String, Object>> objToTriplet(Object obj, boolean includeEmptyField) {
		List<Triplet<String, String, Object>> triplets = new LinkedList<Triplet<String,String,Object>>();
		Triplet<String, String, Object> triplet = null;
		List<String> skipOtherFields = ReflectUtil.SKIP_OTHER_FIELDS;
		Class<?> caz = obj.getClass();
		Field[] fields = caz.getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				String name = field.getName();
				String type = field.getType().getSimpleName();
				Object value = field.get(obj);
				if (skipOtherFields.contains(name)) {
					continue;
				}
				if (includeEmptyField) {
					triplet = new Triplet<String, String, Object>(name, type, value);
					triplets.add(triplet);
				} else {
					if (value == null) {
						continue;
					}
					triplet = new Triplet<String, String, Object>(name, type, value);
					triplets.add(triplet);					
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return triplets;
	}
	
	/**
	 * 将List<Triplet<String, String, Object>>转换为OUT类型
	 * @param <OUT>
	 * @param triplets
	 * @param result
	 * @return
	 */
	public static <OUT> OUT genObj(List<Triplet<String, String, Object>> triplets, OUT result) {
		Object newInstance = null;
		try {
			Class<?> caz = result.getClass();
			Field[] fields = caz.getDeclaredFields();
			Method[] methods = caz.getDeclaredMethods();
			List<Quartet<Method, String, String, Object>> allowMethods = new ArrayList<Quartet<Method, String, String, Object>>();
			for (Field field : fields) {
				String fieldName = field.getName();
				String fieldType = field.getType().getSimpleName().toLowerCase();
				if (SKIP_OTHER_FIELDS.contains(fieldName)) {
					continue;
				}
				for (Triplet<String,String,Object> triplet : triplets) {
					String dFieldName = triplet.getValue0();
					String dFieldType = triplet.getValue1();
					Object dFieldValue = triplet.getValue2();
					if (fieldName.equals(dFieldName)&&fieldType.equals(dFieldType)) {
						for (Method method : methods) {
							String methodName = method.getName();
							if (SKIP_OBJECT_METHODS.contains(methodName)) {
								continue;
							}
							if (methodName.toLowerCase().equals(PREFIX_SET_METHOD+fieldName.toLowerCase())) {
								allowMethods.add(new Quartet<>(method, dFieldName, dFieldType, dFieldValue));
							}
						}
					}
				}
			}
			if (allowMethods.size()>0) {
				newInstance = caz.newInstance();
				for (Quartet<Method, String, String, Object> allowMethod : allowMethods) {
					Method method = allowMethod.getValue0();
					String fieldType = allowMethod.getValue2();
					Object fieldValue = allowMethod.getValue3();
					method.setAccessible(true);
					Parameter[] paramNames = method.getParameters();
					int paramLength = paramNames.length;
					if (paramLength>0) {
						Object[] paramValues = new Object[paramLength];
						for (int i=0; i<paramLength; i++) {
							Parameter parameter = paramNames[i];
							if (parameter.getType().getSimpleName().toLowerCase().equals("date")) {
								if (fieldValue instanceof String) {
									String val = fieldValue.toString();
									paramValues[i]= DateUtil.strToDate(val,val.length());
								}
								if (fieldValue instanceof Date) {
									paramValues[i]=fieldValue;
								}
							} else {
								if(parameter.getType().getSimpleName().toLowerCase().equals(fieldType)) {
									paramValues[i]=fieldValue;
								}								
							}
						}
						method.invoke(newInstance, paramValues);
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return (OUT)newInstance;
	}
	
	public static void printFields(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			if (SKIP_OTHER_FIELDS.contains(fieldName)) {
				continue;
			}
			System.out.println(fieldName);
		}
	}
	
	
}
