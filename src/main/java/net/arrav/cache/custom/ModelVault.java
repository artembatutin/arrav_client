package net.arrav.cache.custom;

import net.arrav.util.Compressionutil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static java.nio.file.StandardOpenOption.READ;

@SuppressWarnings("all")
public final class ModelVault implements Closeable {

    private FileChannel dataChannel;
    private FileChannel metaChannel;

    private int modelCount;

    private ArrayList<Integer> models = new ArrayList<>();

    public boolean contains(int id) {
        return models.contains(id);
    }

    public void init(File dataFile, File metaFile){
        try {
        if (!dataFile.exists() || !metaFile.exists()) {
            throw new IOException(String.format("Could not find models file"));
        }
        dataChannel = FileChannel.open(dataFile.toPath(), READ);
        metaChannel = FileChannel.open(metaFile.toPath(), READ);

        modelCount = (int) (metaChannel.size() / 10);


            for(int id = 0; id < modelCount; id++) {

                metaChannel.position(id * 10);
                final ByteBuffer metaBuf = ByteBuffer.allocate(10);
                metaChannel.read(metaBuf);
                metaBuf.flip();

                final int position = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
                final int len = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
                if(len != 0)
                    models.add(id);

            }
            System.out.println("Loaded "+modelCount+" models.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getModelCount() {
        return modelCount;
    }


    public byte[] getData(int id) {
        try {
            if (!dataChannel.isOpen() || !metaChannel.isOpen()) {
                System.out.println("Model channels are closed!");
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
            final int offsetX = metaBuf.getShort() & 0xFF;
            final int offsetY = metaBuf.getShort() & 0xFF;

            final ByteBuffer dataBuf = ByteBuffer.allocate(len);
            dataChannel.position(pos);
            dataChannel.read(dataBuf);
            ((Buffer) dataBuf).flip();

            byte[] data = Compressionutil.gunzip(dataBuf.array());
            if(data == null)
                System.err.println("Error getting model "+id);
            return data;
        } catch (IOException e) {
            System.out.println(String.format("Error loading model for id=%d", id));
            e.printStackTrace();
        }

        System.out.println(String.format("No model data found for id=%d", id));
        return null;
    }


    public void close() throws IOException {
        dataChannel.close();
        metaChannel.close();
    }
}
