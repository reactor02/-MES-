window.addEventListener("load", function(){
    bind();
});

function bind(){

    // ──────────────────────────────────────────
    // suggestion_register: Quill 에디터
    // ──────────────────────────────────────────
    var editorEl = document.getElementById('suggestRegContent');
    if (editorEl) {
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
                    [{ 'align': [] }]
                ]
            }
        });

        document.getElementById('suggestSubmitBtn').addEventListener('click', function(e) {
            e.preventDefault();

            if (quill.getText().trim().length === 0) {
                alert('내용을 입력하세요.');
                return;
            }

            editorEl.value = quill.root.innerHTML;
            document.getElementById('suggestRegisterForm').submit();
        });
    }

    // ──────────────────────────────────────────
    // suggestion_detail: 삭제
    // ──────────────────────────────────────────
    const btnDelete = document.querySelector('#btnDelete');
    if (btnDelete) {
        btnDelete.addEventListener('click', function () {
            // 10분 체크: ctime 밀리초 기준
            const ctimeMs = parseInt(btnDelete.dataset.ctimems);
            const diffMin = (Date.now() - ctimeMs) / 1000 / 60 - 540;
            if (diffMin >= 10) {
                alert('10분이 지나 삭제가 불가능합니다.');
                return;
            }
            if (!confirm('정말 삭제하시겠습니까?')) return;
            const form = document.createElement('form');
            form.method = 'post';
            form.action = btnDelete.dataset.action;
            const input = document.createElement('input');
            input.type  = 'hidden';
            input.name  = 'boardno';
            input.value = btnDelete.dataset.boardno;
            form.appendChild(input);
            document.body.appendChild(form);
            form.submit();
        });
    }

    // ──────────────────────────────────────────
    // suggestion_detail: 답변완료
    // ──────────────────────────────────────────
    const btnComplete = document.querySelector('#btnComplete');
    if (btnComplete) {
        btnComplete.addEventListener('click', function () {
            // complete == 0(검토중) 이면 먼저 답변을 달아달라고 경고
            if (btnComplete.dataset.complete === '0') {
                alert('답변을 먼저 달아주세요.');
                return;
            }
            if (!confirm('답변완료로 처리하시겠습니까?')) return;
            const form = document.createElement('form');
            form.method = 'post';
            form.action = btnComplete.dataset.action;
            const inputBoardno = document.createElement('input');
            inputBoardno.type  = 'hidden';
            inputBoardno.name  = 'boardno';
            inputBoardno.value = btnComplete.dataset.boardno;
            const inputAction = document.createElement('input');
            inputAction.type  = 'hidden';
            inputAction.name  = 'action';
            inputAction.value = 'complete';
            form.appendChild(inputBoardno);
            form.appendChild(inputAction);
            document.body.appendChild(form);
            form.submit();
        });
    }

    // ──────────────────────────────────────────
    // suggestion_detail: 무한 대댓글 - 답글 버튼 클릭
    // ──────────────────────────────────────────
    document.addEventListener('click', function (e) {
        if (!e.target.classList.contains('btn-reply-small')) return;
        const comno = e.target.dataset.comno;
        const depth = parseInt(e.target.dataset.depth);
        const parentComnoEl = document.getElementById('parentComno');
        if (!parentComnoEl) return;
        parentComnoEl.value = comno;
        const form = document.getElementById('commentForm');
        form.style.paddingLeft = (depth + 1) * 30 + 'px';
        const targetItem = e.target.closest('.comment-item');
        targetItem.after(form);
        document.getElementById('commentInput').placeholder = '답글을 입력하세요';
        document.getElementById('commentInput').focus();
    });
}