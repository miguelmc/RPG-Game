package game.scripting;

import org.lwjgl.util.Point;

import game.Main;
import game.entities.superentities.Player;
import game.features.Stat;

public abstract class AbstractScriptManager {

	public void setHP(int hp){
		Main.getMapManager().getCurrentMap().getPlayer().setHP(hp);
	}
	
	public int getHP(){
		return Main.getMapManager().getCurrentMap().getPlayer().getHP();
	}
	
	public int getMaxHP(){
		return Main.getMapManager().getCurrentMap().getPlayer().getStat(Player.TOTAL+Stat.MAXHP.ID);
	}
	
	public void setMP(int mp){
		Main.getMapManager().getCurrentMap().getPlayer().setMP(mp);
	}
	
	public int getMP(){
		return Main.getMapManager().getCurrentMap().getPlayer().getMP();
	}
	
	public int getMaxMP(){
		return Main.getMapManager().getCurrentMap().getPlayer().getStat(Player.TOTAL+Stat.MAXMP.ID);
	}
	
	public void setMap(int id, Point p){
		Main.getMapManager().setMap(id, p);
	}
	
	public int getMapID(){
		return Main.getMapManager().getCurrentMap().id();
	}
	
	public String getMapName(){
		return Main.getMapManager().getCurrentMap().getName();
	}
	
	public void gainGold(int amount){
		Main.getMapManager().getCurrentMap().getPlayer().gainGold(amount);
	}
	
}
