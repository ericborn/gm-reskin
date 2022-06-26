package gmreskin.skins.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import gmreskin.skins.SkinRenderer;

public class ReptomancerSkinRenderer extends SkinRenderer {
    public ReptomancerSkinRenderer(String skinPath) {
        super(skinPath);
        if (this.isSkinLoaded()) {
            this.animation.addTriggerEvent("0", a -> {
                animation.getCurAnimation().getLayerAnimation("0").setInterMode(Interpolation.linear);
                animation.setCurAnimation("idle");
            });
            this.animation.addTriggerEvent("1", a -> {
                animation.getCurAnimation().getLayerAnimation("0").setInterMode(Interpolation.exp10Out);
            });
        }
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
            case "SUMMON":
                this.animation.setCurAnimation("summon");
                break;
            case "ATTACK":
                this.animation.setCurAnimation("attack");
                break;
        }
    }
}
