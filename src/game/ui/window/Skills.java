package game.ui.window;

import static org.lwjgl.opengl.GL11.glColor3f;
import game.features.Skill;
import game.structure.MapManager;
import game.util.MouseManager;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Util;
import game.util.Writer;
import game.util.Writer.Fonts;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Skills extends Window{
	
	private static final int PADDING = 15;
	private static final Dimension SIZE = new Dimension(174, 256), SQUARE_SIZE = new Dimension(SIZE.getWidth() - PADDING*2, 40),
								   THUMBNAIL_SIZE = new Dimension(30, 30);
	private static List<SkillSquare> squares = new ArrayList<SkillSquare>();
	
	public Skills ()
	{
		super(new Point(350, 450), SIZE);
		for(Skill skill: MapManager.getMap().getPlayer().getSkills())
			squares.add(new SkillSquare(skill));
	}
	
	public void render() 
	{	
		super.render();
		
		for(int y=0; y<squares.size(); y++)
			squares.get(y).render(y);
	}
	
	public void mouse()
	{	
		if(MouseManager.mousePressed())
		{
			int y=0;
			for(SkillSquare square: squares)
				if(square.click(y++))
					MapManager.getMap().getPlayer().setActiveSkill(square.skill);
		}
		
		super.mouse();
	}
	
	class SkillSquare{
		
		private Skill skill;
		private boolean selected = false;
		
		public SkillSquare(Skill skill)
		{
			this.skill = skill;
		}
		
		public void render(int y)
		{
			if(selected)
				glColor3f(0f, 0, .6f);
			else
				glColor3f(.8f, .1f, .1f);
			
			Renderer.renderQuad(new Point(getX() + PADDING, getY() + PADDING + (SQUARE_SIZE.getHeight() + 5)*y),
								SQUARE_SIZE);
			
			glColor3f(1, 1, 1);

			Renderer.render(new Builder(skill.getThumbnail(),
							new Point(getX() + PADDING + 5, getY() + PADDING + (SQUARE_SIZE.getHeight() + 5)*y + 5),
							THUMBNAIL_SIZE));

			Writer.useFont(Fonts.Arial_White_Bold_10);

			List<String> skillInfo = new ArrayList<String>();
			skillInfo.add(skill.getName());
			skillInfo.add(Integer.toString(skill.getLevel()));
						
			Writer.write(skillInfo, 
						 new Point(getX() + PADDING + 5 + THUMBNAIL_SIZE.getWidth() + 5,
								   getY() + PADDING + (SQUARE_SIZE.getHeight() + 5)*y + 5),
						 140);
		}
	
		public boolean click(int y)
		{
			selected = Util.inRange(MouseManager.getPosition(), new Point(getX() + PADDING, getY() + PADDING + (SQUARE_SIZE.getHeight() + 5)*y), SQUARE_SIZE);
			return selected;
		}
		
	}
	

}
