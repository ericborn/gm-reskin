package gmreskin.skins.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gmreskin.skins.SkinRenderer;

public class TransientSkinRenderer extends SkinRenderer {
    public TransientSkinRenderer(String skinPath) {
        super(skinPath);
        if (this.isSkinLoaded()) {
            this.animation.scale = 1.3F;
            this.animation.addTriggerEvent("0", animation -> TransientSkinRenderer.this.animation.setCurAnimation("idle"));
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
    public void onDamaged(int damageAmount) {
        this.animation.setCurAnimation("hit");
    }

    @Override
    public void onChangeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.animation.setCurAnimation("attack");
        }
    }
}
