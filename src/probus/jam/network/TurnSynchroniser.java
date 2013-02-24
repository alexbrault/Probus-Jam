package probus.jam.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import probus.jam.network.packet.NetworkCommand;
import probus.jam.network.packet.SyncSeedPacket;
import probus.jam.network.packet.TurnPacket;

public class TurnSynchroniser {

	public static Random synchedRandom = new Random();
	public static long synchedSeed;
	
	public static final int TURN_QUEUE_LENGTH = 3;
	public static final int TICKS_PER_TURN = 1;
	
	private int currentTurnLength = TICKS_PER_TURN;
	
	private List<NetworkCommand> nextTurnCommands = new ArrayList<NetworkCommand>();
	private PlayerTurnCommands playerCommands;
	private final int numPlayers;
	
	private TurnInfo[] turnInfo = new TurnInfo[TURN_QUEUE_LENGTH];
	private int commandSequence = TURN_QUEUE_LENGTH - 1;
	private int turnSequence = 0;
	private int currentTurnTickCount;
	
	private final PacketLink packetLink;
	private int localId;
	private final CommandListener commandListener;
	
	private boolean isStarted;
	
	public TurnSynchroniser(CommandListener commandListener, PacketLink link, int localId, int numPlayers) {
		this.commandListener = commandListener;
		this.packetLink = link;
		this.localId = localId;
		this.numPlayers = numPlayers;
		
		this.playerCommands = new PlayerTurnCommands(numPlayers);
		
		for (int i = 0; i < turnInfo.length; ++i) {
			turnInfo[i] = new TurnInfo(i);
		}
		
		turnInfo[0].isDone = true;
		turnInfo[1].isDone = true;
		
		synchedSeed = synchedRandom.nextLong();
		synchedRandom.setSeed(synchedSeed);
	}
	
	public int getLocalTick() {
		return turnSequence;
	}

	public synchronized boolean preTurn() {
		if (!isStarted) return false;
		
		int currentTurn = turnSequence % turnInfo.length;
		if (turnInfo[currentTurn].isDone || playerCommands.isAllDone(turnSequence)){
			turnInfo[currentTurn].isDone = true;
			
			if (!turnInfo[currentTurn].isCommandsPopped) {
				turnInfo[currentTurn].isCommandsPopped = true;
				
				for (int i = 0; i < numPlayers; ++i) {
					List<NetworkCommand> commands = playerCommands.popPlayerCommands(i, turnSequence);
					if (commands != null) {
						for (NetworkCommand command : commands) {
							commandListener.handle(i, command);
						}
					}
				}
				
				return true;
			}
		}
		return false;
	}

	public synchronized void postTurn() {
		currentTurnTickCount++;
		if (currentTurnTickCount >= currentTurnLength) {
			int currentTurn = turnSequence % turnInfo.length;
			turnInfo[currentTurn].clearDone();
			turnInfo[currentTurn].turnNumber += TURN_QUEUE_LENGTH;
			
			turnSequence++;
			currentTurnTickCount = 0;
			
			playerCommands.addPlayerCommands(localId, commandSequence, nextTurnCommands);
			sendLocalTurn(turnInfo[commandSequence % turnInfo.length]);
			commandSequence++;
			nextTurnCommands = null;
		}
	}
	
	public void start() {
		isStarted = true;
	}
	
	public synchronized void addCommand(NetworkCommand command) {
		if (nextTurnCommands == null) {
			nextTurnCommands = new ArrayList<NetworkCommand>();
		}
		
		nextTurnCommands.add(command);
	}
	
	public synchronized void onTurnPacket(TurnPacket packet) {
		playerCommands.addPlayerCommands(packet.getPlayerId(), packet.getTurnNumber(), packet.getPlayerCommandList());
	}
	
	public synchronized void onSyncSeedPacket(SyncSeedPacket packet) {
		start();
		synchedSeed = packet.getGameSeed();
		synchedRandom.setSeed(synchedSeed);
	}

	private void sendLocalTurn(TurnInfo turnInfo) {
		if (packetLink != null) {
			packetLink.sendPacket(turnInfo.getLocalPacket(nextTurnCommands));
		}
	}

	private class TurnInfo {

		public boolean isCommandsPopped;
		public boolean isDone;
		private int turnNumber;
		
		public TurnInfo(int turnNumber) {
			this.turnNumber = turnNumber;
		}
		
		public void clearDone() {
			isDone = false;
			isCommandsPopped = false;
		}

		public TurnPacket getLocalPacket(List<NetworkCommand> localPlayerCommands) {
			return new TurnPacket(localId, turnNumber, localPlayerCommands);
		}
	}
}
