package com.example.akumawrath;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

public class ResourcesManager {
	 //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public MainActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    public Font font;
    public int level = 1;
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    
    /* SPLASH SCENE */
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;
    
    /* MENU SCENE */
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;
    public ITextureRegion editor_region;
     
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    
    /* LOADING SCENE */
    public ITextureRegion loading_region;
    
    /* GAME SCENE */
    public ITextureRegion game_background_region;
    public ITextureRegion platform1_region;
    public ITextureRegion platform2_region;
    public ITextureRegion platform3_region;
    public ITextureRegion coin_region;
    
    public BuildableBitmapTextureAtlas gameTextureAtlas;
    
    public ITiledTextureRegion enemy1_region;
    public ITiledTextureRegion player_region;
    public ITextureRegion player_life_region;
    
    public BitmapTextureAtlas mOnScreenControlTexture;
    public ITextureRegion mOnScreenControlBaseTextureRegion;
    public ITextureRegion mOnScreenControlKnobTextureRegion;
    public ITextureRegion mOnScreenControlButtonARegion;
    public ITextureRegion mOnScreenControlButtonBRegion;
    
    /*PARALLAX BACKGROUND*/
    
    public BitmapTextureAtlas mAutoParallaxBackgroundTexture1;

    public ITextureRegion mParallaxLayerBack1;
    public ITextureRegion mParallaxLayerMid1;
	public ITextureRegion mParallaxLayerFront1;
	
	 
    public BitmapTextureAtlas mAutoParallaxBackgroundTexture2;

    public ITextureRegion mParallaxLayerBack2;
    public ITextureRegion mParallaxLayerMid2;
	public ITextureRegion mParallaxLayerFront2;
	
	 
    public BitmapTextureAtlas mAutoParallaxBackgroundTexture3;

    public ITextureRegion mParallaxLayerBack3;
    public ITextureRegion mParallaxLayerMid3;
	public ITextureRegion mParallaxLayerFront3;
	
	// Level Complete Window
	public ITextureRegion complete_window_region;
	public ITextureRegion star_region;
	public ITiledTextureRegion complete_stars_region;
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    
    //---------------------------------------------
    // MENU SCENE
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }
    
    private void loadMenuGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);
    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "Menu_background.png");
    	play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "Play.png");
    	options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "Options.png");
    	editor_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "Editor.png");
    	loading_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "Loading.png"); //Loading scene
    	       
    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
    	

		
    }
    
    private void loadMenuAudio()
    {
        
    }
    
    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }
  
    private void loadMenuFonts()
    {
        FontFactory.setAssetBasePath("font/");
    //    final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        font = FontFactory.createFromAsset(activity.getFontManager(),
        		activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
                activity.getAssets(), "font.ttf", 32f, true,
                Color.RED_ABGR_PACKED_INT);
        
        //font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf",35, true,5,(float)20, 6);
        font.load();
        Debug.e("GAME FONTS LOADED.");
    }
    //---------------------------------------------
    // GAME SCENE
    //---------------------------------------------

    public void loadGameResources()
    {
        loadGameGraphics();
        //loadGameFonts();
        loadGameAudio();
    }
    
    private void loadGameGraphics()
    {
    	//this.level += 1;
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 4000, 2000, TextureOptions.BILINEAR);
        
        platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png"); 
        platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
        platform3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
        coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
        game_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Game_background.png");
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelcomplete.png");
        star_region =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "star2.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);
        enemy1_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "characters/enemy11.png", 6, 2);
        player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "characters/player.png", 9, 10);
        player_life_region =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "player_life.png");
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
        
        /* CONTROLLER RESSOURCES*/
        mOnScreenControlTexture = new BitmapTextureAtlas(activity.getTextureManager(), 456, 128, TextureOptions.BILINEAR);
      		mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, activity, "onscreen_control_base.png", 0, 0);
      		mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, activity, "onscreen_control_knob.png", 128, 0);
      		mOnScreenControlButtonARegion =BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, activity, "buttonA.png", 192, 0);
      		mOnScreenControlButtonBRegion =BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, activity, "buttonB.png", 292, 0);
      		mOnScreenControlTexture.load();
      		/*BACKGROUND*/
      		
      		
      		mAutoParallaxBackgroundTexture3 = new BitmapTextureAtlas(activity.getTextureManager(), 2000, 2000);
    		mParallaxLayerFront3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture3, activity, "level3_parallax_background_layer_front.png", 0, 0);
    		mParallaxLayerBack3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture3, activity, "level3_parallax_background_layer_back.png", 0, 115);
    		mParallaxLayerMid3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture3, activity, "level3_parallax_background_layer_mid.png", 0, 595);
    		mAutoParallaxBackgroundTexture3.load();
	
    		mAutoParallaxBackgroundTexture2 = new BitmapTextureAtlas(activity.getTextureManager(), 2000, 2000);
    		mParallaxLayerFront2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture2, activity, "level2_parallax_background_layer_front.png", 0, 0);
    		mParallaxLayerBack2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture2, activity, "level2_parallax_background_layer_back.png", 0, 188);
    		mParallaxLayerMid2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture2, activity, "level2_parallax_background_layer_mid.png", 0, 670);
    		mAutoParallaxBackgroundTexture2.load();
    		
      		mAutoParallaxBackgroundTexture1 = new BitmapTextureAtlas(activity.getTextureManager(), 2000, 2000);
    		mParallaxLayerFront1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture1, activity, "level1_parallax_background_layer_front.png", 0, 0);
    		mParallaxLayerBack1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture1, activity, "level1_parallax_background_layer_back.png", 0, 676);
    		mParallaxLayerMid1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture1, activity, "level1_parallax_background_layer_mid.png", 0, 1560);
    		mAutoParallaxBackgroundTexture1.load();
    }
    
    private void loadGameFonts()
    {
        FontFactory.setAssetBasePath("font/");
    //    final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        font = FontFactory.createFromAsset(activity.getFontManager(),
        		activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
                activity.getAssets(), "font.ttf", 32f, true,
                Color.YELLOW_ARGB_PACKED_INT);
        
        //font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf",35, true,5,(float)20, 6);
        font.load();
        Debug.e("GAME FONTS LOADED.");
    }
    
    private void loadGameAudio()
    {
        
    }
    
    public void unloadGameTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    }
    //---------------------------------------------
    // SPLASH SCENE
    //---------------------------------------------
    
    public void loadSplashScreen()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 600, 360, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "Splash.png", 0, 0);
    	splashTextureAtlas.load();
    }
    
    public void unloadSplashScreen()
    {
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, MainActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}
