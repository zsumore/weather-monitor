package com.fs121.monitor.servlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;




@WebServlet(urlPatterns = "/druid/*", 
    initParams={
  
            @WebInitParam(name="resetEnable",value="true")// 禁用HTML页面上的“Reset All”功能
    })
public class DruidStatViewServlet extends StatViewServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 998858744703405024L;

}