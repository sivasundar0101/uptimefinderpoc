
package com.poc.uptimefinder.controller;

import com.poc.uptimefinder.UserRequest.Userrequest;
import com.poc.uptimefinder.exception.ResourceNotFoundException;
import com.poc.uptimefinder.model.Urlstatus;
import com.poc.uptimefinder.model.User;
import com.poc.uptimefinder.repository.UrlstatusRepository;
import com.poc.uptimefinder.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UrlstatusRepository urlstatusRepository;

	@GetMapping("/url")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/url/{id}")
	public ResponseEntity<User> getUsersById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
		return ResponseEntity.ok().body(user);
	}

	@PostMapping("/url")
	public User createUser(@Valid @RequestBody User user) throws IOException {
		monitorUrlStatus(user.getUrls(), user.getCreatedAt(), user.getTimeinterval());
		return userRepository.save(user);

	}

	private void monitorUrlStatus(String urls, Date createdAt, int timeinterval) throws IOException {
		// TODO Auto-generated method stub

		Urlstatus urlstatus = new Urlstatus();
		urlstatus.setStatus(getStatus(urls));
		urlstatus.setURL(urls);
		urlstatusRepository.save(urlstatus);
	}

	public static String getStatus(String url) throws IOException {

		String result = "";
		try {
			URL urlObj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			con.setRequestMethod("GET");
			// Set connection timeout
			con.setConnectTimeout(3000);
			con.connect();

			int code = con.getResponseCode();
			if (code == 200) {
				result = "Active";
			}
		} catch (Exception e) {
			result = "Down";
		}
		return result;
	}

	@PutMapping("/url/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
			@Valid @RequestBody User userDetails) throws ResourceNotFoundException {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

		user.setUrls(userDetails.getUrls());
		user.setTimeinterval(userDetails.getTimeinterval());

		final User updatedUser = userRepository.save(user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/url/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws Exception {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Url not found on :: " + userId));

		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@GetMapping("/url/history")
	public ResponseEntity<List<Urlstatus>> getUrlHistory(@Valid @RequestBody String url) {
		List<Urlstatus> urlStatus = urlstatusRepository.findByurl(url);

		return ResponseEntity.ok().body(urlStatus);
	}
	public List<User> insertAll(List<User> p) {
        return (List<User>)userRepository.saveAll(p);
    }
	
	 @PostMapping("/url/bulk")
	    public String addPeople(@RequestBody List<User> user) {
	        if(user != null && !user.isEmpty()) {
	            insertAll(user);
	            return String.format("Added %d url.", user.size());
	        } else {
	            return String.format("Not Added %d nurl.", user.size());
	        }
	    }
	 
	 
	   @DeleteMapping("/url/bulk")
	    public String deletePeople(@RequestBody List<User> ids) {
	        if(!ids.isEmpty()) {
	            if(deleteAll(ids)) {
	                return "Deleted the urls.";
	            } else {
	                return "Cannot delete the person.";
	            }
	        }
	        return "The request should contain a list of Url to be deleted.";
	    }
	    public boolean deleteAll(List<User> ids) {

	        try {
	            userRepository.deleteAll(ids);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }
}
