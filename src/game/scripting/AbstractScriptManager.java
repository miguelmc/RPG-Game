package game.scripting;

import game.entities.superentities.Player;
import game.features.Stat;
import game.structure.MapManager;

import org.lwjgl.util.Point;

/**
 * The abstract type of the object passed to the scripts.
 * Contains basic functions that are usually used in the script.
 */
public abstract class AbstractScriptManager {
	
	public void setHP(int hp){
		getPlayer().setHP(hp);
	}
	
	public int getHP(){
		return getPlayer().getHP();
	}
	
	public int getMaxHP(){
		return getPlayer().getStat(Player.TOTAL+Stat.MAXHP.ID);
	}
	
	public void setMP(int mp){
		getPlayer().setMP(mp);
	}
	
	public int getMP(){
		return getPlayer().getMP();
	}
	
	public int getMaxMP(){
		return getPlayer().getStat(Player.TOTAL+Stat.MAXMP.ID);
	}
	
	public void setMap(int id, Point p){
		MapManager.setMap(id, p);
	}
	
	public int getMapID(){
		return MapManager.getMap().id();
	}
	
	public String getMapName(){
		return MapManager.getMap().getName();
	}
	
	public void gainGold(int amount){
		getPlayer().gainGold(amount);
	}
	
	protected Player getPlayer(){
		return MapManager.getMap().getPlayer();
	}
	
}
