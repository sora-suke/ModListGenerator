package com.sorasuke.modlistgenerator;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

@Mod(
        modid = ModListGenerator.MODID,
        name = ModListGenerator.MODNAME,
        version = ModListGenerator.VERSION,
        acceptedMinecraftVersions  = ModListGenerator.MOD_ACCEPTED_MC_VERSIONS
)
public class ModListGenerator {

    public static final String MODID = "modlistgenerator";
    public static final String MODNAME = "ModListGenerator";
    public static final String VERSION = "1.3";
    public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.10.2,1.12]";

    public static String dirName = "modlist";
    public static String fileName = "list";
    public static boolean enableDetailsModMetadata = false;
    public static boolean endd;
    public static boolean genAsHTML;


    @Mod.Metadata
    public static ModMetadata modMetadata;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        loadMetadata(this.modMetadata);
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        try {
            config.load();
            dirName = config.get("GENERAL", "Directory name", "modlist", "Directory where Mod list is generated").getString();
            fileName = config.get("GENERAL", "File name", "list", "File name of Mod list").getString();
            enableDetailsModMetadata = config.get("GENERAL", "Enable details Mod metadata", false, "If it's true, this Mod writes authors, url and description.").getBoolean();
            //genAsHTML = config.get("GENERAL", "Generate as HTML", false, "If it's true, the Mod list will be generated as HTML.").getBoolean();


        } catch (Exception e) {
        } finally {
            config.save();
        }
        endd = enableDetailsModMetadata;
    }

    @EventHandler
    public void load(FMLPostInitializationEvent event) {

        List<ModContainer> mods = Loader.instance().getModList();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ModListGenerator Version" + this.VERSION + "\n");
        stringBuffer.append(new Date().toString() + "\n");
        stringBuffer.append(mods.size() + " mods loaded." + "\n");
        stringBuffer.append("ModID, ModName, Version" + (endd ? ", Author list, URL, Description" : "") + "\n");

        for(ModContainer mod : mods){
            String s;
            ModMetadata meta = mod.getMetadata();
            s = meta.modId + ", " + mod.getName() + ", " + meta.version + (endd ? (", " + meta.authorList.toString() + ", " + meta.url + ", \"" + meta.description + "\"" ) : "" ) + "\n";
            stringBuffer.append(s);
        }

        try {
            File dir = new File(dirName);
            dir.mkdir();
            File file = new File(dirName+"\\"+fileName+".txt");
            FileWriter filewriter = new FileWriter(file);
            filewriter.write(stringBuffer.toString());
            filewriter.close();
        }catch (Exception e){

        }
    }

    private void loadMetadata(ModMetadata m) {
        m.modId = MODID;
        m.name = MODNAME;
        m.version = VERSION;
        m.url = "https://minecraft.curseforge.com/projects/modlistgenerator";
        m.authorList.add("sora_suke(Twitter:@sora_suke_mc)");
        m.description = "This mod generates mod list!";
        m.autogenerated = false;
    }
}
