package net.arrav.cache.custom;

import net.arrav.graphic.img.BitmapImage;
import net.arrav.graphic.img.PaletteImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardOpenOption.READ;


public final class SpritesCache implements Closeable {

    private BitmapImage[] cache;

    private FileChannel dataChannel;
    private FileChannel metaChannel;

    private int spriteCount;

    public void init(File dataFile, File metaFile) {
        try {
            dataChannel = FileChannel.open(dataFile.toPath(), READ);
            metaChannel = FileChannel.open(metaFile.toPath(), READ);

            spriteCount = (int) (metaChannel.size() / 10);

            cache = new BitmapImage[spriteCount];
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSpriteCount() {
        return spriteCount;
    }

    public BitmapImage get(int id) {
        try {
            if (contains(id)) {
                return cache[id];
            }

            if (!dataChannel.isOpen() || !metaChannel.isOpen()) {
                System.out.println("Sprite channels are closed!");
                return null;
            }

            final int entries = (int) (metaChannel.size() / 10);

            if (id > entries) {
                //System.out.println(String.format("id=%d > size=%d", id, entries));
                return null;
            }

            metaChannel.position(id * 10);

            final ByteBuffer metaBuf = ByteBuffer.allocate(10);
            metaChannel.read(metaBuf);
            ((Buffer) metaBuf).flip();

            try {
                final int pos = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
                final int len = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
                final int offsetX = metaBuf.getShort() & 0xFF;
                final int offsetY = metaBuf.getShort() & 0xFF;

                final ByteBuffer dataBuf = ByteBuffer.allocate(len);

                dataChannel.position(pos);
                dataChannel.read(dataBuf);
                ((Buffer) dataBuf).flip();

                try (InputStream is = new ByteArrayInputStream(dataBuf.array())) {

                    BufferedImage bimage = ImageIO.read(is);

                    if (bimage == null) {
                        System.out.println(String.format("Could not read image at %d", id));
                        return null;
                    }

                    if (bimage.getType() != BufferedImage.TYPE_INT_ARGB) {
                        bimage = convert(bimage, BufferedImage.TYPE_INT_ARGB);
                    }

                    final int[] pixels = ((DataBufferInt) bimage.getRaster().getDataBuffer()).getData();

                    final BitmapImage sprite = new BitmapImage(bimage.getWidth(), bimage.getHeight(), offsetX, offsetY, pixels);

                    // cache so we don't have to perform I/O calls again
                    cache[id] = sprite;

                    return sprite;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            System.out.println("error loading sprite:"+id);
            e.printStackTrace();
        }

        System.out.println(String.format("No sprite found for id=%d", id));
        return null;
    }

    public BufferedImage getBufferedImage(int id) {
        try {
            if (!dataChannel.isOpen() || !metaChannel.isOpen()) {
                System.out.println("Sprite channels are closed!");
                return null;
            }
            final int entries = (int) (metaChannel.size() / 10);
            if (id > entries) {
                System.out.println(String.format("id=%d > size=%d", id, entries));
                return null;
            }

            metaChannel.position(id * 10);

            final ByteBuffer metaBuf = ByteBuffer.allocate(10);
            metaChannel.read(metaBuf);
            ((Buffer) metaBuf).flip();

            final int pos = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
            final int len = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
            final int offsetX = metaBuf.getShort() & 0xFF;
            final int offsetY = metaBuf.getShort() & 0xFF;

            final ByteBuffer dataBuf = ByteBuffer.allocate(len);

            dataChannel.position(pos);
            dataChannel.read(dataBuf);
            ((Buffer) dataBuf).flip();

            try (InputStream is = new ByteArrayInputStream(dataBuf.array())) {

                BufferedImage bimage = ImageIO.read(is);

                if (bimage == null) {
                    System.out.println(String.format("Could not read image at %d", id));
                    return null;
                }
                return bimage;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("No sprite found for id=%d", id));
        return null;
    }

    public byte[] getData(int id) {
        try {
            if (!dataChannel.isOpen() || !metaChannel.isOpen()) {
                System.out.println("Sprite channels are closed!");
                return null;
            }

            final int entries = Math.toIntExact(metaChannel.size() / 10);

            if (id > entries) {
                System.out.println(String.format("id=%d > squaresNeeded=%d", id, entries));
                return null;
            }

            metaChannel.position(id * 10);

            final ByteBuffer metaBuf = ByteBuffer.allocate(10);
            metaChannel.read(metaBuf);
            metaBuf.flip();

            final int pos = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
            final int len = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);

            final ByteBuffer dataBuf = ByteBuffer.allocate(len);

            dataChannel.position(pos);
            dataChannel.read(dataBuf);
            dataBuf.flip();

            return dataBuf.array();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("No sprite data found for id=%d", id));
        return null;
    }




    public PaletteImage getIndexedImage(int id) {

        BufferedImage image = getBufferedImage(id);
        List<Integer> paletteList = new LinkedList<>();
        paletteList.add(0);
        int width = image.getWidth();
        int height = image.getHeight();
        byte[] pixels = new byte[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int red = rgb >> 16 & 0xff;
                int green = rgb >> 8 & 0xff;
                int blue = rgb & 0xff;
                int alpha = rgb & 0xff;
                rgb = red << 16 | green << 8 | blue;
                if (alpha == 255) {
                    rgb = 0;
                }
                int index = paletteList.indexOf(rgb);
                if (index == -1) {

                    if (paletteList.size() < 256) {
                        index = paletteList.size();
                        paletteList.add(rgb);
                    } else {
                        throw new IllegalArgumentException("The target image has more than 255 color in the palette "+id);
                    }
                }
                pixels[x + y * width] = (byte) index;
            }
        }
        int[] palette = new int[paletteList.size()];

        final AtomicInteger index = new AtomicInteger(0);


        for (int pallet = 0; pallet < paletteList.size(); pallet++) {
            palette[index.getAndIncrement()] = paletteList.get(pallet);
        }
       return new PaletteImage(width, height, palette, pixels);
    }


    public boolean contains(int id) {
        return id < cache.length && cache[id] != null;
    }

    public void set(int id, BitmapImage sprite) {
        if (!contains(id)) {
            return;
        }

        cache[id] = sprite;
    }

    public void clear() {
        Arrays.fill(cache, null);
    }

    private static BufferedImage convert(BufferedImage bimage, int type) {
        BufferedImage converted = new BufferedImage(bimage.getWidth(), bimage.getHeight(), type);
        converted.getGraphics().drawImage(bimage, 0, 0, null);
        return converted;
    }

    public void close() throws IOException {
        dataChannel.close();
        metaChannel.close();
    }
}
