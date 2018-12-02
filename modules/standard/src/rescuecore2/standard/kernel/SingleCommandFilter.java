package rescuecore2.standard.kernel;

import kernel.CommandFilter;
import kernel.KernelState;

import rescuecore2.config.Config;
import rescuecore2.messages.Command;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import roborescuemod.kernel.SocketServer;
import roborescuemod.reader.ConfigReader;
import roborescuemod.reader.GmlReader;
import roborescuemod.reader.ServerReader;
import rescuecore2.log.Logger;

import rescuecore2.standard.messages.AKClearArea;
import rescuecore2.standard.messages.AKRest;
import rescuecore2.standard.messages.AKMove;
import rescuecore2.standard.messages.AKLoad;
import rescuecore2.standard.messages.AKUnload;
import rescuecore2.standard.messages.AKRescue;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.Road;
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
	public SocketServer socketServer;
	public GmlReader gmlReader;
	public ServerReader serverReader;
	public Document document;
	public boolean registered = false;
	private boolean sokcetLock = false;

	@Override
	public void initialise(Config config) {

		if (!sokcetLock) {
			socketServer = new SocketServer(12345, "localhost");
			serverReader = new ServerReader();

			// configを送信
			configReader = new ConfigReader();
			gmlReader = new GmlReader();

			document = gmlReader.openGML(configReader.getGmlPath(config));

			socketServer.publishConfig(configReader.readNode(document));
			socketServer.publishConfig(configReader.readEdge(document));
			socketServer.publishConfig(configReader.readRoads(document));
			socketServer.publishConfig(configReader.readBuildings(document));
			socketServer.publishConfig(configReader.readBuildingNeighbour(document)); //neighbour

			socketServer.publishCommand("registry_map");

			socketServer.waitCommand("ready_map");
		}
	}

	@Override
	public void filter(Collection<Command> commands, KernelState state) {

		System.out.println(state.getTime());

		for (Entity entity : state.getWorldModel().getAllEntities()) {
			if (entity instanceof Blockade) {
				System.out.println(((Blockade) entity).getURN());
				for (int i : ((Blockade) entity).getApexes()) {
					System.out.println(i);
				}
			}
		}

		if (!sokcetLock) {
			// time 0の時のみ
			if (!registered) {
				for (Entity entity : state.getWorldModel().getAllEntities()) {
					if (entity instanceof Human) {
						Human h = (Human) entity;
						System.out.println(h.getURN().split(":")[3]);
						socketServer.publishScenario(
								"scenario," + h.getURN().split(":")[3] + "," + String.valueOf(h.getID().getValue())
										+ "," + String.valueOf(h.getPosition().getValue()));
					}
				}
				socketServer.publishCommand("orient_scenario");
				socketServer.waitCommand("ready_scenario");
				registered = true;
			} else {
				// 毎time
				for (Entity entity : state.getWorldModel().getAllEntities()) {
					if (entity instanceof Human) {
						socketServer.publishStetas(serverReader.readerAgentSteta((Human) entity));
					}
					if (entity instanceof Road) {
					}
					if (entity instanceof Building) {
						socketServer.publishStetas(serverReader.readerBuildingState((Building) entity));
					}
					if (entity instanceof Blockade) {
						socketServer.publishStetas(serverReader.readerBlockade((Blockade) entity));
					}
				}
				socketServer.publishStetas(serverReader.readerTime(state));
				socketServer.publishCommand("orient_stetas");
				socketServer.waitCommand("next");
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
