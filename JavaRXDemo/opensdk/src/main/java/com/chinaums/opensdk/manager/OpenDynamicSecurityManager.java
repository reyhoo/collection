package com.chinaums.opensdk.manager;

import android.content.Context;

import com.chinaums.opensdk.util.ByteUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsMessageDigestUtils;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;

/**
 * 用于管理用户证书、密钥之类的数据
 */
public class OpenDynamicSecurityManager implements IOpenManager {

    /**
     * 生成公钥的算法
     */
    private static final String GENERATE_PUBLIC_KEY_ALGORITHM = "RSA";

    /**
     * 生成签名的密钥算法
     */
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * 默认实例
     */
    private static OpenDynamicSecurityManager instance;

    /**
     * 公钥
     */
    private PublicKey verifyKey;

    /**
     * 构造函数
     */
    private OpenDynamicSecurityManager() {

    }

    synchronized public static OpenDynamicSecurityManager getInstance() {
        if (instance == null) {
            instance = new OpenDynamicSecurityManager();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        try {
            initVerifyKey();
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 初始化密钥
     */
    private void initVerifyKey() throws Exception {
        String keyMod = OpenConfigManager
                .getProperty(OpenConfigManager.BCS_VERIFY_KEY_MOD);
        String keyExp = OpenConfigManager
                .getProperty(OpenConfigManager.BCS_VERIFY_KEY_EXP);
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(keyMod),
                new BigInteger(keyExp));
        KeyFactory keyFactory = KeyFactory
                .getInstance(GENERATE_PUBLIC_KEY_ALGORITHM);
        verifyKey = keyFactory.generatePublic(spec);
    }

    /**
     * 验证数据
     *
     * @param data 数组
     * @param sign 私钥签名
     */
    public boolean verify(byte[] data, String sign) throws Exception {
        String sha1Message = UmsMessageDigestUtils.encode(data);
        Signature s = Signature.getInstance(SIGNATURE_ALGORITHM);
        s.initVerify(verifyKey);
        s.update(sha1Message.getBytes());
        return s.verify(ByteUtils.hexString2ByteArray(sign));
    }

}
