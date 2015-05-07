package com.example.akumawrath;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.SAXUtils;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andlabs.andengine.extension.physicsloader.PhysicsEditorLoader;
import org.xml.sax.Attributes;

import android.opengl.GLES20;
import android.view.MotionEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.akumawrath.Enemy.EnemyType;
import com.example.akumawrath.LevelCompletion.StarsCount;
import com.example.akumawrath.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IOnAreaTouchListener{
    private HUD gameHUD;
    private Text scoreText;
    private int score = 0;
    public int level = 1;
    private PhysicsWorld physicsWorld;
    private PhysicsHandler physicsHandler;
    private boolean firstTouch = false;  
    private Player player;
    private Enemy enemy;
    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
        
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelcomplete";

    
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY1 = "enemy1";
    
    private LevelCompletion levelCompleteWindow;
    private Text gameOverText;
    private boolean gameOverDisplayed = false;
    
    /* PRIVATE FCTS */
    
    private void createBackground()
    { 	
//    	attachChild(new Sprite(0, 0, resourcesManager.game_background_region, vbom)
//        {
//            @Override
//            protected void preDraw(GLState pGLState, Camera pCamera) 
//            {
//                super.preDraw(pGLState, pCamera);
//                pGLState.enableDither();
//            }
//        });
       // setBackground(new Background(Color.WHITE));
    	if (ResourcesManager.getInstance().level == 1) {
    		final AutoParallaxBackground AutoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
		final VertexBufferObjectManager vertexBufferObjectManager = vbom;
		AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, 480 - resourcesManager.mParallaxLayerBack1.getHeight(), resourcesManager.mParallaxLayerBack1, vertexBufferObjectManager)));
		AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, 80, resourcesManager.mParallaxLayerMid1, vertexBufferObjectManager)));
		AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, 100, resourcesManager.mParallaxLayerFront1, vertexBufferObjectManager)));
		setBackground(AutoParallaxBackground);
    	} else if (ResourcesManager.getInstance().level == 2) 
    	{
    		
    	  	final AutoParallaxBackground AutoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
    	  	final VertexBufferObjectManager vertexBufferObjectManager = vbom;
    	  	AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, 480 - resourcesManager.mParallaxLayerBack2.getHeight(), resourcesManager.mParallaxLayerBack2, vertexBufferObjectManager)));
    	  	AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-3.0f, new Sprite(0, 50, resourcesManager.mParallaxLayerMid2, vertexBufferObjectManager)));
    	  	AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, 480 - resourcesManager.mParallaxLayerFront2.getHeight(), resourcesManager.mParallaxLayerFront2, vertexBufferObjectManager)));
    		setBackground(AutoParallaxBackground);
    	}else if (ResourcesManager.getInstance().level == 3)
    	{
	  	final AutoParallaxBackground AutoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
	  	final VertexBufferObjectManager vertexBufferObjectManager = vbom;
	  	AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, 480 - resourcesManager.mParallaxLayerBack3.getHeight(), resourcesManager.mParallaxLayerBack3, vertexBufferObjectManager)));
	  	AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-3.0f, new Sprite(0, 100, resourcesManager.mParallaxLayerMid3, vertexBufferObjectManager)));
	  	AutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, 480 - resourcesManager.mParallaxLayerFront3.getHeight(), resourcesManager.mParallaxLayerFront3, vertexBufferObjectManager)));
		setBackground(AutoParallaxBackground);
    	}
    }
    
    private void createHUD()
    {
    	  gameHUD = new HUD();
    	  final Sprite life;
    	final int  click = 0;
    	  life = new Sprite(690, 10, resourcesManager.player_life_region, vbom);
    	  gameHUD.attachChild(life);
    	    // CREATE SCORE TEXT
    	  //  scoreText = new Text(400, 240, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
    	    scoreText = new Text(25, 25, resourcesManager.font, "Score: 0123456789",vbom);
    	    scoreText.setPosition(20, 10);
    	    scoreText.setText("Score: 0");
    	    scoreText.setColor(Color.WHITE);
    	    gameHUD.attachChild(scoreText);  
    	    
    	    physicsHandler = new PhysicsHandler(player);
    	  	
    		final Sprite	 buttonA = new Sprite(620, 340, resourcesManager.mOnScreenControlButtonARegion, vbom)
    	 	 {
    	 		@Override
    	 		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
    	 		{
    	 		if(pSceneTouchEvent.isActionDown())
    	 		{
    	           // Debug.e("BUTTON A PRESSED");  
    	            //running 	        
    	     }
    			return true;
    	 	 };
    	 	 };
    	 	gameHUD.registerTouchArea(buttonA);
    	 	gameHUD.attachChild(buttonA);
    	  	 final Sprite	 buttonB = new Sprite(690, 390, resourcesManager.mOnScreenControlButtonBRegion, vbom)
    	  	 {
    	  		@Override
    	  		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
    	  		{
    	  		if(pSceneTouchEvent.isActionDown())
    	  		{
    	            Debug.e("BUTTON B PRESSED" + player.getClick() + 1); 
    	        	Debug.e("player is Jumping");
    	        	player.setClick(player.getClick() + 1);
    	    		player.setJumping(player.getClick());
    	       
    	     }
    			return true;
    	  	 };
    	  	 };
    	  	gameHUD.registerTouchArea(buttonB);
    	  	gameHUD.attachChild(buttonB);
	    
    	    camera.setHUD(gameHUD);
    	    Debug.e("created text");
    }

    private void addToScore(int i)
    {
        score += i;
        scoreText.setText("Score: " + score);
    }
    
    private void createPhysics()
    { 
    physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 17), false);

    physicsWorld.setContactListener(contactListener());
    registerUpdateHandler(physicsWorld);
    }
    
    private void loadLevel(int levelID) 
    {
        final LevelLoader levelLoader = new LevelLoader();
        
        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
        levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new IEntityLoader() 
        {
        
			public IEntity onLoadEntity(String pEntityName,
					Attributes pAttributes) {
			
                final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
                Debug.e( "WIDTH : "+ width +  "et HEIGHT : "+ height);
                camera.setBounds(0, 0, width, height); // here we set camera bounds
             //   camera.setChaseEntity(player);
                camera.setBoundsEnabled(true);
                return GameScene.this;
            }

        }); 
        
        levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader()             
        {
        	 
			public IEntity onLoadEntity(final String pEntityName,
					Attributes pAttributes) {
				
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);	
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
                
                final Sprite levelObject;
             
                final PhysicsEditorLoader loader = new PhysicsEditorLoader();
                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
                  PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
                   
//            		try {
//            			loader.setAssetBasePath("xml/");
//            			loader.load(activity, physicsWorld, "platform1.xml", levelObject,false, false);
//            	
//            		} catch (IOException e) {
//            			e.printStackTrace();
//            		}
                } 
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
               //    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
               //    body.setUserData("platform2");;
                    try {
            			loader.setAssetBasePath("xml/");
            			loader.load(activity, physicsWorld, "platform2.xml", levelObject,false, false);
            	
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, loader.getBody("platform2"), true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
//                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
//                    body.setUserData("platform3");
                    try {
            			loader.setAssetBasePath("xml/");
            			loader.load(activity, physicsWorld, "platform3.xml", levelObject,false, false);
            	
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, loader.getBody("platform3"), true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
                {
                    levelObject = new Sprite(x, y, resourcesManager.coin_region, vbom)
                    {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) 
                        {
                            super.onManagedUpdate(pSecondsElapsed);
                            
                            if (player.collidesWith(this))
                            {
                                addToScore(10);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                                //registerTouchArea(this);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }  
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
                {
                    player = new Player(x, y, vbom, camera, physicsWorld)
                    {
                        @Override
                        public void onDie()
                        {
                        	if (!gameOverDisplayed)
                            {
                                displayGameOverText();
                            }	
                        }
                    };
                    levelObject = player;
                    Debug.e( "player created");
                } 
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY1))
                {
                	enemy = new Enemy(x, y, vbom, camera, physicsWorld, resourcesManager.enemy1_region)
                    {
                        @Override
                        public void onDie()
                        {
                        	enemy.setDying();
                        }
                    };
                    levelObject = enemy;          
                    enemy.setWalking();
                }else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
                {
                    levelObject = new Sprite(x, y, resourcesManager.star_region, vbom)
                    {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) 
                        {
                            super.onManagedUpdate(pSecondsElapsed);

                            if (player.collidesWith(this))
                            {
                            	ResourcesManager.getInstance().level += 1;
                                levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                                
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else
                {
                    throw new IllegalArgumentException();
                }
            
                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        
        });

        try {
            levelLoader.loadLevelFromAsset(activity.getAssets(), "gfx/game/levels/" + levelID + ".lvl");
    } catch (final IOException e) {
            Debug.e(e);
    }
    }

    
    /* BASE FCTS */
    
    @Override
    public void createScene()
    {
    	//level += 1;
    	createBackground();
    	createHUD();
    	createPhysics();
    	Debug.e("niveau : " + level);
    	loadLevel(ResourcesManager.getInstance().level);
    	createGameOverText();
    	levelCompleteWindow = new LevelCompletion(vbom);
    	setOnSceneTouchListener(this);
      	setOnAreaTouchListener(this);
    	DebugRenderer debug = new DebugRenderer(physicsWorld, vbom);
        this.attachChild(debug);
        createController();
    }

    public void createController(){
        physicsHandler = new PhysicsHandler(player);
        final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(40, 480 - this.resourcesManager.mOnScreenControlBaseTextureRegion.getHeight(), camera, this.resourcesManager.mOnScreenControlBaseTextureRegion, this.resourcesManager.mOnScreenControlKnobTextureRegion, 0.1f, 200, this.vbom, new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				if ((pValueX > 0.5) && player.getRunningState() == false){
					player.setFlippedHorizontal(false);
					player.setSpeed(5);
					player.setWalking();
				} else if ((pValueX < -0.5) && player.getRunningState() == false){
					player.setFlippedHorizontal(true);
					player.setSpeed(-5);			
					player.setWalking();
				}
				if (pValueX < 0.4 && pValueX > -0.4 && pValueY < 0.4 && pValueY > -0.4)
				{
					player.setIdle();
				}
			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
			//	player.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.25f, 1, 1.5f), new ScaleModifier(0.25f, 1.5f, 1)));
			}
		});
    	analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    	analogOnScreenControl.getControlBase().setAlpha(0.5f);
    	analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
    	analogOnScreenControl.getControlBase().setScale(1.25f);
    	analogOnScreenControl.getControlKnob().setScale(1.25f);
    	analogOnScreenControl.refreshControlKnobPosition();

    	this.setChildScene(analogOnScreenControl);    
    }
  
    
    @Override
    public void onBackKeyPressed()
    {
    	ResourcesManager.getInstance().level = 1;	
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    	camera.setHUD(null);
    	camera.setCenter(400, 240);
    	camera.setChaseEntity(null);
    }
    
    private void createGameOverText()
    {
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
        gameOverText.setColor(Color.RED);
    }

    private void displayGameOverText()
    {
        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
        attachChild(gameOverText);
        gameOverDisplayed = true;
    }
    
    
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {
    	if (pSceneTouchEvent.isActionDown())
        {
            if (!firstTouch)
            {
                firstTouch = true;
            }
            else
            {          	 
            	firstTouch = false;
            }
        }
        return false;
    }
    	
    private ContactListener contactListener() 
    {
        ContactListener contactListener = new ContactListener()
        {
            public void beginContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();
;
                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x1.getBody().getUserData().equals("player"))
                    {
                        player.increaseFootContacts(); 
                    }
                    if (x1.getBody().getUserData().equals("enemy11"))
                    {
                        enemy.increaseFootContacts(); 
                    }
                }
                if (x2.getBody().getUserData().equals("platform3") && x1.getBody().getUserData().equals("player"))
                {
                    x2.getBody().setType(BodyType.DynamicBody);
                }
                if ((x2.getBody().getUserData().equals("enemy11") && x1.getBody().getUserData().equals("player")) || (x2.getBody().getUserData().equals("player") && x1.getBody().getUserData().equals("enemy11")))
                {
                    Debug.e("X2 : " + x2.getBody().getUserData());
                    Debug.e("X2 x :" + x2.getBody().getPosition().x + " et X2 y : " + x2.getBody().getPosition().y);
                    Debug.e("X1 : " + x1.getBody().getUserData());
                    Debug.e("X1 x :" + x1.getBody().getPosition().x + " et X1 y : " + x1.getBody().getPosition().y);
                    if ( x1.getBody().getPosition().y <  x2.getBody().getPosition().y)
                    {
                    	enemy.setDying();
                    }else
                    {
                     	player.setDying();
                    }
                }
                if (x2.getBody().getUserData().equals("platform2") && x1.getBody().getUserData().equals("player"))
                {
                    engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
                    {                                    
                        public void onTimePassed(final TimerHandler pTimerHandler)
                        {
                            pTimerHandler.reset();
                            engine.unregisterUpdateHandler(pTimerHandler);
                            x2.getBody().setType(BodyType.DynamicBody);
                        }
                    }));
                }
              
            }

            public void endContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x1.getBody().getUserData().equals("player"))
                    {
                        player.decreaseFootContacts();
                    }
                }
            }

			public void preSolve(Contact contact, Manifold oldManifold) {
			
			}

			public void postSolve(Contact contact, ContactImpulse impulse) {
				
				
			}
        };
        return contactListener;
    }

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
            Debug.e("BUTTON A PRESSED");                               
     }
     else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
   	  Debug.e("BUTTON B PRESSED"); 
     }
		return false;
	}

}
