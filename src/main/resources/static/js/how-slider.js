const howImages = [
    "img/how1.jpg",
    "img/how2.jpg",
    "img/how3.jpg",
    "img/how4.jpg"
];

let howIndex = 0;
let howTimer;

const imgEl = document.getElementById("how-slide");
const stepEls = document.querySelectorAll(".how-steps li");

function setActiveStep(index) {
    stepEls.forEach(el => el.classList.remove("active"));
    stepEls[index].classList.add("active");
}

function changeHowStep(index) {
    howIndex = index;
    imgEl.style.opacity = "0";
    setTimeout(() => {
        imgEl.src = howImages[index];
        imgEl.style.opacity = "1";
    }, 250);
    setActiveStep(index);
}

function startHowAuto() {
    howTimer = setInterval(() => {
        howIndex = (howIndex + 1) % howImages.length;
        changeHowStep(howIndex);
    }, 3000);
}

// Click handler for manual steps
stepEls.forEach(step => {
    step.addEventListener("click", () => {
        clearInterval(howTimer);
        changeHowStep(parseInt(step.dataset.step));
        setTimeout(startHowAuto, 5000);
    });
});

// Initialize slider
changeHowStep(0);
startHowAuto();
