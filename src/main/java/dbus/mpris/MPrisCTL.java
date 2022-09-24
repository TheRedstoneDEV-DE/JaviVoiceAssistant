package dbus.mpris;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder;
import org.freedesktop.dbus.exceptions.DBusException;

import configuration.Manager;

public class MPrisCTL {
	PlasmaBrowserExtension pbe;
	Manager man = new Manager();
	public void init() {
		try {
			DBusConnection dc = DBusConnectionBuilder.forSessionBus().build();
			pbe = (PlasmaBrowserExtension) dc.getRemoteObject(man.get("mpris-module-busname"),
					man.get("mpris-module-objectpath"), PlasmaBrowserExtension.class);
		} catch (DBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void play() {
		pbe.Play();
	}
	public void pause() {
		pbe.Pause();
	}
	public void next() {
		pbe.Next();
	}
	public void prev() {
		pbe.Previos();
	}
}
