package darks.grid.utils;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BytesUtils
{
	
	private static final Logger log = LoggerFactory.getLogger(BytesUtils.class);

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

}
