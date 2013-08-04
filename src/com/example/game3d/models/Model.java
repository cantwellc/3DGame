package com.example.game3d.models;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;

public class Model {
	private final float[] DEFAULT_COLOR = {0.8f,0.1f,0.1f,1.0f};

	private float[] vertices;
	private short[] indices;
	private short[] iNormals;
	
	private static enum objCodes{
		v, vn, f
	}

	/**	Create a new model from a supplied .obj file	*/
	public static Model loadObj(String fileName){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Short> indices = new ArrayList<Short>();
		ArrayList<Short> iNormals = new ArrayList<Short>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try {
			// Read in each line in the obj file one at a time
			while((line = reader.readLine()) != null){
				String[] words = line.split("\\s");
				// If the first "word" of the line is a recognized code add the appropriate data
				try{
					switch (objCodes.valueOf(words[0])) {
					case v:	// New vertex coordinates
						float[] position = toFloatArray(Arrays.copyOfRange(words, 1, words.length));
						for(int i=0; i<position.length; ++i) vertices.add(position[i]);
						break;
					case vn:
						float[] normal = toFloatArray(Arrays.copyOfRange(words, 1, words.length));
						for(int i=0; i<normal.length; ++i) normals.add(normal[i]);
						break;
					case f:	// New set of vertex indices describing a face. And correspinding vertex normals.
						processF(Arrays.copyOfRange(words, 1, words.length));
						short[] face = toShortArray(Arrays.copyOfRange(words, 1, words.length));
						//	Faces are described by triangles and squares.  If it is a square, decompose into 2 triangles
						if(face.length == 4) face = makeTrianglesFromSquare(face);
						//	Blender obj files begin indexing at 1.  Must start at 0 for rendering.  Subtract 1 from each index.
						for(int i=0; i<face.length; ++i) indices.add((short) (face[i]-1));
						break;
					default:
						break;
					}
				}
				catch (Exception e) {	// The first word of the line was not a recognized code.  Ignore it.
				}
			}
		} catch (IOException e) {	// Failed to read new line from file.
			e.printStackTrace();
		}
		Model m = new Model();
		// Convert lists to static sized arrays and add to the new model
		m.setIndices(toShortArray(indices));	
		m.setVertices(toFloatArray(vertices));
		return m;
	}

	private static void processF(String[] words) {
		String line = "F Line: ";
		for(int i=0; i<words.length; ++i){
			String[] data = words[i].split("//");
			line = line + data[0] + "," + data[1] + " ";
		}
		Log.d("3D_GAME_DATA", line);
	}

	private static short[] toShortArray(ArrayList<Short> list) {
		short[] array = new short[list.size()];
		for(int i=0; i<array.length; ++i) array[i] = list.get(i);
		return array;
	}

	private static short[] toShortArray(String[] strings) {
		short[] array = new short[strings.length];
		for(int i=0; i<strings.length; ++i){
			array[i] = Short.valueOf(strings[i]);
		}
		return array;
	}

	private static float[] toFloatArray(ArrayList<Float> list) {
		float[] array = new float[list.size()];
		for(int i=0; i<array.length; ++i) array[i] = list.get(i);
		return array;
	}

	private static float[] toFloatArray(String[] strings) {
		float[] floats = new float[strings.length];
		for(int i=0; i<strings.length; ++i){
			floats[i] = Float.valueOf(strings[i]);
		}
		return floats;
	}

	private static short[] makeTrianglesFromSquare(short[] square) {
		short[] triangles = new short[6];
		triangles[0] = square[0];
		triangles[1] = square[1];
		triangles[2] = square[2];
		triangles[3] = square[0];
		triangles[4] = square[2];
		triangles[5] = square[3];
		return triangles;
	}

	public float[] getVertices() {
		return vertices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public short[] getIndices() {
		return indices;
	}

	public void setIndices(short[] indices) {
		this.indices = indices;
	}
	
	/**	Generate a new drawable shape from this instance of Model	*/
	public DrawableShape getNewDrawableShape(){
		DrawableShape shape = new DrawableShape();
		
		// Create the buffer to hold the vertex position data
		ByteBuffer fbb = ByteBuffer.allocateDirect(vertices.length*Float.SIZE/8);
		fbb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = fbb.asFloatBuffer();
		fb.put(vertices);
		fb.position(0);
		shape.setVertexBuffer(fb);
		
		ByteBuffer sbb = ByteBuffer.allocateDirect(indices.length*Short.SIZE/8);
		sbb.order(ByteOrder.nativeOrder());
		ShortBuffer sb = sbb.asShortBuffer();
		sb.put(indices);
		sb.position(0);
		shape.setIndexBuffer(sb);
		
		shape.setColor(DEFAULT_COLOR);

		return shape;
	}
	
}
