package com.natami.deminator.back.responses;

import java.util.List;

public interface EntityGridUpdate {
	public EntityGrid getPrivateGrid();
	public List<EntityCell> getUpdatedCells();
}
