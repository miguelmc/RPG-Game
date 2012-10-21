package game.scripting;

import game.features.Quest;
import game.ui.MsgBoxManager;
import game.ui.Shop;

/**
 * The object passed to the NPC scripts. This object's public methods can be
 * used in the script.
 */
public class NPCConversationManager extends AbstractScriptManager {

	private int state = 0;
	private Shop shop;

	public void sendOk(String s, int state) {
		MsgBoxManager.sendText(s, false);
		MsgBoxManager.setState(state);
	}

	public void sendOk(String s) {
		sendOk(s, -1);
	}

	public void sendYesNo(String s, int stateYes, int stateNo) {
		MsgBoxManager.sendText(s, true);
		MsgBoxManager.setYesNo(stateYes, stateNo);
	}

	public int getState() {
		return state;
	}

	public void setState(int state2) {
		state = state2;
	}

	public void activateQuest(int id) {
		Quest.activate(id);
	}

	public boolean isQuestAvailable(int id) {
		if (Quest.getReqLevel(id) <= getPlayer().getLevel()) {
			return true;
		}
		return false;
	}

	public boolean isQuestComplete(int id) {
		if (getPlayer().hasQuest(id)) {
			return getPlayer().getQuest(id).isCompleted()
					&& !getPlayer().getQuest(id).isTurnedIn();
		}
		return false;
	}

	public boolean isQuestInProgress(int id) {
		if (getPlayer().hasQuest(id)) {
			return !getPlayer().getQuest(id).isTurnedIn();
		}
		return false;
	}

	public boolean isQuestTurnedIn(int id) {
		if (getPlayer().hasQuest(id))
			return getPlayer().getQuest(id).isTurnedIn();
		return false;
	}

	public void turnQuestIn(int id) {
		if (getPlayer().hasQuest(id) && getPlayer().getQuest(id).isCompleted()) {
			getPlayer().getQuest(id).turnIn();
		}
	}

	public void openShop(String id) {
		shop = new Shop(Integer.parseInt(id, 16));
	}

	public boolean givePlayer(int id) {
		return getPlayer().addItem(shop.getItemByID(id));
	}

}
