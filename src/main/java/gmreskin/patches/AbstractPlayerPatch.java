package gmreskin.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import gmreskin.GMReskin;
import gmreskin.skins.SkinRenderer;

public class AbstractPlayerPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    String.class,
                    AbstractPlayer.PlayerClass.class
            }
    )
    public static class PatchConstructor {
        public static void Prefix(AbstractPlayer p, String name, AbstractPlayer.PlayerClass setClass) {
            int skinIndex = GMReskin.getSkinIndex(p.getClass());
            AbstractMonsterPatch.AddFieldPatch.skinRenderer.set(p, SkinRenderer.getSkinRenderer(p.getClass(), skinIndex));
            SkinRenderer skinRenderer = AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(p);
            if (skinRenderer != null && skinRenderer.isSkinLoaded()) {
                skinRenderer.setOwner(p);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "dispose"
    )
    public static class PatchDispose {
        public static void Postfix(AbstractPlayer p) {
            SkinRenderer skinRenderer = AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(p);
            if (skinRenderer != null) {
                skinRenderer.dispose();
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static void Prefix(AbstractPlayer p) {
            SkinRenderer skinRenderer = AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(p);
            if (skinRenderer != null && skinRenderer.isSkinLoaded()) {
                skinRenderer.update();
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "render"
    )
    public static class PatchRender {
        public static SpireReturn<Void> Prefix(AbstractPlayer p, SpriteBatch sb) {
            SkinRenderer skinRenderer = AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(p);
            if (skinRenderer == null || !skinRenderer.isSkinLoaded()) {
                return SpireReturn.Continue();
            }
            ShaderProgram shaderProgram = sb.getShader();
            sb.setShader(null);
            p.stance.render(sb);
            if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !p.isDead) {
                p.renderHealth(sb);
                if (!p.orbs.isEmpty()) {
                    for (AbstractOrb o : p.orbs) {
                        o.render(sb);
                    }
                }
            }

            if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
                sb.setShader(shaderProgram);
                if (!(boolean) ReflectionHacks.getPrivate(p, AbstractPlayer.class, "renderCorpse")) {
                    skinRenderer.render(sb);
                } else {
                    sb.setColor(Color.WHITE);
                    sb.draw(p.img, p.drawX - (float)p.img.getWidth() * Settings.scale / 2.0F + p.animX, p.drawY, (float)p.img.getWidth() * Settings.scale, (float)p.img.getHeight() * Settings.scale, 0, 0, p.img.getWidth(), p.img.getHeight(), p.flipHorizontal, p.flipVertical);
                }
                sb.setShader(null);
                p.hb.render(sb);
                p.healthHb.render(sb);
            } else {
                sb.setColor(Color.WHITE);
                p.renderShoulderImg(sb);
            }
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    AbstractCard.class,
                    AbstractCreature.class
            }
    )
    public static class PatchUseCardAction {
        public static void Postfix(UseCardAction action, AbstractCard card, AbstractCreature target) {
            if (!card.dontTriggerOnUseCard) {
                SkinRenderer skinRenderer = AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(AbstractDungeon.player);
                if (skinRenderer != null && skinRenderer.isSkinLoaded()) {
                    skinRenderer.onUseCard(card, target);
                }
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    skinRenderer = AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(monster);
                    if (skinRenderer != null && skinRenderer.isSkinLoaded()) {
                        skinRenderer.onUseCard(card, target);
                    }
                }
            }
        }
    }
}
