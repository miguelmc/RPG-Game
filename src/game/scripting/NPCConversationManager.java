package game.scripting;

import game.entities.NPC;
import game.features.Quest;
import game.ui.MsgBoxManager;
import game.ui.Shop;
import game.ui.window.Window;

/**
 * The object passed to the NPC scripts. This object's public methods can be
 * used in the script.
 */
public class NPCConversationManager extends AbstractScriptManager {

	private Shop shop;
	private static NPC activeNPC;
	private int state, onYes, onNo;
	private boolean yesNo;

	public void sendOk(String message, int state) {
		
		yesNo = false;
		
		if(state == -1)
		{
			MsgBoxManager.sendMessage(message, MsgBoxManager.OK);
			state = 0;
		}else
			MsgBoxManager.sendMessage(message, MsgBoxManager.OK, activeNPC);
	}

	public void sendOk(String s) {
		sendOk(s, -1);
	}

	public void sendYesNo(String message, int stateYes, int stateNo) {
		
		yesNo = true;
		onYes = stateYes;
		onNo = stateNo;
		
		MsgBoxManager.sendMessage(message, MsgBoxManager.YES_NO, activeNPC);
	}

	public int getState() {
		if(yesNo)
			return MsgBoxManager.getAnswer() ? onYes : onNo;
		return state;
	}

	public void activateQuest(int id) {
		Quest.activate(id);
	}

	public boolean isQuestAvailable(int id) {
		return Quest.getReqLevel(id) <= getPlayer().getLevel();
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
		Window.openShop(Integer.parseInt(id, 16));
		
	}

	public boolean givePlayer(int id) {
		return getPlayer().addItem(shop.getItemByID(id));
	}
	
	public static void setNPC(NPC npc)
	{
		activeNPC = npc;
	}


}
