package com.xiaojukeji.kafka.manager.web.api.versionone;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 水印访问当前用户
 * 
 * @author lishen
 *
 */
@Controller
@RequestMapping("/")
public class WaterMarkController{

	@ResponseBody
	@RequestMapping(value = "rest/api/user/current")
	public Map<String, String> waterMark(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		map.put("username", (String) request.getSession().getAttribute("username"));
		return map;
	}
}
