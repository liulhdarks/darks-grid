package darks.grid.network;

import java.net.SocketAddress;

public interface GridSession
{
	
	public String getId();

	public boolean sendMessage(Object msg);
	
	public boolean sendSyncMessage(Object msg);
	
	public void close();
	
	public boolean isActive();
	
	public SocketAddress remoteAddress();
}
