let defChart = null;


window.addEventListener("load", () => {
	defGraph();
	init();
})

window.addEventListener("resize", () => {
    if (defChart) {
        defChart.resize();
    }
});


function init() {
	bind();
}

function bind() {
	
}

function defGraph() {
    const canvas = document.querySelector("#defGraph");
    if (!canvas) return;

    if (defChart) {
        defChart.destroy();
    }

    defChart = new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: ['합격', '불량'],
            datasets: [{
                data: [pass, defect],
                backgroundColor: ['#4774E9', '#E94747'],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '60%',
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const value = context.raw;
                            const percent = total === 0 ? 0 : ((value / total) * 100).toFixed(1);
                            return `${context.label}: ${value}개 (${percent}%)`;
                        }
                    }
                }
            }
        }
    });
}