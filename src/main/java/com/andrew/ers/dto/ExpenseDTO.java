package com.andrew.ers.dto;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ExpenseDTO {
	
	private long id;
	private double amount;
	private String description;
	private URL receiptURL; // presigned URL for temporary access by client
	
	public ExpenseDTO() {}
	
	public ExpenseDTO(long id, double amount, String desc, URL receipt) {
		this.id = id;
		this.amount = amount;
		this.description = desc;
		this.receiptURL = receipt;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public URL getReceiptURL() {
		return receiptURL;
	}
	public URI getReceiptURI() throws URISyntaxException {
		if (receiptURL != null) {
			return receiptURL.toURI();
		}
		return null;
	}
	public void setReceiptURL(URL receiptURL) {
		this.receiptURL = receiptURL;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((receiptURL == null) ? 0 : receiptURL.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExpenseDTO other = (ExpenseDTO) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (receiptURL == null) {
			if (other.receiptURL != null)
				return false;
		} else if (!receiptURL.equals(other.receiptURL))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ExpenseDTO [id=" + id + ", amount=" + amount + ", description=" + description + ", receiptURL="
				+ receiptURL + "]";
	}
}
