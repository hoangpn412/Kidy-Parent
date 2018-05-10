package vn.com.kidy.data.model.home;

import java.util.ArrayList;

import vn.com.kidy.data.model.news.NewsList;

/**
 * Created by admin on 1/22/18.
 */

public class News
{
    private String title;

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    private ArrayList<NewsList> data;

    public ArrayList<NewsList> getData() { return this.data; }

    public void setData(ArrayList<NewsList> data) { this.data = data; }
}

