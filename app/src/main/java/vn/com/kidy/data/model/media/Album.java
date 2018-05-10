package vn.com.kidy.data.model.media;

import java.util.ArrayList;

/**
 * Created by admin on 3/2/18.
 */

public class Album
{
    private String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    private String title;

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    private String thumbnail;

    public String getThumbnail() { return this.thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    private String createdDate;

    public String getCreatedDate() { return this.createdDate; }

    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}

