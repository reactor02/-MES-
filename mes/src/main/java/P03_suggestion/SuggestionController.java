package P03_suggestion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import P01_auth.LoginDTO;

@WebServlet("/suggestion/*")
public class SuggestionController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_PATH =
        "C:\\workspace_proj2\\mes\\src\\main\\webapp\\static\\upload\\suggestion";

    SuggestionService suggestionService = new SuggestionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 세션에서 로그인 정보 읽기
        LoginDTO loginDto = (LoginDTO) request.getSession().getAttribute("dto");
        String loginId = loginDto.getEmpid();
        int    auth    = loginDto.getAuth();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/list";

        switch (pathInfo) {

            case "/list": {
                int size = 10;
                int page = 1;
                try { size = Integer.parseInt(request.getParameter("size")); } catch (Exception e) {}
                try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception e) {}

                String searchKeyword = request.getParameter("searchKeyword");
                if (searchKeyword != null) searchKeyword = searchKeyword.trim();

                SuggestionDTO dto = new SuggestionDTO();
                dto.setSize(size);
                dto.setPage(page);
                dto.setSearchKeyword(searchKeyword);

                Map<String, Object> map = suggestionService.getList(dto);
                map.put("size",          size);
                map.put("page",          page);
                map.put("searchKeyword", searchKeyword == null ? "" : searchKeyword);

                int totalCount    = (int) map.get("totalCount");
                int totalPages    = (int) Math.ceil((double) totalCount / size);
                if (totalPages < 1) totalPages = 1;

                int pageGroupSize  = 5;
                int currentGroup   = (int) Math.ceil((double) page / pageGroupSize);
                int groupStartPage = (currentGroup - 1) * pageGroupSize + 1;
                int groupEndPage   = Math.min(groupStartPage + pageGroupSize - 1, totalPages);

                map.put("totalPages",     totalPages);
                map.put("groupStartPage", groupStartPage);
                map.put("groupEndPage",   groupEndPage);

                request.setAttribute("map", map);
                request.getRequestDispatcher(
                    "/WEB-INF/views/P03_suggestion/list.jsp"
                ).forward(request, response);
                break;
            }

            case "/detail": {
                String boardno = request.getParameter("boardno");

                SuggestionDTO detail = suggestionService.getDetail(boardno);

                if (detail == null) {
                    response.sendRedirect(request.getContextPath() + "/suggestion/list");
                    return;
                }

                // 상세 접근 제한: 작성자 본인 또는 auth >= 2만 접근 가능
                boolean isOwner = loginId.equals(detail.getEmpId());
                if (!isOwner && auth < 2) {
                    response.setContentType("text/html;charset=UTF-8");
                    response.getWriter().write("<script>alert('조회 권한이 없습니다.'); location.href='" + request.getContextPath() + "/suggestion/list';</script>");
                    return;
                }
                // 댓글 입력 가능 여부: 작성자 본인 OR auth >= 2
                boolean canComment = isOwner || auth >= 2;

                // JS 10분 체크용 ctime epoch ms:
                // JDBC Timestamp.getTime()은 타임존 해석 이슈로 부정확하므로
                // DAO가 Oracle에서 직접 KST로 보정해 계산한 ctimeKstMs를 사용한다.
                long ctimeMs = detail.getCtimeKstMs();

                List<CommentDTO> commentList = suggestionService.getCommentList(boardno);

                request.setAttribute("detail",      detail);
                request.setAttribute("commentList", commentList);
                request.setAttribute("isOwner",     isOwner);   // 작성자 여부 → 삭제 버튼 표시
                request.setAttribute("ctimeMs",     ctimeMs);   // ctime 밀리초 → JS 10분 체크
                request.setAttribute("canComment",  canComment);
                request.setAttribute("page",        request.getParameter("page"));
                request.setAttribute("size",        request.getParameter("size"));

                request.getRequestDispatcher(
                    "/WEB-INF/views/P03_suggestion/detail.jsp"
                ).forward(request, response);
                break;
            }

            case "/register":
                request.getRequestDispatcher(
                    "/WEB-INF/views/P03_suggestion/register.jsp"
                ).forward(request, response);
                break;

            case "/download": {
                String saveName   = request.getParameter("save");
                String originName = request.getParameter("origin");

                if (saveName == null || saveName.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/suggestion/list");
                    return;
                }

                File file = new File(UPLOAD_PATH + "\\" + saveName);

                if (!file.exists()) {
                    response.setContentType("text/html;charset=UTF-8");
                    response.getWriter().write("<script>alert('파일을 찾을 수 없습니다.'); history.back();</script>");
                    return;
                }

                response.setHeader("Cache-Control", "no-cache");
                String encodedName = new String(originName.getBytes("UTF-8"), "ISO-8859-1");
                response.addHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\"");
                response.setContentLengthLong(file.length());

                byte[] buf = new byte[1024 * 8];
                try (InputStream is = new FileInputStream(file);
                     OutputStream os = response.getOutputStream()) {
                    int count;
                    while ((count = is.read(buf)) != -1) {
                        os.write(buf, 0, count);
                    }
                    os.flush();
                }
                break;
            }

            default:
                response.sendRedirect(request.getContextPath() + "/suggestion/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        LoginDTO loginDto = (LoginDTO) request.getSession().getAttribute("dto");
        String loginId = loginDto.getEmpid();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "";

        switch (pathInfo) {

            case "/insert": {
                SuggestionDTO insertDto = new SuggestionDTO();
                insertDto.setEmpId(loginId);

                try {
                    File uploadDir = new File(UPLOAD_PATH);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    DiskFileItemFactory factory = new DiskFileItemFactory();
                    factory.setRepository(uploadDir);
                    factory.setSizeThreshold(1024 * 1024);

                    ServletFileUpload upload = new ServletFileUpload(factory);
                    upload.setFileSizeMax(1024 * 1024 * 10);

                    List<FileItem> items = upload.parseRequest(request);

                    for (FileItem fileItem : items) {
                        if (fileItem.isFormField()) {
                            String fieldName = fileItem.getFieldName();
                            String value     = fileItem.getString("UTF-8");
                            if ("title".equals(fieldName))   insertDto.setTitle(value);
                            if ("content".equals(fieldName)) insertDto.setContent(value);
                        } else {
                            if (fileItem.getSize() > 0) {
                                String originName = fileItem.getName();
                                String saveName   = System.currentTimeMillis() + "_" + originName;
                                fileItem.write(new File(uploadDir + "\\" + saveName));
                                insertDto.setOriginName(originName);
                                insertDto.setSaveName(saveName);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                suggestionService.insert(insertDto);
                response.sendRedirect(request.getContextPath() + "/suggestion/list?page=1");
                break;
            }

            case "/detail": {
                String action        = request.getParameter("action");
                String detailBoardno = request.getParameter("boardno");

                if ("complete".equals(action)) {
                    suggestionService.updateComplete(detailBoardno);
                }
                response.sendRedirect(request.getContextPath() + "/suggestion/detail?boardno=" + detailBoardno);
                break;
            }

            case "/comment": {
                String commentBoardno = request.getParameter("boardno");
                String commentContent = request.getParameter("commentContent");
                String parentComno    = request.getParameter("parentComno");

                if (parentComno != null && parentComno.trim().isEmpty()) {
                    parentComno = null;
                }

                suggestionService.insertComment(commentBoardno, commentContent, parentComno);

                // 작성자 본인 외의 사람이 댓글 달면 complete 0 → 2(답변달림) 자동 변경
                SuggestionDTO commentTarget = suggestionService.getDetail(commentBoardno);
                if (commentTarget != null && !loginId.equals(commentTarget.getEmpId())) {
                    suggestionService.updateCompleteToAnswered(commentBoardno);
                }

                response.sendRedirect(request.getContextPath() + "/suggestion/detail?boardno=" + commentBoardno);
                break;
            }

            case "/delete": {
                String deleteBoardno = request.getParameter("boardno");

                // 삭제 권한 체크: 작성자 본인인지만 확인 (10분 체크는 JS에서 처리)
                SuggestionDTO deleteTarget = suggestionService.selectOne(deleteBoardno);
                boolean isOwner = deleteTarget != null && loginId.equals(deleteTarget.getEmpId());

                if (!isOwner) {
                    response.sendRedirect(request.getContextPath() + "/suggestion/detail?boardno=" + deleteBoardno);
                    return;
                }

                suggestionService.delete(deleteBoardno);
                response.sendRedirect(request.getContextPath() + "/suggestion/list?page=1");
                break;
            }

            default:
                response.sendRedirect(request.getContextPath() + "/suggestion/list");
        }
    }
}