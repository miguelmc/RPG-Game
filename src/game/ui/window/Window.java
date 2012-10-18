package game.ui.window;

import static org.lwjgl.opengl.GL11.*;

import game.Main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

import game.util.Util;

public abstract class Window {

	protected Point position = new Point(100, 100);
	protected Dimension size = new Dimension(174, 256);
	protected String file;
	protected Texture texture;
	protected boolean active = false;
	protected boolean pressed = false;
	protected int key;
	
	private static Window[] windows = {new Inventory(Keyboard.KEY_I)};
	
	public Window(Point pos, Dimension s){
		position = pos;
		size = s;
	}
	
	public static void init(){	
		for(Window w: windows){
			w.setTexture(w.getClass().getSimpleName().toLowerCase());
		}
	}
	
	public void setPosition(int x, int y){
		position.setLocation(x, y);
	}
	
	public Point getPosition(){
		return position;
	}
	
	public static void renderAll(){
		for(Window w: windows){
		if(w.isActive())
			w.render();
		}
	}
	
	public void render(){
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glColor4f(1,1,1,.6f);
		glLoadIdentity();
		glTranslatef(getPosition().getX(), getPosition().getY(), 0);
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(174f/256f, 0);
			glVertex2f(size.getWidth(), 0);
			glTexCoord2f(174f/256f, 1);
			glVertex2f(size.getWidth(), size.getHeight());
			glTexCoord2f(0, 1);
			glVertex2f(0, size.getHeight());
		glEnd();
		glLoadIdentity();
		glColor4f(1,1,1,1);
		glDisable(GL_TEXTURE_2D);
	}
	
	public void setTexture(String str){
		texture = Util.getTexture("UI/window/"+str+".png");
	}
	
	public boolean isActive(){
		return active;
	}
	
	public static void input(){
		if(Keyboard.getEventKeyState()){
			for(Window w: windows){
				if(Keyboard.getEventKey() == w.getKey()){
					w.toggleActive();
					if(w.getX()>Main.DIM.getWidth() || w.getX()+w.getWidth() < 0 ||
							w.getY() > Main.DIM.getHeight() || w.getY() + w.getHeight() < 0)
						w.setPosition(100, 100);
				}else if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
					w.close();
				}
			}	
		}
	}
	
	protected void close(){
		active = false;
	}
	
	public void toggleActive(){
		active = !active;
	}

	public static void mouseInput() {
		for(Window w: windows){
			if(Mouse.getX() >= w.getX() && Mouse.getX() <= w.getX() + w.getWidth() &&
					Main.DIM.getHeight() - Mouse.getY() + 1 >= w.getY() && Main.DIM.getHeight() - Mouse.getY() + 1 <=
					w.getY() + w.getHeight()){
				if(w.isActive())
					w.mouse();				
			}
		}
	}
	
	protected void mouse(){
		
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public int getX() {
		return position.getX();
	}

	public  void setX(int x) {
		position.setX(x);
	}

	public int getY() {
		return position.getY();
	}

	public void setY(int y) {
		position.setY(y);
	}

	public int getWidth() {
		return size.getWidth();
	}

	public void setWidth(int width) {
		size.setWidth(width);
	}

	public int getHeight() {
		return size.getHeight();
	}

	public void setHeight(int height) {
		size.setHeight(height);
	}
	
	public int getKey(){
		return key;
	}

}