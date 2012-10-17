package game.entities.superentities;

import game.entities.Entity;
import game.features.Skill;
import game.structure.Slot;
import game.util.Util;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public abstract class SuperEntity extends Entity {

	private int damage, facing = DOWN;
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	private ArrayList<Integer> damages = new ArrayList<Integer>();
	private ArrayList<Long> damageTime = new ArrayList<Long>();
	private Texture textures[] = new Texture[4];
	public static final int UP = 0,  RIGHT = 1, DOWN = 2, LEFT = 3;
	
	public SuperEntity(int id) {
		super(id);
		//TODO set superEntity to have 3 textures always
		if(this instanceof Player){
			textures[0] = Util.getTexture("player/" + hexID() + "/back.png");
			textures[1] = Util.getTexture("player/" + hexID() + "/side.png");
			textures[2] = Util.getTexture("player/" + hexID() + "/front.png");
			textures[3] = textures[1];
			setTexture(textures[DOWN]);
		}else if(this instanceof Monster){
			textures[0] = Util.getTexture("monster/" + hexID() + "/texture.png");
			textures[1] = Util.getTexture("monster/" + hexID() + "/texture.png");
			textures[2] = Util.getTexture("monster/" + hexID() + "/texture.png");
			textures[3] = Util.getTexture("monster/" + hexID() + "/texture.png");
		}
		
		face(2);
		
	}

	protected void face(int dir){
		setFacing(dir);
		setTexture(textures[dir]);
	}
	
	public void move(int dir){
		face(dir);
		
		if(canMove(dir)){		
			setPosition(Util.addRelPoints(position(), new Point(0, 1), dir));
			//TODO play move animation
		}
	}
	
	protected boolean canMove(int dir) {
		
		int xMove = 0;
		int yMove = 0;
		
		switch(dir){
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
		
		if(getMap().isPointInMap(targetPos) && getMap().getStrongEntitiesAt(targetPos).isEmpty())
			return true;
		
		return false;
	}

	public void update(){
		for(Skill s: skills){
			s.update();
		}
	}
	
	public void render(){
		Util.render(getTexture(), Util.pointArithmetic(-1, position(), getMap().getOffSet()), getRenderOffSet(), renderSize(), 0, getFacingDir()!=LEFT ? 0:1, 0);
	}
	
	public void midRender(){
		for(Skill s: skills){
			s.render();
		}
	}
	
	public void UIRender(){
		for(int i=0;i<damages.size();i++){
			if(damageTime.get(i) > System.currentTimeMillis()){
			float xpos = (Util.getTextWidth(damages.get(i).toString())-Slot.SIZE)/2;
			Util.write(damages.get(i).toString(), ((getX() - getMap().getOffSet().getX())*Slot.SIZE - xpos), (getY() - getMap().getOffSet().getY())*Slot.SIZE - 20 - Util.getFontHeight()*(damages.size()-1)+i*Util.getFontHeight());
			}else{
				damages.remove(i);
				damageTime.remove(i);
				i--;
			}
		}
	}
	
	protected void attack(int skill){
		getSkill(skill).attack();
	}
	
	public boolean hit(int damage) {
		setHP(getHP()-damage);
		if(getHP()<=0){
			setHP(0);
			die();
			return true;
		}
		damages.add(damage);
		damageTime.add(System.currentTimeMillis()+1700);
		return false;
	}
	
	protected void resetDamages(){
		damages.clear();
		damageTime.clear();
	}
	
	public void die(){
		for(Skill s: skills)
			s.stopAll();
		
		if(!(this instanceof Player)){ //TODO  handle player dead
			getMap().get(position()).removeStrongEntity();
		}
	}	
	
	public void addSkill(int ID){
		skills.add(new Skill(ID, this));
	}
	
	public Skill getSkill(int ID){
		for(Skill s: skills){
			if(s.id() == ID)
				return s;
		}
		return null;
	}

	public int getDamage() {
		double avgDmg = getAverageDamage();
		double deviation = avgDmg*.15;
		double damage = new Random(System.nanoTime()).nextGaussian()*deviation+avgDmg;
		return (int)(damage+.5);
	}
	
	protected double getAverageDamage(){
		return damage;
	}

	public void setDamage(int damage){
		this.damage = damage;
	}
	
	public int getFacingDir(){
		return facing;
	}
	
	public void setFacing(int dir){
		facing = dir;
	}
	
	public void stopAllActions() {
		for(Skill skill: skills){
			skill.stopAll();
		}
	}
	
	public abstract int getHP();
	public abstract void setHP(int hp);
}
