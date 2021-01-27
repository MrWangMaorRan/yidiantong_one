package com.alibaba.cloudapi.sdk.signature;

import android.text.TextUtils;
import android.util.Base64;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by guikong on 17/10/28.
 */
public class HMacSHA1SignerFactory implements ISignerFactory {

    public static final String METHOD = "HmacSHA1";

    private static ISinger singer = null;

    @Override
    public ISinger getSigner() {

        if (null == singer) {
            singer = new HMacSHA1Signer();
        }

        return singer;
    }

    private static class HMacSHA1Signer implements ISinger {
        @Override
        public String sign(String strToSign, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {

            if (TextUtils.isEmpty(strToSign)) {
                throw new IllegalArgumentException("strToSign can not be empty");
            }

            if (TextUtils.isEmpty(secretKey)) {
                throw new IllegalArgumentException("secretKey can not be empty");
            }

            Mac hmacSha1 = Mac.getInstance(METHOD);
            byte[] keyBytes = secretKey.getBytes(SdkConstant.CLOUDAPI_ENCODING);
            hmacSha1.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, METHOD));

            byte[] signResult = hmacSha1.doFinal(strToSign.getBytes(SdkConstant.CLOUDAPI_ENCODING));
//            return byte2String(signResult);
            byte[] base64Bytes = Base64.encode(signResult, Base64.DEFAULT);
            return new String(base64Bytes, SdkConstant.CLOUDAPI_ENCODING);
        }
    }

    static String byte2String(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();


        if (null != data) {
            for (byte b : data) {
                stringBuilder.append(String.format("%02x", b));
            }
        }

        return stringBuilder.toString();
    }
}
