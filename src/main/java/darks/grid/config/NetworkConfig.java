/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.config;

public class NetworkConfig
{
    
    private String bindHost;
    
    private int bindPort;
    
    private int bindPortRange = 10;
    
    private boolean tcpNodelay = true;
    
    private int tcpKeepIdle = 60000;
    
    private int tcpKeepInterval = 5000;
    
    private int tcpKeepCount = 3;
    
    private int tcpBacklog = 128;
    
    private boolean tcpKeepAlive = true;
    
    private boolean tcpReuseAddr = false;
    
    private int tcpSendBufferSize = 8 * 1024;
    
    private int tcpRecvBufferSize = 8 * 1024;
    
    private int recvTimeout = 10000;
    
    private int connectTimeout = 3000;
    
    private int serverWorkerThreadNumber = 6;
    
    private int serverBossThreadDelta = 2;
    
    private int clientWorkerThreadNumber = Runtime.getRuntime().availableProcessors() * 2;
    
    private int nodesExpireTime = 600000;
    
    private int localMsgHandlerNumber = Runtime.getRuntime().availableProcessors() * 2;
    
    private int sendFailRetry = 3;
    
    private int connectFailRetry = 3;
    
    private boolean cacheHistoryNodes = true;
    
    public NetworkConfig()
    {
        
    }

    public String getBindHost()
    {
        return bindHost;
    }

    public void setBindHost(String bindHost)
    {
        this.bindHost = bindHost;
    }

    public int getBindPort()
    {
        return bindPort;
    }

    public void setBindPort(int bindPort)
    {
        this.bindPort = bindPort;
    }

    public int getBindPortRange()
    {
        return bindPortRange;
    }

    public void setBindPortRange(int bindPortRange)
    {
        this.bindPortRange = bindPortRange;
    }

    public boolean isTcpNodelay()
    {
        return tcpNodelay;
    }

    public void setTcpNodelay(boolean tcpNodelay)
    {
        this.tcpNodelay = tcpNodelay;
    }

    public int getRecvTimeout()
    {
        return recvTimeout;
    }

    public void setRecvTimeout(int recvTimeout)
    {
        this.recvTimeout = recvTimeout;
    }

    public int getConnectTimeout()
    {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout)
    {
        this.connectTimeout = connectTimeout;
    }

    

    public int getServerWorkerThreadNumber()
    {
        return serverWorkerThreadNumber;
    }

    public void setServerWorkerThreadNumber(int serverWorkerThreadNumber)
    {
        this.serverWorkerThreadNumber = serverWorkerThreadNumber;
    }

    public int getServerBossThreadDelta()
    {
        return serverBossThreadDelta;
    }

    public void setServerBossThreadDelta(int serverBossThreadDelta)
    {
        this.serverBossThreadDelta = serverBossThreadDelta;
    }

    public int getClientWorkerThreadNumber()
    {
        return clientWorkerThreadNumber;
    }

    public void setClientWorkerThreadNumber(int clientWorkerThreadNumber)
    {
        this.clientWorkerThreadNumber = clientWorkerThreadNumber;
    }

    public int getNodesExpireTime()
	{
		return nodesExpireTime;
	}

	public void setNodesExpireTime(int nodesExpireTime)
	{
		this.nodesExpireTime = nodesExpireTime;
	}
	
	

	public int getTcpKeepIdle()
	{
		return tcpKeepIdle;
	}

	public void setTcpKeepIdle(int tcpKeepIdle)
	{
		this.tcpKeepIdle = tcpKeepIdle;
	}

	public int getTcpKeepInterval()
	{
		return tcpKeepInterval;
	}

	public void setTcpKeepInterval(int tcpKeepInterval)
	{
		this.tcpKeepInterval = tcpKeepInterval;
	}

	public int getTcpKeepCount()
	{
		return tcpKeepCount;
	}

	public void setTcpKeepCount(int tcpKeepCount)
	{
		this.tcpKeepCount = tcpKeepCount;
	}

	public int getTcpBacklog()
	{
		return tcpBacklog;
	}

	public void setTcpBacklog(int tcpBacklog)
	{
		this.tcpBacklog = tcpBacklog;
	}

	public boolean isTcpKeepAlive()
	{
		return tcpKeepAlive;
	}

	public void setTcpKeepAlive(boolean tcpKeepAlive)
	{
		this.tcpKeepAlive = tcpKeepAlive;
	}

	public boolean isTcpReuseAddr()
	{
		return tcpReuseAddr;
	}

	public void setTcpReuseAddr(boolean tcpReuseAddr)
	{
		this.tcpReuseAddr = tcpReuseAddr;
	}
	
	public int getTcpSendBufferSize()
	{
		return tcpSendBufferSize;
	}

	public void setTcpSendBufferSize(int tcpSendBufferSize)
	{
		this.tcpSendBufferSize = tcpSendBufferSize;
	}

	public int getTcpRecvBufferSize()
	{
		return tcpRecvBufferSize;
	}

	public void setTcpRecvBufferSize(int tcpRecvBufferSize)
	{
		this.tcpRecvBufferSize = tcpRecvBufferSize;
	}
	
	

	public int getLocalMsgHandlerNumber()
	{
		return localMsgHandlerNumber;
	}

	public void setLocalMsgHandlerNumber(int localMsgHandlerNumber)
	{
		this.localMsgHandlerNumber = localMsgHandlerNumber;
	}
	
	public int getSendFailRetry()
	{
		return sendFailRetry;
	}

	public void setSendFailRetry(int sendFailRetry)
	{
		this.sendFailRetry = sendFailRetry;
	}

	public boolean isCacheHistoryNodes()
	{
		return cacheHistoryNodes;
	}

	public void setCacheHistoryNodes(boolean cacheHistoryNodes)
	{
		this.cacheHistoryNodes = cacheHistoryNodes;
	}
	
	public int getConnectFailRetry()
	{
		return connectFailRetry;
	}

	public void setConnectFailRetry(int connectFailRetry)
	{
		this.connectFailRetry = connectFailRetry;
	}

	@Override
	public String toString()
	{
		return "NetworkConfig [bindHost=" + bindHost + ", bindPort=" + bindPort
				+ ", bindPortRange=" + bindPortRange + ", tcpNodelay=" + tcpNodelay
				+ ", tcpKeepIdle=" + tcpKeepIdle + ", tcpKeepInterval=" + tcpKeepInterval
				+ ", tcpKeepCount=" + tcpKeepCount + ", tcpBacklog=" + tcpBacklog
				+ ", tcpKeepAlive=" + tcpKeepAlive + ", tcpReuseAddr=" + tcpReuseAddr
				+ ", tcpSendBufferSize=" + tcpSendBufferSize + ", tcpRecvBufferSize="
				+ tcpRecvBufferSize + ", recvTimeout=" + recvTimeout + ", connectTimeout="
				+ connectTimeout + ", serverWorkerThreadNumber=" + serverWorkerThreadNumber
				+ ", serverBossThreadDelta=" + serverBossThreadDelta
				+ ", clientWorkerThreadNumber=" + clientWorkerThreadNumber + ", nodesExpireTime="
				+ nodesExpireTime + ", localMsgHandlerNumber=" + localMsgHandlerNumber
				+ ", sendFailRetry=" + sendFailRetry + ", connectFailRetry=" + connectFailRetry
				+ ", cacheHistoryNodes=" + cacheHistoryNodes + "]";
	}

}
