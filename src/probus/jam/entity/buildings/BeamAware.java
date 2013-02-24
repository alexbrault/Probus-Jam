package probus.jam.entity.buildings;

import probus.jam.entity.Beam;

public interface BeamAware {

	public boolean beamCanPass();
	public void manageBeamCollision(Beam beam);
	public void beamHasStopped(Beam beam);
}
