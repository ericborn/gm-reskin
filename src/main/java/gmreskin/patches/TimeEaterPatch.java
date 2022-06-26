package gmreskin.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.Watcher;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import gmreskin.skins.SkinRenderer;
import gmreskin.skins.monsters.TimeEaterSkinRenderer;

public class TimeEaterPatch {
    @SpirePatch(
            clz = TimeEater.class,
            method = "takeTurn"
    )
    public static class PatchTakeTurn {
        public static void Prefix(TimeEater timeEater) {
            boolean firstTurn = ReflectionHacks.getPrivate(timeEater, TimeEater.class, "firstTurn");
            if (firstTurn) {
                if (AbstractDungeon.player instanceof Watcher) {
                    TimeEaterSkinRenderer skinRenderer = (TimeEaterSkinRenderer) SkinRenderer.getSkinRenderer(timeEater);
                    skinRenderer.getAnimation().setCurAnimation("watcher");
                    skinRenderer.setDefaultAnimation("watcher");
                }
            }
        }

        public static void Postfix(TimeEater timeEater) {
            if (timeEater.nextMove == 5) {
                TimeEaterSkinRenderer skinRenderer = (TimeEaterSkinRenderer) SkinRenderer.getSkinRenderer(timeEater);
                skinRenderer.getAnimation().setCurAnimation("foolish");
                final String defaultAnimation = skinRenderer.getDefaultAnimation();
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        skinRenderer.getAnimation().setCurAnimation(defaultAnimation);
                        isDone = true;
                    }
                });
            }
        }
    }
}
