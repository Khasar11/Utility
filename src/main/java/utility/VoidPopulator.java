package main.java.utility;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class VoidPopulator extends BlockPopulator {

	Block b;

	@Override
	public void populate(World arg0, Random arg1, Chunk arg2) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 255; y++) {
					b = arg2.getBlock(x, y, z);
					b.setBiome(Biome.THE_VOID);
				}
			}
		}

	}

}
