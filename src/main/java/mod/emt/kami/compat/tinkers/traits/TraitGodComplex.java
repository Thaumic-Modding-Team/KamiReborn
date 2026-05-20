package mod.emt.kami.compat.tinkers.traits;

import mod.emt.kami.config.ConfigHandlerKami;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;

public class TraitGodComplex extends AbstractTrait {
    public static final String TAG_UNBREAKABLE = "Unbreakable";

    public TraitGodComplex() {
        super("kami_god_complex", 0xFF910C);
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);

        int partsFound = getParts(rootCompound);
        if (partsFound >= ConfigHandlerKami.integrations.godComplexParts) {
            rootCompound.setBoolean(TAG_UNBREAKABLE, true);
        }
    }

    private int getParts(NBTTagCompound root) {
        int count = 0;
        NBTTagList materialList = TagUtil.getBaseMaterialsTagList(root);

        for (int i = 0; i < materialList.tagCount(); i++) {
            String matId = materialList.getStringTagAt(i);
            Material mat = TinkerRegistry.getMaterial(matId);

            if (mat != null && mat.hasTrait(this.identifier, null)) {
                count++;
            }
        }

        return count;
    }
}
