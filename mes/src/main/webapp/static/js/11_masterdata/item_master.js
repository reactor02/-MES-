console.log(window.itemListForCode);

document.addEventListener('DOMContentLoaded', function() {

    // 품목 그룹 번호에 따라 이름 매핑
    const groupMap = {
        '30': '완제품',
        '20': '반제품',
        '10': '원자재'
    };

    // 수정 모달 관련 요소
    const editButtons = document.querySelectorAll('.icon-btn.edit');
    const editItemModal = document.querySelector('.edit_item_modal');
    const editItemCloseBtn = document.querySelector('.edit_item_close_btn');

    const editItemId = document.getElementById('edit_item_id');
    const editItemName = document.getElementById('edit_item_name');
    const editGId = document.getElementById('edit_g_id');
    const editGIdDisplay = document.getElementById('edit_g_id_display');
    const editSafeQty = document.getElementById('edit_safe_qty');
    const editPay = document.getElementById('edit_pay');
    const editSpec = document.getElementById('edit_spec');
    const editUnit = document.getElementById('edit_unit');

    // 수정 버튼 클릭 시 모달에 값 넣기
    for (let i = 0; i < editButtons.length; i++) {
        editButtons[i].addEventListener('click', function() {

            let itemId = editButtons[i].dataset.itemId;
            let itemName = editButtons[i].dataset.itemName;
            let gId = editButtons[i].dataset.gId;
            let safeQty = editButtons[i].dataset.safeQty;
            let pay = editButtons[i].dataset.pay;
            let spec = editButtons[i].dataset.spec;
            let unit = editButtons[i].dataset.unit;

            if (itemId == null) {
                itemId = '';
            }
            if (itemName == null) {
                itemName = '';
            }
            if (gId == null) {
                gId = '';
            }
            if (safeQty == null || safeQty === '') {
                safeQty = '0';
            }
            if (pay == null || pay === '') {
                pay = '0';
            }
            if (spec == null) {
                spec = '';
            }
            if (unit == null) {
                unit = '';
            }

            if (editItemId != null) {
                editItemId.value = itemId;
            }
            if (editItemName != null) {
                editItemName.value = itemName;
            }
            if (editGId != null) {
                editGId.value = gId;
            }
            if (editGIdDisplay != null) {
                if (groupMap[gId] != null) {
                    editGIdDisplay.value = groupMap[gId];
                } else {
                    editGIdDisplay.value = '';
                }
            }
            if (editSafeQty != null) {
                editSafeQty.value = safeQty;
            }
            if (editPay != null) {
                editPay.value = pay;
            }
            if (editSpec != null) {
                editSpec.value = spec;
            }
            if (editUnit != null) {
                editUnit.value = unit;
            }

            if (editItemModal != null) {
                editItemModal.style.display = 'flex';
            }
        });
    }

    // 수정 모달 닫기 버튼
    if (editItemCloseBtn != null && editItemModal != null) {
        editItemCloseBtn.addEventListener('click', function() {
            editItemModal.style.display = 'none';
        });
    }

    // 등록 모달 관련 요소
    const btnAdd = document.querySelector('.btn-add');
    const addItemModal = document.getElementById('addItemModal');
    const addItemCloseBtn = document.getElementById('cancelAddItemModal');
    const addItemForm = addItemModal != null ? addItemModal.querySelector('form') : null;

    if (btnAdd != null && addItemModal != null) {
        btnAdd.addEventListener('click', function() {
            addItemModal.style.display = 'flex';
            syncAddItemGroupName();
            updateItemCode();
        });
    }

    if (addItemCloseBtn != null && addItemModal != null) {
        addItemCloseBtn.addEventListener('click', function() {
            addItemModal.style.display = 'none';
        });
    }

    // 등록 폼 요소
    const addItemId = document.getElementById('add_item_id');
    const addItemName = document.getElementById('add_item_name');
    const addGId = document.getElementById('add_g_id');
    const addItemGroupName = document.getElementById('add_itemgroup_name');

    let codeItems = window.itemListForCode;
    if (codeItems == null) {
        codeItems = [];
    }

    // 품목 그룹명 hidden 값 세팅
    function syncAddItemGroupName() {
        if (addGId == null || addItemGroupName == null) {
            return;
        }

        const selectedGroup = addGId.value;

        if (groupMap[selectedGroup] != null) {
            addItemGroupName.value = groupMap[selectedGroup];
        } else {
            addItemGroupName.value = '';
        }
    }

    // 그룹별 접두어 정하기
    function changePrefixByGroup(gId) {
        if (gId === '30') {
            return 'fin';
        } else if (gId === '20') {
            return 'semi';
        } else if (gId === '10') {
            return 'raw';
        } else {
            return '';
        }
    }

    // 품목코드 자동 생성
    function updateItemCode() {
        if (addItemId == null || addItemName == null || addGId == null) {
            return;
        }

        const itemName = addItemName.value.trim();
        const gId = addGId.value;

        if (itemName === '' || gId === '') {
            addItemId.value = '';
            return;
        }

        let sameGroupItems = [];

        for (let i = 0; i < codeItems.length; i++) {
            if (String(codeItems[i].gId) === String(gId)) {
                sameGroupItems.push(codeItems[i]);
            }
        }

        let maxNumber = 1000;

        for (let i = 0; i < sameGroupItems.length; i++) {
            let itemId = String(sameGroupItems[i].itemId);
            let parts = itemId.split('_');

            if (parts.length > 1) {
                let number = parseInt(parts[1], 10);

                if (isNaN(number) === false) {
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                }
            }
        }

        const nextNumber = maxNumber + 1;
        const prefix = changePrefixByGroup(gId);

        if (prefix === '') {
            addItemId.value = '';
            return;
        }

        addItemId.value = prefix + '_' + nextNumber;
    }

    // 품목명 입력 시 품목코드 다시 만들기
    if (addItemName != null) {
        addItemName.addEventListener('input', function() {
            updateItemCode();
        });
    }

    // 품목그룹 변경 시 hidden 값, 품목코드 다시 만들기
    if (addGId != null) {
        addGId.addEventListener('change', function() {
            syncAddItemGroupName();
            updateItemCode();
        });
    }

    if (addItemForm != null) {
        addItemForm.addEventListener('submit', function(event) {
            syncAddItemGroupName();
            updateItemCode();

            if (addItemName != null && addItemName.value.trim() === '') {
                event.preventDefault();
                alert('품목명을 입력해주세요.');
                addItemName.focus();
                return;
            }

            if (addItemId != null && addItemId.value.trim() === '') {
                event.preventDefault();
                alert('품목코드가 생성되지 않았습니다. 품목그룹과 품목명을 다시 확인해주세요.');
            }
        });
    }

    // 페이지 처음 열릴 때 1번 실행
    syncAddItemGroupName();
    updateItemCode();

    // 모달 바깥 클릭 시 닫기
    window.addEventListener('click', function(e) {
        if (editItemModal != null) {
            if (e.target === editItemModal) {
                editItemModal.style.display = 'none';
            }
        }

        if (addItemModal != null) {
            if (e.target === addItemModal) {
                addItemModal.style.display = 'none';
            }
        }
    });

});
