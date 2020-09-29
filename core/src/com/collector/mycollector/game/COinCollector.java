package com.collector.mycollector.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class COinCollector extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man ;
	int manstate =0, pause=0 ;
	float gravity = 0.2f ;
	float velocity=0;
	int mY = 0;

	ArrayList<Integer> X = new ArrayList<>();
	ArrayList<Integer> Y = new ArrayList<>();
	Texture prize;
	Random random;
	int prizecount;

    ArrayList<Integer> bX = new ArrayList<>();
    ArrayList<Integer> bY = new ArrayList<>();
    Texture bomb;
    int bC;

    ArrayList<Rectangle> prizeRect = new ArrayList<>();
	ArrayList<Rectangle> Rect = new ArrayList<>();

	Rectangle manRect;
	int score=0;
	BitmapFont font;

	int mainState =0;
    Texture dizzy;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man=new Texture[4];
		man[0]= new Texture("frame-1.png");
		man[1]= new Texture("frame-2.png");
		man[2]= new Texture("frame-3.png");
		man[3]= new Texture("frame-4.png");
        prize =  new Texture("coin.png");
        bomb =  new Texture("bomb.png");
        dizzy = new Texture("dizzy-1.png");
		  mY =Gdx.graphics.getHeight() / 2;
		  random = new Random();
		  font = new BitmapFont();
		  font.setColor(Color.WHITE);
		  font.getData().setScale(10);

	}

	public void makeCoin()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		Y.add((int)height);

		X.add(Gdx.graphics.getWidth());

	}

    public void makeBomb()
    {
        float height = random.nextFloat() * (Gdx.graphics.getHeight()-bomb.getHeight());//change made
        bY.add((int)height);

        bX.add(Gdx.graphics.getWidth());

    }


	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		 if(mainState ==1)//Game is running
		 {

			 if( bC <250)
			 {
				 bC++;
			 }
			 else
			 {
				 bC =0;
				 makeBomb();
			 }
			 Rect.clear(); //change made
			 for(int i = 0; i< bX.size(); i++)
			 {
				 batch.draw(bomb, bX.get(i), bY.get(i));
				 bX.set(i, bX.get(i)-8);
				 Rect.add(new Rectangle(bX.get(i), bY.get(i),bomb.getWidth(),bomb.getHeight()));
			 }





			 if( prizecount <150)
			 {
				 prizecount++;
			 }
			 else
			 {
				 prizecount =0;
				 makeCoin();
			 }

			 prizeRect.clear();
			 for(int i = 0; i< X.size(); i++)
			 {
				 batch.draw(prize, X.get(i), Y.get(i));
				 X.set(i, X.get(i)-4);
				 prizeRect.add(new Rectangle(X.get(i), Y.get(i), prize.getWidth(), prize.getHeight()));
			 }



			 if ( Gdx.input.justTouched())
			 {
				 velocity=-10;
			 }

			 if(pause <8)
			 {
				 pause++;
			 }
			 else {
				 pause = 0;
				 if (manstate < 3) {
					 manstate++;
				 } else {
					 manstate = 0;
				 }

			 }

			 if(mY <=0)
			 {
				 mY =0;
			 }
			 int g= Gdx.graphics.getHeight() - man[manstate].getHeight()/2;
			 if(mY >=g)
			 {
				 mY = Gdx.graphics.getHeight()- man[manstate].getHeight()/2;
			 }


			 velocity +=gravity;//velocity = velocity + gravity
			 mY -= velocity;//manY = manY- velocity

		 }
		 else if(mainState ==0)//about to start
		 {
			 if( Gdx.input.justTouched()) {
				 mainState = 1;
			 }

		 }
		 else if(mainState ==2)//over
		 {
			 if( Gdx.input.justTouched()) {
				 mainState = 1;
				 mY = Gdx.graphics.getHeight()/2;
				 score=0;
				 velocity=0;
				 X.clear();
				 Y.clear();
				 prizeRect.clear();
				 prizecount =0;

				 bX.clear();
				 bY.clear();
				 Rect.clear();
				 bC =0;
			 }
		 }




       if(mainState ==2)
	   {
	   	batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, mY);
	   }else {
		   batch.draw(man[manstate], Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, mY);
	   }



		manRect = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, mY,man[manstate].getWidth(),man[manstate].getHeight());

		for(int i = 0; i< prizeRect.size(); i++)
		{
			if(Intersector.overlaps(manRect, prizeRect.get(i)))
			{
				score++;
				prizeRect.remove(i);
				X.remove(i);
				Y.remove(i);
				break;
			}
		}

		for(int i = 0; i< Rect.size(); i++)
		{
			if(Intersector.overlaps(manRect, Rect.get(i)))
			{
				mainState =2;
			}
		}

		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
