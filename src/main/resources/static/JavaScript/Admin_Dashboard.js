document.addEventListener("DOMContentLoaded", function () {
    // Variables for counting Data
    let pending = 0, preparing = 0, outForDelivery = 0, delivered = 0;
    let revenueMap = {};
    let itemCountMap = {};

    const orderRows = document.querySelectorAll("#ordersTable tbody tr");
    
    orderRows.forEach(row => {
        // 1. Process Status for Doughnut Chart
        let statusText = row.querySelector(".status-badge").innerText.trim().toUpperCase();
        if (statusText === 'PENDING') pending++;
        else if (statusText === 'PREPARING') preparing++;
        else if (statusText === 'OUT FOR DELIVERY') outForDelivery++;
        else if (statusText === 'DELIVERED') delivered++;

        // 2. Process Revenue by Date for Bar Chart
        let amountText = row.cells[2].innerText.replace('₹', '').trim();
        let dateText = row.cells[3].innerText.trim();
        let amount = parseFloat(amountText) || 0;

        if (revenueMap[dateText]) {
            revenueMap[dateText] += amount;
        } else {
            revenueMap[dateText] = amount;
        }

        // 3. Calculate Top Selling Item
        let itemName = row.cells[1].innerText.trim();
        if (itemCountMap[itemName]) {
            itemCountMap[itemName]++;
        } else {
            itemCountMap[itemName] = 1;
        }
    });

    // Find Top Seller
    let topItem = "N/A";
    let maxOrders = 0;
    for (let item in itemCountMap) {
        if (itemCountMap[item] > maxOrders) {
            maxOrders = itemCountMap[item];
            topItem = item;
        }
    }
    document.getElementById("topSellingItem").innerText = topItem;

    // --- Render Order Status Doughnut Chart ---
    const ctxStatus = document.getElementById('statusChart');
    if(ctxStatus) {
        new Chart(ctxStatus.getContext('2d'), {
            type: 'doughnut',
            data: {
                labels: ['Pending', 'Preparing', 'Out for Delivery', 'Delivered'],
                datasets: [{
                    data: [pending, preparing, outForDelivery, delivered],
                    backgroundColor: ['#ff7675', '#fab1a0', '#81ecec', '#55efc4'], 
                    borderColor: ['#121212', '#121212', '#121212', '#121212'], 
                    borderWidth: 2
                }]
            },
            options: { responsive: true, plugins: { legend: { position: 'bottom', labels: { color: 'white' } } } }
        });
    }

    // --- Render Daily Revenue Bar Chart ---
    const ctxRevenue = document.getElementById('revenueChart');
    if(ctxRevenue) {
        new Chart(ctxRevenue.getContext('2d'), {
            type: 'bar',
            data: {
                labels: Object.keys(revenueMap), 
                datasets: [{
                    label: 'Total Revenue (₹)',
                    data: Object.values(revenueMap), 
                    backgroundColor: '#dc143c', // Crimson theme color
                    borderRadius: 4
                }]
            },
            options: { 
                responsive: true, 
                scales: { 
                    y: { beginAtZero: true, ticks: { color: '#ccc' }, grid: { color: '#333' } },
                    x: { ticks: { color: '#ccc' }, grid: { color: '#333' } }
                },
                plugins: { legend: { labels: { color: 'white' } } }
            }
        });
    }
});