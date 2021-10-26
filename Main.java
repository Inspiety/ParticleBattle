package com.author;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Main extends Canvas{
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private boolean running;
	private int FPS, TPS;
	
	public static List<Particle> particle;
	
	private Main() {
		this.running = true;
		particle = new ArrayList<Particle>();
	}
	
	private void start() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double tps = 20.0;
		double ns = 1e+9/tps;
		double deltaTime = 0.0;
		int fps = 0;
		long timer = System.currentTimeMillis();
		
		for(int i = 0; i < 50; i++) particle.add(new Particle());
		
		while(running) {
			long now = System.nanoTime();
			deltaTime += (now - lastTime) / ns;
			lastTime = now;
			while(deltaTime >= 0) {
				tps++;
				deltaTime--;
				update();
			}
			fps++;
			render();
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(fps + " FPS, " + tps + " TPS");
				FPS = fps;
				TPS = (int)tps;
				fps = 0;
				tps = 0;
			}
		}
	}
	
	public int rc = 0; // red
	public int gc = 0; // g
	public int bc = 0; // b
	public int wc = 0; // w
	public void update() {
		rc = 0;
		gc = 0;
		bc = 0;
		wc = 0;
		for(int i = 0; i < particle.size(); i++) {
			if(particle.get(i).team.equals(Teams.team.BLUE)) bc++;
			else if(particle.get(i).team.equals(Teams.team.WHITE)) wc++;
			else if(particle.get(i).team.equals(Teams.team.RED)) rc++;
			else if(particle.get(i).team.equals(Teams.team.GREEN)) gc++;
			particle.get(i).tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
		/**/
		for(int i = 0; i < particle.size(); i++) particle.get(i).render(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 0, 20));
		g.drawString("List Size: " + particle.size(), 0, 20);
		g.drawString(FPS + "f & " + TPS + "t", 0, 40);
		g.drawString("red: " + 	 rc, 0, 60);
		g.drawString("green: " + gc, 0, 80);
		g.drawString("blue: " +  bc, 0, 100);
		g.drawString("white: " + wc, 0, 120);
		/**/
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		frame = new JFrame("Particle Test");
		frame.setSize(1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.add(main);
		frame.setVisible(true);
		
		main.start();
	}
}
