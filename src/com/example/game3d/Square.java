package com.example.game3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

class Square {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    
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


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = { -0.5f,  0.5f, 0.0f,   // top left
                                    -0.5f, -0.5f, 0.0f,   // bottom left
                                     0.5f, -0.5f, 0.0f,   // bottom right
                                     0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
	private int program;
	private int positionHandle;
	private int vertexStride = 0;
	private int colorHandle;
	private float[] color = {0.1f,0.1f,0.6f,1.0f};
	private int mvpMatrixHandle;

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        
        int vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }
    
    public void draw(float[] mvpMatrix){
    	GLES20.glUseProgram(program);
    	
    	positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
    	GLES20.glEnableVertexAttribArray(positionHandle);
    	GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride , vertexBuffer);
    	
    	colorHandle = GLES20.glGetAttribLocation(program, "vColor");
    	GLES20.glUniform4fv(colorHandle, 1, color , 0);
    	
    	mvpMatrixHandle = GLES20.glGetUniformLocation(program, "mvpMatrix");
    }
}
