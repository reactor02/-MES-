package P03_suggestion;

import java.sql.Timestamp;
import java.util.List;

public class SuggestionDTO {

    private String    boardno;
    private String    title;
    private String    content;
    private Timestamp ctime;
    private Timestamp mtime;
    private String    ctimeStr;  // TO_CHAR 포맷 문자열 (화면 출력용)
    private String    mtimeStr;  // TO_CHAR 포맷 문자열 (화면 출력용)
    private int       views;
    private String    empId;
    private int       complete;

    private String originName;
    private String saveName;
    private String ename;
    private String commentContent;
    private List<CommentDTO> commentList;

    private int page;
    private int size;
    private int start;
    private int end;

    // 검색어
    private String searchKeyword;

    // 한국시간 기준 epoch ms (JS 10분 체크용 - JDBC Timestamp의 타임존 해석 이슈 우회)
    private long ctimeKstMs;

    public String    getBoardno()                        { return boardno; }
    public void      setBoardno(String boardno)          { this.boardno = boardno; }
    public String    getTitle()                          { return title; }
    public void      setTitle(String title)              { this.title = title; }
    public String    getContent()                        { return content; }
    public void      setContent(String content)          { this.content = content; }
    public Timestamp getCtime()                          { return ctime; }
    public void      setCtime(Timestamp ctime)           { this.ctime = ctime; }
    public Timestamp getMtime()                          { return mtime; }
    public void      setMtime(Timestamp mtime)           { this.mtime = mtime; }
    public String    getCtimeStr()                       { return ctimeStr; }
    public void      setCtimeStr(String ctimeStr)        { this.ctimeStr = ctimeStr; }
    public String    getMtimeStr()                       { return mtimeStr; }
    public void      setMtimeStr(String mtimeStr)        { this.mtimeStr = mtimeStr; }
    public int       getViews()                          { return views; }
    public void      setViews(int views)                 { this.views = views; }
    public String    getEmpId()                          { return empId; }
    public void      setEmpId(String empId)              { this.empId = empId; }
    public int       getComplete()                       { return complete; }
    public void      setComplete(int complete)           { this.complete = complete; }
    public String    getOriginName()                     { return originName; }
    public void      setOriginName(String originName)    { this.originName = originName; }
    public String    getSaveName()                       { return saveName; }
    public void      setSaveName(String saveName)        { this.saveName = saveName; }
    public String    getEname()                          { return ename; }
    public void      setEname(String ename)              { this.ename = ename; }
    public String    getCommentContent()                 { return commentContent; }
    public void      setCommentContent(String c)         { this.commentContent = c; }
    public List<CommentDTO> getCommentList()             { return commentList; }
    public void      setCommentList(List<CommentDTO> l)  { this.commentList = l; }
    public int       getPage()                           { return page; }
    public void      setPage(int page)                   { this.page = page; }
    public int       getSize()                           { return size; }
    public void      setSize(int size)                   { this.size = size; }
    public int       getStart()                          { return start; }
    public void      setStart(int start)                 { this.start = start; }
    public int       getEnd()                            { return end; }
    public void      setEnd(int end)                     { this.end = end; }
    public String    getSearchKeyword()                  { return searchKeyword; }
    public void      setSearchKeyword(String s)          { this.searchKeyword = s; }
    public long      getCtimeKstMs()                     { return ctimeKstMs; }
    public void      setCtimeKstMs(long ctimeKstMs)      { this.ctimeKstMs = ctimeKstMs; }
}