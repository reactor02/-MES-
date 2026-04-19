window.addEventListener("load", function(){
	bind();
});

function bind(){

	// ──────────────────────────────────────────
	// notice_detail: 삭제
	// ──────────────────────────────────────────
	function submitDelete() {
		if (confirm('삭제하시겠습니까?')) {
			document.getElementById('deleteForm').submit();
		}
	}
	window.submitDelete = submitDelete;

	// ──────────────────────────────────────────
	// notice_detail: URL 복사
	// ──────────────────────────────────────────
	var btnCopyUrl = document.getElementById('btnCopyUrl');
	if (btnCopyUrl) {
		btnCopyUrl.addEventListener('click', function() {
			var btn = this;
			navigator.clipboard.writeText(window.location.href).then(function() {
				btn.textContent = '✓ 복사됨';
				setTimeout(function() { btn.textContent = '🔗 URL 복사'; }, 2000);
			});
		});
	}
	function searchNotice() {
		document.getElementById("searchForm").submit();
	}

	// ──────────────────────────────────────────
	// notice_detail
	// ──────────────────────────────────────────
	function submitDelete() {
		if (confirm("삭제하시겠습니까?")) {
			document.getElementById("deleteForm").submit();
		}
	}

	// ──────────────────────────────────────────
	// Quill 에디터 공통 초기화 함수
	// ──────────────────────────────────────────
	function initQuill(textareaId, submitBtnId, formId) {
		var editorEl = document.getElementById(textareaId);
		if (!editorEl) return;

		// textarea 숨기고 Quill 컨테이너 삽입
		editorEl.style.display = 'none';

		var quillDiv = document.createElement('div');
		quillDiv.style.height = '250px';
		editorEl.parentNode.insertBefore(quillDiv, editorEl);

		var quill = new Quill(quillDiv, {
			theme: 'snow',
			placeholder: '내용을 입력하세요.',
			modules: {
				toolbar: [
					['bold', 'italic', 'underline'],
					[{ 'color': [] }, { 'background': [] }],
					[{ 'align': [] }],
					['clean']
				]
			}
		});

		// 수정 페이지: 기존 내용 로드
		if (editorEl.value && editorEl.value.trim().length > 0) {
			quill.root.innerHTML = editorEl.value;
		}

		document.getElementById(submitBtnId).addEventListener('click', function(e) {
			e.preventDefault();

			if (quill.getText().trim().length === 0) {
				alert('내용을 입력하세요.');
				return;
			}

			editorEl.value = quill.root.innerHTML;
			document.getElementById(formId).submit();
		});
	}

	// 등록 페이지
	initQuill('noticeRegContent',  'noticeSubmitBtn',     'noticeRegisterForm');
	// 수정 페이지
	initQuill('noticeEditContent', 'noticeEditSubmitBtn', 'noticeEditForm');
}