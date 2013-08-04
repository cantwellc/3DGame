package com.example.game3d.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import com.example.game3d.MyRenderer;

import android.opengl.GLES20;
import android.util.Log;

public class DrawableShape implements Drawable {
	private final String vertexShaderCode =
			"uniform mat4 mvpMatrix;" +
		    "attribute vec4 vPosition;" +
		    "void main() {" +
		    "  gl_Position = mvpMatrix*vPosition;" +
		    "}";
	private final String fragmentShaderCode =
		    "precision mediump float;" +
		    "uniform vec4 vColor;" +
		    "void main() {" +
		    "  gl_FragColor = vColor;" +
		    "}";


	private int program;

	private int positionHandle;
	private int colorHandle;
	private int mvpMatrixHandle;

	private float[] position;
	private float[] color;
	private ShortBuffer indexBuffer = null;
	private FloatBuffer vertexBuffer = null;
	private final int COORDS_PER_VERTEX = 3;
	
	/**	Default constructor initializes opengl information */
	public DrawableShape(){
	   	int vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    	int fragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
    	
    	program = GLES20.glCreateProgram();
    	GLES20.glAttachShader(program, vertexShader);
    	GLES20.glAttachShader(program, fragmentShader);
    	GLES20.glLinkProgram(program);
	}
	

	public float[] getPosition() {
		return position;
	}
	public void setPosition(float[] position) {
		this.position = position;
	}
	public float[] getColor() {
		return color;
	}
	public void setColor(float[] color) {
		this.color = color;
	}
	public ShortBuffer getIndexBuffer() {
		return indexBuffer;
	}
	public void setIndexBuffer(ShortBuffer indexBuffer) {
		this.indexBuffer = indexBuffer;
	}
	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}
	public void setVertexBuffer(FloatBuffer vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
	}
	public int getProgram() {
		return program;
	}
	public void setProgram(int program) {
		this.program = program;
	}
	public int getPositionHandle() {
		return positionHandle;
	}
	public void setPositionHandle(int positionHandle) {
		this.positionHandle = positionHandle;
	}
	public int getColorHandle() {
		return colorHandle;
	}
	public void setColorHandle(int colorHandle) {
		this.colorHandle = colorHandle;
	}
	public int getMvpMatrixHandle() {
		return mvpMatrixHandle;
	}
	public void setMvpMatrixHandle(int mvpMatrixHandle) {
		this.mvpMatrixHandle = mvpMatrixHandle;
	}
	
	@Override
	public void draw(float[] mvpMatrix) {
    	int vertexStride = 0;
    	int vertexCount = 12;
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(program);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
         // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        
        
      
        // get handle to vertex shader's mvpMatrix member
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "mvpMatrix");
        // apply the transformation
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,  mvpMatrix, 0);

        // Draw the triangle
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexBuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, indexBuffer);
//        Log.d("3D_GAME_DATA", "Index Buffer Position: " + indexBuffer.position());

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
	}
	

}
