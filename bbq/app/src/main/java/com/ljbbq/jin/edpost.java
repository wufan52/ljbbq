package com.ljbbq.jin;
import android.*;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.database.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import cn.bmob.v3.datatype.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.ljbbq.jin.base.*;
import com.ljbbq.jin.bmob.*;
import es.dmoral.toasty.*;
import java.io.*;
import java.util.*;

import android.support.v7.widget.Toolbar;
import cn.bmob.v3.*;
import com.bumptech.glide.*;
import android.media.*;
import com.ljbbq.jin.tpurl.*;
import com.wega.library.loadingDialog.*;
import com.bumptech.glide.request.*;
import android.view.animation.*;
import com.bumptech.glide.request.target.*;

public class edpost extends BaseActivity
{

	private AlertDialog mAlertDialog;

    @Override
    public void onClick(View p1)
	{
        // TODO: Implement this method
    }

    public static final int CHOOSE_PHOTO = 2;
    private String path = "";
    private EditText ed_content,ed_bt;
    private ImageView select_img,yytp;
	private TextView yynm,yyzz,tjyy;
    private ProgressDialog dialog;
	private Switch nm;
	private boolean nim = false;
    public ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
	final MediaPlayer mp = new MediaPlayer();
	private String yy ="";
	private String tt ="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edpost);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activityloginToolbar1);
        toolbar.setTitle("表白发布");
        setSupportActionBar(toolbar);
        ed_bt = (EditText) findViewById(R.id.bt);
        ed_content = (EditText) findViewById(R.id.ed_contents);
        select_img = (ImageView) findViewById(R.id.edit_img);
		nm = (Switch) findViewById(R.id.nm);
		yytp = (ImageView) findViewById(R.id.yytp);
		yynm = (TextView) findViewById(R.id.yyname);
		yyzz = (TextView) findViewById(R.id.yyzz);
		tjyy = (TextView) findViewById(R.id.tjyy);
        //toolbar添加返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //返回按钮点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
				{
                    Intent a = new Intent();
                    a.setClass(edpost.this, MainActivity.class);
                    startActivity(a);
                    finish();
                }
			});
        android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fa);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View p1)
				{
                    // TODO: Implement this method
                    fb();
                }
            });
		Switch();
		tj();
    }

	private void Switch()
	{
		// TODO: Implement this method
		nm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
											 boolean isChecked)
				{
					// TODO Auto-generated method stub
					if (isChecked)
					{
						//Toasty.success(edpost.this,"匿名开启").show();
						nim = true;
					}
					else
					{
						//Toasty.warning(edpost.this,"匿名关闭").show();
						nim = false;
					}
				}
			});
	}

    /*
     选择图片
     */
    public void choose_img(View view)
	{
        //获取读取数据权限
        if (ContextCompat.checkSelfPermission(edpost.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
		{
            ActivityCompat.requestPermissions(edpost.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        }
		else
		{
            openAlbum();
        }
    }
    private void openAlbum()
	{
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
        switch (requestCode)
		{
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
                    openAlbum();
                }
				else
				{
                    Toast.makeText(this, "你拒绝了相册使用权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        switch (requestCode)
		{

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK)
				{
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19)
					{
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }
					else
					{
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data)
	{
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri))
		{
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority()))
			{
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }
			else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
			{
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
		else if ("content".equalsIgnoreCase(uri.getScheme()))
		{
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
		else if ("file".equalsIgnoreCase(uri.getScheme()))
		{
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }
    private void handleImageBeforeKitKat(Intent data)
	{
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
    private void displayImage(String imagePath)
	{
        if (imagePath != null)
		{
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            select_img.setImageBitmap(bitmap);
            //Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
        }
		else
		{
            Toast.makeText(this, "获取图像失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection)
	{

        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null)
		{
            if (cursor.moveToFirst())
			{
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void fb()
	{
        // TODO: Implement this method
        if (ed_bt.getText().toString().isEmpty() | ed_content.getText().toString().isEmpty())
		{
            Toasty.warning(edpost.this, "请保证内容不为空！！").show();
        }
		else if (path != "")
		{
            dialog = new ProgressDialog(edpost.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setTitle("上传图片中...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            String filepath=path;
            final BmobFile file = new BmobFile(new File(filepath));
            file.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException p1)
					{
                        if (p1 == null)
						{
							if (nim == true)
							{
								User user=User.getCurrentUser(User.class);
								Post post=new Post();
								post.setIcon(file);
								String img_url = file.getUrl();
								post.settpurl(img_url);
								post.setbt(ed_bt.getText().toString());
								post.setnr(ed_content.getText().toString());
								post.settx(user.getimageurl());
								post.setUsername(user.getName());
								post.setnm(true);
								post.setsggq(yynm.getText().toString());
								post.setsgzz(yyzz.getText().toString());
								post.setsgtp(tt);
								post.setsgurl(yy);
								post.setAuthor(user);
								post.save(new SaveListener<String>(){
										@Override
										public void done(String p1, BmobException p2)
										{
											if (p2 == null)
											{
												Toasty.success(edpost.this, "发贴成功！！！").show();
												Intent a = new Intent();
												a.setClass(edpost.this, MainActivity.class);
												startActivity(a);
												finish();
											}
											else
											{
												Toasty.error(edpost.this, "发帖失败！" + p1 + p2).show();
											}
											// TODO: Implement this method
										}
									});
							}
							else if (nim == false)
							{
								User user=User.getCurrentUser(User.class);
								Post post=new Post();
								post.setIcon(file);
								String img_url = file.getUrl();
								post.settpurl(img_url);
								post.setbt(ed_bt.getText().toString());
								post.setnr(ed_content.getText().toString());
								post.settx(user.getimageurl());
								post.setUsername(user.getName());
								post.setnm(false);
								post.setsggq(yynm.getText().toString());
								post.setsgzz(yyzz.getText().toString());
								post.setsgtp(tt);
								post.setsgurl(yy);
								post.setAuthor(user);
								post.save(new SaveListener<String>(){
										@Override
										public void done(String p1, BmobException p2)
										{
											if (p2 == null)
											{
												Toasty.success(edpost.this, "发贴成功！！！").show();
												Intent a = new Intent();
												a.setClass(edpost.this, MainActivity.class);
												startActivity(a);
												finish();
											}
											else
											{
												Toasty.error(edpost.this, "发帖失败！" + p1 + p2).show();
											}
											// TODO: Implement this method
										}
									});
							}
                        }
						else
						{
                            Toasty.error(edpost.this, p1.toString() + "图片上传失败！").show();
                        }
                    }
                    @Override
                    public void onProgress(Integer value)
					{
                        // 返回的上传进度（百分比）
                        dialog.setProgress(value);
                    }
                });
        }
		else
		{
			if (nim == true)
			{
				User user=User.getCurrentUser(User.class);
				Post post=new Post();
				post.setbt(ed_bt.getText().toString());
				post.setnr(ed_content.getText().toString());
				post.settx(user.getimageurl());
				post.setUsername(user.getName());
				post.setnm(true);
				post.setsggq(yynm.getText().toString());
				post.setsgzz(yyzz.getText().toString());
				post.setsgtp(tt);
				post.setsgurl(yy);
				post.setAuthor(user);
				post.save(new SaveListener<String>(){
						@Override
						public void done(String p1, BmobException p2)
						{
							if (p2 == null)
							{
								Toasty.success(edpost.this, "发贴成功！！！").show();
								Intent a = new Intent();
								a.setClass(edpost.this, MainActivity.class);
								startActivity(a);  
								finish();
							}
							else
							{
								Toasty.error(edpost.this, "发帖失败！" + p1 + p2).show();
							}
							// TODO: Implement this method
						}
					});
			}
			else if (nim == false)
			{
				User user=User.getCurrentUser(User.class);
				Post post=new Post();
				post.setbt(ed_bt.getText().toString());
				post.setnr(ed_content.getText().toString());
				post.settx(user.getimageurl());
				post.setUsername(user.getName());
				post.setnm(false);
				post.setsggq(yynm.getText().toString());
				post.setsgzz(yyzz.getText().toString());
				post.setsgtp(tt);
				post.setsgurl(yy);
				post.setAuthor(user);
				post.save(new SaveListener<String>(){
						@Override
						public void done(String p1, BmobException p2)
						{
							if (p2 == null)
							{
								Toasty.success(edpost.this, "发贴成功！！！").show();
								Intent a = new Intent();
								a.setClass(edpost.this, MainActivity.class);
								startActivity(a);  
								finish();
							}
							else
							{
								Toasty.error(edpost.this, "发帖失败！" + p1 + p2).show();
							}
							// TODO: Implement this method
						}
					});
			}
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edpostmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.gq)
		{
            Toasty.success(edpost.this, "成功").show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private void tj()
	{
		// TODO: Implement this method

		tjyy.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					String a = tjyy.getText().toString();
					if (a.equals("添加"))
					{
						final View v =View.inflate(edpost.this, R.layout.bjyy, null);
						final ListView bjyylist = (ListView) v.findViewById(R.id.bjlist);
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
											MyAdapter adapter=new MyAdapter(datas, edpost.this);
											bjyylist.setAdapter(adapter);
										}
									}
									else
									{
										Toasty.error(edpost.this, "网络超时请检查网络！" + p2.toString()).show();
									}
									// TODO: Implement this method
								}
							});
						mAlertDialog = new AlertDialog.Builder(edpost.this)
							.setView(v) //设置布局
							.setPositiveButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
								}
							}).create(); //构建弹窗
						mAlertDialog.setCancelable(false); //返回键不可关闭
						mAlertDialog.setCanceledOnTouchOutside(false); //点击外部不可关闭
						mAlertDialog.setTitle("添加背景音乐"); //弹窗标题
						mAlertDialog.show(); //显示弹窗
					}
					else if (a.equals("撤回"))
					{
						yynm.setText("未添加歌曲");
						yyzz.setText("未知");
						Glide.with(edpost.this).load(R.drawable.yinyue).transform(new GlideCircleTransform(edpost.this)).into(yytp);
						tjyy.setText("添加");
						yy = "";
						tt = "";
					}
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
				convertView = getLayoutInflater().inflate(R.layout.tjsongitem, null);  
				holder.gqname = (TextView) convertView.findViewById(R.id.tjgqname); 
				holder.gqurl = (TextView) convertView.findViewById(R.id.gqurl); 
				holder.gqid = (TextView) convertView.findViewById(R.id.gqid);  
				holder.zz = (TextView) convertView.findViewById(R.id.tjgqzz);
				holder.gqtp = (com.ljbbq.jin.image.imageview) convertView.findViewById(R.id.tjgqtp);
				holder.gqbf = (ImageView) convertView.findViewById(R.id.tjgqbf);
				holder.d = (LinearLayout) convertView.findViewById(R.id.tjgqtj);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
			else
			{
                holder = (ViewHolder) convertView.getTag();
            }

			final bmobsong pes=list.get(position);
            holder.gqname.setText(pes.getgqname());
			holder.gqname.setSelected(true);
            holder.zz.setText(pes.getgqzz());
			holder.gqurl.setText(pes.getgqurl());
			Glide.with(edpost.this)
                //显示获取到的图片链接
                .load(pes.gettp())
                //缩略图（80%）
                .thumbnail(0.5f)
                //淡出淡入动画 默认300毫秒
                .crossFade(300)
                // 跳过缓存
                .skipMemoryCache(false)
                //显示图片
                .into(holder.gqtp);
			holder.d.setOnClickListener(new OnClickListener(){
					private AlertDialog mAlertDialog1;    
					LoadingDialog jz = new LoadingDialog(edpost.this);
					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						final View v =View.inflate(edpost.this, R.layout.bfmenu, null);
						TextView gmane = (TextView) v.findViewById(R.id.bfname);
						TextView gzz = (TextView) v.findViewById(R.id.bfzz);
						final ImageView gtp = (ImageView) v.findViewById(R.id.bftp);
						gmane.setText(pes.getgqname());
						gmane.setSelected(true);
						gzz.setText(pes.getgqzz());
						mAlertDialog1 = new AlertDialog.Builder(edpost.this)
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
						mAlertDialog1.setCancelable(false); //返回键不可关闭
						mAlertDialog1.setCanceledOnTouchOutside(false); //点击外部不可关闭
						mAlertDialog1.setTitle("正在播放中……"); //弹窗标题
						mAlertDialog1.show(); //显示弹窗
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
									Glide.with(edpost.this)
										.load(pes.gettp())
										.asBitmap()
										.listener(new RequestListener<String, Bitmap>() {
											@Override
											public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource)
											{
												jz.cancel();
												Toasty.error(edpost.this, "图片已失效！" + e.getMessage()).show();
												mp.start();
												return false;
											}
											@Override
											public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource)
											{
												jz.cancel();
												Animation animation = AnimationUtils.loadAnimation(edpost.this, R.anim.zz);
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
									mAlertDialog1.dismiss();
								}
							});
					}
				});
			holder.gqbf.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						yynm.setText(pes.getgqname());
						yyzz.setText(pes.getgqzz());
						Glide.with(edpost.this).load(pes.gettp()).transform(new GlideCircleTransform(edpost.this)).into(yytp);
						tjyy.setText("撤回");
						yy = pes.getgqurl();
						tt = pes.gettp();
						mAlertDialog.dismiss();
					}
				});
            return convertView;
        }

        class ViewHolder
		{
            TextView gqname,gqurl; 
			TextView gqid;  
			TextView zz;
			ImageView gqtp,gqbf;
			LinearLayout d;
        }
	}

    @Override
    public void onBackPressed()
	{
        //TODO something
        super.onBackPressed();
        Intent a = new Intent();
        a.setClass(edpost.this, MainActivity.class);
        startActivity(a);
        finish();
    }

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		mp.release();
		super.onDestroy();
	}
}
