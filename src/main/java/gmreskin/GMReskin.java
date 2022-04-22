package gmreskin;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.characters.Watcher;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import gmreskin.patches.AbstractMonsterPatch;
import gmreskin.skins.SkinRenderer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpireInitializer
public class GMReskin implements PostInitializeSubscriber,
                                 EditStringsSubscriber {
    private static final SpireConfig skinIndexInfo;
    private boolean enableDefaultSkin = true;

    static {
        SpireConfig skinIndexInfo1;
        try {
            skinIndexInfo1 = new SpireConfig("GMReskin", "settings");
        } catch (IOException e) {
            skinIndexInfo1 = null;
            e.printStackTrace();
        }
        skinIndexInfo = skinIndexInfo1;
    }

    public static void initialize() {
        new GMReskin();
    }

    public GMReskin() {
        BaseMod.subscribe(this);
    }

    public static String makeID(String name) {
        return "GMReskin:" + name;
    }

    public static String getResourcePath(String resource) {
        return "GMImages/" + resource;
    }

    public static String getLanguageString() {
        String language;
        switch (Settings.language) {
            case ZHS:
                language = "zhs";
                break;
            default:
                language = "eng";
        }
        return language;
    }

    public static void setSkinIndex(Class<? extends AbstractCreature> cls, int index) {
        if (index < 0) index = 0;
        skinIndexInfo.setInt(cls.getName(), index);
    }

    public static int getSkinIndex(Class<? extends AbstractCreature> cls) {
        if (skinIndexInfo.has(cls.getName())) {
            return skinIndexInfo.getInt(cls.getName());
        }
        return 0;
    }

    public static void refreshSkin(AbstractCreature creature) {
        SkinRenderer oldSkinRenderer = AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(creature);
        AbstractMonsterPatch.AddFieldPatch.skinRenderer.set(creature, SkinRenderer.getSkinRenderer(creature.getClass(), getSkinIndex(creature.getClass())));
        SkinRenderer skinRenderer = AbstractMonsterPatch.AddFieldPatch.skinRenderer.get(creature);
        if (skinRenderer != null && skinRenderer.isSkinLoaded()) {
            skinRenderer.setOwner(creature);
            if (creature instanceof AbstractMonster) {
                List<Disposable> disposables = ReflectionHacks.getPrivate(creature, AbstractMonster.class, "disposables");
                List<Disposable> toRemove = new ArrayList<>();
                for (Disposable disposable : disposables) {
                    if (disposable instanceof SkinRenderer && disposable != skinRenderer) {
                        toRemove.add(disposable);
                        disposable.dispose();
                    }
                }
                disposables.removeAll(toRemove);
                disposables.add(skinRenderer);
            } else {
                oldSkinRenderer.dispose();
            }
        }
    }

    public static void disableSkin(Class<? extends AbstractCreature> cls) {
        skinIndexInfo.setInt(cls.getName(), -1);
    }

    public static void saveSkinIndexInfo() {
        try {
            skinIndexInfo.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receivePostInitialize() {
        try {
            skinIndexInfo.load();
            if (skinIndexInfo.has("enableDefaultSkin")) {
                enableDefaultSkin = skinIndexInfo.getBool("enableDefaultSkin");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ModPanel"));
        ModPanel settingsPanel = new ModPanel();
        ModLabeledToggleButton enableDefaultSkinOption = new ModLabeledToggleButton(uiStrings.TEXT[0], 350.0F, 700.0F, Color.WHITE, FontHelper.buttonLabelFont, enableDefaultSkin, settingsPanel, (me) -> {},
                (me) -> {
                    enableDefaultSkin = me.enabled;
                    if (!enableDefaultSkin) {
                        disableSkin(Ironclad.class);
                        disableSkin(TheSilent.class);
                        disableSkin(Defect.class);
                        disableSkin(Watcher.class);
                        disableSkin(LouseNormal.class);
                        disableSkin(LouseDefensive.class);
                        disableSkin(AcidSlime_L.class);
                        disableSkin(AcidSlime_M.class);
                        disableSkin(AcidSlime_S.class);
                        disableSkin(Cultist.class);
                        disableSkin(GremlinFat.class);
                        disableSkin(GremlinThief.class);
                        disableSkin(GremlinTsundere.class);
                        disableSkin(GremlinWarrior.class);
                        disableSkin(GremlinWizard.class);
                        disableSkin(JawWorm.class);
                        disableSkin(SpikeSlime_L.class);
                        disableSkin(SpikeSlime_M.class);
                        disableSkin(SpikeSlime_S.class);
                    } else {
                        setSkinIndex(Ironclad.class, 0);
                        setSkinIndex(TheSilent.class, 0);
                        setSkinIndex(Defect.class, 0);
                        setSkinIndex(Watcher.class, 0);
                        setSkinIndex(LouseNormal.class, 0);
                        setSkinIndex(LouseDefensive.class, 0);
                        setSkinIndex(AcidSlime_L.class, 0);
                        setSkinIndex(AcidSlime_M.class, 0);
                        setSkinIndex(AcidSlime_S.class, 0);
                        setSkinIndex(Cultist.class, 0);
                        setSkinIndex(GremlinFat.class, 0);
                        setSkinIndex(GremlinThief.class, 0);
                        setSkinIndex(GremlinTsundere.class, 0);
                        setSkinIndex(GremlinWarrior.class, 0);
                        setSkinIndex(GremlinWizard.class, 0);
                        setSkinIndex(JawWorm.class, 0);
                        setSkinIndex(SpikeSlime_L.class, 0);
                        setSkinIndex(SpikeSlime_M.class, 0);
                        setSkinIndex(SpikeSlime_S.class, 0);
                    }
                    skinIndexInfo.setBool("enableDefaultSkin", enableDefaultSkin);
                    saveSkinIndexInfo();
                }
        );
        settingsPanel.addUIElement(enableDefaultSkinOption);
        BaseMod.registerModBadge(ImageMaster.loadImage(getResourcePath("ui/badge.png")), "GM Reskin", "Everyone", "TODO", settingsPanel);
    }

    @Override
    public void receiveEditStrings() {
        String language = getLanguageString();
        String uiStrings = Gdx.files.internal("localization/" + language + "/GMReskin-UIStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
    }
}
