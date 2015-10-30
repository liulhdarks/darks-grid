package darks.grid.beans;

import darks.grid.GridException;

public class GridNodeType
{
	
	public static final int TYPE_LOCAL = 1;
	private static final String TYPE_LOCAL_STR = "L";
	
	public static final int TYPE_REMOTE = 2;
	private static final String TYPE_REMOTE_STR = "R";
	
	public static String valueOf(int type)
	{
		switch (type)
		{
		case TYPE_LOCAL:
			return TYPE_LOCAL_STR;
		case TYPE_REMOTE:
			return TYPE_REMOTE_STR;
		default:
			throw new GridException("Invalid grid node type " + type);
		}
	}
}
