package net.edge;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class Session implements Runnable {

	/*
	 * Declared fields.
	 */
	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final Socket socket;
	private final ClientEngine applet;
	private byte[] writeBuffer;
	private int writeIndex;
	private int buffIndex;
	private boolean closed;
	private boolean isWriter;
	private boolean hasIOError;
	private long lastCountTime;
	private int inCount;
	private int outCount;
	public int outPerSec;
	public int inPerSec;

	/**
	 * Main constructor
	 */
	public Session(ClientEngine applet, Socket socket) throws IOException {
		closed = false;
		isWriter = false;
		hasIOError = false;
		this.applet = applet;
		this.socket = socket;
		socket.setSoTimeout(30000);
		socket.setTcpNoDelay(true);
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
	}

	/**
	 * How many bytes are available.
	 */
	public int available() throws IOException {
		if(closed) {
			return 0;
		} else {
			return inputStream.available();
		}
	}

	/**
	 * Closes the connection.
	 */
	public void close() {
		closed = true;
		try {
			if(inputStream != null) {
				inputStream.close();
			}
			if(outputStream != null) {
				outputStream.close();
			}
			if(socket != null) {
				socket.close();
			}
		} catch(final IOException _ex) {
			System.out.println("Error closing stream");
		}
		isWriter = false;
		synchronized(this) {
			notify();
		}
		writeBuffer = null;
	}

	/**
	 * Updates the input and output speed per second.
	 */
	public void updateIOPerSec() {
		long time = System.currentTimeMillis();
		float ds = (float) (time - lastCountTime) / 1000f;
		if(ds < 1)
			return;
		outPerSec = (int) (outCount / ds);
		inPerSec = (int) (inCount / ds);
		inCount = 0;
		outCount = 0;
		lastCountTime = time;
	}

	/**
	 * Reads one byte from the stream.
	 */
	public void read(byte buffer[], int length) throws IOException {
		int i = 0;
		if(closed) {
			return;
		}
		int k;
		for(; length > 0; length -= k) {
			inCount += length;
			k = inputStream.read(buffer, i, length);
			if(k <= 0) {
				throw new IOException("EOF");
			}
			i += k;
		}

	}

	/**
	 * Writes the bytes from the buffer to a waiting queue and starts the streaming process if needed.
	 */
	public void write(byte buffer[], int length) throws IOException {
		if(closed) {
			return;
		}
		if(hasIOError) {
			hasIOError = false;
			throw new IOException("Error in writer thread");
		}
		if(writeBuffer == null) {
			writeBuffer = new byte[5000];
		}
		synchronized(this) {
			for(int l = 0; l < length; l++) {
				writeBuffer[buffIndex] = buffer[l];
				buffIndex = (buffIndex + 1) % 5000;
				if(buffIndex == (writeIndex + 4900) % 5000) {
					throw new IOException("buffer overflow");
				}
			}
			if(!isWriter) {
				isWriter = true;
				applet.startThread(this, 3);
			}
			notify();
		}
	}

	/**
	 * Reads one byte from the stream.
	 */
	public int read() throws IOException {
		if(closed) {
			return 0;
		} else {
			inCount++;
			return inputStream.read();
		}
	}

	/**
	 * Handles the actual writing process.
	 */
	@Override
	public void run() {
		while(isWriter) {
			int i;
			int j;
			synchronized(this) {
				if(buffIndex == writeIndex) {
					try {
						wait();
					} catch(final InterruptedException _ex) {
					}
				}
				if(!isWriter) {
					return;
				}
				j = writeIndex;
				if(buffIndex >= writeIndex) {
					i = buffIndex - writeIndex;
				} else {
					i = 5000 - writeIndex;
				}
			}
			if(i > 0) {
				try {
					outCount += i;
					outputStream.write(writeBuffer, j, i);
				} catch(final IOException _ex) {
					hasIOError = true;
				}
				writeIndex = (writeIndex + i) % 5000;
				try {
					if(buffIndex == writeIndex) {
						outputStream.flush();
					}
				} catch(final IOException _ex) {
					hasIOError = true;
				}
			}
		}
	}
}
