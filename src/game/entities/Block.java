package game.entities;

import game.util.Util;

import org.lwjgl.util.Point;

/**
 * Instance of an object which is invicible and is the building block of an object bigger than 1 tile
 */

public class Block extends Object {

	Object parent;
	
	public Block(Object parent) {
		super(Integer.parseInt("6700", 16));
		setStrong();
		setInvisible(true);
		this.parent = parent;
	}
	
	public void setPosition(Point p){
		
		if(position() == null){
			super.setPosition(p);
			return;
		}
		
		Point difference = Util.pointArithmetic(-1, p, position());
		super.setPosition(p);
		for(Block block: getBlocks()){
			block.setPosition(Util.pointArithmetic(1, block.position(), difference));
		}
	}
	
	public Object getParent(){
		return parent;
	}
	
}
