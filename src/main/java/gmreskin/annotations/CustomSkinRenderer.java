package gmreskin.annotations;

import gmreskin.skins.SkinRenderer;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CustomSkinRenders.class)
public @interface CustomSkinRenderer {
    Class<? extends SkinRenderer> skinCls();

    String anm2Path();

    int index();
}
