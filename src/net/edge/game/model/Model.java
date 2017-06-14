package net.edge.game.model;

import net.edge.Constants;
import net.edge.od.OnDemandFetcher;
import net.edge.Config;
import net.edge.cache.unit.AnimationFrame;
import net.edge.cache.unit.SkinList;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.media.Rasterizer3D;
import net.edge.util.io.Buffer;

public final class Model extends Entity {

	private boolean force_texture;
	private boolean display_model_specific_texture;

	public static final Model model = new Model();
	private static int[] anIntArray1622 = new int[2000];
	private static int[] anIntArray1623 = new int[2000];
	private static int[] anIntArray1624 = new int[2000];
	private static byte[] anIntArray1625 = new byte[2000];
	private byte triPriGlobal;
	private byte[] texType;
	private int version;
	private int[] particleDirectionX;
	private int[] particleDirectionY;
	private int[] particleDirectionZ;
	private byte[] particleLifespanX;
	private byte[] particleLifespanY;
	private int[] particleLifespanZ;
	private int[] texturePrimaryColor;
	private int[] textureSecondaryColor;
	private static byte[][] newModelHeader;
	private static byte[][] oldModelHeader;

	public int vertexAmt;
	public int[] vertexX;
	public int[] vertexY;
	public int[] vertexZ;
	public int triAmt;
	public short[] vertexIndex3d1;
	public short[] vertexIndex3d2;
	public short[] vertexIndex3d3;
	private int[] triCol1;
	private int[] triCol2;
	private int[] triCol3;
	public byte[] triType;
	public int[] triTex;
	private short[] triTexCoord;
	public byte[] triPri;
	private byte[] triAlpha;
	public int[] triFill;
	private int priAmt;
	private int texAmt;
	private short[] texVertex1;
	private short[] texVertex2;
	private short[] texVertex3;
	public int minVertexX;
	public int maxVertexX;
	public int maxVertexZ;
	public int minVertexZ;
	public int maxHorizontalDist;
	public int maxVerticalDistDown;
	private int maxDiagonalDistUpAndDown;
	private int maxDiagonalDistUp;
	public int anInt1654;
	private int[] vertexSkin;
	private int[] triSkin;
	public int[][] anIntArrayArray1657;
	public int[][] triangleSkin;
	public boolean hoverable;
	public int[] vectorNormalX;
	public int[] vectorNormalY;
	public int[] vectorNormalZ;
	public int[] vectorNormalMagnitude;
	private static OnDemandFetcher odFetcher;
	private static boolean[] projTriClipX = new boolean[4096];
	private static boolean[] projTriClipZ = new boolean[4096];
	private static int[] projVertexX = new int[4096];
	private static int[] projVertexY = new int[4096];
	private static int[] vertex2dZ = new int[4096];
	private static int[] projVertexLocalZ = new int[4096];
	private static int[] projTexVertexX = new int[4096];
	private static int[] projTexVertexY = new int[4096];
	private static int[] projTexVertexZ = new int[4096];
	private static int[] anIntArray1671 = new int[3000];
	private static int[][] anIntArrayArray1672 = new int[1500][512];
	private static int[] anIntArray1673 = new int[12];
	private static int[][] anIntArrayArray1674 = new int[12][2000];
	private static int[] anIntArray1675 = new int[2000];
	private static int[] anIntArray1676 = new int[2000];
	private static int[] anIntArray1677 = new int[12];
	private static final int[] triReqX = new int[10];
	private static final int[] triReqY = new int[10];
	private static final int[] triReqCol = new int[10];
	private static int vertex3dDX;
	private static int vertex3dDY;
	private static int vertex3dDZ;
	public static boolean aBoolean1684;
	public static int hoverX;
	public static int hoverY;
	public static int modelHoverAmt;
	public static final long[] modelHover = new long[1000];
	public static int angleSine[];
	public static int angleCosine[];
	private static int[] hslToRgbMap;
	private static int[] shadeAmt;
	private int numUVCoords;
	private short maxDepth;
	private byte[] uvCoordVertexA;
	private byte[] uvCoordVertexB;
	private byte[] uvCoordVertexC;
	private int[] anIntArray1669;
	private float[] textureCoordU;
	private float[] textureCoordV;
	public boolean upscaled;
	//private EmitterTriangle[] emitters;
	//private MagnetVertex[] magnets;

	static {
		angleSine = Rasterizer3D.angleSine;
		angleCosine = Rasterizer3D.angleCosine;
		hslToRgbMap = Rasterizer3D.hslToRgbMap;
		shadeAmt = Rasterizer3D.lightDecay;
	}


	public static void method459(int length, OnDemandFetcher odf) {
		oldModelHeader = new byte[length][];
		newModelHeader = new byte[length][];
		odFetcher = odf;
	}

	public Model(byte[] data) {
		try {
			if(data != null && data.length > 1)
				if(usesNewHeader(data)) {
					decodeNew(data);
				} else {
					decodeOld(data);
				}
			if(!Config.def.isRETAIN_MODEL_PRECISION() && upscaled) {
				scale(32, 32, 32);
				upscaled = false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private boolean usesNewHeader(byte[] data) {
		return data[data.length - 2] == -1 && data[data.length - 1] == -1;
	}

	private void decode800(byte[] is) {
		triAmt = 0;
		triPriGlobal = (byte) 0;
		texAmt = 0;

		Buffer[] buffers = new Buffer[7];
		for(int i = 0; i < buffers.length; i++)
			buffers[i] = new Buffer(is);

		int identifier = buffers[0].getUByte();
		if(identifier != 1)
			System.out.println("Invalid model identifier: " + identifier);
		else {
			buffers[0].getSByte();
			version = buffers[0].getUByte();
			upscaled = version > 13;
			buffers[0].pos = is.length - 26;
			vertexAmt = buffers[0].getUShort();
			triAmt = buffers[0].getUShort();
			texAmt = buffers[0].getUShort();
			int footerFlags = buffers[0].getUByte();
			boolean hasFillAttributes = (footerFlags & 0x1) == 1;
			boolean hasSurfaceEffects = (footerFlags & 0x2) == 2;
			boolean hasVertexNormals = (footerFlags & 0x4) == 4;
			boolean hasManyVertices = (footerFlags & 0x10) == 16;
			boolean hasManyTriangles = (footerFlags & 0x20) == 32;
			boolean hasManyVertexNormals = (footerFlags & 0x40) == 64;
			boolean hasUVCoordinates = (footerFlags & 0x80) == 128;
			int modelPriority = buffers[0].getUByte();
			int modelAlpha = buffers[0].getUByte();
			int modelTriangleSkinValue = buffers[0].getUByte();
			int modelTexture = buffers[0].getUByte();
			int modelVertexSkins = buffers[0].getUByte();
			int modelVerticesX = buffers[0].getUShort();
			int modelVerticesY = buffers[0].getUShort();
			int modelVerticesZ = buffers[0].getUShort();
			int modelVertexPoint = buffers[0].getUShort();
			int modelTextureCoords = buffers[0].getUShort();
			int vertices = buffers[0].getUShort();
			int triangles = buffers[0].getUShort();
			if(!hasManyVertices) {
				if(modelVertexSkins == 1)
					vertices = vertexAmt;
				else
					vertices = 0;
			}
			if(!hasManyTriangles) {
				if(modelTriangleSkinValue == 1)
					triangles = triAmt;
				else
					triangles = 0;
			}
			int textureAmount = 0;
			int particleAmount = 0;
			int particleColor = 0;
			if(texAmt > 0) {
				texType = new byte[texAmt];
				buffers[0].pos = 3;
				for(int tri = 0; tri < texAmt; tri++) {
					byte renderType = texType[tri] = buffers[0].getSByte();
					if(renderType == 0)
						textureAmount++;
					if(renderType >= 1 && renderType <= 3)
						particleAmount++;
					if(renderType == 2)
						particleColor++;
				}
			}
			int pos = 3 + texAmt;
			int vertexFlagsPos = pos;
			pos += vertexAmt;
			int renderTypePos = pos;
			if(hasFillAttributes)
				pos += triAmt;
			int depthTriTypeOffset = pos;
			pos += triAmt;
			int priorityPos = pos;
			if(modelPriority == 255)
				pos += triAmt;
			int triangleSkinPos = pos;
			pos += triangles;
			int vertexSkinsPos = pos;
			pos += vertices;
			int alphaPos = pos;
			if(modelAlpha == 1)
				pos += triAmt;
			int depthTriViewspaceOffset = pos;
			pos += modelVertexPoint;
			int texturePos = pos;
			if(modelTexture == 1)
				pos += triAmt * 2;
			int textureCoordPos = pos;
			pos += modelTextureCoords;
			int colorPos = pos;
			pos += triAmt * 2;
			int vertexXOffsetPos = pos;
			pos += modelVerticesX;
			int vertexYOffsetPos = pos;
			pos += modelVerticesY;
			int vertexZOffsetPos = pos;
			pos += modelVerticesZ;
			int texturedTriangleType0Offset = pos;
			pos += textureAmount * 6;
			int texturedTriangleOffset = pos;
			pos += particleAmount * 6;
			int particleVersion = 6;
			if(version == 14)
				particleVersion = 7;
			else if(version >= 15)
				particleVersion = 9;
			int particleDirectionOffset = pos;
			pos += particleAmount * particleVersion;
			int particleXLifespanOffset = pos;
			pos += particleAmount;
			int particleYLifespanOffset = pos;
			pos += particleAmount;
			int particleZLifespanAndTextureColorOffset = pos;
			pos += particleAmount + particleColor * 2;
			int surfaceEffectsOffset = pos;
			int uvCoordPos = is.length;
			int uvCoordModPos2 = is.length;
			int texCoordUPos = is.length;
			int texCoordVPos = is.length;
			if(hasUVCoordinates) {
				Buffer uvBuffer = new Buffer(is);
				uvBuffer.pos = is.length - 26;
				uvBuffer.pos = uvBuffer.pos - is[uvBuffer.pos - 1];
				numUVCoords = uvBuffer.getUShort();
				int unknown1 = uvBuffer.getUShort();
				int unknown2 = uvBuffer.getUShort();
				uvCoordPos = surfaceEffectsOffset + unknown1;
				uvCoordModPos2 = uvCoordPos + unknown2;
				texCoordUPos = uvCoordModPos2 + vertexAmt;
				texCoordVPos = texCoordUPos + numUVCoords * 2;
			}
			vertexX = new int[vertexAmt];
			vertexY = new int[vertexAmt];
			vertexZ = new int[vertexAmt];
			vertexIndex3d1 = new short[triAmt];
			vertexIndex3d2 = new short[triAmt];
			vertexIndex3d3 = new short[triAmt];
			if(modelVertexSkins == 1)
				vertexSkin = new int[vertexAmt];
			if(hasFillAttributes)
				triType = new byte[triAmt];
			if(modelPriority == 255)
				triPri = new byte[triAmt];
			else
				triPriGlobal = (byte) modelPriority;
			if(modelAlpha == 1)
				triAlpha = new byte[triAmt];
			if(modelTriangleSkinValue == 1)
				triSkin = new int[triAmt];
			if(modelTexture == 1)
				triTex = new int[triAmt];
			if(modelTexture == 1 && (texAmt > 0 || numUVCoords > 0))
				triTexCoord = new short[triAmt];
			triFill = new int[triAmt];
			if(texAmt > 0) {
				texVertex1 = new short[texAmt];
				texVertex2 = new short[texAmt];
				texVertex3 = new short[texAmt];
				if(particleAmount > 0) {
					particleDirectionX = new int[particleAmount];
					particleDirectionY = new int[particleAmount];
					particleDirectionZ = new int[particleAmount];
					particleLifespanX = new byte[particleAmount];
					particleLifespanY = new byte[particleAmount];
					particleLifespanZ = new int[particleAmount];
				}
				if(particleColor > 0) {
					texturePrimaryColor = new int[particleColor];
					textureSecondaryColor = new int[particleColor];
				}
			}
			buffers[0].pos = vertexFlagsPos;
			buffers[1].pos = vertexXOffsetPos;
			buffers[2].pos = vertexYOffsetPos;
			buffers[3].pos = vertexZOffsetPos;
			buffers[4].pos = vertexSkinsPos;
			int vX = 0;
			int vY = 0;
			int vZ = 0;
			for(int point = 0; point < vertexAmt; point++) {
				int vertexFlags = buffers[0].getUByte();
				int vertexXOffset = 0;
				if((vertexFlags & 0x1) != 0)
					vertexXOffset = buffers[1].getSSmart();
				int vertexYOffset = 0;
				if((vertexFlags & 0x2) != 0)
					vertexYOffset = buffers[2].getSSmart();
				int vertexZOffset = 0;
				if((vertexFlags & 0x4) != 0)
					vertexZOffset = buffers[3].getSSmart();
				vertexX[point] = vX + vertexXOffset;
				vertexY[point] = vY + vertexYOffset;
				vertexZ[point] = vZ + vertexZOffset;
				vX = vertexX[point];
				vY = vertexY[point];
				vZ = vertexZ[point];
				if(modelVertexSkins == 1) {
					if(hasManyVertices)
						vertexSkin[point] = buffers[4].getUShortMinusOne();
					else {
						vertexSkin[point] = buffers[4].getUByte();
						if(vertexSkin[point] == 255)
							vertexSkin[point] = -1;
					}
				}
			}
			if(numUVCoords > 0) {
				buffers[0].pos = (uvCoordModPos2);
				buffers[1].pos = (texCoordUPos);
				buffers[2].pos = (texCoordVPos);
				anIntArray1669 = new int[vertexAmt];
				int coord = 0;
				int size = 0;
				for(; coord < vertexAmt; coord++) {
					anIntArray1669[coord] = size;
					size += buffers[0].getUByte();
				}
				uvCoordVertexA = new byte[triAmt];
				uvCoordVertexB = new byte[triAmt];
				uvCoordVertexC = new byte[triAmt];
				textureCoordU = new float[numUVCoords];
				textureCoordV = new float[numUVCoords];
				for(coord = 0; coord < numUVCoords; coord++) {
					textureCoordU[coord] = (buffers[1].getSShort() / 4096.0F);
					textureCoordV[coord] = (buffers[2].getSShort() / 4096.0F);
				}
			}
			buffers[0].pos = (colorPos);
			buffers[1].pos = (renderTypePos);
			buffers[2].pos = (priorityPos);
			buffers[3].pos = (alphaPos);
			buffers[4].pos = (triangleSkinPos);
			buffers[5].pos = (texturePos);
			buffers[6].pos = (textureCoordPos);
			for(int tri = 0; tri < triAmt; tri++) {
				triFill[tri] = (buffers[0].getUShort());
				if(hasFillAttributes)
					triType[tri] = buffers[1].getSByte();
				if(modelPriority == 255)
					triPri[tri] = buffers[2].getSByte();
				if(modelAlpha == 1)
					triAlpha[tri] = buffers[3].getSByte();
				if(modelTriangleSkinValue == 1) {
					if(hasManyTriangles)
						triSkin[tri] = buffers[4].getUShortMinusOne();
					else {
						triSkin[tri] = buffers[4].getUByte();
						if(triSkin[tri] == 255)
							triSkin[tri] = -1;
					}
				}
				if(modelTexture == 1)
					triTex[tri] = (short) ((buffers[5].getUShort()) - 1);
				if(triTexCoord != null) {
					if(triTex[tri] != -1) {
						if(version >= 16)
							triTexCoord[tri] = (short) (buffers[6].getUSmart() - 1);
						else
							triTexCoord[tri] = (short) ((buffers[6].getUByte()) - 1);
					} else
						triTexCoord[tri] = (short) -1;
				}
			}
			maxDepth = -1;
			buffers[0].pos = (depthTriViewspaceOffset);
			buffers[1].pos = (depthTriTypeOffset);
			buffers[2].pos = (uvCoordPos);
			calculateMaxDepth(buffers[0], buffers[1], buffers[2]);
			buffers[0].pos = (texturedTriangleType0Offset);
			buffers[1].pos = (texturedTriangleOffset);
			buffers[2].pos = (particleDirectionOffset);
			buffers[3].pos = (particleXLifespanOffset);
			buffers[4].pos = (particleYLifespanOffset);
			buffers[5].pos = (particleZLifespanAndTextureColorOffset);
			decodeTexturedTriangles(buffers[0], buffers[1], buffers[2], buffers[3], buffers[4], buffers[5]);
			buffers[0].pos = (surfaceEffectsOffset);
			if(hasSurfaceEffects) {
				int numFaces = buffers[0].getUByte();
				if(numFaces > 0) {
					//surfaces = new Surface[numFaces];
					for(int face = 0; face < numFaces; face++) {
						int faceId = buffers[0].getUShort();
						int point = buffers[0].getUShort();
						byte pri;
						if(modelPriority == 255)
							pri = triPri[point];
						else
							pri = (byte) modelPriority;
						/*surfaces[face] = new Surface(faceId, point,
								vertexIndex3d1[point],
								vertexIndex3d2[point],
								vertexIndex3d3[point], pri);*/
					}
				}
				int numSkins = buffers[0].getUByte();
				if(numSkins > 0) {
					//surfaceSkins = new SurfaceSkin[numSkins];
					for(int face = 0; face < numSkins; face++) {
						int skin = buffers[0].getUShort();
						int point = buffers[0].getUShort();
						//surfaceSkins[face] = new SurfaceSkin(skin, point);
					}
				}
			}
			if(hasVertexNormals) {
				int numVertexNormals = buffers[0].getUByte();
				if(numVertexNormals > 0) {
					vectorX = new int[numVertexNormals];
					vectorY = new int[numVertexNormals];
					vectorZ = new int[numVertexNormals];
					vectorMagnitude = new int[numVertexNormals];
					for(int vertex = 0; vertex < numVertexNormals; vertex++) {
						int x = buffers[0].getUShort();
						int y = buffers[0].getUShort();
						int z;
						if(hasManyVertexNormals)
							z = buffers[0].getUShortMinusOne();
						else {
							z = buffers[0].getUByte();
							if(z == 255)
								z = -1;
						}
						byte divisor = buffers[0].getSByte();
						vectorX[vertex] = x;
						vectorY[vertex] = y;
						vectorZ[vertex] = z;
						vectorMagnitude[vertex] = divisor;
					}
				}
			}
		}
	}

	private void decodeOld(byte[] is) {
		boolean has_face_type = false;
		boolean has_texture_type = false;

		Buffer[] buffers = new Buffer[5];
		for(int i = 0; i < buffers.length; i++)
			buffers[i] = new Buffer(is);

		buffers[0].pos = is.length - 18;

		vertexAmt = buffers[0].getUShort();
		triAmt = buffers[0].getUShort();
		texAmt = buffers[0].getUByte();

		int i_157_ = buffers[0].getUByte();
		int modelPriority = buffers[0].getUByte();
		int modelAlpha = buffers[0].getUByte();
		int modelTriSkins = buffers[0].getUByte();
		int modelVertexSkins = buffers[0].getUByte();
		int i_162_ = buffers[0].getUShort();
		int i_163_ = buffers[0].getUShort();
		int i_164_ = buffers[0].getUShort();
		int i_165_ = buffers[0].getUShort();
		int pos = 0;
		int i_167_ = pos;

		pos += vertexAmt;
		int i_168_ = pos;
		pos += triAmt;
		int priorityPos = pos;
		if(modelPriority == 255)
			pos += triAmt;
		int i_170_ = pos;
		if(modelTriSkins == 1)
			pos += triAmt;
		int i_171_ = pos;
		if(i_157_ == 1)
			pos += triAmt;
		int i_172_ = pos;
		if(modelVertexSkins == 1)
			pos += vertexAmt;
		int i_173_ = pos;
		if(modelAlpha == 1)
			pos += triAmt;
		int i_174_ = pos;
		pos += i_165_;
		int i_175_ = pos;
		pos += triAmt * 2;
		int i_176_ = pos;
		pos += texAmt * 6;
		int i_177_ = pos;
		pos += i_162_;
		int i_178_ = pos;
		pos += i_163_;
		int i_179_ = pos;
		vertexX = new int[vertexAmt];
		vertexY = new int[vertexAmt];
		vertexZ = new int[vertexAmt];
		vertexIndex3d1 = new short[triAmt];
		vertexIndex3d2 = new short[triAmt];
		vertexIndex3d3 = new short[triAmt];
		triFill = new int[triAmt];

		if(modelVertexSkins == 1)
			vertexSkin = new int[vertexAmt];

		if(modelTriSkins == 1)
			triSkin = new int[triAmt];

		if(modelAlpha == 1)
			triAlpha = new byte[triAmt];

		if(modelPriority != 255)
			triPriGlobal = (byte) modelPriority;
		else
			triPri = new byte[triAmt];

		if(texAmt > 0) {
			texVertex1 = new short[texAmt];
			texVertex2 = new short[texAmt];
			texVertex3 = new short[texAmt];
			texType = new byte[texAmt];
		}

		pos += i_164_;

		if(i_157_ == 1) {
			triTex = new int[triAmt];
			triType = new byte[triAmt];
			triTexCoord = new short[triAmt];
		}

		buffers[0].pos = i_167_;
		buffers[1].pos = i_177_;
		buffers[2].pos = i_178_;
		buffers[3].pos = i_179_;
		buffers[4].pos = i_172_;
		int i_180_ = 0;
		int i_181_ = 0;
		int i_182_ = 0;
		for(int i_183_ = 0; vertexAmt > i_183_; i_183_++) {
			int i_184_ = buffers[0].getUByte();
			int i_185_ = 0;
			if((i_184_ & 0x1) != 0)
				i_185_ = buffers[1].getSSmart();
			int i_186_ = 0;
			if((0x2 & i_184_) != 0)
				i_186_ = buffers[2].getSSmart();
			int i_187_ = 0;
			if((i_184_ & 0x4) != 0)
				i_187_ = buffers[3].getSSmart();
			vertexX[i_183_] = i_185_ + i_180_;
			vertexY[i_183_] = i_181_ + i_186_;
			vertexZ[i_183_] = i_182_ + i_187_;
			i_180_ = vertexX[i_183_];
			i_181_ = vertexY[i_183_];
			i_182_ = vertexZ[i_183_];
			if(modelVertexSkins == 1)
				vertexSkin[i_183_] = buffers[4].getUByte();
		}
		buffers[0].pos = i_175_;
		buffers[1].pos = i_171_;
		buffers[2].pos = priorityPos;
		buffers[3].pos = i_173_;
		buffers[4].pos = i_170_;
		for(int i_188_ = 0; (i_188_ ^ 0xffffffff) > (triAmt ^ 0xffffffff); i_188_++) {
			triFill[i_188_] = buffers[0].getUShort();
			if(i_157_ == 1) {
				int i_189_ = buffers[1].getUByte();
				if((i_189_ & 0x1) == 1) {
					has_face_type = true;
					triType[i_188_] = (byte) 1;
				} else
					triType[i_188_] = (byte) 0;
				if((i_189_ & 0x2) == 2) {
					triTexCoord[i_188_] = (short) (i_189_ >> 2);
					triTex[i_188_] = triFill[i_188_];
					triFill[i_188_] = 127;
					if(triTex[i_188_] != -1)
						has_texture_type = true;
				} else {
					triTexCoord[i_188_] = (short) -1;
					triTex[i_188_] = (short) -1;
				}
			}
			if(modelPriority == 255)
				triPri[i_188_] = buffers[2].getSByte();
			if(modelAlpha == 1)
				triAlpha[i_188_] = buffers[3].getSByte();
			if(modelTriSkins == 1)
				triSkin[i_188_] = buffers[4].getUByte();
		}
		int used_vertexAmt = -1;
		buffers[0].pos = i_174_;
		buffers[1].pos = i_168_;
		short i_190_ = 0;
		short i_191_ = 0;
		short i_192_ = 0;
		int i_193_ = 0;
		for(int i_194_ = 0; triAmt > i_194_; i_194_++) {
			int i_195_ = buffers[1].getUByte();
			if(i_195_ == 1) {
				i_190_ = (short) (i_193_ + buffers[0].getSSmart());
				i_193_ = i_190_;
				i_191_ = (short) (i_193_ + buffers[0].getSSmart());
				i_193_ = i_191_;
				i_192_ = (short) (buffers[0].getSSmart() + i_193_);
				i_193_ = i_192_;
				vertexIndex3d1[i_194_] = i_190_;
				vertexIndex3d2[i_194_] = i_191_;
				vertexIndex3d3[i_194_] = i_192_;
				if(used_vertexAmt < i_190_)
					used_vertexAmt = i_190_;
				if((i_191_ ^ 0xffffffff) < (used_vertexAmt ^ 0xffffffff))
					used_vertexAmt = i_191_;
				if(i_192_ > used_vertexAmt)
					used_vertexAmt = i_192_;
			}
			if(i_195_ == 2) {
				i_191_ = i_192_;
				i_192_ = (short) (i_193_ + buffers[0].getSSmart());
				i_193_ = i_192_;
				vertexIndex3d1[i_194_] = i_190_;
				vertexIndex3d2[i_194_] = i_191_;
				vertexIndex3d3[i_194_] = i_192_;
				if(used_vertexAmt < i_192_)
					used_vertexAmt = i_192_;
			}
			if(i_195_ == 3) {
				i_190_ = i_192_;
				i_192_ = (short) (buffers[0].getSSmart() + i_193_);
				i_193_ = i_192_;
				vertexIndex3d1[i_194_] = i_190_;
				vertexIndex3d2[i_194_] = i_191_;
				vertexIndex3d3[i_194_] = i_192_;
				if((i_192_ ^ 0xffffffff) < (used_vertexAmt ^ 0xffffffff))
					used_vertexAmt = i_192_;
			}
			if(i_195_ == 4) {
				short i_196_ = i_190_;
				i_190_ = i_191_;
				i_191_ = i_196_;
				i_192_ = (short) (buffers[0].getSSmart() + i_193_);
				i_193_ = i_192_;
				vertexIndex3d1[i_194_] = i_190_;
				vertexIndex3d2[i_194_] = i_191_;
				vertexIndex3d3[i_194_] = i_192_;
				if((~i_192_) < (~used_vertexAmt))
					used_vertexAmt = i_192_;
			}
		}
		buffers[0].pos = i_176_;
		used_vertexAmt++;
		for(int i_197_ = 0; texAmt > i_197_; i_197_++) {
			texType[i_197_] = (byte) 0;
			texVertex1[i_197_] = (short) buffers[0].getUShort();
			texVertex2[i_197_] = (short) buffers[0].getUShort();
			texVertex3[i_197_] = (short) buffers[0].getUShort();
		}
		if(triTexCoord != null) {
			boolean textured = false;
			for(int i_199_ = 0; i_199_ < triAmt; i_199_++) {
				int i_200_ = 0xffff & triTexCoord[i_199_];
				if(i_200_ != 0xffff) {
					if(((texVertex1[i_200_] & 0xffff) == vertexIndex3d1[i_199_]) && ((0xffff & texVertex2[i_200_]) == vertexIndex3d2[i_199_]) && vertexIndex3d3[i_199_] == (texVertex3[i_200_] & 0xffff))
						triTexCoord[i_199_] = (byte) -1;
					else
						textured = true;
				}
			}
			if(!textured)
				triTexCoord = null;
		}
		if(!has_texture_type)
			triTex = null;
		if(!has_face_type)
			triType = null;
		int s = 4 << 7;
	}

	private void decodeNew(byte[] data) {
		try {
			Buffer[] buffers = new Buffer[7];
			for(int i = 0; i < buffers.length; i++)
				buffers[i] = new Buffer(data);

			buffers[0].pos = data.length - 23;
			vertexAmt = buffers[0].getUShort();
			triAmt = buffers[0].getUShort();
			texAmt = buffers[0].getUByte();
			int i_50_ = buffers[0].getUByte();
			boolean have_mode = (i_50_ & 0x1) == 1;
			boolean bool_51_ = (0x2 & i_50_) == 2;
			boolean bool_52_ = (0x4 & i_50_) == 4;
			boolean is525 = (0x8 & i_50_) == 8;
			if(is525) {
				buffers[0].pos -= 7;
				version = buffers[0].getUByte();
				upscaled = version > 13;
				buffers[0].pos += 6;
			}
			int i_54_ = buffers[0].getUByte();
			int have_alpha = buffers[0].getUByte();
			int have_tri_skins = buffers[0].getUByte();
			int have_tex = buffers[0].getUByte();
			int i_58_ = buffers[0].getUByte();
			int i_59_ = buffers[0].getUShort();
			int i_60_ = buffers[0].getUShort();
			int i_61_ = buffers[0].getUShort();
			int i_62_ = buffers[0].getUShort();
			int i_63_ = buffers[0].getUShort();
			int numTexTriangles = 0;
			int i_65_ = 0;
			int i_66_ = 0;
			if(texAmt > 0) {
				texType = new byte[texAmt];
				buffers[0].pos = 0;
				for(int i_67_ = 0; texAmt > i_67_; i_67_++) {
					byte i_68_ = texType[i_67_] = buffers[0].getSByte();
					if(i_68_ >= 1 && i_68_ <= 3)
						i_65_++;
					if(i_68_ == 0)
						numTexTriangles++;
					if(i_68_ == 2)
						i_66_++;
				}
			}
			int data_pos = texAmt;
			int vertex_enc_offset = data_pos;
			data_pos += vertexAmt;
			int tri_mode_start = data_pos;
			if(have_mode)
				data_pos += triAmt;
			int tri_idx_offset = data_pos;
			data_pos += triAmt;
			int i_73_ = data_pos;
			if(i_54_ == 255)
				data_pos += triAmt;
			int tri_skin_start = data_pos;
			if(have_tri_skins == 1)
				data_pos += triAmt;
			int vertex_skin_offset = data_pos;
			if(i_58_ == 1)
				data_pos += vertexAmt;
			int tri_alpha_start = data_pos;
			if(have_alpha == 1)
				data_pos += triAmt;
			int tri_enc_offset = data_pos;
			data_pos += i_62_;
			int tri_tex_start = data_pos;
			if(have_tex == 1)
				data_pos += triAmt * 2;
			int i_79_ = data_pos;
			data_pos += i_63_;
			int tri_colour_start = data_pos;
			data_pos += 2 * triAmt;
			int vertex_x_offset = data_pos;
			data_pos += i_59_;
			int vertex_y_offset = data_pos;
			data_pos += i_60_;
			int vertex_z_offset = data_pos;
			data_pos += i_61_;
			int i_84_ = data_pos;
			data_pos += numTexTriangles * 6;
			int i_85_ = data_pos;
			data_pos += 6 * i_65_;
			int i_86_ = 6;
			if(version != 14) {
				if(version >= 15)
					i_86_ = 9;
			} else
				i_86_ = 7;
			int i_87_ = data_pos;
			data_pos += i_86_ * i_65_;
			int i_88_ = data_pos;
			data_pos += i_65_;
			int i_89_ = data_pos;
			data_pos += i_65_;
			int i_90_ = data_pos;
			data_pos += i_66_ * 2 + i_65_;
			vertexX = new int[vertexAmt];
			vertexY = new int[vertexAmt];
			vertexZ = new int[vertexAmt];
			vertexIndex3d1 = new short[triAmt];
			vertexIndex3d2 = new short[triAmt];
			vertexIndex3d3 = new short[triAmt];
			triFill = new int[triAmt];
			if(have_alpha == 1)
				triAlpha = new byte[triAmt];
			if(have_mode)
				triType = new byte[triAmt];
			if(have_tex == 1)
				triTex = new int[triAmt];
			if(i_58_ == 1)
				vertexSkin = new int[vertexAmt];
			if(have_tri_skins == 1)
				triSkin = new int[triAmt];
			int i_91_ = data_pos;
			if(i_54_ == 255)
				triPri = new byte[triAmt];
			else
				triPriGlobal = (byte) i_54_;
			if(texAmt > 0) {
				texVertex3 = new short[texAmt];
				if(i_66_ > 0) {
					texturePrimaryColor = new int[i_66_];
					textureSecondaryColor = new int[i_66_];
				}
				texVertex1 = new short[texAmt];
				texVertex2 = new short[texAmt];
				if(i_65_ > 0) {
					particleLifespanZ = new int[i_65_];
					particleDirectionZ = new int[i_65_];
					particleDirectionX = new int[i_65_];
					particleLifespanX = new byte[i_65_];
					particleLifespanY = new byte[i_65_];
					particleDirectionY = new int[i_65_];
				}
			}
			if(have_tex == 1 && texAmt > 0)
				triTexCoord = new short[triAmt];
			buffers[0].pos = vertex_enc_offset;
			buffers[1].pos = vertex_x_offset;
			buffers[2].pos = vertex_y_offset;
			buffers[3].pos = vertex_z_offset;
			buffers[4].pos = vertex_skin_offset;
			int last_x = 0;
			int last_y = 0;
			int last_z = 0;
			for(int vert_idx = 0; vert_idx < vertexAmt; vert_idx++) {
				int vert_enc = buffers[0].getUByte();
				int delta_x = 0;
				if((vert_enc & 0x1) != 0)
					delta_x = buffers[1].getSSmart();
				int delta_y = 0;
				if((vert_enc & 0x2) != 0)
					delta_y = buffers[2].getSSmart();
				int delta_z = 0;
				if((vert_enc & 0x4) != 0)
					delta_z = buffers[3].getSSmart();
				vertexX[vert_idx] = delta_x + last_x;
				vertexY[vert_idx] = delta_y + last_y;
				vertexZ[vert_idx] = delta_z + last_z;
				last_x = vertexX[vert_idx];
				last_y = vertexY[vert_idx];
				last_z = vertexZ[vert_idx];
				if(i_58_ == 1)
					vertexSkin[vert_idx] = buffers[4].getUByte();
			}
			buffers[0].pos = tri_colour_start;
			buffers[1].pos = tri_mode_start;
			buffers[2].pos = i_73_;
			buffers[3].pos = tri_alpha_start;
			buffers[4].pos = tri_skin_start;
			buffers[5].pos = tri_tex_start;
			buffers[6].pos = i_79_;
			for(int tri_id = 0; tri_id < triAmt; tri_id++) {
				triFill[tri_id] = buffers[0].getUShort();
				if(have_mode)
					triType[tri_id] = buffers[1].getSByte();//BIT0 - Shading, BIT1 - Texturing
				if(i_54_ == 255)
					triPri[tri_id] = buffers[2].getSByte();
				if(have_alpha == 1)
					triAlpha[tri_id] = buffers[3].getSByte();
				if(have_tri_skins == 1)
					triSkin[tri_id] = buffers[4].getUByte();
				if(have_tex == 1)
					triTex[tri_id] = (short) (buffers[5].getUShort() - 1);
				if(triTexCoord != null) {
					if(triTex[tri_id] == -1)
						triTexCoord[tri_id] = (short) -1;
					else
						triTexCoord[tri_id] = (short) (buffers[6].getUByte() - 1);
				}
			}
			int used_vertex_count = -1;
			buffers[0].pos = tri_enc_offset;
			buffers[1].pos = tri_idx_offset;
			short _a = 0;
			short _b = 0;
			short _c = 0;
			int accumulator = 0;
			for(int tri_ptr = 0; tri_ptr < triAmt; tri_ptr++) {
				int tri_enc = buffers[1].getUByte();
				if(tri_enc == 1) {
					_a = (short) (accumulator + buffers[0].getSSmart());
					accumulator = _a;
					_b = (short) (accumulator + buffers[0].getSSmart());
					accumulator = _b;
					_c = (short) (accumulator + buffers[0].getSSmart());
					accumulator = _c;
					vertexIndex3d1[tri_ptr] = _a;
					vertexIndex3d2[tri_ptr] = _b;
					vertexIndex3d3[tri_ptr] = _c;
					if(used_vertex_count < _a)
						used_vertex_count = _a;
					if(used_vertex_count < _b)
						used_vertex_count = _b;
					if(used_vertex_count < _c)
						used_vertex_count = _c;
				}
				if(tri_enc == 2) {
					_b = _c;
					_c = (short) (accumulator + buffers[0].getSSmart());
					accumulator = _c;
					vertexIndex3d1[tri_ptr] = _a;
					vertexIndex3d2[tri_ptr] = _b;
					vertexIndex3d3[tri_ptr] = _c;
					if(used_vertex_count < _c)
						used_vertex_count = _c;
				}
				if(tri_enc == 3) {
					_a = _c;
					_c = (short) (accumulator + buffers[0].getSSmart());
					accumulator = _c;
					vertexIndex3d1[tri_ptr] = _a;
					vertexIndex3d2[tri_ptr] = _b;
					vertexIndex3d3[tri_ptr] = _c;
					if(used_vertex_count < _c)
						used_vertex_count = _c;
				}
				if(tri_enc == 4) {
					short flip_buf = _a;
					_a = _b;
					_b = flip_buf;
					_c = (short) (accumulator + buffers[0].getSSmart());
					accumulator = _c;
					vertexIndex3d1[tri_ptr] = _a;
					vertexIndex3d2[tri_ptr] = _b;
					vertexIndex3d3[tri_ptr] = _c;
					if(used_vertex_count < _c)
						used_vertex_count = _c;
				}
			}
			used_vertex_count++;
			buffers[0].pos = i_84_;
			buffers[1].pos = i_85_;
			buffers[2].pos = i_87_;
			buffers[3].pos = i_88_;
			buffers[4].pos = i_89_;
			buffers[5].pos = i_90_;
			for(int i_108_ = 0; (~i_108_) > (~texAmt); i_108_++) {
				int i_109_ = 0xff & texType[i_108_];
				if(i_109_ == 0) {
					texVertex1[i_108_] = (short) buffers[0].getUShort();
					texVertex2[i_108_] = (short) buffers[0].getUShort();
					texVertex3[i_108_] = (short) buffers[0].getUShort();
				}
				if(i_109_ == 1) {
					texVertex1[i_108_] = (short) buffers[1].getUShort();
					texVertex2[i_108_] = (short) buffers[1].getUShort();
					texVertex3[i_108_] = (short) buffers[1].getUShort();
					if(version < 15) {
						particleDirectionX[i_108_] = buffers[2].getUShort();
						if(version >= 14)
							particleDirectionY[i_108_] = buffers[2].getUMedium();
						else
							particleDirectionY[i_108_] = buffers[2].getUShort();
						particleDirectionZ[i_108_] = buffers[2].getUShort();
					} else {
						particleDirectionX[i_108_] = buffers[2].getUMedium();
						particleDirectionY[i_108_] = buffers[2].getUMedium();
						particleDirectionZ[i_108_] = buffers[2].getUMedium();
					}
					particleLifespanX[i_108_] = buffers[3].getSByte();
					particleLifespanY[i_108_] = buffers[4].getSByte();
					particleLifespanZ[i_108_] = buffers[5].getSByte();
				}
				if(i_109_ == 2) {
					texVertex1[i_108_] = (short) buffers[1].getUShort();
					texVertex2[i_108_] = (short) buffers[1].getUShort();
					texVertex3[i_108_] = (short) buffers[1].getUShort();
					if(version < 15) {
						particleDirectionX[i_108_] = buffers[2].getUShort();
						if(version >= 14)
							particleDirectionY[i_108_] = buffers[2].getUMedium();
						else
							particleDirectionY[i_108_] = buffers[2].getUShort();
						particleDirectionZ[i_108_] = buffers[2].getUShort();
					} else {
						particleDirectionX[i_108_] = buffers[2].getUMedium();
						particleDirectionY[i_108_] = buffers[2].getUMedium();
						particleDirectionZ[i_108_] = buffers[2].getUMedium();
					}
					particleLifespanX[i_108_] = buffers[3].getSByte();
					particleLifespanY[i_108_] = buffers[4].getSByte();
					particleLifespanZ[i_108_] = buffers[5].getSByte();
					texturePrimaryColor[i_108_] = buffers[5].getSByte();
					textureSecondaryColor[i_108_] = buffers[5].getSByte();
				}
				if(i_109_ == 3) {
					texVertex1[i_108_] = (short) buffers[1].getUShort();
					texVertex2[i_108_] = (short) buffers[1].getUShort();
					texVertex3[i_108_] = (short) buffers[1].getUShort();
					if(version < 15) {
						particleDirectionX[i_108_] = buffers[2].getUShort();
						if(version >= 14)
							particleDirectionY[i_108_] = buffers[2].getUMedium();
						else
							particleDirectionY[i_108_] = buffers[2].getUShort();
						particleDirectionZ[i_108_] = buffers[2].getUShort();
					} else {
						particleDirectionX[i_108_] = buffers[2].getUMedium();
						particleDirectionY[i_108_] = buffers[2].getUMedium();
						particleDirectionZ[i_108_] = buffers[2].getUMedium();
					}
					particleLifespanX[i_108_] = buffers[3].getSByte();
					particleLifespanY[i_108_] = buffers[4].getSByte();
					particleLifespanZ[i_108_] = buffers[5].getSByte();
				}
			}
			buffers[0].pos = i_91_;
			if(bool_51_) {
				int i_110_ = buffers[0].getUByte(); // XXX f_ab -> emitters, fa -> EmitterTriangle
				if(i_110_ > 0) {
					//emitters = new EmitterTriangle[i_110_];
					for(int i_111_ = 0; i_111_ < i_110_; i_111_++) {
						int i_112_ = buffers[0].getUShort();
						int i_113_ = buffers[0].getUShort();
						byte i_114_;
						if(i_54_ == 255)
							i_114_ = triPri[i_113_];
						else
							i_114_ = (byte) i_54_;
						//emitters[i_111_] = new EmitterTriangle(i_112_, vertexIndex3d1[i_113_], vertexIndex3d2[i_113_], vertexIndex3d3[i_113_], i_114_);
					}
				}
				int i_115_ = buffers[0].getUByte();
				if(i_115_ > 0) { // XXX d -> magnets, nc -> MagnetVertex
					//magnets = new MagnetVertex[i_115_];
					for(int i_116_ = 0; i_115_ > i_116_; i_116_++) {
						int i_117_ = buffers[0].getUShort();
						int i_118_ = buffers[0].getUShort();
						//magnets[i_116_] = new MagnetVertex(i_117_, i_118_);
					}
				}
			}
			if(bool_52_) {
				int i_119_ = buffers[0].getUByte();
				if(i_119_ > 0) {
					//vertexNormal1 = new Vector[i_119_];
					vectorX = new int[i_119_];
					vectorY = new int[i_119_];
					vectorZ = new int[i_119_];
					vectorMagnitude = new int[i_119_];
					System.out.println(i_119_);
					for(int i_120_ = 0; i_119_ > i_120_; i_120_++) {
						int i_121_ = buffers[0].getUShort();
						int i_122_ = buffers[0].getUShort();
						int i_123_ = buffers[0].getUByte();
						byte i_124_ = buffers[0].getSByte();
						vectorX[i_120_] = i_121_;
						vectorY[i_120_] = i_122_;
						vectorZ[i_120_] = i_123_;
						vectorMagnitude[i_120_] = i_124_;
						//vertexNormal1[i_120_] = new Vector(i_121_, i_122_, i_123_, i_124_);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	public static void method460(byte[] data, int id, int type) {
		if(type == 0)
			newModelHeader[id] = data != null ? data : EMPTY_BYTE_ARRAY;
		else
			oldModelHeader[id] = data != null ? data : EMPTY_BYTE_ARRAY;
	}


	public static void remove(int index) {
		newModelHeader[index] = null;
		oldModelHeader[index] = null;
	}

	public static Model get(int index) {
		return get(index, 0);
	}

	public static Model get(int index, int type) {
		byte[] data;
		if(type == 0)
			data = newModelHeader[index];
		else
			data = oldModelHeader[index];
		if(data == null) {
			odFetcher.addRequest(type, index);
			return null;
		} else {
			return new Model(data);
		}


	}

	public static boolean isCached(int id) {
		return isCached(id, 0);

	}

	public static boolean isCached(int id, int type) {
		if(type == 0 && newModelHeader[id] != null)
			return true;
		if(type == 6 && oldModelHeader[id] != null)
			return true;

		odFetcher.addRequest(type, id);
		return false;

	}

	private static int adjustLightness(int hsl, int lightness, int type) {
		if((type & 2) == 2) {
			if(lightness < 0) {
				lightness = 0;
			} else if(lightness > 127) {
				lightness = 127;
			}
			lightness = 127 - lightness;
			return lightness;
		}
		lightness = lightness * (hsl & 0x7f) >> 7;
		if(lightness < 2) {
			lightness = 2;
		} else if(lightness > 126) {
			lightness = 126;
		}
		return (hsl & 0xff80) + lightness;
	}

	public static void reset() {
		projTriClipX = null;
		projTriClipZ = null;
		projVertexX = null;
		projVertexY = null;
		projVertexLocalZ = null;
		projTexVertexX = null;
		projTexVertexY = null;
		projTexVertexZ = null;
		anIntArray1671 = null;
		anIntArrayArray1672 = null;
		anIntArray1673 = null;
		anIntArrayArray1674 = null;
		anIntArray1675 = null;
		anIntArray1676 = null;
		anIntArray1677 = null;
		angleSine = null;
		angleCosine = null;
		hslToRgbMap = null;
		shadeAmt = null;
	}

	private Model() {
		hoverable = false;
	}

	public Model(boolean flag, boolean flag1, boolean flag2, Model model) {
		this(flag, flag1, flag2, false, model);
	}

	public Model(boolean flag, boolean flag1, boolean flag2, boolean texture, Model model) {//objects (trees/bushes/walls/roofs/etc) //Graphcis //NPCs
		hoverable = false;
		vertexAmt = model.vertexAmt;
		triAmt = model.triAmt;
		texAmt = model.texAmt;
		if(flag2) {
			vertexX = model.vertexX;
			vertexY = model.vertexY;
			vertexZ = model.vertexZ;
		} else {
			vertexX = new int[vertexAmt];
			vertexY = new int[vertexAmt];
			vertexZ = new int[vertexAmt];
			for(int j = 0; j < vertexAmt; j++) {
				vertexX[j] = model.vertexX[j];
				vertexY[j] = model.vertexY[j];
				vertexZ[j] = model.vertexZ[j];
			}

		}
		if(flag) {
			triFill = model.triFill;
		} else {
			triFill = new int[triAmt];
			if(model.triFill != null)
				for(int k = 0; k != triAmt; k++)
					triFill[k] = model.triFill[k];
		}

		if(flag1) {
			triAlpha = model.triAlpha;
		} else {
			triAlpha = new byte[triAmt];
			if(model.triAlpha != null) {
				for(int i1 = 0; i1 < triAmt; i1++)
					triAlpha[i1] = model.triAlpha[i1];
			}
		}
		vertexSkin = model.vertexSkin;
		triSkin = model.triSkin;
		if(texAmt > 0) {
			texVertex1 = model.texVertex1;
			texVertex2 = model.texVertex2;
			texVertex3 = model.texVertex3;
			triTex = model.triTex;
			display_model_specific_texture = texture;
			triTexCoord = model.triTexCoord;
			texType = model.texType;
		}
		vertexIndex3d1 = model.vertexIndex3d1;
		vertexIndex3d2 = model.vertexIndex3d2;
		vertexIndex3d3 = model.vertexIndex3d3;
		triPriGlobal = model.triPriGlobal;
		triPri = model.triPri;
		triType = model.triType;
		upscaled = model.upscaled;
	}

	public Model(boolean flag, boolean flag1, Model model) {
		hoverable = false;
		vertexAmt = model.vertexAmt;
		triAmt = model.triAmt;
		texAmt = model.texAmt;
		if(flag) {
			vertexY = new int[vertexAmt];
			System.arraycopy(model.vertexY, 0, vertexY, 0, vertexAmt);

		} else {
			vertexY = model.vertexY;
		}

		if(texAmt > 0) {
			texVertex1 = model.texVertex1;
			texVertex2 = model.texVertex2;
			texVertex3 = model.texVertex3;
			triTex = model.triTex;
			triTexCoord = model.triTexCoord;
			texType = model.texType;
		}

		if(flag1) {
			triCol1 = new int[triAmt];
			triCol2 = new int[triAmt];
			triCol3 = new int[triAmt];
			for(int k = 0; k < triAmt; k++) {
				triCol1[k] = model.triCol1[k];
				triCol2[k] = model.triCol2[k];
				triCol3[k] = model.triCol3[k];
			}

			triType = new byte[triAmt];
			if(model.triType == null) {
				for(int l = 0; l < triAmt; l++)
					triType[l] = 0;

			} else {
				System.arraycopy(model.triType, 0, triType, 0, triAmt);
			}
			System.arraycopy(model.vectorX, 0, super.vectorX, 0, model.vectorX.length);
			System.arraycopy(model.vectorY, 0, super.vectorY, 0, model.vectorY.length);
			System.arraycopy(model.vectorZ, 0, super.vectorZ, 0, model.vectorZ.length);
			System.arraycopy(model.vectorMagnitude, 0, super.vectorMagnitude, 0, model.vectorMagnitude.length);
			//super.vectorX = model.vectorX;
			//super.vectorY = model.vectorY;
			//super.vectorZ = model.vectorZ;
			//super.vectorMagnitude = model.vectorMagnitude;
			System.arraycopy(model.vectorNormalX, 0, vectorNormalX, 0, model.vectorNormalX.length);
			System.arraycopy(model.vectorNormalY, 0, vectorNormalY, 0, model.vectorNormalY.length);
			System.arraycopy(model.vectorNormalZ, 0, vectorNormalZ, 0, model.vectorNormalZ.length);
			System.arraycopy(model.vectorNormalMagnitude, 0, vectorNormalMagnitude, 0, model.vectorNormalMagnitude.length);
			//vectorNormalX = model.vectorNormalX;
			//vectorNormalY = model.vectorNormalY;
			//vectorNormalZ = model.vectorNormalZ;
			//vectorNormalMagnitude = model.vectorNormalMagnitude;
		} else {
			triCol1 = model.triCol1;
			triCol2 = model.triCol2;
			triCol3 = model.triCol3;
			triType = model.triType;
		}
		vertexX = model.vertexX;
		vertexZ = model.vertexZ;
		triFill = model.triFill;
		triAlpha = model.triAlpha;
		triPri = model.triPri;
		priAmt = model.priAmt;
		vertexIndex3d1 = model.vertexIndex3d1;
		vertexIndex3d2 = model.vertexIndex3d2;
		vertexIndex3d3 = model.vertexIndex3d3;
		texVertex1 = model.texVertex1;
		texVertex2 = model.texVertex2;
		texVertex3 = model.texVertex3;
		super.maxVerticalDistUp = model.maxVerticalDistUp;
		maxVerticalDistDown = model.maxVerticalDistDown;
		maxHorizontalDist = model.maxHorizontalDist;
		maxDiagonalDistUp = model.maxDiagonalDistUp;
		maxDiagonalDistUpAndDown = model.maxDiagonalDistUpAndDown;
		minVertexX = model.minVertexX;
		maxVertexZ = model.maxVertexZ;
		minVertexZ = model.minVertexZ;
		maxVertexX = model.maxVertexX;
		upscaled = model.upscaled;
	}

	//start
	public Model(Model attatch[]) {
		int number_of_models = 2;
		hoverable = false;
		boolean has_render_type = false;
		boolean has_priorities = false;
		boolean has_alpha = false;
		boolean has_skin = false;
		boolean has_texture = false;
		boolean has_coordinates = false;
		vertexAmt = 0;
		triAmt = 0;
		texAmt = 0;
		triPriGlobal = -1;
		for(int model_index = 0; model_index < number_of_models; model_index++) {
			Model connect = attatch[model_index];
			if(connect != null) {
				vertexAmt += connect.vertexAmt;
				triAmt += connect.triAmt;
				texAmt += connect.texAmt;
				has_render_type |= connect.triType != null;
				if(connect.triPri != null) {
					has_priorities = true;
				} else {
					if(triPriGlobal == -1)
						triPriGlobal = connect.triPriGlobal;
					if(triPriGlobal != connect.triPriGlobal)
						has_priorities = true;
				}
				has_alpha |= connect.triAlpha != null;
				has_skin |= connect.triFill != null;
				has_texture |= connect.triTex != null;
				has_coordinates |= connect.triTexCoord != null;
				upscaled |= connect.upscaled;
			}
		}

		vertexX = new int[vertexAmt];
		vertexY = new int[vertexAmt];
		vertexZ = new int[vertexAmt];
		vertexIndex3d1 = new short[triAmt];
		vertexIndex3d2 = new short[triAmt];
		vertexIndex3d3 = new short[triAmt];
		triCol1 = new int[triAmt];
		triCol2 = new int[triAmt];
		triCol3 = new int[triAmt];
		if(texAmt > 0) {
			texVertex1 = new short[texAmt];
			texVertex2 = new short[texAmt];
			texVertex3 = new short[texAmt];
			texType = new byte[texAmt];
		}
		if(has_render_type)
			triType = new byte[triAmt];
		if(has_priorities)
			triPri = new byte[triAmt];
		if(has_alpha)
			triAlpha = new byte[triAmt];
		if(has_skin)
			triFill = new int[triAmt];
		if(has_texture)
			triTex = new int[triAmt];
		if(has_coordinates)
			triTexCoord = new short[triAmt];
		vertexAmt = 0;
		triAmt = 0;
		texAmt = 0;
		int priority = 0;
		for(int model_index = 0; model_index < number_of_models; model_index++) {
			Model connect = attatch[model_index];
			if(connect != null) {
				int k1 = vertexAmt;
				for(int l1 = 0; l1 < connect.vertexAmt; l1++) {
					int x = connect.vertexX[l1];
					int y = connect.vertexY[l1];
					int z = connect.vertexZ[l1];
					if(upscaled && !connect.upscaled) {
						x <<= 2;
						y <<= 2;
						z <<= 2;
					}
					vertexX[vertexAmt] = x;
					vertexY[vertexAmt] = y;
					vertexZ[vertexAmt] = z;
					vertexAmt++;
				}

				for(int i2 = 0; i2 < connect.triAmt; i2++) {
					vertexIndex3d1[triAmt] = (short) (connect.vertexIndex3d1[i2] + k1);
					vertexIndex3d2[triAmt] = (short) (connect.vertexIndex3d2[i2] + k1);
					vertexIndex3d3[triAmt] = (short) (connect.vertexIndex3d3[i2] + k1);
					triCol1[triAmt] = connect.triCol1[i2];
					triCol2[triAmt] = connect.triCol2[i2];
					triCol3[triAmt] = connect.triCol3[i2];
					if(has_render_type)
						if(connect.triType == null) {
							triType[triAmt] = 0;
						} else {
							int j2 = connect.triType[i2];
							if((j2 & 2) == 2)
								j2 += priority << 2;
							triType[triAmt] = (byte) j2;
						}
					if(has_priorities)
						if(connect.triPri == null)
							triPri[triAmt] = (byte) connect.triPriGlobal;
						else
							triPri[triAmt] = connect.triPri[i2];
					if(has_alpha)
						if(connect.triAlpha == null)
							triAlpha[triAmt] = 0;
						else
							triAlpha[triAmt] = connect.triAlpha[i2];
					if(has_skin && connect.triFill != null)
						triFill[triAmt] = connect.triFill[i2];
					if(has_texture) {
						if(connect.triTex != null && connect.triTex[i2] != -1) {
							triTex[triAmt] = connect.triTex[i2];
						} else {
							triTex[triAmt] = -1;

						}
					}
					if(has_coordinates) {
						triTexCoord[triAmt] = (short) (connect.triTexCoord == null || connect.triTexCoord[i2] == -1 ? -1 : (connect.triTexCoord[i2] & 0xffff) + texAmt);
					}
					triAmt++;
				}
				for(int textured_faces = 0; textured_faces < connect.texAmt; textured_faces++) {
					texType[texAmt] = connect.texType[textured_faces];
					texVertex1[texAmt] = (short) (connect.texVertex1[textured_faces] + k1);
					texVertex2[texAmt] = (short) (connect.texVertex2[textured_faces] + k1);
					texVertex3[texAmt] = (short) (connect.texVertex3[textured_faces] + k1);
					texAmt++;
				}

				priority += connect.texAmt;
			}
		}
		computeBoundsDist();
	}

	public Model(int number_of_models, Model attatch[]) {
		try {
			boolean old_format = false;
			hoverable = false;
			boolean has_render_type = false;
			boolean has_priorities = false;
			boolean has_alpha = false;
			boolean has_skin = false;
			boolean has_texture = false;
			boolean has_coordinates = false;
			boolean color = false;
			vertexAmt = 0;
			triAmt = 0;
			texAmt = 0;
			triPriGlobal = -1;
			Model connect;
			int model_index;
			for(model_index = 0; model_index < number_of_models; model_index++) {
				connect = attatch[model_index];
				if(connect != null) {
					vertexAmt += connect.vertexAmt;
					triAmt += connect.triAmt;
					texAmt += connect.texAmt;
					has_render_type |= connect.triType != null;
					has_alpha |= connect.triAlpha != null;
					if(connect.triPri != null) {
						has_priorities = true;
					} else {
						if(triPriGlobal == -1)// -1
							triPriGlobal = connect.triPriGlobal;

						if(triPriGlobal != connect.triPriGlobal)
							has_priorities = true;
					}
					has_coordinates = has_coordinates | connect.triTexCoord != null;
					has_skin = has_skin | connect.triSkin != null;
					has_texture = has_texture | connect.triTex != null;
					color = color | connect.triFill != null;
					upscaled |= connect.upscaled;
				}
			}
			if(color)
				triFill = new int[triAmt];
			vertexX = new int[vertexAmt];
			vertexY = new int[vertexAmt];
			vertexZ = new int[vertexAmt];
			vertexSkin = new int[vertexAmt];
			vertexIndex3d1 = new short[triAmt];
			vertexIndex3d2 = new short[triAmt];
			vertexIndex3d3 = new short[triAmt];

			if(has_render_type)
				triType = new byte[triAmt];

			if(has_skin)
				triSkin = new int[triAmt];

			if(texAmt > 0) {
				texVertex1 = new short[texAmt];
				texVertex2 = new short[texAmt];
				texVertex3 = new short[texAmt];
				texType = new byte[texAmt];
			}

			if(has_coordinates)
				triTexCoord = new short[triAmt];

			if(has_texture)
				triTex = new int[triAmt];

			if(has_alpha)
				triAlpha = new byte[triAmt];

			if(has_priorities)
				triPri = new byte[triAmt];

			vertexAmt = 0;
			triAmt = 0;
			texAmt = 0;
			int[] offsets = null;
			int texture_face = 0;
			for(model_index = 0; model_index < number_of_models; model_index++) {
				connect = attatch[model_index];
				if(connect != null) {
					int vertex_coord = -1;
					if(offsets != null) {
						vertex_coord = offsets[number_of_models] = vertexAmt;
						for(int l1 = 0; l1 < connect.vertexAmt; l1++) {
							int x = connect.vertexX[l1];
							int y = connect.vertexY[l1];
							int z = connect.vertexZ[l1];
							if(upscaled && !connect.upscaled) {
								x <<= 2;
								y <<= 2;
								z <<= 2;
							}
							vertexX[vertexAmt] = x;
							vertexY[vertexAmt] = y;
							vertexZ[vertexAmt] = z;
							vertexSkin[vertexAmt] = connect.vertexSkin != null ? connect.vertexSkin[l1] : -1;
							vertexAmt++;
						}
					}
					for(int j1 = 0; j1 < connect.triAmt; j1++) {
						if(has_render_type) {
							if(old_format) {
								if(connect.triType == null) {
									triType[triAmt] = 0;
								} else {
									int type = connect.triType[j1];
									if((type & 2) == 2)
										type += texture_face << 2;
									triType[triAmt] = (byte) type;
								}
							} else {
								if(connect.triType != null) {
									triType[triAmt] = connect.triType[j1];
								} else {
									old_format = true;
								}
							}

						}
						if(has_priorities && connect.triPri != null)
							triPri[triAmt] = connect.triPri[j1];
						else if(triPri != null) {// threw null
							// here for
							// torva
							triPri[triAmt] = (byte) connect.triPriGlobal;
						}

						if(has_alpha && connect.triAlpha != null)
							triAlpha[triAmt] = connect.triAlpha[j1];

						if(has_texture) {
							if(connect.triTex != null && connect.triTex[j1] != -1) {
								triTex[triAmt] = connect.triTex[j1];
								display_model_specific_texture = true;
							} else {
								triTex[triAmt] = -1;

							}
						}

						if(has_alpha && connect.triSkin != null)
							triSkin[triAmt] = connect.triSkin[j1];

						if(vertex_coord != -1) {
							vertexIndex3d1[triAmt] = (short) ((connect.vertexIndex3d1[j1]) + vertex_coord);
							vertexIndex3d2[triAmt] = (short) ((connect.vertexIndex3d2[j1]) + vertex_coord);
							vertexIndex3d3[triAmt] = (short) ((connect.vertexIndex3d3[j1]) + vertex_coord);
						} else {
							vertexIndex3d1[triAmt] = (short) (method465(connect, connect.vertexIndex3d1[j1]));
							vertexIndex3d2[triAmt] = (short) (method465(connect, connect.vertexIndex3d2[j1]));
							vertexIndex3d3[triAmt] = (short) (method465(connect, connect.vertexIndex3d3[j1]));
						}
						if(color) {
							if(connect.triFill != null) {
								triFill[triAmt] = connect.triFill[j1];
							} else {
								triFill[triAmt] = 0;
							}
						}
						triAmt++;
					}
				}
			}
			int face = 0;
			for(model_index = 0; model_index < number_of_models; model_index++) {
				connect = attatch[model_index];
				if(connect != null) {
					if(has_coordinates) {
						for(int mapped_pointers = 0; mapped_pointers < connect.triAmt; mapped_pointers++)
							triTexCoord[/* triAmt */face++] = (short) (connect.triTexCoord == null || connect.triTexCoord[mapped_pointers] == -1 ? -1 : (connect.triTexCoord[mapped_pointers] & 0xffff) + texAmt);


					}
					int vertex_coord = offsets != null ? offsets[model_index] : -1;// short vertex_coord = (short) (1 << l1);
					for(int texture_index = 0; texture_index < connect.texAmt; texture_index++) {
						int opcode = (texType[texAmt] = connect.texType[texture_index]);
						if(opcode == 0) {
							if(vertex_coord != -1) {
								texVertex1[texAmt] = (short) method465(connect, (connect.texVertex1[texture_index]) + vertex_coord);
								texVertex2[texAmt] = (short) method465(connect, (connect.texVertex2[texture_index]) + vertex_coord);
								texVertex3[texAmt] = (short) method465(connect, (connect.texVertex3[texture_index]) + vertex_coord);
							} else {
								texVertex1[texAmt] = (short) method465(connect, connect.texVertex1[texture_index]);
								texVertex2[texAmt] = (short) method465(connect, connect.texVertex2[texture_index]);
								texVertex3[texAmt] = (short) method465(connect, connect.texVertex3[texture_index]);
							}
						}
						if(opcode >= 1 && opcode <= 3) {
							if(vertex_coord != -1) {
								texVertex1[texAmt] = (short) ((connect.texVertex1[texture_index]) + vertex_coord);
								texVertex2[texAmt] = (short) ((connect.texVertex2[texture_index]) + vertex_coord);
								texVertex3[texAmt] = (short) ((connect.texVertex3[texture_index]) + vertex_coord);
							} else {
								texVertex1[texAmt] = connect.texVertex1[texture_index];
								texVertex2[texAmt] = connect.texVertex2[texture_index];
								texVertex3[texAmt] = connect.texVertex3[texture_index];
							}
						}
						if(opcode == 2) {

						}
						texAmt++;
					}
					if(old_format)
						texture_face += connect.texAmt;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void drawTriangle(int face) {
		try {
			if(projTriClipZ[face]) {
				drawTriangleOrQuad(face);
				return;
			}

			int a = vertexIndex3d1[face] & 0xffff;
			int b = vertexIndex3d2[face] & 0xffff;
			int c = vertexIndex3d3[face] & 0xffff;
			if(triFill != null && triFill[face] == -1)
				return;
			Rasterizer3D.clippedScan = projTriClipX[face];
			Rasterizer3D.alphaFilter = (triAlpha == null ? 0 : triAlpha[face] & 0xFF);
			int face_type;
			if(triType == null) {
				face_type = 0;
			} else // if(triType[face] != -1)
			{
				face_type = triType[face] & 0xff;
			}

			boolean noTextureValues = (triTex == null || (face < triTex.length && triTex[face] == -1));
			boolean noCoordinates = (triTexCoord == null || (face < triTexCoord.length && triTexCoord[face] == -1));
			
			if(noTextureValues || noCoordinates) {
				if(face_type == 0) {
					//Rasterizer3D.drawFlatTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], hslToRgbMap[triCol1[face]]);
					Rasterizer3D.drawGouraudTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], triCol1[face], triCol2[face], triCol3[face]);
					return;
				}
				if(face_type == 1) {
					Rasterizer3D.drawFlatTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], hslToRgbMap[triCol1[face]]);
					return;
				}
			}
			if(display_model_specific_texture) {
				if(face_type == 0) {
					int texture_type = triTexCoord[face] & 0xffff;
					if(texture_type == 0xffff)
						texture_type = -1;
					if((texType != null && texType[texture_type] >= 0) || texture_type == -1) {
						int x = (texture_type == -1 || texType[texture_type] > 0) ? a : texVertex1[texture_type] & 0xffff;
						int y = (texture_type == -1 || texType[texture_type] > 0) ? b : texVertex2[texture_type] & 0xffff;
						int z = (texture_type == -1 || texType[texture_type] > 0) ? c : texVertex3[texture_type] & 0xffff;
						Rasterizer3D.drawTexturedTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], triCol1[face], triCol2[face], triCol3[face], projTexVertexX[x], projTexVertexX[y], projTexVertexX[z], projTexVertexY[x], projTexVertexY[y], projTexVertexY[z], projTexVertexZ[x], projTexVertexZ[y], projTexVertexZ[z], triTex[face], force_texture, false);
						return;
					}
				}

				if(face_type == 1) {
					int texture_type = triTexCoord[face] & 0xffff;
					if(texture_type == 0xffff)
						texture_type = -1;
					if((texType != null && texType[texture_type] >= 0) || texture_type == -1) {
						int x = (texture_type == -1 || texType[texture_type] > 0) ? a : texVertex1[texture_type] & 0xffff;
						int y = (texture_type == -1 || texType[texture_type] > 0) ? b : texVertex2[texture_type] & 0xffff;
						int z = (texture_type == -1 || texType[texture_type] > 0) ? c : texVertex3[texture_type] & 0xffff;
						Rasterizer3D.drawTexturedTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], triCol1[face], triCol1[face], triCol1[face], projTexVertexX[x], projTexVertexX[y], projTexVertexX[z], projTexVertexY[x], projTexVertexY[y], projTexVertexY[z], projTexVertexZ[x], projTexVertexZ[y], projTexVertexZ[z], triTex[face], force_texture, false);
						return;
					}
				}
			}

			if(face_type == 0) {//TODO: do this.
				if(triTex[face] != -1) {
					int x, y, z;
					if(texType != null && triTexCoord[face] != -1) {
						int var8 = triTexCoord[face] & 0xffff;
						int code = texType[var8] & 0xff;
						x = texVertex1[var8] & 0xffff;
						y = texVertex2[var8] & 0xffff;
						z = texVertex3[var8] & 0xffff;
						if(code != 0) {
							x = a;
							y = b;
							z = c;
						}
					} else {
						x = a;
						y = b;
						z = c;
					}
					Rasterizer3D.drawTexturedTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], triCol1[face], triCol2[face], triCol3[face], projTexVertexX[x], projTexVertexX[y], projTexVertexX[z], projTexVertexY[x], projTexVertexY[y], projTexVertexY[z], projTexVertexZ[x], projTexVertexZ[y], projTexVertexZ[z], triTex[face], force_texture, false);
					return;
				}
				int texture_type = triTexCoord[face] & 0xffff;
				if(texture_type == 0xffff) {
					texture_type = -1;
				}
				if((texType != null && texType[texture_type] >= 0) || texture_type == -1) {
					int x = (texture_type == -1 || texType[texture_type] > 0) ? a : texVertex1[texture_type] & 0xffff;
					int y = (texture_type == -1 || texType[texture_type] > 0) ? b : texVertex2[texture_type] & 0xffff;
					int z = (texture_type == -1 || texType[texture_type] > 0) ? c : texVertex3[texture_type] & 0xffff;
					Rasterizer3D.drawTexturedTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], triCol1[face], triCol1[face], triCol1[face], projTexVertexX[x], projTexVertexX[y], projTexVertexX[z], projTexVertexY[x], projTexVertexY[y], projTexVertexY[z], projTexVertexZ[x], projTexVertexZ[y], projTexVertexZ[z], triTex[face], force_texture, false);
					return;
				}
				Rasterizer3D.drawGouraudTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], triCol1[face], triCol2[face], triCol3[face]);
				return;
			}
			if(face_type == 1) {//TODO: do this.
				if(triTex[face] != -1) {
					int x, y, z;
					if(texType != null && triTexCoord[face] != -1) {
						int var8 = triTexCoord[face] & 0xffff;
						int code = texType[var8] & 0xff;
						x = texVertex1[var8] & 0xffff;
						y = texVertex2[var8] & 0xffff;
						z = texVertex3[var8] & 0xffff;
						if(code != 0) {
							x = a;
							y = b;
							z = c;
						}
					} else {
						x = a;
						y = b;
						z = c;
					}
					Rasterizer3D.drawTexturedTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], triCol1[face], triCol1[face], triCol1[face], projTexVertexX[x], projTexVertexX[y], projTexVertexX[z], projTexVertexY[x], projTexVertexY[y], projTexVertexY[z], projTexVertexZ[x], projTexVertexZ[y], projTexVertexZ[z], triTex[face], force_texture, false);
					return;
				}
				int texture_type = triTexCoord[face] & 0xffff;
				if(texture_type == 0xffff) {
					texture_type = -1;
				}
				if((texType != null && texType[texture_type] >= 0) || texture_type == -1) {
					int x = (texture_type == -1 || texType[texture_type] > 0) ? a : texVertex1[texture_type] & 0xffff;
					int y = (texture_type == -1 || texType[texture_type] > 0) ? b : texVertex2[texture_type] & 0xffff;
					int z = (texture_type == -1 || texType[texture_type] > 0) ? c : texVertex3[texture_type] & 0xffff;
					Rasterizer3D.drawTexturedTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], triCol1[face], triCol1[face], triCol1[face], projTexVertexX[x], projTexVertexX[y], projTexVertexX[z], projTexVertexY[x], projTexVertexY[y], projTexVertexY[z], projTexVertexZ[x], projTexVertexZ[y], projTexVertexZ[z], triTex[face], force_texture, false);
					return;
				}
				Rasterizer3D.drawFlatTriangle(projVertexY[a], projVertexY[b], projVertexY[c], projVertexX[a], projVertexX[b], projVertexX[c], vertex2dZ[a], vertex2dZ[b], vertex2dZ[c], hslToRgbMap[triCol1[face]]);
			}
		} catch(Exception e) {
			//e.printStackTrace();
		}
	}
	
	public void drawModel(int yaw, int pitch, int roll, int dx, int dy, int dz) {
		final int midX = Rasterizer3D.viewport.centerX;
		final int midY = Rasterizer3D.viewport.centerY;
		final int yawSin = angleSine[yaw];
		final int yawCos = angleCosine[yaw];
		final int pitchSin = angleSine[pitch];
		final int pitchCos = angleCosine[pitch];
		final int rollSin = angleSine[roll];
		final int rollCos = angleCosine[roll];
		final int rollZ = dy * rollSin + dz * rollCos >> 16;
		for(int i = 0; i < vertexAmt; i++) {
			if(triFill != null && i <= triFill.length) {
				if (triFill[i] == 65535 || triFill[i] == 16705 || triFill[i] == 37798) {
					triFill[i] = 255;
				}
			}
			int x = vertexX[i] << (upscaled ? 0 : 2);
			int y = vertexY[i] << (upscaled ? 0 : 2);
			int z = vertexZ[i] << (upscaled ? 0 : 2);
			if(pitch != 0) {
				final int k5 = y * pitchSin + x * pitchCos >> 16;
				y = y * pitchCos - x * pitchSin >> 16;
				x = k5;
			}
			if(yaw != 0) {
				final int i6 = z * yawSin + x * yawCos >> 16;
				z = z * yawCos - x * yawSin >> 16;
				x = i6;
			}
			x += dx << 2;
			y += dy << 2;
			z += dz << 2;
			final int j6 = y * rollCos - z * rollSin >> 16;
			z = y * rollSin + z * rollCos >> 16;
			y = j6;
			projVertexLocalZ[i] = (z >> 2) - rollZ;
			projVertexX[i] = midX + x * Scene.focalLength / z;
			projVertexY[i] = midY + y * Scene.focalLength / z;
			if(texAmt > 0) {
				projTexVertexX[i] = x >> 2;
				projTexVertexY[i] = y >> 2;
				projTexVertexZ[i] = z >> 2;
			}
		}
		try {
			method483(false, false, 0);
		} catch(final Exception e) {
			//e.printStackTrace();
		}
	}


	@Override
	public void drawModel(int modelYaw, int rollSin, int rollCos, int yawSin, int yawCos, int camX, int camY, int camZ, long hash) {
		final int j2 = camZ * yawCos - camX * yawSin >> 16;
		final int k2 = camY * rollSin + j2 * rollCos >> 16;
		final int l2 = maxHorizontalDist * rollCos >> 16;
		final int i3 = k2 + l2;
		if(i3 <= Constants.CAM_NEAR || k2 >= Constants.CAM_FAR) {
			return;
		}
		final int j3 = camZ * yawSin + camX * yawCos >> 16;
		int k3 = (j3 - maxHorizontalDist) * Scene.focalLength;
		if(k3 / i3 >= Rasterizer3D.viewport.centerX) {
			return;
		}
		int l3 = (j3 + maxHorizontalDist) * Scene.focalLength;
		if(l3 / i3 <= -Rasterizer3D.viewport.centerX) {
			return;
		}
		final int i4 = camY * rollCos - j2 * rollSin >> 16;
		final int j4 = maxHorizontalDist * rollSin >> 16;
		int k4 = (i4 + j4) * Scene.focalLength;
		if(k4 / i3 <= -Rasterizer3D.viewport.centerY) {
			return;
		}
		final int l4 = j4 + (super.maxVerticalDistUp * rollCos >> 16);
		int i5 = (i4 - l4) * Scene.focalLength;
		if(i5 / i3 >= Rasterizer3D.viewport.centerY) {
			return;
		}
		final int j5 = l2 + (super.maxVerticalDistUp * rollSin >> 16);
		boolean flag = false;
		if(k2 - j5 <= Constants.CAM_NEAR) {
			flag = true;
		}
		boolean hover = false;
		if(hash > 0 && aBoolean1684) {
			int k5 = k2 - l2;
			if(k5 <= Constants.CAM_NEAR) {
				k5 = Constants.CAM_NEAR;
			}
			if(j3 > 0) {
				k3 /= i3;
				l3 /= k5;
			} else {
				l3 /= i3;
				k3 /= k5;
			}
			if(i4 > 0) {
				i5 /= i3;
				k4 /= k5;
			} else {
				k4 /= i3;
				i5 /= k5;
			}
			final int mx = hoverX - Rasterizer3D.viewport.centerX;
			final int my = hoverY - Rasterizer3D.viewport.centerY;
			if(mx > k3 && mx < l3 && my > i5 && my < k4) {
				if(hoverable) {
					modelHover[modelHoverAmt++] = hash;
				} else {
					hover = true;
				}
			}
		}
		final int midX = Rasterizer3D.viewport.centerX;
		final int midY = Rasterizer3D.viewport.centerY;
		int l6 = 0;
		int i7 = 0;
		if(modelYaw != 0) {
			l6 = angleSine[modelYaw];
			i7 = angleCosine[modelYaw];
		}
		for(int i = 0; i < vertexAmt; i++) {
			int x = vertexX[i] << (upscaled ? 0 : 2);
			int y = vertexY[i] << (upscaled ? 0 : 2);
			int z = vertexZ[i] << (upscaled ? 0 : 2);
			if(modelYaw != 0) {
				final int temp = z * l6 + x * i7 >> 16;
				z = z * i7 - x * l6 >> 16;
				x = temp;
			}
			x += camX << 2;
			y += camY << 2;
			z += camZ << 2;
			int tamp = z * yawSin + x * yawCos >> 16;
			z = z * yawCos - x * yawSin >> 16;
			x = tamp;
			tamp = y * rollCos - z * rollSin >> 16;
			z = y * rollSin + z * rollCos >> 16;
			y = tamp;
			if(i >= projVertexLocalZ.length)
				return;
			projVertexLocalZ[i] = (z >> 2) - k2;
			if(z >= Constants.CAM_NEAR) {
				projVertexX[i] = midX + (x * Scene.focalLength / z);
				projVertexY[i] = midY + (y * Scene.focalLength / z);
				vertex2dZ[i] = z >> 2;
			} else {
				projVertexX[i] = -5000;
				flag = true;
			}
			if(flag || texAmt > 0) {
				projTexVertexX[i] = x >> 2;
				projTexVertexY[i] = y >> 2;
				projTexVertexZ[i] = z >> 2;
			}
		}
		try {
			method483(flag, hover, hash);
		} catch(final Exception e) {
			e.printStackTrace();
		}
	}

	public void method464(Model model, boolean flag) {
		method464(model, flag, true, false);
	}

	public void method464(Model model, boolean flag, boolean texture) {
		method464(model, flag, texture, false);
	}

	public void method464(Model model, boolean flag, boolean texture, boolean npc) {
		vertexAmt = model.vertexAmt;
		triAmt = model.triAmt;
		texAmt = model.texAmt;
		if(anIntArray1622.length < vertexAmt) {
			anIntArray1622 = new int[vertexAmt + 100];
			anIntArray1623 = new int[vertexAmt + 100];
			anIntArray1624 = new int[vertexAmt + 100];
		}
		vertexX = anIntArray1622;
		vertexY = anIntArray1623;
		vertexZ = anIntArray1624;
		for(int k = 0; k < vertexAmt; k++) {
			vertexX[k] = model.vertexX[k];
			vertexY[k] = model.vertexY[k];
			vertexZ[k] = model.vertexZ[k];
		}
		if(texAmt > 0) {
			texVertex1 = model.texVertex1;
			texVertex2 = model.texVertex2;
			texVertex3 = model.texVertex3;

			if(texture) {
				triTex = model.triTex;
			} else {
				triTex = new int[texAmt];
				if(model.triTex != null) {
					for(int l = 0; l < texAmt; l++)
						triTex[l] = -1;
				}
			}
			force_texture = npc;
			display_model_specific_texture = texture;
			triTexCoord = model.triTexCoord;
			texType = model.texType;

		}
		if(flag) {
			triAlpha = model.triAlpha;
		} else {
			if(anIntArray1625.length < triAmt) {
				anIntArray1625 = new byte[Math.max(anIntArray1625.length * 2, triAmt)];
			}
			triAlpha = anIntArray1625;
			if(model.triAlpha == null) {
				for(int l = 0; l < triAmt; l++) {
					triAlpha[l] = 0;
				}
			} else {
				System.arraycopy(model.triAlpha, 0, triAlpha, 0, triAmt);
			}
		}
		triType = model.triType;
		triFill = model.triFill;
		triPri = model.triPri;
		priAmt = model.priAmt;
		triangleSkin = model.triangleSkin;
		anIntArrayArray1657 = model.anIntArrayArray1657;
		vertexIndex3d1 = model.vertexIndex3d1;
		vertexIndex3d2 = model.vertexIndex3d2;
		vertexIndex3d3 = model.vertexIndex3d3;
		triCol1 = model.triCol1;
		triCol2 = model.triCol2;
		triCol3 = model.triCol3;
		texVertex1 = model.texVertex1;
		texVertex2 = model.texVertex2;
		texVertex3 = model.texVertex3;
		upscaled = model.upscaled;
	}

	private int method465(Model model, int i) {
		int j = -1;
		int k = model.vertexX[i];
		int l = model.vertexY[i];
		int i1 = model.vertexZ[i];
		if(upscaled && !model.upscaled) {
			k <<= 2;
			l <<= 2;
			i1 <<= 2;
		}
		for(int j1 = 0; j1 < vertexAmt; j1++) {
			if(k != vertexX[j1] || l != vertexY[j1] || i1 != vertexZ[j1]) {
				continue;
			}
			j = j1;
			break;
		}
		if(j == -1) {
			vertexX[vertexAmt] = k;
			vertexY[vertexAmt] = l;
			vertexZ[vertexAmt] = i1;
			if(model.vertexSkin != null) {
				vertexSkin[vertexAmt] = model.vertexSkin[i];
			}
			j = vertexAmt++;
		}
		return j;
	}

	public void computeBoundsDist() {
		super.maxVerticalDistUp = 0;
		maxHorizontalDist = 0;
		maxVerticalDistDown = 0;
		for(int i = 0; i < vertexAmt; i++) {
			int vx = vertexX[i];
			int vy = vertexY[i];
			int vz = vertexZ[i];
			if(upscaled) {
				vx >>= 2;
				vy >>= 2;
				vz >>= 2;
			}
			if(-vy > super.maxVerticalDistUp) {
				super.maxVerticalDistUp = -vy;
			}
			if(vy > maxVerticalDistDown) {
				maxVerticalDistDown = vy;
			}
			final int sq = vx * vx + vz * vz;
			if(sq > maxHorizontalDist) {
				maxHorizontalDist = sq;
			}
		}
		// additions to round the sqrt up
		maxHorizontalDist = (int) (Math.sqrt(maxHorizontalDist) + 0.98999999999999999D);
		maxDiagonalDistUp = (int) (Math.sqrt(maxHorizontalDist * maxHorizontalDist + super.maxVerticalDistUp * super.maxVerticalDistUp) + 0.98999999999999999D);
		maxDiagonalDistUpAndDown = maxDiagonalDistUp + (int) (Math.sqrt(maxHorizontalDist * maxHorizontalDist + maxVerticalDistDown * maxVerticalDistDown) + 0.98999999999999999D);
	}

	public void computeBoundsVertical() {
		super.maxVerticalDistUp = 0;
		maxVerticalDistDown = 0;
		for(int i = 0; i < vertexAmt; i++) {
			final int j = vertexY[i];
			if(-j > super.maxVerticalDistUp) {
				super.maxVerticalDistUp = -j;
			}
			if(j > maxVerticalDistDown) {
				maxVerticalDistDown = j;
			}
		}
		maxDiagonalDistUp = (int) (Math.sqrt(maxHorizontalDist * maxHorizontalDist + super.maxVerticalDistUp * super.maxVerticalDistUp) + 0.98999999999999999D);
		maxDiagonalDistUpAndDown = maxDiagonalDistUp + (int) (Math.sqrt(maxHorizontalDist * maxHorizontalDist + maxVerticalDistDown * maxVerticalDistDown) + 0.98999999999999999D);
	}

	private void computeBoundsAll() {
		super.maxVerticalDistUp = 0;
		maxHorizontalDist = 0;
		maxVerticalDistDown = 0;
		minVertexX = 0xf423f;
		maxVertexX = 0xfff0bdc1;
		maxVertexZ = 0xfffe7961;
		minVertexZ = 0x1869f;
		for(int j = 0; j < vertexAmt; j++) {
			int k = vertexX[j];
			int l = vertexY[j];
			int i1 = vertexZ[j];
			if(upscaled) {
				k >>= 2;
				l >>= 2;
				i1 >>= 2;
			}
			if(k < minVertexX) {
				minVertexX = k;
			}
			if(k > maxVertexX) {
				maxVertexX = k;
			}
			if(i1 < minVertexZ) {
				minVertexZ = i1;
			}
			if(i1 > maxVertexZ) {
				maxVertexZ = i1;
			}
			if(-l > super.maxVerticalDistUp) {
				super.maxVerticalDistUp = -l;
			}
			if(l > maxVerticalDistDown) {
				maxVerticalDistDown = l;
			}
			final int j1 = k * k + i1 * i1;
			if(j1 > maxHorizontalDist) {
				maxHorizontalDist = j1;
			}
		}
		maxHorizontalDist = (int) Math.sqrt(maxHorizontalDist);
		maxDiagonalDistUp = (int) Math.sqrt(maxHorizontalDist * maxHorizontalDist + super.maxVerticalDistUp * super.maxVerticalDistUp);
		maxDiagonalDistUpAndDown = maxDiagonalDistUp + (int) Math.sqrt(maxHorizontalDist * maxHorizontalDist + maxVerticalDistDown * maxVerticalDistDown);
	}

	public void applyEffects() {
		if(vertexSkin != null) {
			final int ai[] = new int[256];
			int j = 0;
			for(int l = 0; l < vertexAmt; l++) {
				final int j1 = vertexSkin[l];
				ai[j1]++;
				if(j1 > j) {
					j = j1;
				}
			}
			anIntArrayArray1657 = new int[j + 1][];
			for(int k1 = 0; k1 <= j; k1++) {
				anIntArrayArray1657[k1] = new int[ai[k1]];
				ai[k1] = 0;
			}
			for(int j2 = 0; j2 < vertexAmt; j2++) {
				final int l2 = vertexSkin[j2];
				anIntArrayArray1657[l2][ai[l2]++] = j2;
			}
			vertexSkin = null;
		}
		if(triSkin != null) {
			final int[] temp = new int[256];
			int max = 0;
			for(int i = 0; i < triAmt; i++) {
				final int blink = triSkin[i];
				temp[blink]++;
				if(blink > max) {
					max = blink;
				}
			}
			triangleSkin = new int[max + 1][];
			for(int i2 = 0; i2 <= max; i2++) {
				triangleSkin[i2] = new int[temp[i2]];
				temp[i2] = 0;
			}
			for(int i = 0; i < triAmt; i++) {
				final int blink = triSkin[i];
				triangleSkin[blink][temp[blink]++] = i;
			}
			triSkin = null;
		}
	}

	public void applyAnimation(int current) {
		if(anIntArrayArray1657 == null) {
			return;
		}
		if(current == -1) {
			return;
		}
		final AnimationFrame animation = AnimationFrame.get(current);
		if(animation == null) {
			return;
		}
		final SkinList skinList = animation.skinList;
		vertex3dDX = 0;
		vertex3dDY = 0;
		vertex3dDZ = 0;
		for(int i = 0; i < animation.skinCount; i++) {
			final int l = animation.anIntArray639[i];
			method472(skinList.anIntArray342[l], skinList.anIntArrayArray343[l], animation.skin3dDX[i], animation.skin3dDY[i], animation.skin3dDZ[i]);
		}
	}

	public void applyAnimation1(int frame, int nextFrame, int end, int cycle) {
		if(anIntArrayArray1657 != null && frame != -1) {
			AnimationFrame currentAnimation = AnimationFrame.get(frame);
			SkinList list1 = currentAnimation.skinList;
			vertex3dDX = 0;
			vertex3dDY = 0;
			vertex3dDZ = 0;
			AnimationFrame nextAnimation = null;
			SkinList list2 = null;
			if(nextFrame != -1) {
				nextAnimation = AnimationFrame.get(nextFrame);
				if(nextAnimation.skinList != list1)
					nextAnimation = null;
				list2 = nextAnimation.skinList;
			}
			if(nextAnimation == null || list2 == null) {
				for(int i_263_ = 0; i_263_ < currentAnimation.skinCount; i_263_++) {
					int i_264_ = currentAnimation.anIntArray639[i_263_];
					method472(list1.anIntArray342[i_264_], list1.anIntArrayArray343[i_264_], currentAnimation.skin3dDX[i_263_], currentAnimation.skin3dDY[i_263_], currentAnimation.skin3dDZ[i_263_]);
				}
			} else {
				for(int i1 = 0; i1 < currentAnimation.skinCount; i1++) {
					int n1 = currentAnimation.anIntArray639[i1];
					int opcode = list1.anIntArray342[n1];
					int[] skin = list1.anIntArrayArray343[n1];
					int x = currentAnimation.skin3dDX[i1];
					int y = currentAnimation.skin3dDY[i1];
					int z = currentAnimation.skin3dDZ[i1];
					boolean found = false;
					for(int i2 = 0; i2 < nextAnimation.skinCount; i2++) {
						int n2 = nextAnimation.anIntArray639[i2];
						if(list2.anIntArrayArray343[n2].equals(skin)) {
							if(opcode != 2) {
								x += (nextAnimation.skin3dDX[i2] - x) * cycle / end;
								y += (nextAnimation.skin3dDY[i2] - y) * cycle / end;
								z += (nextAnimation.skin3dDZ[i2] - z) * cycle / end;
							} else {
								x &= 0xff;
								y &= 0xff;
								z &= 0xff;
								int dx = nextAnimation.skin3dDX[i2] - x & 0xff;
								int dy = nextAnimation.skin3dDY[i2] - y & 0xff;
								int dz = nextAnimation.skin3dDZ[i2] - z & 0xff;
								if(dx >= 128) {
									dx -= 256;
								}
								if(dy >= 128) {
									dy -= 256;
								}
								if(dz >= 128) {
									dz -= 256;
								}
								x = x + dx * cycle / end & 0xff;
								y = y + dy * cycle / end & 0xff;
								z = z + dz * cycle / end & 0xff;
							}
							found = true;
							break;
						}
					}
					if(!found) {
						if(opcode != 3 && opcode != 2) {
							x = x * (end - cycle) / end;
							y = y * (end - cycle) / end;
							z = z * (end - cycle) / end;
						} else if(opcode == 3) {
							x = (x * (end - cycle) + (cycle << 7)) / end;
							y = (y * (end - cycle) + (cycle << 7)) / end;
							z = (z * (end - cycle) + (cycle << 7)) / end;
						} else {
							x &= 0xff;
							y &= 0xff;
							z &= 0xff;
							int dx = -x & 0xff;
							int dy = -y & 0xff;
							int dz = -z & 0xff;
							if(dx >= 128) {
								dx -= 256;
							}
							if(dy >= 128) {
								dy -= 256;
							}
							if(dz >= 128) {
								dz -= 256;
							}
							x = x + dx * cycle / end & 0xff;
							y = y + dy * cycle / end & 0xff;
							z = z + dz * cycle / end & 0xff;
						}
					}
					method472(opcode, skin, x, y, z);
				}
			}
		}
	}

	public void applyAnimation(int currentId, int nextId, int cycle, int length) {
		if(anIntArrayArray1657 == null) {
			return;
		}
		if(currentId == -1) {
			return;
		}
		final AnimationFrame currAnim = AnimationFrame.get(currentId);
		if(currAnim == null) {
			return;
		}
		final SkinList skinList = currAnim.skinList;
		AnimationFrame nextAnim = null;
		if(nextId != -1) {
			nextAnim = AnimationFrame.get(nextId);
			if(nextAnim == null)
				return;
			if(nextAnim.skinList != skinList) {
				nextAnim = null;
			}
		}
		vertex3dDX = 0;
		vertex3dDY = 0;
		vertex3dDZ = 0;
		if(nextAnim == null) {
			for(int i = 0; i < currAnim.skinCount; i++) {
				int i_264_ = currAnim.anIntArray639[i];
				method472(skinList.anIntArray342[i_264_], skinList.anIntArrayArray343[i_264_], currAnim.skin3dDX[i], currAnim.skin3dDY[i], currAnim.skin3dDZ[i]);
			}
		} else {
			int currFrameId = 0;
			int nextFrameId = 0;
			for(int tlistId = 0; tlistId < skinList.length; tlistId++) {
				boolean bool = false;
				if(currFrameId < currAnim.skinCount && currAnim.anIntArray639[currFrameId] == tlistId) {
					bool = true;
				}
				boolean bool_76_ = false;
				if(nextFrameId < nextAnim.skinCount && nextAnim.anIntArray639[nextFrameId] == tlistId) {
					bool_76_ = true;
				}
				if(bool || bool_76_) {
					int defaultModifier = 0;
					int opcode = skinList.anIntArray342[tlistId];
					if(opcode == 3) {
						defaultModifier = 128;
					}
					int currAnimX;
					int currAnimY;
					int currAnimZ;
					if(bool) {
						currAnimX = currAnim.skin3dDX[currFrameId];
						currAnimY = currAnim.skin3dDY[currFrameId];
						currAnimZ = currAnim.skin3dDZ[currFrameId];
						currFrameId++;
					} else {
						currAnimX = defaultModifier;
						currAnimY = defaultModifier;
						currAnimZ = defaultModifier;
					}
					int nextAnimX;
					int nextAnimY;
					int nextAnimZ;
					if(bool_76_) {
						nextAnimX = nextAnim.skin3dDX[nextFrameId];
						nextAnimY = nextAnim.skin3dDY[nextFrameId];
						nextAnimZ = nextAnim.skin3dDZ[nextFrameId];
						nextFrameId++;
					} else {
						nextAnimX = defaultModifier;
						nextAnimY = defaultModifier;
						nextAnimZ = defaultModifier;
					}
					int interpolatedX;
					int interpolatedY;
					int interpolatedZ;
					/*if (opcode == 2) {
						int deltaX = nextAnimX - currAnimX & 0x7ff;
						int deltaY = nextAnimY - currAnimY & 0x7ff;
						int deltaZ = nextAnimZ - currAnimZ & 0x7ff;
						if (deltaX >= 1024) {
							deltaX -= 2048;
						}
						if (deltaY >= 1024) {
							deltaY -= 2048;
						}
						if (deltaZ >= 1024) {
							deltaZ -= 2048;
						}
						interpolatedX = currAnimX + deltaX * cycle / end & 0x7ff;
						interpolatedY = currAnimY + deltaY * cycle / end & 0x7ff;
						interpolatedZ = currAnimZ + deltaZ * cycle / end & 0x7ff;
					} else {
						interpolatedX = currAnimX + (nextAnimX - currAnimX) * cycle / end;
						interpolatedY = currAnimY + (nextAnimY - currAnimY) * cycle / end;
						interpolatedZ = currAnimZ + (nextAnimZ - currAnimZ) * cycle / end;
					}*/
					if(opcode == 2) {
						int deltaX = nextAnimX - currAnimX & 0xff;
						int deltaY = nextAnimY - currAnimY & 0xff;
						int deltaZ = nextAnimZ - currAnimZ & 0xff;
						if(deltaX >= 128) {
							deltaX -= 256;
						}
						if(deltaY >= 128) {
							deltaY -= 256;
						}
						if(deltaZ >= 128) {
							deltaZ -= 256;
						}
						interpolatedX = currAnimX + deltaX * cycle / length & 0xff;
						interpolatedY = currAnimY + deltaY * cycle / length & 0xff;
						interpolatedZ = currAnimZ + deltaZ * cycle / length & 0xff;
					} else {
						interpolatedX = currAnimX + (nextAnimX - currAnimX) * cycle / length;
						interpolatedY = currAnimY + (nextAnimY - currAnimY) * cycle / length;
						interpolatedZ = currAnimZ + (nextAnimZ - currAnimZ) * cycle / length;
					}
					method472(opcode, skinList.anIntArrayArray343[tlistId], interpolatedX, interpolatedY, interpolatedZ);
				}
			}
		}
	}

	public void method471(int[] ai, int j, int animframe) {
		if(animframe == -1) {
			return;
		}
		if(ai == null || j == -1) {
			applyAnimation(animframe);
			return;
		}
		final AnimationFrame class36 = AnimationFrame.get(animframe);
		if(class36 == null) {
			return;
		}
		final AnimationFrame class36_1 = AnimationFrame.get(j);
		if(class36_1 == null) {
			applyAnimation(animframe);
			return;
		}
		final SkinList class18 = class36.skinList;
		vertex3dDX = 0;
		vertex3dDY = 0;
		vertex3dDZ = 0;
		int l = 0;
		int i1 = ai[l++];
		for(int j1 = 0; j1 < class36.skinCount; j1++) {
			int k1;
			for(k1 = class36.anIntArray639[j1]; k1 > i1; i1 = ai[l++])
				;
			if(k1 != i1 || class18.anIntArray342[k1] == 0) {
				method472(class18.anIntArray342[k1], class18.anIntArrayArray343[k1], class36.skin3dDX[j1], class36.skin3dDY[j1], class36.skin3dDZ[j1]);
			}
		}
		vertex3dDX = 0;
		vertex3dDY = 0;
		vertex3dDZ = 0;
		l = 0;
		i1 = ai[l++];
		for(int l1 = 0; l1 < class36_1.skinCount; l1++) {
			int i2;
			for(i2 = class36_1.anIntArray639[l1]; i2 > i1; i1 = ai[l++])
				;
			if(i2 == i1 || class18.anIntArray342[i2] == 0) {
				method472(class18.anIntArray342[i2], class18.anIntArrayArray343[i2], class36_1.skin3dDX[l1], class36_1.skin3dDY[l1], class36_1.skin3dDZ[l1]);
			}
		}
	}

	private void method472(int code, int[] ai, int dx, int dy, int dz) {
		final int i1 = ai.length;
		if(code == 0) {
			int j1 = 0;
			vertex3dDX = 0;
			vertex3dDY = 0;
			vertex3dDZ = 0;
			for(final int l3 : ai) {
				if(l3 < anIntArrayArray1657.length) {
					final int ai5[] = anIntArrayArray1657[l3];
					for(final int j6 : ai5) {
						vertex3dDX += vertexX[j6] >> (upscaled ? 2 : 0);
						vertex3dDY += vertexY[j6] >> (upscaled ? 2 : 0);
						vertex3dDZ += vertexZ[j6] >> (upscaled ? 2 : 0);
						j1++;
					}
				}
			}
			if(j1 > 0) {
				vertex3dDX = vertex3dDX / j1 + dx;
				vertex3dDY = vertex3dDY / j1 + dy;
				vertex3dDZ = vertex3dDZ / j1 + dz;
				return;
			} else {
				vertex3dDX = dx;
				vertex3dDY = dy;
				vertex3dDZ = dz;
				return;
			}
		}
		if(code == 1) { // translate
			for(final int l2 : ai) {
				if(l2 < anIntArrayArray1657.length) {
					final int ai1[] = anIntArrayArray1657[l2];
					for(final int element : ai1) {
						final int j5 = element;
						vertexX[j5] += dx << (upscaled ? 2 : 0);
						vertexY[j5] += dy << (upscaled ? 2 : 0);
						vertexZ[j5] += dz << (upscaled ? 2 : 0);
					}
				}
			}
			return;
		}
		if(code == 2) { // rotate
			for(final int i3 : ai) {
				if(i3 < anIntArrayArray1657.length) {
					final int ai2[] = anIntArrayArray1657[i3];
					for(final int element : ai2) {
						final int k5 = element;
						vertexX[k5] -= vertex3dDX << (upscaled ? 2 : 0);
						vertexY[k5] -= vertex3dDY << (upscaled ? 2 : 0);
						vertexZ[k5] -= vertex3dDZ << (upscaled ? 2 : 0);
						final int k6 = (dx & 0xff) * 8;
						final int l6 = (dy & 0xff) * 8;
						final int i7 = (dz & 0xff) * 8;
						if(i7 != 0) {
							final int j7 = angleSine[i7];
							final int i8 = angleCosine[i7];
							final int l8 = vertexY[k5] * j7 + vertexX[k5] * i8 >> 16;
							vertexY[k5] = vertexY[k5] * i8 - vertexX[k5] * j7 >> 16;
							vertexX[k5] = l8;
						}
						if(k6 != 0) {
							final int k7 = angleSine[k6];
							final int j8 = angleCosine[k6];
							final int i9 = vertexY[k5] * j8 - vertexZ[k5] * k7 >> 16;
							vertexZ[k5] = vertexY[k5] * k7 + vertexZ[k5] * j8 >> 16;
							vertexY[k5] = i9;
						}
						if(l6 != 0) {
							final int l7 = angleSine[l6];
							final int k8 = angleCosine[l6];
							final int j9 = vertexZ[k5] * l7 + vertexX[k5] * k8 >> 16;
							vertexZ[k5] = vertexZ[k5] * k8 - vertexX[k5] * l7 >> 16;
							vertexX[k5] = j9;
						}
						vertexX[k5] += vertex3dDX << (upscaled ? 2 : 0);
						vertexY[k5] += vertex3dDY << (upscaled ? 2 : 0);
						vertexZ[k5] += vertex3dDZ << (upscaled ? 2 : 0);
					}
				}
			}
			return;
		}
		if(code == 3) { // scale
			for(final int j3 : ai) {
				if(j3 < anIntArrayArray1657.length) {
					final int ai3[] = anIntArrayArray1657[j3];
					for(final int element : ai3) {
						final int l5 = element;
						vertexX[l5] -= vertex3dDX << (upscaled ? 2 : 0);
						vertexY[l5] -= vertex3dDY << (upscaled ? 2 : 0);
						vertexZ[l5] -= vertex3dDZ << (upscaled ? 2 : 0);
						vertexX[l5] = vertexX[l5] * dx / 128;
						vertexY[l5] = vertexY[l5] * dy / 128;
						vertexZ[l5] = vertexZ[l5] * dz / 128;
						vertexX[l5] += vertex3dDX << (upscaled ? 2 : 0);
						vertexY[l5] += vertex3dDY << (upscaled ? 2 : 0);
						vertexZ[l5] += vertex3dDZ << (upscaled ? 2 : 0);
					}
				}
			}
			return;
		}
		// adjust alpha
		if(code == 5 && triangleSkin != null && triAlpha != null) {
			if(triangleSkin != null) {
				for(int t_skin : ai) {
					if(t_skin < triangleSkin.length) {
						int[] triangle_list = triangleSkin[t_skin];
						for(int tri_idx : triangle_list) {

							int i_233_ = (triAlpha[tri_idx] & 0xff) + 8 * dx;
							if(i_233_ >= 0) {
								if(i_233_ > 255) {
									i_233_ = 255;
								}
							} else {
								i_233_ = 0;
							}
							triAlpha[tri_idx] = (byte) i_233_;
						}
					}
				}
			}
		}
	}

	public void rotate90() {
		for(int i = 0; i < vertexAmt; i++) {
			final int x = vertexX[i];
			vertexX[i] = vertexZ[i];
			vertexZ[i] = -x;
		}
	}

	public void rotate(int angle) {
		final int sin = angleSine[angle];
		final int cos = angleCosine[angle];
		for(int i = 0; i < vertexAmt; i++) {
			final int x = vertexY[i] * cos - vertexZ[i] * sin >> 16;
			vertexZ[i] = vertexY[i] * sin + vertexZ[i] * cos >> 16;
			vertexY[i] = x;
		}
	}

	public void translate(int x, int y, int z) {
		if(upscaled) {
			x <<= 2;
			y <<= 2;
			z <<= 2;
		}
		for(int i = 0; i < vertexAmt; i++) {
			vertexX[i] += x;
			vertexY[i] += y;
			vertexZ[i] += z;
		}
	}

	public void replaceHsl(int from, int to) {
		for(int i = 0; i < triAmt; i++) {
			if(triFill[i] == from) {
				triFill[i] = to;
			}
		}
	}

	public final void setTexture(final short oldTextureId, final short newTextureId) {
		for(int id = 0; id < triAmt; id++) {
			if(triTex != null)
				if(triTex[id] == oldTextureId) {
					triTex[id] = newTextureId;
				}
		}
	}

	public void insideOut() {
		for(int i = 0; i < vertexAmt; i++) {
			vertexZ[i] = -vertexZ[i];
		}
		for(int i = 0; i < triAmt; i++) {
			final short x = vertexIndex3d1[i];
			vertexIndex3d1[i] = vertexIndex3d3[i];
			vertexIndex3d3[i] = x;
		}
	}

	public void scale(int x, int y, int z) {
		for(int i = 0; i < vertexAmt; i++) {
			vertexX[i] = vertexX[i] * x >> 7;
			vertexY[i] = vertexY[i] * y >> 7;
			vertexZ[i] = vertexZ[i] * z >> 7;
		}
	}

	public void calculateLighting(int lightness, int contrast, int lightx, int lighty, int lightz, boolean flag) {
		final int distance = (int) Math.sqrt(lightx * lightx + lighty * lighty + lightz * lightz);
		final int intensity = contrast * distance >> 8;
		if(triCol1 == null) {
			triCol1 = new int[triAmt];
			triCol2 = new int[triAmt];
			triCol3 = new int[triAmt];
		}
		if(super.vectorX == null) {
			super.vectorX = new int[vertexAmt];
			super.vectorY = new int[vertexAmt];
			super.vectorZ = new int[vertexAmt];
			super.vectorMagnitude = new int[vertexAmt];
		}
		for(int i2 = 0; i2 < triAmt; i2++) {
			if(triFill[i2] == 65535 || triFill[i2] == 16705 || triFill[i2] == 37798) {
				triFill[i2] = 255;
			}
			final int j2 = vertexIndex3d1[i2];
			final int l2 = vertexIndex3d2[i2];
			final int i3 = vertexIndex3d3[i2];
			final int j3 = vertexX[l2] - vertexX[j2] >> (upscaled ? 2 : 0);
			final int k3 = vertexY[l2] - vertexY[j2] >> (upscaled ? 2 : 0);
			final int l3 = vertexZ[l2] - vertexZ[j2] >> (upscaled ? 2 : 0);
			final int i4 = vertexX[i3] - vertexX[j2] >> (upscaled ? 2 : 0);
			final int j4 = vertexY[i3] - vertexY[j2] >> (upscaled ? 2 : 0);
			final int k4 = vertexZ[i3] - vertexZ[j2] >> (upscaled ? 2 : 0);
			int l4 = k3 * k4 - j4 * l3;
			int i5 = l3 * i4 - k4 * j3;
			int j5;
			for(j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192 || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1) {
				l4 >>= 1;
				i5 >>= 1;
			}
			int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
			if(k5 <= 0) {
				k5 = 1;
			}
			l4 = (l4 << 8) / k5;
			i5 = (i5 << 8) / k5;
			j5 = (j5 << 8) / k5;
			if(triType == null || (triType[i2] & 1) == 0) {
				if(j2 < vectorX.length) {
					super.vectorX[j2] += l4;
					super.vectorY[j2] += i5;
					super.vectorZ[j2] += j5;
					super.vectorMagnitude[j2]++;
				}
				if(l2 < vectorX.length) {
					super.vectorX[l2] += l4;
					super.vectorY[l2] += i5;
					super.vectorZ[l2] += j5;
					super.vectorMagnitude[l2]++;
				}
				if(i3 < vectorX.length) {
					super.vectorX[i3] += l4;
					super.vectorY[i3] += i5;
					super.vectorZ[i3] += j5;
					super.vectorMagnitude[i3]++;
				}
			} else {
				final int light = lightness + (lightx * l4 + lighty * i5 + lightz * j5) / intensity;
				triCol1[i2] = adjustLightness(triFill[i2], light, triType[i2]);
			}
		}
		if(flag) {
			doShading(lightness, intensity, lightx, lighty, lightz);
		} else {
			System.arraycopy(super.vectorX, 0, vectorNormalX, 0, super.vectorX.length);
			System.arraycopy(super.vectorY, 0, vectorNormalY, 0, super.vectorY.length);
			System.arraycopy(super.vectorZ, 0, vectorNormalZ, 0, super.vectorZ.length);
			System.arraycopy(super.vectorMagnitude, 0, vectorNormalMagnitude, 0, super.vectorMagnitude.length);
			//vectorNormalX = super.vectorX;
			//vectorNormalY = super.vectorY;
			//vectorNormalZ = super.vectorZ;
			//vectorNormalMagnitude = super.vectorMagnitude;
		}
		if(flag) {
			computeBoundsDist();
		} else {
			computeBoundsAll();
		}
	}

	public void doShading(int lightness, int contrast, int lightx, int lighty, int lightz) {
		for(int i = 0; i < triAmt; i++) {
			final int x3d = vertexIndex3d1[i];
			final int y3d = vertexIndex3d2[i];
			final int z3d = vertexIndex3d3[i];
			if(triType == null) {
				final int tex = triFill[i];
				int light;
				if(x3d < vectorX.length) {
					light = lightness + (lightx * vectorX[x3d] + lighty * vectorY[x3d] + lightz * vectorZ[x3d]) / (contrast * vectorMagnitude[x3d]);
					triCol1[i] = adjustLightness(tex, light, 0);
				}
				if(y3d < vectorX.length) {
					light = lightness + (lightx * vectorX[y3d] + lighty * vectorY[y3d] + lightz * vectorZ[y3d]) / (contrast * vectorMagnitude[y3d]);
					triCol2[i] = adjustLightness(tex, light, 0);
				}
				if(z3d < vectorX.length) {
					light = lightness + (lightx * vectorX[z3d] + lighty * vectorY[z3d] + lightz * vectorZ[z3d]) / (contrast * vectorMagnitude[z3d]);
					triCol3[i] = adjustLightness(tex, light, 0);
				}
			} else if((triType[i] & 1) == 0) {
				final int tex = triFill[i];
				final int type = triType[i];
				int light = lightness + (lightx * vectorX[x3d] + lighty * vectorY[x3d] + lightz * vectorZ[x3d]) / (contrast * vectorMagnitude[x3d]);
				triCol1[i] = adjustLightness(tex, light, type);
				light = lightness + (lightx * vectorX[y3d] + lighty * vectorY[y3d] + lightz * vectorZ[y3d]) / (contrast * vectorMagnitude[y3d]);
				triCol2[i] = adjustLightness(tex, light, type);
				light = lightness + (lightx * vectorX[z3d] + lighty * vectorY[z3d] + lightz * vectorZ[z3d]) / (contrast * vectorMagnitude[z3d]);
				triCol3[i] = adjustLightness(tex, light, type);
			}
		}
		super.vectorX = null;
		super.vectorY = null;
		super.vectorZ = null;
		super.vectorMagnitude = null;
		vectorNormalX = null;
		vectorNormalY = null;
		vectorNormalZ = null;
		vectorNormalMagnitude = null;
		vertexSkin = null;
		triSkin = null;
		if(triType != null) {
			for(int l1 = 0; l1 < triAmt; l1++) {
				if((triType[l1] & 2) == 2) {
					return;
				}
			}
		}
		triFill = null;
	}

	private void method483(boolean flag, boolean flag1, long hash) {
		for(int j = 0; j < maxDiagonalDistUpAndDown; j++) {
			anIntArray1671[j] = 0;
		}
		for(int k = 0; k < triAmt; k++) {
			if(triType == null || triType[k] != -1) {
				final int l = vertexIndex3d1[k];
				final int k1 = vertexIndex3d2[k];
				final int j2 = vertexIndex3d3[k];
				final int i3 = projVertexX[l];
				final int l3 = projVertexX[k1];
				final int k4 = projVertexX[j2];
				if(flag && (i3 == -5000 || l3 == -5000 || k4 == -5000)) {
					projTriClipZ[k] = true;
					final int j5 = (projVertexLocalZ[l] + projVertexLocalZ[k1] + projVertexLocalZ[j2]) / 3 + maxDiagonalDistUp;
					anIntArrayArray1672[j5][anIntArray1671[j5]++] = k;
				} else {
					if(flag1 && method486(hoverX, hoverY, projVertexY[l], projVertexY[k1], projVertexY[j2], i3, l3, k4)) {
						modelHover[modelHoverAmt++] = hash;
						flag1 = false;
					}
					if((i3 - l3) * (projVertexY[j2] - projVertexY[k1]) - (projVertexY[l] - projVertexY[k1]) * (k4 - l3) > 0) {
						projTriClipZ[k] = false;
						projTriClipX[k] = i3 < 0 || l3 < 0 || k4 < 0 || i3 > Rasterizer3D.viewport.width || l3 > Rasterizer3D.viewport.width || k4 > Rasterizer3D.viewport.width;
						final int k5 = (projVertexLocalZ[l] + projVertexLocalZ[k1] + projVertexLocalZ[j2]) / 3 + maxDiagonalDistUp;
						if(k5 > anIntArrayArray1672.length || k5 > anIntArray1671.length) {
							return;
						}
						anIntArrayArray1672[k5][anIntArray1671[k5]++] = k;
					}
				}
			}
		}
		if(triPri == null) {
			for(int i1 = maxDiagonalDistUpAndDown - 1; i1 >= 0; i1--) {
				final int l1 = anIntArray1671[i1];
				if(l1 > 0) {
					final int ai[] = anIntArrayArray1672[i1];
					for(int j3 = 0; j3 < l1; j3++) {
						drawTriangle(ai[j3]);
					}
				}
			}
			return;
		}
		for(int j1 = 0; j1 < 12; j1++) {
			anIntArray1673[j1] = 0;
			anIntArray1677[j1] = 0;
		}
		for(int i2 = maxDiagonalDistUpAndDown - 1; i2 >= 0; i2--) {
			final int k2 = anIntArray1671[i2];
			if(k2 > 0) {
				final int ai1[] = anIntArrayArray1672[i2];
				for(int i4 = 0; i4 < k2; i4++) {
					final int l4 = ai1[i4];
					final int l5 = triPri[l4];
					final int j6 = anIntArray1673[l5]++;
					anIntArrayArray1674[l5][j6] = l4;
					if(l5 < 10) {
						anIntArray1677[l5] += i2;
					} else if(l5 == 10) {
						anIntArray1675[j6] = i2;
					} else {
						anIntArray1676[j6] = i2;
					}
				}
			}
		}

		int l2 = 0;
		if(anIntArray1673[1] > 0 || anIntArray1673[2] > 0) {
			l2 = (anIntArray1677[1] + anIntArray1677[2]) / (anIntArray1673[1] + anIntArray1673[2]);
		}
		int k3 = 0;
		if(anIntArray1673[3] > 0 || anIntArray1673[4] > 0) {
			k3 = (anIntArray1677[3] + anIntArray1677[4]) / (anIntArray1673[3] + anIntArray1673[4]);
		}
		int j4 = 0;
		if(anIntArray1673[6] > 0 || anIntArray1673[8] > 0) {
			j4 = (anIntArray1677[6] + anIntArray1677[8]) / (anIntArray1673[6] + anIntArray1673[8]);
		}
		int tri = 0;
		int k6 = anIntArray1673[10];
		int ai2[] = anIntArrayArray1674[10];
		int ai3[] = anIntArray1675;
		if(tri == k6) {
			tri = 0;
			k6 = anIntArray1673[11];
			ai2 = anIntArrayArray1674[11];
			ai3 = anIntArray1676;
		}
		int i5;
		if(tri < k6) {
			i5 = ai3[tri];
		} else {
			i5 = -1000;
		}
		for(int l6 = 0; l6 < 10; l6++) {
			while(l6 == 0 && i5 > l2) {
				drawTriangle(ai2[tri++]);
				if(tri == k6 && ai2 != anIntArrayArray1674[11]) {
					tri = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if(tri < k6) {
					i5 = ai3[tri];
				} else {
					i5 = -1000;
				}
			}
			while(l6 == 3 && i5 > k3) {
				drawTriangle(ai2[tri++]);
				if(tri == k6 && ai2 != anIntArrayArray1674[11]) {
					tri = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if(tri < k6) {
					i5 = ai3[tri];
				} else {
					i5 = -1000;
				}
			}
			while(l6 == 5 && i5 > j4) {
				drawTriangle(ai2[tri++]);
				if(tri == k6 && ai2 != anIntArrayArray1674[11]) {
					tri = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if(tri < k6) {
					i5 = ai3[tri];
				} else {
					i5 = -1000;
				}
			}
			final int i7 = anIntArray1673[l6];
			final int ai4[] = anIntArrayArray1674[l6];
			for(int j7 = 0; j7 < i7; j7++) {
				drawTriangle(ai4[j7]);
			}
		}
		while(i5 != -1000) {
			drawTriangle(ai2[tri++]);
			if(tri == k6 && ai2 != anIntArrayArray1674[11]) {
				tri = 0;
				ai2 = anIntArrayArray1674[11];
				k6 = anIntArray1673[11];
				ai3 = anIntArray1676;
			}
			if(tri < k6) {
				i5 = ai3[tri];
			} else {
				i5 = -1000;
			}
		}
	}

	private void drawTriangleOrQuad(int idx) {
		if(triFill != null && triAlpha != null)
			if(triFill[idx] == 65535)
				return;// XXX: ARTEM FIX FOR BLACK MAPS.
		final int centerX = Rasterizer3D.viewport.centerX;
		final int centerY = Rasterizer3D.viewport.centerY;
		int pos = 0;
		final int v1 = vertexIndex3d1[idx];
		final int v2 = vertexIndex3d2[idx];
		final int v3 = vertexIndex3d3[idx];
		final int l1 = projTexVertexZ[v1];
		final int i2 = projTexVertexZ[v2];
		final int j2 = projTexVertexZ[v3];
		if(l1 >= Constants.CAM_NEAR) {
			triReqX[pos] = projVertexX[v1];
			triReqY[pos] = projVertexY[v1];
			triReqCol[pos++] = triCol1[idx];
		} else {
			final int k2 = projTexVertexX[v1];
			final int k3 = projTexVertexY[v1];
			final int k4 = triCol1[idx];
			if(j2 >= Constants.CAM_NEAR) {
				final int k5 = (Constants.CAM_NEAR - l1) * shadeAmt[j2 - l1];
				triReqX[pos] = centerX + ((k2 + ((projTexVertexX[v3] - k2) * k5 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqY[pos] = centerY + ((k3 + ((projTexVertexY[v3] - k3) * k5 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqCol[pos++] = k4 + ((triCol3[idx] - k4) * k5 >> 16);
			}
			if(i2 >= Constants.CAM_NEAR) {
				final int l5 = (Constants.CAM_NEAR - l1) * shadeAmt[i2 - l1];
				triReqX[pos] = centerX + ((k2 + ((projTexVertexX[v2] - k2) * l5 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqY[pos] = centerY + ((k3 + ((projTexVertexY[v2] - k3) * l5 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqCol[pos++] = k4 + ((triCol2[idx] - k4) * l5 >> 16);
			}
		}
		if(i2 >= Constants.CAM_NEAR) {
			triReqX[pos] = projVertexX[v2];
			triReqY[pos] = projVertexY[v2];
			triReqCol[pos++] = triCol2[idx];
		} else {
			final int l2 = projTexVertexX[v2];
			final int l3 = projTexVertexY[v2];
			final int l4 = triCol2[idx];
			if(l1 >= Constants.CAM_NEAR) {
				final int i6 = (Constants.CAM_NEAR - i2) * shadeAmt[l1 - i2];
				triReqX[pos] = centerX + ((l2 + ((projTexVertexX[v1] - l2) * i6 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqY[pos] = centerY + ((l3 + ((projTexVertexY[v1] - l3) * i6 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqCol[pos++] = l4 + ((triCol1[idx] - l4) * i6 >> 16);
			}
			if(j2 >= Constants.CAM_NEAR) {
				final int j6 = (Constants.CAM_NEAR - i2) * shadeAmt[j2 - i2];
				triReqX[pos] = centerX + ((l2 + ((projTexVertexX[v3] - l2) * j6 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqY[pos] = centerY + ((l3 + ((projTexVertexY[v3] - l3) * j6 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqCol[pos++] = l4 + ((triCol3[idx] - l4) * j6 >> 16);
			}
		}
		if(j2 >= Constants.CAM_NEAR) {
			triReqX[pos] = projVertexX[v3];
			triReqY[pos] = projVertexY[v3];
			triReqCol[pos++] = triCol3[idx];
		} else {
			final int i3 = projTexVertexX[v3];
			final int i4 = projTexVertexY[v3];
			final int i5 = triCol3[idx];
			if(i2 >= Constants.CAM_NEAR) {
				final int k6 = (Constants.CAM_NEAR - j2) * shadeAmt[i2 - j2];
				triReqX[pos] = centerX + ((i3 + ((projTexVertexX[v2] - i3) * k6 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqY[pos] = centerY + ((i4 + ((projTexVertexY[v2] - i4) * k6 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqCol[pos++] = i5 + ((triCol2[idx] - i5) * k6 >> 16);
			}
			if(l1 >= Constants.CAM_NEAR) {
				final int l6 = (Constants.CAM_NEAR - j2) * shadeAmt[l1 - j2];
				triReqX[pos] = centerX + ((i3 + ((projTexVertexX[v1] - i3) * l6 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqY[pos] = centerY + ((i4 + ((projTexVertexY[v1] - i4) * l6 >> 16)) * Scene.focalLength) / Constants.CAM_NEAR;
				triReqCol[pos++] = i5 + ((triCol1[idx] - i5) * l6 >> 16);
			}
		}
		final int x1 = triReqX[0];
		final int x2 = triReqX[1];
		final int x3 = triReqX[2];
		final int y1 = triReqY[0];
		final int y2 = triReqY[1];
		final int y3 = triReqY[2];
		if((x1 - x2) * (y3 - y2) - (y1 - y2) * (x3 - x2) > 0) {
			Rasterizer3D.clippedScan = false;
			if(pos == 3) {
				if(x1 < 0 || x2 < 0 || x3 < 0 || x1 > Rasterizer3D.viewport.width || x2 > Rasterizer3D.viewport.width || x3 > Rasterizer3D.viewport.width) {
					Rasterizer3D.clippedScan = true;
				}
				int meshType;
				if(triType == null) {
					meshType = 0;
				} else {
					meshType = triType[idx] & 3;
				}
				if(meshType == 0) {
					Rasterizer3D.drawGouraudTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, triReqCol[0], triReqCol[1], triReqCol[2]);
				} else if(meshType == 1) {
					Rasterizer3D.drawFlatTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, hslToRgbMap[triCol1[idx]]);
				} else if(meshType == 2) {
					final int j8 = triType[idx] >> 2;
					final int k9 = texVertex1[j8] & 0xffff;
					final int k10 = texVertex2[j8] & 0xffff;
					final int k11 = texVertex3[j8] & 0xffff;
					Rasterizer3D.drawTexturedTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, triReqCol[0], triReqCol[1], triReqCol[2], projTexVertexX[k9], projTexVertexX[k10], projTexVertexX[k11], projTexVertexY[k9], projTexVertexY[k10], projTexVertexY[k11], projTexVertexZ[k9], projTexVertexZ[k10], projTexVertexZ[k11], triFill[idx], false, false);
				} else if(meshType == 3) {
					final int k8 = triType[idx] >> 2;
					final int l9 = texVertex1[k8] & 0xffff;
					final int l10 = texVertex2[k8] & 0xffff;
					final int l11 = texVertex3[k8] & 0xffff;
					Rasterizer3D.drawTexturedTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, triCol1[idx], triCol1[idx], triCol1[idx], projTexVertexX[l9], projTexVertexX[l10], projTexVertexX[l11], projTexVertexY[l9], projTexVertexY[l10], projTexVertexY[l11], projTexVertexZ[l9], projTexVertexZ[l10], projTexVertexZ[l11], triFill[idx], false, false);
				}
			}
			if(pos == 4) {
				if(x1 < 0 || x2 < 0 || x3 < 0 || x1 > Rasterizer3D.viewport.width || x2 > Rasterizer3D.viewport.width || x3 > Rasterizer3D.viewport.width || triReqX[3] < Rasterizer2D.clipStartX || triReqX[3] > Rasterizer2D.clipEndX) {
					Rasterizer3D.clippedScan = true;
				}
				int i8;
				if(triType == null) {
					i8 = 0;
				} else {
					i8 = triType[idx] & 3;
				}
				if(i8 == 0) {
					Rasterizer3D.drawGouraudTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, triReqCol[0], triReqCol[1], triReqCol[2]);
					Rasterizer3D.drawGouraudTriangle(y1, y3, triReqY[3], x1, x3, triReqX[3], 0, 0, 0, triReqCol[0], triReqCol[2], triReqCol[3]);
					return;
				}
				if(i8 == 1) {
					final int l8 = hslToRgbMap[triCol1[idx]];
					Rasterizer3D.drawFlatTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, l8);
					Rasterizer3D.drawFlatTriangle(y1, y3, triReqY[3], x1, x3, triReqX[3], 0, 0, 0, l8);
					return;
				}
				if(i8 == 2) {
					final int i9 = triType[idx] >> 2;
					final int i10 = texVertex1[i9];
					final int i11 = texVertex2[i9];
					final int i12 = texVertex3[i9];
					Rasterizer3D.drawTexturedTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, triReqCol[0], triReqCol[1], triReqCol[2], projTexVertexX[i10], projTexVertexX[i11], projTexVertexX[i12], projTexVertexY[i10], projTexVertexY[i11], projTexVertexY[i12], projTexVertexZ[i10], projTexVertexZ[i11], projTexVertexZ[i12], triFill[idx], false, false);
					Rasterizer3D.drawTexturedTriangle(y1, y3, triReqY[3], x1, x3, triReqX[3], 0, 0, 0, triReqCol[0], triReqCol[2], triReqCol[3], projTexVertexX[i10], projTexVertexX[i11], projTexVertexX[i12], projTexVertexY[i10], projTexVertexY[i11], projTexVertexY[i12], projTexVertexZ[i10], projTexVertexZ[i11], projTexVertexZ[i12], triFill[idx], false, false);
					return;
				}
				if(i8 == 3) {
					final int j9 = triType[idx] >> 2;
					final int j10 = texVertex1[j9];
					final int j11 = texVertex2[j9];
					final int j12 = texVertex3[j9];
					Rasterizer3D.drawTexturedTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, triCol1[idx], triCol1[idx], triCol1[idx], projTexVertexX[j10], projTexVertexX[j11], projTexVertexX[j12], projTexVertexY[j10], projTexVertexY[j11], projTexVertexY[j12], projTexVertexZ[j10], projTexVertexZ[j11], projTexVertexZ[j12], triFill[idx], false, false);
					Rasterizer3D.drawTexturedTriangle(y1, y3, triReqY[3], x1, x3, triReqX[3], 0, 0, 0, triCol1[idx], triCol1[idx], triCol1[idx], projTexVertexX[j10], projTexVertexX[j11], projTexVertexX[j12], projTexVertexY[j10], projTexVertexY[j11], projTexVertexY[j12], projTexVertexZ[j10], projTexVertexZ[j11], projTexVertexZ[j12], triFill[idx], false, false);
				}
			}
		}
	}

	void calculateMaxDepth(Buffer class475_sub17, Buffer class475_sub17_288_, Buffer class475_sub17_289_) {
		short i = 0;
		short i_290_ = 0;
		short i_291_ = 0;
		int i_292_ = 0;
		for(int i_293_ = 0; i_293_ < triAmt; i_293_++) {
			int i_294_ = class475_sub17_288_.getSByte() & 0xFF;
			int i_295_ = i_294_ & 0x7;
			if(i_295_ == 1) {
				vertexIndex3d1[i_293_] = i = (short) (class475_sub17.getSSmart() + i_292_);
				i_292_ = i;
				vertexIndex3d2[i_293_] = i_290_ = (short) (class475_sub17.getSSmart() + i_292_);
				i_292_ = i_290_;
				vertexIndex3d3[i_293_] = i_291_ = (short) (class475_sub17.getSSmart() + i_292_);
				i_292_ = i_291_;
				if(i > maxDepth)
					maxDepth = i;
				if(i_290_ > maxDepth)
					maxDepth = i_290_;
				if(i_291_ > maxDepth)
					maxDepth = i_291_;
			}
			if(i_295_ == 2) {
				i_290_ = i_291_;
				i_291_ = (short) (class475_sub17.getSSmart() + i_292_);
				i_292_ = i_291_;
				vertexIndex3d1[i_293_] = i;
				vertexIndex3d2[i_293_] = i_290_;
				vertexIndex3d3[i_293_] = i_291_;
				if(i_291_ > maxDepth)
					maxDepth = i_291_;
			}
			if(i_295_ == 3) {
				i = i_291_;
				i_291_ = (short) (class475_sub17.getSSmart() + i_292_);
				i_292_ = i_291_;
				vertexIndex3d1[i_293_] = i;
				vertexIndex3d2[i_293_] = i_290_;
				vertexIndex3d3[i_293_] = i_291_;
				if(i_291_ > maxDepth)
					maxDepth = i_291_;
			}
			if(i_295_ == 4) {
				short i_296_ = i;
				i = i_290_;
				i_290_ = i_296_;
				i_291_ = (short) (class475_sub17.getSSmart() + i_292_);
				i_292_ = i_291_;
				vertexIndex3d1[i_293_] = i;
				vertexIndex3d2[i_293_] = i_290_;
				vertexIndex3d3[i_293_] = i_291_;
				if(i_291_ > maxDepth)
					maxDepth = i_291_;
			}
			if(numUVCoords > 0 && (i_294_ & 0x8) != 0) {
				uvCoordVertexA[i_293_] = (byte) (class475_sub17_289_.getSByte() & 0xFF);
				uvCoordVertexB[i_293_] = (byte) (class475_sub17_289_.getSByte() & 0xFF);
				uvCoordVertexC[i_293_] = (byte) (class475_sub17_289_.getSByte() & 0xFF);
			}
		}
		maxDepth++;
	}

	void decodeTexturedTriangles(Buffer class475_sub17, Buffer class475_sub17_142_, Buffer class475_sub17_143_, Buffer class475_sub17_144_, Buffer class475_sub17_145_, Buffer class475_sub17_146_) {
		for(int i = 0; i < texAmt; i++) {
			int i_147_ = triType[i] & 0xff;
			if(i_147_ == 0) {
				texVertex1[i] = (short) (class475_sub17.getUShort());
				texVertex2[i] = (short) (class475_sub17.getUShort());
				texVertex3[i] = (short) (class475_sub17.getUShort());
			}
			if(i_147_ == 1) {
				texVertex1[i] = (short) (class475_sub17_142_.getUShort());
				texVertex2[i] = (short) (class475_sub17_142_.getUShort());
				texVertex3[i] = (short) (class475_sub17_142_.getUShort());
				if(version < 15) {
					particleDirectionX[i] = class475_sub17_143_.getUShort();
					if(version < 14)
						particleDirectionY[i] = class475_sub17_143_.getUShort();
					else
						particleDirectionY[i] = class475_sub17_143_.getUMedium();
					particleDirectionZ[i] = class475_sub17_143_.getUShort();
				} else {
					particleDirectionX[i] = class475_sub17_143_.getUMedium();
					particleDirectionY[i] = class475_sub17_143_.getUMedium();
					particleDirectionZ[i] = class475_sub17_143_.getUMedium();
				}
				particleLifespanX[i] = class475_sub17_144_.getSByte();
				particleLifespanY[i] = class475_sub17_145_.getSByte();
				particleLifespanZ[i] = class475_sub17_146_.getSByte();
			}
			if(i_147_ == 2) {
				texVertex1[i] = (short) (class475_sub17_142_.getUShort());
				texVertex2[i] = (short) (class475_sub17_142_.getUShort());
				texVertex3[i] = (short) (class475_sub17_142_.getUShort());
				if(version < 15) {
					particleDirectionX[i] = class475_sub17_143_.getUShort();
					if(version < 14)
						particleDirectionY[i] = class475_sub17_143_.getUShort();
					else
						particleDirectionY[i] = class475_sub17_143_.getUMedium();
					particleDirectionZ[i] = class475_sub17_143_.getUShort();
				} else {
					particleDirectionX[i] = class475_sub17_143_.getUMedium();
					particleDirectionY[i] = class475_sub17_143_.getUMedium();
					particleDirectionZ[i] = class475_sub17_143_.getUMedium();
				}
				particleLifespanX[i] = class475_sub17_144_.getSByte();
				particleLifespanY[i] = class475_sub17_145_.getSByte();
				particleLifespanZ[i] = class475_sub17_146_.getSByte();
				texturePrimaryColor[i] = class475_sub17_146_.getSByte();
				textureSecondaryColor[i] = class475_sub17_146_.getSByte();
			}
			if(i_147_ == 3) {
				texVertex1[i] = (short) (class475_sub17_142_.getUShort());
				texVertex2[i] = (short) (class475_sub17_142_.getUShort());
				texVertex3[i] = (short) (class475_sub17_142_.getUShort());
				if(version < 15) {
					particleDirectionX[i] = class475_sub17_143_.getUShort();
					if(version < 14)
						particleDirectionY[i] = class475_sub17_143_.getUShort();
					else
						particleDirectionY[i] = class475_sub17_143_.getUMedium();
					particleDirectionZ[i] = class475_sub17_143_.getUShort();
				} else {
					particleDirectionX[i] = class475_sub17_143_.getUMedium();
					particleDirectionY[i] = class475_sub17_143_.getUMedium();
					particleDirectionZ[i] = class475_sub17_143_.getUMedium();
				}
				particleLifespanX[i] = class475_sub17_144_.getSByte();
				particleLifespanY[i] = class475_sub17_145_.getSByte();
				particleLifespanZ[i] = class475_sub17_146_.getSByte();
			}
		}
	}

	private boolean method486(int i, int j, int k, int l, int i1, int j1, int k1, int l1) {
		if(j < k && j < l && j < i1) {
			return false;
		}
		if(j > k && j > l && j > i1) {
			return false;
		}
		return !(i < j1 && i < k1 && i < l1) && (i <= j1 || i <= k1 || i <= l1);
	}
}