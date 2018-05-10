package vn.com.kidy.data.model.news;

import java.util.ArrayList;

/**
 * Created by admin on 1/25/18.
 */

public class News {

    private ArrayList<NewsList> newsList;

    private String pageNumber;

    private String schoolId;

    public ArrayList<NewsList> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<NewsList> newsList) {
        this.newsList = newsList;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        return "ClassPojo [newsList = " + newsList + ", pageNumber = " + pageNumber + ", schoolId = " + schoolId + "]";
    }

}
