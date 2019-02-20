package org.monet.mocks.businessunit.utils;

import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class LibrarySigner {

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	public static String signText(String text, String keyStorePath, String password) throws Exception {
		byte[] data = signText(text.getBytes(), keyStorePath, password);
		String sData = new String(LibraryBase64.encode(data));
		return sData;
	}

	private static byte[] signText(byte[] text, String keyStorePath, String password) throws Exception {
		KeyStore keyStore = java.security.KeyStore.getInstance("PKCS12");
		X509Certificate[] chain = new X509Certificate[1];

		keyStore.load(new FileInputStream(keyStorePath), password.toCharArray());
		String certificateAlias = keyStore.aliases().nextElement();
		chain[0] = (X509Certificate) keyStore.getCertificate(certificateAlias);

		return createSignature(text, certificateAlias, keyStore, chain, password.toCharArray());
	}

	private static byte[] createSignature(byte[] hash, String privateKeyAlias, KeyStore keyStore, X509Certificate[] chain, char[] password) throws Exception {
		ExternalSignatureCMSSignedDataGenerator generator = new ExternalSignatureCMSSignedDataGenerator();
		ExternalSignatureSignerInfoGenerator si = new ExternalSignatureSignerInfoGenerator(CMSSignedDataGenerator.DIGEST_SHA1,
			CMSSignedDataGenerator.ENCRYPTION_RSA);

		byte[] signedBytes = signMessage(hash, keyStore, privateKeyAlias, password);

		si.setCertificate(chain[0]);
		si.setSignedBytes(signedBytes);

		generator.addSignerInf(si);
		generator.addCertificatesAndCRLs(getCertStore(chain));
		CMSSignedData signedData = generator.generate(new CMSProcessableByteArray(hash), true);

		return signedData.getEncoded();
	}

	private static CertStore getCertStore(Certificate[] certs) throws GeneralSecurityException {
		ArrayList<Certificate> list = new ArrayList<Certificate>();

		for (int i = 0, length = certs == null ? 0 : certs.length; i < length; i++) {
			list.add(certs[i]);
		}

		return CertStore.getInstance("Collection", new CollectionCertStoreParameters(list), "BC");
	}

	private static byte[] signMessage(byte[] textBytes, KeyStore keyStore, String certificateAlias, char[] password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		PrivateKey privatekey = (PrivateKey) keyStore.getKey(certificateAlias, password);

		if (privatekey == null) {
			return null;
		} else {
			Signature signature = Signature.getInstance("SHA1withRSA", keyStore.getProvider());
			signature.initSign(privatekey);
			signature.update(textBytes);
			byte abyte1[] = signature.sign();

			return abyte1;
		}
	}

}
