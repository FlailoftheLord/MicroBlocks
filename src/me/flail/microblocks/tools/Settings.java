package me.flail.microblocks.tools;

import java.util.HashMap;
import java.util.Map;

import me.flail.microblocks.user.User;

public class Settings extends Logger {
	private DataFile settings;

	protected Settings(User user) {
		settings = user.dataFile();
	}

	/**
	 * use {@link #load()} to load the settings for the first time the server starts.
	 */
	public Settings() {
		settings = new DataFile("Settings.yml");

	}

	public void load() {
		settings.setHeader(header);

		loadDefaultValues();
	}

	private void loadDefaultValues() {
		Map<String, Object> values = new HashMap<>();

		values.put("DataSaveInterval", Integer.valueOf(3));

		setValues(settings, values);
	}

	private String header =  "-----------------------------------------------------------------\r\n" +
			"==================================================================#\r\n" +
			"                                                                  #\r\n" +
			"                MicroBlocks by FlailoftheLord.                    #\r\n" +
			"         -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                  #\r\n" +
			"           If you have any Questions or feedback                  #\r\n" +
			"              Join my discord server here:                        #\r\n" +
			"               https://discord.gg/wuxW5PS                         #\r\n" +
			"   ______               __        _____                           #\r\n" +
			"   |       |           /  \\         |        |                    #\r\n" +
			"   |__     |          /____\\        |        |                    #\r\n" +
			"   |       |         /      \\       |        |                    #\r\n" +
			"   |       |_____   /        \\    __|__      |______              #\r\n" +
			"                                                                  #\r\n" +
			"==================================================================#\r\n" +
			"-----------------------------------------------------------------\r\n" +
			"- - -\r\n" +
			" Set the Data saving interval in minutes.\r\n" +
			" This saves all block and user data to the database, can be very laggy if there are alot of players online.\r\n";



	protected void setValues(DataFile file, Map<String, Object> values) {
		for (String key : values.keySet()) {
			if (!file.hasValue(key)) {
				file.setValue(key, values.get(key));
			}
		}
	}

}

