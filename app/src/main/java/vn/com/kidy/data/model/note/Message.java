package vn.com.kidy.data.model.note;

import java.util.Date;

/**
 * Created by admin on 4/12/18.
 */

public class Message
{
    private String content;

    public String getContent() { return this.content; }

    public void setContent(String content) { this.content = content; }

    private boolean isFromParent;

    public boolean getIsFromParent() { return this.isFromParent; }

    public void setIsFromParent(boolean isFromParent) { this.isFromParent = isFromParent; }

    private String createdDate;

    public String getCreatedDate() { return this.createdDate; }

    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
