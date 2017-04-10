package net.edge;

import net.edge.sign.SignLink;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class Updater implements Runnable {

	private BufferedImage image;
	private Graphics2D graphics;
	private int width;
	private int height;
	private int angle;
	private int percent = 1;
	private int cache = -1;
	Client client;
	
	public Updater(Client client) {
		this.client = client;
	}
	
	public void draw(String... progress) {
		if(image == null || width != client.windowWidth || height != client.windowHeight) {
			width = client.windowWidth;
			height = client.windowHeight;
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			graphics = image.createGraphics();
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0, 0, width, height);
		} else {
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0, 0, width, height);
		}

		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Georgia", Font.PLAIN, progress.length > 1 ? 18 : 14));
		int y = 24;
		for(String t : progress) {
			graphics.drawString(t, 10, y);
			y += 35;
		}
		if(progress.length == 1) {
			graphics.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			
			graphics.setColor(Color.WHITE);
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.drawArc((width >> 1) - 50, (height >> 1) - 50, 100, 100, 360 - angle, 225);
			angle = (angle + 1) % 360;
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			graphics.setFont(new Font("Georgia", Font.PLAIN, 21));
			graphics.drawString(percent + "%", (width >> 1) - (graphics.getFontMetrics().stringWidth(percent + "%") >> 1), (height >> 1) + 7);
		}

		client.graphics.drawImage(image, 0, 0, null);
	}
	
	@Override
	public void run() {
		SignLink.getCacheDir();
		if(Client.firstRun) {
			draw("Note: The game downloads data as you play it.", "So please allow some time to the game to gather fresh data.", "", "Game will launch in 10 seconds...");
			try {
				Thread.sleep(10000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		int version = getVersion();
		if(version != -1 && Constants.BUILD != version) {
			OutputStream dest = null;
			URLConnection download;
			InputStream readFileToDownload = null;
			try {
				File jar = new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
				dest = new BufferedOutputStream(new FileOutputStream(jar));
				download = new URL("http://edgeville.net/game/game.jar").openConnection();
				readFileToDownload = download.getInputStream();
				byte[] data = new byte[1024];
				int numRead;
				long numWritten = 0;
				int length = download.getContentLength();
				while((numRead = readFileToDownload.read(data)) != -1) {
					dest.write(data, 0, numRead);
					numWritten += numRead;
					percent = (int) (((double) numWritten / (double) length) * 100D);
					draw("Updating the client...");
				}
			} catch(Exception exception) {
				exception.printStackTrace();
			} finally {
				try {
					if(readFileToDownload != null)
						readFileToDownload.close();
					if(dest != null)
						dest.close();
					Thread.sleep(1000L);
					File jar;
					try {
						jar = new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
						startApplication(jar);
					} catch(URISyntaxException e) {
						e.printStackTrace();
					}
				} catch(IOException | InterruptedException ioe) {

				}
			}
		}
	}

	public void startApplication(File file) {
		try {
			Runtime.getRuntime().exec("java -jar " + file.getAbsolutePath() + "");
			Thread.sleep(1000L);
			System.exit(0);
		} catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int getVersion() {
		URL url;
		try {
			url = new URL("http://edgeville.net/game/version.txt");
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			int version = Integer.parseInt(br.readLine());
			br.close();
			return version;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

}