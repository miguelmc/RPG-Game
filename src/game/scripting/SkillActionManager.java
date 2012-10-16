package game.scripting;

import game.entities.superentities.Monster;
import game.entities.superentities.Player;
import game.features.SkillAttack;
import game.structure.Map;
import game.structure.MapManager;
import game.structure.Slot;
import game.util.Util;

import org.lwjgl.util.Point;

public class SkillActionManager extends AbstractScriptManager{
	
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
				Map map = MapManager.getMap();
				Monster monster = map.getMonsterAt(pos);
				if(monster != null){
					monster.hit((int)(getPlayer().getDamage()*dmg + .5f));
				}
			}else if(activeAttack.getSkill().getAttacker() instanceof Monster){
				if(getPlayer().position().equals(pos))
					getPlayer().hit((int)(activeAttack.getSkill().getAttacker().getDamage()*dmg + .5f));
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
			for(Slot s: MapManager.getMap().getAllSlots()){
				if(s.get(Slot.MONSTER) != null)
					if(s.get(Slot.MONSTER).position().equals(pos))
						return true;
			}
		}
		if(activeAttack.getSkill().getAttacker() instanceof Monster)
			return getPlayer().position().equals(Util.addRelPoints(origin, p, facingDir));
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
		return !MapManager.getMap().getStrongEntitiesAt(p).isEmpty();
	}
}
