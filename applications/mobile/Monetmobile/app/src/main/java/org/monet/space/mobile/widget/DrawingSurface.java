package org.monet.space.mobile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.monet.space.mobile.R;


public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    
    private static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_PAINT_COlOR = 0xFF000000;
    
    
    class DrawThread extends Thread {
      private SurfaceHolder surfaceHolder;

      public DrawThread(SurfaceHolder surfaceHolder){
          this.surfaceHolder = surfaceHolder;
      }

      @Override
      public void run() {
          
          while (getThreadRunning()) {
            
            if(getMustDraw()) {
              
              Canvas holderCanvas = this.surfaceHolder.lockCanvas(null);
              try {
                final Canvas canvas = new Canvas(bitmap);
                
                if (backgroundDrawable != null) {
                  backgroundDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                  backgroundDrawable.draw(canvas);
                } else {
                  canvas.drawColor(backgroundColor);
                }
                
                canvas.drawPath(path, linePaint);

                holderCanvas.drawBitmap (bitmap, 0,  0, null);
              } finally {
                this.surfaceHolder.unlockCanvasAndPost(holderCanvas);
                setMustDraw(false);
              }
            }
          }
      }
    }
    
    private boolean threadRunning = false;
    private boolean mustDraw = false;
    protected DrawThread thread;
    
    private Bitmap bitmap;
    private Path path;
    
    private Paint linePaint;
    private int paintColor;
    private int backgroundColor;
    private Drawable backgroundDrawable = null;
    

    public DrawingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.configureColorAttributes(context, attrs);

        this.path = new Path();
        this.linePaint = this.createLinePaint();
        
        this.bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    private Paint createLinePaint(){
      Paint paint = new Paint();
      paint.setDither(true);
      paint.setColor(this.paintColor);
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeJoin(Paint.Join.ROUND);
      paint.setStrokeCap(Paint.Cap.ROUND);
      paint.setStrokeWidth(3);
      
      return paint;
    }
    
    private void configureColorAttributes(Context context, AttributeSet attrs) {
      TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DrawingSurface);
      
      this.backgroundColor = attributes.getInt(R.styleable.DrawingSurface_background_color, DEFAULT_BACKGROUND_COLOR);
      this.paintColor = attributes.getInt(R.styleable.DrawingSurface_paint_color, DEFAULT_PAINT_COlOR);
      
      attributes.recycle();
    }
    
    private synchronized boolean getMustDraw() {
      return this.mustDraw;
    }
    
    private synchronized void setMustDraw(boolean value) {
      this.mustDraw = value;
    }
    
    private synchronized boolean getThreadRunning() {
      return this.threadRunning;
    }
    
    private synchronized void setThreadRunning(boolean value) {
      this.threadRunning = value;
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,  int height) {
        this.bitmap = Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888);;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      
      this.thread = new DrawThread(getHolder());

      this.setThreadRunning(true);
      this.setMustDraw(true);
      this.thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
      this.setThreadRunning(false);
      
      while (true) {
          try {
              this.thread.join();
              return;
          } catch (InterruptedException e) {
              // we will try it again and again...
          }
      }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
      if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        this.path.moveTo(motionEvent.getX(), motionEvent.getY());
        this.path.lineTo(motionEvent.getX(), motionEvent.getY());

      } else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
        this.path.lineTo(motionEvent.getX(), motionEvent.getY());

      } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
        this.path.lineTo(motionEvent.getX(), motionEvent.getY());
      }    
      
      this.setMustDraw(true);

      return true;
    }
    
    public void clear() {
      this.backgroundDrawable = null;
      this.path.reset();
      this.setMustDraw(true);
    }
    
    public void setImageDrawable(Drawable drawable) {
      this.backgroundDrawable = drawable;
      this.setMustDraw(true);
    }

    public Bitmap getImageBitmap() {
      return this.bitmap;
    }
    
}
