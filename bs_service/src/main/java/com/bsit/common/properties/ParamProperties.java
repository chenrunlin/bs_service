package com.bsit.common.properties;

import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.bsit.common.utils.StringUtil;
/**
 * Title:ParamProperties
 * Description: 属性文件
 * @author chenrl
 * 2016年3月8日上午9:35:13
 */
public class ParamProperties {
	
	private static Logger log = Logger.getLogger(ParamProperties.class);  
	
	private static Hashtable<String, Properties> pptContainer = new Hashtable<String, Properties>();
	
	/**
	 * @Description: 根据属性名获取属性值
	 * @param @param propertiesNmae
	 * @return String
	 * @author chenrl
	 * 2016年3月4日上午10:17:09
	 */
	public static String getPropertiesDataByKey(String propertiesFileName, String propertiesName){
		Properties properties = getProperties(propertiesFileName);
		return properties == null ? null : properties.getProperty(propertiesName);
	}

	/**
	 * @Description: 根据属性文件名称获取属性文件
	 * @param propertiesFileName
	 * @return Properties 返回属性文件
	 * @author chenrl
	 * 2016年3月4日上午10:41:52
	 */
	private static Properties getProperties(String propertiesFileName) {
		if(StringUtil.isEmpty(propertiesFileName)){
			log.error("not find " + propertiesFileName);
			return null;
		}
		//从属性文件容器(pptContainer)中拿出对应的属性对象(properties)
		Properties properties = pptContainer.get(propertiesFileName);
		if (properties == null) {
			//如果属性对象(properties)为空，则load
			properties = loadPropertyFile(propertiesFileName);
		} 
		return properties;
	}

	/**
	 * @Description: 根据属性文件名称加载属性文件，并重新放入属性文件容器(pptContainer)中
	 * @param @param propertiesFileName
	 * @return Properties 属性对象
	 * @author chenrl
	 * 2016年3月4日上午10:44:50
	 */
	private static Properties loadPropertyFile(String propertiesFileName) {
		try {
			Properties properties = new Properties();
			properties.load(ParamProperties.class.getClassLoader().getResourceAsStream(propertiesFileName));
			if(properties != null){
				pptContainer.put(propertiesFileName, properties);
			}
			return properties;
		} catch (Exception e) {
			System.err.println("not find " + propertiesFileName);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @Description: 重新加载属性文件
	 * @param propertiesFileName
	 * @return void
	 * @author chenrl
	 * 2016年3月4日上午11:00:44
	 */
	public final static void reloadPropertyFile(String propertiesFileName) {
		pptContainer.remove(propertiesFileName);
		loadPropertyFile(propertiesFileName);
	}
	
	/**
	 * @param propertyFilePath
	 * @return Properties
	 * @author chenrl
	 * 2016年3月4日上午11:09:46
	 */
	public static void removePropertyFile(String propertiesFileName) {
		pptContainer.remove(propertiesFileName);
	}
	
	/**
	 * @Description: 删除属性值 
	 * @param propertiesFileName
	 * @param key
	 * @return Properties
	 * @author chenrl
	 * 2016年3月4日上午11:20:52
	 */
	public final static Properties removeValue(String propertiesFileName, String key) {
		Properties properties = getProperties(propertiesFileName);
		if (properties == null) {
			return null;
		}
		//移除
		properties.remove(key);
		return properties;
	//	return (String) properties.remove(key);
	}
	
	/**
	 * @Description: 删除属性文件中的Key数组所对应的键值对
	 * @param propertiesFileName
	 * @param key
	 * @return Properties
	 * @author chenrl
	 * 2016年3月4日上午11:18:13
	 */
	public static Properties removeValue(String propertiesFileName, String[] key) {
		if (key == null) {  
		   log.error("key[] is null!");  
		   return null;  
		}
		Properties properties = getProperties(propertiesFileName);
		if(properties == null){
			return null;
		}
		for (String strKey : key) {
			properties.remove(strKey);
		}
		return properties;
	}

	public static void main(String[] args) {
		String propertiesFileName1 = "config.properties";
		String propertiesName1 = "jdbc_url";
		String value1 = getPropertiesDataByKey(propertiesFileName1, propertiesName1);
		System.out.println(value1);
	}

}
