package dbus.mpris;

import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.interfaces.DBusInterface;

@DBusInterfaceName("org.mpris.MediaPlayer2.Player")
public interface PlasmaBrowserExtension extends DBusInterface {
	String PlaybackStatus();
    void Play();
    void Pause();
    void Next();
    void Previous();
}
