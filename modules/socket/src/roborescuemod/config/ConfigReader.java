package roborescuemod.config;

import org.dom4j.Document;
import org.dom4j.Element;

import rescuecore2.config.Config;

public class ConfigReader {

	public String getGmlPath(Config config) {
		return config.getValue("gis.map.dir") + config.getValue("gis.map.file");
	}

	public String getScenarioPath(Config config) {
		return config.getValue("gis.map.dir") + config.getValue("gis.map.scenario");
	}

	private static int readID(Element node) {

		String s = node.attributeValue("id");

		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void readNode(Document doc) {

		int id;
		double x, y, z;
		String value;
		String msgs = "node"; // {entityID, x, y, z}

		for (Object next : doc.getRootElement().elements("nodelist")) {
			Element nodeList = (Element) next;
			for (Object next2 : nodeList.elements("Node")) {
				Element node = (Element) next2;
				// 座標
				value = node.getStringValue().replaceAll("\n", "").replaceAll(" ", "");
				x = Double.parseDouble(value.split(",", 0)[0]);
				y = 0.0;
				z = Double.parseDouble(value.split(",", 0)[1]);
				// entityID
				id = readID(node);
				msgs += "," + String.valueOf(id) + "," + String.valueOf(x) + "," + String.valueOf(y) + ","
						+ String.valueOf(z);
			}
		}

	}

}