package darks.grid.utils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ParamsUtils
{
	
	private static final Logger log = LoggerFactory.getLogger(ParamsUtils.class);
	
	private ParamsUtils()
	{
		
	}
	
	/**
	 * 10.20.10.5:8008,10.12.13.5-15:[8000-8005],10.12.5-7.10-12:9000
	 * @param hosts
	 * @return
	 */
	public static Collection<InetSocketAddress> parseAddress(String hosts)
	{
		Set<InetSocketAddress> result = new LinkedHashSet<InetSocketAddress>();
		String[] ipsArray = hosts.split(",");
		for (String strIps : ipsArray)
		{
			String[] ips = strIps.split(":");
			String strIpList = ips[0];
			String strPortList = ips[1];
            List<String> ipList = new LinkedList<String>();
			if (!"localhost".equals(strIpList))
			{
	            String[] ipArg = strIpList.split("\\.");
	            buildIpList(ipList, ipArg, 0, null);
			}
			else
			{
			    ipList.add(NetworkUtils.getIpAddress());
			}
			List<Integer> portList = buildPortList(strPortList);
			if (ipList.isEmpty() || portList.isEmpty())
			{
				log.error("Invalid ip configure " + strIps);
				continue;
			}
			for (String ip : ipList)
			{
				for (Integer port : portList)
				{
					result.add(new InetSocketAddress(ip, port));
				}
			}
		}
		return result;
	}

	private static void buildIpList(List<String> ipList, String[] ipArg, int index, String prefix)
	{
		List<Integer> scanList = new ArrayList<>();
		String ipVal = ipArg[index++];
		int startVal = -1;
		int endVal = -1;
		if ("*".equals(ipVal))
		{
			startVal = 1;
			endVal = 254;
			scopeIntList(scanList, startVal, endVal);
		}
		else if (ipVal.indexOf("-") > 0)
		{
			String[] arg = ipVal.split("-");
			startVal = Integer.parseInt(arg[0]);
			endVal = Integer.parseInt(arg[1]);
			scopeIntList(scanList, startVal, endVal);
		}
		else if (ipVal.indexOf("/") > 0)
		{
			String[] args = ipVal.split("/");
			for (String val : args)
			{
				scanList.add(Integer.parseInt(val));
			}
		}
		else
		{
			startVal = Integer.parseInt(ipVal);
			endVal = startVal;
			scopeIntList(scanList, startVal, endVal);
		}
		for (Integer v : scanList)
		{
			String newPreffix = (prefix == null || "".equals(prefix)) ? String.valueOf(v) : prefix + "." + v;
			if (index < ipArg.length)
				buildIpList(ipList, ipArg, index, newPreffix);
			else
				ipList.add(newPreffix);
		}
	}
	
	private static void scopeIntList(List<Integer> list, int start, int end)
	{
		for (int i = start; i <= end; i++)
		{
			list.add(i);
		}
	}
	
	private static List<Integer> buildPortList(String strPortList)
	{
		List<Integer> result = new ArrayList<Integer>();
		strPortList = strPortList.replace("[", "").replace("]", "").trim();
		if ("".equals(strPortList))
			return result;
		if (strPortList.indexOf("-") > 0)
		{
			String[] arg = strPortList.split("-");
			int startVal = Integer.parseInt(arg[0]);
			int endVal = Integer.parseInt(arg[1]);
			for (int i = startVal; i <= endVal; i++)
			{
				result.add(i);
			}
		}
		else 
		{
			result.add(Integer.parseInt(strPortList));
		}
		return result;
	}
	
	public static void main(String[] args)
	{
		String hosts = "10.179.102/102.115/116:[12120-12121],10.176.122.92/93:[12120-12121]";
		Collection<InetSocketAddress> list = parseAddress(hosts);
		System.out.println(list.size());
		for (InetSocketAddress addr : list)
		{
			System.out.println(addr);
		}
	}
}
