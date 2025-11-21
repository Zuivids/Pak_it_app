const menuToggle = document.getElementById('menu-toggle');
const navMenu = document.querySelector('#main-nav ul');

// MENU TOGGLE
menuToggle.addEventListener('click', () => navMenu.classList.toggle('open'));

document.querySelectorAll('#main-nav a:not(.login-trigger)').forEach(link => {
    link.addEventListener('click', () => navMenu.classList.remove('open'));
});

document.querySelector('.login-trigger').addEventListener('click', (e) => {
    e.stopPropagation();
});

// LANGUAGE SWITCH
const langButtons = document.querySelectorAll('.lang-btn');

// Store Latvian text once
document.querySelectorAll('[data-en]').forEach(el => {
    if (!el.dataset.lv) el.dataset.lv = el.textContent.trim();
});

// Store placeholder Latvian text once
document.querySelectorAll('[data-en-placeholder]').forEach(el => {
    if (!el.dataset.lvPlaceholder) el.dataset.lvPlaceholder = el.placeholder;
});

langButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        langButtons.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        const lang = btn.dataset.lang;

        document.querySelectorAll('[data-en]').forEach(el => {
            el.textContent = lang === 'en' ? el.dataset.en : el.dataset.lv;
        });

        document.querySelectorAll('[data-en-placeholder]').forEach(el => {
            el.placeholder = lang === 'en' ? el.dataset.enPlaceholder : el.dataset.lvPlaceholder;
        });
    });
});
