package probus.jam.entity.buildings;

import probus.jam.entity.Entity;

public interface Carriable {

	public void carried(Entity carrier);
	public void dropped();
}
