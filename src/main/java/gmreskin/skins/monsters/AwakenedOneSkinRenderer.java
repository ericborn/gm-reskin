package gmreskin.skins.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import gmreskin.skins.SkinRenderer;

public class AwakenedOneSkinRenderer extends SkinRenderer {
    private String defaultAnimation;

    public AwakenedOneSkinRenderer(String skinPath) {
        super(skinPath);
        if (this.isSkinLoaded()) {
            this.defaultAnimation = "idle";
            this.animation.addTriggerEvent("0", a -> animation.setCurAnimation(this.defaultAnimation));
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
        if (this.animation.getCurAnimationName().equals(this.defaultAnimation) || this.animation.getCurAnimationName().equals("hit")) {
            if (ReflectionHacks.getPrivate(this.owner, AwakenedOne.class, "form1")) {
                this.animation.setCurAnimation("hit");
            } else {
                this.animation.setCurAnimation("awakenedHit");
            }
        }
    }

    @Override
    public void onChangeState(String stateName) {
        switch (stateName) {
            case "ATTACK_1":
                this.animation.setCurAnimation("attack");
                break;
            case "ATTACK_2":
                this.animation.setCurAnimation("awakenedAttack");
                break;
            case "REBIRTH":
                this.animation.setCurAnimation("rebirth");
                this.defaultAnimation = "awakenedIdle";
                break;
        }
    }
}
