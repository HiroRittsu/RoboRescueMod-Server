package roborescuemod.reader;

import java.util.ArrayList;

import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.PoliceForce;

public class ServerReader {

	public ArrayList<String> readerAgentSteta(Human human) {

		ArrayList<String> msgs = new ArrayList<>();

		if (human instanceof Civilian) {
			Civilian civilian = (Civilian) human;
			// {civilian, entityID, HP, historyX, historyZ ・・・}
			msgs.add("civilian");
			msgs.add(String.valueOf(civilian.getID()));
			msgs.add(String.valueOf(civilian.getHP()));
			try {
				for (int p : civilian.getPositionHistory()) {
					msgs.add(String.valueOf(p));
				}
			} catch (NullPointerException e) {
				System.out.println("ぬるぽ。");
			}
		}

		if (human instanceof AmbulanceTeam) {
			AmbulanceTeam ambulanceTeam = (AmbulanceTeam) human;
			// {ambulanceteam, enriryID, HP, historyX, historyZ ・・・}
			msgs.add("ambulanceteam");
			msgs.add(String.valueOf(ambulanceTeam.getID()));
			msgs.add(String.valueOf(ambulanceTeam.getHP()));
			try {
				for (int p : ambulanceTeam.getPositionHistory()) {
					msgs.add(String.valueOf(p));
				}
			} catch (NullPointerException e) {
				System.out.println("ぬるぽ。");
			}
		}

		if (human instanceof FireBrigade) {
			FireBrigade fireBrigade = (FireBrigade) human;
			// {firebrigade, enriryID, HP, Water, historyX, historyZ ・・・}
			msgs.add("firebrigade");
			msgs.add(String.valueOf(fireBrigade.getID()));
			msgs.add(String.valueOf(fireBrigade.getHP()));
			msgs.add(String.valueOf(fireBrigade.getWater()));
			try {
				for (int p : fireBrigade.getPositionHistory()) {
					msgs.add(String.valueOf(p));
				}
			} catch (NullPointerException e) {
				System.out.println("ぬるぽ。");
			}
		}

		if (human instanceof PoliceForce) {
			PoliceForce policeForce = (PoliceForce) human;
			// {policeforce, enriryID, HP, historyX, historyZ ・・・}
			msgs.add("ambulanceteam");
			msgs.add(String.valueOf(policeForce.getID()));
			msgs.add(String.valueOf(policeForce.getHP()));
			try {
				for (int p : policeForce.getPositionHistory()) {
					msgs.add(String.valueOf(p));
				}
			} catch (NullPointerException e) {
				System.out.println("ぬるぽ。");
			}
		}
		return msgs;
	}

}