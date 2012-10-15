package game.scripting;

import game.Main;
import game.entities.superentities.Monster;
import game.entities.superentities.Player;
import game.features.SkillAttack;
import game.structure.Map;
import game.structure.Slot;
import game.util.Util;

import org.lwjgl.util.Point;

public class SkillActionManager {
	
	private SkillAttack activeAttack;
	private Point origin;
	private int facingDir;
	
	public SkillActionManager(SkillAttack sa, int facing){
		activeAttack = sa;
		facingDir = facing;
		setOrigin(activeAttack.getSkill().getAttacker().position());
	}
	
	public void hit(Point location[], float dmg){
		for(Point p: location){
			Point pos = Util.addRelPoints(origin, p, facingDir);
			if(activeAttack.getSkill().getAttacker() instanceof Player){
				Map map = Main.getMapManager().getCurrentMap();
				Monster monster = map.getMonsterAt(pos);
				if(monster != null){
					monster.hit((int)(Main.getMapManager().getCurrentMap().getPlayer().getDamage()*dmg + .5f));
				}
			}else if(activeAttack.getSkill().getAttacker() instanceof Monster){
				Player pl = Main.getMapManager().getCurrentMap().getPlayer();
				if(pl.position().equals(pos))
					pl.hit((int)(activeAttack.getSkill().getAttacker().getDamage()*dmg + .5f));
			}
			
		}
	}
	
	public void stop(){
		activeAttack.stop();
	}

	public void setOrigin(Point position) {
		origin = position;	
	}
	
	public boolean hasMonsterAt(Point p){
		if(activeAttack.getSkill().getAttacker() instanceof Player){
			Point pos = Util.addRelPoints(origin, p, facingDir);
			for(Slot s: Main.getMapManager().getCurrentMap().getAllSlots()){
				if(s.get(Slot.MONSTER) != null)
					if(s.get(Slot.MONSTER).position().equals(pos))
						return true;
			}
		}
		if(activeAttack.getSkill().getAttacker() instanceof Monster)
			return Main.getMapManager().getCurrentMap().getPlayer().position().equals(Util.addRelPoints(origin, p, facingDir));
		return false;
	}
	
	public String[] getVariables(int i){
		return activeAttack.getSkill().getVariables(i);
	}
	
	public int getLevel(){
		return activeAttack.getSkill().getLevel();
	}
	
	public void play(Point p){
		activeAttack.play(Util.addRelPoints(origin, p, facingDir));
	}
	
	public boolean hasObjectAt(Point p){
		return !Main.getMapManager().getCurrentMap().getStrongEntitiesAt(p).isEmpty();
	}
}
