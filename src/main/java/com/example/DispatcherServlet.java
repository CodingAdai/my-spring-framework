package com.example;

import com.example.annotation.Controller;
import com.example.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * @author dxd
 */
@WebServlet(urlPatterns = "/*")
public class DispatcherServlet extends FrameworkServlet {

    private static final long serialVersionUID = -1154642122471362619L;

    private HashMap<String, Object> controllerMap = new HashMap<>();
    private Map<String, Method> pathMap = new HashMap<>();



    public DispatcherServlet() {
        System.out.println("DispatcherServlet constructor");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
        System.out.println("DispatcherServlet service");

        System.out.println(req.getPathInfo());
        System.out.println(req.getContextPath());
        System.out.println(req.getServletPath());




    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);

        Method method = pathMap.get(req.getPathInfo());
        if (method != null) {
            try {
                Object demo = method.invoke(controllerMap.get("demo"));


                ServletOutputStream outputStream = resp.getOutputStream();
                String              demo1        = (String) demo;
                outputStream.write(demo1.getBytes());
                outputStream.flush();
                outputStream.close();

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initBean() {
        System.out.println("DispatcherServlet initBean");

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties");

        StringBuilder stringBuilder = new StringBuilder();
        if (in != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
                stringBuilder.append(bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String properties = stringBuilder.toString();
        System.out.println("配置文件内容：" + properties);



        // 下面的会报错
        // String path = File.separator + "com" + File.separator + "example";
        // String path2 = this.getClass().getResource(path).getPath();
        // System.out.println(path2);

        URL resource = this.getClass().getResource("/com/example");
        System.out.println(resource.getPath());
        File file = new File(resource.getPath());
        File[] files = file.listFiles();
        for (File file1 : files) {
            System.out.println(file1);
            if (file1.isFile()) {
                String name = file1.getName();
                System.out.println(name);

                try {
                    Class<?> aClass = this.getClass().getClassLoader().loadClass("com.example.controller.DemoController");
                    if (aClass.isAnnotationPresent(Controller.class)) {
                        System.out.println("@Controller.class");
                        Object o = aClass.newInstance();
                        controllerMap.put("demo", o);

                        Method[] declaredMethods = aClass.getDeclaredMethods();
                        for (Method declaredMethod : declaredMethods) {
                            if (declaredMethod.isAnnotationPresent(GetMapping.class)) {

                                GetMapping annotation = declaredMethod.getAnnotation(GetMapping.class);
                                String value = annotation.value();
                                pathMap.put(value, declaredMethod);

                            }
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }


//        try {
//            JarFile jarFile = new JarFile("");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        File   file  = new File("com" + File.separator + "example");
//        File[] files = file.listFiles();
//        for (File file1 : files) {
//            System.out.println(file1.getName());
//        }

    }
}
