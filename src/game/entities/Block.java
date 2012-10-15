package game.entities;

import game.util.Util;

import org.lwjgl.util.Point;

public class Block extends Object {

	public Block() {
		super(Integer.parseInt("6700", 16));
		setStrong();
		setInvisible(true);
	}
	
	public void render(){
		
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
	
}
