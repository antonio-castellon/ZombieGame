package com.gmail.castellon.antonio.zombiegame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView 
{

	 private SurfaceHolder holder;
     private GameLoopThread gameLoopThread;
     
     
     private List<Sprite> sprites = new ArrayList<Sprite>();
     private List<SplashSprite> temps = new ArrayList<SplashSprite>();
	 private long lastClick;
	 
	 private Bitmap buffer_bitmap;
	 private Canvas buffer_canvas;
	 
	 private Bitmap blood;
	 private Bitmap background;
	 
	 private int initNumZombies = 5;
	 
	 private int[] zombiTypes = {R.drawable.zombie_1, R.drawable.zombie_2,
								 R.drawable.zombie_3, R.drawable.zombie_4,
								 R.drawable.zombie_5, R.drawable.zombie_6,
								 R.drawable.zombie_7, R.drawable.zombie_8};
    
	 Score score;
	 
     public GameView(Context context) 
     {
           super(context);
           gameLoopThread = new GameLoopThread(this);
           holder = getHolder();
           holder.addCallback(new SurfaceHolder.Callback() {

                  @Override
                  public void surfaceDestroyed(SurfaceHolder holder) {
                         boolean retry = true;
                         gameLoopThread.setRunning(false);
                         while (retry) {
                                try {
                                      gameLoopThread.join();
                                      retry = false;
                                } catch (InterruptedException e) {
                                }
                         }
                  }

                  @Override
                  public void surfaceCreated(SurfaceHolder holder) {
                         initCreateSprites();
                         gameLoopThread.setRunning(true);
                         gameLoopThread.start();
                  }

                  @Override
                  public void surfaceChanged(SurfaceHolder holder, int format,
                                int width, int height) {
                  }
           });
           
           
           
     }
    
     private void initCreateSprites() 
     {
    	 createZombies(initNumZombies);
    	 blood = BitmapFactory.decodeResource(getResources(), R.drawable.blood_10);
    	 background = BitmapFactory.decodeResource(getResources(), R.drawable.fullbackground);
    	 score = new Score( BitmapFactory.decodeResource(getResources(), R.drawable.numbers), 
    			 			getWidth() - 400, 
    			 			getHeight() - 120);
     }
     
     /**
      * Add  zombies to the game.
      * @param numZombies
      */
     private void createZombies(int numZombies)
     {
    	 Random rnd = new Random();
   	  
    	 for (int j=0; j < numZombies; j++)
    	 {
    		 int ztype = rnd.nextInt(zombiTypes.length);
    		 sprites.add(createSprite(zombiTypes[ztype]));
    	 }    	
     }
    
     private Sprite createSprite(int resouce) 
     {
           Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
           return new Sprite(this,bmp);
     }
   
     
     public void createScene()
     {
    	 if(buffer_bitmap == null)
         {
             buffer_bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
             buffer_canvas = new Canvas(buffer_bitmap);
         }
    	 
         drawBackground(buffer_canvas);
         
        
         for (int i = temps.size() - 1; i >= 0; i--) 
         {
               temps.get(i).onDraw(buffer_canvas);
         }
           
         for (Sprite sprite : sprites) 
         {
               sprite.onDraw(buffer_canvas);
         }
         
         score.onDraw(buffer_canvas);
     }
     
     
     @Override
     protected void onDraw(Canvas canvas) 
     {
    	 createScene();
    	 canvas.drawBitmap(buffer_bitmap, 0, 0, null); 
     }
     
    private void drawBackground(Canvas canvas) 
    {
    	 canvas.drawBitmap(background, -192*2 , -5 , null);
	}

	@Override
     public boolean onTouchEvent(MotionEvent event) 
     {
           if (System.currentTimeMillis() - lastClick > 500) {
                  lastClick = System.currentTimeMillis();
                  float x = event.getX();
                  float y = event.getY();
                  
                  synchronized (getHolder()) 
                  {
                      for (int i = sprites.size() - 1; i >= 0; i--) 
                      {
                          Sprite sprite = sprites.get(i);
                          
                          if (sprite.isCollition(x, y)) 
                          {
                                sprites.remove(sprite);
                                temps.add(new SplashSprite(temps, this, x, y, blood));
                                score.addScore(10);
                                if (sprites.isEmpty())
                                {
                                	initNumZombies = initNumZombies*2;
                                	createZombies(initNumZombies);
                                }
                                break;
                          }
                      }
                  }
           }
           return true;
     }
}
