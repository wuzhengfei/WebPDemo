package com.example.webpdemo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

	static {
		try {
			System.loadLibrary("webp");
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bitmap bitmap = webpToBitmap();
		ImageView imageView = (ImageView)findViewById(R.id.image_webp);
		imageView.setImageBitmap(bitmap);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private Bitmap webpToBitmap() {
		Bitmap bitmap = null ;
		InputStream is = null ;
		try {
			System.out.println("**************** get input stream");
			is = getResources().openRawResource(R.raw.image_webp);
			int length = is.available();
			System.out.println("**************** length="+length);
			byte[] encoded = new byte[length];
			int rs = is.read(encoded, 0 , length) ;
			System.out.println("**************** read result = "+ rs);
			int[] width = new int[] { 0 };
			int[] height = new int[] { 0 };
			byte[] decoded = com.google.webp.libwebp.WebPDecodeARGB(encoded, length, width, height);
			int[] pixels = new int[width[0] * height[0] ];
			ByteBuffer.wrap(decoded).asIntBuffer().get(pixels);
			
			System.out.println("*********** width="+width[0] + "   height="+height[0]);
			bitmap = Bitmap.createBitmap(pixels, width[0], height[0], Bitmap.Config.ARGB_8888);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if( is != null ){
				try {
					is.close() ;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;

	}
}
