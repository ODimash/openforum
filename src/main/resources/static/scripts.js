// scripts.js
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.toggle-form').forEach(button => {
        button.addEventListener('click', () => {
            const form = document.querySelector(button.dataset.target);
            form.classList.toggle('hidden');
        });
    });
});
