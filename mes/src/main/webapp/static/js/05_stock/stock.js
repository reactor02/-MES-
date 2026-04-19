document.addEventListener('DOMContentLoaded', function () {

    // ── 요소 참조 ────────────────────────────────────────────
    const inSelectWrap    = document.getElementById('in_select_wrap');
    const itemWrap        = document.getElementById('item_wrap');
    const itemSelect      = document.getElementById('item_id');
    const allItemOptions  = Array.from(itemSelect.options).slice(1);

    const lotSearchModal  = document.getElementById('lotSearchModal');
    const lotSearchBody   = document.getElementById('lotSearchBody');
    const lotKeywordInput = document.getElementById('lotKeyword');

    const empSearchModal  = document.getElementById('empSearchModal');
    const empSearchBody   = document.getElementById('empSearchBody');
    const empKeywordInput = document.getElementById('empKeyword');

    const today = new Date().toISOString().split('T')[0];

    // ▶ 입출고일 최대값 오늘로 고정 (내일 이상 선택 불가)
    document.getElementById('io_time').setAttribute('max', today);


    // ── 대분류 변경 → 소분류 필터링 (모달 내부) ─────────────
    document.getElementById('g_id').addEventListener('change', function () {
        const selectedGid = this.value;
        itemSelect.innerHTML = '<option value="">소분류 선택</option>';
        document.getElementById('spec').value = '';
        document.getElementById('unit').value = '';

        if (!selectedGid) return;

        allItemOptions.forEach(function (opt) {
            if (opt.dataset.gid === selectedGid) {
                itemSelect.appendChild(opt.cloneNode(true));
            }
        });
    });


    // ── 소분류 선택 → 규격/단위 자동입력 ────────────────────
    itemSelect.addEventListener('change', function () {
        const opt = this.options[this.selectedIndex];
        document.getElementById('spec').value = opt.dataset.spec || '';
        document.getElementById('unit').value = opt.dataset.unit || '';
    });


    // ── 입고 등록 버튼 ───────────────────────────────────────
    document.querySelector('.btn-register-in').addEventListener('click', function () {
        const inReasonOptions = `
            <option value="">사유 선택</option>
            <option value="구매입">구매입</option>
            <option value="생산입">생산입</option>
            <option value="QC통과">QC통과</option>
            <option value="작업 후 잔여">작업 후 잔여</option>
        `;

        document.getElementById('modalTitle').textContent       = '입고 등록';
        document.getElementById('item_id_hidden').disabled      = true;
        document.getElementById('lot_id_hidden').disabled       = true;
        document.getElementById('io_reason_label').textContent  = '입고사유';
        document.getElementById('io_reason').innerHTML          = inReasonOptions;
        document.getElementById('io_time').value                = today;
        document.getElementById('io_type').value                = '0';

        // ▶ "입출고일" 라벨 → "입고일"
        document.getElementById('io_time_label').textContent = '입고일';

        inSelectWrap.style.display = 'none';
        itemWrap.style.display     = 'block';

        // 수량: 입고는 직접 입력
        const lotQtyInput       = document.getElementById('lot_qty');
        lotQtyInput.readOnly    = false;
        lotQtyInput.placeholder = '수량 입력';
        lotQtyInput.removeAttribute('style');
        lotQtyInput.value       = '';

        // 유통기한: 입고는 직접 입력 가능
        const expiryInput = document.getElementById('expiry_date');
        expiryInput.readOnly = false;
        expiryInput.removeAttribute('style');
        expiryInput.value = '';

        // 작업자: 세션값 자동입력
        document.getElementById('empName').value       = SESSION_ENAME;
        document.getElementById('emp_id_hidden').value = SESSION_EMP_ID;

        clearItemFields();
        document.getElementById('myModal').showModal();
    });


    // ── 출고 등록 버튼 ───────────────────────────────────────
    document.querySelector('.btn-register-out').addEventListener('click', function () {
        const outReasonOptions = `
            <option value="">사유 선택</option>
            <option value="폐기">폐기</option>
            <option value="자재출">자재출</option>
            <option value="매출출">매출출</option>
            <option value="QC검사">QC검사</option>
        `;

        document.getElementById('modalTitle').textContent       = '출고 등록';
        document.getElementById('item_id_hidden').disabled      = false;
        document.getElementById('lot_id_hidden').disabled       = false;
        document.getElementById('io_reason_label').textContent  = '출고사유';
        document.getElementById('io_reason').innerHTML          = outReasonOptions;
        document.getElementById('io_time').value                = today;
        document.getElementById('io_type').value                = '1';

        // ▶ "입출고일" 라벨 → "출고일"
        document.getElementById('io_time_label').textContent = '출고일';

        inSelectWrap.style.display = 'block';
        itemWrap.style.display     = 'none';

        // 유통기한: 출고는 LOT 선택 시 자동입력 (직접 입력 불가)
        const expiryInput            = document.getElementById('expiry_date');
        expiryInput.readOnly         = true;
        expiryInput.style.background = '#f0f2f7';
        expiryInput.style.color      = '#999';
        expiryInput.style.cursor     = 'not-allowed';
        expiryInput.value            = '';

        // 수량: LOT 선택 시 자동입력
        const lotQtyInput            = document.getElementById('lot_qty');
        lotQtyInput.readOnly         = true;
        lotQtyInput.style.background = '#f0f2f7';
        lotQtyInput.style.color      = '#999';
        lotQtyInput.style.cursor     = 'not-allowed';
        lotQtyInput.placeholder      = '자동 입력';
        lotQtyInput.value            = '';

        // 작업자: 세션값 자동입력
        document.getElementById('empName').value       = SESSION_ENAME;
        document.getElementById('emp_id_hidden').value = SESSION_EMP_ID;

        clearItemFields();
        document.getElementById('myModal').showModal();
    });


    // ── 모달 닫기 ────────────────────────────────────────────
    document.getElementById('btnCancel').addEventListener('click', function () {
        document.getElementById('myModal').close();
    });


    // ── 자재 필드 초기화 ─────────────────────────────────────
    function clearItemFields() {
        document.getElementById('item_id_hidden').value    = '';
        document.getElementById('lot_id_hidden').value     = '';
        document.getElementById('spec').value              = '';
        document.getElementById('unit').value              = '';
        document.getElementById('item_name_display').value = '';
    }


    // ── 페이지당 건수 변경 → 목록 재조회 ────────────────────
    document.getElementById('size').addEventListener('change', function () {
        location.href = '/mes/io?page=1&size=' + this.value;
    });


    // ── 입출고 모달 유효성 검사 (submit 시) ─────────────────
    document.querySelector('form').addEventListener('submit', function (e) {
        const ioType   = document.getElementById('io_type').value;
        const ioTime   = document.getElementById('io_time').value;
        const ioReason = document.getElementById('io_reason').value;

        if (ioType === '0') {
            // ── 입고 유효성 검사 ──
            const venderId   = document.querySelector('select[name="vender_id"]').value;
            const gId        = document.getElementById('g_id').value;
            const itemId     = document.getElementById('item_id').value;
            const lotQty     = document.getElementById('lot_qty').value;
            const expiryDate = document.getElementById('expiry_date').value;

            if (!venderId) {
                alert('거래처를 선택해주세요.');
                e.preventDefault(); return;
            }
            if (!gId) {
                alert('자재 대분류를 선택해주세요.');
                e.preventDefault(); return;
            }
            if (!itemId) {
                alert('자재 소분류를 선택해주세요.');
                e.preventDefault(); return;
            }
            if (!ioReason) {
                alert('입고사유를 선택해주세요.');
                e.preventDefault(); return;
            }
            if (!lotQty || Number(lotQty) < 1) {
                alert('수량을 입력해주세요.');
                e.preventDefault(); return;
            }
            if (!expiryDate) {
                alert('유통기한을 입력해주세요.');
                e.preventDefault(); return;
            }
            if (!ioTime) {
                alert('입고일을 선택해주세요.');
                e.preventDefault(); return;
            }

        } else if (ioType === '1') {
            // ── 출고 유효성 검사 ──
            const lotId  = document.getElementById('lot_id_hidden').value;
            const empId  = document.getElementById('emp_id_hidden').value;

            if (!lotId) {
                alert('LOT번호를 검색하여 선택해주세요.');
                e.preventDefault(); return;
            }
            if (!ioReason) {
                alert('출고사유를 선택해주세요.');
                e.preventDefault(); return;
            }
            if (!ioTime) {
                alert('출고일을 선택해주세요.');
                e.preventDefault(); return;
            }
            if (!empId) {
                alert('작업자를 선택해주세요.');
                e.preventDefault(); return;
            }
        }
    });


    // ── LOT 검색 팝업 열기 ───────────────────────────────────
    document.getElementById('btnLotSearch').addEventListener('click', function () {
        lotKeywordInput.value = '';
        lotSearchBody.innerHTML = '<tr><td colspan="8">검색어를 입력하세요</td></tr>';
        lotSearchModal.showModal();
    });

    // LOT 검색 실행
    document.getElementById('btnLotKeywordSearch').addEventListener('click', function () {
        fetchLotList(lotKeywordInput.value);
    });

    // 엔터키로도 검색
    lotKeywordInput.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') fetchLotList(this.value);
    });

    // LOT 목록 AJAX 조회
    let lotAllData = [];
    const LOT_PAGE_SIZE = 10;
    let lotCurrentPage = 1;

    function fetchLotList(keyword) {
        fetch('/mes/io?action=getLotList&keyword=' + encodeURIComponent(keyword))
            .then(res => res.json())
            .then(data => {
                lotAllData = data;
                lotCurrentPage = 1;
                renderLotPage(lotCurrentPage);
            })
            .catch(function (err) {
                console.error('LOT 검색 실패:', err);
            });
    }

    function renderLotPage(page) {
        const body       = lotSearchBody;
        const pagination = document.getElementById('lotPagination');
        body.innerHTML   = '';
        pagination.innerHTML = '';

        if (lotAllData.length === 0) {
            body.innerHTML = '<tr><td colspan="8">검색 결과 없음</td></tr>';
            return;
        }

        const totalPages = Math.ceil(lotAllData.length / LOT_PAGE_SIZE);
        const start      = (page - 1) * LOT_PAGE_SIZE;
        const pageData   = lotAllData.slice(start, start + LOT_PAGE_SIZE);

        pageData.forEach(function (lot) {
            const tr = document.createElement('tr');
            tr.style.cursor = 'pointer';
            tr.innerHTML =
                '<td>' + lot.lot_id               + '</td>' +
                '<td>' + lot.item_id              + '</td>' +
                '<td>' + lot.item_name            + '</td>' +
                '<td>' + lot.spec                 + '</td>' +
                '<td>' + lot.unit                 + '</td>' +
                '<td>' + lot.remaining_qty        + '</td>' +
                '<td>' + (lot.expiry_date || '-') + '</td>' +
                '<td><button type="button" class="btn-lot-select">선택</button></td>';

            tr.querySelector('.btn-lot-select').addEventListener('click', function () {
                document.getElementById('lot_id_display').value    = lot.lot_id;
                document.getElementById('lot_id_hidden').value     = lot.lot_id;
                document.getElementById('item_id_hidden').value    = lot.item_id;
                document.getElementById('item_name_display').value = lot.item_name;
                document.getElementById('spec').value              = lot.spec;
                document.getElementById('unit').value              = lot.unit;
                document.getElementById('lot_qty').value           = lot.remaining_qty;
                document.getElementById('expiry_date').value       = lot.expiry_date || '';
                document.getElementById('empName').value           = lot.ename;
                document.getElementById('emp_id_hidden').value     = lot.emp_id;
                lotSearchModal.close();
            });

            body.appendChild(tr);
        });

        // 페이지네이션 버튼 렌더링
        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement('button');
            btn.type      = 'button';
            btn.textContent = i;
            btn.style.cssText = 'padding:4px 10px; border-radius:5px; border:1px solid #d0d5dd; cursor:pointer; font-size:13px;'
                + (i === page ? 'background:#4a4a6a; color:#fff; font-weight:700;' : 'background:#fff; color:#555;');
            btn.addEventListener('click', function () {
                lotCurrentPage = i;
                renderLotPage(lotCurrentPage);
            });
            pagination.appendChild(btn);
        }
    }

    // LOT 검색 팝업 닫기
    document.getElementById('btnLotSearchCancel').addEventListener('click', function () {
        lotSearchModal.close();
    });


    // ── 작업자 검색 (필터바) ─────────────────────────────────
    document.getElementById('btnFilterEmpSearch').addEventListener('click', function () {
        empKeywordInput.value = '';
        empSearchBody.innerHTML = '<tr><td colspan="4">검색어를 입력하세요</td></tr>';
        empSearchModal.dataset.mode = 'filter';
        empSearchModal.showModal();
    });

    // 작업자 검색 실행
    document.getElementById('btnEmpKeywordSearch').addEventListener('click', function () {
        fetchUserList(empKeywordInput.value);
    });

    // 엔터키로도 검색
    empKeywordInput.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') fetchUserList(this.value);
    });

    // 작업자 목록 AJAX 조회
    function fetchUserList(keyword) {
        fetch('/mes/io?action=getUserList&keyword=' + encodeURIComponent(keyword))
            .then(res => res.json())
            .then(data => {
                if (data.length === 0) {
                    empSearchBody.innerHTML = '<tr><td colspan="4">검색 결과 없음</td></tr>';
                    return;
                }

                empSearchBody.innerHTML = '';
                data.forEach(function (user) {
                    const tr = document.createElement('tr');
                    tr.innerHTML =
                        '<td>' + user.emp_id  + '</td>' +
                        '<td>' + user.ename   + '</td>' +
                        '<td>' + user.dept_no + '</td>' +
                        '<td><button type="button" class="btn-lot-select">선택</button></td>';

                    tr.querySelector('.btn-lot-select').addEventListener('click', function () {
                        if (empSearchModal.dataset.mode === 'filter') {
                            document.getElementById('filterEmp').value   = user.ename;
                            document.getElementById('filterEmpId').value = user.emp_id;
                        } else {
                            document.getElementById('empName').value       = user.ename;
                            document.getElementById('emp_id_hidden').value = user.emp_id;
                        }
                        empSearchModal.close();
                    });

                    empSearchBody.appendChild(tr);
                });
            })
            .catch(function (err) {
                console.error('작업자 검색 실패:', err);
            });
    }

    // 작업자 검색 팝업 닫기
    document.getElementById('btnEmpSearchCancel').addEventListener('click', function () {
        empSearchModal.close();
    });


    // ── 검색 버튼 → 필터 파라미터 붙여서 페이지 이동 ────────
    document.getElementById('btnSearch').addEventListener('click', function () {
        const params = new URLSearchParams();
        params.set('page', '1');
        params.set('size', document.getElementById('size').value);

        const ioType   = document.getElementById('filterIoType').value;
        const vendorId = document.getElementById('filterVendorId').value;
        const gId      = document.getElementById('filterGId').value;
        const itemId   = document.getElementById('filterItemId').value;
        const dateFrom = document.getElementById('filterDateFrom').value;
        const dateTo   = document.getElementById('filterDateTo').value;
        const keyword  = document.getElementById('filterKeyword').value;
        const empId    = document.getElementById('filterEmpId').value;

        if (ioType)   params.set('filterIoType',   ioType);
        if (vendorId) params.set('filterVendorId', vendorId);
        if (gId)      params.set('filterGId',      gId);
        if (itemId)   params.set('filterItemId',   itemId);
        if (dateFrom) params.set('filterDateFrom', dateFrom);
        if (dateTo)   params.set('filterDateTo',   dateTo);
        if (keyword)  params.set('filterKeyword',  keyword);
        if (empId)    params.set('filterEmpId',    empId);
        // filterExpiry는 검색 버튼 클릭 시 초기화 (카드 필터 해제)

        location.href = '/mes/io?' + params.toString();
    });

    // ── 유통기한 카드 클릭 → 필터 적용 ─────────────────────
    const warnCard = document.querySelector('.inv-card-warn');
    const overCard = document.querySelector('.inv-card-over');
    if (warnCard) {
        warnCard.style.cursor = 'pointer';
        warnCard.addEventListener('click', function () {
            location.href = '/mes/io?page=1&size=' + document.getElementById('size').value + '&filterExpiry=warn';
        });
    }
    if (overCard) {
        overCard.style.cursor = 'pointer';
        overCard.addEventListener('click', function () {
            location.href = '/mes/io?page=1&size=' + document.getElementById('size').value + '&filterExpiry=over';
        });
    }


    // ── 기간 날짜 유효성 ─────────────────────────────────────
    document.getElementById('filterDateFrom').addEventListener('change', function () {
        document.getElementById('filterDateTo').min = this.value;
        if (document.getElementById('filterDateTo').value &&
            document.getElementById('filterDateTo').value < this.value) {
            document.getElementById('filterDateTo').value = '';
        }
    });
    const filterItemSelect     = document.getElementById('filterItemId');
    const allFilterItemOptions = Array.from(filterItemSelect.options).slice(1);

    // ── 기간 날짜 유효성: filterDateFrom → filterDateTo min 세팅 ──
    document.getElementById('filterDateFrom').addEventListener('change', function () {
        const dateTo = document.getElementById('filterDateTo');
        dateTo.min = this.value;
        if (dateTo.value && dateTo.value < this.value) {
            dateTo.value = '';
        }
    });

    document.getElementById('filterGId').addEventListener('change', function () {
        const selectedGid = this.value;
        filterItemSelect.innerHTML = '<option value="">자재 소분류</option>';

        if (!selectedGid) {
            allFilterItemOptions.forEach(opt => filterItemSelect.appendChild(opt.cloneNode(true)));
            return;
        }

        allFilterItemOptions.forEach(function (opt) {
            if (opt.dataset.gid === selectedGid) {
                filterItemSelect.appendChild(opt.cloneNode(true));
            }
        });
    });

});