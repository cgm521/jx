package com.choice.wechat.util.alipay.util;

import com.choice.test.utils.Commons;
import com.choice.wechat.persistence.alipay.AlipayBillMapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoDownLoadAlipayBills {
	@Autowired
	private AlipayBillMapper alipayBillMapper;
	
	public void downLoadAlipayBills(){
		try{
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String EndTime = sf.format(date);
			
			Calendar startC = Calendar.getInstance();
			startC.setTime(date);
			startC.add(Calendar.DAY_OF_MONTH, -1);
			
			String StartTime = sf.format(startC.getTime());
			
			List<Map<String,String>> list = AlipaySubmit.getBills("1",StartTime,EndTime);
			
			alipayBillMapper.deleteBills(Commons.partner, StartTime.substring(0,11));
			alipayBillMapper.batchInsertBills(list);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
