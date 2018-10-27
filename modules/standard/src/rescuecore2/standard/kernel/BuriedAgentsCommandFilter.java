package rescuecore2.standard.kernel;

import kernel.AbstractCommandFilter;
import kernel.KernelState;

import rescuecore2.messages.Command;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.log.Logger;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.messages.AKSubscribe;
import rescuecore2.standard.messages.AKSpeak;
import rescuecore2.standard.messages.AKSay;
import rescuecore2.standard.messages.AKExtinguish;

/**
 * A CommandFilter that discards commands from buried agents.
 */
public class BuriedAgentsCommandFilter extends AbstractCommandFilter {

	public BuriedAgentsCommandFilter() {
		System.out.println("###############BuriedAgentsCommandFilter###############");
	}

	@Override
	protected boolean allowed(Command c, KernelState state) {

		EntityID id = c.getAgentID();
		Entity e = state.getWorldModel().getEntity(id);
		System.out.println("################################################");
		for (Entity entity : state.getWorldModel().getAllEntities()) {
			if (entity instanceof Building) {
				System.out.println(((Building) entity).getTemperature());
			}
		}
		System.out.println("################################################");

		if ((c instanceof AKSubscribe) || (c instanceof AKSpeak) || (c instanceof AKSay)) {
			return true;
		}
		if (c instanceof AKExtinguish) {
			System.out.println("消火");
		}
		if (e instanceof Human) {
			Human h = (Human) e;
			System.out.println(
					"Time: " + state.getTime() + "Entity " + h.getURN() + "history " + h.getPositionHistoryProperty());
			if (e instanceof FireBrigade) {
				System.out.println("Water" + ((FireBrigade) e).getWater());
			}
			if (h.isBuriednessDefined() && h.getBuriedness() > 0) {
				Logger.info("Ignoring command " + c + ": Agent " + h + " is buried");
				return false;
			}
		}
		return true;
	}
}
