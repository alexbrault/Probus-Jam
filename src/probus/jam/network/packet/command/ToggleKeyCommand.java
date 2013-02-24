package probus.jam.network.packet.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import probus.jam.network.packet.NetworkCommand;

public class ToggleKeyCommand extends NetworkCommand {
	private int key;
	
	public ToggleKeyCommand() {
	}
	
	public ToggleKeyCommand(int key) {
		this.key = key;
	}

	@Override
	public void read(DataInputStream dis) throws IOException {
		key = dis.readInt();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(key);
	}

	public int getKey() {
		return key;
	}
}
