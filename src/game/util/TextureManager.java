package game.util;

import game.entities.EntityType;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

public class TextureManager
{

	private java.util.Map<Integer, Texture> textures = new HashMap<Integer, Texture>();

	/**
	 * 
	 * <br>
	 * <b>add</b> <br>
	 * <p>
	 * <tt>public void add(int id)</tt>
	 * </p>
	 * Adds a texture to the TextureManager given an id.
	 */
	public void add(int id)
	{

		String filePath = EntityType.getType(id).toString();
		filePath += "/" + Util.hexID(id) + "/texture.png";

		if (get(id) != null)
			return;

		textures.put(id, Util.getTexture(filePath));
	}

	/**
	 * 
	 * <br>
	 * <b>get</b> <br>
	 * <p>
	 * <tt>public Texture get(int id)</tt>
	 * </p>
	 * Retrieves and returns a texture from the TextureManager given an id. <br>
	 * <br>
	 */
	public Texture get(int id)
	{
		return textures.get(id);
	}

	/**
	 * 
	 * <br>
	 * <b>recycle</b> <br>
	 * <p>
	 * <tt>public void recycle(int id, Texture texture)</tt>
	 * </p>
	 * Adds a texture to the TextureManager given the texture. <br>
	 * <br>
	 */
	public void recycle(int id, Texture texture)
	{
		textures.put(id, texture);
	}

	public String toString()
	{
		return textures.toString();
	}

}
