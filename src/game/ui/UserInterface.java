package game.ui;

import static org.lwjgl.opengl.GL11.*;

import game.Main;
import game.entities.superentities.Player;
import game.features.Stat;
import game.ui.window.Window;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import game.util.Util;

public class UserInterface {

	private static ArrayList<String> notifications = new ArrayList<String>();
	private static ArrayList<Long> times = new ArrayList<Long>();
	private static boolean loaded = false;
	
	public static void render(){
		
		Util.useFont("Arial", Font.BOLD, 14, Color.black);
		for(int i=notifications.size()-1; i>=0; i--){
			if(times.get(i) > System.currentTimeMillis()){
				Util.write(notifications.get(i), 10, 80 + i*Util.getFontHeight());
			}else{
				notifications.remove(i);
				times.remove(i);
				i--;
			}
		}		
		
		Window.renderAll();
		
		//HP BAR
		int width = 128;
		int limit = (int)(width*Main.getMapManager().getCurrentMap().getPlayer().getHP()/Main.getMapManager().getCurrentMap().getPlayer().getStat(Player.TOTAL+Stat.MAXHP.ID));
		glColor4f(1f, 0f, 0f, .6f);
		glBegin(GL_QUADS);
			glVertex2f(10, 10);
			glVertex2f(10+limit, 10);
			glVertex2f(10+limit, 32);
			glVertex2f(10, 32);
		glEnd();
		glColor4f(.7f, .7f, .7f, .6f);
		glBegin(GL_QUADS);
			glVertex2f(10+limit, 10);
			glVertex2f(10+width, 10);
			glVertex2f(10+width, 32);
			glVertex2f(10+limit, 32);
		glEnd();
		glColor4f(0f, 0f, 0f, 1f);
		
		//MP BAR
		limit = (int)(width*Main.getMapManager().getCurrentMap().getPlayer().getMP()/Main.getMapManager().getCurrentMap().getPlayer().getStat(Player.TOTAL+Stat.MAXMP.ID));
		glColor4f(0f, 0f, 1f, .6f);
		glBegin(GL_QUADS);
			glVertex2f(200, 10);
			glVertex2f(200+limit, 10);
			glVertex2f(200+limit, 32);
			glVertex2f(200, 32);
		glEnd();
		glColor4f(.7f, .7f, .7f, .6f);
		glBegin(GL_QUADS);
			glVertex2f(200+limit, 10);
			glVertex2f(200+width, 10);
			glVertex2f(200+width, 32);
			glVertex2f(200+limit, 32);
		glEnd();
		glColor4f(0f, 0f, 0f, 1f);
		
		//EXP BAR
		limit = (int)(width*Main.getMapManager().getCurrentMap().getPlayer().getExp()/Main.getMapManager().getCurrentMap().getPlayer().getExpReq());
		glColor4f(1f, .5f, 0f, .6f);
		glBegin(GL_QUADS);
			glVertex2f(390, 10);
			glVertex2f(390+limit, 10);
			glVertex2f(390+limit, 32);
			glVertex2f(390, 32);
		glEnd();
		glColor4f(.7f, .7f, .7f, .6f);
		glBegin(GL_QUADS);
			glVertex2f(390+limit, 10);
			glVertex2f(390+width, 10);
			glVertex2f(390+width, 32);
			glVertex2f(390+limit, 32);
		glEnd();
		glColor4f(0f, 0f, 0f, 1f);
		
		Util.useFont("Courier New",	Font.BOLD, 28, Color.white);
		Util.write(Main.getMapManager().getCurrentMap().getName(), 10, 40);
		Util.write(Integer.toString(Main.getMapManager().getCurrentMap().getPlayer().getLevel()), (float)(Main.DIM.getWidth() - 30), 5f);
				
		if(MsgBoxManager.isActive())
			MsgBoxManager.render();
		
		if(!isLoaded())
			setLoaded(true);
		
	}
	
	public static void sendNotification(String s){
		notifications.add(s);
		times.add(System.currentTimeMillis() + 2000L);
	}

	public static boolean isLoaded(){
		return loaded;
	}
	
	public static void setLoaded(boolean b){
		loaded = b;
	}

	public static void init() {
		Window.init();
				
		//load fonts
		System.out.println("Loading 0%");
		Util.useFont("Courier New", Font.BOLD, 14, Color.white);
		System.out.println("Loading 20%");
		Util.useFont("Monaco", Font.PLAIN, 25, Color.white);
		System.out.println("Loading 40%");
		Util.useFont("Arial", Font.BOLD, 10, Color.white);
		System.out.println("Loading 60%");
		Util.useFont("Arial", Font.BOLD, 14, Color.black);
		System.out.println("Loading 80%");
		Util.useFont("Courier New", Font.BOLD, 28, Color.white);
		System.out.println("Loading 100%");
		
	}
	
}
