function toggleChat() {
    document.getElementById('chatWindow').classList.toggle('active');
}

function handleKeyPress(event) {
    if (event.key === 'Enter') {
        sendMessage();
    }
}

function sendMessage() {
    const input = document.getElementById('userInput');
    let message = input.value.trim().toLowerCase();
    const chatBody = document.getElementById('chatBody');

    if (message !== "") {
        // User Message Add
        const userDiv = document.createElement('div');
        userDiv.className = 'message user-message';
        userDiv.innerHTML = `<p>${input.value}</p>`;
        chatBody.appendChild(userDiv);
        
        input.value = "";
        chatBody.scrollTop = chatBody.scrollHeight;

        // --- BOT "TRAINING" DATA (Dictionary) ---
        const botKnowledge = [
            {
                keywords: ["hi", "hello", "hey", "namaste", "hi cravehub"],
                reply: "Hello! CraveHub mein swagat hai! Aaj kya khana pasand karenge? 🍔"
            },
            {
                keywords: ["biryani", "pulao", "rice", "veg biryani", "chicken biryani"],
                reply: "Biryani? 😍 Best choice! Hamari **Hyderabadi Chicken Biryani (₹410)** world famous hai. Menu mein check karein!"
            },
            {
                keywords: ["paneer", "shahi", "butter masala", "veg", "vegetarian", "do pyaza"],
                reply: "Veg lovers ke liye **Paneer Butter Masala (₹290)** aur **Paneer Do Pyaza (₹270)** best hain! 🥗"
            },
            {
                keywords: ["chicken", "non veg", "butter chicken", "tikka", "afgani"],
                reply: "Chicken cravings? 🍗 **Butter Chicken (₹380)** ya **Afgani Chicken (₹410)** try karein. Ungliyan chaat te reh jayenge!"
            },
            {
                keywords: ["roti", "naan", "paratha", "laccha"],
                reply: "Gravy ke saath **Laccha Paratha (₹30)** perfect combination hai. 🫓"
            },
            {
                keywords: ["snack", "starter", "chinese", "chilli", "chowmein", "momos"],
                reply: "Starter mein **Honey Chilli Potato (₹150)** ya **Chowmein (₹150)** try karein. 🍜"
            },
            {
                keywords: ["sweet", "meetha", "dessert", "gulab jamun", "ice cream"],
                reply: "Khane ke baad kuch meetha ho jaye? Garam-garam **Gulab Jamun (₹50)** best rahenge! 🍨"
            },
            {
                keywords: ["offer", "discount", "coupon", "promo", "cheap"],
                reply: "Abhi **FLAT 50% OFF** chal raha hai first order par! Aur ₹500 se upar free delivery hai. 💸"
            },
            {
                keywords: ["time", "late", "delivery", "kab aayega", "kitni der"],
                reply: "Chinta mat karein! Hum **30 Minutes** mein superfast delivery ki guarantee dete hain. 🛵💨"
            },
            {
                keywords: ["bye", "thanks", "thank you", "dhanyawad", "ok"],
                reply: "You're welcome! Happy Eating from CraveHub! Phir milenge. 😊"
            }
        ];

        // Bot Response Logic
        setTimeout(() => {
            const botDiv = document.createElement('div');
            botDiv.className = 'message bot-message';
            
            let finalReply = "";

            // chek the message to our knowledge base
            for (let i = 0; i < botKnowledge.length; i++) {
                let category = botKnowledge[i];
                // Check if any keyword matches the user's message
                let matched = category.keywords.some(keyword => message.includes(keyword));
                
                if (matched) {
                    finalReply = category.reply;
                    break; 
                }
            }

            if (finalReply === "") {
                finalReply = "Maaf kijiye, main samajh nahi paya. 🤔 Kya aap 'Biryani', 'Chicken', 'Paneer' ya 'Offers' ke baare mein puchna chahte hain?";
            }

            botDiv.innerHTML = `<p>${finalReply}</p>`;
            chatBody.appendChild(botDiv);
            chatBody.scrollTop = chatBody.scrollHeight;
        }, 600); // 600ms delay to make it feel human
    }
}