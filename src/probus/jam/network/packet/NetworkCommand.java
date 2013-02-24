package probus.jam.network.packet;

import probus.jam.network.Packet;
import probus.jam.network.PacketListener;

public abstract class NetworkCommand extends Packet {

	@Override
	public void beHandled(PacketListener packetListener) {
		throw new RuntimeException("Commands aren't supposed to be handled by the link");
	}
}
