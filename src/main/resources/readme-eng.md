# Reskin the Spire

## Introduction

A reskin framework, easy to use.

Also a reskin mod.

## Get Started

### Steps

#### Write skin rendering classes

Needs to extend `SkinRenderer` class, `update()` and `render(SpriteBatch sb)` method are required to implement, other methods can be implemented on demand.

The timing of triggering these methods will be described later.

For example, I wrote a skin for louse:

    package gmreskin.skins.monsters;
    
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import gmreskin.skins.SkinRenderer;
    
    public class LouseNormalSkinRenderer extends SkinRenderer {
        public LouseNormalSkinRenderer(String skinPath) {
            super(skinPath);
            if (this.isSkinLoaded()) this.animation.scale = 1.0F;
        }
    
        @Override
        public void update() {
            this.animation.xPosition = this.owner.hb.cX + this.owner.animX;
            this.animation.yPosition = this.owner.hb.y + this.owner.animY;
            this.animation.flipX = this.owner.flipHorizontal;
            this.animation.flipY = this.owner.flipVertical;
            this.animation.alpha = this.owner.tint.color.a * 255.0F;
            this.animation.update();
        }
    
        @Override
        public void render(SpriteBatch sb) {
            this.animation.render(sb);
        }
    
        @Override
        public void onChangeState(String stateName) {
            switch (stateName) {
                case "CLOSED":
                    this.animation.setCurAnimation("curl");
                    break;
                case "OPEN":
                    this.animation.setCurAnimation("idle");
                    break;
                case "REAR_IDLE":
                    this.animation.setCurAnimation("idle");
                    break;
                default:
                    this.animation.setCurAnimation("idle");
            }
        }
    }

**The skin rendering class must be constructed with one and only one String parameter for the path to the incoming skin file.**

Anm2Player is an animation player porting from The Binding of Isaac.

You can find it on workshop.

#### Register the skin rendering class to framework

This framework uses annotations to register skin

For example, bind and register the skin written above with louse:
    
    @CustomSkinRenderer(
        skinCls = LouseNormalSkinRenderer.class,
        anm2Path = "GMImages/skins/monsters/LouseNormal1.xml",
        index = 0
    )
    @CustomSkinRenderer(
        skinCls = AnotherLouseNormalSkinRenderer.class,
        anm2Path = "GMImages/skins/monsters/LouseNormal2.xml",
        index = 1
    )
    public class LouseNormal {
        ... //louses' code
    }

Yes, you can register multiple skins to the same creature!

This is all the steps to add skin to the enemy/character using this framework.

I hope this framework can help you write less code when you make skin.

### Common methods, and its trigger timing

#### Common methods

    GMReskin.getSkinIndex(Class<? extends AbstractCreature> cls)

Gets the skin index currently used by the specified creature.

    GMReskin.setSkinIndex(Class<? extends AbstractCreature> cls, int index)

Set the skin index currently used by the specified creature as `index`(The index cannot be less than 0, otherwise it will be regarded as 0).

It will not affect existing creatures, and later generated creatures will use this skin.

    GMReskin.refreshSkin(AbstractCreature creature)

Refresh the skin used by this creature immediately and use the currently set skin.

    GMReskin.disableSkin(Class<? extends AbstractCreature> cls)

Disable the skin of the specified creature. Similarly, it will not affect existing creatures, and later generated creatures will not render skin.

    GMReskin.saveSkinIndexInfo()

Save the modification of skin index information by the above method to disk.

    SkinRenderer.getSkinRenderer(Class<T> cls, int index)


Generate a skin with the index `index` for the specified creature.

    SkinRenderer.getSkinRenderer(Class<T> cls)

Generate a skin with index `0` for the specified creature

    SkinRenderer.getSkinRenderer(AbstractCreature creature)

Get the skin currently used by the creature.

#### Methods trigger timing

    onDamaged(int damageAmount)

Trigger this method when this creature receives unblocked damage. `damageAmount` is the amount of unblocked damage points, which will not exceed its current health.

    onUseCard(AbstractCard card, AbstractCreature target)

Triggered when the player plays a card, it will trigger the skin of the player and all enemies at the same time.

    onChangeState(String stateName)

Triggered when the enemy uses `ChangeStateAction` to change the currently used animation