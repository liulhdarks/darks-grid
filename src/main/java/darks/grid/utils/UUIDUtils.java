package darks.grid.utils;

import java.util.UUID;

public final class UUIDUtils
{

	private UUIDUtils()
	{
		
	}
	
	public static String newUUID()
	{
		UUID uuid = UUID.randomUUID();
		long mostSigBits = uuid.getMostSignificantBits();
		long leastSigBits = uuid.getLeastSignificantBits();
        return StringUtils.stringBuffer(digits(mostSigBits >> 32, 8),
        		digits(mostSigBits >> 16, 4),
        		digits(mostSigBits, 4),
        		digits(leastSigBits >> 48, 4),
        		digits(leastSigBits, 12));
	}

    private static String digits(long val, int digits) 
    {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
