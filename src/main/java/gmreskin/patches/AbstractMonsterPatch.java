package gmreskin.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import gmreskin.GMReskin;
import gmreskin.skins.SkinRenderer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AbstractMonsterPatch {
    @SpirePatch(
            clz = AbstractCreature.class,
            method = SpirePatch.CLASS
    )
    public static class AddFieldPatch {
        public static SpireField<SkinRenderer> skinRenderer = new SpireField<>(() -> null);
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    String.class,
                    String.class,
                    int.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    String.class,
                    float.class,
                    float.class,
                    boolean.class
            }
    )
    public static class PatchConstructor {
        public static void Postfix(AbstractMonster m, String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
            int skinIndex = GMReskin.getSkinIndex(m.getClass());
            AddFieldPatch.skinRenderer.set(m, SkinRenderer.getSkinRenderer(m.getClass(), skinIndex));
            SkinRenderer skinRenderer = AddFieldPatch.skinRenderer.get(m);
            if (skinRenderer != null && skinRenderer.isSkinLoaded()) {
                skinRenderer.setOwner(m);
                List<Disposable> disposables = ReflectionHacks.getPrivate(m, AbstractMonster.class, "disposables");
                disposables.add(skinRenderer);
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static void Prefix(AbstractMonster m) {
            SkinRenderer skinRenderer = AddFieldPatch.skinRenderer.get(m);
            if (skinRenderer != null && skinRenderer.isSkinLoaded()) {
                skinRenderer.update();
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "render"
    )
    public static class PatchRender {
        public static SpireReturn<Void> Prefix(AbstractMonster m, SpriteBatch sb) {
            SkinRenderer skinRenderer = AddFieldPatch.skinRenderer.get(m);
            if (skinRenderer == null || !skinRenderer.isSkinLoaded()) {
                return SpireReturn.Continue();
            }
            if (!m.isDead && !m.escaped) {
                skinRenderer.render(sb);
                sb.setShader(null);
                if (!m.isDeadOrEscaped() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic("Runic Dome") && m.intent != AbstractMonster.Intent.NONE && !Settings.hideCombatElements) {
                    try {
                        ReflectionHacks.getCachedMethod(AbstractMonster.class, "renderIntentVfxBehind", SpriteBatch.class).invoke(m, sb);
                        ReflectionHacks.getCachedMethod(AbstractMonster.class, "renderIntent", SpriteBatch.class).invoke(m, sb);
                        ReflectionHacks.getCachedMethod(AbstractMonster.class, "renderIntentVfxAfter", SpriteBatch.class).invoke(m, sb);
                        ReflectionHacks.getCachedMethod(AbstractMonster.class, "renderDamageRange", SpriteBatch.class).invoke(m, sb);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                m.hb.render(sb);
                m.intentHb.render(sb);
                m.healthHb.render(sb);
            }

            if (!AbstractDungeon.player.isDead) {
                m.renderHealth(sb);
                try {
                    ReflectionHacks.getCachedMethod(AbstractMonster.class, "renderName", SpriteBatch.class).invoke(m, sb);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "dispose"
    )
    public static class PatchDispose {
        public static void Postfix(AbstractMonster m) {
            List<Disposable> disposables = ReflectionHacks.getPrivate(m, AbstractMonster.class, "disposables");
            if (disposables != null) {
                disposables.clear();
            }
        }
    }

    @SpirePatch(
            clz = ChangeStateAction.class,
            method = "update"
    )
    public static class PatchChangeState {
        public static void Prefix(ChangeStateAction action) {
            if (!(boolean)ReflectionHacks.getPrivate(action, ChangeStateAction.class, "called")) {
                AbstractMonster m = ReflectionHacks.getPrivate(action, ChangeStateAction.class, "m");
                SkinRenderer renderer = AddFieldPatch.skinRenderer.get(m);
                if (renderer != null && renderer.isSkinLoaded()) {
                    renderer.onChangeState(ReflectionHacks.getPrivate(action, ChangeStateAction.class, "stateName"));
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "useStaggerAnimation"
    )
    public static class PatchUseStaggerAnimation {
        public static void Prefix(AbstractCreature creature) {
            SkinRenderer skinRenderer = AddFieldPatch.skinRenderer.get(creature);
            if (skinRenderer != null && skinRenderer.isSkinLoaded()) {
                skinRenderer.onDamaged(creature.lastDamageTaken);
            }
        }
    }
}
