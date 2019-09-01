package com.ljbbq.jin;

import android.os.Bundle;
import android.widget.Toast;
import com.ljbbq.jin.base.*;
import android.view.*;
import android.support.v4.widget.*;
import android.os.*;
import android.support.v7.widget.Toolbar;
import android.widget.*;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import java.util.*;
import android.content.*;
import com.ljbbq.jin.bmob.*;
import com.bumptech.glide.*;
import com.ljbbq.jin.tpurl.*;
import es.dmoral.toasty.*;
import android.support.v7.widget.*;
import android.view.View.*;
import android.support.v7.app.*;
import android.media.*;
import java.io.*;
import com.bumptech.glide.request.*;
import android.graphics.*;
import android.view.animation.*;
import com.bumptech.glide.request.target.*;
import com.wega.library.loadingDialog.*;

public class song extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
{
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
	}

	private SwipeRefreshLayout songrefresh;
    private Handler handler = new Handler();
    private ListView songlist;
	final MediaPlayer mp = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song);
		Toolbar toolbar = (Toolbar) findViewById(R.id.songtoolbar);
        toolbar.setTitle("我的乐库");
        setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //返回按钮点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
				{
                    finish();
                }
			});
		bmob();
		bmobsong();
		songlist = (ListView) findViewById(R.id.songListView);
		songrefresh = (SwipeRefreshLayout) findViewById(R.id.songrefresh);
		songrefresh.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_blue_bright);
		songrefresh.setOnRefreshListener(this);
		onRefresh();
		android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.songfa);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View p1)
				{
                    // TODO: Implement this method
                    Intent intent = new Intent();
					intent.setClass(song.this, tjsong.class);
					startActivity(intent);
                }
            });
	}

	private void bmob()
	{
		// TODO: Implement this method
		User user = new User();
		user = BmobUser.getCurrentUser(User.class);
		if (user == null)
		{
			Toasty.error(song.this, "请先登录").show();
			Intent intent = new Intent();
			intent.setClass(song.this, login.class);
			startActivity(intent);
			finish();
		}
	}

	private void bmobsong()
	{
		// TODO: Implement this method
		BmobQuery<bmobsong> query = new BmobQuery<>();
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(User.class));
        query.order("-updatedAt");
		query.setLimit(500);
        //包含作者信息
        query.include("author");
        query.findObjects(new FindListener<bmobsong>() {
				@Override
                public void done(List<bmobsong> p1, BmobException p2)
				{
                    if (p2 == null)
					{
                        List<bmobsong>datas=new ArrayList<>();
                        for (bmobsong get:p1)
						{
                            datas.add(get);
                            MyAdapter adapter=new MyAdapter(datas, song.this);
                            songlist.setAdapter(adapter);
                        }
						if(datas.size()==0){
							Toasty.warning(song.this,"你的乐库是空的，点击下面加号添加歌曲吧！").show();
						}
                    }
					else
					{
						Toasty.error(song.this,"网络超时请检查网络！"+p2.toString()).show();
					}
                    // TODO: Implement this method
                }
			});
	}
	class MyAdapter extends BaseAdapter
	{
        private List<bmobsong>list=null;
        private Context context;
        private LayoutInflater mInflater=null;
        public MyAdapter(List<bmobsong> list, Context context)
		{
            this.list = list;
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount()
		{
            return list.size();
        }

        @Override
        public Object getItem(int position)
		{
            return list.get(position);
        }

        @Override
        public long getItemId(int position)
		{
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
		{
            final ViewHolder holder;
            if (convertView == null)
			{
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.songitem, null);
                holder.gqtp = (ImageView) convertView.findViewById(R.id.stp);
                holder.gqname = (TextView) convertView.findViewById(R.id.sname);
                holder.gqzz = (TextView) convertView.findViewById(R.id.szz);
				holder.cc = (CardView) convertView.findViewById(R.id.cardview);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
			else
			{
                holder = (ViewHolder) convertView.getTag();
            }

			final bmobsong pes=list.get(position);
            holder.gqname.setText(pes.getgqname());
			holder.gqname.setSelected(true);
            holder.gqzz.setText(pes.getgqzz());
			Glide.with(song.this).load(pes.gettp()).transform(new GlideCircleTransform(song.this)).into(holder.gqtp);
			holder.cc.setOnClickListener(new OnClickListener(){
					private AlertDialog mAlertDialog;    
					LoadingDialog jz = new LoadingDialog(song.this);
					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						final View v =View.inflate(song.this, R.layout.bfmenu, null);
						TextView gmane = (TextView) v.findViewById(R.id.bfname);
						TextView gzz = (TextView) v.findViewById(R.id.bfzz);
						final ImageView gtp = (ImageView) v.findViewById(R.id.bftp);
						gmane.setText(pes.getgqname());
						gmane.setSelected(true);
						gzz.setText(pes.getgqzz());
						mAlertDialog = new AlertDialog.Builder(song.this)
							.setView(v) //设置布局
							.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									if (mp.isPlaying())
									{
										mp.stop();
										mp.reset();
									}
								}
							}).create(); //构建弹窗
						mAlertDialog.setCancelable(false); //返回键不可关闭
						mAlertDialog.setCanceledOnTouchOutside(false); //点击外部不可关闭
						mAlertDialog.setTitle("正在播放中……"); //弹窗标题
						mAlertDialog.show(); //显示弹窗
						jz.loading();
						try
						{
							mp.setDataSource(pes.getgqurl()); // 根据URI装载音频文件
							mp.prepareAsync();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
								@Override
								public void onPrepared(final MediaPlayer mp)
								{
									Glide.with(song.this)
										.load(pes.gettp())
										.asBitmap()
										.listener(new RequestListener<String, Bitmap>() {
											@Override
											public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource)
											{
												jz.cancel();
												Toasty.error(song.this, "图片已失效！" + e.getMessage()).show();
												mp.start();
												return false;
											}
											@Override
											public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource)
											{
												jz.cancel();
												Animation animation = AnimationUtils.loadAnimation(song.this, R.anim.zz);
												LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
												animation.setInterpolator(lin);
												gtp.startAnimation(animation);
												mp.start();
												return false;
											}
										})
										.into(gtp);
								}
							});
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mp)
								{
									jz.cancel();
									mp.stop();
									mp.reset();
									mAlertDialog.dismiss();
								}
							});
					}
				});
            return convertView;
        }

        class ViewHolder
		{
            public ImageView gqtp;
            public TextView gqname,gqzz;
			public CardView cc;
        }
	}

	@Override
	public void onRefresh()
	{
		// TODO: Implement this method
		songrefresh.setRefreshing(true);
        new Thread() {
            @Override
            public void run()
			{
                // 延迟1秒
                try
				{
                    sleep(2000);
                }
				catch (InterruptedException e)
				{
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                        @Override
                        public void run()
						{
                            bmobsong();                             
                            songrefresh.setRefreshing(false);
                        }
                    });
            }
		}.start();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		mp.release();
		super.onDestroy();
	}

}

