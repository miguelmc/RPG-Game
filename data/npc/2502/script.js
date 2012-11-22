var quest = 2305;
if(cm.isQuestTurnedIn(2305))
	quest = 2306;
switch(cm.getState()){
case 0:
	if(cm.isQuestInProgress(quest)){
		if(cm.isQuestComplete(quest)){
			cm.turnQuestIn(quest);
			if(quest == 2305)
				cm.sendOk("Great!! Here's your reward.");
			else
				cm.sendOk("Incredible. You killed the big rat. There will be no more rat trouble!! Here, take this as reward.");
		}else{
			cm.sendOk("Is that rat really that tough?");
		}
	}else{
		if(cm.isQuestTurnedIn(quest) || !cm.isQuestAvailable(quest)){
			cm.sendOk("Hi. My name is Mathew.");
		}else{
			if(quest == 2305)
				cm.sendYesNo("There's rats everywhere. Could you please kill 5 rats and come back. I heard they come from the sewer next to the store.", 1, 2);
			else
				cm.sendYesNo("Great job killing the rats. But there's still many rats. I heard the master rat is hiding somewhere deep in the sewers. Kill it and you will be rewarded..", 1, 2);
		}
	}
	break;
case 1:
	if(quest == 2305)
		cm.sendOk("Thanks. Come back when you are finished.");
	else
		cm.sendOk("As expected from a great warrior.");
	cm.activateQuest(quest);
	break;
case 2:
	if(quest == 2305)
		cm.sendOk("Come back later.");
	else
		cm.sendOk("Are you afraid of the master rat?");
	break;
}