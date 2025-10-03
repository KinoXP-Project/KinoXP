// Simple API-wrapper
const api = {
    async getUpcoming() {
        const res = await fetch('/api/program/upcoming');
        if (!res.ok) throw new Error('HTTP ' + res.status);
        return res.json();
    },
    async getMovie(id) {
        const res = await fetch('/api/program/movies/' + encodeURIComponent(id));
        if (!res.ok) throw new Error('HTTP ' + res.status);
        return res.json();
    }
};

// Helpers
function formatDateTime(iso) {
    try {
        const d = new Date(iso);
        return d.toLocaleString('da-DK', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    } catch {
        return iso;
    }
}

function escapeHtml(s) {
    return String(s ?? '')
        .replaceAll('&','&amp;')
        .replaceAll('<','&lt;')
        .replaceAll('>','&gt;')
        .replaceAll('"','&quot;')
        .replaceAll("'","&#39;");
}
