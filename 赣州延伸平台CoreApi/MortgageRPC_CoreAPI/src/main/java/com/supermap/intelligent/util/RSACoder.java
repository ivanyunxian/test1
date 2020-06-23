package com.supermap.intelligent.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;    
import java.security.KeyFactory;    
import java.security.KeyPair;    
import java.security.KeyPairGenerator;    
import java.security.PrivateKey;    
import java.security.PublicKey;    
import java.security.Signature;    
import java.security.interfaces.RSAPrivateKey;    
import java.security.interfaces.RSAPublicKey;    
import java.security.spec.PKCS8EncodedKeySpec;    
import java.security.spec.X509EncodedKeySpec;    
   
import java.util.HashMap;    
import java.util.Map;    
import javax.crypto.Cipher;

import com.supermap.realestate_gx.registration.util.Base64Util;    
   
/**   
* RSA安全编码组件   
*    
* @author 李堃  
* @version 1.0   
* @since 1.0   
*/   
public abstract class RSACoder extends Base64Util {    
    public static final String KEY_ALGORITHM = "RSA";    
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";    
    private static final String PUBLIC_KEY = "RSAPublicKey";    
    private static final String PRIVATE_KEY = "RSAPrivateKey"; 
    
//    private static int KeyLength=1024;//指定密匙长度（取值范围：512～2048）
    /** *//**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /** *//**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    //密钥对存放路径
//    private static String publicKeyFile="E:\\workspce3\\bdcinterface\\src\\main\\java\\com\\supermap\\externalInterface\\util\\RSA\\public_key.dat";
//    public static String privateKeyFile="E:\\workspce3\\bdcinterface\\src\\main\\java\\com\\supermap\\externalInterface\\util\\RSA\\private_key.dat";
//    static String publicKey;  
//    static String privateKey; 
    //公钥，对外接口发放时,优先使用公钥文件public_key.dat
    private static final String  publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjSQwxO7112B9ZGtLck093hAIOyX/LYbgT6Vvxq3HwKzeGnVlkdjLPtf2tNAnSRtnVIuUmhGon2Hynodj/6eMtgL1jHWDMDASwwQpkzLj0lbQL+mI19l6jAsK96bM1fNWrKqZkf/Z6Qo3e7zLSpuJ25t1w0aGEDUXoD5wWI3+x/QIDAQAB";
//    //私钥,任何情况下请勿泄露
    private static final String  privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKNJDDE7vXXYH1ka0tyTT3eEAg7Jf8thuBPpW/GrcfArN4adWWR2Ms+1/a00CdJG2dUi5SaEaifYfKeh2P/p4y2AvWMdYMwMBLDBCmTMuPSVtAv6YjX2XqMCwr3pszV81asqpmR/9npCjd7vMtKm4nbm3XDRoYQNRegPnBYjf7H9AgMBAAECgYBTekfqb7FHqwszwbvlmsY4wttOdgOIldfHDjm8Trs8XsvPXWhTLP6DJHahcwZGY4SAK4vdajrfThVEmkDXbNqAGnOf/e0xIPzD30hGWw5ucQM5g8S+Wo3t12PGD5tf/H/yf32tpgcdtAxqaWRcDtv3ntIK8JXbzzv4QdFDEvuxHQJBANjlKX2xrumeD7DKVtyd9uyyFtegdfqtdfEKTTJ0S8AMSsZfoXe+hEb6nULJmkLrekgT22AEhCqsOi868XfU2rsCQQDAuYOQ1jvHl7LP9e14Yr2LSb699ZspHRT5zuSJmtVKwhB/aib7ULntCrMGJQI9UXTBfmytFvLu/MwwRhap0+anAkEAt23vDgc0Fwz+1hP5K/FH/9uJJ+jjfhIu2aBNsyrZWVzL7EK6Kqvr8J9IWlBnbsr5CnYIpIaNRA2N05200xQnuwJBAI4YOajQS8MdVfl+mVfpdVb9SGVGcOAfURfeMyJBxoEYFJHc8mDZZDwhZ11gbAZJyIvhar8z7GnIecQd6RfN5IECQQDQQFRrtXPm8Np0pyb5oiLKDzbcMQJZ9BWvgNBlbGlfJ60wmy7LEMWGZa2HJ29VHqlHnY3HTTg/B+BBiWs1Ijvp";

    /**   
     * 用私钥对信息生成数字签名   x
     * @param data 加密数据   
     * @param privateKey   私钥   
     * @return   
     * @throws Exception   
     */   
    public static String sign(byte[] data) throws Exception {    
        // 解密由base64编码的私钥    s
//    	getPrivateKey(privateKeyFile);
        byte[] keyBytes = decode(privateKey);
        // 构造PKCS8EncodedKeySpec对象    
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);    
   
        // KEY_ALGORITHM 指定的加密算法    
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
   
        // 取私钥匙对象    
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);    
   
        // 用私钥对信息生成数字签名    
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);    
        signature.initSign(priKey);    
        signature.update(data);    
   
        return encode(signature.sign());    
    } 
    /**
     * 用私钥对数据进行数字签名
     * @作者 likun
     * @创建时间 2016年7月5日下午1:43:14
     * @param data 
     * @param privateKeyFile 私钥文件路径
     * @return
     * @throws Exception
     */
//    public static String sign(String data, String privateKeyFile) throws Exception {    
//    	FileInputStream fileInputStream=new FileInputStream(new File(privateKeyFile));
//    	ObjectInputStream inputStream =new ObjectInputStream(fileInputStream);
//        // 读取秘钥    
//    	PrivateKey privateKey=(PrivateKey) inputStream.readObject();
//        // KEY_ALGORITHM 指定的加密算法    
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
//        // 用私钥对信息生成数字签名    
//        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);    
//        signature.initSign(privateKey);    
//        signature.update(data.getBytes("utf-8"));    
//   
//        return encode(signature.sign());    
//    }
   
    /**   
     * 校验数字签名   
     * @param data 加密数据   
     * @param publicKey   公钥   
     * @param sign 数字签名   
     * @return 校验成功返回true 失败返回false   
     * @throws Exception   
     */   
    public static boolean verify(byte[] data, String sign)    
            throws Exception {    
   
        // 解密由base64编码的公钥    
//    	getpublicKey(publicKeyFile);
        byte[] keyBytes = decode(publicKey);  
   
        // 构造X509EncodedKeySpec对象    
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);    
   
        // KEY_ALGORITHM 指定的加密算法    
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
   
        // 取公钥匙对象    
        PublicKey pubKey = keyFactory.generatePublic(keySpec);    
   
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);    
        signature.initVerify(pubKey);    
        signature.update(data);    
   
        // 验证签名是否正常    
        return signature.verify(decode(sign));    
    }    
    /**
     * 验证数字签名
     * @作者 likun
     * @创建时间 2016年7月5日下午2:19:31
     * @param data 待验证的原始字符串
     * @param publicKeyFile 公钥文件路径
     * @param sign 待数字签名
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unused", "resource" })
	public static boolean verify(String data, String publicKeyFile, String sign)    
            throws Exception {    
    	FileInputStream fileInputStream=new FileInputStream(new File(publicKeyFile));
		ObjectInputStream inputStream =new ObjectInputStream(fileInputStream);
        // 读取公钥  
    	PublicKey publicKey=(PublicKey) inputStream.readObject();   
        // KEY_ALGORITHM 指定的加密算法    
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);    
        signature.initVerify(publicKey);    
        signature.update(data.getBytes("utf-8"));    
        // 验证签名是否正常    
        return signature.verify(decode(sign));    
    }   
   
    /**   
     * 解密   
     * 用私钥解密   
     * @param data   
     * @param key   
     * @return   
     * @throws Exception   
     */   
    public static byte[] decryptByPrivateKeyEx(byte[] data, String privatekey)    
            throws Exception {    
        // 对密钥解密    
        byte[] keyBytes = decode(privatekey);    
   
        // 取得私钥    
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);    
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);    
   
        // 对数据解密    
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());    
        cipher.init(Cipher.DECRYPT_MODE, privateKey);    
   
        return cipher.doFinal(data);    
    }    
    
    /**   
     * 解密   
     * 用私钥解密   
     * @param data  待解密数据
     * @param privatekeyFile  私钥文件路径 
     * @return   
     * @throws Exception   
     */   
//    public static byte[] decryptByPrivateKey(byte[] data, String privatekeyFile)    
//            throws Exception {    
//    	FileInputStream fileInputStream=new FileInputStream(new File(privatekeyFile));
//    	ObjectInputStream inputStream =new ObjectInputStream(fileInputStream);
//        // 读取秘钥    
//    	PrivateKey privateKey=(PrivateKey) inputStream.readObject();
//        // 取得私钥    
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
//        // 对数据解密    
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());    
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);    
////        return cipher.doFinal(data); 
//        //考虑加密数据过大，需要分块加密
//        int inputLen = data.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段解密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
//                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(data, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * MAX_DECRYPT_BLOCK;
//        }
//        byte[] decryptedData = out.toByteArray();
//        out.close();
//        return decryptedData;
//    }  
    /**   
     * 解密    替换上面方法
     * 用私钥解密   
     * @param data  待解密数据
     * @param privatekeyFile  私钥文件路径 
     * @return   
     * @throws Exception   
     */  
    public static byte[] decryptByPrivateKey(byte[] encryptedData)  
            throws Exception {
    	//赋值
//    	getPrivateKey(privateKeyFile);
        byte[] keyBytes = decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段解密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }  
    /**   
     * 解密   
     * 用私钥解密   
     * @param data  待解密数据
     * @param privatekeyFile  私钥文件路径 
     * @return   
     * @throws Exception   
     */   
//    public static byte[] decryptByPrivateKey(String data)    
//            throws Exception {    
//    	return decryptByPrivateKey(data.getBytes("utf-8"));
//    } 
   
   
    /**   
     * 解密  
     * 用公钥解密   
     * @param data   
     * @param key   
     * @return   
     * @throws Exception   
     */   
//    public static byte[] decryptByPublicKey(byte[] data) throws Exception {    
//    	FileInputStream fileInputStream=new FileInputStream(new File(publicKeyFile));
//    	ObjectInputStream inputStream =new ObjectInputStream(fileInputStream);
//        // 读取公钥  
//    	PublicKey publicKey=(PublicKey) inputStream.readObject(); 
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
//        // 对数据解密    
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());    
//        cipher.init(Cipher.DECRYPT_MODE, publicKey);    
////        return cipher.doFinal(data); 
//        //待解密数据过大，需要分块解密
//        int inputLen = data.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段解密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
//                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(data, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * MAX_DECRYPT_BLOCK;
//        }
//        byte[] decryptedData = out.toByteArray();
//        out.close();
//        return decryptedData;
//    }  
    /**   
     * 解密   替换上面方法
     * 用公钥解密   
     * @param data   
     * @param key   
     * @return   
     * @throws Exception   
     */  
    public static byte[] decryptByPublicKey(byte[] encryptedData)  
            throws Exception {  
//    	getpublicKey(publicKeyFile);
        byte[] keyBytes = decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, publicK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段解密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }
    /**   
     * 解密  
     * 用公钥解密   
     * @param data   
     * @param key   
     * @return   
     * @throws Exception   
     */   
    public static byte[] decryptByPublicKey(String data) throws Exception {    
        return   decryptByPublicKey(data.getBytes("utf-8")); 
    } 
   
    /**   
     * 加密  
     * 用公钥加密   
     * @param data   
     * @param key   
     * @return   
     * @throws Exception   
     */   
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)  
            throws Exception {  
    	
    	byte[] keyBytes = decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }      
    /**   
     * 加密  
     * 用公钥加密   
     * @param data   
     * @param key   
     * @return   
     * @throws Exception   
     */   
    public static byte[] encryptByPublicKeyFile(byte[] data)  
            throws Exception {
//    	getpublicKey(publicKeyFile);
        byte[] keyBytes = decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }
    /**   
     * 加密 
     * 用私钥加密   
     * @param data   
     * @param key   
     * @return   
     * @throws Exception   
     */   
    public static byte[] encryptByPrivateKeyEx(byte[] data, String privatekey) throws Exception {    
        // 对密钥解密    
        byte[] keyBytes = decode(privatekey);    
   
        // 取得私钥    
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);    
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);    
   
        // 对数据加密    
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());    
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);    
   
        return cipher.doFinal(data);    
    }    
    
    /**   
     * 加密 
     * 用私钥加密   
     * @param data   
     * @param privatekeyFile 私钥文件完整路径   
     * @return   
     * @throws Exception   
     */   
//    public static byte[] encryptByPrivateKey(byte[] data, String privatekeyFile) throws Exception { 
//    	FileInputStream fileInputStream=new FileInputStream(new File(privatekeyFile));
//    	ObjectInputStream inputStream =new ObjectInputStream(fileInputStream);
//        //读取私钥   
//    	PrivateKey privateKey=(PrivateKey)inputStream.readObject();
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);    
//        // 对数据加密    
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());    
//        cipher.init(Cipher.ENCRYPT_MODE, privateKey);    
////        return cipher.doFinal(data);  
//        //待加密数据过大，分块加密
//        int inputLen = data.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段加密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(data, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * MAX_ENCRYPT_BLOCK;
//        }
//        byte[] encryptedData = out.toByteArray();
//        out.close();
//        return encryptedData;
//    }  
    /**   
     * 加密   替换上面方法
     * 用私钥加密   
     * @param data   
     * @param privatekeyFile 私钥文件完整路径   
     * @return   
     * @throws Exception   
     */
    public static byte[] encryptByPrivateKey(byte[] data)  
            throws Exception {  
    	//赋值
//    	getPrivateKey(privateKeyFile);
        byte[] keyBytes = decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }
    /**   
     * 加密 
     * 用私钥加密   
     * @param data   
     * @param privatekeyFile 私钥文件完整路径   
     * @return   
     * @throws Exception   
     */   
    public static byte[] encryptByPrivateKey(String data) throws Exception { 
    	  return encryptByPrivateKey(data.getBytes("utf-8"));
    }  
   
    /**   
     * 取得私钥   
     * @param keyMap   
     * @return   
     * @throws Exception   
     */   
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {    
        Key key = (Key) keyMap.get(PRIVATE_KEY);    
   
        return encode(key.getEncoded());    
    }    
   
    /**   
     * 取得公钥   
     * @param keyMap   
     * @return   
     * @throws Exception   
     */   
    public static String getPublicKey(Map<String, Object> keyMap)    
            throws Exception {    
        Key key = (Key) keyMap.get(PUBLIC_KEY);    
   
        return encode(key.getEncoded());    
    }    
   
    /**   
     * 初始化密钥   
     * @return   
     * @throws Exception   
     */   
    public static Map<String, Object> initKey() throws Exception {    
        KeyPairGenerator keyPairGen = KeyPairGenerator    
                .getInstance(KEY_ALGORITHM);    
        keyPairGen.initialize(1024);    
   
        KeyPair keyPair = keyPairGen.generateKeyPair();    
   
        // 公钥    
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();    
   
        // 私钥    
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();    
   
        Map<String, Object> keyMap = new HashMap<String, Object>(2);    
   
        keyMap.put(PUBLIC_KEY, publicKey);    
        keyMap.put(PRIVATE_KEY, privateKey);    
        return keyMap;    
    }  
   
//    /**
//     * 生成公私密钥对，并保存成文件
//     * @作者 李堃
//     * @创建时间 2016年7月4日下午5:19:57
//     * @param directory 保存路径
//     */
//    public static void saveKeyRSA() throws NoSuchAlgorithmException,
//    FileNotFoundException, IOException {
//	  KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); //创建‘密匙对’生成器
//	  kpg.initialize(KeyLength); //指定密匙长度（取值范围：512～2048）
//	//  KeyPair kp = kpg.genKeyPair(); //生成‘密匙对’，其中包含着一个公匙和一个私匙的信息
//	  KeyPair kp = kpg.generateKeyPair();
//	  PublicKey public_key = kp.getPublic(); //获得公匙
//	  PrivateKey private_key = kp.getPrivate(); //获得私匙
//	  //保存公匙
//	  FileOutputStream public_file_out = new FileOutputStream( publicKeyFile);
//	  ObjectOutputStream public_object_out = new ObjectOutputStream(public_file_out);
//	  public_object_out.writeObject(public_key);
//	  //保存私匙
//	  FileOutputStream private_file_out = new FileOutputStream(privateKeyFile);
//	  ObjectOutputStream private_object_out = new ObjectOutputStream(private_file_out);
//	  private_object_out.writeObject(private_key);
//	 
//}

    /**
     * 对象转数组
     * @作者 think
     * @创建时间 2016年7月4日下午8:56:30
     * @param obj
     * @return
     */
    public static byte[] toByteArray (Object obj) {      
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(obj);        
            oos.flush();         
            bytes = bos.toByteArray ();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            ex.printStackTrace();   
        }      
        return bytes;    
    } 
//    public static void getPrivateKey(String file) throws Exception {  
//    	FileInputStream fileInputStream2=new FileInputStream(new File(URLDecoder.decode(file, "UTF-8")));
//		ObjectInputStream inputStream2 =new ObjectInputStream(fileInputStream2);
//    	PrivateKey Pri=(PrivateKey)inputStream2.readObject();
//    	Map<String, Object> keyMap = new HashMap<String, Object>(2);  
//        keyMap.put(PRIVATE_KEY, Pri);  
//        privateKey = getPrivateKey(keyMap); 
//    } 
//    public static void getpublicKey(String file) throws Exception {  
//    	FileInputStream fileInputStream=new FileInputStream(new File(URLDecoder.decode(file, "UTF-8")));
//    	ObjectInputStream inputStream =new ObjectInputStream(fileInputStream);
//		//读取公钥   
//		PublicKey pub=(PublicKey)inputStream.readObject();
//		Map<String, Object> keyMap = new HashMap<String, Object>(2);  
//        keyMap.put(PUBLIC_KEY, pub);  
//        publicKey = getPublicKey(keyMap);  
//     } 
    
    /**私钥解密且BASE64解码
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKeyBase64(String data) throws Exception{
		return new String(decryptByPrivateKey(decode(data.toString())), "UTF-8");
    }
    /**私钥加密且BASE64编码
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKeyBase64(String data) throws Exception{
		return encode(encryptByPrivateKey(data.toString()));
    }
    /**公钥解密且BASE64解码
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKeyBase64(String data) throws Exception{
		return new String(decryptByPublicKey(decode(data)),"UTF-8");
    }
    /**公钥加密且BASE64编码
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKeyBase64(String data) throws Exception{
		return encode(encryptByPublicKeyFile(data.toString().getBytes("UTF-8")));
    }
    
}  