package darks.grid.beans.meta;

import java.io.Serializable;

import darks.grid.utils.UUIDUtils;

public abstract class BaseMeta implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3863318610098206811L;
	
	private String metaId = UUIDUtils.newUUID();

	public BaseMeta()
	{
		
	}

	public String getMetaId()
	{
		return metaId;
	}

	public void setMetaId(String metaId)
	{
		this.metaId = metaId;
	}
	
	
}
