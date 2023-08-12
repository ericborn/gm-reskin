package gmreskin.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.CollectorStakeEffect;
import gmreskin.GMReskin;
import gmreskin.skins.SkinRenderer;

public class TheCollectorPatch {
    @SpirePatch(
            clz = TheCollector.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(TheCollector collector) {
            if (GMReskin.getSkinIndex(TheCollector.class) != -1) {
                Skeleton skeleton = ReflectionHacks.getPrivate(collector, AbstractCreature.class, "skeleton");
                skeleton.setX(-99999.0F);
                skeleton.setY(-99999.0F);
            }
        }
    }

    @SpirePatch(
            clz = TheCollector.class,
            method = "takeTurn"
    )
    public static class PatchTakeTurn {
        public static void Prefix(TheCollector collector) {
            if (collector.nextMove == 4) {
                if (GMReskin.getSkinIndex(TheCollector.class) != -1) {
                    SkinRenderer.getSkinRenderer(collector).getAnimation().setCurAnimation("you_are_mine");
                }
            }
        }
    }

    @SpirePatch(
            clz = CollectorCurseEffect.class,
            method = "update"
    )
    public static class PatchCurseEffect {
        public static void Postfix(CollectorCurseEffect effect) {
            if (effect.isDone) {
                AbstractMonster collector = AbstractDungeon.getMonsters().getMonster(TheCollector.ID);
                if (collector != null && GMReskin.getSkinIndex(TheCollector.class) != -1) {
                    SkinRenderer.getSkinRenderer(collector).getAnimation().setCurAnimation("idle");
                }
            }
        }
    }

    @SpirePatch(
            clz = CollectorStakeEffect.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchCollectorStakeEffect {
        private static final Texture stake = new Texture(GMReskin.getResourcePath("skins/monsters/stake.png"));

        public static void Postfix(CollectorStakeEffect effect, float x, float y) {
            if (GMReskin.getSkinIndex(TheCollector.class) != -1) {
                ReflectionHacks.setPrivateStatic(CollectorStakeEffect.class, "img", new TextureAtlas.AtlasRegion(stake, 0, 0, 162, 264));
            } else {
                ReflectionHacks.setPrivateStatic(CollectorStakeEffect.class, "img", ImageMaster.vfxAtlas.findRegion("combat/stake"));
            }
        }
    }
}
