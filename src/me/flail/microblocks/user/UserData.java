package me.flail.microblocks.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.flail.microblocks.tools.DataFile;
import me.flail.microblocks.tools.Logger;

public class UserData extends Logger {
	protected UUID playerUuid;
	private DataFile file;

	public enum KickReason {
		BANNED, MUTED, WARNING, CUSTOM
	}

	protected UserData(UUID playerUuid) {
		this.playerUuid = playerUuid;
		file = new DataFile("/PlayerData/" + playerUuid + "/UserData.yml");
	}

	protected DataFile getDataFile() {
		return file;
	}

	public DataFile getBlockData() {
		return new DataFile("/PlayerData/" + playerUuid + "/BlockData.yml");
	}

	protected void loadDefaultValues(User user) {
		Map<String, Object> values = new HashMap<>();
		values.put("UUID", user.uuid().toString());
		values.put("Name", new String[] {user.name()});
		values.put("Online", "false");
		values.put("Gamemode", user.gamemode());
		values.put("Frozen", "false");
		values.put("Muted", "false");
		values.put("Banned", "false");
		/************* YOINK, tyvm! ******************/
		for (String key : values.keySet()) {
			if (!file.hasValue(key)) {
				file.setValue(key, values.get(key));
				continue;
			}
			String[] list = file.getArray(key);
			List<String> newList = new ArrayList<>();
			switch (key) {
			case "Name":
				boolean newName = false;
				for (String s : list) {
					newList.add(s);
					if (!s.equalsIgnoreCase(user.name())) {
						newName = true;
						continue;
					}
					newName = false;
				}

				if (newName) {
					newList.add(user.name());
					file.setValue(key, newList.toArray(new String[] {}));
				}
				break;
			}

		}

	}





}
