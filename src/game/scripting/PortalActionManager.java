package game.scripting;

import game.Main;
import game.entities.Portal;

import org.lwjgl.util.Point;

public class PortalActionManager extends AbstractScriptManager{
	public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	private Portal portal;
	
	public int playerFacingDir(){
		return Main.getMapManager().getCurrentMap().getPlayer().getFacingDir();
	}

	public void setPosition(int x, int y){
		Main.getMapManager().getCurrentMap().getPlayer().setPosition(x, y);
		Main.getMapManager().getCurrentMap().centerView();
	}
	
	public void setPosition(Point pos){
		setPosition(pos.getX(), pos.getY());
	}
	
	public Portal getPortal(){
		return portal;
	}
	
	public int getX(){
		return getPortal().getX();
	}
	
	public int getY(){
		return getPortal().getX();
	}
	
	public void setMap(int id){
		super.setMap(id, new Point(0, 0));
		setPosition(Main.getMapManager().getCurrentMap().getPortalByID(getID()).position());
	}
	
	public void setPortal(Portal portal){
		this.portal = portal;
	}
	
	public int getID(){
		return getPortal().id();
	}
	
}
