package game.entities;

import game.entities.item.EquipItem;
import game.entities.item.EtcItem;
import game.entities.item.UsableItem;
import game.entities.superentities.Monster;
import game.structure.GameObject;
import game.util.Util;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public abstract class Entity extends GameObject{
	
	private Dimension renderSize = new Dimension(1, 1);
	private Point renderOffSet = new Point();
	private boolean strong = false;
	private Point position;
	private Texture texture;
	private boolean invisible = false;
	
	public Entity(int id){
		super(id);
	}
	
	public static Entity createEntity(int id){
		
		EntityType type = EntityType.getType(id);
		
		switch(type){
		case Tile:
			return new Tile(id);
		case Portal:
			return new Portal(id);
		case EquipItem:
			return new EquipItem(id);
		case UseItem:
			return new UsableItem(id);
		case EtcItem:
			return new EtcItem(id);
		case Monster:
			return new Monster(id);
		case NPC:
			return new NPC(id);
		case Object:
			return new Object(id);
		}
		
		throw new IllegalArgumentException("The ID does not match an entity type.");
		
	}

	public void render(){
		if(!isInvisible())
			Util.render(getTexture(), Util.pointArithmetic(-1, position(), getMap().getOffSet()), getRenderOffSet(), renderSize());
	}
	
	public void midRender(){}
	public void UIRender(){}
	public void update(){}
	
	public void render(int x, int y){
		Util.render(texture, new Point(0, 0), new Point(x, y), renderSize);
	}
	
	public Point position(){
		return position;
	}
	
	public int getX(){
		return position().getX();
	}
	
	public int getY(){
		return position().getY();
	}
	
	public void setPosition(Point pos){
		if(position() != null){
			getMap().get(position()).remove(this);
		}
		getMap().get(pos).set(this);
		position = new Point(pos);
	}
	
	public void setX(int x){
		setPosition(x, getY());
	}
	
	public void setY(int y){
		setPosition(getX(), y);
	}

	public void setPosition(int x, int y){
		setPosition(new Point(x, y));
	}
	
	public Point getPositionInGrid(){
		return new Point(getX() - getMap().getOffSet().getX(), getY() - getMap().getOffSet().getY());
	}

	public static void initialize() {
		NPC.initialize();
	}
	
	protected void setStrong(){
		strong = true;
	}
	
	public boolean isStrong(){
		return strong;
	}
	
	public Dimension renderSize(){
		return renderSize;
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public void setTexture(Texture t){
		texture = t;
	}
	
	public Point getRenderOffSet(){
		return renderOffSet;
	}
	
	public void move(Point move){
		setPosition(getX() + move.getX(), getY() + move.getY());
	}
	
	public void modifyPos(Point pos) {
		position = pos;
	}

	public boolean isInvisible() {
		return invisible ;
	}
	
	public void setInvisible(boolean inv) {
		invisible = inv;
	}
}
