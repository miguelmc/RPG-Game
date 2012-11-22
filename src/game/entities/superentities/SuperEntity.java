package game.entities.superentities;

import game.entities.Entity;
import game.features.Skill;
import game.structure.Slot;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Animation;
import game.util.Util;
import game.util.Writer;
import game.util.Writer.Fonts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

/**
 * Abstract type for entities that can move, face a direction and attack
 */
public abstract class SuperEntity extends Entity
{

	private int damage, facing = DOWN;
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	private ArrayList<Integer> damages = new ArrayList<Integer>(); // TODO change to queue
	private ArrayList<Long> damageTime = new ArrayList<Long>();
	
	private Texture texture;
	private List<Animation> moveAnimations = new ArrayList<Animation>();
	protected Animation moveAnimation;
	private int textureOffset = 0;
	protected Point animationPosition = new Point();
	
	public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

	public SuperEntity(int id)
	{
		super(id);
		setStrong();
		
		texture = Util.getTexture("player/2600/texture.png");
		moveAnimations.add(new Animation("player/2600/animations/front.xml", texture));
		moveAnimations.add(new Animation("player/2600/animations/side.xml", texture));
		moveAnimations.add(new Animation("player/2600/animations/back.xml", texture));
		
		face(DOWN);
	}
	
	protected void face(int dir)
	{
		if(moveAnimation != null && moveAnimation.rendering())
			return;
		
		facing = dir;
		switch(dir)
		{
		case UP:
			textureOffset = 2;
			moveAnimation = moveAnimations.get(2);
			break;
		case RIGHT:
		case LEFT:
			textureOffset = 1;
			moveAnimation = moveAnimations.get(1);
			break;
		case DOWN:
			textureOffset = 0;
			moveAnimation = moveAnimations.get(0);
		}
	}

	public void move(int dir)
	{
		face(dir);

		if (canMove(dir))
			moveTo(Util.addRelPoints(position(), new Point(0, 1), dir));
	}

	protected boolean canMove(int dir)
	{
		
		if (moveAnimation.rendering())
			return false;

		// check the slot were its gonna move so that it has no other strong
		// entity
		int xMove = 0;
		int yMove = 0;

		switch (dir)
		{
		case UP:
			yMove--;
			break;
		case RIGHT:
			xMove++;
			break;
		case DOWN:
			yMove++;
			break;
		case LEFT:
			xMove--;
		}

		Point targetPos = new Point(getX() + xMove, getY() + yMove);

		if (getMap().isPointInMap(targetPos) && getMap().get(targetPos).getStrongEntity() == null &&
				getMap().get(targetPos).getTile().id() != 0x2102)
			return true;

		return false;
	}

	public void update()
	{
		for (Skill s : skills)
			s.update();
	}

	public void render()
	{
		if (isInvisible())
			return;
		
		Point renderPos = getPositionInGrid();
		
		if(!moveAnimation.rendering())
		{
				Renderer.render(new Builder(
						texture,
						new Point(renderPos.getX()*Slot.SIZE, renderPos.getY()*Slot.SIZE),
						new Dimension(Slot.SIZE, Slot.SIZE))
						.flipX(getFacingDir() == LEFT)
						.imageSize(moveAnimation.getImageSize().getHeight(), moveAnimation.getImageSize().getWidth())
						.textureOffset(new Point(0, textureOffset*moveAnimation.getImageSize().getHeight()))
						.renderOffset(getOffset()));
		}else
		{
			Point prevRenderPos = new Point(animationPosition.getX() - getMap().getOffSet().getX(), animationPosition.getY() - getMap().getOffSet().getY());
			Point position = new Point((int) (Slot.SIZE*(prevRenderPos.getX() + (renderPos.getX() - prevRenderPos.getX()) * ((float)moveAnimation.currentFrame()/(moveAnimation.totalFrames()+1)))),
									   (int) (Slot.SIZE*(prevRenderPos.getY() + (renderPos.getY() - prevRenderPos.getY()) * ((float)moveAnimation.currentFrame()/(moveAnimation.totalFrames()+1)))));
			moveAnimation.render(new Point(position.getX() + getOffset().getX(), position.getY() + getOffset().getY()), new Dimension(Slot.SIZE, Slot.SIZE), getFacingDir() == LEFT);
		}
	}
	
	public void midRender()
	{
		for (Skill s : skills)
			s.render();
	}

	public void UIRender() // superentities can be attacked, so their damage is displayed above them at an UI level
	{
		Point position;
		if(moveAnimation.rendering())
		{
			Point renderPos = getPositionInGrid();
			Point prevRenderPos = new Point(animationPosition.getX() - getMap().getOffSet().getX(), animationPosition.getY() - getMap().getOffSet().getY());
			position = new Point((int) (Slot.SIZE*(prevRenderPos.getX() + (renderPos.getX() - prevRenderPos.getX()) * ((float)moveAnimation.currentFrame()/(moveAnimation.totalFrames()+1))) + getOffset().getX()),
					   (int) (Slot.SIZE*(prevRenderPos.getY() + (renderPos.getY() - prevRenderPos.getY()) * ((float)moveAnimation.currentFrame()/(moveAnimation.totalFrames()+1)))) + getOffset().getY());
		}else
			position = new Point(getPositionInGrid().getX() * Slot.SIZE, getPositionInGrid().getY() * Slot.SIZE);
		
		for (int i = 0; i < damages.size(); i++)
		{
			if (damageTime.get(i) > System.currentTimeMillis())
			{
				Writer.useFont(Fonts.Arial_White_Bold_10);
				Writer.write(damages.get(i).toString(),
							 new Point((int) ((position.getX() + Slot.SIZE*.5)),
									 position.getY() - 20 - Writer.fontHeight() * (damages.size() - 1) + i * Writer.fontHeight()),
							 Writer.CENTER);
			} else
			{
				damages.remove(i);
				damageTime.remove(i);
				i--;
			}
		}
	}
	
	public void moveTo(Point pos)
	{
		animationPosition.setLocation(position() != null ? position() : new Point(0 ,0));
		super.moveTo(pos);
		moveAnimation.play();
	}

	protected void attack(Skill skill)
	{
		skill.attack();
	}

	public boolean hit(int damage)
	{ // get hit
		setHP(getHP() - damage);
		damages.add(damage); // to render the damage
		damageTime.add(System.currentTimeMillis() + 1700); // TODO? use thread?
		if (getHP() <= 0)
		{
			setHP(0);
			die();
			return true;
		}
		
		return false;
	}

	protected void resetDamages()
	{
		damages.clear();
		damageTime.clear();
	}

	public void die()
	{
		for (Skill s : skills)
			s.stopAll();

		if (!(this instanceof Player))
		{
			getMap().get(position()).removeStrongEntity();
		}
	}

	public void addSkill(int ID)
	{
		skills.add(new Skill(ID, this));
	}

	public Skill getSkill(int ID)
	{
		for (Skill s : skills)
			if (s.id() == ID)
				return s;
		return null;
	}

	public int getDamage()
	{ 
		double avgDmg = getAverageDamage();
		double deviation = avgDmg * .15;
		double damage = new Random(System.nanoTime()).nextGaussian() * deviation + avgDmg;
		return (int) Math.round(damage);
	}

	protected double getAverageDamage()
	{
		return damage;
	}

	protected void setDamage(int damage)
	{
		this.damage = damage;
	}

	public int getFacingDir()
	{
		return facing;
	}

	public void stopAllActions()
	{
		for (Skill skill : skills)
		{
			skill.stopAll();
		}
	}

	public abstract int getHP();

	public abstract void setHP(int hp);
	
	public List<Skill> getSkills()
	{
		return skills;
	}
}
