package roborescuemod.config;

import rescuecore2.config.Config;

public class ConfigReader {

	public String getGmlPath(Config config) {
		return config.getValue("gis.map.dir") + config.getValue("gis.map.file");
	}

	public String getScenarioPath(Config config) {
		return config.getValue("gis.map.dir") + config.getValue("gis.map.scenario");
	}

}