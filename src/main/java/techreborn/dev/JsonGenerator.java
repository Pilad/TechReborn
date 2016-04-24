package techreborn.dev;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameData;
import reborncore.RebornCore;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.LogHelper;
import techreborn.Core;
import techreborn.api.TechRebornAPI;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Mark on 24/04/2016.
 */

//TODO DO NOT SHIP THIS!
public class JsonGenerator {

    public void generate() throws IOException {
        File mcDir = new File(".");
        File exportFolder = new File(mcDir, "export");
        if(!exportFolder.exists()){
            exportFolder.mkdir();
        }
        File assetsFolder = new File(exportFolder, "assets");
        if(!assetsFolder.exists()){
            assetsFolder.mkdir();
        }
        File modFolder = new File(assetsFolder, "techreborn");
        if(!modFolder.exists()){
            modFolder.mkdir();
        }
        File blockstates = new File(modFolder, "blockstates");
        if(!blockstates.exists()){
            blockstates.mkdir();
        }
        File models = new File(modFolder, "models");
        if(!models.exists()){
            models.mkdir();
        }
        File blockModels = new File(models, "block");
        if(!blockModels.exists()){
            blockModels.mkdir();
        }
        File itemModles = new File(models, "item");
        if(!itemModles.exists()){
            itemModles.mkdir();
        }
        File baseJsonFiles = new File(mcDir, "basejsons");
        if(!baseJsonFiles.exists()){
            Core.logHelper.error("Could not find base jsons dir!");
            throw new FileNotFoundException();
        }
        File machineBaseFile = new File(baseJsonFiles, "machineBase.json");
        String machineBase = Files.toString(machineBaseFile, Charsets.UTF_8);
        File machineModelBaseFile = new File(baseJsonFiles, "machineModelBase.json");
        String machineModelBase = Files.toString(machineModelBaseFile, Charsets.UTF_8);
        for(Object object : RebornCore.jsonDestroyer.objectsToDestroy){
            if(object instanceof BlockMachineBase){
                BlockMachineBase base = (BlockMachineBase) object;
                String name = GameData.getBlockRegistry().getNameForObject(base).getResourcePath().replace("tile.techreborn.", "");
                File state = new File(blockstates, name + ".json");
                if(state.exists()){
                    state.delete();
                }
                String output = machineBase;
                output = output.replaceAll("%MODEL%", "techreborn:" + base.getUnlocalizedName());
                output = output.replaceAll("%OFF_TEXTURE%", base.getTextureNameFromState(base.getDefaultState(), EnumFacing.NORTH));
                output = output.replaceAll("%ON_TEXTURE%", base.getTextureNameFromState(base.getDefaultState().withProperty(BlockMachineBase.ACTIVE, true), EnumFacing.NORTH));
                try {
                    FileOutputStream is = new FileOutputStream(state);
                    OutputStreamWriter osw = new OutputStreamWriter(is);
                    Writer w = new BufferedWriter(osw);
                    w.write(output);
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File model = new File(blockModels, base.getUnlocalizedName() + ".json");
                if(model.exists()){
                    model.delete();
                }
                String modelOutput = machineModelBase;
                modelOutput = modelOutput.replaceAll("%MODEL%", base.getUnlocalizedName() );
                modelOutput = modelOutput.replaceAll("%OFF_TEXTURE%", base.getTextureNameFromState(base.getDefaultState(), EnumFacing.NORTH));
                modelOutput = modelOutput.replaceAll("%ON_TEXTURE%", base.getTextureNameFromState(base.getDefaultState().withProperty(BlockMachineBase.ACTIVE, true), EnumFacing.NORTH));
                modelOutput = modelOutput.replaceAll("%SIDE_TEXTURE%", base.getTextureNameFromState(base.getDefaultState(), EnumFacing.EAST));
                modelOutput = modelOutput.replaceAll("%TOP_TEXTURE%", base.getTextureNameFromState(base.getDefaultState(), EnumFacing.UP));
                try {
                    FileOutputStream is = new FileOutputStream(model);
                    OutputStreamWriter osw = new OutputStreamWriter(is);
                    Writer w = new BufferedWriter(osw);
                    w.write(modelOutput);
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}