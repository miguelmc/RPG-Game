package game.ui;

import static org.lwjgl.opengl.GL11.*;

import game.Main;
import game.entities.NPC;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import game.util.Util;

public class MsgBoxManager {
	
	private static String message = "";
	private static boolean selection = true;
	private static boolean yesNo = false;
	private static boolean active = false;
	private static int stateYes;
	private static int stateNo;
	private static int state;
		
	public static boolean getSelection(){
		return selection;
	}
	
	public static void render(){
		if(active){
			int boxHeight = 140;
			int x1 = 20;
			int x2 = Main.DIM.getWidth() - 20;
			int y1 = Main.DIM.getHeight() - 25 - boxHeight;
			int y2 = Main.DIM.getHeight() - 25;
			
			GL11.glColor4f(0f, 0f, 0f,0.6f);
			glBegin(GL_QUADS);
				glVertex2f(x1, y1);
				glVertex2f(x2, y1);
				glVertex2f(x2, y2);
				glVertex2f(x1, y2);
			glEnd();
			
			GL11.glColor4f(1f, 1f, 1f,0.3f);
			glBegin(GL_QUADS);
				glVertex2f(x1+5, y1+5);
				glVertex2f(x2-5, y1+5);
				glVertex2f(x2-5, y2-5);
				glVertex2f(x1+5, y2-5);
			glEnd();
			
			Util.useFont("Monaco", Font.PLAIN, 25, Color.white);
			String lines[] = Util.tokenizeText(message, Main.DIM.getWidth()-50, 4); 
			for(int i=0;i<lines.length;i++){
				if(!lines[i].equals("")){
					Util.write(lines[i], x1+15, y1+10+Util.getFontHeight()*i);
				}
			}

			if(yesNo){
				x1 = (int)(Main.DIM.getWidth()*.7);
				x2 = (int)(Main.DIM.getWidth()*.93);
				y1 = y2 - 20;
				y2 = y2 + 25;
				GL11.glColor4f(0f, 0f, 0f,.7f);
				glBegin(GL_QUADS);
					glVertex2f(x1, y1);
					glVertex2f(x2, y1);
					glVertex2f(x2, y2);
					glVertex2f(x1, y2);
				glEnd();
				
				GL11.glColor4f(1f, 1f, 1f, .4f);
				glBegin(GL_QUADS);
					glVertex2f(x1+5, y1+5);
					glVertex2f(x2-5, y1+5);
					glVertex2f(x2-5, y2-5);
					glVertex2f(x1+5, y2-5);
				glEnd();
				
				
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				
				int avgx=(int)((x1+x2)/2);
				
				int translate = 0;
				if(!selection){
					translate = avgx-x1-4;
				}
				
				GL11.glColor4f(1f, 1f, 0f, .55f);
				glBegin(GL_QUADS);
					glVertex2f(x1+5+translate, y1+5);
					glVertex2f(avgx+translate, y1+5);
					glVertex2f(avgx+translate, y2-5);
					glVertex2f(x1+5+translate, y2-5);
				glEnd();
				
				Util.write("Yes", x1+10, y1+5);
				Util.write("No", x1+88, y1+5);
			}
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}
	}
	
	public static void sendText(String str, boolean YesNo){
		message = str;
		yesNo = YesNo;
		active = true;
	}
	
	public static boolean isActive(){
		return active;
	}

	public static void input() {
		if(Keyboard.getEventKeyState()){
			switch(Keyboard.getEventKey()){
				case Keyboard.KEY_ESCAPE:
					state = -1;
					stateYes = -1;
					stateNo = -1;
				case Keyboard.KEY_SPACE: 
				case Keyboard.KEY_RETURN:
					message = "";
					active = false;
					NPC.getNpc().run(yesNo ? (selection ? stateYes : stateNo) : state);
					selection = true;
					break;
				case Keyboard.KEY_RIGHT:
					selection = false;
					break;
				case Keyboard.KEY_LEFT:
					selection = true;
					break;
			}
		}
		
	}

	public static void setYesNo(int yes, int no) {
		stateYes = yes;
		stateNo = no;
	}
	
	public static void setState(int state2){
		state = state2;
	}

	public static void setActive(boolean Active) {
		active = Active;
	}
	
}
