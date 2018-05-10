package vn.com.kidy.data.model.note;

/**
 * Created by admin on 4/1/18.
 */

public class NoteContent {
    final String content;
    final boolean isFromTeacher;

    public NoteContent(String content, boolean isFromTeacher) {
        this.content = content;
        this.isFromTeacher = isFromTeacher;
    }
}
