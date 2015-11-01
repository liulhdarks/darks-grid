package darks.grid.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NetworkUtils
{
	
	private static final Logger log = LoggerFactory.getLogger(NetworkUtils.class);

	private static String ipLocalAddress = null;

	private NetworkUtils()
	{

	}

	public static String getIpAddress()
	{
		if (ipLocalAddress == null)
		{
			Enumeration<NetworkInterface> netInterfaces = null;
			try
			{
				netInterfaces = NetworkInterface.getNetworkInterfaces();
			}
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
				netInterfaces = null;
			}
			while (ipLocalAddress == null && netInterfaces != null && netInterfaces.hasMoreElements())
			{
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ipLocalAddress == null && ips.hasMoreElements())
				{
					String ip = ips.nextElement().getHostAddress();
					if (ip.matches("^(10|172|100)\\.\\d{1,3}.\\d{1,3}.\\d{1,3}$"))
					{
						ipLocalAddress = ip;
					}
				}
			}
		}
		if (ipLocalAddress == null)
		{
			try
			{
				ipLocalAddress = InetAddress.getLocalHost().getHostAddress();
			}
			catch (UnknownHostException e)
			{
				log.error(e.getMessage(), e);
			}
		}
		if (ipLocalAddress == null)
		    ipLocalAddress = "127.0.0.1";
		return ipLocalAddress;
	}

	public static byte[] getMacBytes()
	{
		try
		{
			InetAddress ia = InetAddress.getLocalHost();
			return NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		}
		catch (Exception e)
		{
			return null;
		}
	}

}
