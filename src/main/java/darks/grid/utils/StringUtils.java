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
	
}
