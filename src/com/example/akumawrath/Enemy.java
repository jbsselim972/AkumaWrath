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
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andlabs.andengine.extension.physicsloader.PhysicsEditorLoader;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

//import com.example.ResourcesManager;


public abstract class Enemy extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	    
	private Body body;
	private PhysicsEditorLoader loader = new PhysicsEditorLoader();
	private int speed = 2;
	private Boolean left = true;
	private int alive = 1;
	private int footContacts = 0;
	public enum EnemyType
    {
        RAT,
        WOLF,
        SNAKE,
    }
    
    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------
    
	public Enemy(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld, ITiledTextureRegion type)
	{
	    super(pX, pY, ResourcesManager.getInstance().enemy1_region, vbo);
	    createPhysics(camera, physicsWorld);  
	}
	

	public PhysicsEditorLoader getLoader(){
		return (loader);
	}
	
	public int getSpeed(){
		return (speed);
	}
	public void setSpeed(int factor){
		speed = factor;
	}
	
	  public void increaseFootContacts()
	    {
	        footContacts++;
	    }

	    public void decreaseFootContacts()
	    {
	        footContacts--;
	    }
    // ---------------------------------------------
    // MEMBER FCTS
    // ---------------------------------------------
    
    public abstract void onDie();
    
    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {       
        try {
			loader.setAssetBasePath("xml/");
			loader.load(ResourcesManager.getInstance().activity, physicsWorld, "enemy11.xml", this,false, false);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, loader.getBody("enemy11"), true, false)
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
           if (alive == 1){
               	 loader.getBody("enemy11").setLinearVelocity(new Vector2(speed,  loader.getBody("enemy11").getLinearVelocity().y));
            } else {
                loader.getBody("enemy11").setLinearVelocity(new Vector2(0, 2));
            	 loader.getBody("enemy11").setActive(false);
                return;
            }
             
        	// loader.getBody("enemy11").setLinearVelocity(new Vector2(speed,  loader.getBody("enemy11").getLinearVelocity().y));
            	 if (footContacts == 5) {
            		 if (left){
                		setFlippedHorizontal(true);
                		left = false;
            		 }
            		 else{
            			setFlippedHorizontal(false);	
                		left = true;
                	 }
            			setSpeed(-speed);
                		footContacts = 0;
            	 }
            }
        });
    }
    
 
    
    public void setWalking()
    {
        final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100,100 ,100 ,100}; 
        animate(ENEMY_ANIMATE, 0, 5, true);    
    }

    public void setDying()
    {
        final long[] ENEMY_DIE_ANIMATE = new long[] { 100, 100, 100,100 ,100 ,100}; 
        animate(ENEMY_DIE_ANIMATE, 6, 11, false);    
        alive = 0;
    }
}
