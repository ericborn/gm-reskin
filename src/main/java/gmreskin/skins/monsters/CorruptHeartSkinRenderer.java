package gmreskin.skins.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import gmreskin.skins.SkinRenderer;

public class CorruptHeartSkinRenderer extends SkinRenderer {
    private final ShaderProgram shaderProgram;
    private float timer = 0.0F;

    public CorruptHeartSkinRenderer(String skinPath) {
        super(skinPath);
        this.shaderProgram = new ShaderProgram(
                Gdx.files.internal("GMShaders/CorruptHeart/vertex.glsl"),
                Gdx.files.internal("GMShaders/CorruptHeart/fragment.glsl"));
        if (!shaderProgram.isCompiled()) {
            throw new RuntimeException(shaderProgram.getLog());
        }
        if (this.isSkinLoaded()) {
            this.animation.scale = 2.3F;
            this.animation.addTriggerEvent("0", a -> {
                CardCrawlGame.sound.playAV("HEART_SIMPLE", MathUtils.random(-0.05F, 0.05F), 0.75F);
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
            });
            this.animation.getCurAnimation().getLayerAnimation("2").setShaderProgram(shaderProgram); //心脏前面的那层罩子
            this.animation.getCurAnimation().getLayerAnimation("2").setPreRenderAction(shaderProgram1 -> {
                Texture heart = animation.getCurAnimation().getLayerAnimation("0").getSpriteSheet();
                Texture mask = animation.getCurAnimation().getLayerAnimation("2").getSpriteSheet();
                heart.bind(1); //心脏
                mask.bind(0); //心脏前面的那层罩子
                shaderProgram1.setUniformi("u_heart", 1);
                shaderProgram1.setUniformf("u_timer", timer);
                shaderProgram1.setUniformf("settings_scale", Settings.scale);
                timer += Gdx.graphics.getDeltaTime();
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
    public void dispose() {
        super.dispose();
        if (shaderProgram != null && shaderProgram.isCompiled()) {
            shaderProgram.dispose();
        }
    }
}
