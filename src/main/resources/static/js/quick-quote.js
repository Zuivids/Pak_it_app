function handleQuoteSubmit(e) {
    e.preventDefault();
    document.getElementById('modal-email-error').style.display = 'none';
    document.getElementById('modal-email-input').value = '';
    document.getElementById('quote-modal').style.display = 'flex';
    setTimeout(function () {
        document.getElementById('modal-email-input').focus();
    }, 50);
}

function submitWithEmail() {
    var emailInput = document.getElementById('modal-email-input');
    var email = emailInput.value.trim();
    var errorEl = document.getElementById('modal-email-error');
    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email || !emailPattern.test(email)) {
        errorEl.style.display = 'block';
        emailInput.focus();
        return;
    }

    errorEl.style.display = 'none';
    document.getElementById('qq-user-email').value = email;
    closeQuoteModal();
    document.getElementById('quick-quote').submit();
}

function closeQuoteModal() {
    document.getElementById('quote-modal').style.display = 'none';
}

document.addEventListener('keydown', function (e) {
    var modal = document.getElementById('quote-modal');
    if (!modal || modal.style.display !== 'flex') return;
    if (e.key === 'Escape') closeQuoteModal();
    if (e.key === 'Enter') submitWithEmail();
});
