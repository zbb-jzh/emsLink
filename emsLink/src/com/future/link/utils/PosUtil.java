package com.future.link.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class PosUtil {

    public static String makeOrderRequest(JSONObject json, String md5Key, String apiUrl) {
        Map<String, String> params = jsonToMap(json);
        params.put("sign", makeSign(md5Key, params));
        return apiUrl + "?" + buildUrlParametersStr(params);
    }


    public static String makeSign(String md5Key, Map<String, String> params) {
        String preStr = buildSignString(params); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String text = preStr + md5Key;
        return DigestUtils.md5Hex(getContentBytes(text)).toUpperCase();
    }

    public static boolean checkSign(String md5Key, Map<String, String> params) {
        String sign = params.get("sign");

        if (StringUtils.isBlank(sign)) {
            return false;
        }

        String signV = makeSign(md5Key, params);

        return StringUtils.equalsIgnoreCase(sign, signV);
    }

    // 获取HttpServletRequest里面的参数，并decode
    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> params2 = new HashMap<>();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            if (values.length > 0) {
                params2.put(key, values[0]);
            }
        }
        return params2;
    }

    public static String genMerOrderId(String orderCode,String srcId) {
        String rand = RandomStringUtils.randomNumeric(4);
        return srcId + rand+orderCode ;
    }

    public static String buildUrlParametersStr(Map<String, String> paramMap) {
        Map.Entry entry;
        StringBuffer buffer = new StringBuffer();

        for (Iterator iterator = paramMap.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();

            buffer.append(entry.getKey().toString()).append("=");
            try {
                if (entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())) {
                    buffer.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
            }

            buffer.append(iterator.hasNext() ? "&" : "");
        }

        return buffer.toString();
    }

    // 使json-lib来进行json到map的转换，fastjson有排序问题，不能用
    public static Map<String, String> jsonToMap(JSONObject json) {
        Map<String, String> map = new HashMap<>();
        for (Object key : json.keySet()) {
            String value = json.getString((String) key);
            map.put((String) key, value);
        }
        return map;
    }

    // 构建签名字符串
    public static String buildSignString(Map<String, String> params) {

        if (params == null || params.size() == 0) {
            return "";
        }

        List<String> keys = new ArrayList<>(params.size());

        for (String key : params.keySet()) {
            if ("sign".equals(key))
                continue;
            if (StringUtils.isEmpty(params.get(key)))
                continue;
            keys.add(key);
        }

        Collections.sort(keys);

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                buf.append(key + "=" + value);
            } else {
                buf.append(key + "=" + value + "&");
            }
        }

        return buf.toString();
    }

    // 根据编码类型获得签名内容byte[]
    public static byte[] getContentBytes(String content) {
        try {
            return content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误");
        }
    }

}
