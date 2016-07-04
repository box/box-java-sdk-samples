package com.box.sdk.webhookawssample;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.box.sdk.webhookawssample.helpers.BoxHelper;

/**
 *
 */
public class CreateUserServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        String username = request.getParameter("username");
        String userpass = request.getParameter("userpass");
        BoxHelper.setBoxAppUserId(request, null);
        BoxHelper.setBoxAppUserName(request, username);
        if (BoxHelper.prepareBoxUser(request, request.getParameter("username"), true)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.setAttribute("name", username);
            }
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "An App User with this name alreadt exists: "
                    + request.getParameter("username"));
            request.getRequestDispatcher("login.jsp").forward(request, response);

        }
    }

}
