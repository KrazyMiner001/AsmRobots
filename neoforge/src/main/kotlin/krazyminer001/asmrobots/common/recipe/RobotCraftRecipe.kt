package krazyminer001.asmrobots.common.recipe

import com.lowdragmc.lowdraglib2.gui.texture.Icons
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.data.ScrollDisplay
import com.lowdragmc.lowdraglib2.gui.ui.data.ScrollerMode
import com.lowdragmc.lowdraglib2.gui.ui.element
import com.lowdragmc.lowdraglib2.gui.ui.elements.itemSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.scrollerView
import com.lowdragmc.lowdraglib2.gui.ui.elements.withItem
import com.lowdragmc.lowdraglib2.integration.xei.IngredientIO
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.PrimitiveCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.vfyjxf.taffy.style.AlignItems
import dev.vfyjxf.taffy.style.FlexDirection
import dev.vfyjxf.taffy.style.TaffyDisplay
import krazyminer001.asmrobots.common.xei.XeiProvider
import krazyminer001.asmrobots.common.xei.XeiSlotHelper.xeiSlotWidget
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.PlacementInfo
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level

class RobotCraftRecipe(
    val result: ItemStackTemplate,
    val items: Map<Ingredient, Int>,
    val commonInfo: Recipe.CommonInfo,
    val bookCategory: ModRecipeBookCategories.RobotBookCategory,
    val groupName: String = ""
) : Recipe<RobotCraftInput> {

    fun calculateInputs(
        input: RobotCraftInput
    ): Map<Holder<Item>, Int>? {
        val inputItems = (0..<input.size())
            .map { input.getItem(it) }
            .groupBy { it.typeHolder() }
            .mapValues { entry -> entry.value.sumOf { it.count } }

        val inputItemsMutable = inputItems.toMutableMap()

        items.forEach { (ingredient, count) ->
            val inputs = inputItems.filter { ingredient.acceptsItem(it.key) }

            var toRemove = count
            for ((key, amount) in inputs) {
                if (toRemove > amount) {
                    inputItemsMutable[key] = 0
                    toRemove -= amount
                } else {
                    inputItemsMutable[key] = amount - toRemove
                    toRemove = 0
                    break
                }
            }

            if (toRemove != 0) {
                return null
            }
        }

        return inputItems
            .mapValues { (key, value) -> value - (inputItemsMutable[key] ?: 0) }
            .filter { (_, value) -> value > 0 }
    }

    override fun matches(
        input: RobotCraftInput,
        level: Level
    ) = calculateInputs(input) != null

    override fun assemble(input: RobotCraftInput) = result.create()

    override fun showNotification() = commonInfo.showNotification

    override fun group() = groupName

    override fun getSerializer() = ModRecipeSerializers.ROBOT_CRAFT

    override fun getType() = ModRecipeTypes.ROBOT_CRAFT

    override fun placementInfo(): PlacementInfo = PlacementInfo.create(
        items.keys.toList()
    )

    override fun recipeBookCategory() = bookCategory.bookCategory.value()

    fun createJEIModularUI() = context(XeiProvider.JEI) { createModularUI() }

    context(provider: XeiProvider)
    fun createModularUI(): ModularUI {
        val root = element({
            layout = {
                display(TaffyDisplay.FLEX)
                flexDirection(FlexDirection.COLUMN)
                gap { all(5) }
            }
        }) {
            scrollerView({
                scrollerViewStyle = {
                    mode(ScrollerMode.VERTICAL)
                    verticalScrollDisplay(ScrollDisplay.ALWAYS)
                }

                layout = {
                    alignSelf(AlignItems.CENTER)
                    height(18 * 3 + 10)
                    width(18 * 3 + 15)
                }
            }) {
                element({
                    layout = {
                        display(TaffyDisplay.GRID)
                        grid {
                            templateColumns("repeat(3, min-content)")
                            templateRows("min-content")
                        }
                        gap {
                            all(0)
                        }
                    }
                }) {
                    items.forEach { (ingredient, count) ->
                        itemSlot {
                            withItem(ItemStack(ingredient.values.first(), count))
                            xeiSlotWidget(IngredientIO.INPUT, ingredient, count)
                        }
                    }
                }
            }

            element({
                layout = {
                    alignSelf(AlignItems.CENTER)
                    height(24)
                    width(24)
                }
                style = {
                    backgroundTexture(Icons.DOWN_ARROW_NO_BAR)
                }
            })

            itemSlot({
                layout = {
                    alignSelf(AlignItems.CENTER)
                }
            }) {
                this.withItem(result.create())
                xeiSlotWidget(IngredientIO.OUTPUT)
            }
        }

        return ModularUI.of(UI.of(root))
    }

    fun createREIModularUI(): ModularUI {
        return context(XeiProvider.REI) { createModularUI() }
//        val root = element({
//            layout = {
//                display(TaffyDisplay.FLEX)
//                flexDirection(FlexDirection.COLUMN)
//                gap { all(5) }
//            }
//        }) {
//            scrollerView({
//                scrollerViewStyle = {
//                    mode(ScrollerMode.VERTICAL)
//                    verticalScrollDisplay(ScrollDisplay.ALWAYS)
//                }
//
//                layout = {
//                    alignSelf(AlignItems.CENTER)
//                    height(18 * 3 + 10)
//                    width(18 * 3 + 15)
//                }
//            }) {
//                element({
//                    layout = {
//                        display(TaffyDisplay.GRID)
//                        grid {
//                            templateColumns("repeat(3, min-content)")
//                            templateRows("min-content")
//                        }
//                        gap {
//                            all(0)
//                        }
//                    }
//                }) {
//                    items.forEach { (ingredient, count) ->
//                        itemSlot {
//                            withItem(ItemStack(ingredient.values.first(), count))
//                            reiWidget(IngredientIO.INPUT, ingredient.values)
//                        }
//                    }
//                }
//            }
//
//            element({
//                layout = {
//                    alignSelf(AlignItems.CENTER)
//                    height(24)
//                    width(24)
//                }
//                style = {
//                    backgroundTexture(Icons.DOWN_ARROW_NO_BAR)
//                }
//            })
//
//            itemSlot({
//                layout = {
//                    alignSelf(AlignItems.CENTER)
//                }
//            }) {
//                this.withItem(result.create())
//                reiWidget(IngredientIO.OUTPUT)
//            }
//        }
//
//        return ModularUI.of(UI.of(root))
    }

    companion object {
        val CODEC: MapCodec<RobotCraftRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ItemStackTemplate.MAP_CODEC.forGetter(RobotCraftRecipe::result),
                Codec.unboundedMap(Ingredient.CODEC, PrimitiveCodec.INT).fieldOf("items")
                    .forGetter(RobotCraftRecipe::items),
                Recipe.CommonInfo.MAP_CODEC.forGetter(RobotCraftRecipe::commonInfo),
                ModRecipeBookCategories.RobotBookCategory.CODEC.fieldOf("bookCategory")
                    .forGetter(RobotCraftRecipe::bookCategory),
                Codec.STRING.fieldOf("groupName").forGetter(RobotCraftRecipe::groupName),
            ).apply(instance, ::RobotCraftRecipe)
        }

        val STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, RobotCraftRecipe::result,
            ByteBufCodecs.map(
                { HashMap(it) },
                Ingredient.CONTENTS_STREAM_CODEC, ByteBufCodecs.INT
            ), RobotCraftRecipe::items,
            Recipe.CommonInfo.STREAM_CODEC, RobotCraftRecipe::commonInfo,
            ModRecipeBookCategories.RobotBookCategory.STREAM_CODEC, RobotCraftRecipe::bookCategory,
            ByteBufCodecs.STRING_UTF8, RobotCraftRecipe::groupName,
            ::RobotCraftRecipe
        )

        const val WIDTH = 89
        const val HEIGHT = 138
    }
}