package gmreskin.skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.anm2player.AnimatedActor;
import gmreskin.GMReskin;
import gmreskin.annotations.CustomSkinRenderer;
import gmreskin.annotations.CustomSkinRenders;
import gmreskin.patches.AbstractMonsterPatch;
import gmreskin.skins.characters.GeneralCharacterSkinRenderer;
import gmreskin.skins.monsters.GeneralMonsterSkinRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public abstract class SkinRenderer implements Disposable {
    protected AbstractCreature owner;

    private boolean hasSkin;

    protected AnimatedActor animation;

    public SkinRenderer(String skinPath) {
        try (InputStream ignored = Gdx.files.internal(skinPath).read()) {
            this.animation = new AnimatedActor(skinPath);
            this.animation.setCurAnimation("idle");
            hasSkin = true;
        } catch (GdxRuntimeException | IOException e) {
            hasSkin = false;
        }
    }

    public abstract void update();

    public abstract void render(SpriteBatch sb);

    public void onDamaged(int damageAmount) {

    }

    public void onUseCard(AbstractCard card, AbstractCreature target) {

    }

    public void setOwner(AbstractCreature owner) {
        this.owner = owner;
    }

    public boolean isSkinLoaded() {
        return hasSkin;
    }

    public void onChangeState(String stateName) {

    }

    public AnimatedActor getAnimation() {
        return this.animation;
    }

    @Override
    public void dispose() {
        if (this.animation != null) {
            this.animation.dispose();
        }
    }

    public static <T extends AbstractCreature> SkinRenderer getSkinRenderer(Class<T> cls, int index) {
        if (cls.isAnnotationPresent(CustomSkinRenders.class)) {
            CustomSkinRenders customSkinRenders = cls.getAnnotation(CustomSkinRenders.class);
            try {
                CustomSkinRenderer skinRenderer = null;
                for (CustomSkinRenderer skinRenderer1 : customSkinRenders.value()) {
                    if (skinRenderer1.index() == index) {
                        skinRenderer = skinRenderer1;
                        break;
                    }
                }
                if (skinRenderer != null) return skinRenderer.skinCls().getConstructor(String.class).newInstance(skinRenderer.anm2Path());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else if (cls.isAnnotationPresent(CustomSkinRenderer.class)) {
            try {
                CustomSkinRenderer skinRenderer = cls.getAnnotation(CustomSkinRenderer.class);
                if (skinRenderer.index() == index) {
                    return skinRenderer.skinCls().getConstructor(String.class).newInstance(skinRenderer.anm2Path());
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (index >= 0) {
            if (AbstractMonster.class.isAssignableFrom(cls)) {
                return new GeneralMonsterSkinRenderer(GMReskin.getResourcePath("skins/monsters/" + cls.getSimpleName() + ".xml"));
            } else if (AbstractPlayer.class.isAssignableFrom(cls)) {
                if (Loader.isModLoaded("CaffeInSpire") && isOriginalCharacter(cls)) {
                    return new GeneralCharacterSkinRenderer(GMReskin.getResourcePath("skins/characters/" + cls.getSimpleName() + "Caffe.xml"));
                } else {
                    return new GeneralCharacterSkinRenderer(GMReskin.getResourcePath("skins/characters/" + cls.getSimpleName() + ".xml"));
                }
            }
        }
        return null;
    }

    private static boolean isOriginalCharacter(Class<?> playerCls) {
        return Ironclad.class.isAssignableFrom(playerCls) || TheSilent.class.isAssignableFrom(playerCls) || Defect.class.isAssignableFrom(playerCls) || Watcher.class.isAssignableFrom(playerCls);
    }

    public static <T extends AbstractCreature> SkinRenderer getSkinRenderer(Class<T> cls) {
        return getSkinRenderer(cls, 0);
    }

    public static SkinRenderer getSkinRenderer(AbstractCreature creature) {
        return AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(creature);
    }
}
