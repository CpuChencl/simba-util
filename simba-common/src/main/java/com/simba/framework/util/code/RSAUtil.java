package com.simba.framework.util.code;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.xmlbeans.impl.util.Base64;

import com.simba.model.constant.ConstantData;

/**
 * RSA加密工具类
 * 
 * @author caozhejun
 *
 */
public class RSAUtil {

	/**
	 * rsa加密
	 * 
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encode(String source, String key) throws Exception {
		RSAPublicKey publicKey = loadPublicKeyByStr(key);
		byte[] data = source.getBytes(ConstantData.DEFAULT_CHARSET);
		byte[] encData = encrypt(publicKey, data);
		return new String(Base64.encode(encData), ConstantData.DEFAULT_CHARSET);
	}

	/**
	 * 生成公钥
	 * 
	 * @param publicKeyStr
	 * @return
	 * @throws Exception
	 */
	public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
		byte[] buffer = Base64.decode(publicKeyStr.getBytes(ConstantData.DEFAULT_CHARSET));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 * @param plainTextData
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(plainTextData);
	}

}
