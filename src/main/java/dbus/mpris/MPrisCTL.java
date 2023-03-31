package dbus.mpris;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.freedesktop.dbus.DBusMap;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.types.Variant;

import configuration.Manager;
import types.Metadata;

public class MPrisCTL {
	PlasmaBrowserExtension pbe;
	DBusConnection dbusconnection;
	Manager man = new Manager();
	public void init() {
		try {
			DBusConnection dc = DBusConnectionBuilder.forSessionBus().build();
			dbusconnection = dc;
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
		pbe.Previous();
	}
	public Metadata getMetadata(){
		try {

            // fetch properties
            Properties properties = dbusconnection.getRemoteObject(man.get("mpris-module-busname"), man.get("mpris-module-objectpath"), Properties.class);

            // get the 'Sessions', which returns a complex type
            final Map<String,Variant<?>> metadata = properties.Get("org.mpris.MediaPlayer2.Player", "Metadata");

			Metadata meta = new Metadata();
			meta.data=metadata;
			

			return meta;
        }catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
