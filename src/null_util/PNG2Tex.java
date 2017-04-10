package null_util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PNG2Tex {

	public static void main(String[] args) {
	}

	private static BufferedImage image;
	private static int[] raster;

	public static void convertAllFiles(String dir, boolean replace) {
		File folder = new File(dir);
		if(!folder.exists() || !folder.isDirectory()) {
			throw new IllegalArgumentException("illegal path: " + dir);
		}
		System.out.println("Starting converting PNGs...");
		ArrayList<File> todoList = new ArrayList<File>();
		for(File file : folder.listFiles()) {
			if(file.getName().toLowerCase().endsWith(".png")) {
				todoList.add(file);
			}
		}
		int size = todoList.size();
		for(int i = 0; i < size; i++) {
			File file = todoList.get(i);
			convert(file, replace);
			System.out.println("\"" + file.getName() + "\", (" + i + " / " + size + ")");
		}
		System.out.println("Done!");
	}

	private static void convert(File file, boolean replace) {
		boolean r = read(file);
		if(!r) {
			return;
		}
		String name = file.getName();
		name = name.substring(0, name.length() - 4) + ".dat";
		boolean w = write(new File(file.getParentFile(), name));
		if(w && replace) {
			file.delete();
		}
	}

	private static boolean read(File file) {
		try {
			image = ImageIO.read(file);
		} catch(IOException e) {
			System.err.println("Error while reading: \"" + file.getName() + "\"");
			return false;
		}
		try {
			raster = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		} catch(Exception e) {
			raster = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), raster, 0, image.getWidth());
		}
		return true;
	}

	private static boolean write(File file) {
		ArrayList<Integer> colorMap = new ArrayList<Integer>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		colorMap.add(0);
		int palettedSize = raster.length;
		for(int i : raster) {
			if((i & 0xffffff) == 0) {
				i = 1;
			} else if((i & 0xffffff) == 0xff00ff) {
				i = 0;
			}
			if(!colorMap.contains(i)) {
				colorMap.add(i);
				palettedSize += 3;
			}
			indexList.add(colorMap.indexOf(i));
		}
		if(file.exists()) {
			System.err.println("\"" + file.getName() + "\" already exists.");
			return false;
		}
		boolean usePalette = colorMap.size() <= 0xff && palettedSize <= raster.length * 3;
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			out.writeShort(image.getWidth());
			out.writeShort(image.getHeight());
			out.writeByte(usePalette ? colorMap.size() : 0);
			if(usePalette) {
				for(int i = 1, len = colorMap.size(); i < len; i++) {
					int c = colorMap.get(i);
					out.writeByte(c >> 16 & 0xff);
					out.writeByte(c >> 8 & 0xff);
					out.writeByte(c & 0xff);
				}
				for(int i = 0, len = indexList.size(); i < len; i++) {
					out.writeByte(indexList.get(i));
				}
			} else {
				for(int i = 0, len = raster.length; i < len; i++) {
					int c = raster[i];
					out.writeByte(c >> 16 & 0xff);
					out.writeByte(c >> 8 & 0xff);
					out.writeByte(c & 0xff);
				}
			}
			out.flush();
			out.close();
		} catch(Exception e) {
			System.err.println("Error while writing: \"" + file.getName() + "\"");
			return false;
		}
		return true;
	}
}
