window.addEventListener("load", () => {
    init();
});

function init() {
    bind();
}

function bind() {

}

function clampNumber(el) {
    let val = el.value;

    // 빈 값 허용
    if (val === '') return '';

    let num = Number(val);
    if (isNaN(num)) return '';

    const min = el.min !== '' ? Number(el.min) : -Infinity;
    const max = el.max !== '' ? Number(el.max) : Infinity;

    if (num < min) return min;
    if (num > max) return max;

    return num;
}

function submitContentModify() {
    const form = document.getElementById("contentModify");
    const checkedStatus = document.querySelector('input[name="status"]:checked');

    const woQty = Number(document.getElementById("woQty").value); // 목표 수량
    const prevQtyEl = document.getElementById("prevQty");
    const prevQtyStr = prevQtyEl.value.trim();
    const prevQty = prevQtyStr === "" ? 0 : Number(prevQtyStr);

    // 작업완료일 때만 완료 수량 필수
    if (checkedStatus && checkedStatus.value === "30") {
        if (completedQtyStr === "") {
            alert("완료 수량을 입력하세요");
            return;
        }

        if (isNaN(completedQty) || completedQty <= 0) {
            alert("완료 수량을 올바르게 입력하세요");
            return;
        }

        if (completedQty < woQty) {
            alert("완료 수량이 목표 수량보다 적으면 작업을 완료할 수 없습니다");
            return;
        }
    }

    form.submit();
}