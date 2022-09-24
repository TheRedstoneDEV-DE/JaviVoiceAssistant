package dbus.mpris;

import java.util.Map;

import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.Variant;


@DBusInterfaceName("org.mpris.MediaPlayer2.Player")
public interface PlasmaBrowserExtension extends DBusInterface {
	String PlaybackStatus();
    void Play();
    void Pause();
    void Next();
    void Previos();
}
