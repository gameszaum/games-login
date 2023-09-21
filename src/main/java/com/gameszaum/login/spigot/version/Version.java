package com.gameszaum.login.spigot.version;

/**
 *
 * @author netindev
 *
 */
public enum Version {

	v1_7_R1("f", "ag"), v1_7_R2("f", "ah"), v1_7_R3("f", "ai"), v1_7_R4("f", "ai"), v1_8_R1("g", "ao"), v1_8_R2("f",
			"ap"), v1_8_R3("h", "aq"), v1_9_R1("h",
			"am"), v1_9_R2("h", "am"), v1_10_R1("h", "am"), v1_11_R1("h", "an"), v1_12_R1("h", "an");

	private static final Version VERSION;

	private Version(String networkManager, String serverConnection) {
		this.networkManager = networkManager;
		this.serverConnection = serverConnection;
	}

	private final String networkManager;
	private final String serverConnection;

	public String getServerConnection() {
		return this.serverConnection;
	}

	public String getNetworkManager() {
		return this.networkManager;
	}

	public static Version getPackageVersion() {
		return Version.VERSION;
	}

	static {
		final String packageVersion = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
				.split(",")[3];
		Version tempVersion = null;
		for (Version versions : Version.values()) {
			if (packageVersion.equals(versions.toString())) {
				tempVersion = versions;
				break;
			}
		}
		VERSION = tempVersion;
	}

}
