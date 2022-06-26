package gmreskin.skins.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import gmreskin.GMReskin;
import gmreskin.skins.SkinRenderer;

public class LagavulinSkinRenderer extends SkinRenderer {
    private float scale = 1.0F;
    private float timer = 0.0F;

    public LagavulinSkinRenderer(String skinPath) {
        super(skinPath);
        this.animation.addTriggerEvent("0", a -> animation.setCurAnimation("idle"));
        GMReskin.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (ReflectionHacks.getPrivate(LagavulinSkinRenderer.this.owner, Lagavulin.class, "asleep")) {
                    LagavulinSkinRenderer.this.animation.setCurAnimation("asleep");
                }
                isDone = true;
            }
        });
    }

    @Override
    public void update() {
        this.animation.xPosition = this.owner.hb.cX + this.owner.animX;
        this.animation.yPosition = this.owner.hb.y + this.owner.animY;
        this.animation.flipX = this.owner.flipHorizontal;
        this.animation.flipY = this.owner.flipVertical;
        this.animation.alpha = this.owner.tint.color.a * 255.0F;
        this.animation.scale = this.scale;
        this.animation.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        this.animation.render(sb);
    }

    @Override
    public void onChangeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.animation.setCurAnimation("attack");
                break;
            case "DEBUFF":
                this.animation.setCurAnimation("suck");
                break;
            case "OPEN":
                this.animation.setCurAnimation("wakeup");
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        timer += Gdx.graphics.getDeltaTime();
                        if (timer >= 0.46F) scale += Gdx.graphics.getDeltaTime() * 0.6F;
                        if (scale >= 1.2F) isDone = true;
                    }
                });
                break;
        }
    }
}
