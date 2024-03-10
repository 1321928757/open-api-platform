package com.apiinterface.sdk.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.util.Arrays;
import java.util.HashMap;

public class SignUtil {
    /**
     * 生成签名
     *
     * @param map
     * @param secretKey
     * @return
     */
    public static String getSign(HashMap map, String secretKey) {
        String data = map.toString() + secretKey;
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
        byte[] signed = sign.sign(data);
        return Arrays.toString(signed);
    }


//    public static Boolean checkSign(HttpServletRequest request) {
//        String secretKey = null;
//        String accessKey = request.getHeader("accessKey");
//        String timestamp = request.getHeader("timestamp");
//        String body = request.getHeader("body");
//        // todo 根据accessKey在数据库查找是否存在，并拿到secretKey
//        if (Objects.equals(accessKey, "pengpeng")) {
//            secretKey = String.valueOf(887640);
//        }else {
//            return false;
//        }
//        //判断时间戳，使相同的请求头不能一直发送
//        long timestampNow = System.currentTimeMillis() / 1000;
//        if (timestampNow - Long.parseLong(timestamp) > 5) {
//            return false;
//        }
//        //验证签名
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("accessKey", accessKey);
//        hashMap.put("body", body);
//        hashMap.put("timestamp",timestamp);
//        String data = hashMap.toString() + secretKey;
//        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
//        byte[] signed = sign.sign(data);
//        return sign.verify(data.getBytes(), signed);
//    }
}
