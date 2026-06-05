function startVoiceSearch() {
    if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
        var SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        var recognition = new SpeechRecognition();

        recognition.lang = 'en-IN'; 
        recognition.interimResults = false; 
        recognition.maxAlternatives = 1;

        var micBtn = document.querySelector('.mic-btn');
        micBtn.classList.add('mic-active');

        recognition.start();

        recognition.onresult = function (event) {
            var transcript = event.results[0][0].transcript;
            
            if (transcript.endsWith('.')) {
                transcript = transcript.slice(0, -1);
            }

            document.getElementById('searchBox').value = transcript;
            micBtn.classList.remove('mic-active');
            
            // Auto submit after voice search
            document.getElementById('searchForm').submit();
        };

        recognition.onspeechend = function () {
            recognition.stop();
            micBtn.classList.remove('mic-active');
        };

        recognition.onerror = function (event) {
            micBtn.classList.remove('mic-active');
            alert("Could not hear clearly. Please try typing.");
        };
    } else {
        alert("Voice Search is not supported in this browser. Try Chrome.");
    }
}