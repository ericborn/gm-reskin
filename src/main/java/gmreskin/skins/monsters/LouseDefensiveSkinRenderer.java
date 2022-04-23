package gmreskin.skins.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gmreskin.skins.SkinRenderer;

public class LouseDefensiveSkinRenderer extends SkinRenderer {
    public LouseDefensiveSkinRenderer(String skinPath) {
        super(skinPath);
        if (this.isSkinLoaded()) this.animation.scale = 0.8F;
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
            case "CLOSED":
                this.animation.setCurAnimation("curl");
                break;
            case "OPEN":
                this.animation.setCurAnimation("idle");
                break;
            case "REAR_IDLE":
                this.animation.setCurAnimation("idle");
                break;
            default:
                this.animation.setCurAnimation("idle");
        }
    }
}
