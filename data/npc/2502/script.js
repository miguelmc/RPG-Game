var quest = 2305;
switch(cm.getState()){
case 0:
	if(cm.isQuestInProgress(quest)){
		if(cm.isQuestComplete(quest)){
			cm.turnQuestIn(quest);
			cm.sendOk("Great!! Here's your reward.");
		}else{
			cm.sendOk("You are not done yet.");
		}
	}else{
		if(cm.isQuestTurnedIn(quest) || !cm.isQuestAvailable(quest)){
			cm.sendOk("Hi. My name is Mathew.");
		}else{
			cm.sendYesNo("There's rats everywhere. Could you please kill 5 rats and come back. I heard they come from the sewer next to the store.", 1, 2);
		}
	}
	break;
case 1:
	cm.sendOk("Thanks. Come back when you are finished.");
	cm.activateQuest(quest);
	break;
case 2:
	cm.sendOk("Come back later.");
	break;
}