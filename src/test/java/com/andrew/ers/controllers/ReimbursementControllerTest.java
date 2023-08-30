package com.andrew.ers.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import com.andrew.ers.dto.ExpenseDTO;
import com.andrew.ers.dto.ReimbursementDTO;

public class ReimbursementControllerTest extends BaseControllerTest {
	
	private void generateMockReimbursementData() throws Exception {
		ReimbursementDTO r = new ReimbursementDTO();
		List<ExpenseDTO> exps = new ArrayList<>();
		exps.add(new ExpenseDTO(1L, 30.21, "test", null));
		exps.add(new ExpenseDTO(1L, 30.21, "abc", null));
		exps.add(new ExpenseDTO(1L, 30.21, "description here", null));
		r.setExpenses(exps);
		String json = mapper.writeValueAsString(r);
		mvc.perform(post("/users/acrenwelge/reimbursements").contentType(MediaType.APPLICATION_JSON).content(json));
	}
	
	@Test
	public void testGetReimbursements() throws Exception {
		mvc.perform(get("/reimbursements").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testGetReimbursementsForUser() throws Exception {
		generateMockReimbursementData();
		mvc.perform(get("/users/acrenwelge/reimbursements").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	
	@Test
	public void testApproveReimbursement() throws Exception {
		generateMockReimbursementData();
		mvc.perform(put("/reimbursements/1/status")
				.content("{\"action\": \"approve\"}")
				.contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isNoContent());
		MvcResult result = mvc.perform(get("/reimbursements/1"))
		.andExpect(status().isOk())
		.andReturn();
		MockHttpServletResponse r = result.getResponse();
		ReimbursementDTO resp = mapper.readValue(r.getContentAsString(), ReimbursementDTO.class);
		assertTrue(resp.isApproved());
	}
	
	@Test
	public void testDenyReimbursement() throws Exception {
		generateMockReimbursementData();
		mvc.perform(put("/reimbursements/1/status")
				.content("{\"action\": \"deny\"}")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		  .andExpect(status().isNoContent());
		MvcResult result = mvc.perform(get("/reimbursements/1"))
		.andExpect(status().isOk())
		.andReturn();
		MockHttpServletResponse r = result.getResponse();
		ReimbursementDTO resp = mapper.readValue(r.getContentAsString(), ReimbursementDTO.class);
		assertFalse(resp.isApproved());
	}
	
}
