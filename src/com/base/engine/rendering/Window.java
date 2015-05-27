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

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.base.engine.core.math.Vector2f;

public class Window {

	private static final Vector2f CENTER_POSITION = new Vector2f(0, 0);

	public static void createWindow(final int width, final int height, final String title) {
		Display.setTitle(title);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Window.CENTER_POSITION.set(Window.getWidth() / 2, Window.getHeight() / 2);
			Display.create();
			Keyboard.create();
			Mouse.create();
		} catch (final LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void render() {
		Display.update();
	}

	public static void dispose() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}

	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	public static int getWidth() {
		return Display.getDisplayMode().getWidth();
	}

	public static int getHeight() {
		return Display.getDisplayMode().getHeight();
	}

	public static String getTitle() {
		return Display.getTitle();
	}

	public Vector2f getCenter() {
		return new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
	}

	public static Vector2f getCenterPosition() {
		return Window.CENTER_POSITION;
	}

}
