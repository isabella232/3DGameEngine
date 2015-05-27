/*
 * Copyright (C) 2014 Benny Bobaganoosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.engine.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.base.engine.core.Util;
import com.base.engine.rendering.resourceManagement.TextureResource;

public class Texture {
	private static final String ERROR_TEXTURE = "test.png";
	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	private TextureResource resource;
	private final String fileName;

	public Texture(final String fileName) {
		this.fileName = fileName;
		final TextureResource oldResource = Texture.loadedTextures.get(fileName);

		if (oldResource != null) {
			resource = oldResource;
			resource.addReference();
		} else {
			resource = Texture.loadTexture(fileName);
			Texture.loadedTextures.put(fileName, resource);
		}
	}

	@Override
	protected void finalize() {
		if (resource.removeReference() && !fileName.isEmpty()) {
			Texture.loadedTextures.remove(fileName);
		}
	}

	public void bind() {
		bind(0);
	}

	public void bind(final int samplerSlot) {
		assert samplerSlot >= 0 && samplerSlot <= 31;
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.getId());
	}

	public int getID() {
		return resource.getId();
	}

	private static TextureResource loadTexture(final String fileName) {

		final File textureFile = new File("./res/textures/" + fileName);

		// If the file does not exist, we can't load it.
		// Instead of a crash, allow the game to show users that
		// the texture is not found. This will render a checkerboard
		// pattern onto items by default if no texture is found.
		// Example of this: Half Life 2
		if (!textureFile.exists()) {
			// TODO: Is this the best way of handling this?
			if (!Texture.loadedTextures.containsKey(Texture.ERROR_TEXTURE)) {
				Texture.loadedTextures.put(Texture.ERROR_TEXTURE, Texture.loadTexture(Texture.ERROR_TEXTURE));
			}

			return Texture.loadedTextures.get(Texture.ERROR_TEXTURE);

		}

		try {
			final BufferedImage image = ImageIO.read(textureFile);
			final int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

			final ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4);
			final boolean hasAlpha = image.getColorModel().hasAlpha();

			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {
					final int pixel = pixels[y * image.getWidth() + x];

					buffer.put((byte) (pixel >> 16 & 0xFF));
					buffer.put((byte) (pixel >> 8 & 0xFF));
					buffer.put((byte) (pixel & 0xFF));
					if (hasAlpha) {
						buffer.put((byte) (pixel >> 24 & 0xFF));
					} else {
						buffer.put((byte) 0xFF);
					}
				}
			}

			buffer.flip();

			final TextureResource resource = new TextureResource();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.getId());

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

			return resource;
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}
}
