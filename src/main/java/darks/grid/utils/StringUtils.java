package darks.grid.utils;

public final class StringUtils
{

	private StringUtils()
	{
		
	}
	
	public static String stringBuffer(Object... strs)
	{
		StringBuilder buf = new StringBuilder();
		for (Object str : strs)
		{
			buf.append(str);
		}
		return buf.toString();
	}
	
	public static String percent(float v)
	{ 
	    v = v < 0.f ? 0.f : v * 100;
	    return String.format("%.2f", v) + "%";
	}
	
	public static String memorySize(long bytesSize)
	{
		if (bytesSize < 1024)
		{
			return stringBuffer(bytesSize, 'B');
		}
		else if (bytesSize < 1024 * 1024)
		{
			float kb = (float) bytesSize / 1024.f;
			return stringBuffer(String.format("%.2f", kb), "KB");
		}
		else
		{
			float mb = (float) bytesSize / 1024.f / 1024.f;
			return stringBuffer(String.format("%.2f", mb), "MB");
		}
	}
}
