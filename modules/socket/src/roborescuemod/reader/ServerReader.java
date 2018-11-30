package roborescuemod.reader;

import kernel.KernelState;
import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.PoliceForce;

public class ServerReader {

	public String readerTime(KernelState state) {
		String msg = "time";
		msg += "," + String.valueOf(state.getTime());
		return msg;
	}

	public String readerAgentSteta(Human human) {

		String msg = "";

		/*if (human instanceof Civilian) {
			Civilian civilian = (Civilian) human;
			// {civilian, entityID, HP, historyX, historyZ ・・・}
			msg += "civilian_steta";
			msg += "," + String.valueOf(civilian.getID());
			msg += "," + String.valueOf(civilian.getHP());
			try {
				for (int p : civilian.getPositionHistory()) {
					msg += "," + String.valueOf(p);
				}
			} catch (NullPointerException e) {
				System.out.println("ぬるぽ。");
			}
			return msg;
		}*/

		if (human instanceof AmbulanceTeam) {
			AmbulanceTeam ambulanceTeam = (AmbulanceTeam) human;
			// {ambulanceteam, enriryID, HP, historyX, historyZ ・・・}
			msg += "ambulanceteam_steta";
			msg += "," + String.valueOf(ambulanceTeam.getID());
			msg += "," + String.valueOf(ambulanceTeam.getHP());
			try {
				for (int p : ambulanceTeam.getPositionHistory()) {
					msg += "," + String.valueOf(p);
				}
			} catch (NullPointerException e) {
				System.out.println("ぬるぽ。");
			}
			return msg;
		}
/*
		if (human instanceof FireBrigade) {
			FireBrigade fireBrigade = (FireBrigade) human;
			// {firebrigade, enriryID, HP, Water, historyX, historyZ ・・・}
			msg += "firebrigade_steta";
			msg += "," + String.valueOf(fireBrigade.getID());
			msg += "," + String.valueOf(fireBrigade.getHP());
			msg += "," + String.valueOf(fireBrigade.getWater());
			try {
				for (int p : fireBrigade.getPositionHistory()) {
					msg += "," + String.valueOf(p);
				}
			} catch (NullPointerException e) {
				System.out.println("ぬるぽ。");
			}
			return msg;
		}

		if (human instanceof PoliceForce) {
			PoliceForce policeForce = (PoliceForce) human;
			// {policeforce, enriryID, HP, historyX, historyZ ・・・}
			msg += "policeforce_steta";
			msg += "," + String.valueOf(policeForce.getID());
			msg += "," + String.valueOf(policeForce.getHP());
			try {
				for (int p : policeForce.getPositionHistory()) {
					msg += "," + String.valueOf(p);
				}
			} catch (NullPointerException e) {
				System.out.println("ぬるぽ。");
			}
			return msg;
		}*/
		return msg;
	}

	public String readerBuildingState(Building building) {
		// {building_state, entityID, brokenness, fieriness, temperature}
		String msg = "building_state";
		msg += "," + String.valueOf(building.getID());
		msg += "," + String.valueOf(building.getBrokenness());
		msg += "," + String.valueOf(building.getFieryness());
		msg += "," + String.valueOf(building.getTemperature());
		return msg;
	}

	public String readerBlockade(Blockade blockade) {
		// {blockade, entityID, apexes, ・・・}
		String msg = "blockade_steta";
		msg += "," + String.valueOf(blockade.getID());
		for (int p : blockade.getApexes()) {
			msg += "," + String.valueOf(p);
		}
		return msg;
	}
}
