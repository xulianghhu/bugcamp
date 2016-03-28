package com.oracle.bugcamp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oracle.bugcamp.jdbc.JdbcTemplate;
import com.oracle.bugcamp.model.Bug;
import com.oracle.bugcamp.model.BugHead;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class BugHeadDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testGetBugList() {
		String sql = "SELECT * FROM RPTHEAD WHERE PROGRAMMER = :programmer and status=:status";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programmer", "LIANGX");
		params.put("status", "30");
		List<BugHead> bugHeads = jdbcTemplate.query(sql, params, BugHead.class);
		System.out.println(bugHeads.size());
	}

	@Test
	public void testGetBugList2() {
		String sql = "select h.rptno, h.rptdate, h.programmer, h.cs_priority, h.status, h.confirm_flag, h.product_id, h.category, h.utility_version, h.subject from rpthead h where h.programmer = :programmer";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programmer", "LIANGX");
		List<Bug> bugs = jdbcTemplate.query(sql, params, Bug.class);
		System.out.println(bugs.size());
	}
}
