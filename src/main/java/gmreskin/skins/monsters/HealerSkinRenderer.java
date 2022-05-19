package gmreskin.skins.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gmreskin.skins.SkinRenderer;

public class HealerSkinRenderer extends SkinRenderer {
    public HealerSkinRenderer(String skinPath) {
        super(skinPath);
        if (this.isSkinLoaded()) {
            this.animation.scale = 1.2F;
            this.animation.addTriggerEvent("0", animation -> HealerSkinRenderer.this.animation.setCurAnimation("idle"));
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
            case "STAFF_RAISE":
                this.animation.setCurAnimation("staff_raise");
        }
    }
}
