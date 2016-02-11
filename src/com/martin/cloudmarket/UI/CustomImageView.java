package com.martin.cloudmarket.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageView extends ImageView {
	
	final int width = 250;
	final int height = 250;
	
	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomImageView(Context context) {
		super(context);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if(drawable==null){
			return;
		}
		//把drawable转换成Bitmap对象,用于显示图片
		Bitmap bm = ((BitmapDrawable)drawable).getBitmap();
		Bitmap bitmap = scaleImage(bm,width,height);
		if(bitmap!=null){
			//创建一个空的位图,这里空是指位图上的内容为空,不是指对象本身
			Bitmap canvasBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			//创建一个空的画布(也就是说画布上什么都没有)
			Canvas myCanvas = new Canvas(canvasBitmap);
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setColor(Color.GREEN);
			//在空的画布上画一个圆
			myCanvas.drawCircle(bitmap.getWidth()/2+0.1f, bitmap.getHeight()/2+0.1f, bitmap.getHeight()/2+0.1f, paint);
			//图2个图片的交集
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			//在画布上画一个图片
			myCanvas.drawBitmap(bitmap, 0, 0, paint);
			//最后把在canvasBitmap上绘制的内容绘制到自定义的控件上通过系统的canvas
			canvas.drawBitmap(canvasBitmap, width/3, height/4,null);
		}
	}

	
	/**
	* 缩放图片
	   * @param bm 要缩放图片
	   * @param newWidth 宽度
	   * @param newHeight 高度
	   * @return处理后的图片
	   */
	  public Bitmap  scaleImage(Bitmap bm, int newWidth, int newHeight){
	    if (bm == null){
	      return null;
	    }
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
	    if (bm != null & !bm.isRecycled()){
	      bm = null;
	    }
	    return newbm;
	  }
}
