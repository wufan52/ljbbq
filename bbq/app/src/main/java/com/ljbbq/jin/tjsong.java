package com.ljbbq.jin;

import android.annotation.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.bumptech.glide.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.request.target.*;
import com.ljbbq.jin.base.*;
import com.ljbbq.jin.bmob.*;
import es.dmoral.toasty.*;
import java.io.*;
import java.util.*;
import okhttp3.*;
import org.json.*;
import android.support.v7.widget.Toolbar;
import okhttp3.Request;
import android.view.animation.*;
import com.wega.library.loadingDialog.*;

public class tjsong extends BaseActivity
{

	final MediaPlayer mp = new MediaPlayer();

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
	}

	public  JSONObject object;
	public 	ListView lv;
	private TextView a;
	public ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tjsong);
		Toolbar toolbar = (Toolbar) findViewById(R.id.tjsongtoolbar);
        toolbar.setTitle("添加歌曲");
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
	}

	public void tt(View v)
	{
		init();
	}

	private void init()
	{
		// TODO: Implement this method
		a = (TextView) findViewById(R.id.a);
		final String u=a.getText().toString();
		final String q="&type=song&pageSize=50&page=0&format=1";
        //https://api.itooi.cn/music/netease/search?key=579621905&s=我喜欢上你内心时的活动&type=song&limit=100&offset=0
		list.clear();
		lv = (ListView) findViewById(R.id.list);
		new Thread(new Runnable() {
				@Override
				public void run()
				{
					OkHttpClient okHttpClient=new OkHttpClient();
					//服务器返回的地址
					Request request=new Request.Builder().url("https://v1.itooi.cn/netease/search?keyword=" + u + q).build();
					try
					{
						Response response=okHttpClient.newCall(request).execute();
						//获取到数据
						String date=response.body().string();
						//把数据传入解析josn数据方法
						jsonJX(date);

					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
	}
	private void jsonJX(String date)
	{
		//判断数据是空
		if (date != null)
		{
			try
			{
				//将字符串转换成jsonObject对象
				JSONObject jsonObject = new JSONObject(date);
				//获取返回数据中flag的值
				String resultCode = jsonObject.getString("msg");  
				//如果返回的值是success则正确
				if (resultCode.equals("OK"))
				{
					//获取到json数据中里的activity数组内容
					JSONArray resultJsonArray = jsonObject.getJSONArray("data");  
					//遍历
					for (int i=0;i < resultJsonArray.length();i++)
					{
						object = resultJsonArray.getJSONObject(i);
						Map<String, Object> map=new HashMap<String, Object>();
						try
						{
							//获取到json数据中的activity数组里的内容name
							String name = object.getString("name");
							//获取到json数据中的activity数组里的内容startTime
							String zz = object.getString("singer");
							String url=object.getString("url");
							String id = object.getString("id");
							String pic = object.getString("pic");
							//存入map
							map.put("name", name);
							map.put("singer", zz);
							map.put("url", url);
							map.put("id", id);
							map.put("pic", pic);
							//ArrayList集合
							list.add(map);
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}  
		}
		else
		{
			Toasty.error(tjsong.this, "网络连接超时！请检查网络重试！").show();
		}
	}
	//Handler运行在主线程中(UI线程中)，  它与子线程可以通过Message对象来传递数据
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					Mybaseadapter list_item=new Mybaseadapter();
					lv.setAdapter(list_item);
					break;
			}
		}
	}; 
	public class Mybaseadapter extends BaseAdapter
	{
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
			ViewHolder viewHolder = null;  
			if (convertView == null)
			{  
				viewHolder = new ViewHolder();  

				convertView = getLayoutInflater().inflate(R.layout.tjsongitem, null);  
				viewHolder.gqname = (TextView) convertView.findViewById(R.id.tjgqname); 
				viewHolder.gqurl = (TextView) convertView.findViewById(R.id.gqurl); 
				viewHolder.gqid = (TextView) convertView.findViewById(R.id.gqid);  
				viewHolder.zz = (TextView) convertView.findViewById(R.id.tjgqzz);
				viewHolder.gqtp = (com.ljbbq.jin.image.imageview) convertView.findViewById(R.id.tjgqtp);
				viewHolder.gqbf = (ImageView) convertView.findViewById(R.id.tjgqbf);
				viewHolder.d = (LinearLayout) convertView.findViewById(R.id.tjgqtj);
				convertView.setTag(viewHolder);  
			}
			else
			{  
				viewHolder = (ViewHolder) convertView.getTag();  
			}  
			viewHolder.gqname.setText(list.get(position).get("name").toString());  
			viewHolder.gqname.setSelected(true);
			viewHolder.zz.setText(list.get(position).get("singer").toString());  
			viewHolder.gqurl.setText(list.get(position).get("url").toString());  
			viewHolder.gqid.setText(list.get(position).get("id").toString());
			Glide.with(tjsong.this)
                //显示获取到的图片链接
                .load(list.get(position).get("pic").toString())
                //缩略图（80%）
                .thumbnail(0.5f)
                //淡出淡入动画 默认300毫秒
                .crossFade(300)
                // 跳过缓存
                .skipMemoryCache(false)
                //显示图片
                .into(viewHolder.gqtp);
			viewHolder.gqbf.setOnClickListener(new OnClickListener(){    
					LoadingDialog jz = new LoadingDialog(tjsong.this);
					@Override
					public void onClick(View p1)
					{
						User user = new User();
						user = BmobUser.getCurrentUser(User.class);
						if (user == null)
						{
							Toasty.error(tjsong.this, "请登录再发表！").show();
							Intent intent = new Intent();
							intent.setClass(tjsong.this, login.class);
							startActivity(intent);
						}
						else
						{
							jz.loading();
							bmobsong song = new bmobsong();
							song.setgqname(list.get(position).get("name").toString());
							song.setgqzz(list.get(position).get("singer").toString());
							song.setgqurl(list.get(position).get("url").toString());
							song.setgqid(list.get(position).get("id").toString());
							song.setgqtp(list.get(position).get("pic").toString());
							song.setAuthor(BmobUser.getCurrentUser(User.class));
							song.save(new SaveListener<String>() {
									@Override
									public void done(String s, BmobException e)
									{
										if (e == null)
										{
											jz.cancel();
											Toasty.success(tjsong.this, "音乐添加成功").show();
										}
										else
										{
											jz.cancel();
											Toasty.error(tjsong.this, "音乐添加失败！" + e.getMessage()).show();
										}
									}
								});
						}

					}
				});
			viewHolder.d.setOnClickListener(new OnClickListener(){

					private AlertDialog mAlertDialog;    
					LoadingDialog loadingDialog = new LoadingDialog(tjsong.this);
					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						final View v =View.inflate(tjsong.this, R.layout.bfmenu, null);
						TextView gmane = (TextView) v.findViewById(R.id.bfname);
						TextView gzz = (TextView) v.findViewById(R.id.bfzz);
						final ImageView gtp = (ImageView) v.findViewById(R.id.bftp);
						gmane.setText(list.get(position).get("name").toString());
						gmane.setSelected(true);
						gzz.setText(list.get(position).get("singer").toString());
						mAlertDialog = new AlertDialog.Builder(tjsong.this)
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
						loadingDialog.loading();
						try
						{
							mp.setDataSource(list.get(position).get("url").toString()); // 根据URI装载音频文件
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
									Glide.with(tjsong.this)
										.load(list.get(position).get("pic").toString())
										.asBitmap()
										.listener(new RequestListener<String, Bitmap>() {
											@Override
											public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource)
											{
												Toasty.error(tjsong.this, "图片已失效！" + e.getMessage()).show();
												loadingDialog.cancel();
												mp.start();
												return false;
											}
											@Override
											public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource)
											{
												Animation animation = AnimationUtils.loadAnimation(tjsong.this, R.anim.zz);
												LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
												animation.setInterpolator(lin);
												gtp.startAnimation(animation);
												mp.start();
												loadingDialog.cancel();
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
									loadingDialog.cancel();
									mp.stop();
									mp.reset();
									mAlertDialog.dismiss();
								}
							});
					}
				});
			return convertView;  
		}  
	}  
	final static class ViewHolder
	{  
		TextView gqname,gqurl; 
		TextView gqid;  
		TextView zz;
		ImageView gqtp,gqbf;
		LinearLayout d;
	}
	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		mp.release();
		super.onDestroy();
	}

}
