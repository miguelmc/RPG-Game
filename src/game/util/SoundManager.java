package game.util;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundManager {

	private static Map<String, Sound> sounds = new HashMap<String, Sound>();
	private static Map<String, Music> music = new HashMap<String, Music>();
	
	public static void initSounds()
	{
		addSound("Level_Up", "data/sound_effects/Level_Up.wav");
		addSound("Hit_1", "data/sound_effects/Hit_1.wav");
		addSound("Hit_2", "data/sound_effects/Hit_2.wav");
		addSound("Hit_3", "data/sound_effects/Hit_3.wav");
		addSound("Pick_Up", "data/sound_effects/Pick_Up.wav");
	}
	
	public static void addSound(String name, String path)
	{
		if(sounds.get(name) == null)
		{
			try 
			{
				sounds.put(name, new Sound(path));
			} catch (SlickException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void addMusic(String name, String path)
	{
		if(music.get(name) == null)
		{
			try 
			{
				music.put(name, new Music(path));
			} catch (SlickException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void playSound(String name)
	{
		sounds.get(name).play();
	}
	
	public static void loopMusic(String name)
	{
		music.get(name).loop();
	}
	
}
