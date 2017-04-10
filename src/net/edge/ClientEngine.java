package net.edge;

import net.edge.activity.ui.UIRenderer;
import net.edge.sign.SignLink;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ClientEngine extends Applet implements Runnable, MouseListener, MouseMotionListener, KeyListener, FocusListener, WindowListener, MouseWheelListener {

	/**
	 * Declared variables.
	 */
	public int invTab;
	private static final long serialVersionUID = -6866891087399185924L;
	private final long[] optims;
	private final int[] keyQueue = new int[128];
	private int state;
	private int delayTime;
	private int tempClickButton;
	private int tempClickX;
	private int tempClickY;
	private int keyOutputPos;
	private int keyInputPos;
	public final int[] keyPressed = new int[128];
	public int minDelay;
	public int fps;
	public int windowWidth;
	public int windowHeight;
	public int idleTime;
	public int mouseWheelAmt;
	public int mouseWheelX;
	public int mouseWheelY;
	public int mouseDragButton;
	public int mouseX;
	public int mouseY;
	public int clickButton;
	public int clickX;
	public int clickY;
	public Graphics graphics;
	public ClientFrame frame;
	public boolean awtFocus;
	private long tempClickTime;
	public long clickTime;
	public UIRenderer uiRenderer;
	private int windowOriginX = 0;
	private int windowOriginY = 0;
	boolean mouseWheelDown;

	/**
	 * ClientShell instance.
	 */
	ClientEngine() {
		invTab = 3;
		delayTime = 20;
		minDelay = 1;
		optims = new long[10];
		awtFocus = true;
	}

	/**
	 * Starts the applet.
	 */
	final void startApplet() {
		this.windowWidth = getWidth();
		this.windowHeight = getHeight();
		graphics = getComponent().getGraphics();
		graphics.drawRect(10, 10, 100, 100);
		startThread(this, 1);
	}

	/**
	 * Starts the application in a resizable state.
	 */
	final void startApplication() {
		this.windowWidth = 765;
		this.windowHeight = 503;
		frame = new ClientFrame(this, this.windowWidth, this.windowHeight, false, false);
		graphics = getComponent().getGraphics();
		startThread(this, 1);
	}

	/**
	 * Rebuils the applet/application. You can chose many varieties of options
	 * of how you want the new frame to pop-up. Here all are those parameters:
	 * @param width       - Sets the frame's width.
	 * @param height      - Sets the frame's height.
	 * @param undecorated - Removes the client's desktop environment window if
	 *                    enabled.
	 * @param resizable   - Sets the window resizable.
	 * @param full        - Sets the window to fullscreen.
	 */
	void rebuildFrame(int width, int height, boolean undecorated, boolean resizable, boolean full) {
		this.windowWidth = width;
		this.windowHeight = height;
		if(frame != null) {
			frame.dispose();
		}
		frame = new ClientFrame(this, width, height, undecorated, resizable);
		frame.addWindowListener(this);
		graphics = frame.getGraphics();
		getComponent().addMouseListener(this);
		getComponent().addMouseMotionListener(this);
		getComponent().addKeyListener(this);
		getComponent().addFocusListener(this);
		getComponent().addMouseWheelListener(this);
	}

	/**
	 * Sets the origin of the window.
	 * Used to center the game view etc. Default origin is left top corner.
	 */
	void setWindowOrigin(int x, int y) {
		graphics.translate(-windowOriginX, -windowOriginY);
		windowOriginX = x;
		windowOriginY = y;
		graphics.translate(windowOriginX, windowOriginY);
	}

	/**
	 * Gets the game component where the graphics are painted.
	 */
	Component getComponent() {
		if(frame != null) {
			return frame;
		} else {
			return this;
		}
	}

	/**
	 * Initializing.
	 */
	void initialize() {
	}

	/**
	 * Process drawing.
	 */
	void update() {
	}

	/**
	 * Process game loop.
	 */
	void process() {
	}

	/**
	 * Clean-up before quit the client.
	 */
	void reset() {
	}

	/**
	 * Close process.
	 */
	private void exit() {
		state = -2;
		if(frame != null) {
			frame.dispose();
		}
		reset();
		if(frame != null) {
			try {
				Thread.sleep(1000L);
			} catch(final Exception _ex) {
			}
			try {
				System.exit(0);
			} catch(final Throwable _ex) {
			}
		}
	}

	public final int getKey() {
		int key = -1;
		if(keyInputPos != keyOutputPos) {
			key = keyQueue[keyOutputPos];
			keyOutputPos = keyOutputPos + 1 & 0x7f;
		}
		return key;
	}
	
	@Override
	public final void paint(Graphics g) {
		if(graphics == null) {
			graphics = g;
		}
	}
	
	public final void setFrameRate(int frameRate) {
		delayTime = 1000 / frameRate;
	}

	public void startThread(Runnable runnable, int priority) {
		final Thread thread = new Thread(runnable);
		thread.start();
		thread.setPriority(priority);
	}
	
	@Override
	public final void update(Graphics g) {
		if(graphics == null) {
			graphics = g;
		}
	}

	@Override
	public final void start() {
		if(state >= 0) {
			state = 0;
		}
	}

	@Override
	public void run() {
		final Component component = getComponent();
		component.addMouseListener(this);
		component.addMouseMotionListener(this);
		component.addKeyListener(this);
		component.addFocusListener(this);
		component.addMouseWheelListener(this);
		if(frame != null) {
			frame.addWindowListener(this);
		}
		int opos = 0;
		int count = 0;
		for(int optim = 0; optim < 10; optim++) {
			optims[optim] = System.currentTimeMillis();
		}
		initialize();
		while(state >= 0) {
			if(state > 0) {
				state--;
				if(state == 0) {
					exit();
					return;
				}
			}
			int ratio = 300;
			int del = 1;
			final long l1 = System.currentTimeMillis();
			if(l1 > optims[opos]) {
				ratio = (int) (2560 * delayTime / (l1 - optims[opos]));
			}
			if(ratio < 25) {
				ratio = 25;
			}
			if(ratio > 256) {
				ratio = 256;
				del = (int) (delayTime - (l1 - optims[opos]) / 10L);
			}
			if(del > delayTime) {
				del = delayTime;
			}
			optims[opos] = l1;
			opos = (opos + 1) % 10;
			if(del > 1) {
				for(int k2 = 0; k2 < 10; k2++) {
					if(optims[k2] != 0L) {
						optims[k2] += del;
					}
				}
			}
			if(del < minDelay) {
				del = minDelay;
			}
			try {
				Thread.sleep(del);
			} catch(final InterruptedException _ex) {
			}
			update();
			for(; count < 256; count += ratio) {
				clickButton = tempClickButton;
				clickX = tempClickX;
				clickY = tempClickY;
				clickTime = tempClickTime;
				tempClickButton = 0;
				process();
				mouseWheelAmt = 0;
				keyOutputPos = keyInputPos;
			}
			count &= 0xff;
			if(delayTime > 0) {
				fps = 1000 * ratio / (delayTime * 256);
			}
		}
		if(state == -1) {
			exit();
		}
	}

	@Override
	public final void stop() {
		if(state >= 0) {
			state = 4000 / delayTime;
		}
	}

	@Override
	public final void destroy() {
		state = -1;
		try {
			Thread.sleep(1000L);
		} catch(final Exception _ex) {
			//empty
		}
		if(state == -1) {
			exit();
		}
	}

	@Override
	public final void focusGained(FocusEvent focusevent) {
		awtFocus = true;
	}

	@Override
	public final void focusLost(FocusEvent focusevent) {
		awtFocus = false;
		for(int i = 0; i < 128; i++) {
			keyPressed[i] = 0;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		mouseWheelAmt += event.getWheelRotation();
		int x = event.getX();
		int y = event.getY();
		if(frame != null) {
			x -= frame.insets.left;
			y -= frame.insets.top;
		}
		x -= windowOriginX;
		y -= windowOriginY;
		mouseWheelX = x;
		mouseWheelY = y;
	}

	public void setTab(int id) {

	}

	@Override
	public final void keyPressed(KeyEvent keyevent) {
		idleTime = 0;
		final int keyCode = keyevent.getKeyCode();
		int keyCharacter = keyevent.getKeyChar();
		if(keyCode == KeyEvent.VK_F12) {
			takeScreenshot();
		} else if(keyCode == KeyEvent.VK_ESCAPE) {
			setTab(3);
		} else if(keyCode == KeyEvent.VK_F1) {
			setTab(3);
		} else if(keyCode == KeyEvent.VK_F2) {
			setTab(4);
		} else if(keyCode == KeyEvent.VK_F3) {
			setTab(5);
		} else if(keyCode == KeyEvent.VK_F4) {
			setTab(6);
		} else if(keyCode == KeyEvent.VK_F5) {
			setTab(0);
		}
		if(keyCharacter < 30) {
			keyCharacter = 0;
		}
		if(keyCode == 37) {
			keyCharacter = 1;
		}
		if(keyCode == 39) {
			keyCharacter = 2;
		}
		if(keyCode == 38) {
			keyCharacter = 3;
		}
		if(keyCode == 40) {
			keyCharacter = 4;
		}
		if(keyCode == 17) {
			keyCharacter = 5;
		}
		if(keyCode == 33) {
			keyCharacter = 6;
		}
		if(keyCode == 34) {
			keyCharacter = 7;
		}
		if(keyCode == 8) {
			keyCharacter = 8;
		}
		if(keyCode == 127) {
			keyCharacter = 8;
		}
		if(keyCode == 9) {
			keyCharacter = 9;
		}
		if(keyCode == 10) {
			keyCharacter = 10;
		}
		if(keyCode >= 112 && keyCode <= 123) {
			keyCharacter = 1008 + keyCode - 112;
		}
		if(keyCode == 36) {
			keyCharacter = 1000;
		}
		if(keyCode == 35) {
			keyCharacter = 1001;
		}
		if(keyCharacter > 0 && keyCharacter < 128) {
			keyPressed[keyCharacter] = 1;
		}
		if(keyCharacter > 4 && keyCharacter != 6 && keyCharacter != 7) {
			keyQueue[keyInputPos] = keyCharacter;
			keyInputPos = keyInputPos + 1 & 0x7f;
		}
	}

	@Override
	public final void keyReleased(KeyEvent keyevent) {
		idleTime = 0;
		final int keyCode = keyevent.getKeyCode();
		char keyCharacter = keyevent.getKeyChar();
		if(keyCharacter < 30) {
			keyCharacter = 0;
		}
		if(keyCode == 37) {
			keyCharacter = 1;
		}
		if(keyCode == 39) {
			keyCharacter = 2;
		}
		if(keyCode == 38) {
			keyCharacter = 3;
		}
		if(keyCode == 40) {
			keyCharacter = 4;
		}
		if(keyCode == 17) {
			keyCharacter = 5;
		}
		if(keyCode == 33) {
			keyCharacter = 6;
		}
		if(keyCode == 34) {
			keyCharacter = 7;
		}
		if(keyCode == 8) {
			keyCharacter = '\b';
		}
		if(keyCode == 127) {
			keyCharacter = '\b';
		}
		if(keyCode == 9) {
			keyCharacter = '\t';
		}
		if(keyCode == 10) {
			keyCharacter = '\n';
		}
		if(keyCharacter > 0 && keyCharacter < '\200') {
			keyPressed[keyCharacter] = 0;
		}
	}

	@Override
	public final void mouseDragged(MouseEvent mouseevent) {
		int x = mouseevent.getX();
		int y = mouseevent.getY();
		if(frame != null) {
			x -= frame.insets.left;
			y -= frame.insets.top;
		}
		x -= windowOriginX;
		y -= windowOriginY;
		if(mouseWheelDown) {
			y = mouseWheelX - mouseevent.getX();
			int k = mouseWheelY - mouseevent.getY();
			mouseWheelDragged(y, -k);
			mouseWheelX = mouseevent.getX();
			mouseWheelY = mouseevent.getY();
			return;
		}
		idleTime = 0;
		mouseX = x;
		mouseY = y;
	}

	void mouseWheelDragged(int param1, int param2) {

	}

	@Override
	public final void keyTyped(KeyEvent keyevent) {
	}

	@Override
	public final void mouseClicked(MouseEvent mouseevent) {
	}

	@Override
	public final void mouseEntered(MouseEvent mouseevent) {
	}

	@Override
	public final void mouseExited(MouseEvent mouseevent) {
		idleTime = 0;
		mouseX = -1;
		mouseY = -1;
	}

	@Override
	public final void mouseMoved(MouseEvent mouseevent) {
		int x = mouseevent.getX();
		int y = mouseevent.getY();
		if(frame != null) {
			x -= frame.insets.left;
			y -= frame.insets.top;
		}
		x -= windowOriginX;
		y -= windowOriginY;
		idleTime = 0;
		mouseX = x;
		mouseY = y;
	}
	
	@Override
	public final void mousePressed(MouseEvent mouseevent) {
		int x = mouseevent.getX();
		int y = mouseevent.getY();
		if(frame != null) {
			x -= frame.insets.left;
			y -= frame.insets.top;
		}
		x -= windowOriginX;
		y -= windowOriginY;
		idleTime = 0;
		tempClickX = x;
		tempClickY = y;
		int type = mouseevent.getButton();
		tempClickTime = System.currentTimeMillis();
		if(type == 2) {
			mouseWheelDown = true;
			mouseWheelX = x;
			mouseWheelY = y;
			return;
		}
		if(mouseevent.isMetaDown()) {
			tempClickButton = 2;
			mouseDragButton = 2;
		} else {
			tempClickButton = 1;
			mouseDragButton = 1;
		}
	}

	@Override
	public final void mouseReleased(MouseEvent mouseevent) {
		idleTime = 0;
		mouseDragButton = 0;
		mouseWheelDown = false;
	}

	@Override
	public final void windowActivated(WindowEvent windowevent) {
	}

	@Override
	public final void windowClosed(WindowEvent windowevent) {
	}

	@Override
	public final void windowClosing(WindowEvent windowevent) {
        String closeOptions[] = {"Yes", "No"};
        int userPrompt = JOptionPane.showOptionDialog(windowevent.getComponent(), "Are you sure you want to exit?", "Edgeville",
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, closeOptions , closeOptions[1]);
        if(userPrompt == JOptionPane.YES_OPTION) {
            destroy();
        }
	}

	@Override
	public final void windowDeactivated(WindowEvent windowevent) {
	}

	@Override
	public final void windowDeiconified(WindowEvent windowevent) {
	}

	@Override
	public final void windowIconified(WindowEvent windowevent) {
	}

	@Override
	public final void windowOpened(WindowEvent windowevent) {
	}

	public static int random(int number) {
		return (int) (Math.random() * (double) (number + 1));
	}

	private void takeScreenshot() {
		try {
			Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
			Point point = window.getLocationOnScreen();
			int x = (int) point.getX();
			int y = (int) point.getY();
			int w = window.getWidth();
			int h = window.getHeight();
			Robot robot = new Robot(window.getGraphicsConfiguration().getDevice());
			Rectangle captureSize = new Rectangle(x, y, w, h);
			java.awt.image.BufferedImage bufferedimage = robot.createScreenCapture(captureSize);
			int picNumber = random(100);
			String fileExtension = "shot";
			File file = new File((new StringBuilder()).append(SignLink.getCacheDir() + "Screenshots/" + fileExtension + " ").append(picNumber).append(".png").toString());
			ImageIO.write(bufferedimage, "png", file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*public void handleInterfaceScrolling(MouseWheelEvent event) throws NullPointerException {
	    int positionX = 0;
        int positionY = 0;
        int offsetX = 0;
        int offsetY = 0;
        int childID = 0;
        if (Main.INSTANCE.openInterfaceID != -1) {
            Widget rsi = Widget.cache[Main.INSTANCE.openInterfaceID];
            offsetX = uiRenderer.isFixed() ? 4 : uiRenderer.getOnScreenWidgetOffsets().x;
            offsetY = uiRenderer.isFixed() ? 4 : uiRenderer.getOnScreenWidgetOffsets().y;
            for (int index = 0; index < rsi.children.length; index++) {
                if (Widget.cache[rsi.children[index]].scrollMax > 0) {
                    childID = index;
                    positionX = rsi.childX[index];
                    positionY = rsi.childY[index];
                    windowWidth = Widget.cache[rsi.children[index]].width;
                    windowHeight = Widget.cache[rsi.children[index]].height;
                    break;
                }
            }
            if (mouseWheelX > offsetX + positionX && mouseWheelY > offsetY + positionY &&
            		mouseWheelX < offsetX + positionX + windowWidth &&
            		mouseY < offsetY + positionY + windowHeight) {
                Widget.cache[rsi.children[childID]].scrollPosition += mouseWheelAmt;
            }
        }
    }*/
}