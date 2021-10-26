package com.author;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class Particle {
	public int x, y;
	public int mx, my;
	public int v = 5, vm = -5;
	public boolean dead;
	private Random random;
	private Color color;
	
	public int power = 1;
	public int powerOp = 255 - power;
	
	private Particle sx;
	private Particle sy;
	
	public Teams.team team;
	
	public Particle() {
		random = new Random();
		this.x = random.nextInt(Main.frame.getWidth());
		this.y = random.nextInt(Main.frame.getHeight());
		
		mx = random.nextInt(v - vm) + vm;
		my = random.nextInt(v - vm) + vm;
		power = random.nextInt(5);
		if(power <= 0) power = 1;
		
		int roll = random.nextInt(4);
		if(roll == 0) team = com.author.Teams.team.BLUE;
		else if(roll == 1) team = com.author.Teams.team.RED;
		else if(roll == 2) team = com.author.Teams.team.WHITE;
		else if(roll == 3) team = com.author.Teams.team.GREEN;
		
		if(team.equals(com.author.Teams.team.BLUE)) {
			color = new Color(0, 0, random.nextInt(255 - 200) + 200);
		} else if(team.equals(com.author.Teams.team.GREEN)) {
			color = new Color(0, random.nextInt(255 - 200) + 200, 0);
		} else if(team.equals(Teams.team.RED)) {
			color = new Color(random.nextInt(255 - 200) + 200, 0, 0);
		} else if(team.equals(Teams.team.WHITE)) {
			color = new Color(random.nextInt(255 - 200) + 200, random.nextInt(255 - 200) + 200, random.nextInt(255 - 200) + 200);
		}
	}
	
	public Particle(int x, int y) {
		this.x = x;
		this.y = y;
		random = new Random();
		
		mx = random.nextInt(v - vm) + vm;
		my = random.nextInt(v - vm) + vm;
		power = random.nextInt(5);
		if(power <= 0) power = 1;
		
		int roll = random.nextInt(4);
		if(roll == 0) team = com.author.Teams.team.BLUE;
		else if(roll == 1) team = com.author.Teams.team.RED;
		else if(roll == 2) team = com.author.Teams.team.WHITE;
		else if(roll == 3) team = com.author.Teams.team.GREEN;
		
		if(team.equals(com.author.Teams.team.BLUE)) {
			color = new Color(0, 0, random.nextInt(255 - 200) + 200);
		} else if(team.equals(com.author.Teams.team.GREEN)) {
			color = new Color(0, random.nextInt(255 - 200) + 200, 0);
		} else if(team.equals(Teams.team.RED)) {
			color = new Color(random.nextInt(255 - 200) + 200, 0, 0);
		} else if(team.equals(Teams.team.WHITE)) {
			color = new Color(random.nextInt(255 - 200) + 200, random.nextInt(255 - 200) + 200, random.nextInt(255 - 200) + 200);
		}
	}
	
	public void tick() {
		x += mx;
		y += my;
		
		for(Particle p : Main.particle) {
			if(this.hitbox().intersects(p.hitbox()) && p != this && !p.dead && power < 20 && !p.team.equals(team)) {
				if(power > p.power) {
					power += 1;
					p.power -= power;
				} else {
					mx *= -1;
					my *= -1;
				}
			}
			
			if(p != this && !p.dead) {
				if(p.x == x) sx = p;
				if(p.y == y) sy = p;
			}
		}
		
		if(power > 10 && power < 20) {
			int i = random.nextInt(5);
			if(i == 0) Main.particle.add(new Particle());
		} else if(power >= 20) {
			for(int i = 0; i < power/2; i++) Main.particle.add(new Particle());
			dead = true;
		}
		
		if(x < 0 || x > Main.frame.getWidth() - 70) {
			mx *= -1;
		} else if(y < 0 || y > Main.frame.getHeight() - 49 && !dead) {
			my *= -1;
		}
		
		if(dead) y--;
		
		powerOp = 255 - power;
		if(power > 255) powerOp = 255;
		else if(power < 0) powerOp = 0;
		if(power <= 0) dead = true;
		
		if(y < Main.frame.getHeight() + 10 && dead) Main.particle.remove(this);
	}
	
	public void render(Graphics2D g) {
		if(!dead) g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), powerOp));
		else g.setColor(Color.DARK_GRAY);
		
		g.fillRoundRect(x, y, (int)hitbox().width, (int)hitbox().height, 50, 50);
		g.drawPolygon(new int[] {x, x + random.nextInt(5 - -5) + 5, x + random.nextInt(15 - -15) + 15, x + random.nextInt(50 - -50) + 50}, 
				new int[] {y, y + random.nextInt(5 - -5) + 5, y + random.nextInt(15 - -15) + 15, y + random.nextInt(50 - -50) + 50}, 
				3);
		g.setColor(new Color(255,255,255,50));
		if(sx != null && sy != null) g.drawLine(x, y, sx.x, sy.y);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 0, 10));
		g.drawString("TEAM:" + team, x, y);
		g.drawString("XY:" + x + "," + y, x, y - 10);
	}
	
	public Rectangle hitbox() {
		return new Rectangle(x, y, 8 * power, 8 * power);
	}
}
