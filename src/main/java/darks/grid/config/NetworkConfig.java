package darks.grid.config;

public class NetworkConfig
{
    
    private String bindHost;
    
    private int bindPort;
    
    private int bindPortRange = 10;
    
    private boolean tcpNodelay = true;
    
    private boolean keepAlive = true;
    
    private boolean reuseAddr = false;
    
    private int recvTimeout = 10000;
    
    private int connectTimeout = 3000;
    
    private int serverWorkerThreadNumber = 6;
    
    private int serverBossThreadDelta = 2;
    
    private int clientWorkerThreadNumber = Runtime.getRuntime().availableProcessors() * 2;
    
    
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

    public boolean isKeepAlive()
    {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive)
    {
        this.keepAlive = keepAlive;
    }

    public boolean isReuseAddr()
    {
        return reuseAddr;
    }

    public void setReuseAddr(boolean reuseAddr)
    {
        this.reuseAddr = reuseAddr;
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

    @Override
    public String toString()
    {
        return "NetworkConfig [bindHost=" + bindHost + ", bindPort=" + bindPort + ", bindPortRange=" + bindPortRange
            + ", tcpNodelay=" + tcpNodelay + ", keepAlive=" + keepAlive + ", reuseAddr=" + reuseAddr + ", recvTimeout="
            + recvTimeout + ", connectTimeout=" + connectTimeout + ", serverWorkerThreadNumber="
            + serverWorkerThreadNumber + ", serverBossThreadDelta=" + serverBossThreadDelta
            + ", clientWorkerThreadNumber=" + clientWorkerThreadNumber + "]";
    }

}
