package com.simba.framework.util.code;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.xmlbeans.impl.util.Base64;

import com.simba.model.constant.ConstantData;

public class RSASignature {

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	public static String sign(String content, String privateKey, String encode)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, SignatureException {
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey.getBytes(ConstantData.DEFAULT_CHARSET)));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
		signature.initSign(priKey);
		signature.update(content.getBytes(encode));
		byte[] signed = signature.sign();
		return new String(Base64.encode(signed), ConstantData.DEFAULT_CHARSET);
	}

	public static String sign(String content, String privateKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey.getBytes(ConstantData.DEFAULT_CHARSET)));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
		signature.initSign(priKey);
		signature.update(content.getBytes());
		byte[] signed = signature.sign();
		return new String(Base64.encode(signed), ConstantData.DEFAULT_CHARSET);
	}

	public static boolean doCheck(String content, String sign, String publicKey, String encode)
			throws UnsupportedEncodingException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decode(publicKey.getBytes(ConstantData.DEFAULT_CHARSET));
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
		signature.initVerify(pubKey);
		signature.update(content.getBytes(encode));
		boolean bverify = signature.verify(Base64.decode(sign.getBytes(ConstantData.DEFAULT_CHARSET)));
		return bverify;
	}

	public static boolean doCheck(String content, String sign, String publicKey)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decode(publicKey.getBytes(ConstantData.DEFAULT_CHARSET));
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
		signature.initVerify(pubKey);
		signature.update(content.getBytes());
		boolean bverify = signature.verify(Base64.decode(sign.getBytes(ConstantData.DEFAULT_CHARSET)));
		return bverify;

	}

}