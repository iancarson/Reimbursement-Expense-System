package com.andrew.ers.services;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.andrew.ers.dto.ExpenseDTO;
import com.andrew.ers.model.Expense;
import com.andrew.ers.repositories.ExpenseRepo;

@Service
public class ExpenseService {
	
	@Autowired
	ExpenseRepo repo;
	
	@Autowired
	S3Service s3;
	
	public static ExpenseDTO convert(Expense e) {
		ExpenseDTO dto = new ExpenseDTO();
		dto.setId(e.getId());
		dto.setAmount(e.getAmount());
		dto.setDescription(e.getDescription());
		return dto;
	}
	
	public static Expense convertDTO(ExpenseDTO e) {
		Expense expense = new Expense();
		expense.setId(e.getId());
		expense.setAmount(e.getAmount());
		expense.setDescription(e.getDescription());
		return expense;
	}
	
	public static List<ExpenseDTO> convert(List<Expense> exps) {
		List<ExpenseDTO> list = new ArrayList<>(exps.size());
		for (Expense e : exps) {
			list.add(convert(e));
		}
		return list;
	}
	
	public static List<Expense> convertDTO(List<ExpenseDTO> exps) {
		List<Expense> list = new ArrayList<>(exps.size());
		for (ExpenseDTO e : exps) {
			list.add(convertDTO(e));
		}
		return list;
	}
	
	/**
	 * Uses s3 service to upload receipt.
	 * Checks for existence of expense in the database first, then proceeds with upload.
	 * The presigned (temporary) receipt URL is added to the database entity and returned
	 * @param id
	 * @param username
	 * @return Optional<Expense> - will be empty if expense does not exist
	 * @throws IOException
	 */
	public Optional<ExpenseDTO> addReceiptToExpenseById(long id, String username, MultipartFile file) throws IOException {
		Optional<Expense> e = repo.findById(id);
		if (e.isPresent()) {
			Expense exp = e.get();
			URL url = s3.uploadReceipt(username, id, file);
			ExpenseDTO edto = convert(exp);
			edto.setReceiptURL(url);
			return Optional.of(edto);
		}
		return Optional.ofNullable(null);
	}

}
