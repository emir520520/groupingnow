package ca.sheridancollege.fangyux.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.sheridancollege.fangyux.beans.CartEvent;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.CartEventRepository;

@Service
public class CartEventServices {

		@Autowired
		private CartEventRepository carteventrepository;
		
		public List<CartEvent> listCartEvents(User user){
			return carteventrepository.findByUser(user);
		}
		
		public void removeEventFromCart(long id) {
			// TODO Auto-generated method stub
			//this.carteventrepository.deleteById(id);
			this.carteventrepository.deleteById(id);
		}
		
		
}
