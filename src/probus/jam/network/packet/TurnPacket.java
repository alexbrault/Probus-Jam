package probus.jam.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import probus.jam.network.Packet;

public class TurnPacket extends Packet {
	private int playerId;
	private int turnNumber;
	private List<NetworkCommand> commands;

	public TurnPacket() {
	}
	
	public TurnPacket(int localId, int turnNumber, List<NetworkCommand> localCommands) {
		playerId = localId;
		this.turnNumber = turnNumber;
		if (localCommands != null) {
			this.commands = new ArrayList<NetworkCommand>(localCommands);
		}
	}
	
	public int getPlayerId() {
		return playerId;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public List<NetworkCommand> getPlayerCommandList() {
		return commands;
	}

	@Override
	public void read(DataInputStream dis) throws IOException {
		playerId = dis.readInt();
		turnNumber = dis.readInt();
		int count = dis.readInt();
		if (count > 0) {
			commands = new ArrayList<NetworkCommand>();
			for (int i = 0; i < count; ++i) {
				NetworkCommand command = (NetworkCommand)Packet.readPacket(dis);
				if (command == null) {
					throw new IOException("Not enough commands in stream for player " + playerId + " at turn " + turnNumber);
				}
				commands.add(command);
			}
		}
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(playerId);
		dos.writeInt(turnNumber);
		if (commands != null) {
			dos.writeInt(commands.size());
			for (NetworkCommand n : commands) {
				Packet.writePacket(n, dos);
			}
		}
		else {
			dos.writeInt(0);
		}
	}

}
