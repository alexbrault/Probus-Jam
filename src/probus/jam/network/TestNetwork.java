package probus.jam.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import probus.jam.network.packet.Ping;
import probus.jam.network.packet.Pong;

public class TestNetwork implements PacketListener {
	private PacketLink link;
	
	public TestNetwork(PacketLink p, boolean isServer) {
		link = p;
		link.setPacketListener(this);
		if (isServer) {
			Ping p1 = new Ping();
			p1.value = "FUUUUUUUUUUUUUUUUUU!";
			link.sendPacket(p1);
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		TestNetwork test;
		if (args.length != 0) {
			ServerSocket serverSocket = new ServerSocket(2017);
			Socket s = serverSocket.accept();
			test = new TestNetwork(new PacketLink(s), true);
		}
		else {
			test = new TestNetwork(new ClientLink("localhost", 2017), false);
		}
		
		while (true) {
			test.link.tick();
			Thread.sleep(1000);
		}
	}

	@Override
	public void handle(Packet packet) {
		if (packet instanceof Ping) {
			System.out.println(((Ping)packet).value);
			Pong p1 = new Pong();
			p1.value = "Pong!";
			link.sendPacket(p1);
		} else if (packet instanceof Pong) {
			System.out.println(((Pong)packet).value);
			Ping p1 = new Ping();
			p1.value = "Ping!";
			link.sendPacket(p1);
		}
	}

}
