package krazyminer001.asmrobots.common.recipe

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModRecipeBookCategories {
    val REGISTRY = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, AsmRobots.ID)

    val ROBOT_MISC_HOLDER = REGISTRY.register("robot_misc", ::RecipeBookCategory)
    val ROBOT_MISC by ROBOT_MISC_HOLDER

    enum class RobotBookCategory(val bookCategory: Holder<RecipeBookCategory>) : StringRepresentable {
        MISC(ROBOT_MISC_HOLDER), ;

        val registeredName = bookCategory.registeredName
        override fun getSerializedName() = registeredName

        companion object {
            val CODEC = StringRepresentable.EnumCodec(
                RobotBookCategory.entries.toTypedArray()
            ) { name -> RobotBookCategory.entries.first { it.registeredName == name } }

            val STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC)
        }
    }
}