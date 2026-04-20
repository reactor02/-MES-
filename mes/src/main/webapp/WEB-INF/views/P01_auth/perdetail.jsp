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
<link rel="stylesheet" href="/mes/static/css/P01_auth/perdetail.css">
</head>

<body>


	<%@ include file="/WEB-INF/views/P00_layout/header.jsp"%>

	<div class="layout_snb">
		<div class="snbContent">
			<%@ include file="/WEB-INF/views/P00_layout/snb.jsp"%>
		</div>
		<div class="content">



			
			<div class="model-body">
			
			     <div class="model-high">
                       <h2 class="h2">사용자 관리 상세</h2>
	             </div>
	             
	             <div>
	             <c:if test="${ not empty messege }">
	             <strong>${ messege }</strong>
	             </c:if>
	             </div>
			<div class="form-parent">
			
			
			<form method="post" action="/mes/pdetail">
					<div class="center">
					<c:if test="${ not empty error }">
					${ not empty error }
					</c:if>
					<c:if test="${ not empty error1 }">
					${ not empty error1 }
					</c:if>
					<input value="${ d.empid }" name="change-auth" style="display : none;">
				           <div class="pd-box">
							사번 : 
							<li class="input-1 radius">
							${ d.empid }
							</li>
				           </div>
				           <br>
						    <div class="pd-box">
						    부서 : <select name="dept" class="input-1 radius">
							<c:forEach var="s" items="${ selectd }">
							<option  value="${s.deptname}" ${ s.deptname == d.deptname ? 'selected' : '' }>${ s.deptname }</option>
							</c:forEach>
							</select>
				           </div>
							<br>
				           <div class="pd-box">
						    이름 : 
						    <li class="input-1 radius">
							${ d.ename }
							</li>
				           </div>
				           <br>
				           <div class="pd-box">
						    권한 : <select name="permission" class="input-1 radius">
							<c:forEach var="a" items="${ selecta }">
							<option  value="${a.auth}" ${ a.auth == d.auth ? 'selected' : '' }>${ a.auth == 1 ? '작업자' : a.auth == 2 ? '관리자' : a.auth == 3 ? '슈퍼바이저' : '권한박탈(퇴사)' }</option>
							</c:forEach>
							</select>
				           </div>
							<br>
						<button type="submit" class="buttonMain" name="pd_btn">권한등록</button>
						<br>
				        <a class="pd-box1" href="/mes/permission">목록으로</a>
						
					</div>
				</form>
				
				<br>
				<br>
				
			</div>
			
			
			</div>
			
			<script>
	            const close_btn = document.querySelector(".close-btn");
	            close_btn.addEventListener('click', function (evt) {
			    window.location.href = "/mes/permission"
	             })
	        </script>



		</div>
	</div>





</body>

</html>
