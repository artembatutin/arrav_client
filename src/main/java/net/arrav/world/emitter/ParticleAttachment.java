package net.arrav.world.emitter;

import java.util.*;

public class ParticleAttachment {

	private static final Map<Integer, int[][]> attachments = new HashMap<>();

	public static int[][] getAttachments(int model) {
        return attachments.get(model);
    }

	static {
		//Completionist cape
		attachments.put(65297, new int[][] { { 494, 0 }, { 488, 0 }, { 485, 0 }, { 476, 0 }, { 482, 0 }, { 479, 0 }, { 491, 0 } });
		attachments.put(65316, new int[][] { { 494, 0 }, { 488, 0 }, { 485, 0 }, { 476, 0 }, { 482, 0 }, { 479, 0 }, { 491, 0 } });

		//Trimmed Completionist Cape
		attachments.put(65295, new int[][] { { 494, 1 }, { 488, 1 }, { 485, 1 }, { 476, 1 }, { 482, 1 }, { 479, 1 }, { 491, 1 } });
		attachments.put(65328, new int[][] { { 494, 1 }, { 488, 1 }, { 485, 1 }, { 476, 1 }, { 482, 1 }, { 479, 1 }, { 491, 1 } });
		
		//Max Cape
		attachments.put(65300, new int[][] { { 113, 2 }, { 116, 2 }, { 109, 2 }, { 189, 2 }, { 100, 2 }, { 129, 2 }, { 128, 2 }, { 199, 2 }, { 191, 2 }, { 150, 2 }, { 98, 2 }, { 148, 2 } });
		attachments.put(65322, new int[][] { { 113, 2 }, { 116, 2 }, { 109, 2 }, { 189, 2 }, { 100, 2 }, { 129, 2 }, { 128, 2 }, { 199, 2 }, { 191, 2 }, { 150, 2 }, { 98, 2 }, { 148, 2 } });
	
		//Master Dung. Cape
		attachments.put(59885, new int[][] { {136, 3 }, { 125, 3 }, { 124, 3 }, { 131, 3 },  {172, 3 }, {144, 3 }, { 143, 3 }, { 142, 3 }, { 161, 3 }, { 123, 3}, { 89, 3 } });
		attachments.put(59887, new int[][] { {136, 3 }, { 125, 3 }, { 124, 3 }, { 131, 3 },  {172, 3 }, {144, 3 }, { 143, 3 }, { 142, 3 }, { 161, 3 }, { 123, 3}, { 89, 3 } });
	}
}