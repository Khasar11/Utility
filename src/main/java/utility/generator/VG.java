package main.java.utility.generator;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class VG extends ChunkGenerator {
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		chunk.setBlock(1, 1, 1, Material.AIR);
		
		return chunk;
	}
	
	@Override
	public java.util.List<BlockPopulator> getDefaultPopulators(World world) {
	    return (java.util.List<BlockPopulator>) Arrays.asList((BlockPopulator)new VoidPopulator());
	}
}
