package game.entities.superentities;

import static game.features.Stat.ATK;
import static game.features.Stat.DEF;
import static game.features.Stat.MAXHP;
import static game.features.Stat.MAXMP;
import static game.features.Stat.STR;
import game.Main;
import game.entities.NPC;
import game.entities.Portal;
import game.entities.item.EquipItem;
import game.entities.item.EquipItem.EquipType;
import game.entities.item.Item;
import game.entities.item.UsableItem;
import game.features.Quest;
import game.features.Skill;
import game.structure.Map;
import game.structure.MapManager;
import game.structure.Slot;
import game.ui.MsgBoxManager;
import game.ui.UserInterface;
import game.ui.window.WindowManager;
import game.util.Timer;
import game.util.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;

/**
 * The main Entity of the game, which is controlled by the user and perform most
 * of the actions of the game.
 */
public class Player extends SuperEntity {

	public static final int INV_LIMIT = 30, MAX_LEVEL = 8, BASE = 0x10, EXTRA = 0x20, TOTAL = 0x30;
	public static final int HELMET = 0, TOPWEAR = 1, BOTTOMWEAR = 2, SHOES = 3, WEAPON = 4;
	private static final int REGEN = 3;
	
	private int level = 1, exp = 0, gold = 0, mp;
	private volatile int hp;
	private volatile java.util.Map<Integer, Integer> stats = new HashMap<Integer, Integer>();
	private java.util.Map<EquipType, EquipItem> equips = new HashMap<EquipType, EquipItem>();
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Quest> quests = new ArrayList<Quest>();
	private transient long nextAtk = 0, nextMove = 0;

	public Player(Point pos) {
		super(Integer.parseInt("2600", 16));
		addSkill(0x0701);

		// TODO player file with base stats, stats per level, damage formula
		// parameters, etc.

		stats.put(BASE + MAXHP.ID, 100);
		stats.put(BASE + MAXMP.ID, 100);
		stats.put(BASE + ATK.ID, 1);
		stats.put(BASE + STR.ID, 10);
		stats.put(BASE + DEF.ID, 5);

		stats.put(EXTRA + MAXHP.ID, 0);
		stats.put(EXTRA + MAXMP.ID, 0);
		stats.put(EXTRA + ATK.ID, 0);
		stats.put(EXTRA + STR.ID, 0);
		stats.put(EXTRA + DEF.ID, 0);

		setHP(getStat(TOTAL + MAXHP.ID));
		setMP(getStat(TOTAL + MAXMP.ID));

		Timer timer = new Timer(this, "regen", 10000);
		timer.start();
	}

	public void regen() {
		setHP(getHP() + REGEN);
		setMP(getMP() + REGEN);
	}

	public void input() {

		if (Keyboard.getEventKeyState()) {
			switch (Keyboard.getEventKey()) {
			case Keyboard.KEY_M:
				for (Slot s : getMap().getAllSlots()) {
					Monster monster = s.getMonster();
					if (monster != null)
						monster.die();
				}
				break;
			case Keyboard.KEY_SPACE:
				action(getMap().get(Util.addRelPoints(position(), new Point(0, 1), getFacingDir())));
				break;
			case Keyboard.KEY_Z:
				attack(getSkill(0x0701));
				break;
			case Keyboard.KEY_X:
				Portal portal = getMap().get(position()).getPortal();
				if (portal != null) {
					portal.run();
				}
				break;
			}
		}
	}

	public void move(int key) {

		boolean moveCamera = false;
		int dir = 0;

		switch (key) {
		case Keyboard.KEY_UP:
			dir = UP;
			moveCamera = getPositionInGrid().getY() - 1 < Map.VIEW_LIMIT
					&& getMap().isPointInMap(
							new Point(0, -1 + getMap().getOffSet().getY()));
			break;
		case Keyboard.KEY_RIGHT:
			dir = RIGHT;
			moveCamera = getPositionInGrid().getX() + 1 >= Main.GRIDSIZE
					.getWidth() - Map.VIEW_LIMIT
					&& getMap().isPointInMap(
							new Point(Main.GRIDSIZE.getWidth()
									+ getMap().getOffSet().getX(), 0));
			break;
		case Keyboard.KEY_DOWN:
			dir = DOWN;
			moveCamera = getPositionInGrid().getY() + 1 >= Main.GRIDSIZE
					.getHeight() - Map.VIEW_LIMIT
					&& getMap().isPointInMap(
							new Point(0, Main.GRIDSIZE.getHeight()
									+ getMap().getOffSet().getY()));
			break;
		case Keyboard.KEY_LEFT:
			dir = LEFT;
			moveCamera = getPositionInGrid().getX() - 1 < Map.VIEW_LIMIT
					&& getMap().isPointInMap(
							new Point(getMap().getOffSet().getX() - 1, 0));
			break;
		default:
			return;
		}

		if (System.currentTimeMillis() < nextMove)
			return;

		Point oldPos = position();

		face(dir);

		if (!canMove(dir))
			return;

		moveTo(Util.addRelPoints(position(), new Point(0, 1), dir));
		nextMove = System.currentTimeMillis() + 150;

		if (moveCamera)
			getMap().moveView(getX() - oldPos.getX(), getY() - oldPos.getY());

		List<Item> items = getMap().get(position()).getItems();

		if (!items.isEmpty())
			for (ListIterator<Item> li = items.listIterator(); li.hasNext();)
				if (gainItem(li.next()))
					li.remove();

	}

	private void action(Slot slot) {
		
		if (slot == null)
			return;
		
		NPC npc = slot.getNPC();
		List<Item> items = slot.getItems();
		
		if (npc != null) {
			npc.run();
		} else if (!items.isEmpty()) {
			for (ListIterator<Item> i = items.listIterator(); i.hasNext();)
				if (gainItem(i.next()))
					i.remove();
		}
	}

	protected void attack(Skill skill) {
		
		if (System.currentTimeMillis() < nextAtk)
			return;
		
		if (getMP() < 2) //TODO check for the skill mp
		{
			UserInterface.sendNotification("You don't have enough MP to use this skill.");
			return;
		}
		super.attack(skill);
		delayAttack(skill.getDelay());
		useMP(2);
	}

	public double getAverageDamage() {
		return getStat(TOTAL + ATK.ID) * 2 + getStat(TOTAL + STR.ID);
	}

	public void delayAttack(int mili) {
		nextAtk = System.currentTimeMillis() + mili;
	}

	public void die() {
		UserInterface.sendNotification("You died.");
		super.die();
		MapManager.setMap(0, new Point(4, 5));
		setHP(getStat(TOTAL+MAXHP.ID));
		setMP(getStat(TOTAL+MAXHP.ID));
		gainExp(-getExp());
	}
	
	public void update() {

		super.update();

		if (MsgBoxManager.isActive() || WindowManager.isShopOpen())
			return;

		// handles movement
		int moveKeys[] = { Keyboard.KEY_UP, Keyboard.KEY_RIGHT,
				Keyboard.KEY_DOWN, Keyboard.KEY_LEFT };

		int keysDown = 0;
		int keyDown = 0;
		for (int key : moveKeys) {
			if (Keyboard.isKeyDown(key)) {
				keyDown = key;
				keysDown++;
			}
		}

		if (keysDown == 1) {
			if (System.currentTimeMillis() > nextMove) {
				move(keyDown);
				nextMove = System.currentTimeMillis() + 200;
			}
		}

	}

	public boolean hit(int damage) {
		damage -= getStat(TOTAL+DEF.ID)/10;
		
		if(damage < 0) damage = 0;
		
		return super.hit(damage);
	}

	public void useItem(UsableItem item) {
		
		assert items.contains(item);
		
		item.use();
		item.setQuantity(item.getQuantity() - 1);
		if (item.getQuantity() == 0)
			removeItem(item);
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	public Item getItem(int id) {
		for (Item i : items) {
			if (i.id() == id)
				return i;
		}
		return null;
	}

	public boolean gainItem(Item i) {

		Item item = getItem(i.id());
		if (item != null && !(item instanceof EquipItem)) {
			item.add(i.getQuantity());
			if (i.getQuantity() > 0)
				UserInterface.sendNotification("You got an item: " + i.getName() + " x" + i.getQuantity() + ".");
			return true;
		}
		if (items.size() < INV_LIMIT) {
			items.add(i);
			if (i.getQuantity() > 0)
				UserInterface.sendNotification("You got an item: "
						+ i.getName() + " x" + i.getQuantity() + ".");
			return true;
		}
		return false;
	}

	public void loseItem(int id, int amount) {
		if (hasItem(id, amount)) {
			getItem(id).setQuantity(getItem(id).getQuantity() - amount);
			if (amount > 0)
				UserInterface.sendNotification("You lost an item: "
						+ getItem(id).getName() + " x" + amount + ".");
			if (getItem(id).getQuantity() <= 0) {
				items.remove(getItem(id));
			}
		}
	}

	public boolean hasItem(int ID, int quantity) {
		if (getItem(ID) != null) {
			if (getItem(ID).getQuantity() >= quantity)
				return true;
		}
		return false;
	}

	public void gainExp(int amount) 
	{
		int expToLevel = getReqExp() - exp;
		if (amount >= expToLevel) 
		{
			exp = amount - expToLevel;
			levelUp();
		} else if (exp + amount < 0) 
			exp = 0;
		else
			exp += amount;
		
		if (amount > 0)
			UserInterface.sendNotification("You gained " + amount + " EXP.");
	}

	private void levelUp() 
	{
		raiseStat(BASE + ATK.ID, 1);
		raiseStat(BASE + STR.ID, 2);
		raiseStat(BASE + DEF.ID, 1);
		level++;
		UserInterface.sendImpNotification("LEVEL UP!!!");
		stats.put(BASE + MAXHP.ID, getStat(BASE + MAXHP.ID) + 5);
		stats.put(BASE + MAXMP.ID, getStat(BASE + MAXMP.ID) + 5);
		setHP(getStat(TOTAL + MAXHP.ID));
		setMP(getStat(TOTAL + MAXHP.ID));
	}

	public void useMP(int amount) 
	{
		setMP(getMP() - amount);
	}

	public void gainGold(int amount) 
	{
		gold += amount;
		if (amount > 0)
			UserInterface.sendNotification("You gained " + amount + " gold.");
	}

	public int getGold() 
	{
		return gold;
	}

	public int getExp() 
	{
		return exp;
	}

	public int getReqExp() 
	{
		return level * 10;
	}

	public int getLevel() 
	{
		return level;
	}

	public int getItemCount() 
	{
		return items.size();
	}

	public List<Item> getItems() 
	{
		return items;
	}

	public void activateQuest(Quest quest) 
	{
		quests.add(quest);
		UserInterface.sendNotification("Quest accepted: " + quest.getName() + ".");
	}

	public Quest getQuest(int id)
	{
		for (Quest q : quests) 
			if (q.id() == id)
				return q;
		return null;
	}

	public boolean hasQuest(int id) {
		return getQuest(id) != null;
	}

	public ArrayList<Quest> getActiveQuests() {
		return quests;
	}
	
	public int getStat(int stat) 
	{
		if (stat >= 0x30)
			return getStat(stat - EXTRA) + getStat(stat - BASE);
		return stats.get(stat);
	}

	public void raiseStat(int stat, int amount) 
	{
		if(stats.containsKey(stat))
			stats.put(stat, getStat(stat) + amount);
		
		setHP(getHP()); //avoid hp>maxhp
		setMP(getMP());
	}

	public void addEquip(EquipItem equip) 
	{
		if (equips.get(equip.getType()) != null)
			removeEquip(equip.getType());
		
		items.remove(items.indexOf(equip));
		
		equips.put(equip.getType(), equip);
		raiseStat(EXTRA + MAXHP.ID, equip.getStat(MAXHP));
		raiseStat(EXTRA + MAXMP.ID, equip.getStat(MAXMP));
		raiseStat(EXTRA + ATK.ID, equip.getStat(ATK));
		raiseStat(EXTRA + DEF.ID, equip.getStat(DEF));
		raiseStat(EXTRA + STR.ID, equip.getStat(STR));
	}

	public void removeEquip(EquipType type) 
	{

		if (equips.get(type) == null)
			return;

		EquipItem equip = equips.get(type);

		if (!gainItem(equip))
			return;

		raiseStat(EXTRA + MAXHP.ID, -equip.getStat(MAXHP));
		raiseStat(EXTRA + MAXMP.ID, -equip.getStat(MAXMP));
		raiseStat(EXTRA + ATK.ID, -equip.getStat(ATK));
		raiseStat(EXTRA + STR.ID, -equip.getStat(STR));
		
		equips.put(type, null);
	}

	public java.util.Map<EquipType, EquipItem> getEquips() 
	{
		return equips;
	}

	
	public EquipItem getEquip(int type) 
	{
		return equips.get(type); 
	} 

	public void setHP(int hp) 
	{
		this.hp = hp;
		if (getHP() > getStat(TOTAL + MAXHP.ID))
			setHP(getStat(TOTAL + MAXHP.ID));
		else if(getHP() < 0)
			setHP(0);
	}

	public int getHP() 
	{
		return hp;
	}

	public void setMP(int mp) 
	{
		this.mp = mp;
		if (getMP() > getStat(TOTAL + MAXMP.ID))
			setMP(getStat(TOTAL + MAXMP.ID));
		else if(getMP() < 0)
			setMP(0);
	}

	public int getMP() 
	{
		return mp;
	}
	
	public void save() throws IOException
	{
		FileOutputStream fileStream = new FileOutputStream("save.data");

		ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

		objectStream.write(position().getX());
		objectStream.write(position().getY());
		objectStream.writeObject(this);
	}

}
