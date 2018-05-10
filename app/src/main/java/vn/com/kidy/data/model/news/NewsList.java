package vn.com.kidy.data.model.news;

/**
 * Created by admin on 1/25/18.
 */

public class NewsList {
    private String newsSummary;

    private String newsId;

    private String status;

    private String newsContent;

    private String schoolId;

    private String createdDate;

    private String newsTitle;

    private String newsPresentImage;

    public String getNewsSummary ()
    {
        return newsSummary;
    }

    public void setNewsSummary (String newsSummary)
    {
        this.newsSummary = newsSummary;
    }

    public String getNewsId ()
    {
        return newsId;
    }

    public void setNewsId (String newsId)
    {
        this.newsId = newsId;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getNewsContent ()
    {
        return newsContent;
    }

    public void setNewsContent (String newsContent)
    {
        this.newsContent = newsContent;
    }

    public String getSchoolId ()
    {
        return schoolId;
    }

    public void setSchoolId (String schoolId)
    {
        this.schoolId = schoolId;
    }

    public String getCreatedDate ()
    {
        return createdDate;
    }

    public void setCreatedDate (String createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getNewsTitle ()
    {
        return newsTitle;
    }

    public void setNewsTitle (String newsTitle)
    {
        this.newsTitle = newsTitle;
    }

    public String getNewsPresentImage ()
    {
        return newsPresentImage;
    }

    public void setNewsPresentImage (String newsPresentImage)
    {
        this.newsPresentImage = newsPresentImage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [newsSummary = "+newsSummary+", newsId = "+newsId+", status = "+status+", newsContent = "+newsContent+", schoolId = "+schoolId+", createdDate = "+createdDate+", newsTitle = "+newsTitle+", newsPresentImage = "+newsPresentImage+"]";
    }

}
