package game.entities.superentities;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import game.entities.Entity;
import game.entities.item.Item;
import game.features.Quest;
import game.structure.Slot;
import game.util.Util;
import game.util.XMLParser;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.util.Point;

public class Monster extends SuperEntity {

	// override setPosition so that it checks if spawnPoint is null and initializes it
	
	private int exp, movePeriod, moveTimer = 0;
	private String name;
	private boolean angry = false, dead = false, respawn;
	//TODO chance item and chance to a map
	private Map<Integer, Integer> dropList = new HashMap<Integer, Integer>();
	private long nextAtk = 0L;

	public Monster(int id){
		this(id, true);
	}
	
	public Monster(int id, boolean respawn){
		super(id);
		
		setStrong();
		addSkill(1792);
		this.respawn = respawn;
		
		parseMonster();
		
		setHP(getMaxHP());
		
		movePeriod = new Random(System.nanoTime()).nextInt(140) + 160;

	}
	
	private void parseMonster() {
		XMLParser parser = new XMLParser("monster/" + hexID() +"/data.xml");
		
		name = parser.getAttribute("Monster", "name");
		setDamage(Integer.parseInt(parser.getAttribute("Monster", "damage")));
		setMaxHP(Integer.parseInt(parser.getAttribute("Monster", "maxHP")));
		exp = Integer.parseInt(parser.getAttribute("Monster", "exp"));
		
		List<java.util.Map<String, String>> drops = parser.getChildrenAttributes("Map/drops");
		for(java.util.Map<String, String> data: drops){
			dropList.put(Integer.parseInt(data.get("id"), 16), Integer.parseInt(data.get("chance")));
		}
		
	}

	public void UIRender(){
		if(dead)
			return;
		
		float cHP = (float)getHP()/(float)getMaxHP();
		
		int width = Slot.SIZE;
		int height = Slot.SIZE;
		
		//HP BAR
		glColor3f(1f, 0f, 0f);
		glLoadIdentity();
		glTranslatef((getX() - getMap().getOffSet().getX())*Slot.SIZE, (getY() - getMap().getOffSet().getY())*Slot.SIZE, 0);
		glBegin(GL_QUADS);
			glVertex2f((float)(width*.13), (float)(height*.07));
			glVertex2f((float)(width*.13) + (float)(width*.74)*cHP, (float)(height*.07));
			glVertex2f((float)(width*.13) + (float)(width*.74)*cHP, (float)(height*.2));
			glVertex2f((float)(width*.13), (float)(height*.2));
		glEnd();
		glLoadIdentity();
		
		//HP BAR BORDER
		glColor3f(0f, 0f, 0f);
		glLoadIdentity();
		glTranslatef((getX() - getMap().getOffSet().getX())*Slot.SIZE, (getY() - getMap().getOffSet().getY())*Slot.SIZE, 0);
		glLineWidth(1);
		glBegin(GL_LINES);
			glVertex2f((float)(width*.13), (float)(height*.07));
			glVertex2f((float)(width*.87), (float)(height*.07));
			glVertex2f((float)(width*.87), (float)(height*.07));
			glVertex2f((float)(width*.87), (float)(height*.2));
			glVertex2f((float)(width*.87), (float)(height*.2));
			glVertex2f((float)(width*.13), (float)(height*.2));
			glVertex2f((float)(width*.13), (float)(height*.2));
			glVertex2f((float)(width*.13), (float)(height*.07));
		glEnd();
		glLoadIdentity();
		
		glColor3f(1f, 1f, 1f);
		
		Util.useFont("Arial", Font.BOLD, 10, Color.white);
		float xTraslation = width/2-(Util.getTextWidth(getName()))/2;
		Util.write(getName(), ((getX() - getMap().getOffSet().getX())*Slot.SIZE + xTraslation), (getY() - getMap().getOffSet().getY())*Slot.SIZE - (float)(Slot.SIZE*.3));
		
		super.UIRender();
		
	}

	public void render() {
		if (dead)
			return;

		if (angry) {
			glColor3f(1f, 0f, 0f);
			super.render();
			glColor3f(1f, 1f, 1f);
		} else {
			super.render();
		}

	}
	
	public void update(){
		
		//AutoMove
		if (moveTimer == movePeriod){
			Random r = new Random(System.nanoTime());
			moveTimer = 0;
			if(angry){
				movePeriod = r.nextInt(40)+40;
				int num1 = 0, num2 = 2;
				if(!(getMap().getPlayer().getX() == getX() || getMap().getPlayer().getY() == getY())){
					if(getMap().getPlayer().getX() > getX() && getMap().getPlayer().getY() < getY()){
						num1 = UP;
						num2 = RIGHT;
					}else if(getMap().getPlayer().getX() > getX() && getMap().getPlayer().getY() > getY()){
						num1 = RIGHT;
						num2 = DOWN;
					}else if(getMap().getPlayer().getX() < getX() && getMap().getPlayer().getY() > getY()){
						num1 = DOWN;
						num2 = LEFT;
					}else if(getMap().getPlayer().getX() < getX() && getMap().getPlayer().getY() < getY()){
						num1 = LEFT;
						num2 = UP;
					}
					ArrayList<Integer> nums = new ArrayList<Integer>(2);
					nums.add(num1); nums.add(num2);
					moveRandom(nums);
				}else{
					if(getMap().getPlayer().getX() == getX()){
						if(getMap().getPlayer().getY() > getY()){	
							move(DOWN);
						}else{
							move(UP);
						}
					}else{
						if(getMap().getPlayer().getX() > getX()){	
							move(RIGHT);
						}else{
							move(LEFT);
						}
					}
				}
			}else{
				movePeriod = r.nextInt(140)+160;
				ArrayList<Integer> nums = new ArrayList<Integer>(4);
				nums.add(0); nums.add(1); nums.add(2); nums.add(3);
				moveRandom(nums);
			}
		}
		moveTimer++;
		
		//AutoAttack
		if(angry){
			if(nextAtk < System.currentTimeMillis()){
				Player p = getMap().getPlayer();
				boolean attack = false;
				switch(getFacingDir()){
				case UP:
					if(p.getX() == getX() && p.getY() < getY())
						attack = true;
					break;
				case RIGHT:
					if(p.getX() > getX() && p.getY() == getY())
						attack = true;
					break;
				case DOWN:
					if(p.getX() == getX() && p.getY() > getY())
						attack = true;
					break;
				case LEFT:
					if(p.getX() < getX() && p.getY() == getY())
						attack = true;
					break;
				}
				if(attack){
					getSkill(1792).attack();
					nextAtk = System.currentTimeMillis() + 2000;
				}
			}
		}
		
		super.update();
		
	}

	private void moveRandom(ArrayList<Integer> nums) {
		
		if(nums.size() == 0)
			return;
		
		Point oldPos = position();
				
		int randNum = new Random(System.nanoTime()).nextInt(nums.size());
		int dir = nums.get(randNum);
		move(dir);
		
		if(oldPos.equals(position())){
			nums.remove(randNum);
			moveRandom(nums);
		}
	}
	
	public boolean hit(int damage){
		angry = true;
		moveTimer = movePeriod;
		
		return super.hit(damage);
	}
	
	public void die(){
		
		dead = true;
		
		for(Integer id: dropList.keySet()){
			int num = new Random(System.nanoTime()).nextInt(100)+1;
			if(num<=dropList.get(id)){
				Item item = (Item) Entity.createEntity(id);
				item.setPosition(position());
			}
		}
		
		getMap().getPlayer().gainExp(getExp());
		
		for(Quest q: getMap().getPlayer().getActiveQuests()){
			q.monsterKill(id());
		}
		
		super.die();

	}
	
	public boolean isDead(){
		return dead;
	}

	public int getDamage() {
		return super.getDamage();
	}
	
	public int getExp() {
		return exp;
	}
	
	public boolean respawns(){
		return respawn;
	}
	
	public String getName(){
		return name;
	}

	public List<Integer> getDropsID() {
		return new ArrayList<Integer>(dropList.keySet());
	}
	
}
