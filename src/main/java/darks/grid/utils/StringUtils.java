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
	
}
