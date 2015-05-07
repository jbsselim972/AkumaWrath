package com.example.akumawrath;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import com.example.akumawrath.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
      //  setBackground(new Background(Color.BLACK));
        attachChild(new Text(350, 240, resourcesManager.font, "Loading...", vbom));
    	 createBackground();
    }

    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }
    
    private void createBackground()	
    {
        attachChild(new Sprite(200, 100, resourcesManager.loading_region, vbom)
        {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) 
            {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        });
    }
    
}