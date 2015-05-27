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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.PointLight;
import com.base.engine.components.SpotLight;
import com.base.engine.core.Transform;
import com.base.engine.core.Util;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.resourceManagement.ShaderResource;

public class Shader {
	private static WeakHashMap<String, ShaderResource> loadedShaders = new WeakHashMap<String, ShaderResource>();

	private ShaderResource resource;
	private final String fileName;

	public Shader(final String fileName) {
		this.fileName = fileName;

		final ShaderResource oldResource = Shader.loadedShaders.get(fileName);

		if (oldResource != null) {
			resource = oldResource;
			resource.addReference();
		} else {
			resource = new ShaderResource();

			final String vertexShaderText = Shader.loadShader(fileName + ".vs");
			final String fragmentShaderText = Shader.loadShader(fileName + ".fs");

			addVertexShader(vertexShaderText);
			addFragmentShader(fragmentShaderText);

			addAllAttributes(vertexShaderText);

			compileShader();

			addAllUniforms(vertexShaderText);
			addAllUniforms(fragmentShaderText);

			Shader.loadedShaders.put(fileName, resource);
		}
	}

	@Override
	protected void finalize() {
		if (resource.removeReference() && !fileName.isEmpty()) {
			Shader.loadedShaders.remove(fileName);
		}
	}

	public void bind() {
		GL20.glUseProgram(resource.getProgram());
	}

	public void updateUniforms(final Transform transform, final Material material, final RenderingEngine renderingEngine) {
		final Matrix4f worldMatrix = transform.getTransformation();
		final Matrix4f MVPMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);

		for (int i = 0; i < resource.getUniformNames().size(); i++) {
			final String uniformName = resource.getUniformNames().get(i);
			final String uniformType = resource.getUniformTypes().get(i);

			if (uniformType.equals("sampler2D")) {
				final int samplerSlot = renderingEngine.getSamplerSlot(uniformName);
				material.getTexture(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			} else if (uniformName.startsWith("T_")) {
				if (uniformName.equals("T_MVP")) {
					setUniform(uniformName, MVPMatrix);
				} else if (uniformName.equals("T_model")) {
					setUniform(uniformName, worldMatrix);
				} else {
					throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
				}
			} else if (uniformName.startsWith("R_")) {
				final String unprefixedUniformName = uniformName.substring(2);
				if (uniformType.equals("vec3")) {
					setUniform(uniformName, renderingEngine.getVector3f(unprefixedUniformName));
				} else if (uniformType.equals("float")) {
					setUniformf(uniformName, renderingEngine.getFloat(unprefixedUniformName));
				} else if (uniformType.equals("DirectionalLight")) {
					setUniformDirectionalLight(uniformName, (DirectionalLight) renderingEngine.getActiveLight());
				} else if (uniformType.equals("PointLight")) {
					setUniformPointLight(uniformName, (PointLight) renderingEngine.getActiveLight());
				} else if (uniformType.equals("SpotLight")) {
					setUniformSpotLight(uniformName, (SpotLight) renderingEngine.getActiveLight());
				} else {
					renderingEngine.updateUniformStruct(transform, material, this, uniformName, uniformType);
				}
			} else if (uniformName.startsWith("C_")) {
				if (uniformName.equals("C_eyePos")) {
					setUniform(uniformName, renderingEngine.getMainCamera().getTransform().getTransformedPos());
				} else {
					throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
				}
			} else {
				if (uniformType.equals("vec3")) {
					setUniform(uniformName, material.getVector3f(uniformName));
				} else if (uniformType.equals("float")) {
					setUniformf(uniformName, material.getFloat(uniformName));
				} else {
					throw new IllegalArgumentException(uniformType + " is not a supported type in Material");
				}
			}
		}
	}

	private void addAllAttributes(final String shaderText) {
		final String ATTRIBUTE_KEYWORD = "attribute";
		int attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);
		int attribNumber = 0;
		while (attributeStartLocation != -1) {
			if (!(attributeStartLocation != 0 && (Character.isWhitespace(shaderText.charAt(attributeStartLocation - 1)) || shaderText.charAt(attributeStartLocation - 1) == ';') && Character.isWhitespace(shaderText.charAt(attributeStartLocation + ATTRIBUTE_KEYWORD.length())))) {
				attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
				continue;

			}

			final int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
			final int end = shaderText.indexOf(";", begin);

			final String attributeLine = shaderText.substring(begin, end).trim();
			final String attributeName = attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length()).trim();

			setAttribLocation(attributeName, attribNumber);
			attribNumber++;

			attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
		}
	}

	private class GLSLStruct {
		public String name;
		public String type;
	}

	private HashMap<String, ArrayList<GLSLStruct>> findUniformStructs(final String shaderText) {
		final HashMap<String, ArrayList<GLSLStruct>> result = new HashMap<String, ArrayList<GLSLStruct>>();

		final String STRUCT_KEYWORD = "struct";
		int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
		while (structStartLocation != -1) {
			if (!(structStartLocation != 0 && (Character.isWhitespace(shaderText.charAt(structStartLocation - 1)) || shaderText.charAt(structStartLocation - 1) == ';') && Character.isWhitespace(shaderText.charAt(structStartLocation + STRUCT_KEYWORD.length())))) {
				structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
				continue;
			}

			final int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
			final int braceBegin = shaderText.indexOf("{", nameBegin);
			final int braceEnd = shaderText.indexOf("}", braceBegin);

			final String structName = shaderText.substring(nameBegin, braceBegin).trim();
			final ArrayList<GLSLStruct> glslStructs = new ArrayList<GLSLStruct>();

			int componentSemicolonPos = shaderText.indexOf(";", braceBegin);
			while (componentSemicolonPos != -1 && componentSemicolonPos < braceEnd) {
				int componentNameEnd = componentSemicolonPos + 1;

				while (Character.isWhitespace(shaderText.charAt(componentNameEnd - 1)) || shaderText.charAt(componentNameEnd - 1) == ';') {
					componentNameEnd--;
				}

				int componentNameStart = componentSemicolonPos;

				while (!Character.isWhitespace(shaderText.charAt(componentNameStart - 1))) {
					componentNameStart--;
				}

				int componentTypeEnd = componentNameStart;

				while (Character.isWhitespace(shaderText.charAt(componentTypeEnd - 1))) {
					componentTypeEnd--;
				}

				int componentTypeStart = componentTypeEnd;

				while (!Character.isWhitespace(shaderText.charAt(componentTypeStart - 1))) {
					componentTypeStart--;
				}

				final String componentName = shaderText.substring(componentNameStart, componentNameEnd);
				final String componentType = shaderText.substring(componentTypeStart, componentTypeEnd);

				final GLSLStruct glslStruct = new GLSLStruct();
				glslStruct.name = componentName;
				glslStruct.type = componentType;

				glslStructs.add(glslStruct);

				componentSemicolonPos = shaderText.indexOf(";", componentSemicolonPos + 1);
			}

			result.put(structName, glslStructs);

			structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
		}

		return result;
	}

	private void addAllUniforms(final String shaderText) {
		final HashMap<String, ArrayList<GLSLStruct>> structs = findUniformStructs(shaderText);

		final String UNIFORM_KEYWORD = "uniform";
		int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);
		while (uniformStartLocation != -1) {
			if (!(uniformStartLocation != 0 && (Character.isWhitespace(shaderText.charAt(uniformStartLocation - 1)) || shaderText.charAt(uniformStartLocation - 1) == ';') && Character.isWhitespace(shaderText.charAt(uniformStartLocation + UNIFORM_KEYWORD.length())))) {
				uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
				continue;
			}

			final int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
			final int end = shaderText.indexOf(";", begin);

			final String uniformLine = shaderText.substring(begin, end).trim();

			final int whiteSpacePos = uniformLine.indexOf(' ');
			final String uniformName = uniformLine.substring(whiteSpacePos + 1, uniformLine.length()).trim();
			final String uniformType = uniformLine.substring(0, whiteSpacePos).trim();

			resource.getUniformNames().add(uniformName);
			resource.getUniformTypes().add(uniformType);
			addUniform(uniformName, uniformType, structs);

			uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
		}
	}

	private void addUniform(final String uniformName, final String uniformType, final HashMap<String, ArrayList<GLSLStruct>> structs) {
		boolean addThis = true;
		final ArrayList<GLSLStruct> structComponents = structs.get(uniformType);

		if (structComponents != null) {
			addThis = false;
			for (final GLSLStruct struct : structComponents) {
				addUniform(uniformName + "." + struct.name, struct.type, structs);
			}
		}

		if (!addThis) {
			return;
		}

		final int uniformLocation = GL20.glGetUniformLocation(resource.getProgram(), uniformName);

		if (uniformLocation == 0xFFFFFFFF) {
			System.err.println("Error: Could not find uniform: " + uniformName);
			new Exception().printStackTrace();
			System.exit(1);
		}

		resource.getUniforms().put(uniformName, uniformLocation);
	}

	private void addVertexShader(final String text) {
		addProgram(text, GL20.GL_VERTEX_SHADER);
	}

	private void addGeometryShader(final String text) {
		addProgram(text, GL32.GL_GEOMETRY_SHADER);
	}

	private void addFragmentShader(final String text) {
		addProgram(text, GL20.GL_FRAGMENT_SHADER);
	}

	private void setAttribLocation(final String attributeName, final int location) {
		GL20.glBindAttribLocation(resource.getProgram(), location, attributeName);
	}

	private void compileShader() {
		GL20.glLinkProgram(resource.getProgram());

		if (GL20.glGetProgrami(resource.getProgram(), GL20.GL_LINK_STATUS) == 0) {
			System.err.println(GL20.glGetProgramInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}

		GL20.glValidateProgram(resource.getProgram());

		if (GL20.glGetProgrami(resource.getProgram(), GL20.GL_VALIDATE_STATUS) == 0) {
			System.err.println(GL20.glGetProgramInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}
	}

	private void addProgram(final String text, final int type) {
		final int shader = GL20.glCreateShader(type);

		if (shader == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}

		GL20.glShaderSource(shader, text);
		GL20.glCompileShader(shader);

		if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
			System.err.println(GL20.glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}

		GL20.glAttachShader(resource.getProgram(), shader);
	}

	private static String loadShader(final String fileName) {
		final StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		final String INCLUDE_DIRECTIVE = "#include";

		try {
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			String line;

			while ((line = shaderReader.readLine()) != null) {
				if (line.startsWith(INCLUDE_DIRECTIVE)) {
					shaderSource.append(Shader.loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
				} else {
					shaderSource.append(line).append("\n");
				}
			}

			shaderReader.close();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return shaderSource.toString();
	}

	public void setUniformi(final String uniformName, final int value) {
		GL20.glUniform1i(resource.getUniforms().get(uniformName), value);
	}

	public void setUniformf(final String uniformName, final float value) {
		GL20.glUniform1f(resource.getUniforms().get(uniformName), value);
	}

	public void setUniform(final String uniformName, final Vector3f value) {
		GL20.glUniform3f(resource.getUniforms().get(uniformName), value.getX(), value.getY(), value.getZ());
	}

	public void setUniform(final String uniformName, final Matrix4f value) {
		GL20.glUniformMatrix4(resource.getUniforms().get(uniformName), true, Util.createFlippedBuffer(value));
	}

	public void setUniformBaseLight(final String uniformName, final BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniformDirectionalLight(final String uniformName, final DirectionalLight directionalLight) {
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}

	public void setUniformPointLight(final String uniformName, final PointLight pointLight) {
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent());
		setUniform(uniformName + ".position", pointLight.getTransform().getTransformedPos());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}

	public void setUniformSpotLight(final String uniformName, final SpotLight spotLight) {
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
}
