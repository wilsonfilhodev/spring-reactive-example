package br.com.wilsonfilho.springreactive.events;

import java.util.Date;

import br.com.wilsonfilho.springreactive.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvents {
	
	private Product product;
	private Date when;

}
