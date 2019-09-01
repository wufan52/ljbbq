package com.ljbbq.jin.bmob;
import cn.bmob.v3.*;

public class bmobsong extends BmobObject
{
	private String gqname,gqtp,gqzz,gqurl,gqid;
	private User author;
	
	public void setgqname(String gqname)
    {
        this.gqname = gqname;
    }
    public String getgqname()
    {
        return gqname;
	}
	
	public void setgqzz(String gqzz)
    {
        this.gqzz = gqzz;
    }
    public String getgqzz()
    {
        return gqzz;
	}

	public void setgqtp(String gqtp)
    {
        this.gqtp = gqtp;
    }
    public String gettp()
    {
        return gqtp;
	}
	
	public void setgqurl(String gqurl)
    {
        this.gqurl = gqurl;
    }
    public String getgqurl()
    {
        return gqurl;
	}
	
	public void setAuthor(User author)
    {
        this.author = author;
    }
    public User getAuthor()
    {
        return author;
    }
	
	public void setgqid(String gqid)
    {
        this.gqid = gqid;
    }
    public String getgqid()
    {
        return gqid;
	}
}
