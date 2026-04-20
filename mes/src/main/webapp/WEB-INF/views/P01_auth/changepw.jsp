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

<link rel="stylesheet" href="/mes/static/css/P01_auth/changepw.css">
</head>

<body>
	<img class="logo" src="static/images/logo.png" alt="MES 로고">
	<h1>알콜스왑 MES</h1>
	<h7>제조 실행 시스템</h7>
	<div class="model-body">
		<div class="model-high">
			<h2>비밀번호 변경</h2>
			
		</div>
		<form method="post" action="/mes/changepw" id="change-form">
			<div class="center">


				<div class="messege"></div>

				<input type="text" class="input-1 radius" name="change_empid"
					placeholder="사원번호를 입력해주세요."><br> <input type="text"
					class="input-1 radius" name="change_phone"
					placeholder="연락처를 입력해주세요."><br>


				<div style="width: 100%; position: relative; height: 30px;">

					<input type="password" class="input-1 radius" name="change_pw"
						id="pw1" placeholder="새비밀번호 : 8 ~ 16자. 영문, 숫자, 특수문자 모두포함" required
						title="8 ~ 16자. 영문, 숫자, 특수문자를 모두 포함해야 합니다."
						pattern="(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&_])[A-Za-z\d@$!%*#?_&]{8,16}">
					<span id="btn1"
						style="position: absolute; right: 20px; top: 5px; cursor: pointer;">
						👁️ </span>
				</div>
				
				<br>			



				<div class="pw-m up"></div>
				
				




				<div style="width: 100%; position: relative; height: 30px;">
					<input type="password" id="pw2" class="input-1 radius"
						name="change_pw2" placeholder="비밀번호 확인"> <span
						id="btn2"
						style="position: absolute; right: 20px; top: 5px; cursor: pointer;">
						👁️ </span>
				</div>


				<div class="pw2-m up"></div>
				<br>
				
				<button type="submit" class="buttonMain" name="changepw_btn">비밀번호
					변경</button>
				<br>
			</div>
		</form>
		<c:if test=" ${not empty error} ">
			<div style="color: red;">${error}</div>
		</c:if>
		<a href="/mes/login">비밀번호가 기억나셨나요? (로그인)</a>
	</div>

	<script>
	//입력창들
	const changeForm = document.querySelector("#change-form");
	const pw = document.querySelector("[name='change_pw']");
	const pw2 = document.querySelector("[name='change_pw2']");
	
	//메세지 창들
	const pwM = document.querySelector(".pw-m");
	const pw2M = document.querySelector(".pw2-m");
	const messege = document.querySelector(".messege");
	
	
	// pw 입력 때마다 체크
	pw.addEventListener('input', function() {
	    if(!pw.checkValidity()){
	        pwM.style.color = 'red';
	        pwM.innerHTML = `
                비밀번호 형식과 일치하지 않습니다.	        
	        `;
	    } else {
	        pwM.innerHTML = `
                	        
	        `;
	        console.log('pw 통과');
	    }
	});


	// pw2: 비밀번호가 서로 일치하지 않습니다.	
	pw2.addEventListener('blur', function() {
	     const pwValue = pw.value
	     const pw2Value = pw2.value
	     
	     //비번 확인칸이 비어있다면 검사 x
	     if (pw2Value === "") {
	         pw2M.innerHTML = "";
	         return;
	     }
	     
	     //비밀번호가 일치하지 않는다면 경고
	    if( pwValue !== pw2Value ){	    	
	 
	        //pw.reportValidity(); // 여기서 말풍선이 뜹니다.
	        pw2M.style.color = 'red';
	        pw2M.innerHTML = `
               비밀번호가 서로 일치하지 않습니다.	        
	        `;
	        
	        setTimeout(function() {
	        	pw2M.innerHTML = `

	        	`
	        }, 1500);
	        
	    } else {
	    	 pw2M.innerHTML = `
	                	        
		        `;
	    	
	    }
	});

	
	//보내는거 막기
	changeForm.addEventListener('submit', function(evt) {
		const isPwCheckValidity = pw.checkValidity();
		
		const pwValue = pw.value
	     const pw2Value = pw2.value
	     
	     if (changeForm.change_empid.value === "" || changeForm.change_phone.value === "") {
	    	 
	    	// 값이 서버로 전송되는 거 막기
				evt.preventDefault();
	    	
	    	    messege.style.color = 'red';
		        messege.innerHTML = `
	               모든 필수 항목을 입력해주세요.	        
		        `;
		        
		        setTimeout(function() {
		        	messege.innerHTML = `

		        	`
		        }, 1500);
		        
		        //친절하게 입력창으로 포커스 이동
		        changeForm.change_empid.focus();
		        return;
	    	    
	    	}
	
		    if( pwValue !== pw2Value ){
		    	
		    	// 값이 서버로 전송되는 거 막기
				evt.preventDefault();
		    	
		        //pw.reportValidity(); 브라우저 경고메세지.
		        messege.style.color = 'red';
		        messege.innerHTML = `
	               비밀번호가 서로 일치하지 않습니다.	        
		        `;
		        
		        
		        setTimeout(function() {
		        	messege.innerHTML = `

		        	`
		        }, 1500);
		        //친절하게 입력창으로 포커스 이동
		        pw.focus();
		        return;
		    } 
		
		
		if( !isPwCheckValidity ) {
			
			// 값이 서버로 전송되는 거 막기
			evt.preventDefault();
			
			// 2. 사용자 피드백 (알림 또는 스타일 변경)
			 messege.innerHTML = `
	                비밀번호가 형식과 일치하지 않습니다.	        
		        `;
		        setTimeout(function() {
		        	messege.innerHTML = `

		        	`
		        }, 2000);
		        
		        //친절하게 입력창으로 포커스 이동
		        pw.focus();
		        			
		}
	})
	
	
	function setupPasswordToggle(inputSelector, buttonSelector) {
    const btn = document.querySelector(buttonSelector);
    const input = document.querySelector(inputSelector);

    // 요소가 잘 찾아졌는지 확인 후 이벤트 등록
    if(btn && input) {
        btn.addEventListener('click', () => {
            const isPassword = input.type === 'password';
            input.type = isPassword ? 'text' : 'password';
            btn.textContent = isPassword ? '🙈' : '👁️';
        });
    } else {
        console.error("요소를 찾을 수 없습니다:", inputSelector, buttonSelector);
    }
}

// HTML에 작성한 ID와 정확히 일치시켜 호출
setupPasswordToggle('#pw1', '#btn1');
setupPasswordToggle('#pw2', '#btn2');
	</script>






</body>

</html>