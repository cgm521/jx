package com.choice.wechat.web.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.choice.test.utils.Commons;
import com.choice.wechat.persistence.alipay.AlipayBillMapper;
import com.choice.wechat.util.alipay.util.AlipaySubmit;
/**
 * 
 * @author King
 * 非定时,直接后台下载接口
 * 可供异常情况下下载某一天的数据
 */
@Controller
@RequestMapping(value="alipay")
public class ReciveAlipayBillController {
	@Autowired
	private AlipayBillMapper alipayBillMapper;
	
	@RequestMapping(value="downLoadAlipayBills")
	public void downLoadAlipayBills(HttpServletRequest request, HttpServletResponse reponse){
		try{
			String now = request.getParameter("now");//yyyy-MM-dd HH:mm:ss
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String EndTime = "";
			Date date = null;
			if(now==null || "".equals(now)){
				date = new Date();
				EndTime = sf.format(date);
			}else{
				date = sf.parse(EndTime);
			}
			
			Calendar startC = Calendar.getInstance();
			startC.setTime(date);
			startC.add(Calendar.DAY_OF_MONTH, -1);
			
			String StartTime = sf.format(startC.getTime());
			
			List<Map<String,String>> list = AlipaySubmit.getBills("1",StartTime,EndTime);
			
			alipayBillMapper.deleteBills(Commons.getConfig().getProperty("alipay_pid"), StartTime.substring(0,11));
			alipayBillMapper.batchInsertBills(list);
			
			reponse.getWriter().write("Y");
		}catch(Exception e){
			e.printStackTrace();
			try {
				reponse.getWriter().write("N");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
