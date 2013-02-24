package probus.jam.gameengine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import probus.jam.entity.Player;
import probus.jam.entity.buildings.Base;
import probus.jam.entity.buildings.Dispenser;
import probus.jam.graphics.GameArt;
import probus.jam.gui.ButtonListener;
import probus.jam.gui.GuiButton;
import probus.jam.gui.GuiMenu;
import probus.jam.gui.menu.GameEndMenu;
import probus.jam.gui.menu.JoinMenu;
import probus.jam.gui.menu.TitleMenu;
import probus.jam.gui.menu.WaitForClientMenu;
import probus.jam.levels.GameLevel;
import probus.jam.network.ClientLink;
import probus.jam.network.CommandListener;
import probus.jam.network.Packet;
import probus.jam.network.PacketLink;
import probus.jam.network.PacketListener;
import probus.jam.network.TurnSynchroniser;
import probus.jam.network.packet.NetworkCommand;
import probus.jam.network.packet.SyncSeedPacket;
import probus.jam.network.packet.TurnPacket;
import probus.jam.network.packet.command.ToggleKeyCommand;

public class ProbusJam extends Canvas implements Runnable, KeyListener, PacketListener, CommandListener, ButtonListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 1024;
	public static final int GAME_HEIGHT = 768;
	
	public static final int MAGIC_WIDTH = -10;
	public static final int MAGIC_HEIGHT = -10;
	
	public static final int VIEWPORT_WIDTH = 1024;
	public static final int VIEWPORT_HEIGHT = 600;

	private boolean running;
	private Cursor emptyCursor;
	
	private static JFrame mainWindow;
	public static Camera camera = new Camera(0, 0);
	private GameLevel level;

	private Stack<GuiMenu> menuStack = new Stack<GuiMenu>();
	private boolean mouseMoved = false;
	private boolean mouseHidden = false;
	private int mouseHideTime = 0;
	
    public MouseButtons mouseButtons = new MouseButtons();
	
	private Keys keys = new Keys();
	private Keys[] synchedKeys = {
			new Keys(), new Keys()
	};
	private Player[] players = new Player[2];
	private Player player;
	
	
	private TurnSynchroniser synchroniser;
	private PacketLink packetLink;
	private ServerSocket serverSocket;
	private boolean isMultiplayer;
	private boolean isServer;
	private int localId;
	private Thread hostThread;
	private int createServerState = 0;
	
	public static void main(String[] args) throws InterruptedException
	{
		ProbusJam manager = new ProbusJam();
		//manager.setSize(GAME_WIDTH, GAME_HEIGHT);
		mainWindow = new JFrame();
		mainWindow.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(manager);
		
		//panel.setPreferredSize(new Dimension(GAME_WIDTH + MAGIC_WIDTH, GAME_HEIGHT + MAGIC_HEIGHT));
		mainWindow.setContentPane(panel);
		mainWindow.pack();
		mainWindow.setResizable(false);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		manager.start();
	}
	
	public ProbusJam()
	{

		setPreferredSize(new Dimension(GAME_WIDTH+MAGIC_WIDTH, GAME_HEIGHT+MAGIC_HEIGHT));
		setMinimumSize(new Dimension(GAME_WIDTH+MAGIC_WIDTH, GAME_HEIGHT+MAGIC_HEIGHT));
		setMaximumSize(new Dimension(GAME_WIDTH+MAGIC_WIDTH, GAME_HEIGHT+MAGIC_HEIGHT));
		addKeyListener(new InputHandler(keys));
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		addMenu(new TitleMenu());
	}
	
	@Override
	public void run()
	{        
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        int frames = 0;
        long lastTimer1 = System.currentTimeMillis();
        
        try {
        	init();
        } catch (Exception e) { 
        	e.printStackTrace();
        	return;
        }
        
        int toTick = 0;

        long lastRenderTime = System.nanoTime();
        int min = 999999999;
        int max = 0;
        
        double framerate = 60;
        int fps;
        
        double nsPerTick = 1000000000.0 / framerate;
        boolean shouldRender = false;
        
        while(running)
        {
            if (!this.hasFocus()) {
                keys.releaseAll();
            }
            
            if (keys.close.wasJustReleased()) {
            	mainWindow.dispose();
            	break;
            }
        	shouldRender = false;
            
        	// Check ticks to process
            while (unprocessed >= 1) {
                toTick++;
                unprocessed -= 1;
            }

            // Adjust ticks to process
            int tickCount = toTick;
            if (toTick > 0 && toTick < 3) {
                tickCount = 1;
            }
            if (toTick > 20) {
                toTick = 20;
            }

            for (int i = 0; i < tickCount; i++) {
                toTick--;
                tick();
                shouldRender = true;
            }

            // BufferStrategy creation
			BufferStrategy bs = getBufferStrategy();
            if (bs == null)
            {
                createBufferStrategy(3);
                continue;
            }
            
            // Render condition
            if(shouldRender)
            {
            	frames++;
            	
            	// Prepare frame to render
                Graphics g = bs.getDrawGraphics();
                
                Random lastRandom = TurnSynchroniser.synchedRandom;
                TurnSynchroniser.synchedRandom = null;
                
    			Render(g);
    			
    			TurnSynchroniser.synchedRandom = lastRandom;

    			// Check time passed between renders
                long renderTime = System.nanoTime();
                int timePassed = (int) (renderTime - lastRenderTime);
                if (timePassed < min) {
                    min = timePassed;
                }
                if (timePassed > max) {
                    max = timePassed;
                }
                lastRenderTime = renderTime;
            }
			
            // Check unprocessed ticks (ns used to compute frame / nsPerTick)
			long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;

            // Sleep to avoid using too much processing
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Render frame
            if (shouldRender) {
                if (bs != null) {
                    bs.show();
                }
            }
            
            // Print FPS
            if (System.currentTimeMillis() - lastTimer1 > 1000) {
                lastTimer1 += 1000;
                fps = frames;
                frames = 0;
                
                mainWindow.setTitle("FPS : " + fps);
            }
        }
	}
	
	private void tick()
	{
		if (level != null) {
			if (Base.bases[0].dispensers[2].isComplete()) {
				addMenu(new GameEndMenu(1));
				level = null;
				return;
			}
			if (Base.bases[1].dispensers[2].isComplete()) {
				addMenu(new GameEndMenu(2));
				level = null;
				return;
			}
		}
		if (packetLink != null) {
			packetLink.tick();
		}
		if (level != null) {
			if (synchroniser.preTurn()) {
				synchroniser.postTurn();
				
				for (int i = 0; i < keys.getAll().size(); ++i) {
					Keys.Key k= keys.getAll().get(i);
					if (k.willToggleOnNextTick()) {
						synchroniser.addCommand(new ToggleKeyCommand(i));
					}
				}
				
				keys.tick();
				for (Keys skeys : synchedKeys) {
					skeys.tick();
				}
				level.tick();
			}
		}
		
		mouseButtons.setPosition(getMousePosition());
		if (!menuStack.isEmpty()) {
			menuStack.peek().tick(mouseButtons);
		}
		if (mouseMoved) {
			mouseMoved = false;
			mouseHideTime = 0;
			if (mouseHidden) {
				mouseHidden = false;
				setCursor(null);
			}
		}
		if (mouseHideTime < 60) {
			mouseHideTime++;
			if (mouseHideTime == 60) {
				setCursor(emptyCursor);
				mouseHidden = true;
			}
		}
		mouseButtons.tick();
		
		if (createServerState == 1) {
			createServerState = 2;
			
			synchroniser = new TurnSynchroniser(this, packetLink, localId, 2);
			
			clearMenus();
			createLevel();
			
			synchroniser.start();
			packetLink.sendPacket(new SyncSeedPacket(TurnSynchroniser.synchedSeed));
			packetLink.setPacketListener(this);
		}
	}
		
	private synchronized void createLevel() {
		level = new GameLevel("/map.bmp");

		
		players[0] = new Player(level, synchedKeys[0], 608, 1024, localId == 0);
		players[0].init();
		level.addEntity(players[0]);
		
		if (isMultiplayer) {
			players[1] = new Player(level, synchedKeys[1], 1952, 1024, localId == 1);
			players[1].init();
			players[1].sheet = GameArt.player2;
			level.addEntity(players[1]); 
		}
		
		player = players[localId];
	}

	private void Render(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if (!menuStack.empty()) {
			menuStack.peek().render(g);
			return;
		}
		
		
		if (level != null) {
			g.setClip(0, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
			g.translate(camera.x, camera.y);
			
			level.render(g);
			g.setClip(null);
			
			g.translate(-camera.x, -camera.y);
			
			renderGUI(g);
		}
		
		//b.render(g);
		//Font.render(g, "(3 : "+player.getHealth(), 10, 700);
				
	}

	private void renderGUI(Graphics g) {
		GameArt.guiBase.Draw(g, 0, GAME_HEIGHT - GameArt.guiBase.getHeight());
		int activeColumn = localId + 1;
		int healthBars = (int) ((float)player.getHealth() / player.getMaxHealth() * 16);
		for (int i = 15; i >= 0; --i) {
			GameArt.lifeGauge.Draw(g, i <= healthBars ? activeColumn : 0, 15-i, 10, 738 - i*8);
		}
		
		int barsToDraw;
		
		barsToDraw = getBarsToDraw(0);		
		GameArt.emptyGauge.Draw(g, 370, 616);
		for (int i = 30 - barsToDraw; i < 30; ++i) {
			GameArt.filledGauge.Draw(g, 0, i, 370, 620+4*i, 0x80900000);
		}

		barsToDraw = getBarsToDraw(1);
		GameArt.emptyGauge.Draw(g, 430, 616);
		for (int i =  30 - barsToDraw; i < 30; ++i) {
			GameArt.filledGauge.Draw(g, 0, i, 430, 620+4*i, 0x80000050);
		}
		
		GameArt.metallicGauge.Draw(g, 490, 616);

		barsToDraw = getBarsToDraw(3);
		GameArt.emptyGauge.Draw(g, 550, 616);
		for (int i =  30 - barsToDraw; i < 30; ++i) {
			GameArt.filledGauge.Draw(g, 0, i, 550, 620+4*i, 0x80005000);
		}

		barsToDraw = getBarsToDraw(4);
		GameArt.emptyGauge.Draw(g, 610, 616);
		for (int i =  30 - barsToDraw; i < 30; ++i) {
			GameArt.filledGauge.Draw(g, 0, i, 610, 620+4*i, 0x80909000);
		}
		
		final int slices = 60;
		final int sliceSize = 180/slices;
		
		Dispenser d = Base.bases[0].dispensers[2];
		float pct = d.getPowerLevel() / (float)d.getPowerNeeded();
		barsToDraw = (int) (pct * slices);
		for (int i = 0; i < slices; ++i) {
			boolean shouldBeFilled = slices - i < barsToDraw;
			GameArt.winCondition.Draw(g, shouldBeFilled?2:0, i, 834, 581 + i * sliceSize);
		}
		
		d = Base.bases[1].dispensers[2];
		pct = d.getPowerLevel() / (float)d.getPowerNeeded();
		barsToDraw = (int) (pct * slices);
		for (int i = 0; i < slices; ++i) {
			boolean shouldBeFilled = slices - i < barsToDraw;
			GameArt.winCondition.Draw(g, shouldBeFilled?3:1, i, 924, 581 + i * sliceSize);
		}
	}

	private int getBarsToDraw(int dispenser) {
		Dispenser d;
		float pct;
		int barsToDraw;
		d = Base.bases[localId].dispensers[dispenser];
		pct = d.getPowerLevel() / (float)d.getPowerNeeded();
		if (pct >= 1) {
			barsToDraw = 30;
		} else {
			barsToDraw = (int) (pct * 24);
		}
		return barsToDraw;
	}
	
	public void start()
	{
        running = true;
		Thread thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
	}
	
	public void stop() {
		running = false;
	}
	
	public void keyTyped(KeyEvent e)
	{
		if (!menuStack.isEmpty()) {
			menuStack.peek().keyTyped(e);
		}
    }
	
	public void keyPressed(KeyEvent e)
	{
		if (!menuStack.isEmpty()) {
			menuStack.peek().keyPressed(e);
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		if (!menuStack.isEmpty()) {
			menuStack.peek().keyReleased(e);
		}		
	}
	
	private void clearMenus() {
		while (!menuStack.isEmpty()) {
			menuStack.pop();
		}
	}
	
	private void addMenu(GuiMenu menu) {
		menuStack.add(menu);
		menu.addButtonListener(this);
	}
	
	private void popMenu() {
		if (!menuStack.isEmpty()) {
			menuStack.pop();
		}
	}

	@Override
	public void handle(Packet packet) {
		if (packet instanceof SyncSeedPacket) {
			if (!isServer) {
				synchroniser.onSyncSeedPacket((SyncSeedPacket) packet);
				createLevel();
			}
		}
		else if (packet instanceof TurnPacket) {
			synchroniser.onTurnPacket((TurnPacket) packet);
		}
	}

	@Override
	public void handle(int playerId, NetworkCommand command) {
		if (command instanceof ToggleKeyCommand) {
			ToggleKeyCommand tkc = (ToggleKeyCommand) command;
			synchedKeys[playerId].getAll().get(tkc.getKey()).toggle();
		}
	}

	private void init() {
		emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty");
		setFocusTraversalKeysEnabled(false);
		requestFocus();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		mouseMoved = true;
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseMoved = true;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseButtons.releaseAll();		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mouseButtons.press(arg0.getButton());		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mouseButtons.release(arg0.getButton());
	}

    public void paint(Graphics g) {
    }

    public void update(Graphics g) {
    }

	@Override
	public void buttonPressed(GuiButton button) {
		switch (button.getId()) {
		case TitleMenu.EXIT_GAME_ID:
			System.exit(0);
			break;
		case TitleMenu.START_GAME_ID:
			clearMenus();
			isMultiplayer = false;
			localId = 0;
			synchroniser = new TurnSynchroniser(this, null, 0, 1);
			synchroniser.start();
			createLevel();
			break;
		case TitleMenu.HOST_GAME_ID:
			onHostGameButton();
			break;
		case TitleMenu.CANCEL_JOIN_ID:
			popMenu();
			if (hostThread != null) {
				hostThread.interrupt();
				hostThread = null;
			}
			break;
		case TitleMenu.JOIN_GAME_ID:
			addMenu(new JoinMenu());
			break;
		case TitleMenu.PERFORM_JOIN_ID:
			menuStack.clear();
			isMultiplayer = true;
			isServer = false;
			try {
				localId = 1;
				packetLink = new ClientLink(TitleMenu.serverIp, 2017);
				synchroniser = new TurnSynchroniser(this, packetLink, localId, 2);
				packetLink.setPacketListener(this);
			} catch (Exception e) {
				e.printStackTrace();
				addMenu(new TitleMenu());
			}
			break;
		case TitleMenu.RESTART_ID:
			clearMenus();
			addMenu(new TitleMenu());
			break;
		}		
	}

	private void onHostGameButton() {
		addMenu(new WaitForClientMenu());
		isMultiplayer = true;
		isServer = true;
		try {
			localId = 0;
			serverSocket = new ServerSocket(2017);
			serverSocket.setSoTimeout(1000);
			hostThread = new Thread() {
				public void run() {
					boolean fail = true;
					try {
						while (!isInterrupted()) {
							Socket socket = null;
							try {
								socket = serverSocket.accept();
							} catch (SocketTimeoutException e) {
							}
							
							if (socket == null) {
								continue;
							}
							fail = false;
							packetLink = new PacketLink(socket);
							createServerState = 1;
							break;
						}							
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (fail) {
						try {
							serverSocket.close();
						} catch (IOException e) {
						}
					}
				}
			};
			hostThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
