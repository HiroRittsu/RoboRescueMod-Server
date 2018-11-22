package roborescuemod.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GMLReader {

	public Document openGML(String PATH) {

		SAXReader reader = new SAXReader();
		Document doc = null;

		try {
			doc = reader.read(PATH);
		} catch (DocumentException e) {
			System.out.println("Not Found");
			e.printStackTrace();
		}

		return doc;
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

	public Building readBuildingData(Element building, Map<Integer, Edge> edges) {

		ArrayList<Integer[]> edge_ids = new ArrayList<>();
		int id = readID(building);
		int floors = 0;
		int buildingcode = 0;
		int importance = 0;

		for (Object next3 : building.elements("Face")) {
			Element face = (Element) next3;

			floors = Integer.parseInt(face.attributeValue("floors"));
			buildingcode = Integer.parseInt(face.attributeValue("buildingcode"));
			importance = Integer.parseInt(face.attributeValue("importance"));

			for (Object next4 : face.elements("directedEdge")) {
				Element directededge = (Element) next4;

				Edge edge = edges.get(Integer.parseInt(directededge.attributeValue("href").replaceAll("#", "")));
				edge_ids.add(edge.getNodeID());
			}
		}
		return new Building(id, floors, "Wood", edge_ids);
	}

	public ArrayList<Building> readBuildings(Document doc, Map<Integer, Edge> edges) {

		if (doc == null)
			return null;

		ArrayList<Building> result = new ArrayList<>();

		for (Object next : doc.getRootElement().elements("buildinglist")) {
			Element roadList = (Element) next;
			for (Object next2 : roadList.elements("building")) {
				Element building = (Element) next2;
				result.add(readBuildingData(building, edges));
			}
		}
		return result;
	}

	public Road readRoadData(Element road, Map<Integer, Edge> edges) {

		int id = readID(road);
		ArrayList<Integer[]> edge_id = new ArrayList<>();

		for (Object next3 : road.elements("Face")) {
			Element face = (Element) next3;
			for (Object next4 : face.elements("directedEdge")) {
				Element directededge = (Element) next4;

				Edge edge = edges.get(Integer.parseInt(directededge.attributeValue("href").replaceAll("#", "")));

				edge_id.add(edge.getNodeID());
			}
		}
		return new Road(id, edge_id);
	}

	public ArrayList<Road> readRoads(Document doc, Map<Integer, Edge> edges) {

		if (doc == null)
			return null;

		ArrayList<Road> result = new ArrayList<>();

		for (Object next : doc.getRootElement().elements("roadlist")) {
			Element roadList = (Element) next;
			for (Object next2 : roadList.elements("road")) {
				Element road = (Element) next2;

				result.add(readRoadData(road, edges));
			}
		}
		return result;
	}

	private Edge readEdgeData(Element edge) {

		int id = readID(edge);
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

		return new Edge(id, nodes);

	}

	public Map<Integer, Edge> readEdge(Document doc) {

		if (doc == null)
			return null;

		Map<Integer, Edge> result = new HashMap<>();

		for (Object next : doc.getRootElement().elements("edgelist")) {
			Element edgeList = (Element) next;
			for (Object next2 : edgeList.elements("Edge")) {
				Element edge = (Element) next2;
				readEdgeData(edge);
				result.put(readID(edge), readEdgeData(edge));
			}
		}
		return result;
	}

	public Map<Integer, Point3D> readNode(Document doc) {

		Map<Integer, Point3D> result = new HashMap<>();
		String value;
		int x;
		int z;

		for (Object next : doc.getRootElement().elements("nodelist")) {
			Element nodeList = (Element) next;
			for (Object next2 : nodeList.elements("Node")) {
				Element node = (Element) next2;
				// 座標
				value = node.getStringValue().replaceAll("\n", "").replaceAll(" ", "");
				x = (int) Double.parseDouble(value.split(",", 0)[0]);
				z = (int) Double.parseDouble(value.split(",", 0)[1]);

				result.put(readID(node), new Point3D(x, 0, z));
			}
		}

		return result;
	}

}