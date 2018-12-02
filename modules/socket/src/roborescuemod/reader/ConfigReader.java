package roborescuemod.reader;

import java.util.ArrayList;

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

	private String readID(Element node) {
		return node.attributeValue("id");
	}

	public ArrayList<String> readNode(Document doc) {

		double x, y, z;
		String value, id;
		ArrayList<String> msgs = new ArrayList<>();

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
				// {entityID, x, y, z}
				msgs.add("node," + id + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z));
			}
		}
		return msgs;
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

	public ArrayList<String> readEdge(Document doc) {

		String id;
		Integer[] node_ids = new Integer[2];
		ArrayList<String> msgs = new ArrayList<>();

		for (Object next : doc.getRootElement().elements("edgelist")) {
			Element edgeList = (Element) next;
			for (Object next2 : edgeList.elements("Edge")) {
				// {entityID, firstID, endID}
				Element edge = (Element) next2;
				id = readID(edge);
				node_ids = readEdgeData(edge);
				msgs.add("edge," + id + "," + String.valueOf(node_ids[0]) + "," + String.valueOf(node_ids[1]));
			}
		}
		return msgs;
	}

	/////////////////////////////////////////////////////////////////////
	private String readRoadData(Element road) {

		String data = "";

		for (Object next3 : road.elements("Face")) {
			Element face = (Element) next3;
			for (Object next4 : face.elements("directedEdge")) {
				Element directededge = (Element) next4;
				data += "," + directededge.attributeValue("href").replaceAll("#", "");
			}
		}
		return data;
	}

	public ArrayList<String> readRoads(Document doc) {

		String id;
		ArrayList<String> msgs = new ArrayList<>();

		for (Object next : doc.getRootElement().elements("roadlist")) {
			Element roadList = (Element) next;
			for (Object next2 : roadList.elements("road")) {
				Element road = (Element) next2;
				id = readID(road);
				// {entityID, floor, material, edgeID,・・・ }
				msgs.add("road," + id + readRoadData(road));
			}
		}
		return msgs;
	}

	/////////////////////////////////////////////////////////////////////

	private String readBuildingData(Element building) {

		String data = "";

		for (Object next3 : building.elements("Face")) {
			Element face = (Element) next3;

			// importance = Integer.parseInt(face.attributeValue("importance"));
			data += "," + face.attributeValue("floors");
			data += "," + face.attributeValue("buildingcode");

			for (Object next4 : face.elements("directedEdge")) {
				Element directededge = (Element) next4;
				data += "," + directededge.attributeValue("href").replaceAll("#", "");
			}
		}
		return data;
	}

	public ArrayList<String> readBuildings(Document doc) {

		String id;
		ArrayList<String> msgs = new ArrayList<>();

		for (Object next : doc.getRootElement().elements("buildinglist")) {
			Element roadList = (Element) next;
			for (Object next2 : roadList.elements("building")) {
				Element building = (Element) next2;
				id = readID(building);
				// {entityID, floor, material, edgeID,・・・ }
				msgs.add("building," + id + readBuildingData(building));
			}
		}
		return msgs;
	}

	//////////////////////////////////////////////////////////////////////////////////

	public String readNeighbourID(Element building) {

		String data = "";

		for (Object next3 : building.elements("Face")) {
			Element face = (Element) next3;
			for (Object next4 : face.elements("directedEdge")) {
				Element directededge = (Element) next4;
				System.out.println("################neighbour " + directededge.attributeValue("neighbour"));
				if (directededge.attributeValue("neighbour") != null) {
					data += "," + directededge.attributeValue("neighbour");
				}
			}
		}
		return data;
	}

	public ArrayList<String> readBuildingNeighbour(Document doc) {

		ArrayList<String> msgs = new ArrayList<>();

		for (Object next : doc.getRootElement().elements("buildinglist")) {
			Element roadList = (Element) next;
			for (Object next2 : roadList.elements("building")) {
				Element building = (Element) next2;
				// {edgeID, ・・・}
				String tmp = readNeighbourID(building);
				if (tmp != "") {
					msgs.add("building_neighbour" + tmp);
				}
			}
		}
		return msgs;
	}

}