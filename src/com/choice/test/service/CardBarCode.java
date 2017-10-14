package com.choice.test.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * 条形码工具类
 * @author xqd
 *
 */
public class CardBarCode {
	/**
	 * 生成文件
	 * @param msg
	 * @param path
	 * @return
	 */
	public File generateFile(String msg,String path){
		File file=new File(path);
		try {
			generate(msg,new FileOutputStream(file));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return file;
	}
	/**
	 * 生成到字节
	 * @param msg
	 * @return
	 */
	public byte[] generate(String msg){
		ByteArrayOutputStream ous=new ByteArrayOutputStream();
		generate(msg,ous);
		return ous.toByteArray();
	}
	/**
	 * 生成到流
	 * @param msg
	 * @param ous
	 */
	public void generate(String msg,OutputStream ous){
		if(StringUtils.isEmpty(msg)||ous==null){
			return;
		}
		Code39Bean bean=new Code39Bean();
		//精细度
		int dpi=150;
		//module 宽度
		double moduleWidth=UnitConv.in2mm(1.0f/dpi);
		//配置对象
		bean.setModuleWidth(moduleWidth);
		bean.setWideFactor(3);
		bean.doQuietZone(false);
		
		String format="image/png";
		try {
			//输出到流
			BitmapCanvasProvider canvas=new BitmapCanvasProvider(ous, format, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
			//生成条形码
			bean.generateBarcode(canvas, msg);
			//结束绘制
			canvas.finish();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
