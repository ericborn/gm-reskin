package gmreskin.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostOrb;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;
import demoMod.anm2player.AnimatedActor;
import gmreskin.GMReskin;

public class HexaghostOrbPatch {
    @SpirePatch(
            clz = HexaghostOrb.class,
            method = SpirePatch.CLASS
    )
    public static class AddFieldPatch {
        public static SpireField<AnimatedActor> orbAnimation = new SpireField<>(() -> {
            AnimatedActor animatedActor = new AnimatedActor(GMReskin.getResourcePath("skins/monsters/HexaghostOrb.xml"));
            animatedActor.setCurAnimation("idle");
            animatedActor.addTriggerEvent("0", animation -> animatedActor.setCurAnimation("idle"));
            animatedActor.addTriggerEvent("1", animation -> animatedActor.setCurAnimation("deactivated"));
            return animatedActor;
        });
    }

    @SpirePatch(
            clz = HexaghostOrb.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static SpireReturn<Void> Prefix(HexaghostOrb orb, float oX, float oY) {
            Color color = ReflectionHacks.getPrivate(orb, HexaghostOrb.class, "color");
            if (!orb.hidden) {
                float x = ReflectionHacks.getPrivate(orb, HexaghostOrb.class, "x");
                float y = ReflectionHacks.getPrivate(orb, HexaghostOrb.class, "y");
                if (orb.activated) {
                    float activateTimer = ReflectionHacks.getPrivate(orb, HexaghostOrb.class, "activateTimer");
                    activateTimer -= Gdx.graphics.getDeltaTime();
                    ReflectionHacks.setPrivate(orb, HexaghostOrb.class, "activateTimer", activateTimer);
                    if (activateTimer < 0.0F) {
                        if (!orb.playedSfx) {
                            orb.playedSfx = true;
                            AbstractDungeon.effectsQueue.add(new GhostIgniteEffect(x + oX, y + oY));
                            AddFieldPatch.orbAnimation.get(orb).setCurAnimation("activate");
                            if (MathUtils.randomBoolean()) {
                                CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1", 0.3F);
                            } else {
                                CardCrawlGame.sound.play("GHOST_ORB_IGNITE_2", 0.3F);
                            }
                        }
                        color.a = MathHelper.fadeLerpSnap(color.a, 1.0F);
                    }
                } else {
                    color.a = MathHelper.fadeLerpSnap(color.a, 0.2F);
                }
                AddFieldPatch.orbAnimation.get(orb).xPosition = x + oX;
                AddFieldPatch.orbAnimation.get(orb).yPosition = y + oY;
                AddFieldPatch.orbAnimation.get(orb).alpha = color.a * 255.0F;
                AddFieldPatch.orbAnimation.get(orb).update();
            } else {
                color.a = MathHelper.fadeLerpSnap(color.a, 0.0F);
            }
            return SpireReturn.Return(null);
        }
    }
}
