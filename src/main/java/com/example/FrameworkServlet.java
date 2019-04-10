package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.Properties;

/**
 * @author dxd
 */
public class FrameworkServlet extends HttpServlet {


    public FrameworkServlet() {
        System.out.println("FrameworkServlet constructor");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("frameworkServlet init");
        initBean();
    }

    protected void initBean() {
        System.out.println("FrameworkServlet initBean");



    }
}
