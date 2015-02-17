package com.gmail.castellon.antonio.zombiegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Score {

	private int score;
	private int x,y;
	
	private Bitmap numbers;
	private int width;
	private int height;
	
	public Score(Bitmap resource, int xPos, int yPos)
	{
		this.numbers = resource;
		this.width = this.numbers.getWidth() / 10;
		this.height = this.numbers.getHeight();
		
		this.x = xPos;
		this.y = yPos;
		
		this.score = 0;
	}
	
	public void addScore(int num)
	{
		score= score + num;
	}
	
	 public void onDraw(Canvas canvas) 
	 {
		 String txtScore = Integer.toString(score);
		 
		 int pos_x = x;
		 
		 for(int i=0; i< txtScore.length(); i++)
		 {
			 char num = txtScore.charAt(i);
			 
			 int srcX = Integer.valueOf(String.valueOf(num)) * width; // 
			 
			 Rect src = new Rect(srcX, 0 , srcX + width, height);
	         Rect dst = new Rect(pos_x, y, pos_x + width, y + height);
	         	        		 
	         canvas.drawBitmap(numbers, src, dst, null);
	         
	         pos_x = pos_x + this.width ;
	         
	       
		 }
	 }
	
}
