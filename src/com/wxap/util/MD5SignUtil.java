package com.wxap.util;

public class MD5SignUtil {
	private static String timestamp="";
	private static String nonceStr="";
	
	public static String Sign(String content, String key)	throws Exception {
//		nonceStr = Sha1Util.getNonceStr();
//		timestamp = Sha1Util.getTimeStamp();
		String signStr = "";

		if ("" == key) {
			throw new Exception("财付通签名key不能为空！");
		}
		if ("" == content) {
			throw new Exception("财付通签名内容不能为空");
		}
		signStr = content + "&key=" + key;

		return MD5Util.MD5Encode(signStr,"UTF-8").toUpperCase();

	}
	public static boolean VerifySignature(String content, String sign, String md5Key) {
		String signStr = content + "&key=" + md5Key;
		String calculateSign = MD5Util.MD5(signStr).toUpperCase();
		String tenpaySign = sign.toUpperCase();
		return (calculateSign == tenpaySign);
	}
	public static String getTimestamp() {
		return timestamp;
	}
	public static void setTimestamp(String timestamp) {
		MD5SignUtil.timestamp = timestamp;
	}
	public static String getNonceStr() {
		return nonceStr;
	}
	public static void setNonceStr(String nonceStr) {
		MD5SignUtil.nonceStr = nonceStr;
	}
}
