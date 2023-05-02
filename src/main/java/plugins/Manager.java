package plugins;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import general.Main;

public class Manager {
	public static HashMap<String,JPlugin> plugins = new HashMap<String,JPlugin>();

	public void loadPlugins() {
		File dir = new File("plugins/");
		if (dir.listFiles().length > 0) {
			for (File f : dir.listFiles()) {
				if (f.getName().endsWith(".jar")) {
					try {
						System.out.println("PluginManager >> loading plugin: " + f.getName().replaceAll(".jar", ""));
						JarFile jar = new JarFile(f);
						JarEntry entry = jar.getJarEntry("plugin.cfg");
						InputStream stream = jar.getInputStream(entry);
						String main = CFGManager.get("MainClass", stream);
						String[] newVocab;
						if (CFGManager.get("NewVocab", stream).contains(" ")) {
							newVocab = CFGManager.get("NewVocab", stream).split(" ");
						} else {
							newVocab = new String[] { CFGManager.get("NewVocab", stream) };
						}
						Class cls = this.getClass();
						ClassLoader loader = URLClassLoader.newInstance(new URL[] { f.toURI().toURL() },
								cls.getClassLoader());
						Class<?> jarClass = Class.forName(main, true, loader);
						System.out.println(jarClass.isAssignableFrom(JPlugin.class));
						Class<? extends JPlugin> plugin = jarClass.asSubclass(JPlugin.class);
						Constructor<? extends JPlugin> constructor = plugin.getConstructor();
						JPlugin result = constructor.newInstance();
						plugins.put(CFGManager.get("NewCommands",stream),result);
						if (newVocab != null) {
							for (String word : newVocab) {
								if(!Main.getMain().vocab.contains(" " + word)) {
									Main.getMain().vocab = Main.getMain().vocab + " " + word;
								}
							}
						}
						result.onLoad();
						jar.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("PluginManager >> nothing to do, no plugins found!");
		}
	}

}
