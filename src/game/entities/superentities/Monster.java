package game.entities.superentities;

import static org.lwjgl.opengl.GL11.glColor3f;
import game.entities.Entity;
import game.entities.item.Item;
import game.features.Quest;
import game.structure.Slot;
import game.util.Animation;
import game.util.Renderer;
import game.util.SoundManager;
import game.util.Writer;
import game.util.Writer.Fonts;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

/**
 * A SuperEntity which attacks the player and moves freely in the map.
 * It drops items and gives exp to the player upon dead.
 */
public class Monster extends SuperEntity
{

	// TODO? auto update on its own thread?

	private static final int MIN_MOVE_TIME = 160, MAX_MOVE_TIME = 300;
	
	private int exp, movePeriod, moveTimer = 0, hp, maxHP, minGold, maxGold;
	private String name;
	private boolean angry = false, dead = false, respawn;
	private Map<Integer, Integer> dropList  = new HashMap<Integer, Integer>();
	private long nextAtk = 0L;
	
	public Monster(int id)
	{
		this(id, true);
	}

	public Monster(int id, boolean respawn)
	{
		super(id);

		if(id == 0x2402)
		{
			setRenderSize(2, 2);
			setOffset(-Slot.SIZE/2, -Slot.SIZE/2);
		}
		
		this.respawn = respawn;
		
		XMLParser parser = new XMLParser(path() + "data.xml");
		
		name = parser.getAttribute("Monster", "name");
		setDamage(Integer.parseInt(parser.getAttribute("Monster", "damage")));
		setMaxHP(Integer.parseInt(parser.getAttribute("Monster", "maxHP")));
		exp = Integer.parseInt(parser.getAttribute("Monster", "exp"));
		minGold = Integer.parseInt(parser.getAttribute("Monster", "minGold"));
		maxGold = Integer.parseInt(parser.getAttribute("Monster", "maxGold"));
		
		List<java.util.Map<String, String>> drops = parser.getChildrenAttributes("Monster/drops");
		for (java.util.Map<String, String> data : drops)
			dropList.put(Integer.parseInt(data.get("id"), 16), Integer.parseInt(data.get("chance")));
		
		List<java.util.Map<String, String>> skills = parser.getChildrenAttributes("Monster/skills");
		for (java.util.Map<String, String> skill : skills)
			addSkill(Integer.parseInt(skill.get("id"), 16));
		
		setHP(getMaxHP());
		calculateMovePeriod();
		
		moveAnimation = new Animation("monster/" + hexID() + "/animations/move.xml", texture);
	}

	public void UIRender()
	{
		if (dead) return;
		
		Point position;
		if(moveAnimation.rendering())
		{
			Point renderPos = getPositionInGrid();
			Point prevRenderPos = new Point(animationPosition.getX() - getMap().getOffSet().getX(), animationPosition.getY() - getMap().getOffSet().getY());
			position = new Point((int) (Slot.SIZE*(prevRenderPos.getX() + (renderPos.getX() - prevRenderPos.getX()) * ((float)moveAnimation.currentFrame()/(moveAnimation.totalFrames()+1))) + getOffset().getX()),
					   (int) (Slot.SIZE*(prevRenderPos.getY() + (renderPos.getY() - prevRenderPos.getY()) * ((float)moveAnimation.currentFrame()/(moveAnimation.totalFrames()+1)))) + getOffset().getY());
		}else
			position = new Point(getPositionInGrid().getX() * Slot.SIZE + getOffset().getX(), getPositionInGrid().getY() * Slot.SIZE + getOffset().getY());
		

		float hp = (float)getHP() / (float)getMaxHP(); // current hp percentage

		// HP BAR
		glColor3f(1f, 0f, 0f); // Red
		Renderer.renderQuad(new Point((int) (position.getX() + Slot.SIZE*.13),
									  (int) (position.getY() + Slot.SIZE*.07)),
						    new Dimension((int) (Slot.SIZE * .74 * hp),
						    			  (int) (Slot.SIZE * .13f)));

		// HP BAR BORDER
		glColor3f(0f, 0f, 0f);
		
		Renderer.renderLines(1,
							 new Point((int) (position.getX() + Slot.SIZE * .13),
									   (int) (position.getY() + Slot.SIZE *.07)), 
							 new Point[]{new Point(0, 0),
										 new Point((int) (Slot.SIZE * .74), 0),
										 new Point((int) (Slot.SIZE * .74), (int) (Slot.SIZE * .13)),
										 new Point(0, (int) (Slot.SIZE * .13))});
		
		glColor3f(1f, 1f, 1f);

		Writer.useFont(Fonts.Arial_White_Bold_10);
		Writer.write(getName(), 
				     new Point((int)((position.getX() + Slot.SIZE*.5)),
				    		 position.getY() - Writer.fontHeight()), 
					 Writer.CENTER);

		super.UIRender();
	}

	public void update()
	{

		//TODO script monster behavior
		
		// AutoMove
		if (moveTimer == movePeriod)
		{
			Random r = new Random(System.nanoTime());
			moveTimer = 0;
			if (angry)
			{
				movePeriod = r.nextInt(40) + 40; // move faster when angry
				int num1 = 0, num2 = 2;
				// moves based on the player position relative to its position
				// TODO rewrite using Util.addRelPoints
				if (!(getMap().getPlayer().getX() == getX() || getMap().getPlayer().getY() == getY()))
				{
					if (getMap().getPlayer().getX() > getX() && getMap().getPlayer().getY() < getY())
					{
						num1 = UP;
						num2 = RIGHT;
					} else if (getMap().getPlayer().getX() > getX() && getMap().getPlayer().getY() > getY())
					{
						num1 = RIGHT;
						num2 = DOWN;
					} else if (getMap().getPlayer().getX() < getX() && getMap().getPlayer().getY() > getY())
					{
						num1 = DOWN;
						num2 = LEFT;
					} else if (getMap().getPlayer().getX() < getX() && getMap().getPlayer().getY() < getY())
					{
						num1 = LEFT;
						num2 = UP;
					}
					ArrayList<Integer> nums = new ArrayList<Integer>(2);
					nums.add(num1);
					nums.add(num2);
					moveRandom(nums);
				} else
				{
					if (getMap().getPlayer().getX() == getX())
					{
						if (getMap().getPlayer().getY() > getY())
						{
							move(DOWN);
						} else
						{
							move(UP);
						}
					} else
					{
						if (getMap().getPlayer().getX() > getX())
						{
							move(RIGHT);
						} else
						{
							move(LEFT);
						}
					}
				}
			} else
			{
				movePeriod = r.nextInt(140) + 160;
				List<Integer> nums = new ArrayList<Integer>();
				nums.add(UP);
				nums.add(RIGHT);
				nums.add(DOWN);
				nums.add(LEFT);
				moveRandom(nums);
			}
		}
		moveTimer++;

		// AutoAttack
		if (angry)
		{
			if (nextAtk < System.currentTimeMillis())
			{
				Player p = getMap().getPlayer();
				boolean attack = false;
				switch (getFacingDir())
				{
				case UP:
					if (p.getX() == getX() && p.getY() < getY())
						attack = true;
					break;
				case RIGHT:
					if (p.getX() > getX() && p.getY() == getY())
						attack = true;
					break;
				case DOWN:
					if (p.getX() == getX() && p.getY() > getY())
						attack = true;
					break;
				case LEFT:
					if (p.getX() < getX() && p.getY() == getY())
						attack = true;
					break;
				}
				if (attack)
				{
					getSkill(0x0701).attack();
					nextAtk = System.currentTimeMillis() + 2000;
				}
			}
		}

		super.update();
	}
	
	public void attack(int skillID)
	{
		getSkill(skillID).attack();
	}

	public void moveRandom(List<Integer> nums)
	{
		if (nums.size() == 0)
			return;

		Point oldPos = position();

		int randNum = new Random(System.nanoTime()).nextInt(nums.size());
		int dir = nums.get(randNum);
		move(dir);

		if (oldPos.equals(position()))
		{
			nums.remove(randNum);
			moveRandom(nums);
		}
	}

	public boolean hit(int damage)
	{
		SoundManager.playSound("Hit_2");
		angry = true;
		moveTimer = movePeriod;

		return super.hit(damage);
	}

	public void die()
	{
		dead = true;

		// drop items
		Random random = new Random(System.nanoTime());
		for (Integer id : dropList.keySet())
		{
			int num = random.nextInt(100) + 1; //random num between 1-100 inclusive
			if (num <= dropList.get(id))
			{
				Item item = (Item) Entity.createEntity(id);
				getMap().add(item, position());
			}
		}

		getMap().getPlayer().gainExp(getExp());
		getMap().getPlayer().gainGold(getGold());

		for (Quest q : getMap().getPlayer().getActiveQuests())
			q.monsterKill(id());

		super.die();
	}

	public boolean isDead()
	{
		return dead;
	}

	public int getDamage()
	{
		return super.getDamage();
	}

	public int getExp()
	{
		return exp;
	}

	public boolean respawns()
	{
		return respawn;
	}

	public String getName()
	{
		return name;
	}

	public List<Integer> getDropsID()
	{
		return new ArrayList<Integer>(dropList.keySet());
	}

	public int getHP()
	{
		return hp;
	}

	public void setHP(int hp)
	{
		this.hp = hp;
		if (this.hp > maxHP)
			this.hp = maxHP;
	}

	public int getMaxHP()
	{
		return maxHP;
	}

	public void setMaxHP(int maxHP)
	{
		this.maxHP = maxHP;
	}

	public int getGold(){
		return new Random().nextInt(maxGold-minGold) + minGold;
	}
	
	private void calculateMovePeriod() {
		movePeriod = new Random(System.nanoTime()).nextInt(MAX_MOVE_TIME - MIN_MOVE_TIME) + MIN_MOVE_TIME;
	}

	public boolean isAngry() {
		return angry;
	}

	public void setAngry(boolean angry) {
		this.angry = angry;
	}
	
}
