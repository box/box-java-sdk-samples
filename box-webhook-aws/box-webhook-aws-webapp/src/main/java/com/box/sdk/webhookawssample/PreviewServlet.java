package com.box.sdk.webhookawssample;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.webhookawssample.helpers.BoxHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;


public class PreviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String boxFileId = request.getParameter("id");
        String boxId = BoxHelper.getBoxAppUserId(request);
        URL previewUrl;
        response.setContentType("text/html");
        BoxAPIConnection userClient = BoxHelper.userClient(boxId);
        if (userClient == null) { // session timeout. force login again.
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        BoxFile boxFile = new BoxFile(userClient, boxFileId);
        previewUrl = boxFile.getPreviewLink();
        response.sendRedirect(previewUrl.toString());
    }
}
