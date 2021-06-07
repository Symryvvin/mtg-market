package ru.aizen.mtg.order.application.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("trader/")
public class TraderResource {

	@GetMapping("{traderId}")
	public ResponseEntity<?> trader(@PathVariable("traderId") long traderId) {
		System.out.println("redirect to identity");
		return null;
	}

}
