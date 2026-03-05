/**
 * API Helper - Gọi REST API từ frontend
 * Base URL: /social-media-app/api
 */
const APP_BASE = '/social-media-app';
const STATIC_BASE = APP_BASE + '/static';
const PAGES_BASE = STATIC_BASE + '/pages';
const API_BASE = APP_BASE + '/api';

function pageUrl(page, params = {}) {
    const url = new URL(PAGES_BASE + '/' + page, window.location.origin);
    Object.entries(params).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '') {
            url.searchParams.set(key, value);
        }
    });
    return url.pathname + url.search;
}

function goToPage(page, params = {}) {
    window.location.href = pageUrl(page, params);
}

// === FETCH WRAPPER ===
async function apiCall(endpoint, options = {}) {
    const url = API_BASE + endpoint;
    const config = {
        credentials: 'same-origin',
        headers: { 'Content-Type': 'application/json' },
        ...options
    };
    try {
        const res = await fetch(url, config);
        const contentType = res.headers.get('content-type') || '';
        if (!contentType.includes('application/json')) {
            return { success: res.ok, message: res.ok ? '' : 'Phản hồi không hợp lệ từ server' };
        }

        return await res.json();
    } catch (err) {
        console.error('API Error:', err);
        return { success: false, message: 'Lỗi kết nối server' };
    }
}

// === USER API ===
const UserAPI = {
    register: (username, password) =>
        apiCall('/users/register', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        }),

    login: (username, password) =>
        apiCall('/users/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        }),

    me: () => apiCall('/users/me'),

    logout: () =>
        apiCall('/users/logout', {
            method: 'POST'
        }),

    getAll: () => apiCall('/users'),

    getById: (id) => apiCall('/users/' + id)
};

// === POST API ===
const PostAPI = {
    getAll: () => apiCall('/posts'),

    getFeed: () => apiCall('/posts/feed'),

    getByUser: (userId) => apiCall('/posts/user/' + userId),

    getById: (id) => apiCall('/posts/' + id),

    create: (title, body, status = 'PUBLISHED') =>
        apiCall('/posts', {
            method: 'POST',
            body: JSON.stringify({ title, body, status })
        }),

    update: (id, title, body, status) =>
        apiCall('/posts/' + id, {
            method: 'PUT',
            body: JSON.stringify({ title, body, status })
        }),

    delete: (id) => apiCall('/posts/' + id, { method: 'DELETE' })
};

// === FOLLOW API ===
const FollowAPI = {
    follow: (targetId) =>
        apiCall('/follows', {
            method: 'POST',
            body: JSON.stringify({ targetId })
        }),

    unfollow: (targetId) =>
        apiCall('/follows?targetId=' + targetId, {
            method: 'DELETE'
        }),

    check: (targetId) =>
        apiCall('/follows/check?targetId=' + targetId),

    getFollowing: (userId) => apiCall('/follows/following/' + userId),

    getFollowers: (userId) => apiCall('/follows/followers/' + userId),

    getCount: (userId) => apiCall('/follows/count/' + userId)
};

// === SESSION HELPER (server-side session + client cache) ===
const Session = {
    currentUser: null,

    save(user) {
        this.currentUser = user;
        return user;
    },

    get() {
        return this.currentUser;
    },

    clear() {
        this.currentUser = null;
    },

    async refresh() {
        const result = await UserAPI.me();
        if (result.success && result.user) {
            return this.save(result.user);
        }

        this.clear();
        return null;
    }
};

// === TOAST NOTIFICATION ===
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = 'toast toast-' + type;
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

// === FORMAT DATE ===
function formatDate(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleDateString('vi-VN') + ' ' + date.toLocaleTimeString('vi-VN', {
        hour: '2-digit', minute: '2-digit'
    });
}

// === CHECK AUTH (redirect nếu chưa login) ===
async function requireAuth() {
    const user = await Session.refresh();
    if (!user) {
        goToPage('login.html');
        return null;
    }
    return user;
}
