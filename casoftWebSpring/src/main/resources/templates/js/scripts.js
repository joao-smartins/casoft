const menuToggle = document.getElementById('menu-toggle');
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('content');
    const overlay = document.getElementById('overlay');

    function toggleSidebar() {
        const isActive = sidebar.classList.contains('active');

        if (window.innerWidth <= 767.98) {
            sidebar.classList.toggle('active');
            overlay.classList.toggle('active');
        } else {
            sidebar.classList.toggle('active');
            if (sidebar.classList.contains('active')) {
                content.style.marginLeft = '250px';
            } else {
                content.style.marginLeft = '0';
            }
        }
    }

    // Botão hamburguer
    menuToggle.addEventListener('click', toggleSidebar);

    // Clica no overlay para fechar (mobile)
    overlay.addEventListener('click', () => {
        sidebar.classList.remove('active');
        overlay.classList.remove('active');
    });

    // Ao redimensionar a tela
    window.addEventListener('resize', () => {
        if (window.innerWidth <= 767.98) {
            sidebar.classList.remove('active');
            overlay.classList.remove('active');
            content.style.marginLeft = '0';
        } else {
            sidebar.classList.add('active');
            overlay.classList.remove('active');
            content.style.marginLeft = '250px';
        }
    });

    // Na inicialização da página
    document.addEventListener('DOMContentLoaded', () => {
        document.body.style.paddingTop = document.querySelector('.header').offsetHeight + 'px';

        if (window.innerWidth <= 767.98) {
            sidebar.classList.remove('active');
            content.style.marginLeft = '0';
        } else {
            sidebar.classList.add('active');
            content.style.marginLeft = '250px';
        }
    });

    // Estilo de focus nos inputs
    function mudaCorFocus(obj) {
        obj.style.color = 'black';
        obj.style.backgroundColor = 'white';
        obj.style.outline = 'none';
        obj.classList.add('focused');
    }

    function mudaCorBlur(obj) {
        obj.style.color = 'white';
        obj.style.backgroundColor = '#8f0101';
        obj.classList.remove('focused');
    }
