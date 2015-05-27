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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.base.engine.core.Util;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;

public class OBJModel {
	private final ArrayList<Vector3f> positions;
	private final ArrayList<Vector2f> texCoords;
	private final ArrayList<Vector3f> normals;
	private final ArrayList<OBJIndex> indices;
	private boolean m_hasTexCoords;
	private boolean m_hasNormals;

	public OBJModel(final String fileName) {
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<OBJIndex>();
		m_hasTexCoords = false;
		m_hasNormals = false;

		BufferedReader meshReader = null;

		try {
			meshReader = new BufferedReader(new FileReader(fileName));
			String line;

			while ((line = meshReader.readLine()) != null) {
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);

				if (tokens.length == 0 || tokens[0].equals("#")) {
					continue;
				} else if (tokens[0].equals("v")) {
					positions.add(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
				} else if (tokens[0].equals("vt")) {
					texCoords.add(new Vector2f(Float.valueOf(tokens[1]), 1.0f - Float.valueOf(tokens[2])));
				} else if (tokens[0].equals("vn")) {
					normals.add(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
				} else if (tokens[0].equals("f")) {
					for (int i = 0; i < tokens.length - 3; i++) {
						indices.add(parseOBJIndex(tokens[1]));
						indices.add(parseOBJIndex(tokens[2 + i]));
						indices.add(parseOBJIndex(tokens[3 + i]));
					}
				}
			}

			meshReader.close();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public IndexedModel toIndexedModel() {
		final IndexedModel result = new IndexedModel();
		final IndexedModel normalModel = new IndexedModel();
		final HashMap<OBJIndex, Integer> resultIndexMap = new HashMap<OBJIndex, Integer>();
		final HashMap<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
		final HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

		for (int i = 0; i < indices.size(); i++) {
			final OBJIndex currentIndex = indices.get(i);

			final Vector3f currentPosition = positions.get(currentIndex.getVertexIndex());
			Vector2f currentTexCoord;
			Vector3f currentNormal;

			if (m_hasTexCoords) {
				currentTexCoord = texCoords.get(currentIndex.getTexCoordIndex());
			} else {
				currentTexCoord = new Vector2f(0, 0);
			}

			if (m_hasNormals) {
				currentNormal = normals.get(currentIndex.getNormalIndex());
			} else {
				currentNormal = new Vector3f(0, 0, 0);
			}

			Integer modelVertexIndex = resultIndexMap.get(currentIndex);

			if (modelVertexIndex == null) {
				modelVertexIndex = result.getPositions().size();
				resultIndexMap.put(currentIndex, modelVertexIndex);

				result.getPositions().add(currentPosition);
				result.getTexCoords().add(currentTexCoord);
				if (m_hasNormals) {
					result.getNormals().add(currentNormal);
				}
			}

			Integer normalModelIndex = normalIndexMap.get(currentIndex.getVertexIndex());

			if (normalModelIndex == null) {
				normalModelIndex = normalModel.getPositions().size();
				normalIndexMap.put(currentIndex.getVertexIndex(), normalModelIndex);

				normalModel.getPositions().add(currentPosition);
				normalModel.getTexCoords().add(currentTexCoord);
				normalModel.getNormals().add(currentNormal);
				normalModel.getTangents().add(new Vector3f(0, 0, 0));
			}

			result.getIndices().add(modelVertexIndex);
			normalModel.getIndices().add(normalModelIndex);
			indexMap.put(modelVertexIndex, normalModelIndex);
		}

		if (!m_hasNormals) {
			normalModel.calcNormals();

			for (int i = 0; i < result.getPositions().size(); i++) {
				result.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
			}
		}

		normalModel.calcTangents();

		for (int i = 0; i < result.getPositions().size(); i++) {
			result.getTangents().add(normalModel.getTangents().get(indexMap.get(i)));
		}

		// for(int i = 0; i < result.GetTexCoords().size(); i++)
		// result.GetTexCoords().Get(i).SetY(1.0f -
		// result.GetTexCoords().Get(i).GetY());

		return result;
	}

	private OBJIndex parseOBJIndex(final String token) {
		final String[] values = token.split("/");

		final OBJIndex result = new OBJIndex();
		result.setVertexIndex(Integer.parseInt(values[0]) - 1);

		if (values.length > 1) {
			if (!values[1].isEmpty()) {
				m_hasTexCoords = true;
				result.setTexCoordIndex(Integer.parseInt(values[1]) - 1);
			}

			if (values.length > 2) {
				m_hasNormals = true;
				result.setNormalIndex(Integer.parseInt(values[2]) - 1);
			}
		}

		return result;
	}
}
