package probus.jam.network;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketLink {
	private static final int SEND_BUFFER_SIZE = 1024 * 5;
	private Socket socket;
	
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private boolean isDisconnected = false;
	
	private List<Packet> incoming = Collections.synchronizedList(new ArrayList<Packet>());
	private List<Packet> outgoing = Collections.synchronizedList(new ArrayList<Packet>());
	
	private Thread readThread;
	private Thread writeThread;
	private PacketListener packetListener;
	
	public PacketLink(Socket socket) throws IOException {
		this.socket = socket;
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), SEND_BUFFER_SIZE));
		
		readThread = new Thread("Read thread") {
			public void run() {
				try {
					while (!isDisconnected) {
						while (readTick())
							;
						
						try {
							sleep(2L);
						} catch (InterruptedException e) {
						}
					}
				} catch (Exception e) {
				}
			}
		};
		
		writeThread = new Thread("write thread") {

			public void run() {
				try {
					while (!isDisconnected) {
						while (writeTick())
							;
						
						try {
							if (outputStream != null) outputStream.flush();
						} catch (IOException e) {
							e.printStackTrace();
							break;
						}
						
						try {
							sleep(2L);
						} catch (InterruptedException e) {
						}
					}
				} catch (Exception e) {
				}
			}
		};
		
		readThread.start();
		writeThread.start();
	}
	
	public void tick() {
		int maxPacketsReceived = 1000;
		while (!incoming.isEmpty() && maxPacketsReceived-- >= 0) {
			Packet p = incoming.remove(0);
			if (packetListener != null) {
				p.beHandled(packetListener);
			}
		}
	}
	
	public void sendPacket(Packet p) {
		if (isDisconnected) return;
		
		outgoing.add(p);
	}
	
	public void setPacketListener(PacketListener pl) {
		packetListener = pl;
	}
	
	private boolean readTick() {
		boolean didSomething = false;
		try {
			Packet p = Packet.readPacket(inputStream);
			
			if (p != null) {
				incoming.add(p);
				didSomething = true;
			}
		} catch (Exception e) {
			if (!isDisconnected) handleException(e);
			return false;
		}
		return didSomething;
	}
	
	private boolean writeTick() {
		boolean didSomething = false;
		try {
			if (!outgoing.isEmpty()) {
				Packet packet = outgoing.remove(0);
				Packet.writePacket(packet, outputStream);
				didSomething = true;
			}
		} catch (Exception e) {
			if (!isDisconnected) handleException(e);
			return false;
		}
		return didSomething;
	}

	private void handleException(Exception e) {
		e.printStackTrace();
		isDisconnected = true;
		try {
			socket.close();
		} catch (IOException ex) {
		}
	}
}
