package img;

import net.arrav.Client;
import net.arrav.cache.unit.ImageCache;
import net.arrav.net.SignLink;
import net.arrav.util.FileToolkit;
import net.arrav.util.io.Buffer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

public class ImagePacker {

	public static void main(String[] args) {
		try {
			pack(null, false);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Converts images to raw dat images to be encoded into the cache later on.
	 * @throws IOException
	 */
	public static void pack(Client client, boolean cache) throws IOException {

		//Images that needs alpha channel removal.
		File[] update = new File(SignLink.getCacheDir() + "/images/alpha_remove/").listFiles();

		//Removing alpha channel for certain images.
		assert update != null;
		for(File file : update) {
			if(file.getName().contains("Store"))//Ignoring mac store file.
				continue;
			if(!file.getName().toLowerCase().contains(".png")) {
				System.out.println("Found something else than a png image.");
				continue;
			}
			//The image.
			BufferedImage bufferedImage = ImageIO.read(file);


			//Copy of the image.
			BufferedImage copy = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

			//New image with white background.
			Graphics2D g2d = copy.createGraphics();
			g2d.setColor(new Color(255, 0, 255)); // Or what ever fill color you want...
			g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
			g2d.drawImage(bufferedImage, 0, 0, null);
			g2d.dispose();

			//Setting any white bg transparent to pink
			Color color = new Color(255, 255, 255);
			Color ncolor = new Color(255, 0, 255);
			for(int x = 0; x < copy.getWidth(); x++) {
				for(int y = 0; y < copy.getHeight(); y++) {
					int rgb = copy.getRGB(x, y);
					boolean toUpdate = (rgb == (color.getRGB()));
					if(toUpdate) {
						copy.setRGB(x, y, ncolor.getRGB());
					} else {
						copy.setRGB(x, y, rgb);
					}
				}
			}

			// write the bufferedImage back to image folder where it will be used.
			ImageIO.write(copy, "png", new File(SignLink.getCacheDir() + "/images/img/" + (file.getName().replace(".png", "").replace(".PNG", "")) + ".png"));

			//// this writes the bufferedImage into a byte array called resultingBytes
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			ImageIO.write(copy, "png", byteArrayOut);

			System.out.println("updated : " + file.getName());
		}

		//Images ready to be packed.
		File[] files = new File(SignLink.getCacheDir() + "/images/img/").listFiles();

		assert files != null;
		for(File file : files) {
			//System.out.println(file.getName());
			if(file.getName().contains("Store"))//Ignoring mac store file.
				continue;
			if(!file.getName().toLowerCase().contains(".png")) {
				System.out.println("Found something else than a png image." + file.getName());
				continue;
			}
			//Gathering data.
			BufferedImage image = ImageIO.read(file.getAbsoluteFile());
			final int index = Integer.parseInt(file.getName().replace(".png", "").replace(".PNG", ""));
			final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
			final int width = image.getWidth();
			final int height = image.getHeight();
			final boolean alpha = image.getColorModel().hasAlpha();

			int xOffset = 0;
			int yOffset = 0;
			BufferedReader br = new BufferedReader(new FileReader(SignLink.getCacheDir() + "/images/offsets.txt"));

			String line;
			while((line = br.readLine()) != null) {
				String[] a = line.split("-");
				if(index == Integer.parseInt(a[0])) {
					xOffset = Integer.parseInt(a[1]);
					yOffset = Integer.parseInt(a[2]);
				}
			}
			br.close();

			//Preparing buf.
			Buffer data = new Buffer(new byte[((width * height) * (alpha ? 4 : 3)) + 13]);
			data.putShort(width);
			data.putShort(height);
			data.putShort(xOffset);
			data.putShort(yOffset);
			data.putByte(alpha ? 1 : 0);
			data.putInt(pixels.length);
			for(byte value : pixels) {
				data.putByte(value);
			}

			if(!cache) {
				System.out.println("Saving " + index + " to " + SignLink.getCacheDir() + "/images/out_dat/" + index + ".dat");
				FileToolkit.writeFile(SignLink.getCacheDir() + "/images/out_dat/" + index + ".dat", data.data);
			} else {
				System.out.println("Packing " + index);
				//Packing into the cache.
				byte[] pack = packRaw(data.data);
				client.cacheIdx[6].writeFile(pack.length, pack, index);
				//Setting images to null(for them to be reloaded).
				ImageCache.clear();
			}
		}
	}

	private static int method207(int i, String s) {
		try {
			for(String line : Files.readAllLines(Paths.get("./redirect.txt"))) {
				String[] l = line.split("-");
				if(l[1].equals(s) && Integer.parseInt(l[2]) == i) {
					return Integer.parseInt(l[0]);
				}
			}
		} catch(NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public static byte[] packRaw(byte[] b) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		GZIPOutputStream zos = new GZIPOutputStream(baos);
		zos.write(b);
		zos.close();

		return baos.toByteArray();
	}

}
