package gmreskin.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.monsters.beyond.SpireGrowth;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.megacrit.cardcrawl.monsters.beyond.WrithingMass;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import gmreskin.GMReskin;
import gmreskin.annotations.CustomSkinRenderer;
import gmreskin.annotations.CustomSkinRenders;
import gmreskin.skins.SkinRenderer;
import gmreskin.skins.characters.GeneralCharacterSkinRenderer;
import gmreskin.skins.monsters.*;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MonsterAddAnnotationPatch {
    @SpirePatch(
            cls = "characters.GodOfAbstract",
            method = "getStartingDeck",
            optional = true
    )
    public static class PatchGodOfAbstract {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(GeneralCharacterSkinRenderer.class, GMReskin.getResourcePath("skins/characters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            cls = "demoMod.scapegoat.characters.ScapegoatCharacter",
            method = "render",
            optional = true
    )
    public static class PatchScapegoat {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(GeneralCharacterSkinRenderer.class, GMReskin.getResourcePath("skins/characters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.stances.AbstractStance") && m.getMethodName().equals("render")) {
                        m.replace("super.render(sb);if (true) return;");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = Transient.class,
            method = "takeTurn"
    )
    public static class PatchTransient {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(TransientSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = WrithingMass.class,
            method = "takeTurn"
    )
    public static class PatchWrithingMass {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(WrithingMassSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = SpireGrowth.class,
            method = "takeTurn"
    )
    public static class PatchSpireGrowth {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(SpireGrowthSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = Healer.class,
            method = "takeTurn"
    )
    public static class PatchHealer {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(HealerSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = Centurion.class,
            method = "takeTurn"
    )
    public static class PatchCenturion {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(CenturionSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = Snecko.class,
            method = "takeTurn"
    )
    public static class PatchSnecko {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(SneckoSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = SnakePlant.class,
            method = "takeTurn"
    )
    public static class PatchSnakePlant {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(SnakePlantSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = LouseNormal.class,
            method = "takeTurn"
    )
    public static class PatchLouseNormal {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(LouseNormalSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = LouseDefensive.class,
            method = "takeTurn"
    )
    public static class PatchLouseDefensive {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(LouseDefensiveSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = Chosen.class,
            method = "takeTurn"
    )
    public static class PatchChosen {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(ChosenSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = SphericGuardian.class,
            method = "takeTurn"
    )
    public static class PatchSphericGuardian {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(SphericGuardianSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = Byrd.class,
            method = "takeTurn"
    )
    public static class PatchByrd {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(ByrdSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    @SpirePatch(
            clz = FungiBeast.class,
            method = "takeTurn"
    )
    public static class PatchFungiBeast {
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
            Annotation skin1 = setCustomSkinRenderer(FungiBeastSkinRenderer.class, GMReskin.getResourcePath("skins/monsters/" + ctClass.getSimpleName() + ".xml"), 0, constPool);
            addAnnotations(ctMethodToPatch, skin1);
        }
    }

    private static Annotation setCustomSkinRenderer(Class<? extends SkinRenderer> skinCls, String anm2Path, int index, ConstPool constPool) {
        Annotation annotation = new Annotation(CustomSkinRenderer.class.getName(), constPool);
        annotation.addMemberValue("skinCls", new ClassMemberValue(skinCls.getName(), constPool));
        annotation.addMemberValue("anm2Path", new StringMemberValue(anm2Path, constPool));
        annotation.addMemberValue("index", new IntegerMemberValue(constPool, index));
        return annotation;
    }

    private static void addAnnotations(CtBehavior ctMethodToPatch, Annotation... annotationsToAdd) {
        CtClass ctClass = ctMethodToPatch.getDeclaringClass();
        ConstPool constPool = ctMethodToPatch.getMethodInfo().getConstPool();
        AnnotationsAttribute attributes = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation skins = new Annotation(CustomSkinRenders.class.getName(), constPool);
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
        AnnotationMemberValue[] annotationMemberValues = new AnnotationMemberValue[annotationsToAdd.length];
        for (int i=0;i<annotationsToAdd.length;i++) {
            annotationMemberValues[i] = new AnnotationMemberValue(annotationsToAdd[i], constPool);
        }
        arrayMemberValue.setValue(annotationMemberValues);
        skins.addMemberValue("value", arrayMemberValue);
        attributes.addAnnotation(skins);
        ctClass.getClassFile().addAttribute(attributes);
    }
}
