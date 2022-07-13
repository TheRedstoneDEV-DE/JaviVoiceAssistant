

public class MPrisCTL {
	public static void loadNative() {
		System.load(Main.MPrisLibPath);
	}
	public static native void play();
	public static native void pause();
	public static native void next();
	public static native void prev();
	public static native void stop();
	public static native void setVolume(double volume);
	public static native void init();
}
