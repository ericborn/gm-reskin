package gmreskin.skins.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostOrb;
import gmreskin.patches.HexaghostOrbPatch;
import gmreskin.skins.SkinRenderer;

import java.util.ArrayList;

public class HexaghostSkinRenderer extends SkinRenderer {
    private float timeMultiplier = 1.0F;

    private AbstractGameAction rotationSpeedUpAction = new AbstractGameAction() {
        @Override
        public void update() {
            timeMultiplier += Gdx.graphics.getDeltaTime();
            if (timeMultiplier >= 2.0F) isDone = true;
        }
    };

    public HexaghostSkinRenderer(String skinPath) {
        super(skinPath);
    }

    @Override
    public void update() {
        this.animation.xPosition = this.owner.hb.cX + this.owner.animX;
        this.animation.yPosition = this.owner.hb.y + this.owner.animY + 249.0F * Settings.scale;
        this.animation.flipX = this.owner.flipHorizontal;
        this.animation.flipY = this.owner.flipVertical;
        this.animation.alpha = this.owner.tint.color.a * 255.0F;
        ReflectionHacks.setPrivate(Gdx.graphics, LwjglGraphics.class, "deltaTime", Gdx.graphics.getDeltaTime() * timeMultiplier);
        this.animation.update();
        ReflectionHacks.setPrivate(Gdx.graphics, LwjglGraphics.class, "deltaTime", Gdx.graphics.getDeltaTime() / timeMultiplier);
    }

    @Override
    public void render(SpriteBatch sb) {
        this.animation.render(sb);
        ArrayList<HexaghostOrb> orbs = ReflectionHacks.getPrivate(this.owner, Hexaghost.class, "orbs");
        for (HexaghostOrb orb : orbs) {
            if (!orb.hidden) {
                HexaghostOrbPatch.AddFieldPatch.orbAnimation.get(orb).render(sb);
            }
        }
    }

    @Override
    public void onChangeState(String stateName) {
        switch (stateName) {
            case "Activate":
                if (this.rotationSpeedUpAction != null) {
                    AbstractDungeon.actionManager.addToBottom(this.rotationSpeedUpAction);
                    this.rotationSpeedUpAction = null;
                }
                break;
        }
    }
}
