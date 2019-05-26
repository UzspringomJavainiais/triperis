package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripRequest;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import com.javainiaisuzspringom.tripperis.services.TripRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TripRequestService tripRequestService;
    @Autowired
    private TripRepository tripRepository;

    @GetMapping("/api/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );
        return ok(model);
    }

    @GetMapping("/api/me/pendingRequests")
    public List<TripRequest> getMyPendingRequests(@AuthenticationPrincipal UserDetails userDetails) {
        return tripRequestService.getMyPendingRequests(userDetails);
    }

    @GetMapping("/api/me/trips")
    public List<Trip> currentUserTrips(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = accountService.loadUserByUsername(userDetails.getUsername());
        return account.getTrips();
    }

    @PostMapping("/api/account")
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO account) {
        if (accountService.exists(account.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists");
        }
        if (account.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must not be empty");
        }
        if (account.getRoleIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must have roles");
        }

        Account savedEntity = accountService.save(account);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }

    @PutMapping("/api/account/{id}")
    public ResponseEntity<AccountDTO> editAccount(@PathVariable(name = "id") Integer id, @RequestBody AccountDTO newVersionDto) {

        Optional<Account> accountResultById = accountService.getById(id);

        if (!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Account account = accountResultById.get();
        Account newVersion = accountService.convertToEntity(newVersionDto);
        newVersion.setId(account.getId());
        newVersion.setPassword(account.getPassword());
        newVersion.setEmail(account.getEmail());

        Account savedEntity = accountRepository.save(newVersion);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.OK);
    }


    @PatchMapping("/api/account/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable(name = "id") Integer id, @RequestBody AccountDTO newVersionDto) {

        Optional<Account> accountResultById = accountService.getById(id);

        if (!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Account account = accountResultById.get();
        Account newVersion = accountService.convertToEntity(newVersionDto);
        account = accountService.mergeChanges(account, newVersion);

        Account savedEntity = accountRepository.save(account);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.OK);
    }


    @GetMapping("/api/account")
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(Account::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/api/account/{id}/tripsInPeriod")
    public ResponseEntity<List<CalendarEntry>> tripsInPeriod(@PathVariable(name = "id") Integer id,
                                                             @RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                                                             @RequestParam(name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd) {

        Optional<Account> accountResultById = accountService.getById(id);

        if (!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountResultById.get();

        List<CalendarEntry> accountFreeDates = accountService.getAccountCalendar(account, dateStart, dateEnd);
        return new ResponseEntity<>(accountFreeDates, HttpStatus.OK);
    }

    @GetMapping(value = "/api/account/{id}/isAccountFree")
    public ResponseEntity<Boolean> isAccountFree(@PathVariable(name = "id") Integer id,
                                                 @RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                                                 @RequestParam(name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd) {
        Optional<Account> accountResultById = accountService.getById(id);

        if (!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountResultById.get();

        List<CalendarEntry> accountFreeDates = accountService.getAccountCalendar(account, dateStart, dateEnd);

        Boolean isAccountFreeInPeriod = accountFreeDates.isEmpty();
        return new ResponseEntity<>(isAccountFreeInPeriod, HttpStatus.OK);
    }

    @GetMapping("/api/account/{id}/trips")
    public ResponseEntity<List<Trip>> getTripsByAccount(@PathVariable Integer id) {
        Optional<Account> account = accountService.getById(id);

        if (!account.isPresent())
            return ResponseEntity.notFound().build();

        Account accountDTO = account.get();
        List<Trip> accountTrips = accountDTO.getTrips();

        return new ResponseEntity<>(accountTrips, HttpStatus.OK);
    }

    @PostMapping("/api/account/{id}/approveTrip/{tripId}")
    public ResponseEntity<AccountDTO> approveTrip(@PathVariable Integer id,
                                                  @PathVariable Integer tripId) {
        Optional<Account> maybeAccount = accountService.getById(id);

        if (!maybeAccount.isPresent())
            return ResponseEntity.notFound().build();

        Optional<Trip> maybeTrip = tripRepository.findById(tripId);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        Account account = maybeAccount.get();

        // TODO: Implement TripRequest search by Trip id

        throw new NotImplementedException();
    }
}
