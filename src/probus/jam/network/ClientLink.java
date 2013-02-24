package probus.jam.network;

import java.io.IOException;
import java.net.Socket;

public class ClientLink extends PacketLink {

	public ClientLink(String host, int port) throws IOException {
		super(new Socket(host, port));
	}

}
