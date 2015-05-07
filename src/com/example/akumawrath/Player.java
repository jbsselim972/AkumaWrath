package com.example.akumawrath;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andlabs.andengine.extension.physicsloader.PhysicsEditorLoader;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

//import com.example.ResourcesManager;


public abstract class Player extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	    
	private Body body;
	private boolean canRun = false;
	private boolean canStop = false;
	private int footContacts = 0;
	int lives = 3;
	private int click = 0;
	private PhysicsEditorLoader loader = new PhysicsEditorLoader();
	private int speed = 5;
    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------
    
	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
	    super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
	    createPhysics(camera, physicsWorld);  
	    camera.setChaseEntity(this);
	    setIdle();
	}
	public int getClick(){
		return (click);
	}
	
	public void setClick(int count){
		click = count;
	}
	public PhysicsEditorLoader getLoader(){
		return (loader);
	}
	
	public boolean getRunningState(){
		return (canRun);
	}
	
	public boolean getIdleState(){
		return (canStop);
	}
	
	public int getSpeed(){
		return (speed);
	}
	public void setSpeed(int factor){
		speed = factor;
	}
    
    // ---------------------------------------------
    // MEMBER FCTS
    // ---------------------------------------------
    
    public abstract void onDie();
    
    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {       
//        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
//        body.setUserData("player");
//        body.setFixedRotation(true);
        try {
			loader.setAssetBasePath("xml/");
			loader.load(ResourcesManager.getInstance().activity, physicsWorld, "player.xml", this,false, false);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, loader.getBody("player"), true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);
                
//                if (getY() >= 480)
//                {                    
//                    onDie();
//                }
//                if (getX() <= 0)
//                {                    
//             	  loader.getBody("player").setLinearVelocity(new Vector2(0, loader.getBody("player").getLinearVelocity().y));
//             	  canRun = false;
//                } 
                if (lives != 0){
                if (canRun)
                {    
               	 loader.getBody("player").setLinearVelocity(new Vector2(speed,  loader.getBody("player").getLinearVelocity().y)); 
                }
                else if (!canRun){
                loader.getBody("player").setLinearVelocity(new Vector2(0,  loader.getBody("player").getLinearVelocity().y));            
                }
                }
                else{
                	 loader.getBody("player").setLinearVelocity(new Vector2(0, 6f));
                	// loader.getBody("player").setActive(false);
                    return;
                }
            }
        });
    }
    
   public void increaseFootContacts()
    {
        footContacts++;
    }

    public void decreaseFootContacts()
    {
        footContacts--;
    }

    public void setIdle()
    {   

    	 if (canRun  && footContacts >= 1 || canStop == false){
    			Debug.e("player is idle");
      final long[] PLAYER_IDLE_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100, 100};
      animate(PLAYER_IDLE_ANIMATE, 25, 31, true);

      canRun = false;
      canStop =true;
      click = 0;
    	 }
    }
    
    public void setWalking()
    {
    	 if (!canRun && footContacts >= 1 ){
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100,100 ,100 ,100,100 ,100 }; 
        animate(PLAYER_ANIMATE, 0, 7, true);
 
      	 canRun = true;
      	 click = 0;
    	 }
    }
    
    public void setRunning()
    {
      canRun = true;
        final long[] PLAYER_RUN_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100};
           
        animate(PLAYER_RUN_ANIMATE, 32, 37, true);
    }
    
    public void setJumping(int click)
    {
    	  if (footContacts < 1 && click > 2) 
    	    {
    	        return; 
    	    }	
    	  Debug.e("les clicks sont au nombre de " + click);
    	//  canRun = false; 	  
    	  loader.getBody("player").setLinearVelocity(new Vector2( loader.getBody("player").getLinearVelocity().x, -9));
    	  
    	   final long[] PLAYER_JUMP_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100, 100, 100, 100};
    	   final long[] PLAYER_DOUBLE_JUMP_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100, 100, 100};
    	   
    	   if (click == 2) {
    		animate(PLAYER_DOUBLE_JUMP_ANIMATE, 17, 24, false);
    		ResourcesManager.getInstance().engine.registerUpdateHandler(new TimerHandler(2.8f, new ITimerCallback()
    	    {                                    
    	        public void onTimePassed(final TimerHandler pTimerHandler)
    	        {
    	            pTimerHandler.reset();
   	         	Debug.e("passed time : " + footContacts);
    	            ResourcesManager.getInstance().engine.unregisterUpdateHandler(pTimerHandler);
    	            canStop = false;
    	             	Debug.e("walking smoothly " + footContacts);
    	            	setIdle();
    	        }
    	    }));
    	   }else if (click == 1)
    	   {
    			animate(PLAYER_JUMP_ANIMATE, 8, 16, false);
        		ResourcesManager.getInstance().engine.registerUpdateHandler(new TimerHandler(1.4f, new ITimerCallback()
        	    {                                    
        	        public void onTimePassed(final TimerHandler pTimerHandler)
        	        {
        	            pTimerHandler.reset();
       	         	Debug.e("passed time : " + footContacts);
        	            ResourcesManager.getInstance().engine.unregisterUpdateHandler(pTimerHandler);
        	            canStop = false;
        	             	Debug.e("walking smoothly " + footContacts);
        	            	setIdle();
        	        }
        	    }));
    	   }else {
    		   click = 0;
    	   }
    }
    

    public void setDying()
    {
    	final long[] PLAYER_DIE_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
        
        animate(PLAYER_DIE_ANIMATE, 38, 48, false);
        lives -= 1;
    }
}
