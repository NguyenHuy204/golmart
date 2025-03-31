// Main JavaScript file for Golmart Store

document.addEventListener('DOMContentLoaded', function() {
    // Promotional Buttons Hover Animation
    const promoButtons = document.querySelectorAll('.promo-btn');
    promoButtons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = '0 10px 15px rgba(0, 0, 0, 0.1)';
        });
        
        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = 'none';
        });
    });
    
    // Auth Buttons Animation
    const authButtons = document.querySelectorAll('.auth-btn');
    authButtons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'scale(1.05)';
        });
        
        button.addEventListener('mouseleave', function() {
            this.style.transform = 'scale(1)';
        });
    });
    
    // Search Bar Focus Effect
    const searchBar = document.querySelector('.search-bar');
    if (searchBar) {
        searchBar.addEventListener('focus', function() {
            this.style.boxShadow = '0 0 10px rgba(0, 0, 0, 0.1)';
            this.style.backgroundColor = '#f7e9d7';
        });
        
        searchBar.addEventListener('blur', function() {
            this.style.boxShadow = 'none';
            this.style.backgroundColor = '#f0debb';
        });
    }
}); 