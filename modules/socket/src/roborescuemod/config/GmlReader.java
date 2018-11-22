package roborescuemod.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class GmlReader {

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
	
	

}
