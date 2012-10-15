package game.util;

import game.entities.EntityType;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

public class TextureManager {

	private java.util.Map<Integer, Texture> mapTextures = new HashMap<Integer, Texture>();
	
	public void add(int id){
				
		String filePath = EntityType.getType(id).toString();
		filePath += "/" + Util.hexID(id) + "/texture.png";
		
		add(id, filePath);		
	}

	public Texture get(int id){
		return mapTextures.get(id);
	}
	
	public void add(int id, String path){
		
		if(get(id) != null)
			return;
		
		mapTextures.put(id, Util.getTexture(path));
		
	}
	
	public void recycle(int id, Texture texture){
		mapTextures.put(id, texture);
	}
	
}
