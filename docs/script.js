/**
 * Main script file for CarShowcase Hub
 * Handles:
 * 1. API Base URL
 * 2. Authentication (Token storage, User state)
 * 3. Dynamic Navigation Bar
 * 4. Logout
 * 5. Helper functions (loading, errors)
 */

// Base URL for your Spring Boot API
const API_BASE_URL = 'http://localhost:8080';

/**
 * Runs on every page load to check auth status and update the nav bar.
 */
document.addEventListener('DOMContentLoaded', () => {
    updateNavigationBar();
});

/**
 * Checks localStorage for auth token and updates the nav bar links.
 */
function updateNavigationBar() {
    const navLinks = document.querySelector('.nav .more');
    if (!navLinks) {
        console.error("Navigation '.more' element not found.");
        return; 
    }

    const token = localStorage.getItem('authToken');
    const userRole = localStorage.getItem('userRole');
    const username = localStorage.getItem('username');
    
    // Default links (Home, Inventory) are included for everyone
    let linksHtml = `
        <li><a href="./">Home</a></li>
        <li><a href="inventary.html">Inventory</a></li>
    `; 

    if (token && userRole && username) {
        // User is logged in
        if (userRole === 'ROLE_OWNER') {
            // OWNER: Includes Dashboard, excludes Test Drive
            linksHtml += `
                <li><a href="owner-dashboard.html">Owner Dashboard</a></li>
            `;
        } else {
            // REGULAR USER: Includes Test Drive
            linksHtml += `
                <li><a href="test-drive.html">Test Drive</a></li>
            `;
        }
        
        // Add welcome message and logout button
        linksHtml += `
            <div style="display:flex;flex-direction:column;justify-content:center;"><li><a href="#" id="logout-btn">Logout</a></li><li><a href="#" id="logout-btn">welcom ${username}</a></li></div>
        `;
    } else {
        // User is a guest (not logged in): Includes Test Drive, Login, and Register
        linksHtml += `
            <li><a href="test-drive.html">Test Drive</a></li>
            <li><a href="login.html">Login</a></li>
            <li><a href="register.html">Register</a></li>
        `;
    }

    navLinks.innerHTML = linksHtml;

    // Add event listener for the new logout button
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            logout();
        });
    }
}

/**
 * Clears user data from localStorage and redirects to home.
 */
function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('username');
    
    // Redirect to home page
    window.location.href = 'Home.html';
}

/**
 * Helper function to get the JWT token.
 * @returns {string | null} The auth token
 */
function getAuthToken() {
    return localStorage.getItem('authToken');
}

/**
 * Helper function to show a generic error message
 * @param {HTMLElement} container - The element to inject the error into
 * @param {string} message - The error message
 */
function displayError(container, message) {
    if (container) {
        container.innerHTML = `<p id="error-message">${message}</p>`;
    }
}

/**
 * Helper function to show a loading spinner
 * @param {HTMLElement} container - The element to inject the spinner into
 */
function displayLoading(container) {
    if (container) {
        container.innerHTML = `<p id="loading-spinner">Loading...</p>`;
    }
}
const showButton = document.querySelector('.logo');
const slider = document.querySelector('#slide');

function showSlider() {
  console.log('button clicked');
  slider.classList.remove('hidden');
}

function hideSlider() {
    console.log('clicked outside')
  slider.classList.add('hidden');
}

showButton.addEventListener('click', showSlider);

document.addEventListener('click', function(event) {
    console.log(showButton)
    console.log(event.target)
  if (event.target !== showButton && !slider.contains(event.target)) {
    hideSlider();
  }
});
