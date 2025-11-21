const images = [
    '/img/picture1.jpg',
    '/img/picture2.jpg',
    '/img/picture3.jpg'
];

let current = 0;
const bgDiv = document.querySelector('.hero-bg');

function changeBackground() {
    bgDiv.style.backgroundImage = `url('${images[current]}')`;
    current = (current + 1) % images.length;
}

changeBackground();
setInterval(changeBackground, 5000);
