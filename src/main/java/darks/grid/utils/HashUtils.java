package darks.grid.utils;

public class HashUtils
{
	/*RSHash*/
    public static long RSHash(String str)
    {
        int b = 378551;
        int a = 63689;
        long hash = 0;
        for(int i = 0; i < str.length(); i++)
        {
            hash = hash * a + str.charAt(i);
            a = a * b;
         }
         return hash;
     }
    /*JSHash*/
    public static long JSHash(String str)
    {
        long hash = 1315423911;
        for(int i = 0; i < str.length(); i++)
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        return hash;
    }
    /*PJWHash*/
    public static long PJWHash(String str)
    {
        long BitsInUnsignedInt = (long)(4 * 8);
        long ThreeQuarters = (long)((BitsInUnsignedInt * 3) / 4);
        long OneEighth = (long)(BitsInUnsignedInt / 8);
        long HighBits = (long)(0xFFFFFFFF)<<(BitsInUnsignedInt-OneEighth);
        long hash = 0;
        long test = 0;
        for(int i = 0; i < str.length(); i++)
        {
            hash = (hash << OneEighth) + str.charAt(i);
            if((test = hash & HighBits) != 0)
                hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
        }
        return hash;
    }
    /*ELFHash*/
    public static long ELFHash(String str)
    {
        long hash = 0;
        long x = 0;
        for(int i = 0; i < str.length(); i++)
        {
            hash = (hash << 4) + str.charAt(i);
            if(( x = hash & 0xF0000000L) != 0)
            hash ^= ( x >> 24);
            hash &= ~x;
        }
        return hash;
    }
    /*BKDRHash*/
    public static long BKDRHash(String str)
    {
        long seed = 131;//31131131313131131313etc..
        long hash = 0;
        for(int i = 0; i < str.length(); i++)
        hash = (hash * seed) + str.charAt(i);
        return hash;
    }
    /*SDBMHash*/
    public static long SDBMHash(String str)
    {
        long hash = 0;
        for(int i = 0; i < str.length(); i++)
        hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
        return hash;
    }
    /*DJBHash*/
    public static long DJBHash(String str)
    {
        long hash = 5381;
        for(int i = 0; i < str.length(); i++)
        hash = ((hash << 5) + hash) + str.charAt(i);
        return hash;
    }
    /*DEKHash*/
    public static long DEKHash(String str)
    {
        long hash = str.length();
        for(int i = 0; i < str.length(); i++)
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        return hash;
    }
    /*BPHash*/
    public static long BPHash(String str)
    {
        long hash=0;
        for(int i = 0;i < str.length(); i++)
        hash = hash << 7 ^ str.charAt(i);
        return hash;
    }
    /*FNVHash*/
    public static long FNVHash(String str)
    {
        long fnv_prime = 0x811C9DC5;
        long hash = 0;
        for(int i = 0; i < str.length(); i++) 
    {
        hash *= fnv_prime;
        hash ^= str.charAt(i);
    }
        return hash;
    }
    /*APHash*/
    public static long APHash(String str)
    {
        long hash = 0xAAAAAAAA;
        for(int i = 0; i < str.length(); i++)
        {
            if((i & 1) == 0)
                hash ^=((hash << 7) ^ str.charAt(i) ^ (hash >> 3));
            else
                hash ^= (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
        }
        return hash;
    }
    
    public static void main(String[] args)
    {
    	int[] vs = new int[]{32, 64, 11968, 61, 131, 137, 244};
    	System.out.println("RSHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + RSHash(String.valueOf(v)));
    	System.out.println("JSHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + JSHash(String.valueOf(v)));
    	System.out.println("PJWHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + PJWHash(String.valueOf(v)));
    	System.out.println("ELFHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + ELFHash(String.valueOf(v)));
    	System.out.println("BKDRHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + BKDRHash(String.valueOf(v)));
    	System.out.println("SDBMHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + SDBMHash(String.valueOf(v)));
    	System.out.println("DJBHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + DJBHash(String.valueOf(v)));
    	System.out.println("DEKHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + DEKHash(String.valueOf(v)));
    	System.out.println("BPHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + BPHash(String.valueOf(v)));
    	System.out.println("FNVHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + FNVHash(String.valueOf(v)));
    	System.out.println("APHash------------------------------");
    	for (int v : vs)
        	System.out.println(v + " " + APHash(String.valueOf(v)));
    }
}
