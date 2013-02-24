package probus.jam.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import probus.jam.network.Packet;

public class Ping extends Packet {
	public String value;
	
	@Override
	public void read(DataInputStream dis) throws IOException {
		value = dis.readUTF();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(value);
	}

}
