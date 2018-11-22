package org.spring.springboot.util;

public class MathUtil {
	
	//二进制数据转化为16进制字符串
	 public static String bytesToHexString(byte[] src){
	        StringBuilder stringBuilder = new StringBuilder();
	        if (src == null || src.length <= 0) {
	            return null;
	        }
	        for (int i = 0; i < src.length; i++) {
	            int v = src[i] & 0xFF;
	            String hv = Integer.toHexString(v);
	 
	            stringBuilder.append(i + ":");
	 
	            if (hv.length() < 2) {
	                stringBuilder.append(0);
	            }
	            stringBuilder.append(hv + ";");
	        }
	        return stringBuilder.toString();
	    }
	//16进制字符串转化为二进制数据：
	public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
 
    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

    /**
    * 得到十六进制数的静态方法
    * @param decimalNumber 十进制数
    * @return 四位十六进制数字符串
    */
    public static String getHexString(int decimalNumber) {
	    //将十进制数转为十六进制数
	    String hex = Integer.toHexString(decimalNumber);
	    //转为大写
	    //hex = hex.toUpperCase();
	    //加长到四位字符，用0补齐
	    while (hex.length() < 8) {
	    hex = "0" + hex;
	    }
	    return hex;
    }
    
    /**
    * 得到十六进制数的静态方法
    * @param decimalNumber 十进制数
    * @return 四位十六进制数字符串
    */
    public static String getHexString2(int decimalNumber) {
	    //将十进制数转为十六进制数
	    String hex = Integer.toHexString(decimalNumber);
	    //转为大写
	    hex = hex.toUpperCase();
	    //加长到四位字符，用0补齐
	    while (hex.length() < 2) {
	    hex = "0" + hex;
	    }
	    return hex;
    }
    

  

}
