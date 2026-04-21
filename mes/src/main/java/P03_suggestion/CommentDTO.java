package P03_suggestion;

import java.sql.Timestamp;

public class CommentDTO {

    private String    comno;
    private String    content;
    private Timestamp ctime;
    private String    ctimeStr;  // TO_CHAR 포맷 문자열 (화면 출력용)
    private String    boardno;
    private String    parentComno;
    private int       depth;
    private String    writer;    // 작성자 이름 (로그인한 사람의 ename)

    public String    getComno()                          { return comno; }
    public void      setComno(String comno)              { this.comno = comno; }
    public String    getContent()                        { return content; }
    public void      setContent(String content)          { this.content = content; }
    public Timestamp getCtime()                          { return ctime; }
    public void      setCtime(Timestamp ctime)           { this.ctime = ctime; }
    public String    getCtimeStr()                       { return ctimeStr; }
    public void      setCtimeStr(String ctimeStr)        { this.ctimeStr = ctimeStr; }
    public String    getBoardno()                        { return boardno; }
    public void      setBoardno(String boardno)          { this.boardno = boardno; }
    public String    getParentComno()                    { return parentComno; }
    public void      setParentComno(String parentComno)  { this.parentComno = parentComno; }
    public int       getDepth()                          { return depth; }
    public void      setDepth(int depth)                 { this.depth = depth; }
    public String    getWriter()                         { return writer; }
    public void      setWriter(String writer)            { this.writer = writer; }
}