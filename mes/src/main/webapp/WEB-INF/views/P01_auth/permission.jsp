<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html lang="kr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

    <link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
    <script src="/mes/static/js/00_layout/header.js"></script>

    <link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
    <script src="/mes/static/js/00_layout/snb.js"></script>
    
    <link rel="stylesheet" href="/mes/static/css/P01_auth/permission.css">
</head>

<body>

<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
    
    <div class="layout_snb">
        <div class="snbContent">
            <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
        </div>
        <div class="content">
            

<div class="snb-bro">
<div class="board-box">
				<div class="box-type4 radius">
					<div class="mp-title">
						<div class="model-close">
                            <h2>사용자 관리</h2>
		                    
	                    </div>
						<span>사용자 관리 및 수정</span>
					</div>
					<form method="get" action="permission">
					
					<c:forEach var="l" items="${ list }">
						<div class="per-tool">
						<a href="pdetail?empid=${ l.empid }">
						<div class="per-chain radius">
							<li>사번 : ${ l.empid }</li> 
							<li>이름 : ${ l.ename }</li> 
							<li>부서 : ${ l.deptname }</li> 							
							<li>권한레벨 : ${ l.auth }</li> 							
						</div>						
						</a>
						</div>
					</c:forEach>
					
					<div class="next">
					<c:forEach var="m" begin="1" end="${ page_no }">
						<button class="per-btn" name="per_btn" value="${ m }" class="buttonMain">${ m }</button>
					</c:forEach>
					</div>
					
					</form>
				</div>
			</div>
			</div>

			
        </div>
    </div>




</body>

</html>