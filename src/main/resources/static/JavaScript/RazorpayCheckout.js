document.addEventListener("DOMContentLoaded", function () {
    // Get the amount that was set in the HTML file
    let amount = parseInt(paymentAmount);

    if (!amount || amount <= 0) {
        alert("Invalid amount. Returning to menu.");
        window.location.href = "/product/back";
        return;
    }

    // 1. Create Order API call
    fetch('/payment/create_order', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({amount: amount})
    })
    .then(response => response.json())
    .then(order => {

        // 2. Razorpay configuration
        var options = {
            "key": "rzp_test_SmO9iDl0lLi7yb", 
            "amount": order.amount,
            "currency": "INR",
            "name": "CraveHub",
            "description": "Food Delivery Payment",
            "image": window.location.origin + "/Images/logo.png",
            
            "order_id": order.id,
            "handler": function (response) {
                // 3. SUCCESS HANDLER: Send data to /processPayment using a hidden form
                var form = document.createElement("form");
                form.method = "POST";
                form.action = "/processPayment";

                var pid = document.createElement("input");
                pid.type = "hidden";
                pid.name = "payment_id";
                pid.value = response.razorpay_payment_id;
                form.appendChild(pid);

                var oid = document.createElement("input");
                oid.type = "hidden";
                oid.name = "order_id";
                oid.value = response.razorpay_order_id;
                form.appendChild(oid);

                document.body.appendChild(form);
                form.submit();
            },
            "prefill": {
                "name": "",
                "email": "",
                "contact": ""
            },
            "theme": {
                "color": "#dc143c" // Crimson Red
            },
            "modal": {
                "ondismiss": function () {
                    window.location.href = "/product/back"; 
                }
            }
        };

        // 4. Open Razorpay Interface
        var rzp = new Razorpay(options);
        rzp.on('payment.failed', function (response) {
            alert("Payment Failed! Reason: " + response.error.description);
            window.location.href = "/product/back";
        });
        rzp.open();

    })
    .catch(error => {
        console.error('Error:', error);
        alert("Could not connect to Payment Gateway. Please try again.");
        window.location.href = "/product/back";
    });
});