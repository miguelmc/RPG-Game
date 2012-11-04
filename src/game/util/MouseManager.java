package game.util;

import game.Main;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

public class MouseManager{

	public static final int LEFT = 0, RIGHT = 1;
	
	boolean lastEventisReleaseEvent = false;
	
	public boolean mouseReleased() { return !isButtonDown(0) && !mouseMoved(); }
	
	public boolean mousePressed() { return isButtonDown(0) && !mouseMoved(); }
	
	public boolean mouseMoved() { return getDX() != 0 || getDY() != 0; }
	
	public boolean isButtonDown(int button) { return Mouse.isButtonDown(button); }
	
	public boolean hasEvent() { return Mouse.next(); }
	
	public int getDX(){	return Mouse.getDX(); }
	
	public int getDY(){ return -Mouse.getDY(); }
	
	public int getX() { return Mouse.getX(); }
	
	public int getY() { return Main.DIM.getHeight() - Mouse.getY() - 1; }
	
	public Point getPosition() { return new Point(Mouse.getX(), Mouse.getY()); }
	
}
