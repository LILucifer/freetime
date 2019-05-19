package com.weixiao.smart.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wulp
 *
 */
public class BeanDateConnverter implements Converter
{
	private static final Logger logger = LoggerFactory.getLogger(BeanDateConnverter.class);
	public static final String[] ACCEPT_DATE_FORMATS = {
		"yyyy-MM-dd HH:mm:ss",
		"yyyy-MM-dd"
	};

	public BeanDateConnverter() {
	}

	public Object convert(Class arg0, Object value) {
		logger.debug("conver " + value + " to date object");
		if(value==null) return null;
		String dateStr ="";
		if(value instanceof Date){
			SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateStr = fo.format(value);
		}else{
			dateStr=value.toString();
			dateStr=dateStr.replace("T", " ");
		}
		try{
			return DateUtils.parseDate(dateStr, ACCEPT_DATE_FORMATS);
		}catch(Exception ex){
			logger.debug("parse date error:"+ex.getMessage());
		}
		HashMap map;
		return null;
	}
}
