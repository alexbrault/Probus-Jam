package probus.jam.network;

import java.util.ArrayList;
import java.util.List;

import probus.jam.network.packet.NetworkCommand;

public class PlayerTurnCommands {

	private List<List<PlayerCommands>> playerCommands;

	public PlayerTurnCommands(int numPlayers) {
		playerCommands = new ArrayList<List<PlayerCommands>>();
		for (int i = 0; i < numPlayers; ++i) {
			playerCommands.add(new ArrayList<PlayerCommands>());
		}
	}

	public boolean isAllDone(int turnNumber) {
		for (List<PlayerCommands> commandList : playerCommands) {
			boolean found = false;
			for (PlayerCommands commands : commandList) {
				if (commands.turnNumber == turnNumber) {
					found  = true;
					break;
				}
			}
			
			if (!found) {
				return false;
			}
		}
		
		return true;
	}

	public List<NetworkCommand> popPlayerCommands(int playerId, int turnNumber) {
		for (PlayerCommands commands : playerCommands.get(playerId)) {
			if (commands.turnNumber == turnNumber) {
				playerCommands.get(playerId).remove(commands);
				return commands.commands;
			}
		}
		
		return null;
	}

	public void addPlayerCommands(int playerId, int turnNumber, List<NetworkCommand> commands) {
		playerCommands.get(playerId).add(new PlayerCommands(turnNumber, commands));
	}
	
	private class PlayerCommands {

		private int turnNumber;
		private List<NetworkCommand> commands;
		
		public PlayerCommands(int turnNumber, List<NetworkCommand> commands) {
			this.turnNumber = turnNumber;
			this.commands = commands;
		}		
	}
}
