package probus.jam.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import probus.jam.network.packet.*;
import probus.jam.network.packet.command.ToggleKeyCommand;

public abstract class Packet {
	private static Map<Integer, Class<? extends Packet>> idToClassMap = new HashMap<Integer, Class<? extends Packet>>();
	private static Map<Class<? extends Packet>, Integer> classToIdMap = new HashMap<Class<? extends Packet>, Integer>();
	
	private static void map(int id, Class<? extends Packet> clazz) {
		if (idToClassMap.containsKey(id)) throw new IllegalArgumentException("Duplicate packet id :" + id);
		if (classToIdMap.containsKey(clazz)) throw new IllegalArgumentException("Duplicate packet class :" + clazz);
		idToClassMap.put(id, clazz);
		classToIdMap.put(clazz, id);
	}
	
	static {
		map(3, SyncSeedPacket.class);
		map(4, TurnPacket.class);
		map(5, ToggleKeyCommand.class);
	}
	
	public final int getId() {
		return classToIdMap.get(getClass());
	}
	
	public abstract void read(DataInputStream dis) throws IOException;
	public abstract void write(DataOutputStream dos) throws IOException;
	
	public static Packet readPacket(DataInputStream dis) throws IOException {
		int id = 0;
		Packet packet = null;
		
		try {
			id = dis.read();
			if (id == -1) return null;
			
			packet = getPacket(id);
			if (packet == null) throw new IOException("Bad packet id: " + id);
			
			packet.read(dis);
		} catch (EOFException e) {
			System.out.println("Reached end of stream");
			return null;
		}
		
		return packet;
	}

	private static Packet getPacket(int id) {
		try {
			Class<? extends Packet> clazz = idToClassMap.get(id);
			if (clazz == null) return null;
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Skipping packet with id: " + id);
			return null;
		}
	}

	public static void writePacket(Packet packet, DataOutputStream outputStream) throws IOException {
		outputStream.write(packet.getId());
		packet.write(outputStream);
	}

	public void beHandled(PacketListener packetListener) {
		packetListener.handle(this);
	}
}
