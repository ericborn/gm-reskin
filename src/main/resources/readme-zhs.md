# Reskin the Spire

## 简介

原本是GM_Whitose委托我做的一个换皮mod，结果一时上头，写成了一个换皮框架

应该还算是比较容易使用的框架

## 使用说明

### 操作步骤

#### 编写皮肤渲染类

需要继承SkinRenderer抽象类，其中update和render方法是必须实现的，其他方法可按需实现

它们的触发时机将在稍后进行说明

例如，我们为虱虫编写一个皮肤：

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

**皮肤渲染类的构造方法必须是有且仅有一个String类型的参数，用于传入皮肤文件所在路径。**

这个框架使用Anm2Player作为动画播放器(已上架创意工坊)，如果你有制作过以撒的mod，那么你会对这个动画播放器感到非常亲切

因为它就是从以撒的结合中移植过来的，直接用以撒自带的动画编辑器就能制作动画，上手难度比骨骼动画小

#### 将皮肤渲染类注册到框架

本框架使用注解来注册皮肤

例如，将上面编写的皮肤跟虱虫绑定并注册：
    
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
        ... //虱虫的代码
    }
    
没错，同一种生物可以为其注册多种皮肤！

这就是使用本框架为敌人/角色添加皮肤的全部步骤

希望这个框架在你制作皮肤时能帮你少写一点代码

(GM画的也很棒，也是本mod出现的一个动机)

### 常用方法、接口触发时机

#### 常用方法

    GMReskin.getSkinIndex(Class<? extends AbstractCreature> cls)

获得指定种类生物当前使用的皮肤下标(下标不能小于0，否则会视为0)

    GMReskin.setSkinIndex(Class<? extends AbstractCreature> cls, int index)

将指定种类生物当前使用的皮肤下标设为`index`
。不会影响已有的生物，之后生成的该种生物会使用此皮肤

    GMReskin.refreshSkin(AbstractCreature creature)

立刻刷新这个生物使用的皮肤，使用当前设置的皮肤

    GMReskin.disableSkin(Class<? extends AbstractCreature> cls)

停用指定种类生物的皮肤。同样不会不会影响已有的生物，之后生成的该种生物不会渲染皮肤

    GMReskin.saveSkinIndexInfo()

保存上述方法对皮肤下标信息的修改到硬盘

    SkinRenderer.getSkinRenderer(Class<T> cls, int index)

为指定种类生物生成一个下标为`index`的皮肤

    SkinRenderer.getSkinRenderer(Class<T> cls)

为指定种类生物生成一个下标为`0`的皮肤

    SkinRenderer.getSkinRenderer(AbstractCreature creature)

获得当前生物使用的皮肤

#### 触发时机

    onDamaged(int damageAmount)

当这个生物受到未被格挡的伤害时，触发此方法，`damageAmount`是未被格挡的伤害点数，不会超过其当前生命值

    onUseCard(AbstractCard card, AbstractCreature target)

当玩家打出一张牌时触发，会同时触发玩家和所有敌人身上的皮肤

    onChangeState(String stateName)

当敌人使用ChangeStateAction改变当前使用的动画时触发