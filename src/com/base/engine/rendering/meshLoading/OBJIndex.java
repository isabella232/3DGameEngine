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

package com.base.engine.rendering.meshLoading;

public class OBJIndex {
	private int vertexIndex;
	private int texCoordIndex;
	private int normalIndex;

	public int getVertexIndex() {
		return vertexIndex;
	}

	public int getTexCoordIndex() {
		return texCoordIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public void setVertexIndex(final int val) {
		vertexIndex = val;
	}

	public void setTexCoordIndex(final int val) {
		texCoordIndex = val;
	}

	public void setNormalIndex(final int val) {
		normalIndex = val;
	}

	@Override
	public boolean equals(final Object obj) {
		
		if (!(obj instanceof OBJIndex)) {
			return super.equals(obj);
		}
		
		final OBJIndex index = (OBJIndex) obj;

		return vertexIndex == index.vertexIndex && texCoordIndex == index.texCoordIndex && normalIndex == index.normalIndex;
	}

	@Override
	public int hashCode() {
		final int BASE = 17;
		final int MULTIPLIER = 31;

		int result = BASE;

		result = MULTIPLIER * result + vertexIndex;
		result = MULTIPLIER * result + texCoordIndex;
		result = MULTIPLIER * result + normalIndex;

		return result;
	}
}
