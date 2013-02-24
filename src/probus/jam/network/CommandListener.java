package probus.jam.network;

import probus.jam.network.packet.NetworkCommand;

public interface CommandListener {
	public void handle(int playerId, NetworkCommand command);
}
