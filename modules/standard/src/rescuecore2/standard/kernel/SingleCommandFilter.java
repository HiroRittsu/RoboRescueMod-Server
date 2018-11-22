package rescuecore2.standard.kernel;

import kernel.CommandFilter;
import kernel.KernelState;

import rescuecore2.config.Config;
import rescuecore2.messages.Command;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import roborescuemod.config.ConfigReader;
import roborescuemod.config.GmlReader;
import rescuecore2.log.Logger;

import rescuecore2.standard.messages.AKClearArea;
import rescuecore2.standard.messages.AKRest;
import rescuecore2.standard.messages.AKMove;
import rescuecore2.standard.messages.AKLoad;
import rescuecore2.standard.messages.AKUnload;
import rescuecore2.standard.messages.AKRescue;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.messages.AKClear;
import rescuecore2.standard.messages.AKExtinguish;

import java.util.Set;

import org.dom4j.Document;

import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * A CommandFilter that ensures only one non-communication command is allowed
 * per agent.
 */
public class SingleCommandFilter implements CommandFilter {

	public ConfigReader configReader;
	public GmlReader gmlReader;
	public Document document;

	@Override
	public void initialise(Config config) {

		// configを送信
		configReader = new ConfigReader();
		gmlReader = new GmlReader();

		document = gmlReader.openGML(configReader.getGmlPath(config));
		
	}

	@Override
	public void filter(Collection<Command> commands, KernelState state) {

		System.out.println(state.getTime());

		for (Entity entity : state.getWorldModel().getAllEntities()) {
			if (entity instanceof Human) {
				System.out.println(((Human) entity).getPosition().getValue());
			}
		}

		Set<EntityID> sent = new HashSet<EntityID>();
		Iterator<Command> it = commands.iterator();
		while (it.hasNext()) {
			Command c = it.next();
			if (filterable(c)) {
				EntityID sender = c.getAgentID();
				if (sent.contains(sender)) {
					it.remove();
					Logger.info("Ignoring command " + c + ": Agent " + sender + " already sent a command");
				} else {
					sent.add(sender);
					Logger.debug(sender + " sent command " + c);
				}
			}
		}
	}

	private boolean filterable(Command c) {
		return (c instanceof AKRest) || (c instanceof AKMove) || (c instanceof AKLoad) || (c instanceof AKUnload)
				|| (c instanceof AKRescue) || (c instanceof AKClear) || (c instanceof AKClearArea)
				|| (c instanceof AKExtinguish);
	}
}
