package net.arrav.game.emitter;

import net.arrav.media.img.BitmapImage;

import java.util.Random;

public class ParticleDefinition {

	public static final Random RANDOM = new Random(System.currentTimeMillis());
	
	private ParticleVector gravity;
	
	private float startSize = 1f;
	private float endSize = 1f;
	
	public int particleDepth;
	private int startColor = -1;
	private int endColor = -1;
	
	private ParticleVector startVelocity = ParticleVector.ZERO;
	private ParticleVector endVelocity = ParticleVector.ZERO;
	
	private SpawnShape spawnShape = new PointSpawnShape(ParticleVector.ZERO);
	
	private float startAlpha = 1f;
	private float endAlpha = 0.05f;
	
	private int lifeSpan = 1;
	private int spawnRate = 1;
	private BitmapImage sprite;
	private ParticleVector velocityStep;
	private int colorStep;
	private float sizeStep;
	private float alphaStep;
	
	public static ParticleDefinition[] cache = new ParticleDefinition[] {
		new ParticleDefinition() { {
			//Completionist Cape
            setStartVelocity(new ParticleVector(0, -2, 0));
            setEndVelocity(new ParticleVector(0, -2, 0));
            setGravity(new ParticleVector(0, 2 / 4, 0));
            setLifeSpan(19);
            setStartColor(0xFFFFFF);
            setSpawnRate(4);
            setStartSize(1.25f);
            setEndSize(0);
            setStartAlpha(0.095f);
            updateSteps();
            setColorStep(0x000000);}
		},
        new ParticleDefinition() { {
            //Trimmed Completionist Cape
        	 setStartVelocity(new ParticleVector(0, -2, 0));
             setEndVelocity(new ParticleVector(0, -2, 0));
             setGravity(new ParticleVector(0, 2 / 4, 0));
             setLifeSpan(19);
             setStartColor(0xFFD900);
             setSpawnRate(4);
             setStartSize(1.25f);
             setEndSize(0);
             setStartAlpha(0.095f);
             updateSteps();
             setColorStep(0x000000);}
		},
		new ParticleDefinition() { {
			//Max Cape
			setStartVelocity(new ParticleVector(1, -1, 1));
			setEndVelocity(new ParticleVector(1, -1, 1));
			setGravity(new ParticleVector(0, 2 / 4, 0));
			setLifeSpan(19);
			setStartColor(0x96D0E3);
			setSpawnRate(1);
			setStartSize(1.7f);
			setEndSize(0);
			setStartAlpha(0.100f);
			updateSteps();
			setColorStep(0x000000); }
		},
		new ParticleDefinition() { {
			//Master Dung. Cape
			setStartVelocity(new ParticleVector(0, -1, 0));
			setEndVelocity(new ParticleVector(0, -1, 0));
			setGravity(new ParticleVector(0, 2 / 4, 0));
			setLifeSpan(19);
			setStartColor(0x000000);
			setSpawnRate(4);
			setStartSize(1.25f);
			setEndSize(0.11f);
			setStartAlpha(0.020f);
			updateSteps();
			setColorStep(0x000000); }
		}
	};
	
	public final SpawnShape getSpawnedShape() {
		return spawnShape;
	}

	public final float getStartAlpha() {
		return startAlpha;
	}

	public final void setStartAlpha(float startAlpha) {
		this.startAlpha = startAlpha;
	}

	public final float getAlphaStep() {
		return alphaStep;
	}

	public final BitmapImage getSprite() {
		return sprite;
	}

	public final void setSprite(BitmapImage sprite) {
		this.sprite = sprite;
	}

	public final int getSpawnRate() {
		return this.spawnRate;
	}

	public final void setSpawnRate(int spawnRate) {
		this.spawnRate = spawnRate;
	}
	
	public final void setStartSize(float startSize) {
		this.startSize = startSize;
	}
	
	public final float getStartSize() {
		return startSize;
	}
	
	public float getEndSize() {
		return endSize;
	}
	
    public int getEndColor() {
        return endColor;
    }

	public final void setEndSize(float endSize) {
		this.endSize = endSize;
	}

	public final int getStartColor() {
		return startColor;
	}

	public final void setStartColor(int startColor) {
		this.startColor = startColor;
	}
	
	public int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
	
	public final ParticleVector getStartVelocity(int id) {
		switch (id) {
		default:
			return new ParticleVector(this.startVelocity.getX() + randomWithRange(-1, 1), this.startVelocity.getY() + randomWithRange(0, 0), this.startVelocity.getZ() + randomWithRange(-1, 1));
		}
	}
	
    public ParticleVector getGravity() {
        return gravity;
    }

    public void setGravity(ParticleVector gravity) {
        this.gravity = gravity;
    }

	public final void setStartVelocity(ParticleVector startVelocity) {
		this.startVelocity = startVelocity;
	}
	
    public ParticleVector getEndVelocity() {
        return endVelocity;
    }

	public final void setEndVelocity(ParticleVector endVelocity) {
		this.endVelocity = endVelocity;
	}

	public final int getLifeSpan() {
		return lifeSpan;
	}

	public final void setLifeSpan(int lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	public final void setColorStep(int colorStep) {
		this.colorStep = colorStep;
	}

	public final float getSizeStep() {
		return sizeStep;
	}

	public final ParticleVector getVelocityStep() {
		return velocityStep;
	}

	public final int getColorStep() {
		return colorStep;
	}

	public final void updateSteps() {
		this.sizeStep = (endSize - startSize) / (lifeSpan * 1f);
		this.colorStep = (endColor - startColor) / lifeSpan;
		this.velocityStep = endVelocity.subtract(startVelocity).divide(lifeSpan);
		this.alphaStep = (endAlpha - startAlpha) / lifeSpan;
	}
}