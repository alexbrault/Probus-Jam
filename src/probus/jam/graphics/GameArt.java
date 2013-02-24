package probus.jam.graphics;

public class GameArt {
	
	public static final SpriteSheet bullet = new SpriteSheet("/BallsSheet.png", 16, 16);
	public static final SpriteSheet beam = new SpriteSheet("/laserSheet.png", 32, 32);
	
	public static final Image sandTile = new Image("/Tuile.png");
	public static final SpriteSheet player1 = new SpriteSheet("/Player1.png",32,48);
	public static final SpriteSheet player2 = new SpriteSheet("/Player2.png",32,48);
	
	// base
	public static final SpriteSheet baseP1 = new SpriteSheet("/Base.png", 192, 160);
	public static final SpriteSheet baseP2 = new SpriteSheet("/BaseEnnemi2.png", 192, 160);
	
	public static final SpriteSheet indicatorP1Green = new SpriteSheet("/GreenFlash.png", 32, 32);
	public static final SpriteSheet indicatorP1Blue = new SpriteSheet("/BlueFlash.png", 32, 32);
	public static final SpriteSheet indicatorP1Canon = new SpriteSheet("/RaimbowFlash.png", 32, 32);
	public static final SpriteSheet indicatorP1Red = new SpriteSheet("/RedFlash.png", 32, 32);
	public static final SpriteSheet indicatorP1Yellow = new SpriteSheet("/YelloFlash.png", 32, 32);
	
	public static final SpriteSheet indicatorP2Green = new SpriteSheet("/GreenFlashEnnemi.png", 32, 32);
	public static final SpriteSheet indicatorP2Blue = new SpriteSheet("/BlueFlashEnnemi.png", 32, 32);
	public static final SpriteSheet indicatorP2Canon = new SpriteSheet("/RaimbowFlashEnnemi.png", 32, 32);
	public static final SpriteSheet indicatorP2Red = new SpriteSheet("/RedFlashEnnemi.png", 32, 32);
	public static final SpriteSheet indicatorP2Yellow = new SpriteSheet("/YelloFlashEnnemi.png", 32, 32);
	
	// Mobs
	public static final SpriteSheet mommy = new SpriteSheet("/mommy.png", 48, 48);
	
	// Building
	public static final SpriteSheet mirror = new SpriteSheet("/MirorSheet.png", 32, 32);
	public static final Image generator = new Image("/BeamGenerator.png");
	public static final Image focusseur = new Image("/FocusBeamSheet.png");
	public static final SpriteSheet splitter = new SpriteSheet("/SpliterSheet.png",32,32);
	public static final SpriteSheet lens = new SpriteSheet("/lensSheet.png", 32, 32);
	public static final Image heart = new Image("/heart.png");
	
	//font
	public static final SpriteSheet font = new SpriteSheet("/gui/FontSprite.png",19,16);
	public static final SpriteSheet button = new SpriteSheet("/gui/Button.png", 256, 32);
	
	//GUI
	public static final Image guiBase = new Image("/gui/Base.png");
	public static final SpriteSheet lifeGauge = new SpriteSheet("/gui/LifeGauge.PNG", 64, 8);
	public static final Image emptyGauge = new Image("/gui/EmptyChargingGauge.png");
	public static final SpriteSheet filledGauge = new SpriteSheet("/gui/FilledChargingGauge.PNG", 41, 4);
	public static final Image metallicGauge = new Image("/gui/MetallicGauge.png");
	public static final SpriteSheet winCondition = new SpriteSheet("/gui/WinCondition.PNG", 90, 3);

	
	//Decoration
	public static final Image arbre = new Image("/Tree3.png");
	public static final Image arbre2 = new Image("/arbre2.png");
	public static final Image arbre3 = new Image("/Arbre.png");
	public static final Image rock1 = new Image("/Rock1.png");
	public static final Image rock2 = new Image("/Rock2.png");
	public static final Image rock3 = new Image("/Rock3.png");
	public static final Image rock4 = new Image("/Rock4.png");
	public static final Image wall = new Image("/wall.png");


	
	//Menu
	public static final Image titleScreen = new Image("/Title.jpg");
	public static final Image endScreen = new Image("/Fin.png");
}
