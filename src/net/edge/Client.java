package net.edge;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.edge.activity.GameActivity;
import net.edge.activity.TitleActivity;
import net.edge.activity.panel.PanelHandler;
import net.edge.activity.panel.impl.ObjectCreationPanel.ConstructionObject;
import net.edge.activity.ui.UIComponent;
import net.edge.activity.ui.util.ConstitutionHandler;
import net.edge.activity.ui.util.OrbHandler;
import net.edge.activity.ui.util.TaskHandler;
import net.edge.cache.CacheArchive;
import net.edge.cache.CacheIndex;
import net.edge.cache.CacheUnpacker;
import net.edge.cache.impl.InterfaceLoader;
import net.edge.game.CollisionMap;
import net.edge.game.MapDecoder;
import net.edge.game.Scene;
import net.edge.game.WorldObjectSpawn;
import net.edge.game.tile.EntityUnit;
import net.edge.game.tile.GroundDecoration;
import net.edge.media.Rasterizer2D;
import net.edge.media.Rasterizer3D;
import net.edge.media.Viewport;
import net.edge.media.font.BitmapFont;
import net.edge.media.img.PaletteImage;
import net.edge.media.tex.Texture;
import net.edge.od.OnDemandEntry;
import net.edge.od.OnDemandFetcher;
import net.edge.sign.SignLink;
import net.edge.util.collect.LinkedDeque;
import net.edge.util.collect.SinglyLinkableEntry;
import net.edge.util.io.Buffer;
import net.edge.util.io.ISAACCipher;
import net.edge.util.string.StringEncoder;
import net.edge.activity.Activity;
import net.edge.activity.panel.impl.*;
import net.edge.activity.ui.UI;
import net.edge.activity.ui.util.SkillOrbHandler;
import net.edge.cache.unit.*;
import net.edge.game.model.*;
import net.edge.game.tile.Wall;
import net.edge.game.tile.WallDecoration;
import net.edge.media.GraphicalComponent;
import net.edge.media.img.BitmapImage;
import net.edge.util.MouseTracker;
import net.edge.util.WebToolkit;
import net.edge.util.string.StringUtils;
import img.ImagePacker;

import java.applet.AppletContext;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.zip.CRC32;

/*
 * TODO: Main interface scrolling with mouse scroll.
 */
public class Client extends ClientEngine {
	
	/*
	 * Npc information values.
	 */
	public int npcInfoId;
	public int[] npcDrops;
	public int[] npcDropsId;
	public int[] npcDropsMin;
	public int[] npcDropsMax;
	public int[] npcDropsChance;
	public boolean npcSug;
	public int npcSugMin;
	public int npcSugMax;
	
	/*
	 * Panel
	 */
	public boolean marketSearch;
	public boolean panelSearch;
	public String panelSearchInput;
	
	public String[] scoreNames = new String[20];
	public int[] scoreKills = new int[20];
	public int[] scoreDeaths = new int[20];
	public int[] killstreak = new int[20];

	/*
	 * Public static fields.
	 * TODO: Keep all static fields in this section.
	 */
	private static int pkt183Count;
	private static int pkt189Count;
	public static int pkt77Count;
	private static int pkt150Count;
	private static int pkt238Count;
	public static int pkt246Count;
	private static int pkt152Count;
	private static int pkt85Count;
	private static int pkt200Count;
	private static int pkt136Count;
	private static int pkt36Count;
	public static int[] BIT_MASK = new int[32];
	private static int[] EXP_FOR_LEVEL = new int[99];

	/*
	 * Final fields
	 * TODO: Keep all static fields in this section.
	 */
	//Static final fields.
	public static boolean firstRun = false;
	public static final String validUsernameOrPasswordChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
	public static final int[] anIntArray1204 = {9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486};
	public static final int[][] anIntArrayArray1003 = {{6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193}, {8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239}, {25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003}, {4626, 11146, 6439, 12, 4758, 10270}, {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}};
	private static final long serialVersionUID = -7411772866641918319L;
	// Non-static final fields.
	private final CRC32 crc;
	public final Interface chatWidget;
	public final PaletteImage[] modIcons;
	public final String[] chatAuthor;
	public final String[] chatMessage;
	public final boolean[] aBooleanArray876;
	public final int[] anIntArray873;
	public final int[] currentExp;
	public final int[] currentStats;
	private final int[] currentStatGoals;
	public final int[] anIntArray928;
	public final int[] chatType;
	public final int[] compassClipStarts;
	public final int[] anIntArray1030;
	public final int[] maxStats;
	public final int[] compassLineLengths;
	public final int[] anIntArray1203;
	public final int[] minimapLineLengths;
	public final int[] minimapLineStarts;
	public final int[] olderTabInterfaces = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
	public final int[] newerTabInterfaces = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
	public final String[] atPlayerActions;
	public final String[] chatMessagesSpoken;
	private final boolean aBoolean994;
	public final boolean[] atPlayerArray;
	private final int maxPlayers;
	private final int localPlayerIndex;
	public final int anInt975;
	public final int[] chatPositionsX;
	public final int[] chatPositionsY;
	public final int[] chatColorEffects;
	public final int[] chatAnimationEffects;
	public final int[] chatLoopCycles;
	public final int[] anIntArray1045;
	private final int[] anIntArray990;
	private final int[] anIntArray1065;
	private final int[] anIntArray1240;
	public final int[] chatOffsetsY;
	public final int[] chatOffsetsX;
	public final int[] chatColors = {0xffff00, 0xff0000, 0xff00, 0xffff, 0xff00ff, 0xffffff, 0x4080ff, 0xff8000};
	private final int[] anIntArray1177 = {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
	private final int[][][] constructRegionData;
	private final long[] ignoreListAsLongs;
	private boolean rollCharacterInInterface;

	/*
	 * Instance fields.
	 * TODO: Keep all instance fields in this section.
	 */
	public PanelHandler panelHandler = new PanelHandler();
	public TitleActivity titleActivity = new TitleActivity();
	public GameActivity gameActivity = new GameActivity();
	public GraphicalComponent mapGraphics;
	public GraphicalComponent gameGraphics;
	public GraphicalComponent updateGraphics;
	public GraphicalComponent chatGraphics;
	public GraphicalComponent inventoryGraphics;
	public Viewport chatAreaViewport;
	public Viewport tabAreaViewport;
	public Viewport gameAreaViewport;
	public Viewport fixedFullScreenViewport;
	public BitmapFont smallFont;
	public BitmapFont plainFont;
	public BitmapFont boldFont;
	public BitmapFont fancyFont;
	public BitmapFont smallHitFont;
	public BitmapFont bigHitFont;
	public CacheIndex[] cacheIdx;
	public CacheArchive titleArchive;
	public OnDemandFetcher onDemandRequester;
	public Session socketStream;
	public LinkedDeque[][][] sceneItems;
	public MouseTracker mouseDetection;
	public CollisionMap[] collisionMaps;
	public TaskHandler taskHandler;
	public BitmapImage mapFlag;
	public BitmapImage mapArrow;
	public BitmapImage scrollBarTop;
	public BitmapImage scrollBarDown;
	public BitmapImage mapDotItem;
	public BitmapImage mapDotNPC;
	public BitmapImage mapDotPlayer;
	public BitmapImage mapDotFriend;
	public BitmapImage mapDotTeam;
	public BitmapImage minimapImage;
	public BitmapImage[] chatButtons;
	public BitmapImage[] sideIcons;
	public BitmapImage[] hitMarks;
	public BitmapImage[] mapFunctions;
	public BitmapImage[] mapFunctionImage;
	public BitmapImage[] clickCross;
	public PaletteImage[] runes;
	public PaletteImage[] mapScenes;
	public NPC[] npcList;
	public Player localPlayer;
	public Player[] playerList;
	public Scene scene;
	public Buffer outBuffer;
	public String chatBoxStatement;
	public String chatInput;
	public String amountOrNameInput;
	public String outBoundInput;
	public String promptInputTitle;
	public String spellTooltip;
	public String promptInput;
	public String titleMessage;
	public String selectedItemName;
	public String localPassword;
	public String localUsername;
	public String[] menuItemName;
	public byte[][][] tiles;
	public boolean bankSearching;
	public String bankSearch;
	public boolean flagged;
	public boolean menuOpened;
	public boolean aBoolean954;
	public boolean cameraMoved;
	public boolean updateInventory;
	public boolean loggedIn;
	public boolean forcedCameraLocation;
	public boolean draggingItem;
	public boolean updateAllGraphics;
	public boolean messagePromptRaised;
	public boolean showTab = true;
	public boolean showChat = true;
	private boolean noclip = false;
	public boolean aBoolean972;
	public int hoveredChannelButton;
	public int selectedChannelButton;
	public int chatTypeView;
	public int clanChatMode;
	public int npcListSize;
	public int privateChatMode;
	public int openInterfaceID;
	public int cameraZoom = 0;
	public int cameraLocationX;
	public int cameraLocationZ;
	public int cameraLocationY;
	public int cameraRoll;
	public int cameraYaw;
	public int playerCount;
	public int friendsCount;
	public int crossX;
	public int crossY;
	public int crossIndex;
	public int crossType;
	public int cameraPlane;
	public int itemPressTimer;
	public int minimapOverlay;
	public int loadingStage;
	public int baseX;
	public int baseY;
	public int loginFailures;
	public int chatWidgetId;
	public int combatMultiwayMode;
	public int invDestSlot;
	public int lastActiveInvInterface;
	public int mapFunctionCount;
	public int activeInterfaceType;
	public int itemPressX;
	public int itemPressY;
	public int chatScrollPos;
	public int systemUpdateTimer;
	public int menuPos;
	public int spellSelected;
	private int spellId;
	private int autocastId;
	public int loopCycle;
	public int minimapZoom;
	public int mapVerticalRotation;
	public int cameraAngleX;
	public int invOverlayInterfaceID;
	public int splitPrivateChat;
	public int minimapAngle;
	public int chatContentHeight;
	public int scrollBarChangeAmount;
	public int inputDialogState;
	public int atInventoryLoopCycle;
	public int atInventoryInterfaceType;
	public int tradeMode;
	public int mouseButtonsToggle;
	public int walkX;
	public int walkY;
	public int forcedChatWidgetId;
	public int itemSelected;
	public int publicChatMode;
	public int drawCycle;
	public int fullscreenWidgetId;
	public int anInt849;
	public int hintType;
	public int cameraAngleAdjustX;
	public int cameraAngleOffsetXChange;
	public int anInt913;
	public int anInt933;
	public int anInt934;
	public int anInt935;
	public int anInt945;
	public int anInt984;
	public int anInt988;
	public int anInt1010;
	public int anInt1011;
	public int anInt1014;
	public int anInt1015;
	public int anInt1016;
	public int anInt1018;
	public int anInt1022;
	public int anInt1054;
	public int invWidgetId;
	public int invSrcSlot;
	public int cameraAdjustY;
	public int cameraOffsetYChange;
	public int anInt1142;
	public int minimapZoomChange;
	public int minimapAngleChange;
	public int NPCHintID;
	public int anInt1237;
	public int anInt1238;
	public int anInt1254;
	public int anInt1265;
	public int cameraAdjustX;
	public int cameraOffsetXChange;
	public int anInt1044;
	public int anInt1129;
	public int anInt1315;
	public int anInt1500;
	public int anInt1501;
	private int duelMode;
	private int nodeID = 10;
	public int[] friendsNodeIDs;
	public int[] npcEntryList;
	public int[] playerEntryList;
	public int[] variousSettings;
	public int[] mapFunctionX;
	public int[] mapFunctionY;
	public int[] menuItemCode;
	public int[] anIntArray851;
	public int[] anIntArray852;
	public int[] anIntArray853;
	public int[] anIntArray1190;
	public int[] anIntArray1191;
	public int[] currentShopInterfacePrices;
	public int[][][] sceneGroundZ;
	public long aLong1220;
	public long[] friendsListAsLongs;
	private Buffer aStream_834;
	private Buffer inBuffer;
	private Buffer outStream;
	private Buffer[] playerBuffer;
	private CacheUnpacker cacheHandler;
	private ISAACCipher encryption;
	private LinkedDeque aClass19_1013;
	private LinkedDeque aClass19_1056;
	private LinkedDeque aClass19_1179;
	private String reportAbuseInput;
	private String[] friendsList;
	public ObjectArrayList<ClanSettingPanel.ClanMember> clanMatesList;
	public String[] clanBansList;
	private byte[][] terrainData;
	private byte[][] objectData;
	private boolean windowUpdateReq = false;
	private boolean canMute;
	private boolean loadGeneratedMap;
	private boolean aBoolean1031;
	private boolean aBoolean1047;
	private boolean regionLoaded;
	private boolean inTutorialIsland;
	private boolean aBoolean1149;
	private int planeReq;
	private int daysSinceLastLogin;
	private int ignoreCount;
	public int localPrivilege;
	private int weight;
	public int menuX;
	public int menuY;
	public int menuWidth;
	public int menuHeight;
	public int spriteDrawX;
	public int spriteDrawY;
	private int pktSize;
	private int pktType;
	private int friendsListAction;
	public int energy;
	private int unreadMessages;
	private int daysSinceRecovChange;
	private int reportAbuseInterfaceID;
	private int membersInt;
	public int spellUsableOn;
	public int chatEffectsToggle;
	private int atInventoryInterface;
	private int atInventoryIndex;
	private int anInt839;
	private int anInt841;
	private int anInt842;
	private int anInt843;
	public int anInt886;
	private int anInt893;
	private int anInt900;
	public int anInt936;
	public int hintRegionX;
	public int hintRegionY;
	private int anInt995;
	private int anInt996;
	private int anInt997;
	private int anInt998;
	private int anInt999;
	public int anInt1026;
	private int anInt1036;
	private int anInt1037;
	public int anInt1039;
	private int anInt1046;
	public int anInt1048;
	public int regionX;
	public int regionY;
	private int anInt1169;
	private int forcedCameraTileX;
	private int forcedCameraTileY;
	private int forcedCameraPixelZ;
	private int anInt1101;
	private int anInt1102;
	private int anInt1137;
	public int anInt1186;
	public int anInt1187;
	private int anInt1193;
	private int anInt1251;
	private int anInt1257;
	public int anInt1264;
	private int anInt1268;
	private int anInt1269;
	private int anInt1283;
	private int anInt1284;
	private int anInt1285;
	private int unknownInt10;
	private int[] bigX;
	private int[] bigY;
	private int[] anIntArray840;
	private int[] anIntArray894;
	public int[] menuItemArg1;
	public int[] menuItemArg2;
	public int[] menuItemArg3;
	public long[] menuItemArg4;
	private int[] mapCoordinates;
	public int[] terrainDataIds;
	public int[] objectDataIds;
	private int[][] walkingPathCost;
	private int[][] walkingPathFrom;
	private int[][] anIntArrayArray929;
	private long aLong824;
	private long aLong953;
	private long aLong1215;
	private int fadeInStartCycle;
	private int fadeInEndCycle;
	private int hiddenEndCycle;
	private int fadeOutEndCycle;

	public Client() {
		UI.client = this;
		crc = new CRC32();
		cacheHandler = new CacheUnpacker(this);
		fullscreenWidgetId = -1;
		chatTypeView = Constants.MSG_ALL;
		clanChatMode = 0;
		hoveredChannelButton = -1;
		selectedChannelButton = 0;
		hitMarks = new BitmapImage[20];
		walkingPathCost = new int[104][104];
		friendsNodeIDs = new int[200];
		sceneItems = new LinkedDeque[4][104][104];
		aStream_834 = new Buffer(new byte[5000]);
		npcList = new NPC[16384];
		npcEntryList = new int[16384];
		anIntArray840 = new int[1000];
		outStream = Buffer.newPooledBuffer();
		openInterfaceID = -1;
		currentExp = new int[Constants.SKILL_AMOUNT];
		currentStatGoals = new int[Constants.SKILL_AMOUNT];
		anIntArray873 = new int[5];
		aBooleanArray876 = new boolean[5];
		reportAbuseInput = "";
		unknownInt10 = -1;
		menuOpened = false;
		chatInput = "";
		maxPlayers = 2048;
		localPlayerIndex = 2047;
		playerList = new Player[maxPlayers];
		playerEntryList = new int[maxPlayers];
		anIntArray894 = new int[maxPlayers];
		playerBuffer = new Buffer[maxPlayers];
		cameraAngleOffsetXChange = 1;
		walkingPathFrom = new int[104][104];
		currentStats = new int[Constants.SKILL_AMOUNT];
		ignoreListAsLongs = new long[100];
		anIntArray928 = new int[5];
		anIntArrayArray929 = new int[104][104];
		chatType = new int[500];
		chatAuthor = new String[500];
		chatMessage = new String[500];
		chatButtons = new BitmapImage[4];
		sideIcons = new BitmapImage[15];
		aBoolean954 = true;
		friendsListAsLongs = new long[200];
		spriteDrawX = -1;
		spriteDrawY = -1;
		compassClipStarts = new int[33];
		cacheIdx = new CacheIndex[Constants.CACHE_INDEX_COUNT];
		variousSettings = new int[3000];
		aBoolean972 = false;
		anInt975 = 50;
		chatPositionsX = new int[anInt975];
		chatPositionsY = new int[anInt975];
		chatOffsetsY = new int[anInt975];
		chatOffsetsX = new int[anInt975];
		chatColorEffects = new int[anInt975];
		chatAnimationEffects = new int[anInt975];
		chatLoopCycles = new int[anInt975];
		chatMessagesSpoken = new String[anInt975];
		planeReq = -1;
		anIntArray990 = new int[5];
		aBoolean994 = false;
		amountOrNameInput = "";
		outBoundInput = "";
		aClass19_1013 = new LinkedDeque();
		cameraMoved = false;
		anInt1018 = -1;
		anIntArray1030 = new int[5];
		aBoolean1031 = false;
		mapFunctions = new BitmapImage[100];
		chatWidgetId = -1;
		maxStats = new int[Constants.SKILL_AMOUNT];
		anIntArray1045 = new int[2000];
		aBoolean1047 = true;
		minimapLineStarts = new int[151];
		anInt1054 = -1;
		aClass19_1056 = new LinkedDeque();
		compassLineLengths = new int[33];
		chatWidget = new Interface();
		mapScenes = new PaletteImage[100];
		anIntArray1065 = new int[7];
		mapFunctionX = new int[1000];
		mapFunctionY = new int[1000];
		regionLoaded = false;
		friendsList = new String[200];
		clanMatesList = new ObjectArrayList<>();
		clanBansList = new String[0];
		inBuffer = Buffer.newPooledBuffer();
		menuItemArg1 = new int[500];
		menuItemArg2 = new int[500];
		menuItemArg3 = new int[500];
		menuItemArg4 = new long[500];
		menuItemCode = new int[500];
		updateInventory = false;
		promptInputTitle = "";
		atPlayerActions = new String[5];
		atPlayerArray = new boolean[5];
		constructRegionData = new int[4][13][13];
		cameraOffsetYChange = 2;
		mapFunctionImage = new BitmapImage[1000];
		inTutorialIsland = false;
		aBoolean1149 = false;
		clickCross = new BitmapImage[8];
		loggedIn = false;
		canMute = false;
		loadGeneratedMap = false;
		bankSearching = false;
		forcedCameraLocation = false;
		minimapZoomChange = 1;
		localUsername = "";
		localPassword = "";
		reportAbuseInterfaceID = -1;
		aClass19_1179 = new LinkedDeque();
		mapVerticalRotation = 128;
		invOverlayInterfaceID = -1;
		outBuffer = Buffer.newPooledBuffer();
		menuItemName = new String[500];
		anIntArray1203 = new int[5];
		minimapAngleChange = 2;
		chatContentHeight = 114;
		promptInput = "";
		modIcons = new PaletteImage[9];
		minimapLineLengths = new int[151];
		collisionMaps = new CollisionMap[4];
		anIntArray1240 = new int[100];
		draggingItem = false;
		currentShopInterfacePrices = new int[40];
		updateAllGraphics = false;
		messagePromptRaised = false;
		titleMessage = "";
		forcedChatWidgetId = -1;
		cameraOffsetXChange = 2;
		bigX = new int[4000];
		bigY = new int[4000];
	}

	@Override
	void initialize() {
		Activity.client = this;
		SkillOrbHandler.client = this;
		OrbHandler.client = this;
		Interface.client = this;
		Player.client = this;
		//new BETACacheDownload(this);
		cacheHandler.load();
	}

	/**
	 * This method is ran on the start up of the web applet.
	 */
	@Override
	public void init() {
		try {
			nodeID = 10;
			SignLink.storeId = 32;
			SignLink.startPriv(InetAddress.getLocalHost());
			setBackground(Color.BLACK);
			requestFocusInWindow();
			startApplet();
		} catch(final Exception exception) {
			System.out.println("Failed to launch the applet.");
		}
	}

	/**
	 * This method is ran on the start up of the desktop application.
	 */
	public static void main(String[] args) {
		try {
			if(args != null && args.length > 0) {
				boolean dev = Boolean.parseBoolean(args[0]);
				Constants.JAGGRAB_ENABLED = !dev;
				Constants.USER_HOME_FILE_STORE = !dev;
				TitleActivity.connection = dev ? 1 : 0;
			}
			SignLink.storeId = 32;
			SignLink.startPriv(InetAddress.getLocalHost());
			final Client client = new Client();
			client.nodeID = 10;
			client.startApplication();
		} catch(final Exception exception) {
			exception.printStackTrace();
			System.out.println("Failed to launch the application.");
		}
	}

	/**
	 * Run / Starts the run process.
	 */
	@Override
	public void run() {
		if(!CacheUnpacker.successfullyLoaded) {
			super.run();
		}
	}

	/**
	 * Start runnable thread with it's priority.
	 * @param thread
	 * @param priority
	 */
	@Override
	public void startThread(Runnable thread, int priority) {
		if(priority > 10) {
			priority = 10;
		}
		if(SignLink.mainApp != null) {
			SignLink.startThread(thread, priority); // Starts the main applications thread.
		} else {
			super.startThread(thread, priority); // Starts Game thread.
		}
	}

	/**
	 * [ProcessGameQuit] Cleans out all streams. Streams represent resources
	 * which you must always clean up explicitly, by calling the close method.
	 * Some java.io classes (apparently just the output classes) include a flush
	 * method. When a close method is called on a such a class, it automatically
	 * performs a flush. There is no need to explicitly call flush before
	 * calling close.
	 * <p/>
	 * One stream is chained to another by passing it to the constructor of some
	 * second stream. When this second stream is closed, then it automatically
	 * closes the original underlying stream as well.
	 * <p/>
	 * If multiple streams are chained together, then closing the one which was
	 * the last to be constructed, and is thus at the highest level of
	 * abstraction, will automatically close all the underlying streams. So, one
	 * only has to call close on one stream in order to close (and flush, if
	 * applicable) an entire series of related streams.
	 */
	@Override
	public void reset() {
		SignLink.reportError = false;
		try {
			//Socket stream clean-up.
			if(socketStream != null) {
				socketStream.close();
			}
			socketStream = null;
			//Mouse utils clean-up.
			if(mouseDetection != null) {
				mouseDetection.running = false;
			}
			//Close ondemand process.
			onDemandRequester.disable();
			onDemandRequester = null;
			//Puts everything on null to ensure secure shut down.
			mouseDetection = null;
			aStream_834 = null;
			outBuffer = null;
			outStream = null;
			inBuffer = null;
			mapCoordinates = null;
			terrainData = null;
			objectData = null;
			terrainDataIds = null;
			objectDataIds = null;
			sceneGroundZ = null;
			tiles = null;
			scene = null;
			collisionMaps = null;
			walkingPathFrom = null;
			walkingPathCost = null;
			bigX = null;
			bigY = null;
			inventoryGraphics = null;
			mapGraphics = null;
			gameGraphics = null;
			updateGraphics = null;
			chatGraphics = null;
			sideIcons = null;
			hitMarks = null;
			clickCross = null;
			hitMarks = null;
			mapDotItem = null;
			mapDotNPC = null;
			mapDotPlayer = null;
			mapDotFriend = null;
			mapDotTeam = null;
			mapScenes = null;
			mapFunctions = null;
			anIntArrayArray929 = null;
			playerList = null;
			playerEntryList = null;
			anIntArray894 = null;
			playerBuffer = null;
			anIntArray840 = null;
			npcList = null;
			npcEntryList = null;
			sceneItems = null;
			aClass19_1179 = null;
			aClass19_1013 = null;
			aClass19_1056 = null;
			menuItemArg2 = null;
			menuItemArg3 = null;
			menuItemArg4 = null;
			menuItemCode = null;
			menuItemArg1 = null;
			menuItemName = null;
			variousSettings = null;
			mapFunctionX = null;
			mapFunctionY = null;
			mapFunctionImage = null;
			minimapImage = null;
			friendsList = null;
			friendsListAsLongs = null;
			clanMatesList = null;
			clanBansList = null;
			friendsNodeIDs = null;
			titleActivity = null;
			gameActivity = null;
			panelHandler = null;
			//Handles nullLoaders to shutdown all data.
			cacheHandler.reset();
			LocationType.reset();
			NPCType.reset();
			ObjectType.reset();
			UnderlayFloorType.cache = null;
			Identikit.cache = null;
			Interface.cache = null;
			DeformSequence.cache = null;
			SpotAnimation.cache = null;
			SpotAnimation.modelcache = null;
			VariancePopulation.cache = null;
			Player.modelcache = null;
			Rasterizer3D.reset();
			Scene.reset();
			Model.reset();
			AnimationFrame.reset();
			MaterialType.reset();
			ImageCache.reset();
			Texture.reset();
			//Performs garbage collect.
			System.gc();
		} catch(final Exception _ex) {
			System.out.println("Unable to properly close all streams.");
		}
	}

	/**
	 * There may be a sitution when we need to execute a block of code several
	 * number of times, and is often referred to as a loop.
	 *
	 * Here is some few main client's loop process.
	 */
	/**
	 * Process game components handler loop.
	 */
	@Override
	public void process() {
		try {
			loopCycle++;
			final Activity activity = getActivity();
			if(activity != null) {
				activity.process();
			}
			processOnDemandQueue();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process game drawing loop.
	 */
	@Override
	public void update() {
		try {
			if(windowUpdateReq) {
				updateWindow();
				windowUpdateReq = false;
			} else {
				checkWindow();
			}
			final Activity activity = getActivity();
			if(activity != null) {
				activity.update();
			}
			scrollBarChangeAmount = 0;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private Activity getActivity() {
		if(!loggedIn) {
			return titleActivity;
		} else {
			return gameActivity;
		}
	}

	public void setMode(int type) {
		if(uiRenderer.getMode() != type) {
			uiRenderer.setMode(type);
			if(frame == null) {
				if(type == Constants.UI_FIXED) {
					windowWidth = 765;
					windowHeight = 503;
					graphics.fillRect(0, 0, windowWidth, windowHeight);
					//super.setWindowOrigin(width / 2 - 382, height / 2 - 251);
					super.setWindowOrigin(0, 0);
					updateWindow();
				} else if(type == Constants.UI_RESIZABLE) {
					int windowWidth = getWidth();
					int windowHeight = getHeight();
					graphics.fillRect(0, 0, windowWidth, windowHeight);
					super.setWindowOrigin(0, 0);
					updateWindow();
				}
				if(type == Constants.UI_FULLSCREEN) {
					uiRenderer.setMode(Constants.UI_RESIZABLE);
					pushMessage("Please use the desktop client to play in the fullscreen mode.", 0, "");
				}
			} else {
				int width = 765;
				int height = 503;
				if(type == Constants.UI_FIXED) {
					width = 765;
					height = 503;
				} else if(type == Constants.UI_RESIZABLE) {
					width = 775;
					height = 535;
				} else if(type == Constants.UI_FULLSCREEN) {
					final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					width = screenSize.width;
					height = screenSize.height;
				}
				super.rebuildFrame(width, height, type == 2, type == 1, type == 2);
				windowUpdateReq = true;
			}
		}
	}

	/**
	 * Get applet context.
	 * @return applet context
	 */
	@Override
	public AppletContext getAppletContext() {
		if(SignLink.mainApp != null) {
			return SignLink.mainApp.getAppletContext(); // Returns the main application context.
		} else {
			return super.getAppletContext(); // Returns the applet context.
		}
	}

	/**
	 * Gets the code base url.
	 * @return url
	 */
	@Override
	public URL getCodeBase() {
		try {
			return new URL((longToIp(TitleActivity.CONNECTIONS[TitleActivity.connection].getIp())) + ":" + 80); // Returns the server's URL.
		} catch(final Exception _ex) {
			//empty
		}
		return null; // Empty returnment statement.
	}

	/**
	 * Get game components.
	 * @return
	 */
	@Override
	public Component getComponent() {
		if(SignLink.mainApp != null) {
			return SignLink.mainApp; // Returns main application Components.
		}
		if(super.frame != null) {
			return super.frame; // Returns gameframe's components.
		} else {
			return this; // Client returnment statement.
		}
	}

	@Override
	public String getParameter(String s) {
		if(SignLink.mainApp != null) {
			return SignLink.mainApp.getParameter(s);
		} else {
			return super.getParameter(s);
		}
	}

	public static String valueToKOrM(int j) {
		if(j < 0x186a0) {
			return j + "";
		}
		if(j < 0x989680) {
			return j / 1000 + "K";
		} else {
			return j / 0xf4240 + "M";
		}
	}

	private static String valueToKOrMLong(int i) {
		//for(int k = s.length() - 3; k > 0; k -= 3) {
		//	s = s.substring(0, k) + "," + s.substring(k);
		//}
		
		if(i > 1000000) {
			return "million @whi@(" + (i/1000000) + ")";
		} else if(i > 1000) {
			return "K @whi@(" + (i/1000) + ")";
		} else {
			return i + "";
		}
	}

	public void checkWindow() {
		if(frame != null) {
			int dimension = frame.getContentWidth();
			if(windowWidth != dimension) {
				windowWidth = dimension;
				windowUpdateReq = true;
			}
			dimension = frame.getContentHeight();
			if(windowHeight != dimension) {
				windowHeight = dimension;
				windowUpdateReq = true;
			}
		} else {
			int dimension = super.getWidth();
			if(windowWidth != dimension) {
				windowWidth = dimension;
				windowUpdateReq = true;
			}
			dimension = super.getHeight();
			if(windowHeight != dimension) {
				windowHeight = dimension;
				windowUpdateReq = true;
			}
		}
	}

	public void updateWindow() {
		int gameAreaWidth;
		int gameAreaHeight;
		if(uiRenderer.isFixed()) {
			gameAreaWidth = 519;
			gameAreaHeight = 338;
		} else if(frame != null) {
			gameAreaWidth = windowWidth;
			gameAreaHeight = windowHeight;
		} else {
			gameAreaWidth = super.getWidth();
			gameAreaHeight = super.getHeight();
		}
		if(frame == null) {
			if(uiRenderer.isFixed()) {
				int offx = super.getWidth() / 2 - 765 / 2;
				if(offx < 0) {
					offx = 0;
				}
				int offy = (super.getHeight() / 2 - 503 / 2) / 2;
				if(offy < 0) {
					offy = 0;
				}
				super.setWindowOrigin(0, 0);
				super.graphics.setColor(Color.BLACK);
				super.graphics.fillRect(0, 0, super.getWidth(), super.getHeight());
				super.setWindowOrigin(offx, offy);
			} else {
				super.setWindowOrigin(0, 0);
			}
		}
		if(uiRenderer.isResizableOrFull()) {
			Rasterizer3D.viewport = new Viewport(0, 0, gameAreaWidth, gameAreaHeight, gameAreaWidth);
			updateAllGraphics = true;
		} else {
			Rasterizer3D.viewport = gameAreaViewport;
		}
		final int ai[] = new int[9];
		for(int i8 = 0; i8 < 9; i8++) {
			final int k8 = 128 + i8 * 32 + 15;
			final int l8 = 600 + k8 * 3;
			final int i9 = Rasterizer3D.angleSine[k8];
			ai[i8] = l8 * i9 >> 16;
		}
		Scene.setViewport(500, 800, gameAreaWidth, gameAreaHeight, ai);
		gameGraphics = new GraphicalComponent(gameAreaWidth, gameAreaHeight);
		super.graphics = frame.getGraphics();
	}

	private void updatePlayers(int i, Buffer buffer) {
		anInt839 = 0;
		anInt893 = 0;
		method117(buffer);
		method134(buffer);
		addPlayers(buffer, i);
		method49(buffer);
		for(int k = 0; k < anInt839; k++) {
			final int l = anIntArray840[k];
			if(playerList[l].anInt1537 != loopCycle) {
				playerList[l] = null;
			}
		}
		if(buffer.pos != i) {
			SignLink.reportError("Error packet size mismatch in getplayer pos:" + buffer.pos + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for(int i1 = 0; i1 < playerCount; i1++) {
			if(playerList[playerEntryList[i1]] == null) {
				SignLink.reportError(localUsername + " null entry in pl list - pos:" + i1 + " size:" + playerCount);
				throw new RuntimeException("eek");
			}
		}
	}

	private void updateNPCs(Buffer buffer, int psize) {
		anInt839 = 0;
		anInt893 = 0;
		updateNPCMovement(buffer);
		addNewNPC(psize, buffer);
		method86(buffer);
		for(int k = 0; k < anInt839; k++) {
			final int l = anIntArray840[k];
			if(npcList[l].anInt1537 != loopCycle) {
				npcList[l].type = null;
				npcList[l] = null;
			}
		}
		if(buffer.pos != psize) {
			SignLink.reportError(localUsername + " size mismatch in getnpcpos - pos:" + buffer.pos + " psize:" + psize);
			throw new RuntimeException("eek");
		}
		for(int pos = 0; pos < npcListSize; pos++) {
			if(npcList[npcEntryList[pos]] == null) {
				SignLink.reportError(localUsername + " null entry in npc list - pos:" + pos + " size:" + npcListSize);
				throw new RuntimeException("eek");
			}
		}

	}

	private void addFriend(long l) {
		try {
			if(l == 0L) {
				return;
			}
			if(friendsCount >= 100 && anInt1046 != 1) {
				pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			if(friendsCount >= 200) {
				pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			final String s = StringUtils.formatName(StringUtils.decryptName(l));
			for(int i = 0; i < friendsCount; i++) {
				if(friendsListAsLongs[i] == l) {
					pushMessage(s + " is already on your friend list", 0, "");
					return;
				}
			}
			for(int j = 0; j < ignoreCount; j++) {
				if(ignoreListAsLongs[j] == l) {
					pushMessage("Please remove " + s + " from your ignore list first", 0, "");
					return;
				}
			}

			if(s.equals(localPlayer.name)) {
				return;
			} else {
				friendsList[friendsCount] = s;
				friendsListAsLongs[friendsCount] = l;
				friendsNodeIDs[friendsCount] = 0;
				friendsCount++;
				outBuffer.putOpcode(188);
				outBuffer.putLong(l);
				return;
			}
		} catch(final RuntimeException runtimeexception) {
			SignLink.reportError("15283, " + (byte) 68 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void addIgnore(long l) {
		try {
			if(l == 0L) {
				return;
			}
			if(ignoreCount >= 100) {
				pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			final String s = StringUtils.formatName(StringUtils.decryptName(l));
			for(int j = 0; j < ignoreCount; j++) {
				if(ignoreListAsLongs[j] == l) {
					pushMessage(s + " is already on your ignore list", 0, "");
					return;
				}
			}
			for(int k = 0; k < friendsCount; k++) {
				if(friendsListAsLongs[k] == l) {
					pushMessage("Please unlink " + s + " from your friend list first", 0, "");
					return;
				}
			}

			ignoreListAsLongs[ignoreCount++] = l;
			outBuffer.putOpcode(133);
			outBuffer.putLong(l);
			return;
		} catch(final RuntimeException runtimeexception) {
			SignLink.reportError("45688, " + l + ", " + 4 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void delFriend(long l) {
		try {
			if(l == 0L) {
				return;
			}
			for(int i = 0; i < friendsCount; i++) {
				if(friendsListAsLongs[i] != l) {
					continue;
				}
				friendsCount--;
				for(int j = i; j < friendsCount; j++) {
					friendsList[j] = friendsList[j + 1];
					friendsNodeIDs[j] = friendsNodeIDs[j + 1];
					friendsListAsLongs[j] = friendsListAsLongs[j + 1];
				}
				outBuffer.putOpcode(215);
				outBuffer.putLong(l);
				break;
			}
		} catch(final RuntimeException runtimeexception) {
			SignLink.reportError("18622, " + false + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private void delIgnore(long l) {
		try {
			if(l == 0L) {
				return;
			}
			for(int j = 0; j < ignoreCount; j++) {
				if(ignoreListAsLongs[j] == l) {
					ignoreCount--;
					System.arraycopy(ignoreListAsLongs, j + 1, ignoreListAsLongs, j, ignoreCount - j);

					outBuffer.putOpcode(74);
					outBuffer.putLong(l);
					return;
				}
			}

			return;
		} catch(final RuntimeException runtimeexception) {
			SignLink.reportError("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}


	public void processOnDemandQueue() {
		do {
			OnDemandEntry entry;
			do {
				entry = onDemandRequester.nextRequest();
				if(entry == null) {
					return;
				}
				if(entry.type == 0 || entry.type == 6) {
					Model.method460(entry.data, entry.id, entry.type);
				}
				if(entry.type == 1 && entry.data != null) {
					AnimationFrame.decode(entry.data, entry.id);
				}
				if(entry.type == 3 && loadingStage == 1)  {
					for(int i = 0; i < terrainData.length; i++) {
						if(terrainDataIds[i] == entry.id) {
							terrainData[i] = entry.data;
							if(entry.data == null) {
								terrainDataIds[i] = -1;
							}
							break;
						}
						if(objectDataIds[i] != entry.id) {
							continue;
						}
						objectData[i] = entry.data;
						if(entry.data == null) {
							objectDataIds[i] = -1;
						}
						break;
					}
				}
				if(entry.type == 4 && entry.data != null) {
					Texture.decode(entry.data, entry.id);
				}
				if(entry.type == 5 && entry.data != null) {
					ImageCache.setImage(new BitmapImage(entry.data), entry.id);
				}
			} while(entry.type != 93 || !onDemandRequester.mapCached(entry.id));
			MapDecoder.decode(new Buffer(entry.data), onDemandRequester, onDemandRequester.mapOld(entry.id));
		} while(true);
	}

	public void drawWidget(Interface widget, int xPosition, int yPosition, int activeScroll, UIComponent component) {
		if(widget.type != 0 || widget.subId == null) {
			return;
		}
		if(widget.hoverTriggered && anInt1026 != widget.id && anInt1048 != widget.id && anInt1039 != widget.id) {
			return;
		}
		final int[] saveClip = Rasterizer2D.getClip();
		Rasterizer2D.setClip(xPosition, yPosition, xPosition + widget.width, yPosition + widget.height);
		for(int child = 0; child < widget.subId.length; child++) {
			int xPos = widget.subX[child] + xPosition;
			int yPos = widget.subY[child] + yPosition - activeScroll;
			final Interface childWidget = Interface.cache[widget.subId[child]];
			xPos += childWidget.offsetX;
			yPos += childWidget.offsetY;
			if(childWidget.contentType > 0) {
				drawFriendsListOrWelcomeScreen(childWidget);
			}
			if(childWidget.type == Constants.WIDGET_MAIN) {
				if(childWidget.scrollPos > childWidget.scrollMax - childWidget.height) {
					childWidget.scrollPos = childWidget.scrollMax - childWidget.height;
				}
				if(childWidget.scrollPos < 0) {
					childWidget.scrollPos = 0;
				}
				drawWidget(childWidget, xPos, yPos, childWidget.scrollPos, component);
				if(childWidget.scrollMax > childWidget.height) {
					if(uiRenderer.id == 459) {
						gameActivity.drawOldScrollbar(xPos + childWidget.width, yPos, childWidget.height, childWidget.scrollMax, childWidget.scrollPos);
					} else {
						gameActivity.drawScrollbar(xPos + childWidget.width, yPos, childWidget.height, childWidget.scrollMax, childWidget.scrollPos);
					}
				}
			} else if(childWidget.type != Constants.WIDGET_STRING_2) {
				if(childWidget.type == Constants.WIDGET_INVENTORY) {
					int item = 0;
					for(int i = 0; i < childWidget.height; i++) {
						for(int i2 = 0; i2 < childWidget.width; i2++) {
							int x = xPos + i2 * (32 + childWidget.invPadX);
							int y = yPos + i * (32 + childWidget.invPadY);
							if(item < 20) {
								x += childWidget.invIconX[item];
								y += childWidget.invIconY[item];
							}
							if(childWidget.invId[item] > 0) {
								int mouseDragOffsetX = 0;
								int mouseDragOffsetY = 0;
								final int itemID = childWidget.invId[item] - 1;
								if(x > Rasterizer2D.clipStartX - 32 && x < Rasterizer2D.clipEndX && y > Rasterizer2D.clipStartY - 32 && y < Rasterizer2D.clipEndY || activeInterfaceType != 0 && invSrcSlot == item) {
									int borderColor = 0;
									if(itemSelected == 1 && anInt1283 == item && anInt1284 == childWidget.id) {
										borderColor = 0xffffff;
									}
									final BitmapImage itemImage = ObjectType.getIcon(itemID, childWidget.invAmt[item], borderColor);
									if(itemImage != null) {
										if(activeInterfaceType != 0 && invSrcSlot == item && invWidgetId == childWidget.id) {
											mouseDragOffsetX = mouseX - itemPressX;
											mouseDragOffsetY = mouseY - itemPressY;
											if(mouseDragOffsetX < 5 && mouseDragOffsetX > -5) {
												mouseDragOffsetX = 0;
											}
											if(mouseDragOffsetY < 5 && mouseDragOffsetY > -5) {
												mouseDragOffsetY = 0;
											}
											if(itemPressTimer < 10) {
												mouseDragOffsetX = 0;
												mouseDragOffsetY = 0;
											}
											itemImage.drawImage(x + mouseDragOffsetX, y + mouseDragOffsetY, 128);
											if(y + mouseDragOffsetY < Rasterizer2D.clipStartY && widget.scrollPos > 0) {
												int i10 = anInt945 * (Rasterizer2D.clipStartY - y - mouseDragOffsetY) / 3;
												if(i10 > anInt945 * 10) {
													i10 = anInt945 * 10;
												}
												if(i10 > widget.scrollPos) {
													i10 = widget.scrollPos;
												}
												widget.scrollPos -= i10;
												itemPressY += i10;
											}
											if(y + mouseDragOffsetY + 32 > Rasterizer2D.clipEndY && widget.scrollPos < widget.scrollMax - widget.height) {
												int j10 = anInt945 * (y + mouseDragOffsetY + 32 - Rasterizer2D.clipEndY) / 3;
												if(j10 > anInt945 * 10) {
													j10 = anInt945 * 10;
												}
												if(j10 > widget.scrollMax - widget.height - widget.scrollPos) {
													j10 = widget.scrollMax - widget.height - widget.scrollPos;
												}
												widget.scrollPos += j10;
												itemPressY -= j10;
											}
										} else if(atInventoryInterfaceType != 0 && atInventoryIndex == item && atInventoryInterface == childWidget.id) {
											itemImage.drawImage(x, y);
										} else {
											itemImage.drawImage(x, y);
										}
										if(itemImage.imageOriginalWidth == 33 || childWidget.invAmt[item] != 1) {
											final int amount = childWidget.invAmt[item];
											String amt = valueToKOrM(amount);
											int color = 0xffff00;
											if(amt.endsWith("M")) {
												color = 0x00ff80;
											} else if(amt.endsWith("K")) {
												color = 0xffffff;
											}
											smallFont.drawLeftAlignedString(amt, x + 1 + mouseDragOffsetX, y + 10 + mouseDragOffsetY, 0);
											smallFont.drawLeftAlignedString(amt, x + mouseDragOffsetX, y + 9 + mouseDragOffsetY, color);
										}
									}
								}
							} else if(childWidget.invIcon != null && item < 20) {
								if(childWidget.invIcon[item] != 0 && childWidget.invIcon[item] != 0) {
									final BitmapImage itemImage = ImageCache.get(childWidget.invIcon[item]);
									if(itemImage != null) {
										itemImage.drawImage(x, y);
									}
								}
							}
							item++;
						}
					}
				} else if(childWidget.type == Constants.WIDGET_RECTANGLE) {
					boolean hover = false;
					if(anInt1039 == childWidget.id || anInt1048 == childWidget.id || anInt1026 == childWidget.id) {
						hover = true;
					}
					int color;
					if(interfaceIsSelected(childWidget)) {
						color = childWidget.colorAlt;
						if(hover && childWidget.hoverColorAlt != 0) {
							color = childWidget.hoverColorAlt;
						}
					} else {
						color = childWidget.color;
						if(hover && childWidget.hoverColor != 0) {
							color = childWidget.hoverColor;
						}
					}
					if(childWidget.alpha == 0) {
						if(childWidget.rectFilled) {
							Rasterizer2D.fillRectangle(xPos, yPos, childWidget.width, childWidget.height, color);
						} else {
							Rasterizer2D.drawRectangle(xPos, yPos, childWidget.width, childWidget.height, color);
						}
					} else if(childWidget.rectFilled) {
						Rasterizer2D.fillRectangle(xPos, yPos, childWidget.width, childWidget.height, color, 256 - (childWidget.alpha & 0xff));
					} else {
						Rasterizer2D.drawRectangle(xPos, yPos, childWidget.width, childWidget.height, color, 256 - (childWidget.alpha & 0xff));
					}
				} else if(childWidget.type == Constants.WIDGET_STRING) {
					final BitmapFont font = Interface.fonts[childWidget.fontId];
					String message = childWidget.text;
					boolean hover = false;
					if(anInt1039 == childWidget.id || anInt1048 == childWidget.id || anInt1026 == childWidget.id) {
						hover = true;
					}
					int color;
					if(interfaceIsSelected(childWidget)) {
						color = childWidget.colorAlt;
						if(hover && childWidget.hoverColorAlt != 0) {
							color = childWidget.hoverColorAlt;
						}
						if(childWidget.textAlt.length() > 0) {
							message = childWidget.textAlt;
						}
					} else {
						color = childWidget.color;
						if(hover && childWidget.hoverColor != 0) {
							color = childWidget.hoverColor;
						}
					}
					if(childWidget.actionType == 6 && aBoolean1149) {
						message = "Please wait...";
						color = childWidget.color;
					}
					if(component == UIComponent.CHAT) {
						if(color == 0xffff00)
							color = 255;
						if(color == 49152)
							color = 0xffffff;
						if(uiRenderer.getId() == 1) {
							if(color == 0x000000)
								color = 0xecdec6;
							if(color == 0xffff00)
								color = 0x000000;
							if(color == 0x800000)
								color = 0xc5a44c;
							if(color == 0x0000ff)
								color = 0xff9100;
						}
					}
					for(int l6 = yPos + font.lineHeight; message.length() > 0; l6 += font.lineHeight) {
						if(message.contains("%")) {
							do {
								final int k7 = message.indexOf("%1");
								if(k7 == -1) {
									break;
								}
								message = message.substring(0, k7) + interfaceIntToString(extractInterfaceValues(childWidget, 0)) + message.substring(k7 + 2);
							} while(true);
							do {
								final int l7 = message.indexOf("%2");
								if(l7 == -1) {
									break;
								}
								message = message.substring(0, l7) + interfaceIntToString(extractInterfaceValues(childWidget, 1)) + message.substring(l7 + 2);
							} while(true);
							do {
								final int i8 = message.indexOf("%3");
								if(i8 == -1) {
									break;
								}
								message = message.substring(0, i8) + interfaceIntToString(extractInterfaceValues(childWidget, 2)) + message.substring(i8 + 2);
							} while(true);
							do {
								final int j8 = message.indexOf("%4");
								if(j8 == -1) {
									break;
								}
								message = message.substring(0, j8) + interfaceIntToString(extractInterfaceValues(childWidget, 3)) + message.substring(j8 + 2);
							} while(true);
							do {
								final int k8 = message.indexOf("%5");
								if(k8 == -1) {
									break;
								}
								message = message.substring(0, k8) + interfaceIntToString(extractInterfaceValues(childWidget, 4)) + message.substring(k8 + 2);
							} while(true);
							do {
								int index = message.indexOf("%6");

								if(index == -1) {
									break;
								}

								message = message.substring(0, index) + interfaceIntToString(extractInterfaceValues(widget, 5)) + message.substring(index + 2);
							} while(true);
						}
						int rank = 0;
						if(message.contains("@ra")) {
							rank = Byte.parseByte(Character.toString(message.charAt(3)));
							message = message.substring(5);
						}
						final int l8 = message.indexOf("\\n");
						String s1;
						if(l8 != -1) {
							s1 = message.substring(0, l8);
							message = message.substring(l8 + 2);
						} else {
							s1 = message;
							message = "";
						}
						if(Config.def.isDEBUG_INDEXES()) {
							s1 = childWidget.id + "";
						}
						if(rank >= 1) {
							ImageCache.get(1626 + rank).drawImage(xPos + childWidget.width / 2, yPos + font.lineHeight / 2 - 3);
							xPos += 11;
						}
						if(childWidget.textCenter) {
							font.drawCenteredEffectString(s1, xPos + childWidget.width / 2, l6, color, childWidget.textShadow);
						} else {
							switch (childWidget.textAlign) {
								case 0:
									font.drawLeftAlignedEffectString(s1, xPos, l6, color, childWidget.textShadow);
									break;
								case 1:
									font.drawCenteredEffectString(s1, xPos + (childWidget.width / 2), l6, color, childWidget.textShadow);
									break;
								case 2:
									font.drawRightAlignedEffectString(s1, xPos, l6, color, childWidget.textShadow);
									break;
								case 3:
									font.drawCenteredEffectString(s1, xPos, l6, color, childWidget.textShadow);
									break;
								default:
									font.drawLeftAlignedEffectString(s1, xPos, l6, color, childWidget.textShadow);
									break;
							}
						}
					}
				} else if(childWidget.type == Constants.WIDGET_IMAGE) {
					BitmapImage image = null;
					if(childWidget.image < -1) {
						image = ObjectType.getIcon(-childWidget.image, 0, 0);
					} else {
						if(interfaceIsSelected(childWidget) || (childWidget.imageAlt != childWidget.image && childWidget.hoverTriggered && mouseInRegion(xPos, yPos, xPos + childWidget.width, yPos + childWidget.height))) {
							if(childWidget.imageAlt != 0)
								image = ImageCache.get(childWidget.imageAlt);
						} else {
							if(childWidget.image != -1)
								image = ImageCache.get(childWidget.image);
						}
					}
					if(image != null) {
						if(spellSelected == 1 && childWidget.id == spellId && spellId != 0 && image != null) {
							image.drawImage(xPos, yPos, 50);
						} else if(childWidget.imageTransp)
							image.drawImage(xPos, yPos, 100 + childWidget.alpha);
						else
							image.drawImage(xPos, yPos);
						if(childWidget.id == autocastId && childWidget.id == spellId && anIntArray1045[108] != 0)
							ImageCache.get(1832).drawImage(xPos-3, yPos-3);
					}
				} else if(childWidget.type == Constants.WIDGET_MODEL) {
					final int centerX = Rasterizer3D.viewport.centerX;
					final int centerY = Rasterizer3D.viewport.centerY;
					Rasterizer3D.viewport.centerX = xPos + childWidget.width / 2;
					Rasterizer3D.viewport.centerY = yPos + childWidget.height / 2;
					final int i5 = Rasterizer3D.angleSine[childWidget.modelYaw] * childWidget.modelZoom >> 16;
					final int l5 = Rasterizer3D.angleCosine[childWidget.modelYaw] * childWidget.modelZoom >> 16;
					final boolean isSelected = interfaceIsSelected(childWidget);
					int animationID;
					if(isSelected) {
						animationID = childWidget.modelAnimAlt;
					} else {
						animationID = childWidget.modelAnim;
					}
					Model model;
					if(animationID == -1) {
						model = childWidget.getModel(-1, -1, isSelected);
					} else {
						if(animationID > DeformSequence.cache.length)
							return;
						final DeformSequence animation = DeformSequence.cache[animationID];
						if(animation == null)
							return;
						model = childWidget.getModel(animation.anIntArray354[childWidget.modelAnimLength], animation.frameList[childWidget.modelAnimLength], isSelected);
					}
					if(widget.id == forcedChatWidgetId) {
						Rasterizer2D.setClip(25, 25, 505, 116);
					}
					if(childWidget.id == 15125) {
						if(leftClickInRegion(xPos, yPos - 180, xPos + childWidget.width, yPos - 80 + childWidget.height)) {
							rollCharacterInInterface = !rollCharacterInInterface;
						}
					}
					if(Config.def.isDEBUG_INDEXES()) {
						boldFont.drawCenteredEffectString(childWidget.id + "", xPos + childWidget.width / 2, yPos, 0x000000, childWidget.textShadow);
					}
					if(model != null) {
						model.drawModel(childWidget.modelRoll, 0, childWidget.modelYaw, 0, i5, l5);
					}
					Rasterizer2D.removeClip();
					Rasterizer3D.viewport.centerX = centerX;
					Rasterizer3D.viewport.centerY = centerY;
				} else if(childWidget.type == Constants.WIDGET_INVENTORY_2) {
					final BitmapFont font = Interface.fonts[childWidget.fontId];
					int item = 0;
					for(int yColumn = 0; yColumn < childWidget.height; yColumn++) {
						for(int xColumn = 0; xColumn < childWidget.width; xColumn++) {
							if(childWidget.invId[item] > 0) {
								final ObjectType itemDef = ObjectType.get(childWidget.invId[item] - 1);
								String itemData = itemDef.name;
								if(itemDef.stackable || childWidget.invAmt[item] != 1) {
									itemData = itemData + " x" + valueToKOrMLong(childWidget.invAmt[item]);
								}
								final int x = xPos + xColumn * (115 + childWidget.invPadX);
								final int y = yPos + yColumn * (12 + childWidget.invPadY);
								if(childWidget.textCenter) {
									font.drawCenteredEffectString(itemData, x + childWidget.width / 2, y, childWidget.color, childWidget.textShadow);
								} else {
									font.drawLeftAlignedEffectString(itemData, x, y, childWidget.color, childWidget.textShadow);
								}
							}
							item++;
						}
					}
				} else if(childWidget.type == Constants.WIDGET_TOOLTIP && (anInt1500 == childWidget.id || anInt1044 == childWidget.id || anInt1129 == childWidget.id) && anInt1501 == 50 && !menuOpened) {
					int boxWidth = 0;
					int boxHeight = 0;
					final BitmapFont font = plainFont;
					for(String message = childWidget.text; message.length() > 0; ) {
						if(message.contains("%")) {
							do {
								final int k7 = message.indexOf("%1");
								if(k7 == -1) {
									break;
								}
								message = message.substring(0, k7) + interfaceIntToString(extractInterfaceValues(childWidget, 0)) + message.substring(k7 + 2);
							} while(true);
							do {
								final int l7 = message.indexOf("%2");
								if(l7 == -1) {
									break;
								}
								message = message.substring(0, l7) + interfaceIntToString(extractInterfaceValues(childWidget, 1)) + message.substring(l7 + 2);
							} while(true);
							do {
								final int i8 = message.indexOf("%3");
								if(i8 == -1) {
									break;
								}
								message = message.substring(0, i8) + interfaceIntToString(extractInterfaceValues(childWidget, 2)) + message.substring(i8 + 2);
							} while(true);
							do {
								final int j8 = message.indexOf("%4");
								if(j8 == -1) {
									break;
								}
								message = message.substring(0, j8) + interfaceIntToString(extractInterfaceValues(childWidget, 3)) + message.substring(j8 + 2);
							} while(true);
							do {
								final int k8 = message.indexOf("%5");
								if(k8 == -1) {
									break;
								}
								message = message.substring(0, k8) + interfaceIntToString(extractInterfaceValues(childWidget, 4)) + message.substring(k8 + 2);
							} while(true);
						}
						final int lineSplit = message.indexOf("\\n");
						String splitMessage;
						if(lineSplit != -1) {
							splitMessage = message.substring(0, lineSplit);
							message = message.substring(lineSplit + 2);
						} else {
							splitMessage = message;
							message = "";
						}
						final int lineWidth = font.getEffectStringWidth(splitMessage);
						if(lineWidth > boxWidth) {
							boxWidth = lineWidth;
						}
						boxHeight += font.lineHeight + 1;
					}

					int skillId = -1;
					int[] skillIds = {0, 3, 14, 2, 16, 13, 1, 15, 10, 4, 17, 7, 5, 12, 11, 6, 9, 8, 20, 18, 19, 21, 22, 23, 24};
					for(int i = 0; i < 25; i++) {
						if(Constants.MORE_DETAILS_PANEL_ID[i] == childWidget.id) {
							if(currentStatGoals[skillIds[i]] == 0) {
								break;
							}
							boolean maxLevel = false;
							if(maxStats[skillIds[i]] >= 99) {
								if(skillIds[i] != 24) {
									maxStats[skillIds[i]] = 99;
									maxLevel = true;
								} else if(maxStats[skillIds[i]] >= 120) {
									maxStats[skillIds[i]] = 120;
									maxLevel = true;
								}
							}
							if(!maxLevel)
								skillId = i;
							break;
						}
					}
					if(skillId != -1) {
						boxHeight += 15;
					}

					boxWidth += 6;
					boxHeight += 7;
					int x = xPos + childWidget.width - 5 - boxWidth;
					int y = yPos + childWidget.height + 5;
					if(x < xPos + 5) {
						x = xPos + 5;
					}
					if(x + boxWidth > xPosition + widget.width) {
						x = xPosition + widget.width - boxWidth;
					}
					if(y + boxHeight > yPosition + widget.height) {
						y = yPos - boxHeight;
					}
					if(component == UIComponent.INVENTORY) {//Boundaries
						if(widget.id == 3917) {//fixing skill menus
							if(widget.subX[child] + boxWidth > 205) {
								x -= (widget.subX[child] + boxWidth) - 190;
							}
							if(widget.subY[child] + boxHeight > 220) {
								y -= (boxHeight + widget.subY[child]) - 160;
							}
						}
					}

					Rasterizer2D.fillRectangle(x, y, boxWidth, boxHeight, 0xFFFFA0);
					Rasterizer2D.drawRectangle(x, y, boxWidth, boxHeight, 0);
					if(skillId != -1) {
						Double percentage = (double) (currentStatGoals[skillIds[skillId]] != 0 ? (currentExp[skillIds[skillId]] * 100) / getXPForLevel(currentStatGoals[skillIds[skillId]]) : (currentExp[skillIds[skillId]] * 100) / getXPForLevel(currentStats[skillIds[skillId]] + 1));
						Rasterizer2D.fillRectangle(x + 5, (y + boxHeight) - 15, boxWidth - 10, 12, 0xE62E00);
						Rasterizer2D.fillRectangle(x + 5, (y + boxHeight) - 15, (int) ((percentage / 100) * (boxWidth - 10)), 12, 0x00B800);
						Rasterizer2D.drawRectangle(x + 5, (y + boxHeight) - 15, boxWidth - 10, 12, 0);
						smallFont.drawCenteredString(currentStatGoals[skillIds[skillId]] != 0 ? "To level " + currentStatGoals[skillIds[skillId]] : "Next level", x + (boxWidth / 2), (y + boxHeight) - 4, 0x000);
					}
					String message = childWidget.text;
					for(int j11 = y + font.lineHeight + 2; message.length() > 0; j11 += font.lineHeight + 1) {
						if(message.contains("%")) {
							do {
								final int k7 = message.indexOf("%1");
								if(k7 == -1) {
									break;
								}
								message = message.substring(0, k7) + interfaceIntToString(extractInterfaceValues(childWidget, 0)) + message.substring(k7 + 2);
							} while(true);
							do {
								final int l7 = message.indexOf("%2");
								if(l7 == -1) {
									break;
								}
								message = message.substring(0, l7) + interfaceIntToString(extractInterfaceValues(childWidget, 1)) + message.substring(l7 + 2);
							} while(true);
							do {
								final int i8 = message.indexOf("%3");
								if(i8 == -1) {
									break;
								}
								message = message.substring(0, i8) + interfaceIntToString(extractInterfaceValues(childWidget, 2)) + message.substring(i8 + 2);
							} while(true);
							do {
								final int j8 = message.indexOf("%4");
								if(j8 == -1) {
									break;
								}
								message = message.substring(0, j8) + interfaceIntToString(extractInterfaceValues(childWidget, 3)) + message.substring(j8 + 2);
							} while(true);
							do {
								final int k8 = message.indexOf("%5");
								if(k8 == -1) {
									break;
								}
								message = message.substring(0, k8) + interfaceIntToString(extractInterfaceValues(childWidget, 4)) + message.substring(k8 + 2);
							} while(true);
						}
						final int lineSplit = message.indexOf("\\n");
						String splitMessage;
						if(lineSplit != -1) {
							splitMessage = message.substring(0, lineSplit);
							message = message.substring(lineSplit + 2);
						} else {
							splitMessage = message;
							message = "";
						}
						if(childWidget.textCenter) {
							font.drawCenteredEffectString(splitMessage, x + childWidget.width / 2, j11, y, false);
						} else if(splitMessage.contains("\\r")) {
							final String text = splitMessage.substring(0, splitMessage.indexOf("\\r"));
							final String text2 = splitMessage.substring(splitMessage.indexOf("\\r") + 2);
							font.drawLeftAlignedEffectString(text, x + 3, j11, 0, false);
							final int rightX = boxWidth + x - font.getEffectStringWidth(text2) - 2;
							font.drawLeftAlignedEffectString(text2, rightX, j11, 0, false);
						} else {
							font.drawLeftAlignedEffectString(splitMessage, x + 3, j11, 0, false);
						}
					}
				}
			}
		}
		Rasterizer2D.setClip(saveClip);
	}

	public void drawSoak(int damage, int opacity, int drawPos, int x) {
		x -= 12;
		int soakLength = (int) Math.log10(damage) + 1;
		ImageCache.get(188).drawAlphaImage(spriteDrawX + x, drawPos - 12, opacity);
		x += 20;
		ImageCache.get(180).drawAlphaImage(spriteDrawX + x, drawPos - 12, opacity);
		x += 4;
		for(int i = 0; i < soakLength * 2; i++) {
			ImageCache.get(181).drawAlphaImage(spriteDrawX + x, drawPos - 12, opacity);
			x += 4;
		}
		ImageCache.get(182).drawAlphaImage(spriteDrawX + x, drawPos - 10, opacity);
		smallHitFont.drawCenteredString(damage + "", spriteDrawX - 8 + x + (soakLength == 1 ? 5 : 0), drawPos + 32, 0xffffff, opacity);
	}

	private void drawFriendsListOrWelcomeScreen(Interface class9) {
		int j = class9.contentType;
		if(j >= 1 && j <= 100 || j >= 701 && j <= 800) {
			if(j == 1 && anInt900 == 0) {
				class9.text = "Loading friend list";
				class9.actionType = 0;
				return;
			}
			if(j == 1 && anInt900 == 1) {
				class9.text = "Connecting to friendserver";
				class9.actionType = 0;
				return;
			}
			if(j == 2 && anInt900 != 2) {
				class9.text = "Please wait...";
				class9.actionType = 0;
				return;
			}
			int k = friendsCount;
			if(anInt900 != 2) {
				k = 0;
			}
			if(j > 700) {
				j -= 601;
			} else {
				j--;
			}
			if(j >= k) {
				class9.text = "";
				class9.actionType = 0;
				return;
			} else {
				class9.text = friendsList[j];
				class9.actionType = 1;
				return;
			}
		}
		if(j >= 1004 && j <= 1028) {
			j -= 1004;
			boolean maxLevel = false;
			if(maxStats[j] >= 99) {
				if(j != 24) {
					maxStats[j] = 99;
					maxLevel = true;
				} else if(maxStats[j] >= 120) {
					maxStats[j] = 120;
					maxLevel = true;
				}
			}
			int[] ids = {0, 3, 14, 2, 16, 13, 1, 15, 10, 4, 17, 7, 5, 12, 11, 6, 9, 8, 20, 18, 19, 21, 22, 23, 24};
			class9.text = (Constants.SKILL_NAMES_ORDERED[j] + ": " + currentStats[ids[j]] + "/" + maxStats[ids[j]] + "\\n") +
					("Current XP: \\r" + currentExp[ids[j]] + "\\n") +
					(!maxLevel ? "Next level: \\r" + getXPForLevel(maxStats[ids[j]] + 1) + "\\n" : "Max Level Reached!") +
					(!maxLevel ? "Remainder: \\r" + (getXPForLevel(maxStats[ids[j]] + 1) - currentExp[ids[j]]) + "\\n" : "") +
					((maxLevel || currentStatGoals[ids[j]] == 0) ? "" : "Target lvl: \\r" + currentStatGoals[ids[j]] + "\\n") +
					((maxLevel || currentStatGoals[ids[j]] == 0) ? "" : "Remainder: \\r" + (getXPForLevel(currentStatGoals[ids[j]]) - currentExp[ids[j]]));
			return;
		}
		if(j >= 101 && j <= 200 || j >= 801 && j <= 900) {
			int l = friendsCount;
			if(anInt900 != 2) {
				l = 0;
			}
			if(j > 800) {
				j -= 701;
			} else {
				j -= 101;
			}
			if(j >= l) {
				class9.text = "";
				class9.actionType = 0;
				return;
			}
			if(friendsNodeIDs[j] == 0) {
				class9.text = "@red@Offline";
			} else if(friendsNodeIDs[j] == nodeID) {
				class9.text = "@gre@Online";
			} else {
				class9.text = "@red@Offline";
			}
			class9.actionType = 1;
			return;
		}
		if(j == 203) {
			int i1 = friendsCount;
			if(anInt900 != 2) {
				i1 = 0;
			}
			class9.scrollMax = i1 * 15 + 20;
			if(class9.scrollMax <= class9.height) {
				class9.scrollMax = class9.height + 1;
			}
			return;
		}
		if(j >= 401 && j <= 500) {
			if((j -= 401) == 0 && anInt900 == 0) {
				class9.text = "Loading ignore list";
				class9.actionType = 0;
				return;
			}
			if(j == 1 && anInt900 == 0) {
				class9.text = "Please wait...";
				class9.actionType = 0;
				return;
			}
			int j1 = ignoreCount;
			if(anInt900 == 0) {
				j1 = 0;
			}
			if(j >= j1) {
				class9.text = "";
				class9.actionType = 0;
				return;
			} else {
				class9.text = StringUtils.formatName(StringUtils.decryptName(ignoreListAsLongs[j]));
				class9.actionType = 1;
				return;
			}
		}
		if(j == 503) {
			class9.scrollMax = ignoreCount * 15 + 20;
			if(class9.scrollMax <= class9.height) {
				class9.scrollMax = class9.height + 1;
			}
			return;
		}
		if(j == 327) {

			characterInInterface(class9);

			if(aBoolean1031) {
				for(int k1 = 0; k1 < 7; k1++) {
					final int l1 = anIntArray1065[k1];
					if(l1 >= 0 && !Identikit.cache[l1].bodyModelCached()) {
						return;
					}
				}

				aBoolean1031 = false;
				final Model aclass30_sub2_sub4_sub6s[] = new Model[7];
				int i2 = 0;
				for(int j2 = 0; j2 < 7; j2++) {
					final int k2 = anIntArray1065[j2];
					if(k2 >= 0) {
						aclass30_sub2_sub4_sub6s[i2++] = Identikit.cache[k2].getBodyModel();
					}
				}

				final Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
				for(int l2 = 0; l2 < 5; l2++) {
					if(anIntArray990[l2] != 0) {
						model.replaceHsl(anIntArrayArray1003[l2][0], anIntArrayArray1003[l2][anIntArray990[l2]]);
						if(l2 == 1) {
							model.replaceHsl(anIntArray1204[0], anIntArray1204[anIntArray990[l2]]);
						}
					}
				}

				model.applyEffects();
				model.applyAnimation(DeformSequence.cache[localPlayer.anInt1511].frameList[0]);
				model.calculateLighting(64, 850, -30, -50, -30, true);
				class9.modelType = 5;
				class9.modelId = 0;
				Interface.method208(aBoolean994, model);
			}
			return;
		}
		if(j == 328) {

			characterInInterface(class9);

			if(aBoolean1031) {
				final Model characterDisplay = localPlayer.getAnimatedModel();
				for(int l2 = 0; l2 < 5; l2++) {
					if(anIntArray990[l2] != 0) {
						characterDisplay.replaceHsl(anIntArrayArray1003[l2][0], anIntArrayArray1003[l2][anIntArray990[l2]]);
						if(l2 == 1) {
							characterDisplay.replaceHsl(anIntArray1204[0], anIntArray1204[anIntArray990[l2]]);
						}
					}
				}
				final int staticFrame = localPlayer.anInt1511;
				characterDisplay.applyEffects();
				characterDisplay.applyAnimation(DeformSequence.cache[staticFrame].frameList[0]);
				// characterDisplay.method479(64, 850, -30, -50, -30, true);
				class9.modelType = 5;
				class9.modelId = 0;
				Interface.method208(aBoolean994, characterDisplay);
			}
			return;
		}
		if(j == 600) {
			class9.text = reportAbuseInput;
			if(loopCycle % 20 < 10) {
				class9.text += "|";
				return;
			} else {
				class9.text += " ";
				return;
			}
		}
		if(j == 613) {
			if(localPrivilege >= 1) {
				if(canMute) {
					class9.color = 0xff0000;
					class9.text = "Moderator option: Mute player for 48 hours: <ON>";
				} else {
					class9.color = 0xffffff;
					class9.text = "Moderator option: Mute player for 48 hours: <OFF>";
				}
			} else {
				class9.text = "";
			}
		}
		if(j == 650 || j == 655) {
			if(anInt1193 != 0) {
				String s;
				if(daysSinceLastLogin == 0) {
					s = "earlier today";
				} else if(daysSinceLastLogin == 1) {
					s = "yesterday";
				} else {
					s = daysSinceLastLogin + " days ago";
				}
				class9.text = "You last logged in " + s + " from: " + SignLink.dns;
			} else {
				class9.text = "";
			}
		}
		if(j == 651) {
			if(unreadMessages == 0) {
				class9.text = "0 unread messages";
				class9.color = 0xffff00;
			}
			if(unreadMessages == 1) {
				class9.text = "1 unread message";
				class9.color = 65280;
			}
			if(unreadMessages > 1) {
				class9.text = unreadMessages + " unread messages";
				class9.color = 65280;
			}
		}
		if(j == 652) {
			if(daysSinceRecovChange == 201) {
				if(membersInt == 1) {
					class9.text = "@yel@This is a non-members world: @whi@Since you are a member we";
				} else {
					class9.text = "";
				}
			} else if(daysSinceRecovChange == 200) {
				class9.text = "You have not yet set any password recovery questions.";
			} else {
				String s1;
				if(daysSinceRecovChange == 0) {
					s1 = "Earlier today";
				} else if(daysSinceRecovChange == 1) {
					s1 = "Yesterday";
				} else {
					s1 = daysSinceRecovChange + " days ago";
				}
				class9.text = s1 + " you changed your recovery questions";
			}
		}
		if(j == 653) {
			if(daysSinceRecovChange == 201) {
				if(membersInt == 1) {
					class9.text = "@whi@recommend you use a members world instead. You may use";
				} else {
					class9.text = "";
				}
			} else if(daysSinceRecovChange == 200) {
				class9.text = "We strongly recommend you do so now to secure your account.";
			} else {
				class9.text = "If you do not remember making this change then cancel it immediately";
			}
		}
		if(j == 654) {
			if(daysSinceRecovChange == 201) {
				if(membersInt == 1) {
					class9.text = "@whi@this world but member benefits are unavailable whilst here.";
					return;
				} else {
					class9.text = "";
					return;
				}
			}
			if(daysSinceRecovChange == 200) {
				class9.text = "Do this from the 'account management' area on our front webpage";
				return;
			}
			class9.text = "Do this from the 'account management' area on our front webpage";
		}
	}


	private boolean buildFriendsListMenu(Interface class9) {
		int i = class9.contentType;
		if(i >= 1 && i <= 200 || i >= 701 && i <= 900) {
			if(i >= 801) {
				i -= 701;
			} else if(i >= 701) {
				i -= 601;
			} else if(i >= 101) {
				i -= 101;
			} else {
				i--;
			}
			menuItemName[menuPos] = "Remove @whi@" + friendsList[i];
			menuItemCode[menuPos] = 792;
			menuPos++;
			menuItemName[menuPos] = "Message @whi@" + friendsList[i];
			menuItemCode[menuPos] = 639;
			menuPos++;
			return true;
		}
		if(i >= 401 && i <= 500) {
			menuItemName[menuPos] = "Remove @whi@" + class9.text;
			menuItemCode[menuPos] = 322;
			menuPos++;
			return true;
		} else {
			return false;
		}
	}

	public void buildWidgetMenu(int i, Interface widget, int k, int l, int i1, int j1) {
		if(widget.type != 0 || widget.subId == null || widget.hoverTriggered) {
			return;
		}
		if(k < i || i1 < l || k > i + widget.width || i1 > l + widget.height) {
			return;
		}
		for(int child = 0; child < widget.subId.length; child++) {
			int xPos = widget.subX[child] + i;
			int yPos = widget.subY[child] + l - j1;
			final Interface childWidget = Interface.cache[widget.subId[child]];
			xPos += childWidget.offsetX;
			yPos += childWidget.offsetY;
			if((widget.id == 3917 || childWidget.hoverInterToTrigger >= 0 || childWidget.hoverColor != 0) &&
					k >= xPos && i1 >= yPos && k < xPos + childWidget.width && i1 < yPos + childWidget.height) {
				if(childWidget.hoverInterToTrigger >= 0) {
					anInt886 = childWidget.hoverInterToTrigger;
				} else {
					anInt886 = childWidget.id;
				}
				if(childWidget.type == 8 && k >= xPos && i1 >= yPos && k < xPos + childWidget.width && i1 < yPos + childWidget.height) {
					anInt1315 = childWidget.id;
				}
			}
			if(childWidget.type == 0) {
				buildWidgetMenu(xPos, childWidget, k, yPos, i1, childWidget.scrollPos);
				if(childWidget.scrollMax > childWidget.height) {
					gameActivity.processScrollbar(xPos + childWidget.width, yPos, k, i1, childWidget.height, childWidget.scrollMax, childWidget);
				}
			} else {
				if(childWidget.actionType == 1 && k >= xPos && i1 >= yPos && k < xPos + childWidget.width && i1 < yPos + childWidget.height) {
					boolean flag = false;
					if(childWidget.contentType != 0) {
						flag = buildFriendsListMenu(childWidget);
					}
					if(!flag) {
						menuItemName[menuPos] = childWidget.tooltip;
						if(Config.def.isDEBUG_INDEXES()) {
							menuItemName[menuPos] += " @mag@" + childWidget.id;
						}
						menuItemCode[menuPos] = 315;
						menuItemArg3[menuPos] = childWidget.id;
						menuPos++;
					}
				}
				if(childWidget.actionType == 2 && spellSelected == 0 && k >= xPos && i1 >= yPos && k < xPos + childWidget.width && i1 < yPos + childWidget.height) {
					String s = childWidget.selectedActionName;
					if(s.indexOf(" ") != -1) {
						s = s.substring(0, s.indexOf(" "));
					}
					if(((childWidget.spellUsableOn & 8) == 8) || ((childWidget.spellUsableOn & 2) == 2)) {
						menuItemName[menuPos] = (autocastId == childWidget.id ? "Cancel autocast" : "Autocast") + "@gre@ " + childWidget.spellName;
						menuItemCode[menuPos] = autocastId == childWidget.id ? 105 : 104;
						menuItemArg3[menuPos] = childWidget.id;
						menuPos++;
					}
					menuItemName[menuPos] = s + " @gre@" + childWidget.spellName;
					if(Config.def.isDEBUG_INDEXES()) {
						menuItemName[menuPos] += " @mag@" + childWidget.id;
					}
					menuItemCode[menuPos] = 626;
					menuItemArg3[menuPos] = childWidget.id;
					menuPos++;
				}
				if(childWidget.actionType == 3 && k >= xPos && i1 >= yPos && k < xPos + childWidget.width && i1 < yPos + childWidget.height) {
					menuItemName[menuPos] = "Close";
					if(Config.def.isDEBUG_INDEXES()) {
						menuItemName[menuPos] += " @mag@" + childWidget.id;
					}
					menuItemCode[menuPos] = 200;
					menuItemArg3[menuPos] = childWidget.id;
					menuPos++;
				}
				if(childWidget.actionType == 4 && k >= xPos && i1 >= yPos && k < xPos + childWidget.width && i1 < yPos + childWidget.height) {
					menuItemName[menuPos] = childWidget.tooltip;
					if(Config.def.isDEBUG_INDEXES()) {
						menuItemName[menuPos] += " @mag@" + childWidget.id;
					}
					menuItemCode[menuPos] = 169;
					menuItemArg3[menuPos] = childWidget.id;
					menuPos++;
				}
				if(childWidget.actionType == 5 && k >= xPos && i1 >= yPos && k < xPos + childWidget.width && i1 < yPos + childWidget.height) {
					menuItemName[menuPos] = childWidget.tooltip;
					menuItemCode[menuPos] = 646;
					menuItemArg3[menuPos] = childWidget.id;
					menuPos++;
				}
				if(childWidget.actionType == 6 && !aBoolean1149 && k >= xPos && i1 >= yPos && k < xPos + childWidget.width && i1 < yPos + childWidget.height) {
					menuItemName[menuPos] = childWidget.tooltip;
					if(Config.def.isDEBUG_INDEXES()) {
						menuItemName[menuPos] += " @mag@" + childWidget.id;
					}
					menuItemCode[menuPos] = 679;
					menuItemArg3[menuPos] = childWidget.id;
					menuPos++;
				}
				if(k >= xPos && i1 >= yPos && k < xPos + (childWidget.type == 4 ? Interface.fonts[childWidget.fontId].getStringWidth(childWidget.text) : childWidget.width) && i1 < yPos + childWidget.height) {
					if(childWidget.actions != null) {
						if((childWidget.type == 4 && childWidget.text.length() > 0) || childWidget.type == 5) {
							for(int action = childWidget.actions.length - 1; action >= 0; action--) {
								if(childWidget.actions[action] != null) {
									menuItemName[menuPos] = childWidget.actions[action] + " " + (childWidget.type == 4 ? childWidget.text : "");
									menuItemCode[menuPos] = 647;
									menuItemArg2[menuPos] = action;
									menuItemArg3[menuPos] = childWidget.id;
									menuPos++;
								}
							}
						}
					}
				}
				if(childWidget.type == 2) {
					int k2 = 0;
					for(int l2 = 0; l2 < childWidget.height; l2++) {
						for(int i3 = 0; i3 < childWidget.width; i3++) {
							int j3 = xPos + i3 * (32 + childWidget.invPadX);
							int k3 = yPos + l2 * (32 + childWidget.invPadY);
							if(k2 < 20) {
								j3 += childWidget.invIconX[k2];
								k3 += childWidget.invIconY[k2];
							}
							if(k >= j3 && i1 >= k3 && k < j3 + 32 && i1 < k3 + 32) {
								invDestSlot = k2;
								lastActiveInvInterface = childWidget.id;
								if(childWidget.invId[k2] > 0) {
									final ObjectType itemDef = ObjectType.get(childWidget.invId[k2] - 1);
									if(itemSelected == 1 && childWidget.isInv) {
										if(childWidget.id != anInt1284 || k2 != anInt1283) {
											menuItemName[menuPos] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
											menuItemCode[menuPos] = 870;
											menuItemArg1[menuPos] = itemDef.id;
											menuItemArg2[menuPos] = k2;
											menuItemArg3[menuPos] = childWidget.id;
											menuPos++;
										}
									} else if(spellSelected == 1 && childWidget.isInv) {
										if((spellUsableOn & 0x10) == 16) {
											menuItemName[menuPos] = spellTooltip + " @lre@" + itemDef.name;
											menuItemCode[menuPos] = 543;
											menuItemArg1[menuPos] = itemDef.id;
											menuItemArg2[menuPos] = k2;
											menuItemArg3[menuPos] = childWidget.id;
											menuPos++;
										}
									} else {
										if(childWidget.isInv) {
											for(int l3 = 4; l3 >= 3; l3--) {
												if(itemDef.actions != null && itemDef.actions[l3] != null) {
													menuItemName[menuPos] = itemDef.actions[l3] + " @lre@" + itemDef.name;
													if(l3 == 3) {
														menuItemCode[menuPos] = 493;
													}
													if(l3 == 4) {
														menuItemCode[menuPos] = 847;
													}
													menuItemArg1[menuPos] = itemDef.id;
													menuItemArg2[menuPos] = k2;
													menuItemArg3[menuPos] = childWidget.id;
													menuPos++;
												} else if(l3 == 4) {
													menuItemName[menuPos] = "Drop @lre@" + itemDef.name;
													menuItemCode[menuPos] = 847;
													menuItemArg1[menuPos] = itemDef.id;
													menuItemArg2[menuPos] = k2;
													menuItemArg3[menuPos] = childWidget.id;
													menuPos++;
												}
											}
										}
										if(childWidget.invUse) {
											menuItemName[menuPos] = "Use @lre@" + itemDef.name;
											menuItemCode[menuPos] = 447;
											menuItemArg1[menuPos] = itemDef.id;
											menuItemArg2[menuPos] = k2;
											menuItemArg3[menuPos] = childWidget.id;
											menuPos++;
										}
										if(childWidget.isInv && itemDef.actions != null) {
											for(int i4 = 2; i4 >= 0; i4--) {
												if(itemDef.actions[i4] != null) {
													menuItemName[menuPos] = itemDef.actions[i4] + " @lre@" + itemDef.name;
													if(i4 == 0) {
														menuItemCode[menuPos] = 74;
													}
													if(i4 == 1) {
														menuItemCode[menuPos] = 454;
													}
													if(i4 == 2) {
														menuItemCode[menuPos] = 539;
													}
													menuItemArg1[menuPos] = itemDef.id;
													menuItemArg2[menuPos] = k2;
													menuItemArg3[menuPos] = childWidget.id;
													menuPos++;
												}
											}
										}
										if(childWidget.menuItem != null) {
											for(int j4 = 4; j4 >= 0; j4--) {
												if(childWidget.menuItem[j4] != null) {
													menuItemName[menuPos] = childWidget.menuItem[j4] + " @lre@" + itemDef.name;
													if(j4 == 0) {
														menuItemCode[menuPos] = 632;
													}
													if(j4 == 1) {
														menuItemCode[menuPos] = 78;
													}
													if(j4 == 2) {
														menuItemCode[menuPos] = 867;
													}
													if(j4 == 3) {
														menuItemCode[menuPos] = 431;
													}
													if(j4 == 4) {
														menuItemCode[menuPos] = 53;
													}
													menuItemArg1[menuPos] = itemDef.id;
													menuItemArg2[menuPos] = k2;
													menuItemArg3[menuPos] = childWidget.id;
													menuPos++;
												}
											}
										}
										menuItemName[menuPos] = "Examine @lre@" + itemDef.name;
										if(Config.def.isDEBUG_INDEXES()) {
											menuItemName[menuPos] += " @mag@" + itemDef.id;
										}
										menuItemCode[menuPos] = 1125;
										menuItemArg1[menuPos] = itemDef.id;
										menuItemArg2[menuPos] = k2;
										menuItemArg3[menuPos] = childWidget.id;
										menuPos++;
									}
								}
							}
							k2++;
						}
					}
				}
			}
		}
	}

	private int extractInterfaceValues(Interface class9, int j) {
		if(class9.valueIndexArray == null || j >= class9.valueIndexArray.length) {
			return -2;
		}
		try {
			final int ai[] = class9.valueIndexArray[j];
			int k = 0;
			int l = 0;
			int i1 = 0;
			do {
				final int j1 = ai[l++];
				int k1 = 0;
				byte byte0 = 0;
				if(j1 == 0) {
					return k;
				}
				if(j1 == 1) {
					if(class9.id == 4016)
						k1 = currentStats[ai[l++]] / 10;
					else
						k1 = currentStats[ai[l++]];
				}
				if(j1 == 2) {
					k1 = maxStats[ai[l++]];
				}
				if(j1 == 3) {
					k1 = currentExp[ai[l++]];
				}
				if(j1 == 4) {
					final Interface container = Interface.cache[ai[l++]];
					final int k2 = ai[l++];
					if(k2 >= 0 && k2 < ObjectType.length) {
						for(int j3 = 0; j3 < container.invId.length; j3++) {
							if(container.invId[j3] == k2 + 1) {
								k1 += container.invAmt[j3];
							}
						}
					}
				}
				if(j1 == 5) {
					k1 = variousSettings[ai[l++]];
				}
				if(j1 == 6) {
					k1 = EXP_FOR_LEVEL[maxStats[ai[l++]] - 1];
				}
				if(j1 == 7) {
					k1 = variousSettings[ai[l++]] * 100 / 46875;
				}
				if(j1 == 8) {
					k1 = localPlayer.combatLevel;
				}
				if(j1 == 9) {
					for(int l1 = 0; l1 < Constants.SKILL_AMOUNT; l1++) {
						//if(Constants.SKILL_ENABLED[l1]) {
						k1 += maxStats[l1];
						//}
					}
				}
				if(j1 == 10) {
					final Interface class9_2 = Interface.cache[ai[l++]];
					final int l2 = ai[l++] + 1;
					if(l2 >= 0 && l2 < ObjectType.length) {
						for(final int item : class9_2.invId) {
							if(item != l2) {
								continue;
							}
							k1 = 0x3b9ac9ff;
							break;
						}
					}
				}
				if(j1 == 11) {
					k1 = energy;
				}
				if(j1 == 12) {
					k1 = weight;
				}
				if(j1 == 13) {
					final int i2 = variousSettings[ai[l++]];
					final int i3 = ai[l++];
					k1 = (i2 & 1 << i3) == 0 ? 0 : 1;
				}
				if(j1 == 14) {
					final int j2 = ai[l++];
					final VaryingBit varBit = VaryingBit.cache[j2];
					int l3 = varBit.configId;
					final int i4 = varBit.leastSignificantBit;
					final int j4 = varBit.mostSignificantBit;
					final int k4 = BIT_MASK[j4 - i4];
					k = variousSettings[l3] >> i4 & k4;
				}
				if(j1 == 15) {
					byte0 = 1;
				}
				if(j1 == 16) {
					byte0 = 2;
				}
				if(j1 == 17) {
					byte0 = 3;
				}
				if(j1 == 18) {
					k1 = (localPlayer.x >> 7) + baseX;
				}
				if(j1 == 19) {
					k1 = (localPlayer.y >> 7) + baseY;
				}
				if(j1 == 20) {
					k1 = ai[l++];
				}
				if(byte0 == 0) {
					if(i1 == 0) {
						k += k1;
					}
					if(i1 == 1) {
						k -= k1;
					}
					if(i1 == 2 && k1 != 0) {
						k /= k1;
					}
					if(i1 == 3) {
						k *= k1;
					}
					i1 = 0;
				} else {
					i1 = byte0;
				}
			} while(true);
		} catch(final Exception _ex) {
			return -1;
		}
	}

	private boolean promptUserForInput(Interface class9) {
		final int j = class9.contentType;
		if(anInt900 == 2) {
			if(j == 201) {
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 1;
				promptInputTitle = "Enter name of friend to add to list";
			}
			if(j == 202) {
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 2;
				promptInputTitle = "Enter name of friend to delete from list";
			}
		}
		if(j == 205) {
			anInt1011 = 250;
			return true;
		}
		if(j == 501) {
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 4;
			promptInputTitle = "Enter name of player to add to list.";
		}
		if(j == 502) {
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 5;
			promptInputTitle = "Enter name of player to delete from list.";
		}
		if(j >= 300 && j <= 313) {
			final int k = (j - 300) / 2;
			final int j1 = j & 1;
			int i2 = anIntArray1065[k];
			if(i2 != -1) {
				do {
					if(j1 == 0 && --i2 < 0) {
						i2 = Identikit.length - 1;
					}
					if(j1 == 1 && ++i2 >= Identikit.length) {
						i2 = 0;
					}
				}
				while(Identikit.cache[i2].widgetDisplayed || Identikit.cache[i2].partId != k + (aBoolean1047 ? 0 : 7));


				anIntArray1065[k] = i2;

				if((anIntArray1065[2] >= 443 && anIntArray1065[2] <= 456) || (anIntArray1065[2] >= 556 && anIntArray1065[2] <= 564)) {
					anIntArray1065[3] = -1;
				} else {
					if(anIntArray1065[3] == -1)
						if(!aBoolean1047)
							anIntArray1065[3] = 61;
						else
							anIntArray1065[3] = 26;
					if(k == 3)
						anIntArray1065[k] = i2;

				}

				aBoolean1031 = true;
			}
		}
		if(j >= 314 && j <= 323) {
			final int l = (j - 314) / 2;
			final int k1 = j & 1;
			int j2 = anIntArray990[l];
			if(k1 == 0 && --j2 < 0) {
				j2 = anIntArrayArray1003[l].length - 1;
			}
			if(k1 == 1 && ++j2 >= anIntArrayArray1003[l].length) {
				j2 = 0;
			}
			anIntArray990[l] = j2;
			aBoolean1031 = true;
		}
		if(j == 324 && !aBoolean1047) {
			aBoolean1047 = true;
			method45();
		}
		if(j == 325 && aBoolean1047) {
			aBoolean1047 = false;
			method45();
		}
		if(j == 326) {
			outBuffer.putOpcode(101);
			outBuffer.putByte(aBoolean1047 ? 0 : 1);
			for(int i1 = 0; i1 < 7; i1++) {
				if(anIntArray1065[i1] == -1) {
					outBuffer.putShort(0);
				} else {
					outBuffer.putShort(anIntArray1065[i1]);

				}
			}
			for(int l1 = 0; l1 < 5; l1++) {
				outBuffer.putShort(anIntArray990[l1]);
			}
			return true;
		}
		if(j == 613) {
			canMute = !canMute;
		}
		if(j >= 601 && j <= 612) {
			clearTopInterfaces();
			if(reportAbuseInput.length() > 0) {
				outBuffer.putOpcode(218);
				outBuffer.putLong(StringUtils.encryptName(reportAbuseInput));
				outBuffer.putByte(j - 601);
				outBuffer.putByte(canMute ? 1 : 0);
			}
		}
		return false;
	}

	private void characterInInterface(Interface class9) {

		if(!Config.def.isCHARACTER_PREVIEW()) {
			class9.modelYaw = 150;
			class9.modelRoll = (int) (Math.sin(loopCycle / 40D) * 256D) & 0x7ff;
			class9.offsetY = 0;
			class9.modelZoom = 560;
			return;
		}

		if(Config.def.isCHARACTER_PREVIEW()) {
			class9.modelYaw = 70;
			class9.offsetY = 10;
			class9.modelZoom = class9.id == 15125 ? 535 : 585;
			if(rollCharacterInInterface) {
				class9.modelRoll -= 10;
				if(class9.modelRoll < 0) {
					class9.modelRoll += 2048;
				}
			} else if(class9.modelRoll != 0) {
				if(class9.modelRoll > 1023) {
					class9.modelRoll = class9.modelRoll + 20;
					if(class9.modelRoll > 2047) {
						class9.modelRoll = 0;
					}
				} else {
					class9.modelRoll = class9.modelRoll - 20;
					if(class9.modelRoll < 0) {
						class9.modelRoll = 0;
					}
				}
			}
		}
	}

	private boolean interfaceIsSelected(Interface class9) {
		if(class9.valueCompareType == null) {
			return false;
		}
		for(int i = 0; i < class9.valueCompareType.length; i++) {
			final int j = extractInterfaceValues(class9, i);
			final int k = class9.requiredValues[i];
			if(class9.valueCompareType[i] == 2) {
				if(j >= k) {
					return false;
				}
			} else if(class9.valueCompareType[i] == 3) {
				if(j <= k) {
					return false;
				}
			} else if(class9.valueCompareType[i] == 4) {
				if(j == k) {
					return false;
				}
			} else if(j != k) {
				return false;
			}
		}

		return true;
	}

	private String interfaceIntToString(int j) {
		if(j < 0x3b9ac9ff) {
			return j + "";
		} else {
			return "*";
		}
	}

	public void updateStrings(String str, int i) {
		switch(i) {
			case 1675:
				Interface.cache[17508].text = str;
				break;// Stab
			case 1676:
				Interface.cache[17509].text = str;
				break;// Slash
			case 1677:
				Interface.cache[17510].text = str;
				break;// Cursh
			case 1678:
				Interface.cache[17511].text = str;
				break;// Magic
			case 1679:
				Interface.cache[17512].text = str;
				break;// Range
			case 1680:
				Interface.cache[17513].text = str;
				break;// Stab
			case 1681:
				Interface.cache[17514].text = str;
				break;// Slash
			case 1682:
				Interface.cache[17515].text = str;
				break;// Crush
			case 1683:
				Interface.cache[17516].text = str;
				break;// Magic
			case 1684:
				Interface.cache[17517].text = str;
				break;// Range
			case 1686:
				Interface.cache[17518].text = str;
				break;// Strength
			case 1687:
				Interface.cache[17519].text = str;
				break;// Prayer
		}
	}

	private void handleSettings(int index) {
		final int optionToChange = VariancePopulation.cache[index].anInt709;
		if(optionToChange == 0) {
			return;
		}
		final int config = variousSettings[index];
		if(optionToChange == 1) {
			if(config == 1) {
				Rasterizer3D.setBrightness(0.90000000000000002F);
			}
			if(config == 2) {
				Rasterizer3D.setBrightness(0.80000000000000004F);
			}
			if(config == 3) {
				Rasterizer3D.setBrightness(0.69999999999999996F);
			}
			if(config == 4) {
				Rasterizer3D.setBrightness(0.59999999999999998F);
			}
			ObjectType.iconcache.clear();
			updateAllGraphics = true;
		}
		if(optionToChange == 5) {
			mouseButtonsToggle = config;
		}
		if(optionToChange == 6) {
			chatEffectsToggle = config;
		}
		if(optionToChange == 8) {
			splitPrivateChat = config;
		}
		if(optionToChange == 9) {
			anInt913 = config;
		}
	}

	private void clearMemory() {
		LocationType.modelCache.clear();
		LocationType.animatedModelCache.clear();
		NPCType.modelcache.clear();
		ObjectType.modelcache.clear();
		ObjectType.iconcache.clear();
		Player.modelcache.clear();
		SpotAnimation.modelcache.clear();
	}

	private void spawnGroundItem(int x, int y) {
		final LinkedDeque objList = sceneItems[cameraPlane][x][y];
		if(objList == null) {
			scene.removeObjectUnit(cameraPlane, x, y);
			return;
		}
		int k = 0xfa0a1f01;
		Object object = null;
		for(ObjectStack item = (ObjectStack) objList.getFirst(); item != null; item = (ObjectStack) objList.getNext()) {
			final ObjectType obj = ObjectType.get(item.id);
			int l = obj.value;
			if(obj.stackable) {
				l *= item.amount + 1;
			}
			if(l > k) {
				k = l;
				object = item;
			}
		}
		objList.addFirst((SinglyLinkableEntry) object);
		Object obj1 = null;
		Object obj2 = null;
		for(ObjectStack class30_sub2_sub4_sub2_1 = (ObjectStack) objList.getFirst(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (ObjectStack) objList.getNext()) {
			if(class30_sub2_sub4_sub2_1.id != ((ObjectStack) object).id && obj1 == null) {
				obj1 = class30_sub2_sub4_sub2_1;
			}
			if(class30_sub2_sub4_sub2_1.id != ((ObjectStack) object).id && class30_sub2_sub4_sub2_1.id != ((ObjectStack) obj1).id && obj2 == null) {
				obj2 = class30_sub2_sub4_sub2_1;
			}
		}
		long i1 = x + (y << 7) + 0xC000000000L;
		scene.setObjectUnit((Entity) object, (Entity) obj1, (Entity) obj2, cameraPlane, x, y, method42(cameraPlane, x * 128 + 64, y * 128 + 64), i1);
	}

	public boolean menuHasAddFriend(int j) {
		if(j < 0) {
			return false;
		}
		int k = menuItemCode[j];
		if(k >= 2000) {
			k -= 2000;
		}
		return k == 337;
	}

	public void determineMenuSize() {
		int w = (Config.def.getSELECTED_MENU() > 3 ? plainFont : boldFont).getEffectStringWidth("Choose Option");
		for(int j = 0; j < menuPos; j++) {
			if(Config.def.isDEBUG_INDEXES()) {
				menuItemName[j] += " @mon@-> " + menuItemCode[j];
				menuItemName[j] += ":{" + menuItemArg1[j];
				menuItemName[j] += "," + menuItemArg2[j];
				menuItemName[j] += "," + menuItemArg3[j];
				menuItemName[j] += "," + menuItemArg4[j] + "}" + j;
			}
			final int k = (Config.def.getSELECTED_MENU() > 3 ? plainFont : boldFont).getEffectStringWidth(menuItemName[j]);
			if(k > w) {
				w = k;
			}
		}
		w += 8;
		int winw = uiRenderer.isFixed() ? 765 : windowWidth;
		int winh = uiRenderer.isFixed() ? 503 : windowHeight;
		final int l = 15 * menuPos + 21;
		if(super.clickX >= 0 && super.clickY >= 0) {
			int i1 = super.clickX - w / 2;
			if(i1 + w > winw) {
				i1 = winw - w;
			}
			if(i1 < 0) {
				i1 = 0;
			}
			int l1 = super.clickY;
			if(l1 + l > winh) {
				l1 = winh - l;
			}
			if(l1 < 0) {
				l1 = 0;
			}
			menuOpened = true;
			menuX = i1;
			menuY = l1;
			menuWidth = w;
			menuHeight = 15 * menuPos + 22;
		}
	}

	public void setCameraPos(int j, int roll, int x, int z, int yaw, int y) {
		final int roll2 = 2048 - roll & 0x7ff;
		final int yaw2 = 2048 - yaw & 0x7ff;
		int dx = 0;
		int dz = 0;
		int dy = j;
		if(roll2 != 0) {
			final int roll2sin = Model.angleSine[roll2];
			final int roll2cos = Model.angleCosine[roll2];
			final int dz2 = dz * roll2cos - dy * roll2sin >> 16;
			dy = dz * roll2sin + dy * roll2cos >> 16;
			dz = dz2;
		}
		if(yaw2 != 0) {
			final int yaw2sin = Model.angleSine[yaw2];
			final int yaw2cos = Model.angleCosine[yaw2];
			final int dx2 = dy * yaw2sin + dx * yaw2cos >> 16;
			dy = dy * yaw2cos - dx * yaw2sin >> 16;
			dx = dx2;
		}
		cameraLocationX = x - dx;
		cameraLocationZ = z - dz;
		cameraLocationY = y - dy;
		cameraRoll = roll;
		cameraYaw = yaw;
	}

	public void calcForcedCameraPos() {
		int campxx = forcedCameraTileX * 128 + 64;
		int campxy = forcedCameraTileY * 128 + 64;
		int k = method42(cameraPlane, campxx, campxy) - forcedCameraPixelZ;
		if(cameraLocationX < campxx) {
			cameraLocationX += anInt1101 + (campxx - cameraLocationX) * anInt1102 / 1000;
			if(cameraLocationX > campxx) {
				cameraLocationX = campxx;
			}
		}
		if(cameraLocationX > campxx) {
			cameraLocationX -= anInt1101 + (cameraLocationX - campxx) * anInt1102 / 1000;
			if(cameraLocationX < campxx) {
				cameraLocationX = campxx;
			}
		}
		if(cameraLocationZ < k) {
			cameraLocationZ += anInt1101 + (k - cameraLocationZ) * anInt1102 / 1000;
			if(cameraLocationZ > k) {
				cameraLocationZ = k;
			}
		}
		if(cameraLocationZ > k) {
			cameraLocationZ -= anInt1101 + (cameraLocationZ - k) * anInt1102 / 1000;
			if(cameraLocationZ < k) {
				cameraLocationZ = k;
			}
		}
		if(cameraLocationY < campxy) {
			cameraLocationY += anInt1101 + (campxy - cameraLocationY) * anInt1102 / 1000;
			if(cameraLocationY > campxy) {
				cameraLocationY = campxy;
			}
		}
		if(cameraLocationY > campxy) {
			cameraLocationY -= anInt1101 + (cameraLocationY - campxy) * anInt1102 / 1000;
			if(cameraLocationY < campxy) {
				cameraLocationY = campxy;
			}
		}
		campxx = anInt995 * 128 + 64;
		campxy = anInt996 * 128 + 64;
		k = method42(cameraPlane, campxx, campxy) - anInt997;
		final int x = campxx - cameraLocationX;
		final int z = k - cameraLocationZ;
		final int y = campxy - cameraLocationY;
		final int distFromOrigin = (int) Math.sqrt(x * x + y * y);
		int rollLimit = (int) (Math.atan2(z, distFromOrigin) * 325.94900000000001D) & 0x7ff;
		final int yawLimit = (int) (Math.atan2(x, y) * -325.94900000000001D) & 0x7ff;
		if(rollLimit < 128) {
			rollLimit = 128;
		}
		if(rollLimit > 383) {
			rollLimit = 383;
		}
		if(cameraRoll < rollLimit) {
			cameraRoll += anInt998 + (rollLimit - cameraRoll) * anInt999 / 1000;
			if(cameraRoll > rollLimit) {
				cameraRoll = rollLimit;
			}
		}
		if(cameraRoll > rollLimit) {
			cameraRoll -= anInt998 + (cameraRoll - rollLimit) * anInt999 / 1000;
			if(cameraRoll < rollLimit) {
				cameraRoll = rollLimit;
			}
		}
		int j2 = yawLimit - cameraYaw;
		if(j2 > 1024) {
			j2 -= 2048;
		}
		if(j2 < -1024) {
			j2 += 2048;
		}
		if(j2 > 0) {
			cameraYaw += anInt998 + j2 * anInt999 / 1000;
			cameraYaw &= 0x7ff;
		}
		if(j2 < 0) {
			cameraYaw -= anInt998 + -j2 * anInt999 / 1000;
			cameraYaw &= 0x7ff;
		}
		int k2 = yawLimit - cameraYaw;
		if(k2 > 1024) {
			k2 -= 2048;
		}
		if(k2 < -1024) {
			k2 += 2048;
		}
		if(k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0) {
			cameraYaw = yawLimit;
		}
	}

	public void loadingStages() {
		if(Config.def.isLOW_MEM() && loadingStage == 2 && MapDecoder.plane != cameraPlane) {
			gameGraphics.setCanvas();
			fancyFont.drawLeftAlignedEffectString("Loading - please wait...", 10, 20, 0xffffff, true);
			if(uiRenderer.isResizableOrFull()) {
				gameGraphics.drawGraphics(0, 0, super.graphics);
			} else {
				gameGraphics.drawGraphics(4, 4, super.graphics);
			}
			loadingStage = 1;
			aLong824 = System.currentTimeMillis();
		}
		if(loadingStage == 1) {
			final int j = initialiseRegionLoading();
			if(j != 0 && System.currentTimeMillis() - aLong824 > 0x57e40L) {
				SignLink.reportError(localUsername + " glcfb " + aLong1215 + "," + j + "," + Config.def.isLOW_MEM() + "," + cacheIdx[0] + "," + onDemandRequester.getRequestCount() + "," + cameraPlane + "," + regionX + "," + regionY);
				aLong824 = System.currentTimeMillis();
			}
		}
		if(loadingStage == 2 && cameraPlane != planeReq) {
			planeReq = cameraPlane;
			renderMinimap(cameraPlane);
		}
	}

	public void connect(String username, String password) {
		if(username.length() == 0 || password.length() == 0) {
			if(username.length() > 0) {
				titleMessage = "Password form is empty.";
				return;
			} else if(password.length() > 0) {
				titleMessage = "Username form is empty.";
				return;
			}
			titleMessage = "You have to input username\nand password to play.";
			return;
		}
		SignLink.errorName = username;
		try {
			TitleActivity.scrollOpened = false;
			while(TitleActivity.scrollValue < 110) {
				titleActivity.update();
			}
			socketStream = new Session(this, openSocket(TitleActivity.CONNECTIONS[TitleActivity.connection].getPort()));
			final long nameAsLong = StringUtils.encryptName(username);
			final int i = (int) (nameAsLong >> 16 & 31L);
			outBuffer.pos = 0;
			outBuffer.putByte(14);
			outBuffer.putByte(i);
			socketStream.write(outBuffer.data, 2);
			for(int j = 0; j < 8; j++) {
				socketStream.read();
			}
			int returnCode = socketStream.read();
			final int i1 = returnCode;
			if(returnCode == 0) {
				socketStream.read(inBuffer.data, 8);
				inBuffer.pos = 0;
				aLong1215 = inBuffer.getLong();
				final int ai[] = new int[4];
				ai[0] = (int) (Math.random() * 99999999D);
				ai[1] = (int) (Math.random() * 99999999D);
				ai[2] = (int) (aLong1215 >> 32);
				ai[3] = (int) aLong1215;
				outBuffer.pos = 0;
				outBuffer.putByte(10);
				outBuffer.putInt(ai[0]);
				outBuffer.putInt(ai[1]);
				outBuffer.putInt(ai[2]);
				outBuffer.putInt(ai[3]);
				outBuffer.putInt(999999);
				outBuffer.putLine(username);
				outBuffer.putLine(password);
				outBuffer.doKeys();
				outStream.pos = 0;
				outStream.putByte(16);
				outStream.putByte(outBuffer.pos + 36 + 1 + 1 + 2);
				outStream.putByte(255);
				outStream.putShort(Constants.BUILD);
				outStream.putByte(1);
				for(int l1 = 0; l1 < 9; l1++) {
					outStream.putInt(CacheUnpacker.EXPECTED_CRC[l1]);
				}
				outStream.putBytes(outBuffer.data, 0, outBuffer.pos);
				outBuffer.cipher = new ISAACCipher(ai);
				for(int j2 = 0; j2 < 4; j2++) {
					ai[j2] += 50;
				}
				encryption = new ISAACCipher(ai);
				socketStream.write(outStream.data, outStream.pos);
				returnCode = socketStream.read();
				titleMessage = "";
			} else {
				TitleActivity.scrollOpened = true;
			}
			if(returnCode == 1) {
				try {
					Thread.sleep(2000L);
				} catch(final Exception _ex) {
				}
				connect(username, password);
				return;
			}
			if(returnCode == 2) {
				localPrivilege = socketStream.read();
				//flagged = socketStream.read() == 1;
				titleMessage = "";
				taskHandler = new TaskHandler(this);
				aLong1220 = 0L;
				anInt1022 = 0;
				mouseDetection = new MouseTracker(this);
				mouseDetection.index = 0;
				super.awtFocus = true;
				aBoolean954 = true;
				loggedIn = true;
				updateWindow();
				outBuffer.pos = 0;
				inBuffer.pos = 0;
				pktType = -1;
				anInt841 = -1;
				anInt842 = -1;
				anInt843 = -1;
				pktSize = 0;
				systemUpdateTimer = 0;
				anInt1011 = 0;
				hintType = 0;
				menuPos = 0;
				menuOpened = false;
				super.idleTime = 0;
				for(int j1 = 0; j1 < 500; j1++) {
					chatMessage[j1] = null;
				}
				itemSelected = 0;
				spellSelected = 0;
				loadingStage = 0;
				if(Constants.ANTI_BOT_ENABLED) {
					cameraAdjustX = (int) (Math.random() * 100D) - 50;
					cameraAdjustY = (int) (Math.random() * 110D) - 55;
					cameraAngleAdjustX = (int) (Math.random() * 80D) - 40;
					minimapAngle = (int) (Math.random() * 120D) - 60;
					minimapZoom = (int) (Math.random() * 30D) - 20;
					cameraAngleX = (int) (Math.random() * 20D) - 10 & 0x7ff;
				} else {
					cameraAdjustX = 0;
					cameraAdjustY = 0;
					cameraAngleAdjustX = 0;
					minimapAngle = 0;
					minimapZoom = 0;
					cameraAngleX = 0;
				}
				minimapOverlay = 0;
				planeReq = -1;
				walkX = 0;
				walkY = 0;
				playerCount = 0;
				npcListSize = 0;
				for(int i2 = 0; i2 < maxPlayers; i2++) {
					playerList[i2] = null;
					playerBuffer[i2] = null;
				}
				for(int k2 = 0; k2 < 16384; k2++) {
					npcList[k2] = null;
				}
				localPlayer = playerList[localPlayerIndex] = new Player();
				aClass19_1013.clear();
				aClass19_1056.clear();
				for(int plane = 0; plane < 4; plane++) {
					for(int x = 0; x < 104; x++) {
						for(int y = 0; y < 104; y++) {
							sceneItems[plane][x][y] = null;
						}
					}
				}
				aClass19_1179 = new LinkedDeque();
				fullscreenWidgetId = -1;
				anInt900 = 0;
				friendsCount = 0;
				chatWidgetId = -1;
				forcedChatWidgetId = -1;
				openInterfaceID = -1;
				invOverlayInterfaceID = -1;
				anInt1018 = -1;
				aBoolean1149 = false;
				invTab = 3;
				inputDialogState = 0;
				menuOpened = false;
				messagePromptRaised = false;
				chatBoxStatement = null;
				combatMultiwayMode = 0;
				anInt1054 = -1;
				aBoolean1047 = true;
				method45();
				for(int j3 = 0; j3 < 5; j3++) {
					anIntArray990[j3] = 0;
				}
				for(int l3 = 0; l3 < 5; l3++) {
					atPlayerActions[l3] = null;
					atPlayerArray[l3] = false;
				}
				pkt200Count = 0;
				pkt152Count = 0;
				pkt189Count = 0;
				pkt36Count = 0;
				pkt183Count = 0;
				pkt136Count = 0;
				pkt85Count = 0;
				titleActivity.reset();
				if(gameActivity == null) {
					gameActivity = new GameActivity();
				}
				gameActivity.initialize();
				if(uiRenderer.getId() < 562) {
					invTab = 3;
				} else {
					invTab = 4;
				}
				return;
			}
			/*if(returnCode == 15) {
				inLobby = false;
				loggedIn = true;
				outBuffer.pos = 0;
				inBuffer.pos = 0;
				pktType = -1;
				anInt841 = -1;
				anInt842 = -1;
				anInt843 = -1;
				pktSize = 0;
				systemUpdateTimer = 0;
				menuPos = 0;
				menuOpened = false;
				aLong824 = System.currentTimeMillis();
				return;
			}*/
			TitleActivity.scrollOpened = true;
			if(returnCode == 3) {
				titleMessage = "Invalid username or password.";
				return;
			}
			if(returnCode == 4) {
				titleMessage = "Your account has been disabled.\nPlease check your message-center for details.";
				return;
			}
			if(returnCode == 5) {
				titleMessage = "Your account is already logged in.\nTry again in 60 secs...";
				return;
			}
			if(returnCode == 6) {
				titleMessage = "Edgeville has been updated!\nPlease reload this page.";
				return;
			}
			if(returnCode == 7) {
				titleMessage = "This world is full.\nPlease use a different world.";
				return;
			}
			if(returnCode == 8) {
				titleMessage = "Unable to connect.\nLogin server offline.";
				return;
			}
			if(returnCode == 9) {
				titleMessage = "Login limit exceeded.\nToo many connections from your address.";
				return;
			}
			if(returnCode == 10) {
				titleMessage = "Unable to connect.\nBad session nodeId.";
				return;
			}
			if(returnCode == 11) {
				titleMessage = "Login server rejected session.\nPlease try again.";
				return;
			}
			if(returnCode == 12) {
				titleMessage = "You need a members account to login to this world.\nPlease subscribe, or use a different world.";
				return;
			}
			if(returnCode == 13) {
				titleMessage = "Could not complete login.\nPlease try using a different world.";
				return;
			}
			if(returnCode == 14) {
				titleMessage = "The server is currently updating.\nPlease wait a moment.";
				return;
			}
			if(returnCode == 16) {
				titleMessage = "Login attempts exceeded.\nPlease wait 1 minute and try again.";
				return;
			}
			if(returnCode == 17) {
				titleMessage = "You are standing in a members-only area.\nTo play on this world move to a free area first.";
				return;
			}
			if(returnCode == 18) {
				titleMessage = "You are currently running an outdated client version.\nPlease visit www.edgeville.net to get the newest version.";
				try {
					new Updater(this).run();
				} catch(Exception e) {
					e.printStackTrace();
				}
				return;
			}
			if(returnCode == 19) {
				titleMessage = "The server is currently starting up.\nPlease allow it a moment.";
				return;
			}
			if(returnCode == 20) {
				titleMessage = "Invalid loginserver requested.\nPlease try using a different world.";
				return;
			}
			if(returnCode == 21) {
				for(int k1 = socketStream.read(); k1 >= 0; k1--) {
					titleMessage = "You have only just left another world.\nYour profile will be transferred in: " + k1 + " seconds.";
					titleActivity.update();
					try {
						Thread.sleep(1000L);
					} catch(final Exception _ex) {
					}
				}

				connect(username, password);
				return;
			}
			if(returnCode == -1) {
				if(i1 == 0) {
					if(loginFailures < 2) {
						try {
							Thread.sleep(2000L);
						} catch(final Exception ignored) {
						}
						loginFailures++;
						connect(username, password);
					} else {
						TitleActivity.scrollOpened = true;
						titleMessage = "No response from loginserver.\nPlease wait 1 minute and try again.";
					}
				} else {
					TitleActivity.scrollOpened = true;
					titleMessage = "No response from server.\nPlease try using a different world.";
				}
			} else {
				TitleActivity.scrollOpened = true;
				titleMessage = "Unexpected server response.\nPlease try using a different world.";
			}
		} catch(final IOException e) {
			TitleActivity.scrollOpened = true;
			titleMessage = "Server is currently offline.\nPlease try later...";
		}
	}

	/*public long ipToLong(String ipAddress) {

		String[] ipAddressInArray = ipAddress.split("\\.");

		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {

			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);

		}

		return result;
	}*/

	public String longToIp(long ip) {
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}

	public Socket openSocket(int port) throws IOException {
		long ip = TitleActivity.CONNECTIONS[TitleActivity.connection].getIp();
		return new Socket(InetAddress.getByName(longToIp(ip)), port);
	}

	public void logOut() {
		try {
			if(socketStream != null) {
				socketStream.close();
			}
		} catch(final Exception _ex) {
		}
		socketStream = null;
		loggedIn = false;
		TitleActivity.scrollOpened = true;
		clearMemory();
		scene.clear();
		for(int i = 0; i < 4; i++) {
			collisionMaps[i].clear();
		}
		System.gc();
		taskHandler = null;
		updateWindow();
		ImageCache.reset();
		Texture.reset();
		gameActivity.reset();
		panelHandler.close();
		titleActivity.initialize();
	}

	public void dropClient() {
		if(anInt1011 > 0) {
			logOut();
			return;
		}
		gameGraphics.setCanvas();
		plainFont.drawLeftAlignedEffectString("Connection lost", 10, 25, 0xffffff, true);
		plainFont.drawLeftAlignedEffectString("Please wait - attempting to reestablish...", 10, 40, 0xffffff, true);
		if(uiRenderer.isResizableOrFull()) {
			gameGraphics.drawGraphics(0, 0, super.graphics);
		} else {
			gameGraphics.drawGraphics(0, 0, super.graphics);
		}
		minimapOverlay = 0;
		walkX = 0;
		final Session rsSocket = socketStream;
		loggedIn = false;
		loginFailures = 0;
		connect(localUsername, localPassword);
		if(!loggedIn) {
			logOut();
		}
		try {
			rsSocket.close();
		} catch(final Exception _ex) {
		}
	}

	/**
	 * Gets the document base host.
	 * @return string
	 */
	public String getDocumentBaseHost() {
		if(SignLink.mainApp != null) {
			return SignLink.mainApp.getDocumentBase().getHost().toLowerCase();
		}
		if(super.frame != null) {
			return ""; // Empty returnment statement.
		} else {
			return ""; // Empty returnment statement.
		}
	}

	public void clearTopInterfaces() {
		outBuffer.putOpcode(130);
		if(invOverlayInterfaceID != -1) {
			invOverlayInterfaceID = -1;
			aBoolean1149 = false;
			updateInventory = true;
		}
		if(forcedChatWidgetId != -1) {
			forcedChatWidgetId = -1;
			aBoolean1149 = false;
		}
		openInterfaceID = -1;
		fullscreenWidgetId = -1;
	}

	public void executeMenuAction(int item) {
		if(item < 0) {
			return;
		}
		if(inputDialogState != 0) {
			inputDialogState = 0;
		}
		final int arg1 = menuItemArg1[item];
		final int arg2 = menuItemArg2[item];
		final int arg3 = menuItemArg3[item];
		final long arg4 = menuItemArg4[item];
		int code = menuItemCode[item];
		if(code >= 2000) {
			code -= 2000;
		}
		if(code == 1051) {
			outBuffer.putOpcode(185);
			outBuffer.putShort(152);
		}
		if(code == 1052) {
			if(uiRenderer.getId() > 560)
				invTab = 16;
			else
				invTab = 10;
		}
		if(code == 1053) {
			outBuffer.putOpcode(185);
			outBuffer.putShort(48);
		}
		if(code == 1054) {
			outBuffer.putOpcode(185);
			outBuffer.putShort(49);
		}
		if(code == 582 && panelHandler.action()) {
			final NPC npc = npcList[arg1];
			if(npc != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, npc.smallY[0], localPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(57);
				outBuffer.putShortPlus128(anInt1285);
				outBuffer.putShortPlus128(arg1);
				outBuffer.putLitEndShort(anInt1283);
				outBuffer.putShortPlus128(anInt1284);
			}
		}
		if(code == 234 && panelHandler.action()) {
			boolean flag1 = findWalkingPath(2, 0, 0, 0, localPlayer.smallY[0], 0, 0, arg3, localPlayer.smallX[0], false, arg2);
			if(!flag1) {
				flag1 = findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, arg3, localPlayer.smallX[0], false, arg2);
			}
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			outBuffer.putOpcode(236);
			outBuffer.putLitEndShort(arg3 + baseY);
			outBuffer.putShort(arg1);
			outBuffer.putLitEndShort(arg2 + baseX);
		}
		if(code == 62 && panelHandler.action()) {
			if(clickLoc(arg4, arg3, arg2)) {
				outBuffer.putOpcode(192);
				outBuffer.putShort(anInt1284);
				outBuffer.putMedium((int) (arg4 >> 14 & 0xFFFFFF));
				outBuffer.putLitEndShortPlus128(arg3 + baseY);
				outBuffer.putLitEndShort(anInt1283);
				outBuffer.putLitEndShortPlus128(arg2 + baseX);
				outBuffer.putShort(anInt1285);
			}
		}
		if(code == 511 && panelHandler.action()) {
			boolean flag2 = findWalkingPath(2, 0, 0, 0, localPlayer.smallY[0], 0, 0, arg3, localPlayer.smallX[0], false, arg2);
			if(!flag2) {
				flag2 = findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, arg3, localPlayer.smallX[0], false, arg2);
			}
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			outBuffer.putOpcode(25);
			outBuffer.putLitEndShort(anInt1284);
			outBuffer.putShortPlus128(anInt1285);
			outBuffer.putShort(arg1);
			outBuffer.putShortPlus128(arg3 + baseY);
			outBuffer.putLitEndShortPlus128(anInt1283);
			outBuffer.putShort(arg2 + baseX);
		}
		if(code == 74) {
			outBuffer.putOpcode(122);
			outBuffer.putLitEndShortPlus128(arg3);
			outBuffer.putShortPlus128(arg2);
			outBuffer.putLitEndShort(arg1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 315) {
			final Interface class9 = Interface.cache[arg3];
			boolean flag8 = true;
			if(class9.contentType > 0) {
				flag8 = promptUserForInput(class9);
			}
			if(flag8) {
				switch(arg3) {
				/* Client-sided button clicking */
					case 25843:
						if(panelHandler.isSettingOpen())
							panelHandler.close();
						else {
							openInterfaceID = -1;
							panelHandler.open(new SettingPanel());
						}
						break;
					case 1668:
						sendFrame248(17500, 3213);
						break;
					default:
						outBuffer.putOpcode(185);
						outBuffer.putShort(arg3);
						break;
				}
			}
		}
		if(code == 561 && panelHandler.action()) {
			final Player player = playerList[arg1];
			if(player != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, player.smallY[0], localPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				pkt136Count += arg1;
				if(pkt136Count >= 90) {
					pkt136Count = 0;
				}
				outBuffer.putOpcode(128);
				outBuffer.putShort(arg1);
			}
		}
		if(code == 20 && panelHandler.action()) {
			final NPC npc = npcList[arg1];
			if(npc != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, npc.smallY[0], localPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(155);
				outBuffer.putLitEndShort(arg1);
			}
		}
		if(code == 779 && panelHandler.action()) {
			final Player player = playerList[arg1];
			if(player != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, player.smallY[0], localPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(153);
				outBuffer.putLitEndShort(arg1);
			}
		}
		if(code == 516) {
			int x = (!menuOpened ? super.clickX : arg2) - Rasterizer3D.viewport.getX();
			int y = (!menuOpened ? super.clickY : arg3) - Rasterizer3D.viewport.getY();
			scene.click(x, y);
		}
		if(code == 1062) {
			pkt183Count += baseX;
			clickLoc(arg4, arg3, arg2);
			outBuffer.putOpcode(228);
			outBuffer.putMedium((int) (arg4 >> 14 & 0xFFFFFF));
			outBuffer.putShort(arg2 + baseX);
			outBuffer.putShort(arg3 + baseY);
		}
		if(code == 679 && !aBoolean1149) {
			outBuffer.putOpcode(40);
			aBoolean1149 = true;
		}
		if(code == 431) {
			outBuffer.putOpcode(129);
			outBuffer.putShortPlus128(arg2);
			outBuffer.putShort(arg3);
			outBuffer.putShortPlus128(arg1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 337 || code == 42 || code == 792 || code == 322) {
			final String s = menuItemName[item];
			final int k1 = s.indexOf("@whi@");
			if(k1 != -1) {
				final long l3 = StringUtils.encryptName(s.substring(k1 + 5).trim());
				if(code == 337) {
					addFriend(l3);
				}
				if(code == 42) {
					addIgnore(l3);
				}
				if(code == 792) {
					delFriend(l3);
				}
				if(code == 322) {
					delIgnore(l3);
				}
			}
		}
		if(code == 53) {
			outBuffer.putOpcode(135);
			outBuffer.putLitEndShort(arg2);
			outBuffer.putShortPlus128(arg3);
			outBuffer.putLitEndShort(arg1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 539) {
			outBuffer.putOpcode(16);
			outBuffer.putShortPlus128(arg1);
			outBuffer.putLitEndShortPlus128(arg2);
			outBuffer.putLitEndShortPlus128(arg3);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 484 || code == 6) {
			String s1 = menuItemName[item];
			final int l1 = s1.indexOf("@whi@");
			if(l1 != -1) {
				s1 = s1.substring(l1 + 5).trim();
				final String s7 = StringUtils.formatName(StringUtils.decryptName(StringUtils.encryptName(s1)));
				boolean flag9 = false;
				for(int j3 = 0; j3 < playerCount; j3++) {
					final Player class30_sub2_sub4_sub1_sub2_7 = playerList[playerEntryList[j3]];
					if(class30_sub2_sub4_sub1_sub2_7 == null || class30_sub2_sub4_sub1_sub2_7.name == null || !class30_sub2_sub4_sub1_sub2_7.name.equalsIgnoreCase(s7)) {
						continue;
					}
					findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_7.smallY[0], localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_7.smallX[0]);
					if(code == 484) {
						outBuffer.putOpcode(139);
						outBuffer.putLitEndShort(playerEntryList[j3]);
					}
					if(code == 6) {
						pkt136Count += arg1;
						if(pkt136Count >= 90) {
							pkt136Count = 0;
						}
						outBuffer.putOpcode(128);
						outBuffer.putShort(playerEntryList[j3]);
					}
					flag9 = true;
					break;
				}

				if(!flag9) {
					pushMessage("Unable to find " + s7, 0, "");
				}
			}
		}
		if(code == 870) {
			outBuffer.putOpcode(53);
			outBuffer.putShort(arg2);
			outBuffer.putShortPlus128(anInt1283);
			outBuffer.putLitEndShortPlus128(arg1);
			outBuffer.putShort(anInt1284);
			outBuffer.putLitEndShort(anInt1285);
			outBuffer.putShort(arg3);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 847) {
			outBuffer.putOpcode(87);
			outBuffer.putShortPlus128(arg1);
			outBuffer.putShort(arg3);
			outBuffer.putShortPlus128(arg2);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 626) {
			final Interface class9_1 = Interface.cache[arg3];
			spellSelected = 1;
			spellId = class9_1.id;
			anInt1137 = arg3;
			spellUsableOn = class9_1.spellUsableOn;
			itemSelected = 0;
			String s4 = class9_1.selectedActionName;
			if(s4.indexOf(" ") != -1) {
				s4 = s4.substring(0, s4.indexOf(" "));
			}
			String s8 = class9_1.selectedActionName;
			if(s8.indexOf(" ") != -1) {
				s8 = s8.substring(s8.indexOf(" ") + 1);
			}
			spellTooltip = s4 + " " + class9_1.spellName + " " + s8;
			if(spellUsableOn == 16) {
				if(uiRenderer.getId() == 562)
					invTab = 4;
				else
					invTab = 3;
				updateInventory = true;
			}
			return;
		}
		if(code == 78) {
			outBuffer.putOpcode(117);
			outBuffer.putLitEndShortPlus128(arg3);
			outBuffer.putLitEndShortPlus128(arg1);
			outBuffer.putLitEndShort(arg2);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 104) {
			Interface class9_1 = Interface.cache[arg3];
			spellId = class9_1.id;
			if(autocastId != class9_1.id) {
				autocastId = class9_1.id;
				outBuffer.putOpcode(185);
				outBuffer.putShort(class9_1.id);
			} else if(spellId != 0) {
				autocastId = class9_1.id;
				outBuffer.putOpcode(185);
				outBuffer.putShort(class9_1.id);
			}
		}
		if(code == 105) {
			Interface class9_1 = Interface.cache[arg3];
			spellId = class9_1.id;
			autocastId = 0;
			outBuffer.putOpcode(185);
			outBuffer.putShort(6666);
		}
		if(code == 27 && panelHandler.action()) {
			final Player player = playerList[arg1];
			if(player != null && !panelHandler.isShopOpen()) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, player.smallY[0], localPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				pkt189Count += arg1;
				if(pkt189Count >= 54) {
					pkt189Count = 0;
				}
				outBuffer.putOpcode(73);
				outBuffer.putLitEndShort(arg1);
			}
		}
		if(code == 213 && panelHandler.action()) {
			boolean flag3 = findWalkingPath(2, 0, 0, 0, localPlayer.smallY[0], 0, 0, arg3, localPlayer.smallX[0], false, arg2);
			if(!flag3) {
				flag3 = findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, arg3, localPlayer.smallX[0], false, arg2);
			}
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			outBuffer.putOpcode(79);
			outBuffer.putLitEndShort(arg3 + baseY);
			outBuffer.putShort(arg1);
			outBuffer.putShortPlus128(arg2 + baseX);
		}
		if(code == 632) {
			outBuffer.putOpcode(145);
			outBuffer.putShortPlus128(arg3);
			outBuffer.putShortPlus128(arg2);
			outBuffer.putShortPlus128(arg1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 1004) {
			if(olderTabInterfaces[10] != -1) {
				invTab = 10;
				updateInventory = true;
			}
		}
		if(code == 1003) {
			clanChatMode = 2;
		}
		if(code == 1002) {
			clanChatMode = 1;
		}
		if(code == 1001) {
			clanChatMode = 0;
		}
		if(code == 1000) {
			selectedChannelButton = 4;
			if(chatTypeView != Constants.MSG_CLAN) {
				chatTypeView = Constants.MSG_CLAN;
				showChat = true;
			} else {
				showChat = !showChat;
			}
		}
		if(code == 999) {
			selectedChannelButton = 0;
			if(chatTypeView != Constants.MSG_ALL) {
				chatTypeView = Constants.MSG_ALL;
				showChat = true;
			} else {
				showChat = !showChat;
			}
		}
		if(code == 998) {
			selectedChannelButton = 1;
			if(chatTypeView != Constants.MSG_GAME) {
				chatTypeView = Constants.MSG_GAME;
				showChat = true;
			} else {
				showChat = !showChat;
			}
		}
		if(code == 997) {
			publicChatMode = 3;
		}
		if(code == 996) {
			publicChatMode = 2;
		}
		if(code == 995) {
			publicChatMode = 1;
		}
		if(code == 994) {
			publicChatMode = 0;
		}
		if(code == 993) {
			selectedChannelButton = 2;
			if(chatTypeView != Constants.MSG_PUBLIC) {
				chatTypeView = Constants.MSG_PUBLIC;
				showChat = true;
			} else {
				showChat = !showChat;
			}
		}
		if(code == 992) {
			privateChatMode = 2;
		}
		if(code == 991) {
			privateChatMode = 1;
		}
		if(code == 990) {
			privateChatMode = 0;
		}
		if(code == 989) {
			selectedChannelButton = 3;
			if(chatTypeView != Constants.MSG_PRIVATE) {
				chatTypeView = Constants.MSG_PRIVATE;
				showChat = true;
			} else {
				showChat = !showChat;
			}
		}
		if(code == 987) {
			tradeMode = 2;
		}
		if(code == 986) {
			tradeMode = 1;
		}
		if(code == 985) {
			tradeMode = 0;
		}
		if(code == 984) {
			selectedChannelButton = 5;
			if(chatTypeView != Constants.MSG_REQUEST) {
				chatTypeView = Constants.MSG_REQUEST;
				showChat = true;
			} else {
				showChat = !showChat;
			}
		}
		if(code == 983) {
			duelMode = 2;
		}
		if(code == 982) {
			duelMode = 1;
		}
		if(code == 981) {
			duelMode = 0;
		}
		if(code == 1014) {
			cameraAngleX = 0;
			mapVerticalRotation = 120;
		}
		if(code == 493) {
			outBuffer.putOpcode(75);
			outBuffer.putLitEndShortPlus128(arg3);
			outBuffer.putLitEndShort(arg2);
			outBuffer.putShortPlus128(arg1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 652 && panelHandler.action()) {
			boolean flag4 = findWalkingPath(2, 0, 0, 0, localPlayer.smallY[0], 0, 0, arg3, localPlayer.smallX[0], false, arg2);
			if(!flag4) {
				flag4 = findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, arg3, localPlayer.smallX[0], false, arg2);
			}
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			outBuffer.putOpcode(156);
			outBuffer.putShortPlus128(arg2 + baseX);
			outBuffer.putLitEndShort(arg3 + baseY);
			outBuffer.putLitEndShortPlus128(arg1);
		}
		if(code == 94 && panelHandler.action()) {
			boolean flag5 = findWalkingPath(2, 0, 0, 0, localPlayer.smallY[0], 0, 0, arg3, localPlayer.smallX[0], false, arg2);
			if(!flag5) {
				flag5 = findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, arg3, localPlayer.smallX[0], false, arg2);
			}
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			outBuffer.putOpcode(181);
			outBuffer.putLitEndShort(arg3 + baseY);
			outBuffer.putShort(arg1);
			outBuffer.putLitEndShort(arg2 + baseX);
			outBuffer.putShortPlus128(anInt1137);
		}
		if(code == 647) {
			outBuffer.putOpcode(213);
			outBuffer.putInt(arg3);
			outBuffer.putInt(arg2);
			switch(arg3) {

			}
		}
		if(code == 646) {
			outBuffer.putOpcode(185);
			outBuffer.putShort(arg3);
			final Interface class9_2 = Interface.cache[arg3];
			if(class9_2.valueIndexArray != null && class9_2.valueIndexArray[0][0] == 5) {
				final int i2 = class9_2.valueIndexArray[0][1];
				if(variousSettings[i2] != class9_2.requiredValues[0]) {
					variousSettings[i2] = class9_2.requiredValues[0];
					handleSettings(i2);
				}
			}
			switch(arg3) {

			}
		}
		if(code == 225) {
			final NPC npc = npcList[arg1];
			if(npc != null && panelHandler.action()) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, npc.smallY[0], localPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(17);
				outBuffer.putLitEndShortPlus128(arg1);
			}
		}
		if(code == 965) {
			final NPC npc = npcList[arg1];
			if(npc != null && panelHandler.action()) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, npc.smallY[0], localPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				pkt152Count++;
				if(pkt152Count >= 96) {
					pkt152Count = 0;
				}
				outBuffer.putOpcode(21);
				outBuffer.putShort(arg1);
			}
		}
		if(code == 413) {
			final NPC npc = npcList[arg1];
			if(npc != null && panelHandler.action()) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, npc.smallY[0], localPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(131);
				outBuffer.putLitEndShortPlus128(arg1);
				outBuffer.putShortPlus128(anInt1137);
			}
		}
		if(code == 200) {
			clearTopInterfaces();
		}
		if(code == 1025) {
			final NPC npc = npcList[arg1];
			if(npc != null) {
				NPCType entityDef = npc.type;
				if(entityDef.childrenIDs != null) {
					entityDef = entityDef.getSubNPCType();
				}
				if(entityDef != null) {
					String s9;
					if(entityDef.description != null) {
						s9 = entityDef.description;
					} else {
						s9 = "It's a " + entityDef.name + ".";
					}
					pushMessage(s9, 0, "");
				}
			}
		}
		if(code == 1026) {
			outBuffer.putOpcode(134);
			outBuffer.putShort(arg1);
		}
		if(code == 900 && panelHandler.action()) {
			clickLoc(arg4, arg3, arg2);
			outBuffer.putOpcode(252);
			outBuffer.putMedium((int) (arg4 >> 14 & 0xFFFFFF));
			outBuffer.putShort(arg2 + baseX);
			outBuffer.putShort(arg3 + baseY);
		}
		if(code == 412 && panelHandler.action()) {
			final NPC npc = npcList[arg1];
			if(npc != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, npc.smallY[0], localPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(72);
				outBuffer.putShortPlus128(arg1);
			}
		}
		if(code == 365 && panelHandler.action()) {
			final Player player = playerList[arg1];
			if(player != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, player.smallY[0], localPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(249);
				outBuffer.putShortPlus128(arg1);
				outBuffer.putLitEndShort(anInt1137);
			}
		}
		if(code == 729 && panelHandler.action()) {
			final Player player = playerList[arg1];
			if(player != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, player.smallY[0], localPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(39);
				outBuffer.putLitEndShort(arg1);
			}
		}
		if(code == 577 && panelHandler.action()) {
			final Player player = playerList[arg1];
			if(player != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, player.smallY[0], localPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(139);
				outBuffer.putLitEndShort(arg1);
			}
		}
		if(code == 956 && panelHandler.action()) {
			if(clickLoc(arg4, arg3, arg2)) {
				outBuffer.putOpcode(35);
				outBuffer.putMedium((int) (arg4 >> 14 & 0xFFFFFF));
				outBuffer.putShort(arg2 + baseX);
				outBuffer.putShort(arg3 + baseY);
				outBuffer.putShort(anInt1137);
			}
		}
		if(code == 567 && panelHandler.action()) {
			boolean flag6 = findWalkingPath(2, 0, 0, 0, localPlayer.smallY[0], 0, 0, arg3, localPlayer.smallX[0], false, arg2);
			if(!flag6) {
				flag6 = findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, arg3, localPlayer.smallX[0], false, arg2);
			}
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			outBuffer.putOpcode(23);
			outBuffer.putLitEndShort(arg3 + baseY);
			outBuffer.putLitEndShort(arg1);
			outBuffer.putLitEndShort(arg2 + baseX);
		}
		if(code == 1900) {
			outBuffer.putOpcode(145);
			outBuffer.putShortPlus128(3900);
			outBuffer.putShortPlus128(arg3);
			outBuffer.putShortPlus128(arg1);
		}
		if(code == 867) {
			if((arg1 & 3) == 0) {
				pkt200Count++;
			}
			if(pkt200Count >= 59) {
				pkt200Count = 0;
			}
			outBuffer.putOpcode(43);
			outBuffer.putLitEndShort(arg3);
			outBuffer.putShortPlus128(arg1);
			outBuffer.putShortPlus128(arg2);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 543) {
			outBuffer.putOpcode(237);
			outBuffer.putShort(arg2);
			outBuffer.putShortPlus128(arg1);
			outBuffer.putShort(arg3);
			outBuffer.putShortPlus128(anInt1137);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 606) {
			final String s2 = menuItemName[item];
			final int j2 = s2.indexOf("@whi@");
			if(j2 != -1) {
				if(openInterfaceID == -1) {
					clearTopInterfaces();
					reportAbuseInput = s2.substring(j2 + 5).trim();
					canMute = false;
					for(final Interface element : Interface.cache) {
						if(element == null || element.contentType != 600) {
							continue;
						}
						reportAbuseInterfaceID = openInterfaceID = element.parent;
						break;
					}

				} else {
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
			}
		}
		if(code == 491 && panelHandler.action()) {
			final Player player = playerList[arg1];
			if(player != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, player.smallY[0], localPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				outBuffer.putOpcode(14);
				outBuffer.putShortPlus128(anInt1284);
				outBuffer.putShort(arg1);
				outBuffer.putShort(anInt1285);
				outBuffer.putLitEndShort(anInt1283);
			}
		}
		if(code == 639) {
			final String s3 = menuItemName[item];
			final int k2 = s3.indexOf("@whi@");
			if(k2 != -1) {
				final long l4 = StringUtils.encryptName(s3.substring(k2 + 5).trim());
				int k3 = -1;
				for(int i4 = 0; i4 < friendsCount; i4++) {
					if(friendsListAsLongs[i4] != l4) {
						continue;
					}
					k3 = i4;
					break;
				}

				if(k3 != -1 && friendsNodeIDs[k3] > 0) {
					inputDialogState = 0;
					messagePromptRaised = true;
					promptInput = "";
					friendsListAction = 3;
					aLong953 = friendsListAsLongs[k3];
					promptInputTitle = "Enter message to send to " + friendsList[k3];
				}
			}
		}
		if(code == 454) {
			outBuffer.putOpcode(41);
			outBuffer.putShort(arg1);
			outBuffer.putShortPlus128(arg2);
			outBuffer.putShortPlus128(arg3);
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			if(Interface.cache[arg3].parent == openInterfaceID) {
				atInventoryInterfaceType = 1;
			}
			if(Interface.cache[arg3].parent == forcedChatWidgetId) {
				atInventoryInterfaceType = 3;
			}
		}
		if(code == 478 && panelHandler.action()) {
			final NPC npc = npcList[arg1];
			if(npc != null) {
				findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, npc.smallY[0], localPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				if((arg1 & 3) == 0) {
					pkt85Count++;
				}
				if(pkt85Count >= 53) {
					pkt85Count = 0;
				}
				outBuffer.putOpcode(18);
				outBuffer.putLitEndShort(arg1);
			}
		}
		if(code == 113 && panelHandler.action()) {
			clickLoc(arg4, arg3, arg2);
			outBuffer.putOpcode(70);
			outBuffer.putMedium((int) (arg4 >> 14 & 0xFFFFFF));
			outBuffer.putShort(arg2 + baseX);
			outBuffer.putShort(arg3 + baseY);
		}
		if(code == 872 && panelHandler.action()) {
			clickLoc(arg4, arg3, arg2);
			outBuffer.putOpcode(234);
			outBuffer.putMedium((int) (arg4 >> 14 & 0xFFFFFF));
			outBuffer.putShort(arg2 + baseX);
			outBuffer.putShort(arg3 + baseY);
		}
		if(code == 502 && panelHandler.action()) {
			clickLoc(arg4, arg3, arg2);
			outBuffer.putOpcode(132);
			outBuffer.putMedium((int) (arg4 >> 14 & 0xFFFFFF));
			outBuffer.putShort(arg2 + baseX);
			outBuffer.putShort(arg3 + baseY);
		}
		if(code == 1125) {
			atInventoryLoopCycle = 0;
			atInventoryInterface = arg3;
			atInventoryIndex = arg2;
			atInventoryInterfaceType = 2;
			final ObjectType itemDef = ObjectType.get(arg1);
			final Interface class9_4 = Interface.cache[arg3];
			String s5;
			if(class9_4 != null && class9_4.invAmt[arg2] >= 0x186a0) {
				s5 = class9_4.invAmt[arg2] + " x " + itemDef.name;
			} else if(itemDef.description != null) {
				s5 = itemDef.description;
			} else {
				s5 = "It's a " + itemDef.name + ".";
			}
			pushMessage(s5, 0, "");
		}
		if(code == 169) {
			outBuffer.putOpcode(185);
			outBuffer.putShort(arg3);
			final Interface class9_3 = Interface.cache[arg3];
			if(class9_3.valueIndexArray != null && class9_3.valueIndexArray[0][0] == 5) {
				final int l2 = class9_3.valueIndexArray[0][1];
				variousSettings[l2] = 1 - variousSettings[l2];
				handleSettings(l2);
			}
		}
		if(code == 447) {
			itemSelected = 1;
			anInt1283 = arg2;
			anInt1284 = arg3;
			anInt1285 = arg1;
			selectedItemName = ObjectType.get(arg1).name;
			spellSelected = 0;
			return;
		}
		if(code == 1226) {
			final LocationType loc = LocationType.getPrecise(arg1);
			String s10;
			if(loc.description != null) {
				s10 = loc.description;
			} else {
				s10 = "It's a " + loc.name + ".";
			}
			pushMessage(s10, 0, "");
		}
		if(code == 244 && panelHandler.action()) {
			boolean flag7 = findWalkingPath(2, 0, 0, 0, localPlayer.smallY[0], 0, 0, arg3, localPlayer.smallX[0], false, arg2);
			if(!flag7) {
				flag7 = findWalkingPath(2, 0, 1, 0, localPlayer.smallY[0], 1, 0, arg3, localPlayer.smallX[0], false, arg2);
			}
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			outBuffer.putOpcode(253);
			outBuffer.putLitEndShort(arg2 + baseX);
			outBuffer.putLitEndShortPlus128(arg3 + baseY);
			outBuffer.putShortPlus128(arg1);
		}
		if(code == 1448) {
			final ObjectType itemDef_1 = ObjectType.get(arg1);
			String s6;
			if(itemDef_1.description != null) {
				s6 = itemDef_1.description;
			} else {
				s6 = "It's a " + itemDef_1.name + ".";
			}
			pushMessage(s6, 0, "");
		}
		itemSelected = 0;
		spellSelected = 0;
	}

	public boolean findWalkingPath(int i, int j, int k, int i1, int origY, int k1, int l1, int destY, int origX, boolean flag, int destX) {
		//if(panelHandler.isShopOpen())
		//	return false;
		final byte sizeX = 104;
		final byte sizeY = 104;
		for(int x = 0; x < sizeX; x++) {
			for(int y = 0; y < sizeY; y++) {
				walkingPathFrom[x][y] = 0;
				walkingPathCost[x][y] = 0x5f5e0ff;
			}
		}
		int x = origX;
		int y = origY;
		walkingPathFrom[origX][origY] = 99;
		walkingPathCost[origX][origY] = 0;
		int pnt = 0;
		int point = 0;
		bigX[pnt] = origX;
		bigY[pnt++] = origY;
		boolean found = false;
		final int nPoints = bigX.length;
		final int flagMap[][] = collisionMaps[cameraPlane].flagMap;
		while(point != pnt) {
			x = bigX[point];
			y = bigY[point];
			point = (point + 1) % nPoints;
			if(x == destX && y == destY) {
				found = true;
				break;
			}
			if(i1 != 0) {
				if((i1 < 5 || i1 == 10) && collisionMaps[cameraPlane].method219(x, y, destX, destY, j, i1 - 1)) {
					found = true;
					break;
				}
				if(i1 < 10 && collisionMaps[cameraPlane].method220(destX, destY, y, i1 - 1, j, x)) {
					found = true;
					break;
				}
			}
			if(k1 != 0 && k != 0 && collisionMaps[cameraPlane].method221(destY, destX, x, k, l1, k1, y)) {
				found = true;
				break;
			}
			final int cost = walkingPathCost[x][y] + 1;
			if(x > 0 && walkingPathFrom[x - 1][y] == 0 && ((flagMap[x - 1][y] & 0x1280108) == 0 || noclip)) {
				bigX[pnt] = x - 1;
				bigY[pnt] = y;
				pnt = (pnt + 1) % nPoints;
				walkingPathFrom[x - 1][y] = 2;
				walkingPathCost[x - 1][y] = cost;
			}
			if(x < sizeX - 1 && walkingPathFrom[x + 1][y] == 0 && ((flagMap[x + 1][y] & 0x1280180) == 0 || noclip)) {
				bigX[pnt] = x + 1;
				bigY[pnt] = y;
				pnt = (pnt + 1) % nPoints;
				walkingPathFrom[x + 1][y] = 8;
				walkingPathCost[x + 1][y] = cost;
			}
			if(y > 0 && walkingPathFrom[x][y - 1] == 0 && ((flagMap[x][y - 1] & 0x1280102) == 0 || noclip)) {
				bigX[pnt] = x;
				bigY[pnt] = y - 1;
				pnt = (pnt + 1) % nPoints;
				walkingPathFrom[x][y - 1] = 1;
				walkingPathCost[x][y - 1] = cost;
			}
			if(y < sizeY - 1 && walkingPathFrom[x][y + 1] == 0 && ((flagMap[x][y + 1] & 0x1280120) == 0 || noclip)) {
				bigX[pnt] = x;
				bigY[pnt] = y + 1;
				pnt = (pnt + 1) % nPoints;
				walkingPathFrom[x][y + 1] = 4;
				walkingPathCost[x][y + 1] = cost;
			}
			if(x > 0 && y > 0 && walkingPathFrom[x - 1][y - 1] == 0 && ((flagMap[x - 1][y - 1] & 0x128010e) == 0 && (flagMap[x - 1][y] & 0x1280108) == 0 && (flagMap[x][y - 1] & 0x1280102) == 0 || noclip)) {
				bigX[pnt] = x - 1;
				bigY[pnt] = y - 1;
				pnt = (pnt + 1) % nPoints;
				walkingPathFrom[x - 1][y - 1] = 3;
				walkingPathCost[x - 1][y - 1] = cost;
			}
			if(x < sizeX - 1 && y > 0 && walkingPathFrom[x + 1][y - 1] == 0 && ((flagMap[x + 1][y - 1] & 0x1280183) == 0 && (flagMap[x + 1][y] & 0x1280180) == 0 && (flagMap[x][y - 1] & 0x1280102) == 0 || noclip)) {
				bigX[pnt] = x + 1;
				bigY[pnt] = y - 1;
				pnt = (pnt + 1) % nPoints;
				walkingPathFrom[x + 1][y - 1] = 9;
				walkingPathCost[x + 1][y - 1] = cost;
			}
			if(x > 0 && y < sizeY - 1 && walkingPathFrom[x - 1][y + 1] == 0 && ((flagMap[x - 1][y + 1] & 0x1280138) == 0 && (flagMap[x - 1][y] & 0x1280108) == 0 && (flagMap[x][y + 1] & 0x1280120) == 0 || noclip)) {
				bigX[pnt] = x - 1;
				bigY[pnt] = y + 1;
				pnt = (pnt + 1) % nPoints;
				walkingPathFrom[x - 1][y + 1] = 6;
				walkingPathCost[x - 1][y + 1] = cost;
			}
			if(x < sizeX - 1 && y < sizeY - 1 && walkingPathFrom[x + 1][y + 1] == 0 && ((flagMap[x + 1][y + 1] & 0x12801e0) == 0 && (flagMap[x + 1][y] & 0x1280180) == 0 && (flagMap[x][y + 1] & 0x1280120) == 0 || noclip)) {
				bigX[pnt] = x + 1;
				bigY[pnt] = y + 1;
				pnt = (pnt + 1) % nPoints;
				walkingPathFrom[x + 1][y + 1] = 12;
				walkingPathCost[x + 1][y + 1] = cost;
			}
		}
		anInt1264 = 0;
		if(!found) {
			if(flag) {
				int i5 = 100;
				for(int k5 = 1; k5 < 2; k5++) {
					for(int i6 = destX - k5; i6 <= destX + k5; i6++) {
						for(int l6 = destY - k5; l6 <= destY + k5; l6++) {
							if(i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && walkingPathCost[i6][l6] < i5) {
								i5 = walkingPathCost[i6][l6];
								x = i6;
								y = l6;
								anInt1264 = 1;
								found = true;
							}
						}
					}
					if(found) {
						break;
					}
				}

			}
			if(!found) {
				return false;
			}
		}
		point = 0;
		bigX[point] = x;
		bigY[point++] = y;
		int l5;
		for(int j5 = l5 = walkingPathFrom[x][y]; x != origX || y != origY; j5 = walkingPathFrom[x][y]) {
			if(j5 != l5) {
				l5 = j5;
				bigX[point] = x;
				bigY[point++] = y;
			}
			if((j5 & 2) != 0) {
				x++;
			} else if((j5 & 8) != 0) {
				x--;
			}
			if((j5 & 1) != 0) {
				y++;
			} else if((j5 & 4) != 0) {
				y--;
			}
		}
		// if(cancelWalk) { return i4 > 0; }

		if(point > 0) {
			int k4 = point;
			if(k4 > 25) {
				k4 = 25;
			}
			point--;
			final int firstX = bigX[point];
			final int firstY = bigY[point];
			pkt36Count += k4;
			if(pkt36Count >= 92) {
				pkt36Count = 0;
			}
			if(i == 0) {
				outBuffer.putOpcode(164);
				outBuffer.putByte(k4 + k4 + 3);
			}
			if(i == 1) {
				outBuffer.putOpcode(248);
				outBuffer.putByte(k4 + k4 + 3 + 14);
			}
			if(i == 2) {
				outBuffer.putOpcode(98);
				outBuffer.putByte(k4 + k4 + 3);
			}
			outBuffer.putLitEndShortPlus128(firstX + baseX);
			walkX = bigX[0];
			walkY = bigY[0];
			for(int j7 = 1; j7 < k4; j7++) {
				point--;
				outBuffer.putByte(bigX[point] - firstX);
				outBuffer.putByte(bigY[point] - firstY);
			}
			outBuffer.putLitEndShort(firstY + baseY);
			outBuffer.putOppositeByte(super.keyPressed[5] != 1 ? 0 : 1);
			return true;
		}
		return i != 1;
	}


	public boolean isFriendOrSelf(String s) {
		if(s == null) {
			return false;
		}
		for(int i = 0; i < friendsCount; i++) {
			if(s.equalsIgnoreCase(friendsList[i])) {
				return true;
			}
		}
		return s.equalsIgnoreCase(localPlayer.name);
	}

	public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
		return super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1 && super.mouseY <= y2;
	}

	public boolean leftClickInRegion(int x1, int y1, int x2, int y2) {
		return super.clickButton == 1 && super.clickX >= x1 && super.clickX <= x2 && super.clickY >= y1 && super.clickY <= y2;
	}

	public boolean rightClickInRegion(int x1, int y1, int x2, int y2) {
		return super.clickButton == 2 && super.clickX >= x1 && super.clickX <= x2 && super.clickY >= y1 && super.clickY <= y2;
	}

	public void pushMessage(String msg, int type, String author) {
		if(type == 0 && chatWidgetId != -1) {
			chatBoxStatement = msg;
			super.clickButton = 0;
		}
		for(int i = 499; i > 0; i--) {
			chatType[i] = chatType[i - 1];
			chatAuthor[i] = chatAuthor[i - 1];
			chatMessage[i] = chatMessage[i - 1];
		}
		chatType[0] = type;
		chatAuthor[0] = author;
		chatMessage[0] = msg;
	}

	public void randomizeBackground(PaletteImage background) {
		final int j = 256;
		for(int k = 0; k < anIntArray1190.length; k++) {
			anIntArray1190[k] = 0;
		}

		for(int l = 0; l < 5000; l++) {
			final int i1 = (int) (Math.random() * 128D * j);
			anIntArray1190[i1] = (int) (Math.random() * 256D);
		}
		for(int j1 = 0; j1 < 20; j1++) {
			for(int k1 = 1; k1 < j - 1; k1++) {
				for(int i2 = 1; i2 < 127; i2++) {
					final int k2 = i2 + (k1 << 7);
					anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128] + anIntArray1190[k2 + 128]) / 4;
				}
			}
			final int ai[] = anIntArray1190;
			anIntArray1190 = anIntArray1191;
			anIntArray1191 = ai;
		}
		if(background != null) {
			int l1 = 0;
			for(int j2 = 0; j2 < background.trueHeight; j2++) {
				for(int l2 = 0; l2 < background.trueWidth; l2++) {
					if(background.entryList[l1++] != 0) {
						final int i3 = l2 + 16 + background.offsetX;
						final int j3 = j2 + 16 + background.offsetY;
						final int k3 = i3 + (j3 << 7);
						anIntArray1190[k3] = 0;
					}
				}
			}
		}
	}

	public void drawFadeTransition() {
		if(fadeOutEndCycle < loopCycle) {
			return;
		}
		int alpha;
		if(fadeInEndCycle >= loopCycle) {
			alpha = (loopCycle - fadeInStartCycle << 8) / (fadeInEndCycle - fadeInStartCycle);
		} else if(hiddenEndCycle >= loopCycle) {
			alpha = 255;
		} else {
			alpha = 255 - (loopCycle - hiddenEndCycle << 8) / (fadeOutEndCycle - hiddenEndCycle);
		}
		Rasterizer2D.fillRectangle(0, 0, Rasterizer2D.clipEndX, Rasterizer2D.clipEndY, 0, alpha);
	}

	public boolean parsePacket() {
		if(socketStream == null) {
			return false;
		}
		try {
			int i = socketStream.available();
			if(i == 0) {
				return false;
			}
			if(pktType == -1) {
				socketStream.read(inBuffer.data, 1);
				pktType = inBuffer.data[0] & 0xff;
				if(encryption != null) {
					pktType = pktType - encryption.nextInt() & 0xff;
				}
				pktSize = Constants.PACKET_SIZE[pktType];
				i--;
			}
			if(pktSize == -1) {
				if(i > 0) {
					socketStream.read(inBuffer.data, 1);
					pktSize = inBuffer.data[0] & 0xff;
					i--;
				} else {
					return false;
				}
			}
			if(pktSize == -2) {
				if(i > 1) {
					socketStream.read(inBuffer.data, 2);
					inBuffer.pos = 0;
					pktSize = inBuffer.getUShort();
					i -= 2;
				} else {
					return false;
				}
			}
			if(i < pktSize) {
				return false;
			}
			inBuffer.pos = 0;
			socketStream.read(inBuffer.data, pktSize);
			anInt843 = anInt842;
			anInt842 = anInt841;
			anInt841 = pktType;
			switch(pktType) {

				case 82:
					int inter = inBuffer.getInt();
					int content = inBuffer.getInt();
					Interface.cache[inter].modelId = content;
					pktType = -1;
					return true;

				case 80: // fade transition
					fadeInStartCycle = loopCycle - 1;
					fadeInEndCycle = fadeInStartCycle + inBuffer.getUByte();
					hiddenEndCycle = fadeInEndCycle + inBuffer.getUByte();
					fadeOutEndCycle = hiddenEndCycle + inBuffer.getUByte();
					pktType = -1;
					return true;

				case 81:
					updatePlayers(pktSize, inBuffer);
					regionLoaded = false;
					pktType = -1;
					return true;

				case 176:
					daysSinceRecovChange = inBuffer.getOppositeUByte();
					unreadMessages = inBuffer.getUShortMinus128();
					membersInt = inBuffer.getUByte();
					anInt1193 = inBuffer.getMidEndInt();
					daysSinceLastLogin = inBuffer.getUShort();
					if(anInt1193 != 0 && openInterfaceID == -1) {
						SignLink.dnsLookUp(StringUtils.intToByteValueString(anInt1193));
						clearTopInterfaces();
						char c = '\u028A';
						if(daysSinceRecovChange != 201 || membersInt == 1) {
							c = '\u028F';
						}
						reportAbuseInput = "";
						canMute = false;
						for(final Interface element : Interface.cache) {
							if(element == null || element.contentType != c) {
								continue;
							}
							openInterfaceID = element.parent;

						}
					}
					pktType = -1;
					return true;

				case 64:
					anInt1268 = inBuffer.getUByte();
					anInt1269 = inBuffer.getUByte();
					for(int j = anInt1268; j < anInt1268 + 8; j++) {
						for(int l9 = anInt1269; l9 < anInt1269 + 8; l9++) {
							if(sceneItems[cameraPlane][j][l9] != null) {
								sceneItems[cameraPlane][j][l9] = null;
								spawnGroundItem(j, l9);
							}
						}
					}
					for(WorldObjectSpawn class30_sub1 = (WorldObjectSpawn) aClass19_1179.getFirst(); class30_sub1 != null; class30_sub1 = (WorldObjectSpawn) aClass19_1179.getNext()) {
						if(class30_sub1.anInt1297 >= anInt1268 && class30_sub1.anInt1297 < anInt1268 + 8 && class30_sub1.anInt1298 >= anInt1269 && class30_sub1.anInt1298 < anInt1269 + 8 && class30_sub1.anInt1295 == cameraPlane) {
							class30_sub1.anInt1294 = 0;
						}
					}
					pktType = -1;
					return true;

				case 185:
					final int id = inBuffer.getLitEndUShortMinus128();
					Interface.cache[id].modelType = 3;
					if(localPlayer.desc == null) {
						Interface.cache[id].modelId = (localPlayer.characterParts[0] << 25) + (localPlayer.characterParts[4] << 20) + (localPlayer.equipment[0] << 15) + (localPlayer.equipment[8] << 10) + (localPlayer.equipment[11] << 5) + localPlayer.equipment[1];
					} else {
						Interface.cache[id].modelId = (int) (0x12345678L + localPlayer.desc.id);
					}
					pktType = -1;
					return true;

				/* Clan message packet */
				case 217:
					try {
						String author = inBuffer.getLine();
						String msg = inBuffer.getLine();
						final String clan = inBuffer.getLine();
						final int rights = inBuffer.getUShort();
						author = clan + ":" + author;
						if(rights >= 1) {
							author = "@cr" + rights + "@" + author;
						}
						msg = StringEncoder.processInput(msg);
						pushMessage(msg, 7, author);
					} catch(final Exception e) {
						e.printStackTrace();
					}
					pktType = -1;
					return true;

				case 107:
					forcedCameraLocation = false;
					for(int l = 0; l < 5; l++) {
						aBooleanArray876[l] = false;
					}
					pktType = -1;
					return true;

				case 72:
					final int i1 = inBuffer.getLitEndUShort();
					final Interface class9 = Interface.cache[i1];
					for(int k15 = 0; k15 < class9.invId.length; k15++) {
						class9.invId[k15] = -1;
						class9.invId[k15] = 0;
					}
					pktType = -1;
					return true;

				case 214:
					ignoreCount = pktSize / 8;
					for(int j1 = 0; j1 < ignoreCount; j1++) {
						ignoreListAsLongs[j1] = inBuffer.getLong();
					}
					pktType = -1;
					return true;

				case 166:
					forcedCameraLocation = true;
					forcedCameraTileX = inBuffer.getUByte();
					forcedCameraTileY = inBuffer.getUByte();
					forcedCameraPixelZ = inBuffer.getUShort();
					anInt1101 = inBuffer.getUByte();
					anInt1102 = inBuffer.getUByte();
					if(anInt1102 >= 100) {
						cameraLocationX = forcedCameraTileX * 128 + 64;
						cameraLocationY = forcedCameraTileY * 128 + 64;
						cameraLocationZ = method42(cameraPlane, cameraLocationX, cameraLocationY) - forcedCameraPixelZ;
					}
					pktType = -1;
					return true;

				case 134:
					final int skill = inBuffer.getUByte();
					final int exp = inBuffer.getMixEndInt();
					final int level = inBuffer.getInt();
					currentExp[skill] = exp;
					currentStats[skill] = level;
					maxStats[skill] = 1;
					for(int k20 = 0; k20 < 98; k20++) {
						if(exp >= EXP_FOR_LEVEL[k20]) {
							maxStats[skill] = k20 + 2;
						}
					}
					OrbHandler.updateOrbs(skill);
					pktType = -1;
					return true;

				case 135:
					int skillId = inBuffer.getUByte();
					int goal = inBuffer.getUByte();
					if(goal == currentStats[skillId] + 1)
						goal = 0;
					currentStatGoals[skillId] = goal;
					pktType = -1;
					return true;

				case 71:
					int interfaceId = inBuffer.getUShort();
					int tabOld = inBuffer.getReversedOppositeUByte();
					int tabNew = inBuffer.getReversedOppositeUByte();
					if(interfaceId == 65535) {
						interfaceId = -1;
					}
					olderTabInterfaces[tabOld] = interfaceId;
					newerTabInterfaces[tabNew] = interfaceId;
					
					if(uiRenderer.getId() == 562) {
						if(interfaceId == -1 && invTab == tabNew) {
							invTab = 4;
						}
					} else {
						if(interfaceId == -1 && invTab == tabOld) {
							invTab = 3;
						}
					}
					updateInventory = true;
					pktType = -1;
					return true;

				case 109:
					logOut();
					pktType = -1;
					return false;

				case 70:
					final int k2 = inBuffer.getSShort();
					final int l10 = inBuffer.getLitEndSShort();
					final int i16 = inBuffer.getLitEndUShort();
					if(Interface.cache[i16] == null) {
						pktType = -1;
						return true;
					}
					final Interface class9_5 = Interface.cache[i16];
					class9_5.offsetX = k2;
					class9_5.offsetY = l10;
					pktType = -1;
					return true;

				case 73:
				case 241:
					int l2 = regionX;
					int i11 = regionY;
					if(pktType == 73) {
						l2 = inBuffer.getUShortMinus128();
						i11 = inBuffer.getUShort();
						loadGeneratedMap = false;
					}
					if(pktType == 241) {
						l2 = inBuffer.getUShortMinus128();
						i11 = inBuffer.getUShort();
						for (int z = 0; z < 4; z++) {
							for (int x = 0; x < 13; x++) {
								for (int y = 0; y < 13; y++) {
									int i26 = inBuffer.getUByte();
									if (i26 == 5) {
										int val = inBuffer.getInt();
										constructRegionData[z][x][y] = val;
									} else {
										constructRegionData[z][x][y] = -1;
									}
								}
							}
						}
						loadGeneratedMap = true;
					}
					if(regionX == l2 && regionY == i11 && loadingStage == 2) {
						pktType = -1;
						loadRegion();
						return true;
					}
					regionX = l2;
					regionY = i11;
					baseX = (regionX - 6) << 3;
					baseY = (regionY - 6) << 3;
					inTutorialIsland = (regionX / 8 == 48 || regionX / 8 == 49) && regionY / 8 == 48;
					if(regionX / 8 == 48 && regionY / 8 == 148) {
						inTutorialIsland = true;
					}
					loadingStage = 1;
					aLong824 = System.currentTimeMillis();
					gameGraphics.setCanvas();
					fancyFont.drawLeftAlignedEffectString("Loading - please wait...", 10, 20, 0xffffff, true);
					if(uiRenderer.isResizableOrFull()) {
						gameGraphics.drawGraphics(0, 0, super.graphics);
					} else {
						gameGraphics.drawGraphics(0, 0, super.graphics);
					}
					if(pktType == 73) {
						int r = 0;
						for(int i21 = (regionX - 6) / 8; i21 <= (regionX + 6) / 8; i21++) {
							for(int k23 = (regionY - 6) / 8; k23 <= (regionY + 6) / 8; k23++) {
								r++;
							}
						}
						terrainData = new byte[r][];
						objectData = new byte[r][];
						mapCoordinates = new int[r];
						terrainDataIds = new int[r];
						objectDataIds = new int[r];
						r = 0;
						for(int x = (regionX - 6) / 8; x <= (regionX + 6) / 8; x++) {
							for(int y = (regionY - 6) / 8; y <= (regionY + 6) / 8; y++) {
								mapCoordinates[r] = (x << 8) + y;
								if(inTutorialIsland && (y == 49 || y == 149 || y == 147 || x == 50 || x == 49 && y == 47)) {
									terrainDataIds[r] = -1;
									objectDataIds[r] = -1;
									r++;
								} else {
									final int k28 = terrainDataIds[r] = onDemandRequester.getMapId(0, y, x);
									if(k28 != -1) {
										onDemandRequester.addRequest(3, k28);
									}
									final int j30 = objectDataIds[r] = onDemandRequester.getMapId(1, y, x);
									if(j30 != -1) {
										onDemandRequester.addRequest(3, j30);
									}
									r++;
								}
							}
						}
					}
					if(pktType == 241) {
						int l16 = 0;
						final int ai[] = new int[676];
						for(int i24 = 0; i24 < 4; i24++) {
							for(int k26 = 0; k26 < 13; k26++) {
								for(int l28 = 0; l28 < 13; l28++) {
									final int k30 = constructRegionData[i24][k26][l28];
									if(k30 != -1) {
										final int k31 = k30 >> 14 & 0x3ff;
										final int i32 = k30 >> 3 & 0x7ff;
										int k32 = (k31 / 8 << 8) + i32 / 8;
										for(int j33 = 0; j33 < l16; j33++) {
											if(ai[j33] != k32) {
												continue;
											}
											k32 = -1;

										}
										if(k32 != -1) {
											ai[l16++] = k32;
										}
									}
								}
							}
						}
						terrainData = new byte[l16][];
						objectData = new byte[l16][];
						mapCoordinates = new int[l16];
						terrainDataIds = new int[l16];
						objectDataIds = new int[l16];
						for(int l26 = 0; l26 < l16; l26++) {
							final int i29 = mapCoordinates[l26] = ai[l26];
							final int l30 = i29 >> 8 & 0xff;
							final int l31 = i29 & 0xff;
							final int j32 = terrainDataIds[l26] = onDemandRequester.getMapId(0, l31, l30);
							if(j32 != -1) {
								onDemandRequester.addRequest(3, j32);
							}
							final int i33 = objectDataIds[l26] = onDemandRequester.getMapId(1, l31, l30);
							if(i33 != -1) {
								onDemandRequester.addRequest(3, i33);
							}
						}
					}
					final int i17 = baseX - anInt1036;
					final int j21 = baseY - anInt1037;
					anInt1036 = baseX;
					anInt1037 = baseY;
					for(int j24 = 0; j24 < 16384; j24++) {
						final NPC npc = npcList[j24];
						if(npc != null) {
							for(int j29 = 0; j29 < 10; j29++) {
								npc.smallX[j29] -= i17;
								npc.smallY[j29] -= j21;
							}
							npc.x -= i17 << 7;
							npc.y -= j21 << 7;
						}
					}
					for(int i27 = 0; i27 < maxPlayers; i27++) {
						final Player player = playerList[i27];
						if(player != null) {
							for(int i31 = 0; i31 < 10; i31++) {
								player.smallX[i31] -= i17;
								player.smallY[i31] -= j21;
							}
							player.x -= i17 << 7;
							player.y -= j21 << 7;
						}
					}
					regionLoaded = true;
					byte byte1 = 0;
					byte byte2 = 104;
					byte byte3 = 1;
					if(i17 < 0) {
						byte1 = 103;
						byte2 = -1;
						byte3 = -1;
					}
					byte byte4 = 0;
					byte byte5 = 104;
					byte byte6 = 1;
					if(j21 < 0) {
						byte4 = 103;
						byte5 = -1;
						byte6 = -1;
					}
					for(int k33 = byte1; k33 != byte2; k33 += byte3) {
						for(int l33 = byte4; l33 != byte5; l33 += byte6) {
							final int i34 = k33 + i17;
							final int j34 = l33 + j21;
							for(int k34 = 0; k34 < 4; k34++) {
								if(i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104) {
									sceneItems[k34][k33][l33] = sceneItems[k34][i34][j34];
								} else {
									sceneItems[k34][k33][l33] = null;
								}
							}
						}
					}
					for(WorldObjectSpawn worldObj = (WorldObjectSpawn) aClass19_1179.getFirst(); worldObj != null; worldObj = (WorldObjectSpawn) aClass19_1179.getNext()) {
						worldObj.anInt1297 -= i17;
						worldObj.anInt1298 -= j21;
						if(worldObj.anInt1297 < 0 || worldObj.anInt1298 < 0 || worldObj.anInt1297 >= 104 || worldObj.anInt1298 >= 104) {
							worldObj.unlinkPrimary();
						}
					}
					if(walkX != 0) {
						walkX -= i17;
						walkY -= j21;
					}
					forcedCameraLocation = false;
					pktType = -1;
					return true;

				case 208:
					final int i3 = inBuffer.getInt();
					if(i3 >= 0) {
						method60(i3);
					}
					anInt1018 = i3;
					pktType = -1;
					return true;

				case 99:
					minimapOverlay = inBuffer.getUByte();
					pktType = -1;
					return true;

				case 75:
					final int mediaID = inBuffer.getLitEndUShortMinus128();
					final int widgetID = inBuffer.getLitEndUShortMinus128();
					Interface.cache[widgetID].modelType = 2;
					Interface.cache[widgetID].modelId = mediaID;
					pktType = -1;
					return true;

				case 114:
					systemUpdateTimer = inBuffer.getLitEndUShort() * 30;
					pktType = -1;
					return true;

				case 60:
					anInt1269 = inBuffer.getUByte();
					anInt1268 = inBuffer.getOppositeUByte();
					while(inBuffer.pos < pktSize) {
						final int k3 = inBuffer.getUByte();
						method137(inBuffer, k3);
					}
					pktType = -1;
					return true;

				case 35:
					final int l3 = inBuffer.getUByte();
					final int k11 = inBuffer.getUByte();
					final int j17 = inBuffer.getUByte();
					final int k21 = inBuffer.getUByte();
					aBooleanArray876[l3] = true;
					anIntArray873[l3] = k11;
					anIntArray1203[l3] = j17;
					anIntArray928[l3] = k21;
					anIntArray1030[l3] = 0;
					pktType = -1;
					return true;

				case 104:
					final int j4 = inBuffer.getOppositeUByte();
					final int i12 = inBuffer.getReversedOppositeUByte();
					String s6 = inBuffer.getLine();
					if(j4 >= 1 && j4 <= 5) {
						if(s6.equalsIgnoreCase("null")) {
							s6 = null;
						}
						atPlayerActions[j4 - 1] = s6;
						atPlayerArray[j4 - 1] = i12 == 0;
					}
					pktType = -1;
					return true;

				case 78:
					walkX = 0;
					pktType = -1;
					return true;

				case 252:
					final String task = inBuffer.getLine();
					taskHandler.completeTask(task);
					pktType = -1;
					return true;

				case 253:
					final String s = inBuffer.getLine();
					if(s.endsWith(":tradereq:")) {
						final String s3 = s.substring(0, s.indexOf(":"));
						final long l17 = StringUtils.encryptName(s3);
						boolean flag2 = false;
						for(int j27 = 0; j27 < ignoreCount; j27++) {
							if(ignoreListAsLongs[j27] != l17) {
								continue;
							}
							flag2 = true;

						}
						if(!flag2 && anInt1251 == 0) {
							pushMessage("wishes to trade with you.", 4, s3);
						}
					} else if(s.endsWith("#url#")) {
						final String link = s.substring(0, s.indexOf("#"));
						pushMessage("Join us at: ", 9, link);
					} else if(s.endsWith(":dicereq:")) {
						String s4 = s.substring(0, s.indexOf(":"));
						long l18 = StringUtils.encryptName(s4);
						boolean flag3 = false;
						for(int k27 = 0; k27 < ignoreCount; k27++) {
							if(ignoreListAsLongs[k27] != l18) {
								continue;
							}
							flag3 = true;
						}
						if(!flag3 && anInt1251 == 0) {
							pushMessage("wishes to dice with you.", 10, s4);
						}
						pushMessage("Confirm dicing by right clicking " + s4 + ".", 10, " - ");
					} else if(s.endsWith(":duelreq:")) {
						final String s4 = s.substring(0, s.indexOf(":"));
						final long l18 = StringUtils.encryptName(s4);
						boolean flag3 = false;
						for(int k27 = 0; k27 < ignoreCount; k27++) {
							if(ignoreListAsLongs[k27] != l18) {
								continue;
							}
							flag3 = true;

						}
						if(!flag3 && anInt1251 == 0) {
							pushMessage("wishes to duel with you.", 8, s4);
						}
					} else if(s.endsWith(":chalreq:")) {
						final String s5 = s.substring(0, s.indexOf(":"));
						final long l19 = StringUtils.encryptName(s5);
						boolean flag4 = false;
						for(int l27 = 0; l27 < ignoreCount; l27++) {
							if(ignoreListAsLongs[l27] != l19) {
								continue;
							}
							flag4 = true;

						}
						if(!flag4 && anInt1251 == 0) {
							final String s8 = s.substring(s.indexOf(":") + 1, s.length() - 9);
							pushMessage(s8, 8, s5);
						}
					} else {
						pushMessage(s, 0, "");
					}
					pktType = -1;
					return true;

				case 1:
					for(int k4 = 0; k4 < playerList.length; k4++) {
						if(playerList[k4] != null) {
							playerList[k4].anim = -1;
						}
					}
					for(int j12 = 0; j12 < npcList.length; j12++) {
						if(npcList[j12] != null) {
							npcList[j12].anim = -1;
						}
					}
					pktType = -1;
					return true;

				case 50:
					final long l4 = inBuffer.getLong();
					final int i18 = inBuffer.getUByte();
					String s7 = StringUtils.formatName(StringUtils.decryptName(l4));
					for(int k24 = 0; k24 < friendsCount; k24++) {
						if(l4 != friendsListAsLongs[k24]) {
							continue;
						}
						if(friendsNodeIDs[k24] != i18) {
							friendsNodeIDs[k24] = i18;
							if(i18 >= 2) {
								pushMessage(s7 + " has logged in.", 5, "");
							}
							if(i18 <= 1) {
								pushMessage(s7 + " has logged out.", 5, "");
							}
						}
						s7 = null;

					}
					if(s7 != null && friendsCount < 200) {
						friendsListAsLongs[friendsCount] = l4;
						friendsList[friendsCount] = s7;
						friendsNodeIDs[friendsCount] = i18;
						friendsCount++;
					}
					for(boolean flag6 = false; !flag6; ) {
						flag6 = true;
						for(int k29 = 0; k29 < friendsCount - 1; k29++) {
							if(friendsNodeIDs[k29] != nodeID && friendsNodeIDs[k29 + 1] == nodeID || friendsNodeIDs[k29] == 0 && friendsNodeIDs[k29 + 1] != 0) {
								final int j31 = friendsNodeIDs[k29];
								friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
								friendsNodeIDs[k29 + 1] = j31;
								final String s10 = friendsList[k29];
								friendsList[k29] = friendsList[k29 + 1];
								friendsList[k29 + 1] = s10;
								final long l32 = friendsListAsLongs[k29];
								friendsListAsLongs[k29] = friendsListAsLongs[k29 + 1];
								friendsListAsLongs[k29 + 1] = l32;
								flag6 = false;
							}
						}
					}
					pktType = -1;
					return true;

				case 51:
					for(int fresh = 0; fresh < 100; fresh++) {
						if(Interface.cache[50144 + fresh] != null)
							Interface.cache[50144 + fresh].text = "";
					}
					int size = inBuffer.getUShort();
					clanMatesList = new ObjectArrayList<>(size);
					for(int c = 0; c < size; c++) {
						String name = inBuffer.getLine();
						boolean muted = inBuffer.getBoolean();
						int rank = inBuffer.getUByte();
						name = StringUtils.formatName(name.toLowerCase().replace("_", " "));
						String ranked = rank == 0 ? "" : "@ra" + rank + "@";
						Interface.cache[50144 + c].text = ranked + name;
						clanMatesList.add(c, new ClanSettingPanel.ClanMember(name, rank, muted));
					}
					pktType = -1;
					return true;

				case 52:
					int size2 = inBuffer.getUShort();
					clanBansList = new String[size2];
					for(int c = 0; c < size2; c++) {
						clanBansList[c] = StringUtils.formatName(inBuffer.getLine().toLowerCase().replace("_", " "));
					}
					pktType = -1;
					return true;

				case 110:
					energy = inBuffer.getUByte();
					OrbHandler.updateRun();
					pktType = -1;
					return true;

				case 254:
					hintType = inBuffer.getUByte();
					if(hintType == 1) {
						NPCHintID = inBuffer.getUShort();
					}
					if(hintType >= 2 && hintType <= 6) {
						if(hintType == 2) {
							hintRegionX = 64;
							hintRegionY = 64;
						}
						if(hintType == 3) {
							hintRegionX = 0;
							hintRegionY = 64;
						}
						if(hintType == 4) {
							hintRegionX = 128;
							hintRegionY = 64;
						}
						if(hintType == 5) {
							hintRegionX = 64;
							hintRegionY = 0;
						}
						if(hintType == 6) {
							hintRegionX = 64;
							hintRegionY = 128;
						}
						hintType = 2;
						anInt934 = inBuffer.getUShort();
						anInt935 = inBuffer.getUShort();
						anInt936 = inBuffer.getUByte();
					}
					if(hintType == 10) {
						anInt933 = inBuffer.getUShort();
					}
					pktType = -1;
					return true;

				case 248:
					final int i5 = inBuffer.getUShortMinus128();
					final int k12 = inBuffer.getUShort();
					if(forcedChatWidgetId != -1) {
						forcedChatWidgetId = -1;
					}
					if(inputDialogState != 0) {
						inputDialogState = 0;
					}
					if(i5 == 65532) {
						openInterfaceID = -1;
						panelHandler.open(new ShopPanel(true));
					} else if(i5 == 65534) {
						openInterfaceID = -1;
						panelHandler.open(new ShopPanel(false));
					} else if(i5 == 65533) {
						openInterfaceID = -1;
						panelHandler.open(new BankPanel());
					} else {
						if(panelHandler.action())
							panelHandler.close();
						openInterfaceID = i5;
					}
					invOverlayInterfaceID = k12;
					updateInventory = true;
					aBoolean1149 = false;
					pktType = -1;
					return true;

				case 79:
					final int j5 = inBuffer.getLitEndUShort();
					int l12 = inBuffer.getUShortMinus128();
					final Interface class9_3 = Interface.cache[j5];
					if(class9_3 != null && class9_3.type == 0) {
						if(l12 < 0) {
							l12 = 0;
						}
						if(l12 > class9_3.scrollMax - class9_3.height) {
							l12 = class9_3.scrollMax - class9_3.height;
						}
						class9_3.scrollPos = l12;
					}
					pktType = -1;
					return true;

				case 68:
					for(int k5 = 0; k5 < variousSettings.length; k5++) {
						if(variousSettings[k5] != anIntArray1045[k5]) {
							variousSettings[k5] = anIntArray1045[k5];
							handleSettings(k5);
						}
					}
					pktType = -1;
					return true;

				case 196:
					final long form = inBuffer.getLong();
					final int j18 = inBuffer.getInt();
					final int l21 = inBuffer.getUByte();
					boolean flag5 = false;
					for(int i28 = 0; i28 < 100; i28++) {
						if(anIntArray1240[i28] != j18) {
							continue;
						}
						flag5 = true;

					}
					if(l21 <= 1) {
						for(int l29 = 0; l29 < ignoreCount; l29++) {
							if(ignoreListAsLongs[l29] != form) {
								continue;
							}
							flag5 = true;

						}
					}
					if(!flag5 && anInt1251 == 0) {
						try {
							anIntArray1240[anInt1169] = j18;
							anInt1169 = (anInt1169 + 1) % 100;
							final String s9 = StringEncoder.getString(pktSize - 13, inBuffer);
							if(l21 >= 1)
								pushMessage(s9, 2, "@cr" + l21 + "@" + StringUtils.formatName(StringUtils.decryptName(form)));
							else
								pushMessage(s9, 2, StringUtils.formatName(StringUtils.decryptName(form)));
						} catch(final Exception exception1) {
							SignLink.reportError("cde1");
						}
					}
					pktType = -1;
					return true;
					
				case 130:
					int count = inBuffer.getUByte();
					ObjectCreationPanel.getObjects().clear();
					for(int i4 = 0; i4 < count; i4++) {
						int furObj = inBuffer.getUShort();
						int furLvl = inBuffer.getUByte();
						int furItemsCount = inBuffer.getUByte();
						Item[] items = new Item[furItemsCount];
						for(int i2 = 0; i2 < furItemsCount; i2++) {
							items[i2] = new Item(inBuffer.getUShort(), inBuffer.getUShort());
						}
						ConstructionObject obj = new ConstructionObject(furObj, furLvl, items);
						ObjectCreationPanel.getObjects().add(obj);
					}
					panelHandler.open(new ObjectCreationPanel());
					pktType = -1;
					return true;
					
					
				case 131:
					for(WorldObjectSpawn class30_sub1 = (WorldObjectSpawn) aClass19_1179.getFirst(); class30_sub1 != null; class30_sub1 = (WorldObjectSpawn) aClass19_1179.getNext()) {
						method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1300, class30_sub1.anInt1301, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1299);
						class30_sub1.unlinkPrimary();
					}
					pktType = -1;
					return true;
					
					

				case 85:
					anInt1269 = inBuffer.getOppositeUByte();
					anInt1268 = inBuffer.getOppositeUByte();
					pktType = -1;
					return true;

				case 24:
					anInt1054 = inBuffer.getReversedUByte();
					if(anInt1054 == invTab) {
						if(anInt1054 == 3) {
							invTab = 1;
						} else {
							invTab = 3;
						}
					}
					pktType = -1;
					return true;

				case 246:
					final int i6 = inBuffer.getLitEndUShort();
					final int i13 = inBuffer.getUShort();
					final int k18 = inBuffer.getUShort();
					if(k18 == 65535) {
						Interface.cache[i6].modelType = 0;
						pktType = -1;
						return true;
					} else {
						final ObjectType itemDef = ObjectType.get(k18);
						Interface.cache[i6].modelType = 4;
						Interface.cache[i6].modelId = k18;
						Interface.cache[i6].modelYaw = itemDef.iconYaw;
						Interface.cache[i6].modelRoll = itemDef.iconRoll;
						Interface.cache[i6].modelZoom = itemDef.iconZoom * 100 / i13;
						pktType = -1;
						return true;
					}

				case 171:
					final boolean flag1 = inBuffer.getUByte() == 1;
					final int j13 = inBuffer.getUShort();
					Interface.cache[j13].hoverTriggered = flag1;
					pktType = -1;
					return true;

				case 142:
					final int j6 = inBuffer.getLitEndUShort();
					method60(j6);
					if(forcedChatWidgetId != -1) {
						forcedChatWidgetId = -1;
					}
					if(inputDialogState != 0) {
						inputDialogState = 0;
					}
					invOverlayInterfaceID = j6;
					updateInventory = true;
					openInterfaceID = -1;
					aBoolean1149 = false;
					pktType = -1;
					return true;

				case 126:
					final String text = inBuffer.getLine();
					final int frame = inBuffer.getUShortMinus128();
					updateStrings(text, frame);
					if(Interface.cache[frame] != null)
						Interface.cache[frame].text = text;
					pktType = -1;
					return true;

				case 206:
					publicChatMode = inBuffer.getUByte();
					privateChatMode = inBuffer.getUByte();
					tradeMode = inBuffer.getUByte();
					pktType = -1;
					return true;

				case 240:
					weight = inBuffer.getSShort();
					pktType = -1;
					return true;

				case 8:
					final int k6 = inBuffer.getLitEndUShortMinus128();
					final int l13 = inBuffer.getUShort();
					Interface.cache[k6].modelType = 1;
					Interface.cache[k6].modelId = l13;
					pktType = -1;
					return true;
				
				case 121:
					npcInfoId = inBuffer.getInt();
					if(npcInfoId == 0) {
						panelSearch = true;
						panelHandler.open(new DropPanel());
						npcSug = false;
						promptInput = "";
						panelSearchInput = "";
						promptInputTitle = "What monster are you searching for?";
						messagePromptRaised = true;
						pktType = -1;
						return true;
					} else {
						panelSearch = false;
					}
					final int commonCount = inBuffer.getUShort();
					npcDrops = new int[commonCount];
					for(int common = 0; common < commonCount; common++) {
						npcDrops[common] = inBuffer.getUShort();
					}
					final int uniqueCount = inBuffer.getUShort();
					npcDropsId = new int[uniqueCount];
					npcDropsMin = new int[uniqueCount];
					npcDropsMax = new int[uniqueCount];
					npcDropsChance = new int[uniqueCount];
					for(int unique = 0; unique < uniqueCount; unique++) {
						npcDropsId[unique] = inBuffer.getUShort();
						npcDropsMin[unique] = inBuffer.getUShort();
						npcDropsMax[unique] = inBuffer.getUShort();
						npcDropsChance[unique] = inBuffer.getUByte();
					}
					pktType = -1;
					return true;

				case 122:
					final int l6 = inBuffer.getLitEndUShortMinus128();
					final int i14 = inBuffer.getLitEndUShortMinus128();
					final int i19 = i14 >> 10 & 0x1f;
					final int i22 = i14 >> 5 & 0x1f;
					final int l24 = i14 & 0x1f;
					Interface.cache[l6].color = (i19 << 19) + (i22 << 11) + (l24 << 3);
					pktType = -1;
					return true;

				case 230:
					final int j7 = inBuffer.getUShortMinus128();
					final int j14 = inBuffer.getUShort();
					final int k19 = inBuffer.getUShort();
					final int k22 = inBuffer.getLitEndUShortMinus128();
					Interface.cache[j14].modelYaw = k19;
					Interface.cache[j14].modelRoll = k22;
					Interface.cache[j14].modelZoom = j7;
					pktType = -1;
					return true;

				case 221:
					anInt900 = inBuffer.getUByte();
					pktType = -1;
					return true;
				
				case 53:
					final int itemGroupID = inBuffer.getUShort();
					final Interface group = Interface.cache[itemGroupID];
					int total = inBuffer.getUShort();
					if(itemGroupID == 3900) {
						group.invId = new int[total];
						group.invAmt = new int[total];
						currentShopInterfacePrices = new int[total];
					}
					for(int j22 = 0; j22 < total; j22++) {
						boolean provided = false;
						if(itemGroupID == 3900) {
							provided = inBuffer.getBoolean();
						}
						int i25 = provided ? 999999999 : 0;
						if(!provided) {
							i25 = inBuffer.getUByte();
							if(i25 == 255) {
								i25 = inBuffer.getMidEndInt();
							}
						}
						group.invId[j22] = inBuffer.getLitEndUShortMinus128();
						group.invAmt[j22] = i25;
						if(itemGroupID == 3900) {
							int price = inBuffer.getUByte();
							if(price == 255) {
								price = inBuffer.getMidEndInt();
							}
							currentShopInterfacePrices[j22] = price;
						}
					}
					for(int j25 = total; j25 < group.invId.length; j25++) {
						group.invId[j25] = 0;
						group.invAmt[j25] = 0;
						if(itemGroupID == 3900) {
							currentShopInterfacePrices[j25] = 0;
						}
					}
					pktType = -1;
					return true;
				
				case 54://price change
					int price = inBuffer.getUByte();
					if(price == 255)
						price = inBuffer.getMidEndInt();
					int itemId = inBuffer.getLitEndUShortMinus128();
					for(int shopIndex = 0; shopIndex < Interface.cache[3900].invId.length; shopIndex++) {
						if(Interface.cache[3900].invId[shopIndex] == itemId - 1) {
							currentShopInterfacePrices[shopIndex] = price;
							break;
						}
					}
					pktType = -1;
					return true;
				
				case 55://stock change
					int amount = inBuffer.getUByte();
					if(amount == 255)
						amount = inBuffer.getMidEndInt();
					int itemId2 = inBuffer.getLitEndUShortMinus128();
					for(int shopIndex = 0; shopIndex < Interface.cache[3900].invId.length; shopIndex++) {
						if(Interface.cache[3900].invId[shopIndex] == itemId2 - 1) {
							Interface.cache[3900].invAmt[shopIndex] = amount;
							break;
						}
					}
					pktType = -1;
					return true;

				case 177:
					forcedCameraLocation = true;
					anInt995 = inBuffer.getUByte();
					anInt996 = inBuffer.getUByte();
					anInt997 = inBuffer.getUShort();
					anInt998 = inBuffer.getUByte();
					anInt999 = inBuffer.getUByte();
					if(anInt999 >= 100) {
						final int k7 = anInt995 * 128 + 64;
						final int k14 = anInt996 * 128 + 64;
						final int i20 = method42(cameraPlane, k7, k14) - anInt997;
						final int l22 = k7 - cameraLocationX;
						final int k25 = i20 - cameraLocationZ;
						final int j28 = k14 - cameraLocationY;
						final int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
						cameraRoll = (int) (Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
						cameraYaw = (int) (Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
						if(cameraRoll < 128) {
							cameraRoll = 128;
						}
						if(cameraRoll > 383) {
							cameraRoll = 383;
						}
					}
					pktType = -1;
					return true;

				case 249:
					anInt1046 = inBuffer.getReversedOppositeUByte();
					unknownInt10 = inBuffer.getLitEndUShortMinus128();
					pktType = -1;
					return true;

				case 65:
					updateNPCs(inBuffer, pktSize);
					pktType = -1;
					return true;

				case 27:
					messagePromptRaised = false;
					inputDialogState = 1;
					amountOrNameInput = "";
					outBoundInput = "";
					promptInput = "";
					promptInputTitle = inBuffer.getLine();
					pktType = -1;
					return true;
				
				case 30:
					int key = inBuffer.getUShort();
					if(key >= 0 && key < scoreDeaths.length) {
						scoreKills[key] = inBuffer.getUShort();
						scoreDeaths[key] = inBuffer.getUShort();
						killstreak[key] = inBuffer.getUShort();
						scoreNames[key] = inBuffer.getLine();
					}
					pktType = -1;
					return true;
				
				case 100:
					String link = inBuffer.getLine();
					WebToolkit.openWebpage("http://www.edgeville.net/" + link);
					pktType = -1;
					return true;

				case 187:
					messagePromptRaised = false;
					inputDialogState = 2;
					amountOrNameInput = "";
					outBoundInput = "";
					promptInput = "";
					promptInputTitle = inBuffer.getLine();
					pktType = -1;
					return true;

				case 97:
					final int l7 = inBuffer.getUShort();
					boolean real = false;
					if(l7 == 65535) {
						panelHandler.close();
						aBoolean1149 = false;
						pktType = -1;
						return true;
					} else if(l7 == 65522) {//-14
						panelHandler.open(new RoomCreationPanel());
					} else if(l7 == 65523) {//-13
						panelHandler.open(new CounterPanel());
					} else if(l7 == 65524) {//-12
						panelHandler.open(new PvPPanel());
					} else if(l7 == 65525) {//-11
						panelHandler.open(new DropPanel());
					} else if(l7 == 65526) {//-10
						panelHandler.open(new SlayerPanel());
					} else if(l7 == 65527) {//-9
						panelHandler.open(new MonsterPanel());
					} else if(l7 == 65528) {//-8
						panelHandler.open(new SummoningPanel());
					} else if(l7 == 65529) {//-7
						panelHandler.open(new BossPanel());
					} else if(l7 == 65530) {//-6
						panelHandler.open(new MinigamePanel());
					} else if(l7 == 65531) {//-5
						panelHandler.open(new IronManSelectionPanel());
					} else if(l7 == 65532) {//-4
						panelHandler.open(new SkillPanel());
					} else if(l7 == 65533) {//-3
						panelHandler.open(new ClanSettingPanel());
					} else {
						real = true;
						method60(l7);
					}
					if(invOverlayInterfaceID != -1) {
						invOverlayInterfaceID = -1;
						updateInventory = true;
					}
					if(forcedChatWidgetId != -1) {
						forcedChatWidgetId = -1;
					}
					if(inputDialogState != 0) {
						inputDialogState = 0;
					}
					if(!real) {
						openInterfaceID = -1;
					} else {
						openInterfaceID = l7;
						if(!panelHandler.isBlocked())
							panelHandler.close();
					}
					aBoolean1149 = false;
					pktType = -1;
					return true;

				case 218:
					final int i8 = inBuffer.getLitEndSShortMinus128();
					chatWidgetId = i8;
					pktType = -1;
					return true;

				case 87:
					final int j8 = inBuffer.getLitEndUShort();
					final int l14 = inBuffer.getMixEndInt();
					anIntArray1045[j8] = l14;
					if(variousSettings[j8] != l14) {
						variousSettings[j8] = l14;
						handleSettings(j8);
					}
					pktType = -1;
					return true;

				case 36:
					final int k8 = inBuffer.getLitEndUShort();
					final byte byte0 = inBuffer.getSByte();
					anIntArray1045[k8] = byte0;
					if(variousSettings[k8] != byte0) {
						variousSettings[k8] = byte0;
						handleSettings(k8);
					}
					if(k8 == 173) {
						OrbHandler.runEnabled = byte0 == 0;
					}
					if(k8 == 174) {
						OrbHandler.poisoned = byte0 == 1;
					}
					if(k8 == 175) {
						OrbHandler.prayersEnabled = byte0 == 1;
					}
					if(k8 == 176) {
						OrbHandler.summonEnabled = byte0 == 1;
					}
					pktType = -1;
					return true;

				case 61:
					combatMultiwayMode = inBuffer.getUByte();
					pktType = -1;
					return true;

				case 200:
					int chatInterfaceId = inBuffer.getUShort();
					int chatAnimation = inBuffer.getSShort();
					final Interface widget1 = Interface.cache[chatInterfaceId];
					widget1.modelAnim = chatAnimation;
					if(widget1.id != 250)
						widget1.modelZoom = 2000;
					if(chatAnimation == -1) {
						widget1.modelAnimLength = 0;
						widget1.modelAnimDelay = 0;
					}
					pktType = -1;
					return true;

				case 219:
					if(invOverlayInterfaceID != -1) {
						invOverlayInterfaceID = -1;
						updateInventory = true;
					}
					if(forcedChatWidgetId != -1) {
						forcedChatWidgetId = -1;
					}
					if(inputDialogState != 0) {
						inputDialogState = 0;
					}
					if(panelHandler.isActive()) {
						panelHandler.close();
					}
					openInterfaceID = -1;
					aBoolean1149 = false;
					pktType = -1;
					return true;

				case 34:
					final int widget = inBuffer.getUShort();
					final Interface class9_2 = Interface.cache[widget];
					while(inBuffer.pos < pktSize) {
						final int slot = inBuffer.getUByte();
						int item = inBuffer.getUShort();
						int l25 = inBuffer.getUByte();
						if(l25 == 255) {
							l25 = inBuffer.getInt();
						}
						if(widget >= 270 && widget <= 279)
							item -= 1;
						if(slot >= 0 && slot < class9_2.invId.length) {
							class9_2.invId[slot] = item;
							class9_2.invAmt[slot] = l25;
						}
					}
					pktType = -1;
					return true;

				case 4:
				case 44:
				case 84:
				case 101:
				case 105:
				case 117:
				case 147:
				case 151:
				case 156:
				case 160:
				case 215:
					method137(inBuffer, pktType);
					pktType = -1;
					return true;

				case 106:
					int tabOldForce = inBuffer.getUByte();
					int tabNewForce = inBuffer.getUByte();
					if(uiRenderer.getId() == 562) {
						invTab = tabNewForce;
					} else {
						invTab = tabOldForce;
					}
					updateInventory = true;
					if(!showTab)
						showTab = true;
					pktType = -1;
					return true;

				case 164:
					final int j9 = inBuffer.getLitEndUShort();
					method60(j9);
					if(invOverlayInterfaceID != -1) {
						invOverlayInterfaceID = -1;
						updateInventory = true;
					}
					forcedChatWidgetId = j9;
					openInterfaceID = -1;
					aBoolean1149 = false;
					pktType = -1;
					return true;

			}
			SignLink.reportError("T1 - " + pktType + "," + pktSize + " - " + anInt842 + "," + anInt843);
			logOut();
		} catch(final IOException _ex) {
			dropClient();
			_ex.printStackTrace();
		} catch(final Exception exception) {
			exception.printStackTrace();
			String s2 = "T2 - " + pktType + "," + anInt842 + "," + anInt843 + " - " + pktSize + "," + (baseX + localPlayer.smallX[0]) + "," + (baseY + localPlayer.smallY[0]) + " - ";
			for(int j15 = 0; j15 < pktSize && j15 < 50; j15++) {
				s2 = s2 + inBuffer.data[j15] + ",";
			}
			SignLink.reportError(s2);
			logOut();
		}
		return true;
	}

	public void sendFrame219() {
		if(invOverlayInterfaceID != -1) {
			invOverlayInterfaceID = -1;
			updateInventory = true;
		}
		if(forcedChatWidgetId != -1) {
			forcedChatWidgetId = -1;
		}
		if(inputDialogState != 0) {
			inputDialogState = 0;
		}
		openInterfaceID = -1;
		aBoolean1149 = false;
	}

	public void sendFrame248(int interfaceID, int sideInterfaceID) {
		if(forcedChatWidgetId != -1) {
			forcedChatWidgetId = -1;
		}
		if(inputDialogState != 0) {
			inputDialogState = 0;
		}
		openInterfaceID = interfaceID;
		invOverlayInterfaceID = sideInterfaceID;
		updateInventory = true;
		aBoolean1149 = false;
	}

	public void sendFrame36(int id, int state) {
		anIntArray1045[id] = state;
		if(variousSettings[id] != state) {
			variousSettings[id] = state;
			handleSettings(id);
		}
	}

	public void sendPacket185(int button, int toggle, int type) {
		switch(type) {
			case 135:
				final Interface class9 = Interface.cache[button];
				boolean flag8 = true;
				if(class9.contentType > 0) {
					flag8 = promptUserForInput(class9);
				}
				if(flag8) {
					outBuffer.putOpcode(185);
					outBuffer.putShort(button);
				}
				break;
			case 646:
				outBuffer.putOpcode(185);
				outBuffer.putShort(button);
				final Interface class9_2 = Interface.cache[button];
				if(class9_2.valueIndexArray != null && class9_2.valueIndexArray[0][0] == 5) {
					if(variousSettings[toggle] != class9_2.requiredValues[0]) {
						variousSettings[toggle] = class9_2.requiredValues[0];
						handleSettings(toggle);
					}
				}
				break;
			case 169:
				outBuffer.putOpcode(185);
				outBuffer.putShort(button);
				final Interface class9_3 = Interface.cache[button];
				if(class9_3.valueIndexArray != null && class9_3.valueIndexArray[0][0] == 5) {
					variousSettings[toggle] = 1 - variousSettings[toggle];
					handleSettings(toggle);
				}
				switch(button) {
					case 19136:
						System.out.println("toggle = " + toggle);
						if(toggle == 0) {
							sendFrame36(173, toggle);
						}
						if(toggle == 1) {
							sendPacket185(153, 173, 646);
						}
						break;
				}
				break;
		}
	}

	public void method104() {
		StillGraphic graphic = (StillGraphic) aClass19_1056.getFirst();
		for(; graphic != null; graphic = (StillGraphic) aClass19_1056.getNext()) {
			if(graphic.anInt1560 != cameraPlane || graphic.noEffects) {
				graphic.unlinkPrimary();
			} else if(loopCycle >= graphic.anInt1564) {
				graphic.moveAnimation(anInt945);
				if(graphic.noEffects) {
					graphic.unlinkPrimary();
				} else {
					scene.addEntity(graphic.anInt1560, 0, graphic.anInt1563, -1, graphic.anInt1562, 60, graphic.anInt1561, graphic, false);
				}
			}
		}

	}

	public void method114() {
		for(int i = -1; i < playerCount; i++) {
			int j;
			if(i == -1) {
				j = localPlayerIndex;
			} else {
				j = playerEntryList[i];
			}
			final Player player = playerList[j];
			if(player != null) {
				method96(player);
			}
		}
	}
	
	public void renderMinimap(int plane) {
		final int[] raster = minimapImage.imageRaster;
		for(int index = 0; index < raster.length; index++) {
			raster[index] = 0;
		}
		for(int y = 1; y < 103; y++) {
			int index = 24628 + (103 - y) * 512 * 4;
			for(int x = 1; x < 103; x++) {
				if((tiles[plane][x][y] & 0x18) == 0) {
					scene.setMinimapFloorColor(raster, index, plane, x, y);
				}
				if(plane < 3 && (tiles[plane + 1][x][y] & 8) != 0) {
					scene.setMinimapFloorColor(raster, index, plane + 1, x, y);
				}
				index += 4;
			}
		}
		int white;
		int red;
		if(Constants.ANTI_BOT_ENABLED) {
			white = (238 + (int) (Math.random() * 20D) - 10 << 16) + (238 + (int) (Math.random() * 20D) - 10 << 8) + 238 + (int) (Math.random() * 20D) - 10;
			red = 238 + (int) (Math.random() * 20D) - 10 << 16;
		} else {
			white = 0xeeeeee;
			red = 0xee0000;
		}
		minimapImage.setCanvas();
		for(int y = 1; y < 103; y++) {
			for(int x = 1; x < 103; x++) {
				if((tiles[plane][x][y] & 0x18) == 0) {
					method50(y, white, x, red, plane);
				}
				if(plane < 3 && (tiles[plane + 1][x][y] & 8) != 0) {
					method50(y, white, x, red, plane + 1);
				}
			}
		}
		gameGraphics.setCanvas();
		mapFunctionCount = 0;
		for(int x = 0; x < 104; x++) {
			for(int y = 0; y < 104; y++) {
				long hash = scene.getGroundDecorHash(cameraPlane, x, y);
				if(hash != 0) {
					hash = hash >> 14 & 0xFFFFFF;
					final int mapFunction = LocationType.getPrecise((int) hash).mapFunction;
					if(mapFunction >= 0) {
						mapFunctionImage[mapFunctionCount] = mapFunctions[mapFunction];
						mapFunctionX[mapFunctionCount] = x;
						mapFunctionY[mapFunctionCount] = y;
						mapFunctionCount++;
					}
				}
			}
		}
	}
	
	
	public void method115() {
		if(loadingStage == 2) {
			boolean passed = false;
			for(WorldObjectSpawn class30_sub1 = (WorldObjectSpawn) aClass19_1179.getFirst(); class30_sub1 != null; class30_sub1 = (WorldObjectSpawn) aClass19_1179.getNext()) {
				if(class30_sub1.anInt1294 > 0) {
					class30_sub1.anInt1294--;
				}
				if(class30_sub1.anInt1294 == 0) {
					if(class30_sub1.anInt1299 < 0 || MapDecoder.method178(class30_sub1.anInt1299, class30_sub1.anInt1301)) {
						method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1300, class30_sub1.anInt1301, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1299);
						class30_sub1.unlinkPrimary();
					}
				} else {
					if(class30_sub1.anInt1302 > 0) {
						class30_sub1.anInt1302--;
					}
					if(class30_sub1.anInt1302 == 0 && class30_sub1.anInt1297 >= 1 && class30_sub1.anInt1298 >= 1 && class30_sub1.anInt1297 <= 102 && class30_sub1.anInt1298 <= 102 && (class30_sub1.anInt1291 < 0 || MapDecoder.method178(class30_sub1.anInt1291, class30_sub1.id))) {
						method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.face, class30_sub1.id, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1291);
						class30_sub1.anInt1302 = -1;
						if(class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1299 == -1) {
							class30_sub1.unlinkPrimary();
						} else if(class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.face == class30_sub1.anInt1300 && class30_sub1.id == class30_sub1.anInt1301) {
							class30_sub1.unlinkPrimary();
						}
						passed = true;
					}
				}
			}
			if(passed) {
				renderMinimap(cameraPlane);
			}
		}
	}

	public boolean method119(int i, int j) {
		boolean flag1 = false;
		final Interface class9 = Interface.cache[j];
		for(final int element : class9.subId) {
			if(element == -1) {
				continue;
			}
			final Interface class9_1 = Interface.cache[element];
			if(class9_1.type == 1) {
				flag1 |= method119(i, class9_1.id);
			}
			if(class9_1.type == 6 && (class9_1.modelAnim != -1 || class9_1.modelAnimAlt != -1)) {
				final boolean flag2 = interfaceIsSelected(class9_1);
				int l;
				if(flag2) {
					l = class9_1.modelAnimAlt;
				} else {
					l = class9_1.modelAnim;
				}
				if(l != -1) {
					final DeformSequence animation = DeformSequence.cache[l];
					for(class9_1.modelAnimDelay += i; class9_1.modelAnimDelay > animation.getFrame(class9_1.modelAnimLength); ) {
						class9_1.modelAnimDelay -= animation.getFrame(class9_1.modelAnimLength) + 1;
						class9_1.modelAnimLength++;
						if(class9_1.modelAnimLength >= animation.length) {
							class9_1.modelAnimLength -= animation.animCycle;
							if(class9_1.modelAnimLength < 0 || class9_1.modelAnimLength >= animation.length) {
								class9_1.modelAnimLength = 0;
							}
						}
						flag1 = true;
					}

				}
			}
		}

		return flag1;
	}

	public int method120() {
		if(Config.def.isROOF_OFF()) {
			return cameraPlane;
		}
		int j = 3;
		if(cameraRoll < 310) {
			int k = cameraLocationX >> 7;
			int l = cameraLocationY >> 7;
			final int i1 = localPlayer.x >> 7;
			final int j1 = localPlayer.y >> 7;
			if(k >= tiles[cameraPlane].length)
				return j;
			if(l >= tiles[cameraPlane][k].length)
				return j;
			if((tiles[cameraPlane][k][l] & 4) != 0) {
				j = cameraPlane;
			}
			int k1;
			if(i1 > k) {
				k1 = i1 - k;
			} else {
				k1 = k - i1;
			}
			int l1;
			if(j1 > l) {
				l1 = j1 - l;
			} else {
				l1 = l - j1;
			}
			if(k1 > l1 && k1 != 0) {
				final int i2 = l1 * 0x10000 / k1;
				int k2 = 32768;
				while(k != i1) {
					if(k < i1) {
						k++;
					} else if(k > i1) {
						k--;
					}
					if((tiles[cameraPlane][k][l] & 4) != 0) {
						j = cameraPlane;
					}
					k2 += i2;
					if(k2 >= 0x10000) {
						k2 -= 0x10000;
						if(l < j1) {
							l++;
						} else if(l > j1) {
							l--;
						}
						if((tiles[cameraPlane][k][l] & 4) != 0) {
							j = cameraPlane;
						}
					}
				}
			} else if(l1 != 0) {
				final int j2 = k1 * 0x10000 / l1;
				int l2 = 32768;
				while(l != j1) {
					if(l < j1) {
						l++;
					} else if(l > j1) {
						l--;
					}
					if((tiles[cameraPlane][k][l] & 4) != 0) {
						j = cameraPlane;
					}
					l2 += j2;
					if(l2 >= 0x10000) {
						l2 -= 0x10000;
						if(k < i1) {
							k++;
						} else if(k > i1) {
							k--;
						}
						if((tiles[cameraPlane][k][l] & 4) != 0) {
							j = cameraPlane;
						}
					}
				}
			}
		}
		if((tiles[cameraPlane][localPlayer.x >> 7][localPlayer.y >> 7] & 4) != 0) {
			j = cameraPlane;
		}
		return j;
	}

	public int method121() {
		if(Config.def.isROOF_OFF()) {
			return cameraPlane;
		}
		final int j = method42(cameraPlane, cameraLocationX, cameraLocationY);
		if(j - cameraLocationZ < 800 && (tiles[cameraPlane][cameraLocationX >> 7][cameraLocationY >> 7] & 4) != 0) {
			return cameraPlane;
		} else {
			return 3;
		}
	}

	public void method26(boolean flag) {
		for(int j = 0; j < npcListSize; j++) {
			final NPC npc = npcList[npcEntryList[j]];
			long hash = 0x4000000000L + (npcEntryList[j] << 14);
			if(npc == null || !npc.isVisible() || npc.type.visible != flag) {
				continue;
			}
			final int l = npc.x >> 7;
			final int i1 = npc.y >> 7;
			if(l < 0 || l >= 104 || i1 < 0 || i1 >= 104) {
				continue;
			}
			if(npc.anInt1540 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
				if(anIntArrayArray929[l][i1] == anInt1265) {
					continue;
				}
				anIntArrayArray929[l][i1] = anInt1265;
			}
			if(!npc.type.clickable) {
				hash += 0x100000000L;
			}
			scene.addEntity(cameraPlane, npc.yaw, method42(cameraPlane, npc.x, npc.y), hash, npc.y, (npc.anInt1540 - 1) * 64 + 60, npc.x, npc, npc.aBoolean1541);
		}
	}

	public void method37() {//TODO: REMOVED, add it back efficiently.
		/*if(Config.MOVING_TEXTURES) {
			if(Rasterizer3D.textureCacheIndex[17] >= j) {
				final PaletteImage background = Rasterizer3D.textureImage[17];
				final int k = background.trueWidth * background.trueHeight - 1;
				final int j1 = background.trueWidth * anInt945 * 2;
				final byte abyte0[] = background.entryList;
				final byte abyte3[] = aByteArray912;
				for(int i2 = 0; i2 <= k; i2++) {
					abyte3[i2] = abyte0[i2 - j1 & k];
				}
				background.entryList = abyte3;
				aByteArray912 = abyte0;
				Rasterizer3D.saveTextureToCache(17);
				pkt226Count++;
				if(pkt226Count > 1235) {
					pkt226Count = 0;
					outBuffer.putOpcode(226);
					outBuffer.putByte(0);
					final int l2 = outBuffer.pos;
					outBuffer.putShort(58722);
					outBuffer.putByte(240);
					outBuffer.putShort((int) (Math.random() * 65536D));
					outBuffer.putByte((int) (Math.random() * 256D));
					if((int) (Math.random() * 2D) == 0) {
						outBuffer.putShort(51825);
					}
					outBuffer.putByte((int) (Math.random() * 256D));
					outBuffer.putShort((int) (Math.random() * 65536D));
					outBuffer.putShort(7130);
					outBuffer.putShort((int) (Math.random() * 65536D));
					outBuffer.putShort(61657);
					outBuffer.putLengthAfterwards(outBuffer.pos - l2);
				}
			}
			if(Rasterizer3D.textureCacheIndex[24] >= j) {
				final PaletteImage image = Rasterizer3D.textureImage[24];
				final int len = image.trueWidth * image.trueHeight - 1;
				final int shift = image.trueWidth * anInt945 * 2;
				final byte abyte1[] = image.entryList;
				final byte abyte4[] = aByteArray912;
				for(int j2 = 0; j2 <= len; j2++) {
					abyte4[j2] = abyte1[j2 - shift & len];
				}
				image.entryList = abyte4;
				aByteArray912 = abyte1;
				Rasterizer3D.saveTextureToCache(24);
			}
			if(Rasterizer3D.textureCacheIndex[34] >= j) {
				final PaletteImage background_2 = Rasterizer3D.textureImage[34];
				final int i1 = background_2.trueWidth * background_2.trueHeight - 1;
				final int l1 = background_2.trueWidth * anInt945 * 2;
				final byte abyte2[] = background_2.entryList;
				final byte abyte5[] = aByteArray912;
				for(int k2 = 0; k2 <= i1; k2++) {
					abyte5[k2] = abyte2[k2 - l1 & i1];
				}
				background_2.entryList = abyte5;
				aByteArray912 = abyte2;
				Rasterizer3D.saveTextureToCache(34);
			}
			if(Rasterizer3D.textureCacheIndex[40] >= j) {
				final PaletteImage background_2 = Rasterizer3D.textureImage[40];
				final int i1 = background_2.trueWidth * background_2.trueHeight - 1;
				final int l1 = background_2.trueWidth * anInt945 * 2;
				final byte abyte2[] = background_2.entryList;
				final byte abyte5[] = aByteArray912;
				for(int k2 = 0; k2 <= i1; k2++) {
					abyte5[k2] = abyte2[k2 - l1 & i1];
				}
				background_2.entryList = abyte5;
				aByteArray912 = abyte2;
				Rasterizer3D.saveTextureToCache(40);
			}
		}*/
	}

	public void method38() {
		for(int i = -1; i < playerCount; i++) {
			int j;
			if(i == -1) {
				j = localPlayerIndex;
			} else {
				j = playerEntryList[i];
			}
			final Player player = playerList[j];
			if(player != null && player.chatLoopCycle > 0) {
				player.chatLoopCycle--;
				if(player.chatLoopCycle == 0) {
					player.chatSpoken = null;
				}
			}
		}
		for(int k = 0; k < npcListSize; k++) {
			final int l = npcEntryList[k];
			final NPC npc = npcList[l];
			if(npc != null && npc.chatLoopCycle > 0) {
				npc.chatLoopCycle--;
				if(npc.chatLoopCycle == 0) {
					npc.chatSpoken = null;
				}
			}
		}
	}

	public int method42(int plane, int x, int y) {
		final int tilex = x >> 7;
		final int tiley = y >> 7;
		if(tilex < 0 || tiley < 0 || tilex > 103 || tiley > 103) {
			return 0;
		}
		int p = plane;
		if(p < 3 && (tiles[1][tilex][tiley] & 2) == 2) {
			p++;
		}
		final int x2 = x & 0x7f;
		final int y2 = y & 0x7f;
		final int i2 = sceneGroundZ[p][tilex][tiley] * (128 - x2) + sceneGroundZ[p][tilex + 1][tiley] * x2 >> 7;
		final int j2 = sceneGroundZ[p][tilex][tiley + 1] * (128 - x2) + sceneGroundZ[p][tilex + 1][tiley + 1] * x2 >> 7;
		return i2 * (128 - y2) + j2 * y2 >> 7;
	}

	public void method47(boolean flag) {
		if(localPlayer.x >> 7 == walkX && localPlayer.y >> 7 == walkY) {
			walkX = 0;
		}
		int count = playerCount;
		if(flag) {
			count = 1;
		}
		for(int l = 0; l < count; l++) {
			Player player;
			long hash;
			if(flag) {
				player = localPlayer;
				hash = localPlayerIndex << 14;
			} else {
				player = playerList[playerEntryList[l]];
				hash = playerEntryList[l] << 14;
			}
			if(player == null || !player.isVisible()) {
				continue;
			}
			player.noTransform = (!Config.def.isLOW_MEM() && playerCount > 50 || playerCount > 200) && !flag && player.idleAnim == player.anInt1511;
			final int j1 = player.x >> 7;
			final int k1 = player.y >> 7;
			if(j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104) {
				continue;
			}
			if(player.aModel_1714 != null && loopCycle >= player.anInt1707 && loopCycle < player.anInt1708) {
				player.noTransform = false;
				player.anInt1709 = method42(cameraPlane, player.x, player.y);
				scene.addPlayer(cameraPlane, player.y, player, player.yaw, player.anInt1722, player.x, player.anInt1709, player.anInt1719, player.anInt1721, hash, player.anInt1720);
				continue;
			}
			if((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
				if(anIntArrayArray929[j1][k1] == anInt1265) {
					continue;
				}
				anIntArrayArray929[j1][k1] = anInt1265;
			}
			player.anInt1709 = method42(cameraPlane, player.x, player.y);
			scene.addEntity(cameraPlane, player.yaw, player.anInt1709, hash, player.y, 60, player.x, player, player.aBoolean1541);
		}
	}

	public void method55() {
		for(Projectile projectile = (Projectile) aClass19_1013.getFirst(); projectile != null; projectile = (Projectile) aClass19_1013.getNext()) {
			if(projectile.anInt1597 != cameraPlane || loopCycle > projectile.anInt1572) {
				projectile.unlinkPrimary();
			} else if(loopCycle >= projectile.anInt1571) {
				if(projectile.anInt1590 > 0) {
					final NPC npc = npcList[projectile.anInt1590 - 1];
					if(npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312) {
						projectile.method455(loopCycle, npc.y, method42(projectile.anInt1597, npc.x, npc.y) - projectile.anInt1583, npc.x);
					}
				}
				if(projectile.anInt1590 < 0) {
					final int j = -projectile.anInt1590 - 1;
					Player player;
					if(j == unknownInt10) {
						player = localPlayer;
					} else {
						player = playerList[j];
					}
					if(player != null && player.x >= 0 && player.x < 13312 && player.y >= 0 && player.y < 13312) {
					//	projectile.method455(loopCycle, player.y, method42(projectile.anInt1597, player.x, player.y) - projectile.anInt1583, player.x);
					}
				}
				projectile.method456(anInt945);
				scene.addEntity(cameraPlane, projectile.anInt1595, (int) projectile.aDouble1587, -1, (int) projectile.aDouble1586, 60, (int) projectile.aDouble1585, projectile, false);
			}
		}

	}

	public void method70() {
		anInt1251 = 0;
		final int j = (localPlayer.x >> 7) + baseX;
		final int k = (localPlayer.y >> 7) + baseY;
		if(j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136) {
			anInt1251 = 1;
		}
		if(j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535) {
			anInt1251 = 1;
		}
		if(anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062) {
			anInt1251 = 0;
		}
	}

	public void method73() {
		do {
			final int j = getKey();
			if(j == -1) {
				break;
			}
			if(j == 9) {
				tabToReplyPm();
				return;
			}
			if(j == 32 && (forcedChatWidgetId == 356 || forcedChatWidgetId == 359 || forcedChatWidgetId == 363 || forcedChatWidgetId == 368 || forcedChatWidgetId == 306) && !aBoolean1149) {
				outBuffer.putOpcode(40);
				aBoolean1149 = true;
			}
			if(openInterfaceID != -1 && openInterfaceID == reportAbuseInterfaceID) {
				if(j == 8 && reportAbuseInput.length() > 0) {
					reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
				}
				if((j >= 97 && j <= 122 || j >= 65 && j <= 90 || j >= 48 && j <= 57 || j == 32) && reportAbuseInput.length() < 12) {
					reportAbuseInput += (char) j;
				}
			} else if(messagePromptRaised) {
				if(j >= 32 && j <= 122 && promptInput.length() < 80) {
					promptInput += (char) j;
					if(panelSearch)
						panelSearchInput += (char) j;
				}
				if(j == 8 && promptInput.length() > 0) {
					promptInput = promptInput.substring(0, promptInput.length() - 1);
					if(panelSearch)
						panelSearchInput = panelSearchInput.substring(0, panelSearchInput.length() - 1);
				}
				if(j == 13 || j == 10) {
					messagePromptRaised = false;
					if(marketSearch && panelSearch) {
						outBuffer.putOpcode(105);
						outBuffer.putLong(StringUtils.encryptName(panelSearchInput));
						marketSearch = false;
						panelSearchInput = null;
						promptInput = "";
						promptInputTitle = "";
						inputDialogState = 0;
					}
					if(npcSug) {
						if(panelSearch) {
							npcSugMin = 0;
							npcSugMax = 0;
							panelSearchInput = null;
							promptInput = "";
							promptInputTitle = "";
							inputDialogState = 0;
							npcSug = false;
							return;
						}
						if(panelSearchInput == null || panelSearchInput.length() == 0) {
							panelSearchInput = promptInput;
							promptInputTitle = "Minimum count?";
							inputDialogState = 1;
						} else {
							ObjectType item = null;
							for(int i = 0; i < 22520; i++) {
								try {
									ObjectType obj = ObjectType.get(i);
									if(obj == null)
										continue;
									if(obj.name == null)
										continue;
									if(Objects.equals(obj.name.toLowerCase(), panelSearchInput.toLowerCase())) {
										item = obj;
										break;
									}
								} catch(Exception e) {
									
								}
							}
							DropPanel.Chance ch = DropPanel.getChance(promptInput);
							if(ch == null)
								pushMessage("Couldn't resolve the rarity of your suggested drop.", 1, "client");
							else if(item == null)
								pushMessage("Couldn't find the item you suggested.", 1, "client");
							else {
								outBuffer.putOpcode(19);
								outBuffer.putByte(ch.ordinal());
								outBuffer.putShort(item.id);
								outBuffer.putShort(npcSugMin);
								outBuffer.putShort(npcSugMin);
							}
							npcSugMin = 0;
							npcSugMax = 0;
							panelSearchInput = null;
							promptInput = "";
							promptInputTitle = "";
							inputDialogState = 0;
							npcSug = false;
						}
						return;
					}
					if(friendsListAction == 1) {
						final long l = StringUtils.encryptName(promptInput);
						addFriend(l);
					}
					if(friendsListAction == 2 && friendsCount > 0) {
						final long l1 = StringUtils.encryptName(promptInput);
						delFriend(l1);
					}
					if(friendsListAction == 3 && promptInput.length() > 0) {
						outBuffer.putOpcode(126);
						outBuffer.putByte(0);
						final int k = outBuffer.pos;
						outBuffer.putLong(aLong953);
						StringEncoder.putString(promptInput, outBuffer);
						outBuffer.putLengthAfterwards(outBuffer.pos - k);
						promptInput = StringEncoder.processInput(promptInput);
						// promptInput = Censor.doCensor(promptInput);
						pushMessage(promptInput, 6, StringUtils.formatName(StringUtils.decryptName(aLong953)));
						if(privateChatMode == 2) {
							privateChatMode = 1;
							outBuffer.putOpcode(95);
							outBuffer.putByte(publicChatMode);
							outBuffer.putByte(privateChatMode);
							outBuffer.putByte(tradeMode);
						}
					}
					if(friendsListAction == 4 && ignoreCount < 100) {
						final long l2 = StringUtils.encryptName(promptInput);
						addIgnore(l2);
					}
					if(friendsListAction == 5 && ignoreCount > 0) {
						final long l3 = StringUtils.encryptName(promptInput);
						delIgnore(l3);
					}
					if(friendsListAction == 6) {
						if(amountOrNameInput.length() > 0) {
							outBuffer.putOpcode(60);
							outBuffer.putLong(StringUtils.encryptName(amountOrNameInput));
						}
					}
				}
			} else if(inputDialogState == 1) {
				if(j == 107 || j == 75 || j == 109 || j == 77) {//k(107/75), m(109/77) inputs.
					if(outBoundInput.length() == 0)
						outBoundInput += (char) j;
					return;
				}
				if(j >= 48 && j <= 57 && amountOrNameInput.length() < 10) {
					amountOrNameInput += (char) j;
				}
				if(j == 8 && (amountOrNameInput.length() > 0 || outBoundInput.length() > 0)) {
					if(outBoundInput.length() > 0) {
						outBoundInput = outBoundInput.substring(0, outBoundInput.length() - 1);
						return;
					}
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
				}
				if(j == 13 || j == 10) {
					if(amountOrNameInput.length() > 0) {
						int i1 = 0;
						try {
							i1 = Integer.parseInt(amountOrNameInput);
						} catch(final Exception ignored) {}
						if(outBoundInput.length() > 0) {
							if(outBoundInput.contains("k") || outBoundInput.contentEquals("K")) {
								i1 *= 1000;
							}
							if(outBoundInput.contains("m") || outBoundInput.contentEquals("M")) {
								i1 *= 1000000;
							}
							outBoundInput = "";
						}
						if(npcSug) {
							//npc suggestion output.
							if(npcSugMin > 0) {
								npcSugMax = i1;
								inputDialogState = 0;
								amountOrNameInput = "";
								promptInput = "";
								promptInputTitle = "rarity? always,common,uncommon,rare,very_rare,extremely_rare";
								messagePromptRaised = true;
							} else {
								npcSugMin = i1;
								promptInputTitle = "Maximum count?";
								inputDialogState = 1;
								amountOrNameInput = "";
							}
							return;
						}
						outBuffer.putOpcode(208);
						outBuffer.putInt(i1);
					}
					inputDialogState = 0;
				}
			} else if(inputDialogState == 2) {
				if(j >= 32 && j <= 122 && amountOrNameInput.length() < 12) {
					amountOrNameInput += (char) j;
				}
				if(j == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
				}
				if(j == 13 || j == 10) {
					if(amountOrNameInput.length() > 0) {
						outBuffer.putOpcode(60);
						outBuffer.putLong(StringUtils.encryptName(amountOrNameInput));
					}
					inputDialogState = 0;
				}
			} else if(panelHandler.isBankpen() && bankSearching) {
				if(j >= 32 && j <= 122 && bankSearch.length() < 80) {
					bankSearch += (char) j;
				}
				if(j == 8 && bankSearch.length() > 0) {
					bankSearch = bankSearch.substring(0, bankSearch.length() - 1);
				}
			} else if(forcedChatWidgetId == -1) {
				if(j >= 32 && j <= 122 && chatInput.length() < (uiRenderer.id == 1 ? 90 : 70)) {
					chatInput += (char) j;
				}
				if(j == 8 && chatInput.length() > 0) {
					chatInput = chatInput.substring(0, chatInput.length() - 1);
				}
				if((j == 13 || j == 10) && chatInput.length() > 0) {
					if(chatInput.equals("::gc")) {
						System.gc();
						final Runtime runtime = Runtime.getRuntime();
						final int mem = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
						pushMessage("--> mem: " + mem + "k", 0, "");
					}
					if(chatInput.equals("::fps")) {
						Config.def.setFPS_ON(!Config.def.isFPS_ON());
						pushMessage("--> fps " + (Config.def.isFPS_ON() ? "on" : "off"), 0, "");
					}
					if(localPrivilege == 4) {
						if(chatInput.startsWith("//setspecto")) {
							final int amt = Integer.parseInt(chatInput.substring(12));
							anIntArray1045[300] = amt;
							if(variousSettings[300] != amt) {
								variousSettings[300] = amt;
								handleSettings(300);
							}
						}
						if(chatInput.equals("clientdrop")) {
							dropClient();
						}
						if(chatInput.startsWith("full")) {
							try {
								final String[] args = chatInput.split(" ");
								final int id1 = Integer.parseInt(args[1]);
								final int id2 = Integer.parseInt(args[2]);
								fullscreenWidgetId = id1;
								openInterfaceID = id2;
								pushMessage("Opened Interface", 0, "");
							} catch(final Exception e) {
								pushMessage("Interface Failed to load", 0, "");
							}
						}
						if(chatInput.equals("::msg1")) {
							pushMessage("Game message.", 0, "");
							pushMessage("Chat message.", 1, "@cr1@Edgeville");
							pushMessage("Received private message.", 2, "@cr2@Edgeville");
							pushMessage("wishes to trade with you.", 4, "Edgeville");
							pushMessage("Edgeville has logged in/out.", 5, "");
							pushMessage("Sent private message.", 6, "@cr6@Edgeville");
							pushMessage("Clan message.", 7, "@cr7@Clan Name:Clanmate Name");
							pushMessage("sent some request.", 8, "Edgeville");
						}
						if(chatInput.equals("::msg2")) {
							pushMessage("Regular", 1, "Edgeville");
							pushMessage("Moderator", 1, "@cr1@Edgeville");
							pushMessage("Super-Moderator", 1, "@cr2@Edgeville");
							pushMessage("Administrator", 1, "@cr3@Edgeville");
							pushMessage("Developer", 1, "@cr4@Edgeville");
							pushMessage("Designer", 1, "@cr5@Edgeville");
							pushMessage("Respected-Member", 1, "@cr6@Edgeville");
							pushMessage("Donator", 1, "@cr7@Edgeville");
							pushMessage("Super-Donator", 1, "@cr8@Edgeville");
							pushMessage("Extreme-Donator", 1, "@cr9@Edgeville");
						}
						if(chatInput.equals("::commands")) {
							pushMessage("--> commands", 0, "");
							pushMessage("::debugdat / ::debugidx - Toggle debug", 0, "");
							pushMessage("::reint - Reloads the interfaces", 0, "");
							pushMessage("::repackimg - Repacks the image index", 0, "");
							pushMessage("::gc - Forces garbage collection and prints the debug data", 0, "");
							pushMessage("::data - Prints the debug data", 0, "");
							pushMessage("::noclip - Toggles clipping", 0, "");
							pushMessage("::logout / ::login_[name]_[pass] / ::relog - Might be useful", 0, "");
							pushMessage("::ortho - Toggle between orthographic and perspective views", 0, "");
							pushMessage("::cls - Clears the chatbox", 0, "");
							pushMessage("::commands - This command", 0, "");
						} else if(chatInput.equals("::dat")) {
							Config.def.setDEBUG_DATA(!Config.def.isDEBUG_DATA());
							pushMessage("--> data debug " + (Config.def.isDEBUG_DATA() ? "on" : "off"), 0, "");
						} else if(chatInput.equals("::repackimg")) {
							try {
								ImagePacker.pack(this, true);
								pushMessage("--> repacked image index ", 0, "");
							} catch(IOException e) {
								e.printStackTrace();
								pushMessage("--> failed to repack image index", 0, "");
							}
						} else if(chatInput.equals("::idx")) {
							Config.def.setDEBUG_INDEXES(!Config.def.isDEBUG_INDEXES());
							pushMessage("--> index debug " + (Config.def.isDEBUG_INDEXES() ? "on" : "off"), 0, "");
						} else if(chatInput.equals("::data")) {
							final Runtime runtime = Runtime.getRuntime();
							final int mem = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
							pushMessage("--> fps: " + super.fps + ", mem: " + mem + "k", 0, "");
						} else if(chatInput.equals("::cls")) {
							for(int j1 = 0; j1 < 500; j1++) {
								chatMessage[j1] = null;
							}
						} else if(chatInput.equals("::noclip")) {
							noclip = !noclip;
							pushMessage("--> noclip " + (noclip ? "on" : "off"), 0, "");
						} else if(chatInput.equals("::logout")) {
							logOut();
						} else if(chatInput.startsWith("::login_")) {
							String[] args = chatInput.substring(8).split("_");
							if(args.length == 2) {
								logOut();
								for(int i = 0; i < 5; i++) {
									connect(args[0], args[1]);
									if(loggedIn) {
										break;
									}
									try {
										Thread.sleep(1000L);
									} catch(InterruptedException e) {
									}
								}
							}
						} else if(chatInput.equals("::relog")) {
							logOut();
							for(int i = 0; i < 5; i++) {
								connect(localUsername, localPassword);
								if(loggedIn) {
									break;
								}
								try {
									Thread.sleep(1000L);
								} catch(InterruptedException e) {
								}
							}
						} else if(chatInput.equals("::packobj")) {
							try {
								LocationType.pack();
							} catch(IOException e) {
								e.printStackTrace();
							}
						} else if(chatInput.equals("::new")) {
							for(int f : terrainDataIds) {
								onDemandRequester.setNew(f);
							}
							for(int o : objectDataIds) {
								onDemandRequester.setNew(o);
							}
							onDemandRequester.packMapIndex();
							loadRegion();
							pushMessage("set new", 0, "client");
						} else if(chatInput.equals("::old")) {
							for(int f : terrainDataIds) {
								onDemandRequester.setOld(f);
							}
							for(int o : objectDataIds) {
								onDemandRequester.setOld(o);
							}
							onDemandRequester.packMapIndex();
							loadRegion();
							pushMessage("set old", 0, "client");
						} else if(chatInput.equals("::maps")) {
							System.out.println("Started repacking region maps.");
							
							for(int f : terrainDataIds) {
								File file = new File(SignLink.getCacheDir() + "maps/" + f + ".gz");
								if(file.exists()) {
									byte[] data = new byte[(int) file.length()];
									FileInputStream fis = null;
									try {
										fis = new FileInputStream(file);
										fis.read(data);
										fis.close();
									} catch(Exception e) {
										e.printStackTrace();
									}
									if(data != null && data.length > 0) {
										cacheIdx[4].writeFile(data.length, data, f);
										System.out.println("Repacked " + f + ".");
									} else {
										System.out.println("Unable to locate index " + f + ".");
									}
								}
							}
							for(int o : objectDataIds) {
								File file = new File(SignLink.getCacheDir() + "maps/" + o + ".gz");
								if(file.exists()) {
									byte[] data = new byte[(int) file.length()];
									FileInputStream fis = null;
									try {
										fis = new FileInputStream(file);
										fis.read(data);
										fis.close();
									} catch(Exception e) {
										e.printStackTrace();
									}
									if(data != null && data.length > 0) {
										cacheIdx[4].writeFile(data.length, data, o);
										System.out.println("Repacked " + o + ".");
									} else {
										System.out.println("Unable to locate index " + o + ".");
									}
								}
							}
							onDemandRequester.packMapIndex();
							loadRegion();
						} else if(chatInput.equals("::mapsi")) {
							System.out.println("repacking map index.");
							onDemandRequester.packMapIndex();
							loadRegion();
						}
						if(chatInput.equals("::roll")) {
							rollCharacterInInterface = !rollCharacterInInterface;
						}
						if(chatInput.equals("::fog")) {
							Config.def.setSMOOTH_FOG(!Config.def.isSMOOTH_FOG());
							pushMessage("--> fog " + (Config.def.isSMOOTH_FOG() ? "on" : "off"), 0, "");
						}
						if(chatInput.equals("::tween")) {
							Config.def.setTWEENING(!Config.def.isTWEENING());
							pushMessage("--> tween " + (Config.def.isTWEENING() ? "on" : "off"), 0, "");
						}
						if(chatInput.equals("::mat")) {
							Config.def.setGROUND_MATERIALS(!Config.def.isGROUND_MATERIALS());
							pushMessage("--> mat " + (Config.def.isGROUND_MATERIALS() ? "on" : "off"), 0, "");
						}
						if(chatInput.startsWith("hitmark")) {
							try {
								final String[] args = chatInput.split(" ");
								Config.def.setHITSPLATS(Integer.parseInt(args[1]));
							} catch(final Exception e) {
								pushMessage("Interface Failed to load", 0, "");
							}
						}
						if(chatInput.startsWith("::msg_")) {
							String[] args = chatInput.substring(6).split("_");
							if(args.length == 3) {
								try {
									pushMessage(args[1], Integer.parseInt(args[2]), args[0]);
								} catch(Exception e) {
								}
							}
						}
						if(chatInput.equals("::m0") || chatInput.equals("::m1") || chatInput.equals("::m2")) {
							setMode(Integer.parseInt(chatInput.substring(3)));
						}
						if(chatInput.equals("::custom")) {
							uiRenderer.switchRevision(1);
						}
						if(chatInput.equals("::osrs")) {
							uiRenderer.switchRevision(2);
						}
						if(chatInput.equals("::459")) {
							uiRenderer.switchRevision(459);
						}
						if(chatInput.equals("::525")) {
							uiRenderer.switchRevision(525);
						}
						if(chatInput.equals("::562")) {
							uiRenderer.switchRevision(562);
						}
						if(chatInput.equals("::packinterface")) {
							Interface.pack();
						}
						if(chatInput.equals("::reint")) {
							new InterfaceLoader(cacheHandler.getCacheArchive(3, "interface", CacheUnpacker.EXPECTED_CRC[3])).run(this);
						}
						if(chatInput.equals("::task")) {
							taskHandler.completeTask("Dragon Slayer\nKill 100 dragons.");
						}
						if(chatInput.equals("::task2")) {
							taskHandler.completeTask("Dragon Slayer\nKill 100 dragons.");
							taskHandler.completeTask("Another task!\nUnknown.\n\nSpecial information\nhere.");
						}
					}
					if(chatInput.startsWith("::")) {
						outBuffer.putOpcode(103);
						outBuffer.putByte(chatInput.length() - 1);
						outBuffer.putLine(chatInput.substring(2));
					} else if(chatInput.startsWith("/")) {
						outBuffer.putOpcode(104);
						outBuffer.putByte(chatInput.length());
						outBuffer.putLine(chatInput.substring(1));
					} else {
						String s = chatInput.toLowerCase();
						int chatColorEffect = 0;
						if(s.startsWith("yellow:")) {
							chatColorEffect = 0;
							chatInput = chatInput.substring(7);
						} else if(s.startsWith("red:")) {
							chatColorEffect = 1;
							chatInput = chatInput.substring(4);
						} else if(s.startsWith("green:")) {
							chatColorEffect = 2;
							chatInput = chatInput.substring(6);
						} else if(s.startsWith("cyan:")) {
							chatColorEffect = 3;
							chatInput = chatInput.substring(5);
						} else if(s.startsWith("purple:")) {
							chatColorEffect = 4;
							chatInput = chatInput.substring(7);
						} else if(s.startsWith("white:")) {
							chatColorEffect = 5;
							chatInput = chatInput.substring(6);
						} else if(s.startsWith("blue:")) {
							chatColorEffect = 6;
							chatInput = chatInput.substring(5);
						} else if(s.startsWith("orange:")) {
							chatColorEffect = 7;
							chatInput = chatInput.substring(7);
						} else if(s.startsWith("flash1:")) {
							chatColorEffect = 8;
							chatInput = chatInput.substring(7);
						} else if(s.startsWith("flash2:")) {
							chatColorEffect = 9;
							chatInput = chatInput.substring(7);
						} else if(s.startsWith("flash3:")) {
							chatColorEffect = 10;
							chatInput = chatInput.substring(7);
						} else if(s.startsWith("glow1:")) {
							chatColorEffect = 11;
							chatInput = chatInput.substring(6);
						} else if(s.startsWith("glow2:")) {
							chatColorEffect = 12;
							chatInput = chatInput.substring(6);
						} else if(s.startsWith("glow3:")) {
							chatColorEffect = 13;
							chatInput = chatInput.substring(6);
						}
						s = chatInput.toLowerCase();
						int chatAnimationEffect = 0;
						if(s.startsWith("wave:")) {
							chatAnimationEffect = 1;
							chatInput = chatInput.substring(5);
						} else if(s.startsWith("wave2:")) {
							chatAnimationEffect = 2;
							chatInput = chatInput.substring(6);
						} else if(s.startsWith("shake:")) {
							chatAnimationEffect = 3;
							chatInput = chatInput.substring(6);
						} else if(s.startsWith("scroll:")) {
							chatAnimationEffect = 4;
							chatInput = chatInput.substring(7);
						} else if(s.startsWith("slide:")) {
							chatAnimationEffect = 5;
							chatInput = chatInput.substring(6);
						}

						outBuffer.putOpcode(4);
						outBuffer.putByte(0);
						final int j3 = outBuffer.pos;
						outBuffer.putReversedByte(chatAnimationEffect);
						outBuffer.putReversedByte(chatColorEffect);
						aStream_834.pos = 0;
						StringEncoder.putString(chatInput, aStream_834);
						outBuffer.putBytesReversedOrderPlus128(aStream_834.data, 0, aStream_834.pos);
						outBuffer.putLengthAfterwards(outBuffer.pos - j3);
						chatInput = StringEncoder.processInput(chatInput);
						localPlayer.chatSpoken = chatInput;
						localPlayer.chatColorEffect = chatColorEffect;
						localPlayer.chatAnimationEffect = chatAnimationEffect;
						localPlayer.chatLoopCycle = 150;
						if(localPrivilege >= 1)
							pushMessage(localPlayer.chatSpoken, 1, "@cr" + localPrivilege + "@" + localPlayer.name);
						else
							pushMessage(localPlayer.chatSpoken, 1, localPlayer.name);
						if(publicChatMode == 2) {
							publicChatMode = 3;
							outBuffer.putOpcode(95);
							outBuffer.putByte(publicChatMode);
							outBuffer.putByte(privateChatMode);
							outBuffer.putByte(tradeMode);
						}
					}
					chatInput = "";
				}
			}
		} while(true);
	}

	public void method95() {
		for(int j = 0; j < npcListSize; j++) {
			final int k = npcEntryList[j];
			final NPC npc = npcList[k];
			if(npc != null) {
				method96(npc);
			}
		}
	}

	private void method100(Mobile entity) {
		if(entity.anInt1504 == 0) {
			return;
		}
		if(entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
			final NPC npc = npcList[entity.interactingEntity];
			if(npc != null) {
				final int i1 = entity.x - npc.x;
				final int k1 = entity.y - npc.y;
				if(i1 != 0 || k1 != 0) {
					entity.turnDirection = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
				}
			}
		}
		if(entity.interactingEntity >= 32768) {
			int j = entity.interactingEntity - 32768;
			if(j == unknownInt10) {
				j = localPlayerIndex;
			}
			final Player player = playerList[j];
			if(player != null) {
				final int l1 = entity.x - player.x;
				final int i2 = entity.y - player.y;
				if(l1 != 0 || i2 != 0) {
					entity.turnDirection = (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
				}
			}
		}
		if((entity.anInt1538 != 0 || entity.anInt1539 != 0) && (entity.smallXYIndex == 0 || entity.anInt1503 > 0)) {
			final int k = entity.x - (entity.anInt1538 - baseX - baseX) * 64;
			final int j1 = entity.y - (entity.anInt1539 - baseY - baseY) * 64;
			if(k != 0 || j1 != 0) {
				entity.turnDirection = (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
			}
			entity.anInt1538 = 0;
			entity.anInt1539 = 0;
		}
		final int l = entity.turnDirection - entity.yaw & 0x7ff;
		if(l != 0) {
			if(l < entity.anInt1504 || l > 2048 - entity.anInt1504) {
				entity.yaw = entity.turnDirection;
			} else if(l > 1024) {
				entity.yaw -= entity.anInt1504;
			} else {
				entity.yaw += entity.anInt1504;
			}
			entity.yaw &= 0x7ff;
			if(entity.idleAnim == entity.anInt1511 && entity.yaw != entity.turnDirection) {
				if(entity.anInt1512 != -1) {
					entity.idleAnim = entity.anInt1512;
					return;
				}
				entity.idleAnim = entity.anInt1554;
			}
		}
	}

	private void method101(Mobile entity) {
		entity.aBoolean1541 = false;
		if(entity.idleAnim != -1) {
			if(entity.idleAnim > DeformSequence.cache.length)
				return;
			DeformSequence animation = DeformSequence.cache[entity.idleAnim];
			if(animation == null)
				return;
			entity.idleAnimCycle++;
			if(entity.idleAnimFrame < animation.length && entity.idleAnimCycle > animation.getFrame(entity.idleAnimFrame)) {
				entity.idleAnimCycle -= animation.getFrame(entity.idleAnimFrame);
				entity.idleAnimFrame++;
			}
			if(entity.idleAnimFrame >= animation.length) {
				entity.idleAnimFrame = 0;
			}
			if(Config.def.isTWEENING()) {
				entity.nextIdleAnimFrame = entity.idleAnimFrame + 1;
			}
			if(entity.nextIdleAnimFrame >= animation.length) {
				entity.nextIdleAnimFrame = -1;
			}
		}
		if(entity.spotAnim != -1 && loopCycle >= entity.anInt1523) {
			if(entity.spotAnimFrame < 0) {
				entity.spotAnimFrame = 0;
			}
			if(entity.spotAnim > SpotAnimation.cache.length || SpotAnimation.cache[entity.spotAnim] == null)
				return;
			if(entity.spotAnim > SpotAnimation.cache.length)
				return;
			final DeformSequence animation_1 = SpotAnimation.cache[entity.spotAnim].animFrameSequence;
			if(animation_1 == null)
				return;
			for(entity.spotAnimCycle++; entity.spotAnimFrame < animation_1.length && entity.spotAnimCycle > animation_1.getFrame(entity.spotAnimFrame); entity.spotAnimFrame++) {
				entity.spotAnimCycle -= animation_1.getFrame(entity.spotAnimFrame);
			}
			if(entity.spotAnimFrame >= animation_1.length && (entity.spotAnimFrame < 0 || entity.spotAnimFrame >= animation_1.length)) {
				entity.spotAnim = -1;
			}
			if(Config.def.isTWEENING()) {
				entity.nextSpotAnimFrame = entity.spotAnimFrame + 1;
			}
			if(entity.nextSpotAnimFrame >= animation_1.length) {
				entity.nextSpotAnimFrame = -1;
			}
		}
		if(entity.anim != -1 && entity.animDelay <= 1) {
			if(entity.anim > DeformSequence.cache.length) {
				return;
			}
			final DeformSequence animation_2 = DeformSequence.cache[entity.anim];
			if(animation_2 == null)
				return;
			if(animation_2.precedenceAnimating == 1 && entity.anInt1542 > 0 && entity.anInt1547 <= loopCycle && entity.anInt1548 < loopCycle) {
				entity.animDelay = 1;
				return;
			}
		}
		if(entity.anim != -1 && entity.animDelay == 0) {
			if(entity.anim > DeformSequence.cache.length) {
				return;
			}
			final DeformSequence animation_3 = DeformSequence.cache[entity.anim];
			if(animation_3 == null)
				return;
			for(entity.animCycle++; entity.animFrame < animation_3.length && entity.animCycle > animation_3.getFrame(entity.animFrame); entity.animFrame++) {
				entity.animCycle -= animation_3.getFrame(entity.animFrame);
			}
			if(entity.animFrame >= animation_3.length) {
				entity.animFrame -= animation_3.animCycle;
				entity.anInt1530++;
				if(entity.anInt1530 >= animation_3.maximumLoops) {
					entity.anim = -1;
				} else if(entity.animFrame < 0 || entity.animFrame >= animation_3.length) {
					entity.anim = -1;
				}
			}
			if(Config.def.isTWEENING()) {
				entity.nextAnimFrame = entity.animFrame + 1;
			}
			if(entity.nextAnimFrame >= animation_3.length) {
				entity.nextAnimFrame = -1;
			}
			entity.aBoolean1541 = animation_3.dynamic;
		}
		if(entity.animDelay > 0) {
			entity.animDelay--;
		}
	}

	private void method107(int i, int j, Buffer stream, Player player) {
		if((i & 0x400) != 0) {
			player.anInt1543 = stream.getReversedUByte();
			player.anInt1545 = stream.getReversedUByte();
			player.anInt1544 = stream.getReversedUByte();
			player.anInt1546 = stream.getReversedUByte();
			player.anInt1547 = stream.getLitEndUShortMinus128() + loopCycle;
			player.anInt1548 = stream.getUShortMinus128() + loopCycle;
			player.anInt1549 = stream.getReversedUByte();
			player.method446();
		}
		if((i & 0x100) != 0) {
			player.spotAnim = stream.getLitEndUShort();
			final int k = stream.getInt();
			player.spotAnimOffset = k >> 16;
			player.anInt1523 = loopCycle + (k & 0xffff);
			player.spotAnimFrame = 0;
			player.spotAnimCycle = 0;
			if(player.anInt1523 > loopCycle) {
				player.spotAnimFrame = -1;
			}
			if(player.spotAnim == 65535) {
				player.spotAnim = -1;
			}
		}
		if((i & 8) != 0) {
			int l = stream.getLitEndUShort();
			if(l == 65535) {
				l = -1;
			}
			final int i2 = stream.getOppositeUByte();
			if(l == player.anim && l != -1) {
				final int i3 = DeformSequence.cache[l].replayMode;
				if(i3 == 1) {
					player.animFrame = 0;
					player.animCycle = 0;
					player.animDelay = i2;
					player.anInt1530 = 0;
				}
				if(i3 == 2) {
					player.anInt1530 = 0;
				}
			} else if(l == -1 || player.anim == -1 || DeformSequence.cache[l].priority >= DeformSequence.cache[player.anim].priority) {
				player.anim = l;
				player.animFrame = 0;
				player.animCycle = 0;
				player.animDelay = i2;
				player.anInt1530 = 0;
				player.anInt1542 = player.smallXYIndex;
			}
		}
		if((i & 4) != 0) {
			player.chatSpoken = stream.getLine();
			if(player.chatSpoken.charAt(0) == '~') {
				player.chatSpoken = player.chatSpoken.substring(1);
				pushMessage(player.chatSpoken, 1, player.name);
			} else if(player == localPlayer) {
				pushMessage(player.chatSpoken, 1, player.name);
			}
			player.chatColorEffect = 0;
			player.chatAnimationEffect = 0;
			player.chatLoopCycle = 150;
		}
		if((i & 0x80) != 0) {
			final int i1 = stream.getLitEndUShort();
			final int j2 = stream.getUByte();
			final int j3 = stream.getOppositeUByte();
			final int k3 = stream.pos;
			if(player.name != null && player.visible) {
				final long l3 = StringUtils.encryptName(player.name);
				boolean flag = false;
				if(j2 <= 1) {
					for(int i4 = 0; i4 < ignoreCount; i4++) {
						if(ignoreListAsLongs[i4] != l3) {
							continue;
						}
						flag = true;
						break;
					}
				}
				if(!flag && anInt1251 == 0) {
					try {
						aStream_834.pos = 0;
						stream.getBytesReversedOrder(aStream_834.data, 0, j3);
						aStream_834.pos = 0;
						final String s = StringEncoder.getString(j3, aStream_834);
						player.chatSpoken = s;
						player.chatColorEffect = i1 >> 8;
						player.privelege = j2;
						player.chatAnimationEffect = i1 & 0xff;
						player.chatLoopCycle = 150;
						if(j2 >= 1)
							pushMessage(s, 1, "@cr" + j2 + "@" + player.name);
						else
							pushMessage(s, 1, player.name);
					} catch(final Exception exception) {
						SignLink.reportError("cde2");
					}
				}
			}
			stream.pos = k3 + j3;
		}
		if((i & 1) != 0) {
			player.interactingEntity = stream.getLitEndUShort();
			if(player.interactingEntity == 65535) {
				player.interactingEntity = -1;
			}
		}
		if((i & 0x10) != 0) {
			final int j1 = stream.getOppositeUByte();
			final byte abyte0[] = new byte[j1];
			final Buffer stream_1 = new Buffer(abyte0);
			stream.getBytes(abyte0, 0, j1);
			playerBuffer[j] = stream_1;
			player.updatePlayer(stream_1);
		}
		if((i & 2) != 0) {
			player.anInt1538 = stream.getLitEndUShortMinus128();
			player.anInt1539 = stream.getLitEndUShort();
		}
		if((i & 0x20) != 0) {
			int k1 = stream.getUShort();
			int k2 = stream.getUByte();
			int icon = stream.getUByte();
			int soakDamage = stream.getUShort();
			player.updateHitData(k2, k1, loopCycle, icon, soakDamage);
			player.loopCycleStatus = loopCycle + 300;
			ConstitutionHandler handler = ConstitutionHandler.getInstance();
			player.maxHealth = stream.getUShort();
			handler.setMaxAmount(player.maxHealth);
			player.currentHealth = stream.getUShort();
			handler.setAmount(player.currentHealth);
		}
		if((i & 0x200) != 0) {
			int l1 = stream.getUShort();
			int l2 = stream.getUByte();
			int icon = stream.getUByte();
			int soakDamage = stream.getUShort();
			player.updateHitData(l2, l1, loopCycle, icon, soakDamage);
			player.loopCycleStatus = loopCycle + 300;
			ConstitutionHandler handler = ConstitutionHandler.getInstance();
			player.maxHealth = stream.getUShort();
			handler.setMaxAmount(player.maxHealth);
			player.currentHealth = stream.getUShort();
			handler.setAmount(player.currentHealth);
		}
	}

	private void method117(Buffer stream) {
		stream.beginBitAccess();
		final int j = stream.getBits(1);
		if(j == 0) {
			return;
		}
		final int k = stream.getBits(2);
		if(k == 0) {
			anIntArray894[anInt893++] = localPlayerIndex;
			return;
		}
		if(k == 1) {
			final int l = stream.getBits(3);
			localPlayer.moveInDir(false, l);
			final int k1 = stream.getBits(1);
			if(k1 == 1) {
				anIntArray894[anInt893++] = localPlayerIndex;
			}
			return;
		}
		if(k == 2) {
			final int i1 = stream.getBits(3);
			localPlayer.moveInDir(true, i1);
			final int l1 = stream.getBits(3);
			localPlayer.moveInDir(true, l1);
			final int j2 = stream.getBits(1);
			if(j2 == 1) {
				anIntArray894[anInt893++] = localPlayerIndex;
			}
			return;
		}
		if(k == 3) {
			cameraPlane = stream.getBits(2);
			final int j1 = stream.getBits(1);
			final int i2 = stream.getBits(1);
			if(i2 == 1) {
				anIntArray894[anInt893++] = localPlayerIndex;
			}
			final int k2 = stream.getBits(7);
			final int l2 = stream.getBits(7);
			localPlayer.setPos(l2, k2, j1 == 1);
		}
	}

	private void method130(int j, int k, int face, int i1, int j1, int id, int l1, int i2, int j2) {
		WorldObjectSpawn object = null;
		for(WorldObjectSpawn obj = (WorldObjectSpawn) aClass19_1179.getFirst(); obj != null; obj = (WorldObjectSpawn) aClass19_1179.getNext()) {
			if(obj.anInt1295 != l1 || obj.anInt1297 != i2 || obj.anInt1298 != j1 || obj.anInt1296 != i1) {
				continue;
			}
			object = obj;
			break;
		}

		if(object == null) {
			object = new WorldObjectSpawn();
			object.anInt1295 = l1;
			object.anInt1296 = i1;
			object.anInt1297 = i2;
			object.anInt1298 = j1;
			method89(object);
			aClass19_1179.addLast(object);
		}
		object.anInt1291 = k;
		object.id = id;
		object.face = face;
		object.anInt1302 = j2;
		object.anInt1294 = j;
	}

	private void method134(Buffer stream) {
		final int j = stream.getBits(8);
		if(j < playerCount) {
			for(int k = j; k < playerCount; k++) {
				anIntArray840[anInt839++] = playerEntryList[k];
			}
		}
		if(j > playerCount) {
			SignLink.reportError(localUsername + " Too many players");
			throw new RuntimeException("eek");
		}
		playerCount = 0;
		for(int l = 0; l < j; l++) {
			final int i1 = playerEntryList[l];
			final Player player = playerList[i1];
			final int j1 = stream.getBits(1);
			if(j1 == 0) {
				playerEntryList[playerCount++] = i1;
				player.anInt1537 = loopCycle;
			} else {
				final int k1 = stream.getBits(2);
				if(k1 == 0) {
					playerEntryList[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					anIntArray894[anInt893++] = i1;
				} else if(k1 == 1) {
					playerEntryList[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					final int l1 = stream.getBits(3);
					player.moveInDir(false, l1);
					final int j2 = stream.getBits(1);
					if(j2 == 1) {
						anIntArray894[anInt893++] = i1;
					}
				} else if(k1 == 2) {
					playerEntryList[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					final int i2 = stream.getBits(3);
					player.moveInDir(true, i2);
					final int k2 = stream.getBits(3);
					player.moveInDir(true, k2);
					final int l2 = stream.getBits(1);
					if(l2 == 1) {
						anIntArray894[anInt893++] = i1;
					}
				} else if(k1 == 3) {
					anIntArray840[anInt839++] = i1;
				}
			}
		}
	}

	private void method137(Buffer stream, int j) {
		if(j == 84) {
			final int k = stream.getUByte();
			final int j3 = anInt1268 + (k >> 4 & 7);
			final int i6 = anInt1269 + (k & 7);
			final int l8 = stream.getUShort();
			final int k11 = stream.getUShort();
			final int l13 = stream.getUShort();
			if(j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104) {
				final LinkedDeque class19_1 = sceneItems[cameraPlane][j3][i6];
				if(class19_1 != null) {
					for(ObjectStack class30_sub2_sub4_sub2_3 = (ObjectStack) class19_1.getFirst(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (ObjectStack) class19_1.getNext()) {
						if(class30_sub2_sub4_sub2_3.id != (l8 & 0x7fff) || class30_sub2_sub4_sub2_3.amount != k11) {
							continue;
						}
						class30_sub2_sub4_sub2_3.amount = l13;
						break;
					}

					spawnGroundItem(j3, i6);
				}
			}
			return;
		}
		if(j == 105) {
			final int l = stream.getUByte();
			final int k3 = anInt1268 + (l >> 4 & 7);
			final int j6 = anInt1269 + (l & 7);
			final int i9 = stream.getUShort();
			final int l11 = stream.getUByte();
			final int i14 = l11 >> 4 & 0xf;
			final int i16 = l11 & 7;
		}
		if(j == 215) {
			final int i1 = stream.getUShortMinus128();
			final int l3 = stream.getReversedUByte();
			final int k6 = anInt1268 + (l3 >> 4 & 7);
			final int j9 = anInt1269 + (l3 & 7);
			final int i12 = stream.getUShortMinus128();
			final int j14 = stream.getUShort();
			if(k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != unknownInt10) {
				final ObjectStack class30_sub2_sub4_sub2_2 = new ObjectStack();
				class30_sub2_sub4_sub2_2.id = i1;
				class30_sub2_sub4_sub2_2.amount = j14;
				if(sceneItems[cameraPlane][k6][j9] == null) {
					sceneItems[cameraPlane][k6][j9] = new LinkedDeque();
				}
				sceneItems[cameraPlane][k6][j9].addLast(class30_sub2_sub4_sub2_2);
				spawnGroundItem(k6, j9);
			}
			return;
		}
		if(j == 156) {
			final int j1 = stream.getReversedOppositeUByte();
			final int i4 = anInt1268 + (j1 >> 4 & 7);
			final int l6 = anInt1269 + (j1 & 7);
			final int k9 = stream.getUShort();
			if(i4 >= 0 && l6 >= 0 && i4 < 104 && l6 < 104) {
				final LinkedDeque class19 = sceneItems[cameraPlane][i4][l6];
				if(class19 != null) {
					for(ObjectStack item = (ObjectStack) class19.getFirst(); item != null; item = (ObjectStack) class19.getNext()) {
						if(item.id != (k9 & 0x7fff)) {
							continue;
						}
						item.unlinkPrimary();
						break;
					}

					if(class19.getFirst() == null) {
						sceneItems[cameraPlane][i4][l6] = null;
					}
					spawnGroundItem(i4, l6);
				}
			}
			return;
		}
		if(j == 160) {
			final int k1 = stream.getReversedUByte();
			final int x = anInt1268 + (k1 >> 4 & 7);
			final int y = anInt1269 + (k1 & 7);
			final int l9 = stream.getReversedUByte();
			int j12 = l9 >> 2;
			final int k14 = l9 & 3;
			final int j16 = anIntArray1177[j12];
			final int j17 = stream.getUShortMinus128();
			if(x >= 0 && y >= 0 && x < 103 && y < 103) {
				final int z1 = sceneGroundZ[cameraPlane][x][y];
				final int z2 = sceneGroundZ[cameraPlane][x + 1][y];
				final int z3 = sceneGroundZ[cameraPlane][x + 1][y + 1];
				final int z4 = sceneGroundZ[cameraPlane][x][y + 1];
				if(j16 == 0) {
					final Wall wall = scene.getWall(cameraPlane, x, y);
					if(wall != null) {
						int k21 = (int) (wall.hash >> 14 & 0xFFFFFF);
						if(j12 == 2) {
							wall.model1 = new Location(k21, 4 + k14, 2, z2, z3, z1, z4, j17, false);
							wall.model2 = new Location(k21, k14 + 1 & 3, 2, z2, z3, z1, z4, j17, false);
						} else {
							wall.model1 = new Location(k21, k14, j12, z2, z3, z1, z4, j17, false);
						}
					}
				}
				if(j16 == 1) {
					final WallDecoration walldec = scene.getWallDecor(cameraPlane, x, y);
					if(walldec != null) {
						walldec.model = new Location((int) (walldec.hash >> 14 & 0xFFFFFF), 0, 4, z2, z3, z1, z4, j17, false);
					}
				}
				if(j16 == 2) {
					final EntityUnit class28 = scene.getEntityUnit(cameraPlane, x, y);
					if(j12 == 11) {
						j12 = 10;
					}
					if(class28 != null) {
						class28.model = new Location((int) (class28.hash >> 14 & 0xFFFFFF), k14, j12, z2, z3, z1, z4, j17, false);
					}
				}
				if(j16 == 3) {
					final GroundDecoration class49 = scene.getGroundDecor(cameraPlane, x, y);
					if(class49 != null) {
						class49.model = new Location((int) (class49.hash >> 14 & 0xFFFFFF), k14, 22, z2, z3, z1, z4, j17, false);
					}
				}
			}
			return;
		}
		if(j == 147) {
			final int l1 = stream.getReversedUByte();
			final int k4 = anInt1268 + (l1 >> 4 & 7);
			final int j7 = anInt1269 + (l1 & 7);
			final int i10 = stream.getUShort();
			byte byte0 = stream.getReversedSByte();
			final int l14 = stream.getLitEndUShort();
			byte byte1 = stream.getOppositeSByte();
			final int k17 = stream.getUShort();
			final int k18 = stream.getReversedUByte();
			final int j19 = k18 >> 2;
			final int i20 = k18 & 3;
			final int l20 = anIntArray1177[j19];
			byte byte2 = stream.getSByte();
			final int l21 = stream.getUShort();
			byte byte3 = stream.getOppositeSByte();
			Player player;
			if(i10 == unknownInt10) {
				player = localPlayer;
			} else {
				player = playerList[i10];
			}
			if(player != null) {
				final LocationType class46 = LocationType.getRelative(l21);
				final int i22 = sceneGroundZ[cameraPlane][k4][j7];
				final int j22 = sceneGroundZ[cameraPlane][k4 + 1][j7];
				final int k22 = sceneGroundZ[cameraPlane][k4 + 1][j7 + 1];
				final int l22 = sceneGroundZ[cameraPlane][k4][j7 + 1];
				final Model model = class46.getModelAt(j19, i20, i22, j22, k22, l22, -1, -1, -1, -1); //XXX
				if(model != null) {
					method130(k17 + 1, -1, 0, l20, j7, 0, cameraPlane, k4, l14 + 1);
					player.anInt1707 = l14 + loopCycle;
					player.anInt1708 = k17 + loopCycle;
					player.aModel_1714 = model;
					int i23 = class46.sizeX;
					int j23 = class46.sizeY;
					if(i20 == 1 || i20 == 3) {
						i23 = class46.sizeY;
						j23 = class46.sizeX;
					}
					player.anInt1711 = k4 * 128 + i23 * 64;
					player.anInt1713 = j7 * 128 + j23 * 64;
					player.anInt1712 = method42(cameraPlane, player.anInt1711, player.anInt1713);
					if(byte2 > byte0) {
						final byte byte4 = byte2;
						byte2 = byte0;
						byte0 = byte4;
					}
					if(byte3 > byte1) {
						final byte byte5 = byte3;
						byte3 = byte1;
						byte1 = byte5;
					}
					player.anInt1719 = k4 + byte2;
					player.anInt1721 = k4 + byte0;
					player.anInt1720 = j7 + byte3;
					player.anInt1722 = j7 + byte1;
				}
			}
		}
		if(j == 151) {
			final int i2 = stream.getReversedOppositeUByte();
			final int l4 = anInt1268 + (i2 >> 4 & 7);
			final int k7 = anInt1269 + (i2 & 7);
			final int j10 = stream.getInt();
			final int k12 = stream.getReversedUByte();
			final int i15 = k12 >> 2;
			final int k16 = k12 & 3;
			final int l17 = anIntArray1177[i15];
			if(l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104) {
				method130(-1, j10, k16, l17, k7, i15, cameraPlane, l4, 0);
			}
			return;
		}
		if(j == 4) {
			final int j2 = stream.getUByte();
			int i5 = anInt1268 + (j2 >> 4 & 7);
			int l7 = anInt1269 + (j2 & 7);
			final int k10 = stream.getUShort();
			final int l12 = stream.getUByte();
			final int j15 = stream.getUShort();
			if(i5 >= 0 && l7 >= 0 && i5 < 104 && l7 < 104) {
				i5 = i5 * 128 + 64;
				l7 = l7 * 128 + 64;
				final StillGraphic class30_sub2_sub4_sub3 = new StillGraphic(cameraPlane, loopCycle, j15, k10, method42(cameraPlane, i5, l7) - l12, l7, i5);
				aClass19_1056.addLast(class30_sub2_sub4_sub3);
			}
			return;
		}
		if(j == 44) {
			final int k2 = stream.getLitEndUShortMinus128();
			final int j5 = stream.getUShort();
			final int i8 = stream.getUByte();
			final int l10 = anInt1268 + (i8 >> 4 & 7);
			final int i13 = anInt1269 + (i8 & 7);
			if(l10 >= 0 && i13 >= 0 && l10 < 104 && i13 < 104) {
				final ObjectStack class30_sub2_sub4_sub2_1 = new ObjectStack();
				class30_sub2_sub4_sub2_1.id = k2;
				class30_sub2_sub4_sub2_1.amount = j5;
				if(sceneItems[cameraPlane][l10][i13] == null) {
					sceneItems[cameraPlane][l10][i13] = new LinkedDeque();
				}
				sceneItems[cameraPlane][l10][i13].addLast(class30_sub2_sub4_sub2_1);
				spawnGroundItem(l10, i13);
			}
			return;
		}
		if(j == 101) {
			final int l2 = stream.getOppositeUByte();
			final int k5 = l2 >> 2;
			final int j8 = l2 & 3;
			final int i11 = anIntArray1177[k5];
			final int j13 = stream.getUByte();
			final int k15 = anInt1268 + (j13 >> 4 & 7);
			final int l16 = anInt1269 + (j13 & 7);
			if(k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104) {
				method130(-1, -1, j8, i11, l16, k5, cameraPlane, k15, 0);
			}
			return;
		}
		if(j == 117) {
			final int i3 = stream.getUByte();
			int l5 = anInt1268 + (i3 >> 4 & 7);
			int k8 = anInt1269 + (i3 & 7);
			int j11 = l5 + stream.getSByte();
			int k13 = k8 + stream.getSByte();
			final int l15 = stream.getSShort();
			final int i17 = stream.getUShort();
			final int i18 = stream.getUByte() * 4;
			final int l18 = stream.getUByte() * 4;
			final int k19 = stream.getUShort();
			final int j20 = stream.getUShort();
			final int i21 = stream.getUByte();
			final int j21 = stream.getUByte();
			if(l5 >= 0 && k8 >= 0 && l5 < 104 && k8 < 104 && j11 >= 0 && k13 >= 0 && j11 < 104 && k13 < 104 && i17 != 65535) {
				l5 = l5 * 128 + 64;
				k8 = k8 * 128 + 64;
				j11 = j11 * 128 + 64;
				k13 = k13 * 128 + 64;
				final Projectile class30_sub2_sub4_sub4 = new Projectile(i21, l18, k19 + loopCycle, j20 + loopCycle, j21, cameraPlane, method42(cameraPlane, l5, k8) - i18, k8, l5, l15, i17);
				class30_sub2_sub4_sub4.method455(k19 + loopCycle, k13, method42(cameraPlane, j11, k13) - l18, j11);
				aClass19_1013.addLast(class30_sub2_sub4_sub4);
			}
		}
	}

	private void updateNPCMovement(Buffer stream) {
		stream.beginBitAccess();
		final int index = stream.getBits(8);
		if(index < npcListSize) {
			for(int l = index; l < npcListSize; l++) {
				anIntArray840[anInt839++] = npcEntryList[l];
			}
		}
		if(index > npcListSize) {
			SignLink.reportError(localUsername + " Too many npcs - index: " + index + " - size: " + npcListSize);
			throw new RuntimeException("eek");
		}
		npcListSize = 0;
		for(int i1 = 0; i1 < index; i1++) {
			final int j1 = npcEntryList[i1];
			final NPC npc = npcList[j1];
			final int k1 = stream.getBits(1);
			if(k1 == 0) {
				npcEntryList[npcListSize++] = j1;
				npc.anInt1537 = loopCycle;
			} else {
				final int l1 = stream.getBits(2);
				if(l1 == 0) {
					npcEntryList[npcListSize++] = j1;
					npc.anInt1537 = loopCycle;
					anIntArray894[anInt893++] = j1;
				} else if(l1 == 1) {
					npcEntryList[npcListSize++] = j1;
					npc.anInt1537 = loopCycle;
					final int i2 = stream.getBits(3);
					npc.moveInDir(false, i2);
					final int k2 = stream.getBits(1);
					if(k2 == 1) {
						anIntArray894[anInt893++] = j1;
					}
				} else if(l1 == 2) {
					npcEntryList[npcListSize++] = j1;
					npc.anInt1537 = loopCycle;
					final int j2 = stream.getBits(3);
					npc.moveInDir(true, j2);
					final int l2 = stream.getBits(3);
					npc.moveInDir(true, l2);
					final int i3 = stream.getBits(1);
					if(i3 == 1) {
						anIntArray894[anInt893++] = j1;
					}
				} else if(l1 == 3) {
					anIntArray840[anInt839++] = j1;
				}
			}
		}

	}

	private void method142(int i, int j, int k, int l, int i1, int j1, int k1) {
		if(i1 >= 1 && i >= 1 && i1 <= 102 && i <= 102) {
			if(Config.def.isLOW_MEM() && j != cameraPlane) {
				return;
			}
			long hash = 0;
			if(j1 == 0) {
				hash = scene.getWallHash(j, i1, i);
			}
			if(j1 == 1) {
				hash = scene.getWallDecorHash(j, i1, i);
			}
			if(j1 == 2) {
				hash = scene.getEntityUnitHash(j, i1, i);
			}
			if(j1 == 3) {
				hash = scene.getGroundDecorHash(j, i1, i);
			}
			if(hash != 0) {
				final int i3 = scene.getWallsForMinimap(j, i1, i, hash);
				final int j2 = (int) (hash >> 14 & 0xFFFFFF);
				final int k2 = i3 & 0x1f;
				final int l2 = i3 >> 6;
				if(j1 == 0) {
					scene.removeWall(i1, j, i);
					final LocationType class46 = LocationType.getPrecise(j2);
					if(class46.solid) {
						collisionMaps[j].method215(l2, k2, class46.walkable, i1, i);
					}
				}
				if(j1 == 1) {
					scene.removeWallDecor(j, i1, i);
				}
				if(j1 == 2) {
					scene.removeEntityUnits(j, i1, i);
					final LocationType class46_1 = LocationType.getPrecise(j2);
					if(i1 + class46_1.sizeX > 103 || i + class46_1.sizeX > 103 || i1 + class46_1.sizeY > 103 || i + class46_1.sizeY > 103) {
						return;
					}
					if(class46_1.solid) {
						collisionMaps[j].method216(l2, class46_1.sizeX, i1, i, class46_1.sizeY, class46_1.walkable);
					}
				}
				if(j1 == 3) {
					scene.removeGroundDecor(j, i1, i);
					final LocationType class46_2 = LocationType.getPrecise(j2);
					if(class46_2.solid && class46_2.hasActions) {
						collisionMaps[j].setUnblocked(i1, i);
					}
				}
			}
			if(k1 >= 0) {
				int j3 = j;
				if(j3 < 3 && (tiles[1][i1][i] & 2) == 2) {
					j3++;
				}
				MapDecoder.method188(scene, k, i, l, j3, collisionMaps[j], sceneGroundZ, i1, k1, j);
			}
		}
	}
	
	private MapDecoder decoder;

	public void loadRegion() {
		try {
			planeReq = -1;
			aClass19_1056.clear();
			aClass19_1013.clear();
			Texture.reset();
			clearMemory();
			scene.clear();
			for(int plane = 0; plane < 4; plane++) {
				collisionMaps[plane].clear();
			}
			for(int plane = 0; plane < 4; plane++) {
				for(int x = 0; x < 104; x++) {
					for(int y = 0; y < 104; y++) {
						tiles[plane][x][y] = 0;
					}
				}
			}
			decoder = new MapDecoder(tiles, sceneGroundZ);
			final int dataLength = terrainData.length;
			outBuffer.putOpcode(0);
			if(!loadGeneratedMap) {
				for(int i3 = 0; i3 < dataLength; i3++) {
					final int offsetX = ((mapCoordinates[i3] >> 8) << 6) - baseX;
					final int offsetY = ((mapCoordinates[i3] & 0xff) << 6) - baseY;
					final byte data[] = terrainData[i3];
					if(data != null) {
						decoder.method180(data, offsetY, offsetX, (regionX - 6) << 3, (regionY - 6) << 3, collisionMaps);
					}
				}
				for(int j4 = 0; j4 < dataLength; j4++) {
					final int offsetX = ((mapCoordinates[j4] >> 8) << 6) - baseX;
					final int offsetY = ((mapCoordinates[j4] & 0xff) << 6) - baseY;
					final byte data[] = terrainData[j4];
					if(data == null && regionY < 800) {
						decoder.method174(offsetY, 64, 64, offsetX);
					}
				}
				pkt238Count++;
				if(pkt238Count > 160) {
					pkt238Count = 0;
				}
				outBuffer.putOpcode(0);
				for(int i6 = 0; i6 < dataLength; i6++) {
					final byte data[] = objectData[i6];
					final boolean oldMap = onDemandRequester.mapOld(objectDataIds[i6]);
					if(data != null) {
						final int offsetX = ((mapCoordinates[i6] >> 8) << 6) - baseX;
						final int offsetY = ((mapCoordinates[i6] & 0xff) << 6) - baseY;
						decoder.method190(offsetX, collisionMaps, offsetY, scene, data, oldMap);
					}
				}
			}
			if(loadGeneratedMap) {
				for(int j3 = 0; j3 < 4; j3++) {
					for(int k4 = 0; k4 < 13; k4++) {
						for(int j6 = 0; j6 < 13; j6++) {
							final int l7 = constructRegionData[j3][k4][j6];
							if(l7 != -1) {
								final int i9 = l7 >> 24 & 3;
								final int l9 = l7 >> 1 & 3;
								final int j10 = l7 >> 14 & 0x3ff;
								final int l10 = l7 >> 3 & 0x7ff;
								final int j11 = (j10 >> 3 << 8) + (l10 >> 3);
								for(int l11 = 0; l11 < mapCoordinates.length; l11++) {
									if(mapCoordinates[l11] != j11 || terrainData[l11] == null) {
										continue;
									}
									decoder.method179(i9, l9, collisionMaps, k4 << 3, (j10 & 7) << 3, terrainData[l11], (l10 & 7) << 3, j3, j6 * 8);
									break;
								}

							}
						}
					}
				}
				for(int l4 = 0; l4 < 13; l4++) {
					for(int k6 = 0; k6 < 13; k6++) {
						final int i8 = constructRegionData[0][l4][k6];
						if(i8 == -1) {
							decoder.method174(k6 << 3, 8, 8, l4 << 3);
						}
					}
				}
				outBuffer.putOpcode(0);
				for(int l6 = 0; l6 < 4; l6++) {
					for(int j8 = 0; j8 < 13; j8++) {
						for(int j9 = 0; j9 < 13; j9++) {
							final int i10 = constructRegionData[l6][j8][j9];
							if(i10 != -1) {
								final int k10 = i10 >> 24 & 3;
								final int i11 = i10 >> 1 & 3;
								final int k11 = i10 >> 14 & 0x3ff;
								final int i12 = i10 >> 3 & 0x7ff;
								final int j12 = (k11 >> 3 << 8) + (i12 >> 3);
								for(int k12 = 0; k12 < mapCoordinates.length; k12++) {
									if(mapCoordinates[k12] != j12 || objectData[k12] == null) {
										continue;
									}
									final boolean oldMap = onDemandRequester.mapOld(objectDataIds[k12]);
									decoder.method183(collisionMaps, scene, k10, j8 << 3, (i12 & 7) << 3, l6, objectData[k12], (k11 & 7) * 8, i11, j9 * 8, oldMap);
									break;
								}
							}
						}
					}
				}
			}
			outBuffer.putOpcode(0);
			decoder.set(collisionMaps, scene);
			gameGraphics.setCanvas();
			outBuffer.putOpcode(0);
			int plane = MapDecoder.setPlane;
			if(plane > cameraPlane) {
				plane = cameraPlane;
			}
			if(plane < cameraPlane - 1) {
				plane = cameraPlane - 1;
			}
			if(Config.def.isLOW_MEM()) {
				scene.initPlane(MapDecoder.setPlane);
			} else {
				scene.initPlane(0);
			}
			for(int x = 0; x < 104; x++) {
				for(int y = 0; y < 104; y++) {
					spawnGroundItem(x, y);
				}
			}
			pkt150Count++;
			if(pkt150Count > 98) {
				pkt150Count = 0;
				outBuffer.putOpcode(150);
			}
			method63();
		} catch(final Exception exception) {
			exception.printStackTrace();
		}
		LocationType.modelCache.clear();
		LocationType.animatedModelCache.clear();
		System.gc();
		Texture.reset();
		onDemandRequester.method566();
		int k = (regionX - 6) / 8 - 1;
		int j1 = (regionX + 6) / 8 + 1;
		int i2 = (regionY - 6) / 8 - 1;
		int l2 = (regionY + 6) / 8 + 1;
		if(inTutorialIsland) {
			k = 49;
			j1 = 50;
			i2 = 49;
			l2 = 50;
		}
		for(int l3 = k; l3 <= j1; l3++) {
			for(int j5 = i2; j5 <= l2; j5++) {
				if(l3 == k || l3 == j1 || j5 == i2 || j5 == l2) {
					final int j7 = onDemandRequester.getMapId(0, j5, l3);
					if(j7 != -1) {
						onDemandRequester.passiveRequest(j7, 3);
					}
					final int k8 = onDemandRequester.getMapId(1, j5, l3);
					if(k8 != -1) {
						onDemandRequester.passiveRequest(k8, 3);
					}
				}
			}
		}
	}

	private void method45() {
		aBoolean1031 = true;
		for(int j = 0; j < 7; j++) {
			anIntArray1065[j] = -1;
			for(int k = 0; k < Identikit.length; k++) {
				if(Identikit.cache[k].widgetDisplayed || Identikit.cache[k].partId != j + (aBoolean1047 ? 0 : 7)) {
					continue;
				}
				anIntArray1065[j] = k;

				if(anIntArray1065[2] >= 443 && anIntArray1065[2] <= 456) {
					anIntArray1065[3] = -1;
				} else {
					if(anIntArray1065[3] == -1)
						anIntArray1065[3] = 26;
					if(j == 3)
						anIntArray1065[j] = k;

				}

				break;
			}
		}
	}

	private void addNewNPC(int i, Buffer buffer) {
		while(buffer.bit + 21 < i * 8) {
			final int k = buffer.getBits(14);
			if(k == 16383) {
				break;
			}
			if(npcList[k] == null) {
				npcList[k] = new NPC();
			}
			final NPC npc = npcList[k];
			npcEntryList[npcListSize++] = k;
			npc.anInt1537 = loopCycle;
			int l = buffer.getBits(5);
			if(l > 15) {
				l -= 32;
			}
			int i1 = buffer.getBits(5);
			if(i1 > 15) {
				i1 -= 32;
			}
			final int j1 = buffer.getBits(1);
			npc.type = NPCType.get(buffer.getBits(16));
			final int k1 = buffer.getBits(1);
			if(k1 == 1) {
				anIntArray894[anInt893++] = k;
			}
			npc.anInt1540 = npc.type.boundaryDimension;
			npc.anInt1504 = npc.type.degreesToTurn;
			npc.anInt1554 = npc.type.walkAnimationId;
			npc.anInt1555 = npc.type.turnAroundAnimationId;
			npc.anInt1556 = npc.type.turnRightAnimationId;
			npc.anInt1557 = npc.type.turnLeftAnimationId;
			npc.anInt1511 = npc.type.standAnimationId;
			npc.setPos(localPlayer.smallX[0] + i1, localPlayer.smallY[0] + l, j1 == 1);
		}
		buffer.endBitAccess();
	}

	private void method49(Buffer stream) {
		for(int j = 0; j < anInt893; j++) {
			final int k = anIntArray894[j];
			final Player player = playerList[k];
			int l = stream.getUByte();
			if((l & 0x40) != 0) {
				l += stream.getUByte() << 8;
			}
			method107(l, k, stream, player);
		}
	}

	public void method50(int y, int k, int x, int i1, int plane) {
		long hash = scene.getWallHash(plane, x, y);
		if(hash != 0) {
			final int l1 = scene.getWallsForMinimap(plane, x, y, hash);
			final int k2 = l1 >> 6 & 3;
			final int i3 = l1 & 0x1f;
			int k3 = k;
			if(hash > 0) {
				k3 = i1;
			}
			final int ai[] = minimapImage.imageRaster;
			final int k4 = 24624 + x * 4 + (103 - y) * 512 * 4;
			final int i5 = (int) (hash >> 14 & 0xFFFFFF);
			final LocationType class46_2 = LocationType.getPrecise(i5);
			if(class46_2.mapScene != -1) {
				final PaletteImage background_2 = mapScenes[class46_2.mapScene];
				if(background_2 != null) {
					final int i6 = (class46_2.sizeX * 4 - background_2.trueWidth) / 2;
					final int j6 = (class46_2.sizeY * 4 - background_2.trueHeight) / 2;
					background_2.drawImage(48 + x * 4 + i6, 48 + (104 - y - class46_2.sizeY) * 4 + j6);
				}
			} else {
				if(i3 == 0 || i3 == 2) {
					if(k2 == 0) {
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else if(k2 == 1) {
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else if(k2 == 2) {
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else if(k2 == 3) {
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
				}
				if(i3 == 3) {
					if(k2 == 0) {
						ai[k4] = k3;
					} else if(k2 == 1) {
						ai[k4 + 3] = k3;
					} else if(k2 == 2) {
						ai[k4 + 3 + 1536] = k3;
					} else if(k2 == 3) {
						ai[k4 + 1536] = k3;
					}
				}
				if(i3 == 2) {
					if(k2 == 3) {
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else if(k2 == 0) {
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else if(k2 == 1) {
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else if(k2 == 2) {
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
				}
			}
		}
		hash = scene.getEntityUnitHash(plane, x, y);
		if(hash != 0) {
			final int i2 = scene.getWallsForMinimap(plane, x, y, hash);
			final int l2 = i2 >> 6 & 3;
			final int j3 = i2 & 0x1f;
			final int l3 = (int) (hash >> 14 & 0xFFFFFF);
			final LocationType class46_1 = LocationType.getPrecise(l3);
			if(class46_1.mapScene != -1) {
				if(class46_1.mapScene == 18180)
					return;
				final PaletteImage background_1 = mapScenes[class46_1.mapScene];
				if(background_1 != null) {
					final int j5 = (class46_1.sizeX * 4 - background_1.trueWidth) / 2;
					final int k5 = (class46_1.sizeY * 4 - background_1.trueHeight) / 2;
					background_1.drawImage(48 + x * 4 + j5, 48 + (104 - y - class46_1.sizeY) * 4 + k5);
				}
			} else if(j3 == 9) {
				int l4 = 0xeeeeee;
				if(hash > 0) {
					l4 = 0xee0000;
				}
				final int ai1[] = minimapImage.imageRaster;
				final int l5 = 24624 + x * 4 + (103 - y) * 512 * 4;
				if(l2 == 0 || l2 == 2) {
					ai1[l5 + 1536] = l4;
					ai1[l5 + 1024 + 1] = l4;
					ai1[l5 + 512 + 2] = l4;
					ai1[l5 + 3] = l4;
				} else {
					ai1[l5] = l4;
					ai1[l5 + 512 + 1] = l4;
					ai1[l5 + 1024 + 2] = l4;
					ai1[l5 + 1536 + 3] = l4;
				}
			}
		}
		hash = scene.getGroundDecorHash(plane, x, y);
		if(hash != 0) {
			final int j2 = (int) (hash >> 14 & 0xFFFFFF);
			final LocationType class46 = LocationType.getPrecise(j2);
			if(class46.mapScene != -1) {
				final PaletteImage background = mapScenes[class46.mapScene];
				if(background != null) {
					final int i4 = (class46.sizeX * 4 - background.trueWidth) / 2;
					final int j4 = (class46.sizeY * 4 - background.trueHeight) / 2;
					background.drawImage(48 + x * 4 + i4, 48 + (104 - y - class46.sizeY) * 4 + j4);
				}
			}
		}
	}

	private int initialiseRegionLoading() {
		for(int i = 0; i < terrainData.length; i++) {
			if(terrainData[i] == null && terrainDataIds[i] != -1) {
				return -1;
			}
			if(objectData[i] == null && objectDataIds[i] != -1) {
				return -2;
			}
		}
		boolean regionsCached = true;
		for(int j = 0; j < terrainData.length; j++) {
			final byte data[] = objectData[j];
			if(data != null) {
				int blockX = (mapCoordinates[j] >> 8) * 64 - baseX;
				int blockY = (mapCoordinates[j] & 0xff) * 64 - baseY;
				final boolean oldMap = onDemandRequester.mapOld(objectDataIds[j]);
				if(loadGeneratedMap) {
					blockX = 10;
					blockY = 10;
				}
				regionsCached &= MapDecoder.regionCached(blockX, data, blockY, oldMap);
			}
		}
		if(!regionsCached) {
			return -3;
		}
		if(regionLoaded) {
			return -4;
		} else {
			loadingStage = 2;
			MapDecoder.plane = cameraPlane;
			loadRegion();
			outBuffer.putOpcode(121);
			return 0;
		}
	}

	private void method60(int i) {
		final Interface class9 = Interface.cache[i];
		for(final int element : class9.subId) {
			if(element == -1) {
				break;
			}
			final Interface class9_1 = Interface.cache[element];
			if(class9_1.type == 1) {
				method60(class9_1.id);
			}
			class9_1.modelAnimLength = 0;
			class9_1.modelAnimDelay = 0;
		}
	}

	private void method63() {
		WorldObjectSpawn class30_sub1 = (WorldObjectSpawn) aClass19_1179.getFirst();
		for(; class30_sub1 != null; class30_sub1 = (WorldObjectSpawn) aClass19_1179.getNext()) {
			if(class30_sub1.anInt1294 == -1) {
				class30_sub1.anInt1302 = 0;
				method89(class30_sub1);
			} else {
				class30_sub1.unlinkPrimary();
			}
		}

	}

	private boolean clickLoc(long hash, int y, int x) {
		final int i1 = (int) (hash >> 14 & 0xFFFFFF);
		final int j1 = scene.getWallsForMinimap(cameraPlane, x, y, hash);
		if(j1 == -1) {
			return false;
		}
		final int k1 = j1 & 0x1f;
		final int l1 = j1 >> 6 & 3;
		if(!panelHandler.action())
			return false;
		if(k1 == 10 || k1 == 11 || k1 == 22) {
			final LocationType class46 = LocationType.getPrecise(i1);
			int i2;
			int j2;
			if(l1 == 0 || l1 == 2) {
				i2 = class46.sizeX;
				j2 = class46.sizeY;
			} else {
				i2 = class46.sizeY;
				j2 = class46.sizeX;
			}
			int k2 = class46.face;
			if(l1 != 0) {
				k2 = (k2 << l1 & 0xf) + (k2 >> 4 - l1);
			}
			findWalkingPath(2, 0, j2, 0, localPlayer.smallY[0], i2, k2, y, localPlayer.smallX[0], false, x);
		} else {
			findWalkingPath(2, l1, 0, k1 + 1, localPlayer.smallY[0], 0, 0, y, localPlayer.smallX[0], false, x);
		}
		crossX = super.clickX;
		crossY = super.clickY;
		crossType = 2;
		crossIndex = 0;
		return true;
	}

	private void method86(Buffer stream) {
		for(int j = 0; j < anInt893; j++) {
			final int k = anIntArray894[j];
			final NPC entity = npcList[k];
			int l = stream.getUByte();
			if((l & 0x40) != 0) {
				l += stream.getUByte() << 8;
			}
			if((l & 0x400) != 0) {
				entity.anInt1543 = stream.getReversedUByte();
				entity.anInt1545 = stream.getReversedUByte();
				entity.anInt1544 = stream.getReversedUByte();
				entity.anInt1546 = stream.getReversedUByte();
				entity.anInt1547 = stream.getLitEndUShortMinus128() + loopCycle;
				entity.anInt1548 = stream.getUShortMinus128() + loopCycle;
				entity.anInt1549 = stream.getReversedUByte();
				entity.method446();
			}
			if((l & 0x100) != 0) {
				entity.spotAnim = stream.getUShort();
				final int k1 = stream.getInt();
				entity.spotAnimOffset = k1 >> 16;
				entity.anInt1523 = loopCycle + (k1 & 0xffff);
				entity.spotAnimFrame = 0;
				entity.spotAnimCycle = 0;
				if(entity.anInt1523 > loopCycle) {
					entity.spotAnimFrame = -1;
				}
				if(entity.spotAnim == 65535) {
					entity.spotAnim = -1;
				}
			}
			if((l & 8) != 0) {
				int i1 = stream.getLitEndUShort();
				if(i1 == 65535) {
					i1 = -1;
				}
				final int i2 = stream.getUByte();
				if(i1 == entity.anim && i1 != -1) {
					final int l2 = DeformSequence.cache[i1].replayMode;
					if(l2 == 1) {
						entity.animFrame = 0;
						entity.animCycle = 0;
						entity.animDelay = i2;
						entity.anInt1530 = 0;
					}
					if(l2 == 2) {
						entity.anInt1530 = 0;
					}
				} else if(i1 == -1 || entity.anim == -1 || DeformSequence.cache[i1].priority >= DeformSequence.cache[entity.anim].priority) {
					entity.anim = i1;
					entity.animFrame = 0;
					entity.animCycle = 0;
					entity.animDelay = i2;
					entity.anInt1530 = 0;
					entity.anInt1542 = entity.smallXYIndex;
				}
			}
			if((l & 4) != 0) {
				entity.chatSpoken = stream.getLine();
				entity.chatLoopCycle = 100;
			}
			if((l & 0x80) != 0) {
				entity.type = NPCType.get(stream.getLitEndUShortMinus128());
				entity.anInt1540 = entity.type.boundaryDimension;
				entity.anInt1504 = entity.type.degreesToTurn;
				entity.anInt1554 = entity.type.walkAnimationId;
				entity.anInt1555 = entity.type.turnAroundAnimationId;
				entity.anInt1556 = entity.type.turnRightAnimationId;
				entity.anInt1557 = entity.type.turnLeftAnimationId;
				entity.anInt1511 = entity.type.standAnimationId;
			}
			if((l & 0x10) != 0) {
				entity.interactingEntity = stream.getUShort();
				if(entity.interactingEntity == 65535) {
					entity.interactingEntity = -1;
				}
			}
			if((l & 1) != 0) {
				entity.anInt1538 = stream.getLitEndUShort();
				entity.anInt1539 = stream.getLitEndUShort();
			}
			if((l & 2) != 0) {
				int l1 = stream.getUShort();
				int k2 = stream.getUByte();
				int icon = stream.getUByte();
				entity.updateHitData(k2, l1, loopCycle, icon, 0);
				entity.loopCycleStatus = loopCycle + 300;
				entity.currentHealth = stream.getUShort();
				entity.maxHealth = 100;
				entity.special = stream.getUByte();
			}
			if((l & 0x20) != 0) {
				int j1 = stream.getUShort();
				int j2 = stream.getUByte();
				int icon = stream.getUByte();
				entity.updateHitData(j2, j1, loopCycle, icon, 0);
				entity.loopCycleStatus = loopCycle + 300;
				entity.currentHealth = stream.getUShort();
				entity.maxHealth = 100;
				entity.special = stream.getUByte();
			}
		}
	}

	private void method89(WorldObjectSpawn class30_sub1) {
		long hash = 0;
		int j = -1;
		int k = 0;
		int l = 0;
		if(class30_sub1.anInt1296 == 0) {
			hash = scene.getWallHash(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		}
		if(class30_sub1.anInt1296 == 1) {
			hash = scene.getWallDecorHash(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		}
		if(class30_sub1.anInt1296 == 2) {
			hash = scene.getEntityUnitHash(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		}
		if(class30_sub1.anInt1296 == 3) {
			hash = scene.getGroundDecorHash(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		}
		if(hash != 0) {
			final int i1 = scene.getWallsForMinimap(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298, hash);
			j = (int) (hash >> 14 & 0xFFFFFF);
			k = i1 & 0x1f;
			l = i1 >> 6;
		}
		class30_sub1.anInt1299 = j;
		class30_sub1.anInt1301 = k;
		class30_sub1.anInt1300 = l;
	}

	private void addPlayers(Buffer stream, int i) {
		while(stream.bit + 10 < i * 8) {
			final int j = stream.getBits(11);
			if(j == 2047) {
				break;
			}
			if(playerList[j] == null) {
				playerList[j] = new Player();
				if(playerBuffer[j] != null) {
					playerList[j].updatePlayer(playerBuffer[j]);
				}
			}
			playerEntryList[playerCount++] = j;
			final Player player = playerList[j];
			player.anInt1537 = loopCycle;
			final int k = stream.getBits(1);
			if(k == 1) {
				anIntArray894[anInt893++] = j;
			}
			final int l = stream.getBits(1);
			int i1 = stream.getBits(5);
			if(i1 > 15) {
				i1 -= 32;
			}
			int j1 = stream.getBits(5);
			if(j1 > 15) {
				j1 -= 32;
			}
			player.setPos(localPlayer.smallX[0] + j1, localPlayer.smallY[0] + i1, l == 1);
		}
		stream.endBitAccess();
	}

	private void method96(Mobile entity) {
		if(entity.x < 128 || entity.y < 128 || entity.x >= 13184 || entity.y >= 13184) {
			entity.anim = -1;
			entity.spotAnim = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
			entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
			entity.method446();
		}
		if(entity == localPlayer && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
			entity.anim = -1;
			entity.spotAnim = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
			entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
			entity.method446();
		}
		if(entity.anInt1547 > loopCycle) {
			method97(entity);
		} else if(entity.anInt1548 >= loopCycle) {
			method98(entity);
		} else {
			method99(entity);
		}
		method100(entity);
		method101(entity);
	}

	private void method97(Mobile entity) {
		final int i = entity.anInt1547 - loopCycle;
		final int j = entity.anInt1543 * 128 + entity.anInt1540 * 64;
		final int k = entity.anInt1545 * 128 + entity.anInt1540 * 64;
		entity.x += (j - entity.x) / i;
		entity.y += (k - entity.y) / i;
		entity.anInt1503 = 0;
		if(entity.anInt1549 == 0) {
			entity.turnDirection = 1024;
		}
		if(entity.anInt1549 == 1) {
			entity.turnDirection = 1536;
		}
		if(entity.anInt1549 == 2) {
			entity.turnDirection = 0;
		}
		if(entity.anInt1549 == 3) {
			entity.turnDirection = 512;
		}
	}

	private void method98(Mobile entity) {
		if(entity.anInt1548 == loopCycle || entity.anim == -1 || entity.animDelay != 0 || entity.animCycle + 1 > DeformSequence.cache[entity.anim].getFrame(entity.animFrame)) {
			final int i = entity.anInt1548 - entity.anInt1547;
			final int j = loopCycle - entity.anInt1547;
			final int k = entity.anInt1543 * 128 + entity.anInt1540 * 64;
			final int l = entity.anInt1545 * 128 + entity.anInt1540 * 64;
			final int i1 = entity.anInt1544 * 128 + entity.anInt1540 * 64;
			final int j1 = entity.anInt1546 * 128 + entity.anInt1540 * 64;
			entity.x = (k * (i - j) + i1 * j) / i;
			entity.y = (l * (i - j) + j1 * j) / i;
		}
		entity.anInt1503 = 0;
		if(entity.anInt1549 == 0) {
			entity.turnDirection = 1024;
		}
		if(entity.anInt1549 == 1) {
			entity.turnDirection = 1536;
		}
		if(entity.anInt1549 == 2) {
			entity.turnDirection = 0;
		}
		if(entity.anInt1549 == 3) {
			entity.turnDirection = 512;
		}
		entity.yaw = entity.turnDirection;
	}

	private void method99(Mobile entity) {
		entity.idleAnim = entity.anInt1511;
		if(entity.smallXYIndex == 0) {
			entity.anInt1503 = 0;
			return;
		}
		if(entity.anim != -1 && entity.animDelay == 0 && DeformSequence.cache.length > entity.anim) {
			final DeformSequence animation = DeformSequence.cache[entity.anim];
			if(entity.anInt1542 > 0 && animation.precedenceAnimating == 0) {
				entity.anInt1503++;
				return;
			}
			if(entity.anInt1542 <= 0 && animation.precedenceWalking == 0) {
				entity.anInt1503++;
				return;
			}
		}
		final int i = entity.x;
		final int j = entity.y;
		final int k = entity.smallX[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
		final int l = entity.smallY[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
		if(k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
			entity.x = k;
			entity.y = l;
			return;
		}
		if(i < k) {
			if(j < l) {
				entity.turnDirection = 1280;
			} else if(j > l) {
				entity.turnDirection = 1792;
			} else {
				entity.turnDirection = 1536;
			}
		} else if(i > k) {
			if(j < l) {
				entity.turnDirection = 768;
			} else if(j > l) {
				entity.turnDirection = 256;
			} else {
				entity.turnDirection = 512;
			}
		} else if(j < l) {
			entity.turnDirection = 1024;
		} else {
			entity.turnDirection = 0;
		}
		int i1 = entity.turnDirection - entity.yaw & 0x7ff;
		if(i1 > 1024) {
			i1 -= 2048;
		}
		int j1 = entity.anInt1555;
		if(i1 >= -256 && i1 <= 256) {
			j1 = entity.anInt1554;
		} else if(i1 >= 256 && i1 < 768) {
			j1 = entity.anInt1557;
		} else if(i1 >= -768 && i1 <= -256) {
			j1 = entity.anInt1556;
		}
		if(j1 == -1) {
			j1 = entity.anInt1554;
		}
		entity.idleAnim = j1;
		int k1 = 4;
		if(entity.yaw != entity.turnDirection && entity.interactingEntity == -1 && entity.anInt1504 != 0) {
			k1 = 2;
		}
		if(entity.smallXYIndex > 2) {
			k1 = 6;
		}
		if(entity.smallXYIndex > 3) {
			k1 = 8;
		}
		if(entity.anInt1503 > 0 && entity.smallXYIndex > 1) {
			k1 = 8;
			entity.anInt1503--;
		}
		if(entity.aBooleanArray1553[entity.smallXYIndex - 1]) {
			k1 <<= 1;
		}
		if(k1 >= 8 && entity.idleAnim == entity.anInt1554 && entity.anInt1505 != -1) {
			entity.idleAnim = entity.anInt1505;
		}
		if(i < k) {
			entity.x += k1;
			if(entity.x > k) {
				entity.x = k;
			}
		} else if(i > k) {
			entity.x -= k1;
			if(entity.x < k) {
				entity.x = k;
			}
		}
		if(j < l) {
			entity.y += k1;
			if(entity.y > l) {
				entity.y = l;
			}
		} else if(j > l) {
			entity.y -= k1;
			if(entity.y < l) {
				entity.y = l;
			}
		}
		if(entity.x == k && entity.y == l) {
			entity.smallXYIndex--;
			if(entity.anInt1542 > 0) {
				entity.anInt1542--;
			}
		}
	}

	void mouseWheelDragged(int i, int j) {
		if(!mouseWheelDown)
			return;
		this.anInt1186 += i * 3;
		this.anInt1187 += (j << 1);
	}

	@Override
	public void setTab(int id) {
		boolean more = uiRenderer.getId() == 562;
		if(more && newerTabInterfaces[id] == -1)
			return;
		if(!more && olderTabInterfaces[id] == -1)
			return;
		super.invTab = id;
	}

	public void repackCacheIndex(int cacheIndex) {
		if(!(new File(SignLink.getCacheDir() + "index" + cacheIndex).exists()))
			return;
		System.out.println("Started repacking index " + cacheIndex + ".");
		int indexLength = new File(indexLocation(cacheIndex, -1)).listFiles().length;
		File[] file = new File(indexLocation(cacheIndex, -1)).listFiles();
		try {
			for(int index = 0; index < indexLength; index++) {
				if(file[index].getName().contains("Store"))
					continue;
				int fileIndex = Integer.parseInt(getFileNameWithoutExtension(file[index].toString()));
				byte[] data = fileToByteArray(cacheIndex, fileIndex);
				if(data != null && data.length > 0) {
					cacheIdx[cacheIndex].writeFile(data.length, data, fileIndex);
					System.out.println("Repacked " + fileIndex + ".");
				} else {
					System.out.println("Unable to locate index " + fileIndex + ".");
				}
			}
		} catch(Exception e) {
			System.out.println("Error packing cache index " + cacheIndex + ".");
		}
	}

	public String indexLocation(int cacheIndex, int index) {
		return SignLink.getCacheDir() + "index" + cacheIndex + "/" + (index != -1 ? index + ".gz" : "");
	}

	public byte[] fileToByteArray(int cacheIndex, int index) {
		try {
			if(indexLocation(cacheIndex, index).length() <= 0 || indexLocation(cacheIndex, index) == null) {
				return null;
			}
			File file = new File(indexLocation(cacheIndex, index));
			byte[] fileData = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(fileData);
			fis.close();
			return fileData;
		} catch(Exception e) {
			return null;
		}
	}

	public static String getFileNameWithoutExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if(0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}
	
	public void tabToReplyPm() {
		String name = null;
		for(int j = 0; j < 100; j++) {
			if(chatMessage[j] != null) {
				int chatType = this.chatType[j];
				if(chatType == 2 || chatType == 7) {
					name = chatAuthor[j];
					break;
				}
			}
		}
		if(name == null)
			pushMessage("You have not recieved any messages.", 0, "");
		try {
			if(name != null) {
				long namel = StringUtils.encryptName(name.trim());
				int node = -1;
				for(int count = 0; count < friendsCount; count++) {
					if(friendsListAsLongs[count] != namel)
						continue;
					node = count;
					break;
				}
				if(node != -1 && friendsNodeIDs[node] > 0) {
					inputDialogState = 0;
					messagePromptRaised = true;
					promptInput = "";
					friendsListAction = 3;
					aLong953 = friendsListAsLongs[node];
					promptInputTitle = "Enter message to send to " + friendsList[node];
				} else {
					pushMessage("That player is currently offline.", 0, "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CRC32 getCrc() {
		return crc;
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for(int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if(lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	static {
		EXP_FOR_LEVEL = new int[99];
		int points = 0;
		for(int level = 0; level < 99; level++) {
			int l = level + 1;
			int i1 = (int) (l + 300D * Math.pow(2D, l / 7D));
			points += i1;
			EXP_FOR_LEVEL[level] = points / 4;
		}
		BIT_MASK = new int[32];
		points = 2;
		for(int k = 0; k < 32; k++) {
			BIT_MASK[k] = points - 1;
			points += points;
		}
	}
}