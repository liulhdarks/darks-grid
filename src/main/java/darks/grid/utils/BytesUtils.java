package darks.grid.utils;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BytesUtils
{
	
	private static final Logger log = LoggerFactory.getLogger(BytesUtils.class);

    private static final char[] HEXDUMP_TABLE = new char[256 * 4];

    static 
    {
        final char[] DIGITS = "0123456789abcdef".toCharArray();
        for (int i = 0; i < 256; i ++) {
            HEXDUMP_TABLE[ i << 1     ] = DIGITS[i >>> 4 & 0x0F];
            HEXDUMP_TABLE[(i << 1) + 1] = DIGITS[i       & 0x0F];
        }
    }
    
	private BytesUtils()
	{

	}
	
	public static String md5(String s)
	{
		char[] charArray = s.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
		return md5(byteArray);
	}
	
	public static String md5(byte[] bytes)
	{
		MessageDigest md5 = null;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return "";
		}
		byte[] md5Bytes = md5.digest(bytes);
		return hexToString(md5Bytes);
	}

	public static String hexToString(byte[] bytes)
	{
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			int val = ((int) bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	
	/**
     * Returns a <a href="http://en.wikipedia.org/wiki/Hex_dump">hex dump</a>
     * of the specified byte array.
     */
    public static String hexDump(byte[] array) {
        return hexDump(array, 0, array.length);
    }

    /**
     * Returns a <a href="http://en.wikipedia.org/wiki/Hex_dump">hex dump</a>
     * of the specified byte array's sub-region.
     */
    public static String hexDump(byte[] array, int fromIndex, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length: " + length);
        }
        if (length == 0) {
            return "";
        }

        int endIndex = fromIndex + length;
        char[] buf = new char[length << 1];

        int srcIdx = fromIndex;
        int dstIdx = 0;
        for (; srcIdx < endIndex; srcIdx ++, dstIdx += 2) {
            System.arraycopy(HEXDUMP_TABLE, (array[srcIdx] & 0xFF) << 1, buf, dstIdx, 2);
        }

        return new String(buf);
    }
    
    /** *//**  
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在  
     * 包含汉字的字符串时存在隐患，现调整如下：  
     * @param src 要截取的字符串  
     * @param start_idx 开始坐标（包括该坐标)  
     * @param end_idx   截止坐标（包括该坐标）  
     * @return  
     */  
    public static String subString(String src, int start_idx, int end_idx)
    {   
        byte[] b = src.getBytes();   
        StringBuilder buf = new StringBuilder();   
        for(int i = start_idx; i <= end_idx; i++)
        {   
        	buf.append((char) b[i]);
        }   
        return buf.toString();   
    }   
}
