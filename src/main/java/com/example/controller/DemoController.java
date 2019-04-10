package com.example.controller;

import com.example.annotation.Controller;
import com.example.annotation.GetMapping;

/**
 * @author dxd
 */
@Controller
public class DemoController {


    @GetMapping(value = "/home")
    public String test() {
        return "home";
    }


}
