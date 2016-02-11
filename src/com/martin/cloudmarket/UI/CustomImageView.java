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
		//��drawableת����Bitmap����,������ʾͼƬ
		Bitmap bm = ((BitmapDrawable)drawable).getBitmap();
		Bitmap bitmap = scaleImage(bm,width,height);
		if(bitmap!=null){
			//����һ���յ�λͼ,�������ָλͼ�ϵ�����Ϊ��,����ָ������
			Bitmap canvasBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			//����һ���յĻ���(Ҳ����˵������ʲô��û��)
			Canvas myCanvas = new Canvas(canvasBitmap);
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setColor(Color.GREEN);
			//�ڿյĻ����ϻ�һ��Բ
			myCanvas.drawCircle(bitmap.getWidth()/2+0.1f, bitmap.getHeight()/2+0.1f, bitmap.getHeight()/2+0.1f, paint);
			//ͼ2��ͼƬ�Ľ���
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			//�ڻ����ϻ�һ��ͼƬ
			myCanvas.drawBitmap(bitmap, 0, 0, paint);
			//������canvasBitmap�ϻ��Ƶ����ݻ��Ƶ��Զ���Ŀؼ���ͨ��ϵͳ��canvas
			canvas.drawBitmap(canvasBitmap, width/3, height/4,null);
		}
	}

	
	/**
	* ����ͼƬ
	   * @param bm Ҫ����ͼƬ
	   * @param newWidth ���
	   * @param newHeight �߶�
	   * @return������ͼƬ
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
