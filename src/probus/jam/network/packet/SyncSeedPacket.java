package probus.jam.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import probus.jam.network.Packet;

public class SyncSeedPacket extends Packet {

	private long gameSeed;
	
	public SyncSeedPacket() {
	}

	public SyncSeedPacket(long gameSeed) {
		this.gameSeed = gameSeed;
	}


	@Override
	public void read(DataInputStream dis) throws IOException {
		gameSeed = dis.readLong();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeLong(gameSeed);
	}

	public long getGameSeed() {
		return gameSeed;
	}

}
