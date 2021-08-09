package cst8218.onei0212.game;

import cst8218.onei0212.entity.Sprite;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;

/**
 * Defines the unique session between the server and client. The only unique 
 * attribute is the color in which new sprites will appear as.
 * 
 * @author tgk
 */
@Stateful
public class SpriteSession implements SpriteSessionRemote {

    public static final Random random = new Random();
    Color color = new Color(
            random.nextInt(255), 
            random.nextInt(255), 
            random.nextInt(255));
    @EJB
    private SpriteGame spriteGame;

    public List<Sprite> getSpriteList() {
        return spriteGame.getSpriteList();
    }

    @Override
    public void newSprite(MouseEvent event) {
        spriteGame.newSprite(event, color);
    }
    @Override
    public int getHeight() {
        return spriteGame.HEIGHT;
    }
    @Override
    public int getWidth() {
        return spriteGame.WIDTH;
    }
}
