package game.scripting;

import game.Main;
import game.features.Quest;
import game.ui.MsgBoxManager;

public class NPCConversationManager extends AbstractScriptManager {
	
	private int state = 0;
	
	public void sendOk(String s, int state){
		MsgBoxManager.sendText(s, false);
		MsgBoxManager.setState(state);
	}
	
	public void sendOk(String s){
		sendOk(s, -1);
	}
	
	public void sendYesNo(String s, int stateYes, int stateNo){
		MsgBoxManager.sendText(s, true);
		MsgBoxManager.setYesNo(stateYes,stateNo);
	}

	public int getState() {
		return state;
	}

	public void setState(int state2) {
		state = state2;
	}
	
	public void activateQuest(int id){
		Quest.activate(id);
	}
	
	public boolean isQuestAvailable(int id){
		if(Quest.getReqLevel(id) <= Main.getMapManager().getCurrentMap().getPlayer().getLevel()){
			return true;
		}
		return false;
	}
	
	public boolean isQuestComplete(int id){
		if(Main.getMapManager().getCurrentMap().getPlayer().hasQuest(id)){
			return Main.getMapManager().getCurrentMap().getPlayer().getQuest(id).isCompleted() && !Main.getMapManager().getCurrentMap().getPlayer().getQuest(id).isTurnedIn();
		}
		return false;
	}
	
	public boolean isQuestInProgress(int id){
		if(Main.getMapManager().getCurrentMap().getPlayer().hasQuest(id)){
			return !Main.getMapManager().getCurrentMap().getPlayer().getQuest(id).isTurnedIn();
		}
		return false;
	}
	
	public boolean isQuestTurnedIn(int id){
		if(Main.getMapManager().getCurrentMap().getPlayer().hasQuest(id))
			return Main.getMapManager().getCurrentMap().getPlayer().getQuest(id).isTurnedIn();
		return false;
	}
	
	public void turnQuestIn(int id){
		if(Main.getMapManager().getCurrentMap().getPlayer().hasQuest(id) && Main.getMapManager().getCurrentMap().getPlayer().getQuest(id).isCompleted()){
			Main.getMapManager().getCurrentMap().getPlayer().getQuest(id).turnIn();
		}
	}
	
}
