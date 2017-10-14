package com.choice.wxc.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.choice.wxc.exception.CRUDException;
/**
 * AES加密、解密公用方法
 * @author ZGL
 * @Date 2014-11-03 09:58:56
 *
 */
public class AESEncrypt {

	/** 
	 * AES加密 
	 *  
	 * @param content 需要加密的内容 
	 * @param password  加密密码 
	 * @return 
	 */  
	public static byte[] encrypt(String content, String password) {  
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(password.getBytes());
            kgen.init(128,secureRandom);
            SecretKey secretKey = kgen.generateKey();  
            byte[] enCodeFormat = secretKey.getEncoded();  
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密   
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        }  
        return null;  
	} 
	
	/**AES解密 
	 * @param content  待解密内容 
	 * @param password 解密密钥 
	 * @return 
	 */  
	public static byte[] decrypt(byte[] content, String password) {  
	        try {  
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
                secureRandom.setSeed(password.getBytes());
                kgen.init(128,secureRandom);
                 SecretKey secretKey = kgen.generateKey();  
                 byte[] enCodeFormat = secretKey.getEncoded();  
                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
                 Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
                byte[] result = cipher.doFinal(content);  
                return result; // 加密   
	        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
	        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
	        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	        	e.printStackTrace();  
	        } catch (BadPaddingException e) {  
                e.printStackTrace();  
	        }  
	        return null;  
	}  
	
    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < buf.length; i++) {
                    String hex = Integer.toHexString(buf[i] & 0xFF);
                    if (hex.length() == 1) {
                            hex = '0' + hex;
                    }
                    sb.append(hex.toUpperCase());
            }
            return sb.toString();
    }
    
    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length() < 1)
                    return null;
            byte[] result = new byte[hexStr.length()/2];
            for (int i = 0;i< hexStr.length()/2; i++) {
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
                    result[i] = (byte) (high * 16 + low);
            }
            return result;
    }
    /**
     * 转码解密防止产生乱码
     * @param strParam
     * @param erpKey
     * @return
     * @throws CRUDException
     */
    public static String getDecryptToString(String strParam,String erpKey) throws Exception{
    	//结果字符串
    	String str= "";
    	try{
			//将16进制转为2进制以便解密
			byte[] decryptFrom = AESEncrypt.parseHexStr2Byte(strParam);  
			//解密
			if(ValueCheck.IsNotEmpty(decryptFrom)){
				byte[] decryptResult  = AESEncrypt.decrypt(decryptFrom,  erpKey);
				str = new String(decryptResult,"utf-8");
			}
    	}catch (Exception e) {
    		throw new Exception(e);
		}
		return str;
    }
    public static void main(String[] args) throws Exception, UnsupportedEncodingException {
	}
}
