package game.features;

import game.entities.item.Item;
import game.structure.GameObject;
import game.ui.UserInterface;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A task that can be assigned by a NPC.
 * Can give rewards upon completion.
 */
public class Quest extends GameObject
{
	// life cycle -> quest available > quest accepted (in progress) > quest completed > quest turned in
	
	// TODO change to have an state and fit the cycle (i.e.) state = AVAILABLE
	// TODO quest required level - create an static initialization to read all
	private String NAME, DESCRIPTION;
	private Map<Integer, List<Integer>> monsterKills = new HashMap<Integer, List<Integer>>(); 
	private boolean turnedIn = false;
	private Map<Integer, Integer> requiredItems = new HashMap<Integer, Integer>();
	private ArrayList<Item> itemReward = new ArrayList<Item>();
	private int requiredGold = 0, goldReward = 0, expReward = 0;
	
	private static Map<Integer, Integer> levelRequirement = new HashMap<Integer, Integer>();

	static
	{
		XMLParser parser = new XMLParser("quest/LevelRequirement.xml");
		
		List<Map<String, String>> reqLevels = parser.getChildrenAttributes("Requirement");
		for(Map<String, String> questReq: reqLevels)
		{
			levelRequirement.put(Integer.parseInt(questReq.get("id"), 16), Integer.parseInt(questReq.get("level")));
		}
	}
	
	public Quest(int id)
	{
		super(id);

		XMLParser parser = new XMLParser("quest/" + hexID() + ".xml");

		// quest info
		NAME = parser.getAttribute("Quest", "name");
		DESCRIPTION = parser.getAttribute("Quest", "description");

		// quest requirements
		List<Map<String, String>> reqMonsterKills = parser.getChildrenAttributes("Quest/requirements/monster_kills");
		for (Map<String, String> attributes : reqMonsterKills)
		{
			monsterKills.put(
					Integer.parseInt(attributes.get("id"), 16),
					Arrays.asList(Integer.parseInt(attributes.get("amount")),
							Integer.parseInt(attributes.get("amount"))));
		}

		List<Map<String, String>> reqItems = parser.getChildrenAttributes("Quest/requirements/items");
		for (Map<String, String> attributes : reqItems)
		{
			requiredItems.put(Integer.parseInt(attributes.get("id"), 16), Integer.parseInt(attributes.get("amount")));
		}
		
		System.out.println(requiredItems);

		requiredGold = Integer.parseInt(parser.getAttribute("Quest/requirements/gold", "amount"));
		
		//quest rewards
		expReward = Integer.parseInt(parser.getAttribute("Quest/rewards/exp", "amount"));
		goldReward = Integer.parseInt(parser.getAttribute("Quest/rewards/gold", "amount"));

		List<Map<String, String>> itemRewards = parser.getChildrenAttributes("Quest/rewards/items");
		for (Map<String, String> attributes : itemRewards)
		{
			requiredItems.put(Integer.parseInt(attributes.get("id"), 16), Integer.parseInt(attributes.get("amount")));
		}
	}

	public void monsterKill(int id)
	{
		if (!isTurnedIn())
		{
			int killsLeft = monsterKills.get(id).get(0);
			if (killsLeft > 0)
			{
				monsterKills.get(id).set(0, killsLeft - 1);
				int totalKills = monsterKills.get(id).get(1);
				UserInterface.sendNotification("Quest " + getName() + ": " + (totalKills - killsLeft + 1) + "/"
						+ totalKills);
			}
		}
	}

	public List<Integer> getMonstersToKill()
	{
		return new ArrayList<Integer>(monsterKills.keySet());
	}

	public int getKillsLeft(int id)
	{

		List<Integer> monster = monsterKills.get(id);

		if (monster != null)
			return monster.get(0);

		return -1;
	}

	public int getTotalKills(int id)
	{

		List<Integer> monster = monsterKills.get(id);

		if (monster != null)
			return monster.get(1);

		return -1;

	}

	public boolean isCompleted()
	{
		for (Map.Entry<Integer, Integer> item : requiredItems.entrySet())
		{
			if (!getMap().getPlayer().hasItem(item.getKey(), item.getValue()))
				return false;
		}

		if (getMap().getPlayer().getGold() < requiredGold || !killsComplete())
			return false;

		return true;
	}

	private boolean killsComplete()
	{

		for (List<Integer> kills : monsterKills.values())
		{
			if (kills.get(0) != 0)
			{
				return false;
			}
		}

		return true;

	}

	public String getName()
	{
		return NAME;
	}

	public String getDescription()
	{
		return DESCRIPTION;
	}

	public boolean isTurnedIn()
	{
		return turnedIn;
	}

	public void turnIn()
	{
		if (!isCompleted())
			return;

		for (Map.Entry<Integer, Integer> item : requiredItems.entrySet())
			getMap().getPlayer().loseItem(item.getKey(), item.getValue());

		getMap().getPlayer().gainGold(-requiredGold);
		getMap().getPlayer().gainGold(goldReward);
		getMap().getPlayer().gainExp(expReward);

		for (Item i : itemReward)
		{
			getMap().getPlayer().addItem(i);
		}

		turnedIn = true;
	}

	public static int getReqLevel(int id)
	{
		return levelRequirement.get(id);
	}

	public static void activate(int id)
	{
		if (getMap().getPlayer().getLevel() >= getReqLevel(id))
			getMap().getPlayer().addQuest(new Quest(id));
	}

	public String toString()
	{
		return getName();
	}

}
