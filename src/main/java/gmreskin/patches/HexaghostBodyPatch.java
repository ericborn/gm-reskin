package gmreskin.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostBody;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostOrb;

import java.util.ArrayList;

public class HexaghostBodyPatch {
    @SpirePatch(
            clz = HexaghostBody.class,
            method = "render"
    )
    public static class PatchRender {
        public static SpireReturn<Void> Prefix(HexaghostBody body, SpriteBatch sb) {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
            clz = HexaghostBody.class,
            method = "dispose"
    )
    public static class PatchDispose {
        public static void Postfix(HexaghostBody body) {
            ArrayList<HexaghostOrb> orbs = ReflectionHacks.getPrivate(ReflectionHacks.getPrivate(body, HexaghostBody.class, "m"), Hexaghost.class, "orbs");
            for (HexaghostOrb orb : orbs) {
                HexaghostOrbPatch.AddFieldPatch.orbAnimation.get(orb).dispose();
            }
        }
    }
}
