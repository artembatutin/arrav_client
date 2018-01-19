package net.arrav.util;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class WebToolkit {

	private WebToolkit() {
	}

	public static void openWebpage(URI uri) {
		final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch(URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void openWebpage(String url) {
		try {
			openWebpage(new URL(url));
		} catch(MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
