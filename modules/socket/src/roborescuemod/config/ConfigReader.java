package roborescuemod.config;

import java.util.HashMap;
import java.util.Map;

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

	///////////////////////////////////////////////////////////////

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

	public String readNode(Document doc) {

		int id;
		double x, y, z;
		String value;
		String msg = "node"; // {entityID, x, y, z}

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
				msg += "," + String.valueOf(id) + "," + String.valueOf(x) + "," + String.valueOf(y) + ","
						+ String.valueOf(z);
			}
		}

		if (msg.split(",").length <= 1) {
			msg += ",None";
		}

		return msg;
	}

	/////////////////////////////////////////////////////////////////////

	private Integer[] readEdgeData(Element edge) {

		Integer[] nodes = new Integer[2];

		for (Object next3 : edge.elements("directedNode")) {

			Element directedNodeElement = (Element) next3;
			if (directedNodeElement.attributeValue("orientation").equals("-")) {
				nodes[0] = Integer.parseInt(directedNodeElement.attributeValue("href").replaceAll("#", ""));
			}
			if (directedNodeElement.attributeValue("orientation").equals("+")) {
				nodes[1] = Integer.parseInt(directedNodeElement.attributeValue("href").replaceAll("#", ""));
			}
		}

		return nodes;
	}

	public String readEdge(Document doc) {

		int id;
		Integer[] node_ids = new Integer[2];
		String msg = "edge"; // {entityID, firstID, endID}

		for (Object next : doc.getRootElement().elements("edgelist")) {
			Element edgeList = (Element) next;
			for (Object next2 : edgeList.elements("Edge")) {
				Element edge = (Element) next2;
				id = readID(edge);
				node_ids = readEdgeData(edge);
				msg += "," + String.valueOf(id) + "," + String.valueOf(node_ids[0]) + "," + String.valueOf(node_ids[1]);
			}
		}

		return msg;
	}

}