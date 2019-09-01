package com.ljbbq.jin;
import com.ljbbq.jin.base.*;
import android.support.v4.widget.*;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.os.*;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import java.util.*;
import com.ljbbq.jin.bmob.*;
import cn.bmob.v3.exception.*;
import android.widget.*;
import es.dmoral.toasty.*;
import android.content.*;
import com.bumptech.glide.*;
import com.ljbbq.jin.tpurl.*;
import android.widget.AdapterView.*;

public class scactivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
{
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
	}
	private ListView sclist;
	private SwipeRefreshLayout screfresh;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scactivity);
		Toolbar toolbar = (Toolbar) findViewById(R.id.sctoolbar);
        toolbar.setTitle("我的收藏");
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
		sclist = (ListView) findViewById(R.id.scListView);
		screfresh = (SwipeRefreshLayout) findViewById(R.id.screfresh);
		screfresh.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_blue_bright);
		screfresh.setOnRefreshListener(this);
		onRefresh();
		scbmob();
		list();
	}

	private void list()
	{
        // TODO: Implement this method
        sclist.setOnItemClickListener(new OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
                    View adapterView=p2;
                    final TextView id = (TextView) adapterView.findViewById(R.id.a1listitemTextView5);
					Intent 传递=new Intent();
					传递.putExtra("objectid", id.getText().toString());
					传递.setClass(scactivity.this, postxx.class);
					startActivity(传递);
					// TODO: Implement this method
                }
			});
    }

	private void scbmob()
	{
		// TODO: Implement this method
		User m=BmobUser.getCurrentUser(User.class);
		BmobQuery<Post> query=new BmobQuery<>();
		query.addWhereEqualTo("likes", m);
		query.include("author");
		query.setLimit(500);
		query.findObjects(new FindListener<Post>(){
				@Override
				public void done(List<Post> p1, BmobException p2)
				{
					if (p2 == null)
					{
						List<Post>datas=new ArrayList<>();
                        for (Post get:p1)
						{
                            datas.add(get);
                            MyAdapter adapter=new MyAdapter(datas, scactivity.this);
                            sclist.setAdapter(adapter);
                        }
					}else{
						Toasty.error(scactivity.this,"未知错误请检查网络连接！"+p2.toString()).show();
					}
				}
			});
	}
	
	class MyAdapter extends BaseAdapter
	{
        private List<Post>list=null;
        private Context context;
        private LayoutInflater mInflater=null;
        public MyAdapter(List<Post> list, Context context)
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
                convertView = mInflater.inflate(R.layout.item, null);
				holder.ImageURL = (ImageView) convertView.findViewById(R.id.f1listitemImageView1);
                holder.Title = (TextView) convertView.findViewById(R.id.f1listitemTextView1);
                holder.Content = (TextView) convertView.findViewById(R.id.f1listitemTextView2);
                holder.userimg = (ImageView) convertView.findViewById(R.id.a1listitemImageView1);
                holder.size = (TextView) convertView.findViewById(R.id.a1listitemTextView1);
                holder.likes = (TextView) convertView.findViewById(R.id.a1listitemTextView2);
                holder.PL = (TextView) convertView.findViewById(R.id.a1listitemTextView3);
                holder.username = (TextView) convertView.findViewById(R.id.a1listitemTextView4);
                holder.id = (TextView) convertView.findViewById(R.id.a1listitemTextView5);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
			else
			{
                holder = (ViewHolder) convertView.getTag();
            }
			final Post pes=list.get(position);

            if (pes.gettpurl() == null)
			{
                holder.ImageURL.setVisibility(View.GONE);
            }
			else
			{
                holder.ImageURL.setVisibility(View.VISIBLE);
                Glide.with(scactivity.this).load(pes.gettpurl()).transform(new yu(scactivity.this, 16)).into(holder.ImageURL);   
            }
            if (pes.getnm() == true)
			{
				Glide.with(scactivity.this).load(R.drawable.ninv).transform(new GlideCircleTransform(scactivity.this)).into(holder.userimg);
				holder.username.setText("匿名用户");
			}
			else if (pes.getnm() == false)
			{
				Glide.with(scactivity.this).load(pes.getAuthor().getimageurl()).transform(new GlideCircleTransform(scactivity.this)).into(holder.userimg);
				holder.username.setText(pes.getAuthor().getName());
			}
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.Title.setText(pes.getbt());
            holder.Content.setText(pes.getnr());
            String sl1=Integer.toString(pes.getdzSize());
            holder.size.setText(sl1);
            String sl2=Integer.toString(pes.getscSize());
            holder.likes.setText(sl2);
            String sl3=Integer.toString(pes.getPlsize());
            holder.PL.setText(sl3);
            holder.id.setText(pes.getObjectId());
			return convertView;
        }

        class ViewHolder
		{
            public ImageView ImageURL,userimg;
            public TextView Title;
            public TextView Content;
            public TextView size,likes,PL,username,id;
        }
	}

	@Override
	public void onRefresh()
	{
		// TODO: Implement this method
		screfresh.setRefreshing(true);
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
                            scbmob();              
                            screfresh.setRefreshing(false);
                        }
                    });
            }
		}.start();
	}
}