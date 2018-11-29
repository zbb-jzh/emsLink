package com.future.link.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class WXUtils {
	
	/**
     * 排序方法
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 将字符串进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的内容
     */
    public static String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /** 
     * 签名 
     * 第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序）， 
     * 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。  
     * 特别注意以下重要规则：  
     * ◆ 参数名ASCII码从小到大排序（字典序）；  
     * ◆ 如果参数的值为空不参与签名；  
     * ◆ 参数名区分大小写；  
     * ◆ 验证调用返回或微信主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。  
     * ◆ 微信接口可能增加字段，验证签名时必须支持增加的扩展字段  
     * 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。 
     * key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 
     * @param map 
     * @return 
     * @throws UnsupportedEncodingException  
     */  
    public static String getSign(Map<String,Object> map, String key, String characterEncoding) throws UnsupportedEncodingException{  
        ArrayList<String> list = new ArrayList<String>();  
        for(Map.Entry<String,Object> entry:map.entrySet()){  
            //sign不参与验签  
            if(entry.getKey()=="sign"){  
                continue;  
            }  
            //参数为空不参与签名  
            if(entry.getValue()!=""){  
                list.add(entry.getKey() + "=" + entry.getValue());  
            }  
        }  
        int size = list.size();  
        String [] arrayToSort = list.toArray(new String[size]);  
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);  
        StringBuilder sb = new StringBuilder();  
        for(int i = 0; i < size; i ++) {  
            sb.append(arrayToSort[i]);  
            if(i!=size-1){  
                sb.append("&");  
            }  
        }  
        String result = sb.toString();  
        result += "&key=" + key;  
        //Util.log("Sign Before MD5:" + result);  
        result = MD5Util.MD5Encode(result, characterEncoding).toUpperCase();  
        //Util.log("Sign Result:" + result);  
        return result;  
    }  

}
