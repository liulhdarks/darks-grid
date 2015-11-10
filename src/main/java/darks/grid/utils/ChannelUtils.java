package darks.grid.utils;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public final class ChannelUtils
{
	
	private static final Logger log = LoggerFactory.getLogger(ChannelUtils.class);
	
	private ChannelUtils()
	{
		
	}

	public static String getChannelId(Channel channel)
	{
		try
		{
			InetSocketAddress ipAddr = (InetSocketAddress) channel.remoteAddress();
			String pid = MachineUtils.getProcessId();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (ipAddr != null)
			    baos.write(ipAddr.toString().getBytes());
			if (pid != null)
			    baos.write(pid.getBytes());
			byte[] bytes = baos.toByteArray();
			return BytesUtils.md5(bytes);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return null;
		}
	}
}
