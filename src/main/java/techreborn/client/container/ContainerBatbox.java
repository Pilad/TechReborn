package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.generator.TileGenerator;
import techreborn.tiles.storage.TileBatBox;

public class ContainerBatbox extends RebornContainer {

    EntityPlayer player;

    TileBatBox tile;

    public int burnTime = 0;
    public int totalBurnTime = 0;
    public int energy;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int tickTime;

    public ContainerBatbox(TileBatBox tile, EntityPlayer player) {
        super();
        this.tile = tile;
        this.player = player;


        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9
                        + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
                    142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (this.energy != (int) tile.getEnergy()) {
                icrafting.sendProgressBarUpdate(this, 2, (int) tile.getEnergy());
            }
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting crafting) {
        super.onCraftGuiOpened(crafting);
        crafting.sendProgressBarUpdate(this, 2, (int) tile.getEnergy());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value) {
        if (id == 0) {
            this.burnTime = value;
        } else if (id == 1) {
            this.totalBurnTime = value;
        } else if (id == 2) {
            this.energy = value;
        }
        this.tile.setEnergy(energy);
    }

    public int getScaledBurnTime(int i) {
        return (int) (((float) burnTime / (float) totalBurnTime) * i);
    }
}