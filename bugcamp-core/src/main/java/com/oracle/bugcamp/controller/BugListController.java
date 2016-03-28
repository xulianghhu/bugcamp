package com.oracle.bugcamp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oracle.bugcamp.jdbc.JdbcTemplate;
import com.oracle.bugcamp.model.Bug;

@Controller
public class BugListController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "bugList", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		String sql = "select h.rptno, h.rptdate, h.programmer, h.cs_priority, h.status, h.confirm_flag, h.product_id, h.category, h.utility_version, h.subject from rpthead h where h.programmer = :programmer";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programmer", "LIANGX");
		List<Bug> bugs = jdbcTemplate.query(sql, params, Bug.class);
		model.addAttribute("bugs", bugs);
		return "buglist";
	}
}
