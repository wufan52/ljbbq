package com.ljbbq.jin.bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.datatype.BmobFile;

public class Post extends BmobObject
{
    private String username;
    private String bt;
    private String nr;
    private String tpurl;
    private BmobFile icon;
    private String tx;
    private int scsize;
	private BmobRelation Fabulous;
    private User author;
    private BmobRelation likes;
    private int dzsize;
	private int plsize;
	private int fxsize;
	private String sggq,sgtp,sgurl,sgzz;
	private boolean nm;
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    public String getUsername()
    {
        return username;
	}
    
    public void setbt(String bt)
    {
        this.bt = bt;
    }
    public String getbt()
    {
        return bt;
	}
    
    public void setnr(String nr)
    {
        this.nr = nr;
    }
    public String getnr()
    {
        return nr;
	}
    
    public void settpurl(String tpurl)
    {
        this.tpurl = tpurl;
    }
    public String gettpurl()
    {
        return tpurl;
	}
    
    public BmobFile getIcon() {
        return icon;
    }
    public void setIcon(BmobFile icon) {
        this.icon = icon;
	}
    
    public void settx(String tx)
    {
        this.tx = tx;
    }
    public String gettx()
    {
        return tx;
	}
    
    public void setscSize(int scsize)
    {
        this.scsize = scsize;
    }
    public int getscSize()
    {
        return scsize;
	}
    
	public void setdzSize(int dzsize)
    {
        this.dzsize = dzsize;
    }
    public int getdzSize()
    {
        return dzsize;
	}
	
    public void setfxsize(int fxsize)
    {
        this.fxsize = fxsize;
    }
    public int getfxsize()
    {
        return fxsize;
    }

    public void setPlsize(int plsize)
    {
        this.plsize = plsize;
    }
    public int getPlsize()
    {
        return plsize;
	}
    
    public void setAuthor(User author)
    {
        this.author = author;
    }
    public User getAuthor()
    {
        return author;
    }
	
	public void setFabulous(BmobRelation fabulous)
	{
		Fabulous = fabulous;
	}
	public BmobRelation getFabulous()
	{
		return Fabulous;
	}

    public void setLikes(BmobRelation likes)
    {
        this.likes = likes;
    }
    public BmobRelation getLikes()
    {
        return likes;
	}
	
	public void setsggq(String sggq)
    {
        this.sggq = sggq;
    }
    public String getsggq()
    {
        return sggq;
	}
	
	public void setsgtp(String sgtp)
    {
        this.sgtp = sgtp;
    }
    public String getsgtp()
    {
        return sgtp;
	}
	
	public void setsgurl(String sgurl)
    {
        this.sgurl = sgurl;
    }
    public String getsgurl()
    {
        return sgurl;
	}
	
	public void setsgzz(String sgzz)
    {
        this.sgzz = sgzz;
    }
    public String getsgzz()
    {
        return sgzz;
	}
	
	public void setnm(boolean nm)
    {
        this.nm = nm;
    }
    public boolean getnm()
    {
        return nm;
	}
}
