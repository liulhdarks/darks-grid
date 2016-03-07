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

package darks.grid.beans;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import darks.grid.utils.StringUtils;

public class GridAddress implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -715203941820641100L;

    private String hostName;
    
    private int port;
    
    public GridAddress() 
    {
        
    }
    
    public GridAddress(InetSocketAddress addr) 
    {
        this(addr.getHostName(), addr.getPort());
    }

    public GridAddress(String hostName, int port) 
    {
        this.hostName = hostName;
        this.port = port;
    }
    
    public InetSocketAddress getSocketAddress()
    {
        return new InetSocketAddress(hostName, port);
    }
    
    public static GridAddress wrap(InetSocketAddress addr)
    {
        return new GridAddress(addr);
    }
    
    public static GridAddress wrap(SocketAddress addr)
    {
        return wrap((InetSocketAddress) addr);
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return StringUtils.stringBuffer(hostName, ':', port);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GridAddress other = (GridAddress) obj;
        if (hostName == null) {
            if (other.hostName != null)
                return false;
        } else if (!hostName.equals(other.hostName))
            return false;
        if (port != other.port)
            return false;
        return true;
    }
    
    
}
