package com.demo.socialmedia.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Helper thao tác với session đăng nhập.
 */
public final class SessionAuth {

    public static final String CURRENT_USER_ID = "currentUserId";

    private SessionAuth() {
    }

    public static void signIn(HttpServletRequest request, int userId) {
        HttpSession existing = request.getSession(false);
        if (existing != null) {
            existing.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(CURRENT_USER_ID, userId);
    }

    public static Integer getCurrentUserId(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object value = session.getAttribute(CURRENT_USER_ID);
        return value instanceof Integer ? (Integer) value : null;
    }

    public static void signOut(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}
