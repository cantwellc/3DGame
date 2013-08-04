package com.example.game3d;


import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.game3d.models.DrawableShape;
import com.example.game3d.models.Model;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.Environment;
import android.util.Log;

public class MyRenderer implements Renderer {
	
	public static String MODEL_PATH = "/storage/extSdCard/3DGame/";
	
	private Model sphereModel;
	private Model cubeModel;
	private Triangle triangle;
	private DrawableShape sphere;
	private DrawableShape cube;
	private float[] projMatrix;
	private float[] viewMatrix;
	private float[] mvpMatrix;					// 	model view projection matrix
	private float[] eye = {0f,0f,-5f};
	private float[] center = {0f,0f,0f};
	private float[] up = {0f,1f,0f};
	private float[] lightPosition = {10f,10f,-2f};

	
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		//	Set camera view matrix
		Matrix.setLookAtM(viewMatrix, 0, eye[0], eye[1], eye[2], center[0], center[1], center[2], up[0], up[1], up[2]);
		
		//	Create the transformation matrix
		Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, viewMatrix, 0);
		
		//	Draw the triangle with the given transform
//		triangle.draw(mvpMatrix);
//		sphere.draw(mvpMatrix);
//		cube.draw(mvpMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
		// 	Set projection matrix
		float ratio = (float) width/height;
		Matrix.frustumM(projMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		projMatrix = new float[16];
		viewMatrix = new float[16];
		mvpMatrix = new float[16];
		
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		triangle = new Triangle();

//		sphereModel = Model.loadObj(MODEL_PATH + "simpleSphere.obj");
//		sphere = sphereModel.getNewDrawableShape();
		
		cubeModel = Model.loadObj(MODEL_PATH + "CubeN.obj");
		cube = cubeModel.getNewDrawableShape();
		
	}

	
	public static int loadShader(int type, String shaderCode){

	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
	


}
