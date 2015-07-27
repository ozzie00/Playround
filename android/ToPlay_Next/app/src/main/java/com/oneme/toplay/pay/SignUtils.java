package com.oneme.toplay.pay;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class SignUtils {



	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf   = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean verify(String content, String publickey, byte[] signature) {
		try {

			byte[] bobEncodedPubKey          = Base64.decode(publickey);
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(bobEncodedPubKey);
			KeyFactory keyFactory            = KeyFactory.getInstance(ALGORITHM);
			PublicKey bobPubKey              = keyFactory.generatePublic(bobPubKeySpec);
			Signature sig                    = Signature.getInstance(SIGN_ALGORITHMS);
			sig.initVerify(bobPubKey);
			sig.update(content.getBytes(DEFAULT_CHARSET));
			return (sig.verify(signature));

		} catch (Exception e) {

		}

		return false;

	}

}
