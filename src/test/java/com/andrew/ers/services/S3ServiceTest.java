package com.andrew.ers.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class S3ServiceTest {
	
	@Autowired
	S3Service s3;
	
	@Test
	public void testObjectKey() {
		String username = "acrenwelge";
		int expense_id = 1;
		String key = s3.getBucketKey(username, expense_id);
		assertEquals(key, "acrenwelge/receipt-1");
	}
}
