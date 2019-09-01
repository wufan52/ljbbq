package com.ljbbq.jin;
import android.app.*;
import com.ljbbq.jin.base.*;
import android.view.*;
import android.os.*;
import android.support.v7.widget.*;
import android.content.*;

public class ztgh extends BaseActivity
{

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ztqh);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		//toolbar添加返回按钮
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//返回按钮点击事件
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					Intent a = new Intent();
					a.setClass(ztgh.this, MainActivity.class);
					startActivity(a);
                    finish();
				}
			});
	}
	@Override
    public void onBackPressed()
	{
        //TODO something
        super.onBackPressed();
        Intent a = new Intent();
        a.setClass(ztgh.this, MainActivity.class);
        startActivity(a);
        finish();
    }
}
