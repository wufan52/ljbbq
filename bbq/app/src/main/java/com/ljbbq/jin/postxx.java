package com.ljbbq.jin;
import com.ljbbq.jin.base.*;
import android.view.*;
import android.os.*;
import android.text.*;
import android.support.v4.widget.*;
import android.widget.*;
import java.util.*;
import android.content.*;
import android.support.v7.widget.Toolbar;
import com.ljbbq.jin.bmob.*;
import android.view.View.*;
import cn.bmob.v3.*;
import cn.bmob.v3.datatype.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import com.ljbbq.jin.tpurl.*;
import es.dmoral.toasty.*;
import android.widget.AdapterView.*;
import android.support.v7.app.*;
import android.graphics.*;
import java.text.*;
import com.wega.library.loadingDialog.*;
import com.bumptech.glide.*;
import android.media.*;
import java.io.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.request.target.*;
import android.view.animation.*;

public class postxx extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
{
	private SwipeRefreshLayout sr;
	private ListView list;
	private Handler handler = new Handler();
	List<comment>datas=new ArrayList<>();
	private MyAdapter myAdapter;
	private TextView name,message,dz1,pl1,fx1,sc1,foo,sggq;
	private String ID;
	private ImageView img1,img2,sc,sgtp;
	private View header,footer;
	private String ti;
	private boolean dian = false;
	private boolean souc = false;
	private com.ljbbq.jin.view.Round dzt,sct,plt,fxt;
	MediaPlayer mp3 = new MediaPlayer();
	private String gqurl = "";

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
		setContentView(R.layout.postxx);
		Intent 接收=postxx.this.getIntent();
		ID = 接收.getStringExtra("objectid");
		final Toolbar toolbar = (Toolbar) findViewById(R.id.activityloginToolbar1);
		BmobQuery<Post>Query=new BmobQuery<Post>();
		Query.getObject(ID, new QueryListener<Post>(){
				@Override
				public void done(Post p1, BmobException p2)
				{
					if (p2 == null)
					{
						toolbar.setTitle(p1.getbt());
					}
				}
			});
		//toolbar.setTitle(ti);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true); 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Intent intent=new Intent();
					setResult(0, intent);
					finish();
				}
			});
		sr = (SwipeRefreshLayout) findViewById(R.id.swipe_re1);
		sr.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_blue_bright);
		sr.setOnRefreshListener(this);
		list = (ListView)findViewById(R.id.reListView1);
		header = getLayoutInflater().inflate(R.layout.postxx1, null);
		name = (TextView) header.findViewById(R.id.postname);
		message = (TextView) header.findViewById(R.id.postnr);	
		img1 = (ImageView) header.findViewById(R.id.posttp);
		img2 = (ImageView) header.findViewById(R.id.posttx);
		sggq = (TextView) header.findViewById(R.id.sgq);
		sggq.setSelected(true);
		sgtp = (ImageView) header.findViewById(R.id.stp);
		footer = LayoutInflater.from(postxx.this).inflate(R.layout.footer, null);
		foo = (TextView) footer.findViewById(R.id.foo);
		list.addHeaderView(header);
		list.addFooterView(footer, null, true);
		//禁止底部出现分割线 
		list.setFooterDividersEnabled(false);
		myAdapter = new MyAdapter(datas, postxx.this);
		list.setAdapter(myAdapter);
		intbmob();
		initlist();
		button();
	}

	private void intbmob()
	{
		// TODO: Implement this method
		final User user = BmobUser.getCurrentUser(User.class);
		final LoadingDialog t1 = new LoadingDialog(postxx.this);
		t1.loading();
		if (user == null)
		{
			t1.cancel();
			Toasty.warning(postxx.this, "请登录后再操作！").show();
			Intent on = new Intent();
			on.setClass(postxx.this, login.class);
			startActivity(on);
			finish();
		}
		else
		{
			BmobQuery<User> query = new BmobQuery<User>();
			Post post = new Post();
			post.setObjectId(ID);
			//likes是Post表中的字段，用来存储所有喜欢该帖子的用户
			query.addWhereRelatedTo("likes", new BmobPointer(post));
			query.findObjects(new FindListener<User>() {
					@Override
					public void done(List<User> object, BmobException e)
					{
						if (e == null)
						{
							List<User>data=new ArrayList<>();
							for (User p:object)
							{
								if (p.getUsername().equals(user.getUsername()))
								{
									souc = true;
								}
							}
							BmobQuery<Post>Query=new BmobQuery<Post>();
							Query.include("author");
							Query.getObject(ID, new QueryListener<Post>(){
									@Override
									public void done(Post p1, BmobException p2)
									{
										if (p2 == null)
										{
											t1.cancel();
											if (p1.gettpurl() == null)
											{
												img1.setVisibility(View.GONE);
											}
											else
											{
												img1.setVisibility(View.VISIBLE);
												Glide.with(postxx.this).load(p1.gettpurl()).transform(new yu(postxx.this, 16)).into(img1);	
											}
											if (p1.getnm() == true)
											{
												name.setText("匿名用户");
												Glide.with(postxx.this).load(R.drawable.ninv).transform(new GlideCircleTransform(postxx.this)).into(img2);
											}
											else if (p1.getnm() == false)
											{
												Glide.with(postxx.this).load(p1.getAuthor().getimageurl()).transform(new GlideCircleTransform(postxx.this)).into(img2);
												name.setText(p1.getAuthor().getName());
											}
											ti = p1.getbt();
											message.setText(p1.getnr());
											String dd1=Integer.toString(p1.getdzSize());
											String ss1=Integer.toString(p1.getscSize());
											String pp1=Integer.toString(p1.getPlsize());
											dz1.setText(dd1);
											sc1.setText(ss1);
											pl1.setText(pp1);
											if (souc == true)
											{
												Glide.with(postxx.this).load(R.drawable.ic_heart).into(sc);
											}
											else
											{
												Glide.with(postxx.this).load(R.drawable.ic_favorite_outline_black_24dp).into(sc);
											}
											if (p1.getsggq().equals("未添加歌曲"))
											{
												sggq.setText("未添加歌曲");
												Glide.with(postxx.this).load(R.drawable.ic_audiotrack_black_24dp).transform(new GlideCircleTransform(postxx.this)).into(sgtp);
											}
											else
											{
												sggq.setText(p1.getsggq());
												gqurl = p1.getsgurl();
												Glide.with(postxx.this)
													.load(p1.getsgtp())
													.asBitmap()
													.listener(new RequestListener<String, Bitmap>() {
														@Override
														public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource)
														{
															Toasty.error(postxx.this, "图片已失效！" + e.getMessage()).show();
															yinyue();
															return false;
														}
														@Override
														public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource)
														{
															yinyue();
															return false;
														}
													})
													.into(sgtp);
											}
										}
										else
										{
											t1.cancel();
											Toasty.error(postxx.this, "加载失败！" + p2).show();
										}
									}

									private void yinyue()
									{
										// TODO: Implement this method
										try
										{
											mp3.setDataSource(gqurl);
											mp3.prepareAsync();
										}
										catch (IOException e)
										{} // 根据URI装载音频文件
										mp3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
												@Override
												public void onPrepared(final MediaPlayer mp)
												{
													if (user.getisgq() == true)
													{
														Animation animation = AnimationUtils.loadAnimation(postxx.this, R.anim.zz);
														LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
														animation.setInterpolator(lin);
														sgtp.startAnimation(animation);
														mp3.start();
													}
												}
											});
										mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
												@Override
												public void onCompletion(MediaPlayer mp)
												{
													mp3.stop();
													mp3.reset();
												}
											});
									}
								});
						}
						else
						{
							t1.cancel();
						}
					}
				});
		}
	}

	private void button()
	{
		// TODO: Implement this method
		final User user = BmobUser.getCurrentUser(User.class);
	    dz1 = (TextView) findViewById(R.id.dz1);
		pl1 = (TextView) findViewById(R.id.pl1);
		sc = (ImageView) findViewById(R.id.sc);
		sc1 = (TextView) findViewById(R.id.sc1);
		fx1 = (TextView) findViewById(R.id.fx1);
		dzt = (com.ljbbq.jin.view.Round) findViewById(R.id.dzt);
		plt = (com.ljbbq.jin.view.Round) findViewById(R.id.plt);
		sct = (com.ljbbq.jin.view.Round) findViewById(R.id.sct);
		fxt = (com.ljbbq.jin.view.Round) findViewById(R.id.fxt);
		dzt.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if (user == null)
					{
						Toasty.warning(postxx.this, "请登录后再操作！").show();
					}
					else
					{
						if (dian == false)
						{
							BmobQuery<Post>Query=new BmobQuery<Post>();
							Query.getObject(ID, new QueryListener<Post>(){
									@Override
									public void done(final Post p1, BmobException p2)
									{
										Post post = new Post();
										post.setObjectId(ID);
										post.update(new UpdateListener() {
												@Override
												public void done(BmobException e)
												{
													if (e == null)
													{
														final Post Size=new Post();
														Size.setdzSize(p1.getdzSize() + 1);
														Size.setPlsize(p1.getPlsize());
														Size.setscSize(p1.getscSize());
														Size.setfxsize(p1.getfxsize());
														Size.setnm(p1.getnm());
														Size.update(ID, new UpdateListener(){

																@Override
																public void done(BmobException p1)
																{
																	if (p1 == null)
																	{
																		String zz1=Integer.toString(Size.getdzSize());
																		dz1.setText(zz1);
																		dian = true;
																	}
																	// TODO: Implement this method
																}
															});
													}
													else
													{
														Toasty.error(postxx.this, "点赞失败！").show();
													}
												}
											});
									}
								});
						}
						else
						{
							Toasty.warning(postxx.this, "你已经点过赞了，请不要重复!").show();
						}
					}
				}
			});

		plt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					User users = BmobUser.getCurrentUser(User.class);
					if (users == null)
					{
						Toasty.warning(postxx.this, "请登录后再操作！").show();
					}
					else
					{
						commentr();
					}
				}
			});

		sct.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					final LoadingDialog l = new LoadingDialog(postxx.this);
					final User user = BmobUser.getCurrentUser(User.class);
					if (user == null)
					{
						Toasty.warning(postxx.this, "请登录后再操作！").show();
					}
					else if (souc == true)
					{
						// TODO: Implement this method
						l.loading();
						BmobQuery<Post>Query=new BmobQuery<Post>();
						Query.getObject(ID, new QueryListener<Post>(){
								@Override
								public void done(final Post p1, BmobException p2)
								{
									Post post = new Post();
									post.setObjectId(ID);
									BmobRelation relation = new BmobRelation();
									relation.remove(user);
									post.setLikes(relation);
									post.update(new UpdateListener() {
											@Override
											public void done(BmobException e)
											{
												if (e == null)
												{
													final Post Size=new Post();
													Size.setscSize(p1.getscSize() - 1);
													Size.setPlsize(p1.getPlsize());
													Size.setdzSize(p1.getdzSize());
													Size.setfxsize(p1.getfxsize());
													Size.setnm(p1.getnm());
													Size.update(ID, new UpdateListener(){
															@Override
															public void done(BmobException p1)
															{
																if (p1 == null)
																{
																	l.cancel();
																	Toasty.success(postxx.this, "已取消收藏！").show();
																	String ss1=Integer.toString(Size.getscSize());
																	sc1.setText(ss1);
																	Glide.with(postxx.this).load(R.drawable.ic_favorite_outline_black_24dp).into(sc);
																	souc = false;
																}
																else
																{
																	l.cancel();
																	Toasty.error(postxx.this, "取消失败！").show();
																}
																// TODO: Implement this method
															}
														});
												}
												else
												{
													l.cancel();
													Toasty.error(postxx.this, "取消失败！").show();
												}
											}

										});
									// TODO: Implement this method
								}
							});
					}
					else
					{
						l.loading();
						BmobQuery<Post>Query=new BmobQuery<Post>();
						Query.getObject(ID, new QueryListener<Post>(){
								@Override
								public void done(final Post p1, BmobException p2)
								{
									Post post = new Post();
									post.setObjectId(ID);
									BmobRelation relation = new BmobRelation();
									relation.add(user);
									post.setLikes(relation);
									post.update(new UpdateListener() {
											@Override
											public void done(BmobException e)
											{
												if (e == null)
												{
													final Post Size=new Post();
													Size.setscSize(p1.getscSize() + 1);
													Size.setPlsize(p1.getPlsize());
													Size.setdzSize(p1.getdzSize());
													Size.setfxsize(p1.getfxsize());
													Size.setnm(p1.getnm());
													Size.update(ID, new UpdateListener(){
															@Override
															public void done(BmobException p1)
															{
																if (p1 == null)
																{
																	l.cancel();
																	Toasty.success(postxx.this, "已添加到我的收藏！").show();
																	String ss1=Integer.toString(Size.getscSize());
																	sc1.setText(ss1);
																	Glide.with(postxx.this).load(R.drawable.ic_heart).into(sc);
																	souc = true;
																}
																else
																{
																	l.cancel();
																	Toasty.error(postxx.this, "添加失败！").show();
																}
																// TODO: Implement this method
															}
														});
													//toast("添加到我的收藏成功");
												}
												else
												{
													l.cancel();
													Toasty.error(postxx.this, "添加失败！").show();
												}
											}

										});
									// TODO: Implement this method
								}
							});
					}
				}
			});

		fxt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method

				}
			});
	}

	private void commentr()
	{
		// TODO: Implement this method
		final LoadingDialog t = new LoadingDialog(postxx.this);
		final EditText sign=new EditText(postxx.this);
		sign.setHint("评论内容………");
		AlertDialog.Builder dialog=new AlertDialog.Builder(postxx.this);
		dialog.setTitle("发布评论");
		dialog.setView(sign);
		dialog.setPositiveButton("发布", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					t.loading();
					SimpleDateFormat formatter = new SimpleDateFormat("yy年MM月dd日HH时mm分");
					String date = formatter.format(new Date());
					final User user=User.getCurrentUser(User.class);
					Post post=new Post();
					post.setObjectId(ID);
					comment com=new comment();
					com.setUse(user);
					com.setUseimg(user.getimageurl());
					com.setPost(post);
					com.setUser(user.getName());
					com.settime(date);
					com.setMessage(sign.getText().toString());
					com.save(new SaveListener<String>(){
							@Override
							public void done(String p1, BmobException p2)
							{
								if (p2 == null)
								{
									//Toasty.success(postxx.this, "评论成功！").show();
									BmobQuery<Post>Query=new BmobQuery<Post>();
									Query.getObject(ID, new QueryListener<Post>(){
											@Override
											public void done(final Post p1, BmobException p2)
											{
												Post post = new Post();
												post.setObjectId(ID);
												post.update(new UpdateListener() {
														@Override
														public void done(BmobException e)
														{
															if (e == null)
															{
																final Post Size=new Post();
																Size.setPlsize(p1.getPlsize() + 1);
																Size.setdzSize(p1.getdzSize());
																Size.setscSize(p1.getscSize());
																Size.setfxsize(p1.getfxsize());
																Size.setnm(p1.getnm());
																Size.update(ID, new UpdateListener(){

																		@Override
																		public void done(BmobException p1)
																		{
																			if (p1 == null)
																			{
																				t.cancel();
																				Toasty.success(postxx.this, "评论成功！").show();
																				String pp1=Integer.toString(Size.getPlsize());
																				pl1.setText(pp1);
																			}
																			// TODO: Implement this method
																		}
																	});
															}
															else
															{
																t.cancel();
																Toasty.error(postxx.this, "未知错误！").show();
															}
														}
													});
											}
										});
									initlist();
								}
								else
								{
									t.cancel();
									Toasty.error(postxx.this, "网络超时请检查网络！" + p2.toString()).show();
								}
								// TODO: Implement this method
							}
						});
					// TODO: Implement this method
				}
			});
		dialog.setNegativeButton("取消", null);
		dialog.create().show();
	}

	private void initlist()
	{
		// TODO: Implement this method
		BmobQuery<comment> query=new BmobQuery<comment>();
		Post post=new Post();
		post.setObjectId(ID);
		query.addWhereEqualTo("post", new BmobPointer(post));
		query.order("createdAt");//依照maps排序时间排序
		query.include("use");
//返回50条maps，如果不加上这条语句，默认返回10条maps
		query.setLimit(500);
		//query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
		query.findObjects(new FindListener<comment>(){
				@Override
				public void done(List<comment> p1, BmobException p2)
				{
					if (p2 == null)
					{
						datas.clear();
						for (comment get:p1)
						{
							datas.add(get);
						}
						if (p1.size() == 0)
						{
							foo.setText("还没有评论，快来占楼吧😉😉😏");
						}
						else
						{
							foo.setText("没有更多评论了😂😂😂");
						}
						myAdapter.notifyDataSetChanged();
					}
					else
					{
					}
					// TODO: Implement this method
				}
			});
	}

	class MyAdapter extends BaseAdapter
	{
		private List<comment>list=null;
		private Context context;
		private LayoutInflater mInflater=null;
		public MyAdapter(List<comment> list, Context context)
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
				convertView = mInflater.inflate(R.layout.postxx22, null);
				holder.userimg = (ImageView) convertView.findViewById(R.id.relistitem1ImageView1);
				holder.name = (TextView) convertView.findViewById(R.id.relistitem1TextView1);
				holder.message = (TextView) convertView.findViewById(R.id.relistitem1TextView2);
				holder.lc = (TextView) convertView.findViewById(R.id.relistitem1TextView4);
				holder.time = (TextView) convertView.findViewById(R.id.relistitem1TextView3);
				holder.userxb = (ImageView) convertView.findViewById(R.id.userxb);
				convertView.setTag(holder);//绑定ViewHolder对象
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			final comment pes=list.get(position);
			Glide.with(postxx.this).load(pes.getUse().getimageurl()).transform(new GlideCircleTransform(postxx.this)).into(holder.userimg);
			/**设置TextView显示的内容，即我们存放在动态数组中的数据*/
			int x = position + 1;
			String s=String.valueOf(x);
			holder.lc.setText(s + "楼"); 
			holder.name.setText(pes.getUse().getName());
			holder.message.setText(pes.getMessage());
			holder.time.setText(pes.gettime());
			String xb = pes.getUse().getxb();
			if (xb.equals("男神"))
			{
				Glide.with(postxx.this).load(R.drawable.lan).asBitmap().into(holder.userxb);
			}
			else if (xb.equals("女神"))
			{
				Glide.with(postxx.this).load(R.drawable.nv).asBitmap().into(holder.userxb);
			}
			return convertView;
		}

		class ViewHolder
		{
			public ImageView userimg,userxb;
			public TextView name,message,lc,time;
		}
	}

	@Override
	public void onRefresh()
	{
		sr.setRefreshing(true);

		new Thread() {
			@Override
			public void run()
			{
				// 延迟1秒
				try
				{
					sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				handler.post(new Runnable() {
						@Override
						public void run()
						{
							initlist();							
							sr.setRefreshing(false);
						}
					});
			}
		}.start();
		// TODO: Implement this method
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		mp3.stop();
		mp3.release();
		super.onDestroy();
	}
	
}
