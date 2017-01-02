package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarUnpacker {
	private URLClassLoader loader = null;
	private File f;
	
	public JarUnpacker(File jar) throws MalformedURLException{
		this.f = jar;
		loader = new URLClassLoader(new URL[] { f.toURI().toURL() }, this.getClass().getClassLoader());
	}
	
	@SuppressWarnings("rawtypes")
	public JarInputStream unpack() throws IOException {
		InputStream inStream = new FileInputStream(f);
		JarInputStream in = new JarInputStream(inStream);	
		return in;
	}

	public URLClassLoader getLoader() {
		return loader;
	}
}
