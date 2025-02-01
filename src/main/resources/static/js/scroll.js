// Function to handle scroll animation
function handleScrollAnimation() {
    const cards = document.querySelectorAll('.card'); // Select all cards
    const triggerBottom = window.innerHeight / 1.2; // Trigger point for animation
    const triggerTop = window.innerHeight / 5; // Point to hide the animation when scrolling up

    cards.forEach(card => {
        const cardTop = card.getBoundingClientRect().top; // Get card position
        const cardBottom = card.getBoundingClientRect().bottom;

        if (cardTop < triggerBottom && cardBottom > triggerTop) {
            card.classList.add('appear'); // Add class to make card slide in
        } else {
            card.classList.remove('appear'); // Remove class to make card slide out
        }
    });
}

// Listen for scroll events
window.addEventListener('scroll', handleScrollAnimation);

// Trigger the animation on page load
document.addEventListener('DOMContentLoaded', handleScrollAnimation);
const cardContainer = document.querySelector('.card-container');
const prevButton = document.querySelector('.nav-button.prev');
const nextButton = document.querySelector('.nav-button.next');

// Calculate card width dynamically
const cardWidth = document.querySelector('.card').offsetWidth + 20; // Including gap

nextButton.addEventListener('click', () => {
    cardContainer.scrollBy({ left: cardWidth, behavior: 'smooth' });
});

prevButton.addEventListener('click', () => {
    cardContainer.scrollBy({ left: -cardWidth, behavior: 'smooth' });
});
