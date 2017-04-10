package net.edge;

import javax.imageio.ImageIO;

import java.awt.*;
import java.io.IOException;

public final class ClientFrame extends Frame {

	private static final long serialVersionUID = -7570170911972501444L;
	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Dimension screenSize = toolkit.getScreenSize();
	public int screenWidth = (int) screenSize.getWidth();
	public int screenHeight = (int) screenSize.getHeight();
	protected final Insets insets;
	private final ClientEngine clientStub;

	/**
	 * Creates a new client frame.
	 * @param gameStub        The game stub.
	 * @param gameFrameWidth  The game width.
	 * @param gameFrameHeight The game height.
	 * @param undecorative    The decorative flag.
	 * @param resizable       The resizable flag.
	 */
	public ClientFrame(ClientEngine gameStub, int gameFrameWidth, int gameFrameHeight, boolean undecorative, boolean resizable) {
		this.clientStub = gameStub; // Initialize the stub.
		setTitle("Edgeville - Official Client | " + (undecorative ? "Fullscreen" : resizable ? "Resizable" : "Fixed")); // Sets the frame's title.
		setResizable(resizable); // Sets the frame resizable or fixed.
		setUndecorated(undecorative); // Sets the frame undecorative or windowed.
		try {
			Image icon = ImageIO.read(getClass().getResource("icon.png"));
			setIconImage(icon);
			Cursor cursor = toolkit.createCustomCursor(ImageIO.read(getClass().getResource("cursor.png")), new Point(0, 0), "cursor");
			setCursor(cursor);
		} catch(IOException e) {
		}
		
		setVisible(true); // Sets the frame to a visible state.
		insets = getInsets(); // Initialize the insets.
		setSize(gameFrameWidth + insets.left + insets.right, gameFrameHeight + insets.top + insets.bottom); // Sets the size.

		//The following 2 lines do not work with linux

		//setLocation((screenWidth - gameFrameWidth) / 2, (screenHeight - gameFrameHeight) / 2); // Sets the location to middle of the screen.
		//setLocationRelativeTo(null); //Sets the location of the window relative to the specified component.

		setBackground(Color.BLACK); // Sets the background color.
		requestFocus(); // Requests the user's focus.
		toFront(); // Displays frame to front.
	}

	/**
	 * Gets the game height.
	 * @return The game height.
	 */
	public int getContentHeight() {
		return getHeight() - (insets.top + insets.bottom);
	}

	/**
	 * Gets the frame size.
	 * @return The frame size.
	 */
	public Dimension getContentSize() {
		return new Dimension(this.getSize().width - (insets.left + insets.right), this.getSize().height - (insets.top + insets.bottom));
	}

	/**
	 * Gets the frame width.
	 * @return The frame width.
	 */
	public int getContentWidth() {
		return getWidth() - (insets.left + insets.right);
	}

	/**
	 * Gets the graphics.
	 * @return graphics
	 */
	@Override
	public Graphics getGraphics() {
		final Graphics graphics = super.getGraphics();
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.translate(insets != null ? insets.left : 0, insets != null ? insets.top : 0);
		return graphics;
	}

	/**
	 * Paint the graphics that was gathered.
	 * @param graphics
	 */
	@Override
	public void paint(Graphics graphics) {
		clientStub.paint(graphics);
	}

	/**
	 * Update the graphics that was painted.
	 * @param graphics
	 */
	@Override
	public void update(Graphics graphics) {
		clientStub.update(graphics);
	}
}
