/**
 * Renders the footer so it can be inserted inside a div with the "footer" ID
 * Note: new Date().getFullYear() returns the year in YYYY format
 */
function renderFooter() {
    const footer = document.getElementById('footer');
    //TODO: create the rest of pages so the links take you somewhere

    footer.innerHTML = `
        <footer class="footer">
            <div class="footer-logo">
                <img src="/assets/images/logo/logo.png" alt="Clinic Logo" />
                <p>&copy; Copyright ${new Date().getFullYear()}. All rights reserved.</p>
            </div>

            <div class="footer-columns">
                <div class="footer-column">
                    <h4>Company</h4>
                    <a href="#">About</a>
                    <a href="#">Careers</a>
                    <a href="#">Press</a>
                </div>

                <div class="footer-column">
                    <h4>Support</h4>
                    <a href="#">Account</a>
                    <a href="#">Help Center</a>
                    <a href="#">Contact</a>
                </div>

                <div class="footer-column">
                    <h4>Legals</h4>
                    <a href="#">Terms</a>
                    <a href="#">Privacy Policy</a>
                    <a href="#">Licensing</a>
                </div>
            </div>
        </footer>
    `;
}


//calling the function so it's executed when the file loads
renderFooter();