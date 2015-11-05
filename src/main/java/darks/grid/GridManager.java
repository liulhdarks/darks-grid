package darks.grid;

import darks.grid.config.GridConfiguration;

public interface GridManager
{

	public boolean initialize(GridConfiguration config);

	public void destroy();
}
