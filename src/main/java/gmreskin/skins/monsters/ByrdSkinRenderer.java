package gmreskin.skins.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gmreskin.skins.SkinRenderer;

public class ByrdSkinRenderer extends SkinRenderer {
    public ByrdSkinRenderer(String skinPath) {
        super(skinPath);
        if (this.isSkinLoaded()) this.animation.scale = 0.7F;
    }

    @Override
    public void update() {
        this.animation.xPosition = this.owner.hb.cX + this.owner.animX;
        this.animation.yPosition = this.owner.hb.y + this.owner.animY;
        this.animation.flipX = this.owner.flipHorizontal;
        this.animation.flipY = this.owner.flipVertical;
        this.animation.alpha = this.owner.tint.color.a * 255.0F;
        this.animation.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        this.animation.render(sb);
    }

    @Override
    public void onChangeState(String stateName) {
        switch (stateName) {
            case "GROUNDED":
                this.animation.setCurAnimation("grounded");
                break;
            case "FLYING":
                this.animation.setCurAnimation("idle");
                break;
        }
    }
}
