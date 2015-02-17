/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parelon.pskc;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.DestroyFailedException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * This class handles all the operation regarding both AES encryption/decryption
 * and HMAC-SHA1 signature.
 *
 * @author Vincenzo Abate
 * @author Marco Di Carlo
 */
public class CryptManager {

    private String privateKeyBytes, publicKeyBytes, aesKeyBytes, macKeyBytes;
    private Cipher aesCipher, rsaDecryptCipher, rsaEncryptCipher;
    private Mac macSignature;
    private PrivateKey rsaPrivateKey;
    private SecretKey aesKey, macKey;
    private PublicKey rsaPublicKey;
    private AlgorithmParameterSpec cipherParamSpec;
    private BlumBlumShub randomize;
    /**
     * Constructor
     */
    public CryptManager() {
        System.out.println("Inizializzazione randomizzatore");
        randomize = new BlumBlumShub(128);
        System.out.println("Randomizzatore inizializzato");
    }

    /**
     * Initialize RSA Keys and Ciphers. All RSA keys MUST be Base64Decode first!
     *
     * @param privateKeyFragment1 First half of the Private Key
     * @param privateKeyFragment2 Second half of the Private Key
     * @param publicKey Public Key
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     * @throws com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException
     */
    public void initRsaOperations(byte[] privateKeyFragment1, byte[] privateKeyFragment2, byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, Base64DecodingException, UnsupportedEncodingException, DestroyFailedException {
        initPrivateKey(privateKeyFragment1, privateKeyFragment2);
        this.rsaPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        this.rsaDecryptCipher = Cipher.getInstance("RSA");
        this.rsaDecryptCipher.init(Cipher.DECRYPT_MODE, this.rsaPrivateKey);
        this.rsaEncryptCipher = Cipher.getInstance("RSA");
        this.rsaEncryptCipher.init(Cipher.ENCRYPT_MODE, this.rsaPublicKey);
    }

    /**
     * Initialize AES-128-CBC Key and Cipher with PKCS5 Padding.
     *
     * @param aesKeyBytes
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public void initAesOperations(byte[] aesKeyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, Base64DecodingException, DecoderException {
        this.aesKey = new SecretKeySpec(Hex.decodeHex(new String(aesKeyBytes).toCharArray()), "AES");
        this.aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }
    /**
     * Initialize AES-128-CBC Key and Cipher with PKCS5 Padding.
     *
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public String initAesOperations() throws NoSuchAlgorithmException, NoSuchPaddingException, Base64DecodingException, DecoderException {
        byte[] aesKeyBytes = new byte[16];
        this.randomize.nextBytes(aesKeyBytes);
        this.aesKey = new SecretKeySpec(aesKeyBytes, "AES");
        this.aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        return Hex.encodeHexString(aesKeyBytes);
    }

    /**
     * Initialize HMAC-SHA1 key and cipher.
     *
     * @param macKeyBytes
     * @throws NoSuchAlgorithmException
     */
    public void initHmacOperations(byte[] macKeyBytes) throws NoSuchAlgorithmException, Base64DecodingException, DecoderException {
        this.macKey = new SecretKeySpec(Hex.decodeHex(new String(macKeyBytes).toCharArray()), "HmacSHA1");
        this.macSignature = Mac.getInstance("HmacSHA1");
    }
    /**
     * Initialize HMAC-SHA1 key and cipher.
     *
     * @param macKeyBytes
     * @throws NoSuchAlgorithmException
     */
    public void initHmacOperations() throws NoSuchAlgorithmException, Base64DecodingException, DecoderException {
        byte[] macKeyBytes = new byte[40];
        this.randomize.nextBytes(macKeyBytes);
        this.macKey = new SecretKeySpec(macKeyBytes, "HmacSHA1");
        this.macSignature = Mac.getInstance("HmacSHA1");
    }

    /**
     * Initialize all the keys and the ciphers. All RSA keys MUST be Base64Decode first!
     *
     * @param privateKeyFragment1
     * @param privateKeyFragment2
     * @param publicKey
     * @param aesKey
     * @param macKey
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     */
    public void initAll(byte[] privateKeyFragment1, byte[] privateKeyFragment2, byte[] publicKey, byte[] aesKey, byte[] macKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, Base64DecodingException, UnsupportedEncodingException, DestroyFailedException, DecoderException {
        initRsaOperations(privateKeyFragment1, privateKeyFragment2, publicKey);
        initAesOperations(aesKey);
        initHmacOperations(macKey);
    }
    /**
     * Initialize all the keys and the ciphers. All RSA keys MUST be Base64Decode first!
     *
     * @param privateKeyFragment1
     * @param privateKeyFragment2
     * @param publicKey
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     */
    public String initAll(byte[] privateKeyFragment1, byte[] privateKeyFragment2, byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, Base64DecodingException, UnsupportedEncodingException, DestroyFailedException, DecoderException {
        initRsaOperations(privateKeyFragment1, privateKeyFragment2, publicKey);
        String preSharedKey = initAesOperations();
        initHmacOperations();
        return preSharedKey;
    }

    /**
     * Returns the ciphered HMAC-SHA1 Key.
     *
     * @return Ciphered HMAC-SHA1 key: to be Base64-encoded
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException
     */
    public byte[] getAesCipheredMacKey() throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, Base64DecodingException, NoSuchAlgorithmException {
        byte[] Iv = new byte[16];
        randomize.nextBytes(Iv);
        this.cipherParamSpec = new IvParameterSpec(Iv);
        this.aesCipher.init(Cipher.ENCRYPT_MODE, this.aesKey, cipherParamSpec);
        byte[] encryptedMacKey = this.aesCipher.doFinal(this.macKey.getEncoded());
        return concatByteArrays(Iv, encryptedMacKey);
    }

    /**
     * Encrypt the plain text value using AES-128-CBC/PKCS5Padding.
     * 
     * @param plainValue Value to encrypt.
     * @return Ciphered secret.
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     * @throws com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException 
     */
    public byte[] aesEncryptSecret(byte[] plainValue) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, Base64DecodingException, UnsupportedEncodingException, NoSuchAlgorithmException, DecoderException {
        byte[] Iv = new byte[16]; // MUST be random
        randomize.nextBytes(Iv);
        byte[] secretBytes = Hex.decodeHex(new String(plainValue).trim().toCharArray());
        this.cipherParamSpec = new IvParameterSpec(Iv);
        this.aesCipher.init(Cipher.ENCRYPT_MODE, this.aesKey, this.cipherParamSpec);
        byte[] encryptedSecret = this.aesCipher.doFinal(secretBytes);
        return concatByteArrays(Iv, encryptedSecret);
    }

    /**
     * Compute HMAC-SHA1 signature for the given ciphered secret.
     * 
     * @param cipheredSecret Ciphered secret to sign.
     * @return Signature.
     */
    public byte[] signCipheredSecret(byte[] cipheredSecret) throws Base64DecodingException, NoSuchAlgorithmException, InvalidKeyException {
        this.macSignature = Mac.getInstance("HmacSHA1");
        this.macSignature.init(new SecretKeySpec(this.macKey.getEncoded(), "HmacSHA1"));
        return this.macSignature.doFinal(cipheredSecret);
    }

    /**
     * Not implemented: not needed.
     * 
     * @param cipheredSecret
     * @return 
     */
    public byte[] aesDecryptCipheredSecret(byte[] cipheredSecret) {
        return null;
    }
    
    public byte[] convertRsaToAes(String rsaCipheredSecret) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, Base64DecodingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, DecoderException {
        return this.aesEncryptSecret(this.rsaDecryptSecret(rsaCipheredSecret));
    }

    /**
     * Recover the full private key as bytes array and initialize the PrivateKey.
     * Fragments MUST be Base64Decode first!
     * 
     * @param privateKeyFragment1 First half of the Private Key.
     * @param privateKeyFragment2 Second half of the Private Key.
     * 
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    private void initPrivateKey(byte[] privateKeyFragment1, byte[] privateKeyFragment2) throws NoSuchAlgorithmException, InvalidKeySpecException, Base64DecodingException {
        this.rsaPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(concatByteArrays(privateKeyFragment1, privateKeyFragment2)));
    }

    /**
     * Concat byte arrays.
     * 
     * @param piece1
     * @param piece2
     * @return 
     */
    private byte[] concatByteArrays(byte[] piece1, byte[] piece2) throws Base64DecodingException {
        byte[] result = new byte[piece1.length + piece2.length];
        System.arraycopy(piece1, 0, result, 0, piece1.length);
        System.arraycopy(piece2, 0, result, piece1.length, piece2.length);
        return result;
    }
    
    private byte[] rsaDecryptSecret(String cipheredSecret) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, Base64DecodingException {
        this.rsaDecryptCipher = Cipher.getInstance("RSA");
        this.rsaDecryptCipher.init(Cipher.DECRYPT_MODE, this.rsaPrivateKey);
        return this.rsaDecryptCipher.doFinal(Base64.decode(cipheredSecret));
    }

    public void initRsaOperations(byte[] fragment1, byte[] fragment2) throws NoSuchAlgorithmException, InvalidKeySpecException, Base64DecodingException, NoSuchPaddingException, InvalidKeyException {
        initPrivateKey(fragment1, fragment2);
        this.rsaDecryptCipher = Cipher.getInstance("RSA");
        this.rsaDecryptCipher.init(Cipher.DECRYPT_MODE, this.rsaPrivateKey);
    }
}