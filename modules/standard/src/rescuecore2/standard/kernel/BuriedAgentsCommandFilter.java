package rescuecore2.standard.kernel;

import kernel.AbstractCommandFilter;
import kernel.KernelState;

import rescuecore2.messages.Command;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.log.Logger;

import rescuecore2.standard.entities.Human;
import rescuecore2.standard.messages.AKSubscribe;
import rescuecore2.standard.socketcommunication.KernelNode;
import rescuecore2.standard.messages.AKSpeak;
import rescuecore2.standard.messages.AKSay;

/**
 * A CommandFilter that discards commands from buried agents.
 */
public class BuriedAgentsCommandFilter extends AbstractCommandFilter {

	KernelNode kernelNode = null;

	public BuriedAgentsCommandFilter() {
		kernelNode = new KernelNode();
		kernelNode.socketThread();
	}

	@Override
	protected boolean allowed(Command c, KernelState state) {

		System.out.println(c.getURN());

		EntityID id = c.getAgentID();
		Entity e = state.getWorldModel().getEntity(id);

		if ((c instanceof AKSubscribe) || (c instanceof AKSpeak) || (c instanceof AKSay)) {
			return true;
		}

		if (e instanceof Human) {

			System.out.println(c.getTime());

			Human h = (Human) e;

			kernelNode.addAgentData(c.getTime(), h.getURN().split(":")[3], h.getID().getValue(),
					h.getPositionHistory());

			if (h.isBuriednessDefined() && h.getBuriedness() > 0) {
				Logger.info("Ignoring command " + c + ": Agent " + h + " is buried");
				return false;
			}
		}

		return true;
	}
}
