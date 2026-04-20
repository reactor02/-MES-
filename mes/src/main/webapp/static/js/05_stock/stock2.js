window.addEventListener("load", function(){
	bind()
})

function bind(){

    document.getElementById('btnSearch').addEventListener('click', function() {
        const gId     = document.getElementById('filterGId').value;
        const keyword = document.getElementById('filterKeyword').value;
        const size    = document.getElementById('size').value;
        // filterStock은 검색 버튼 클릭 시 초기화 (카드 필터 해제)
        location.href = '/mes/stock?page=1&size=' + size
            + '&filterGId=' + encodeURIComponent(gId)
            + '&filterKeyword=' + encodeURIComponent(keyword);
    });

    // 필터 초기화 버튼 - 자재분류, 키워드, 재고상태 필터 모두 해제
    document.getElementById('btnReset').addEventListener('click', function() {
        var size = document.getElementById('size').value;
        location.href = '/mes/stock?page=1&size=' + size;
    });

    document.getElementById('filterGId').addEventListener('change', function() {
        document.getElementById('btnSearch').click();
    });

    document.getElementById('size').addEventListener('change', function() {
        document.getElementById('btnSearch').click();
    });

    // ── 요약 카드 클릭 → 필터 적용 ──────────────────────────
    var size = document.getElementById('size').value;

    // 전체 품목 카드
    var totalCard = document.querySelector('.inv-card:not(.inv-card-normal):not(.inv-card-lack)');
    if (totalCard) {
        totalCard.style.cursor = 'pointer';
        totalCard.addEventListener('click', function() {
            location.href = '/mes/stock?page=1&size=' + size;
        });
    }

    // 정상재고 카드
    var normalCard = document.querySelector('.inv-card-normal');
    if (normalCard) {
        normalCard.style.cursor = 'pointer';
        normalCard.addEventListener('click', function() {
            location.href = '/mes/stock?page=1&size=' + size + '&filterStock=normal';
        });
    }

    // 부족재고 카드
    var lackCard = document.querySelector('.inv-card-lack');
    if (lackCard) {
        lackCard.style.cursor = 'pointer';
        lackCard.addEventListener('click', function() {
            location.href = '/mes/stock?page=1&size=' + size + '&filterStock=lack';
        });
    }

}