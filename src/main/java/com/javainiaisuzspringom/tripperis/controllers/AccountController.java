package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripRequest;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
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

import javax.persistence.OptimisticLockException;
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
    @Autowired
    private RoleRepository roleRepository;

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

    @GetMapping("/api/me/organizing")
    public List<Trip> getMyOrganizingTrips(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = accountService.loadUserByUsername(userDetails.getUsername());
        return account.getOrganizedTrips();
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
    public ResponseEntity editAccount(@PathVariable(name = "id") Integer id, @RequestBody AccountDTO newVersionDto) {

        Optional<Account> accountResultById = accountService.getById(id);

        if (!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Account account = accountResultById.get();

        // Needed for handling the OptimisticLockException
//        account.setOptLockVersion(newVersionDto.getOptLockVersion());

        account.setFirstName(newVersionDto.getFirstName());
        account.setLastName(newVersionDto.getLastName());

        account.getRoles().clear();
        for (Integer roleId : newVersionDto.getRoleIds()) {
            Optional<Role> maybeRole = roleRepository.findById(roleId);

            if (maybeRole.isPresent())
                account.getRoles().add(maybeRole.get());
        }

        Account savedEntity;
        try {
            savedEntity = accountRepository.save(account);
        } catch (OptimisticLockException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("err.optimistic.lock");
        }

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

    @DeleteMapping("/api/account/{id}")
    public ResponseEntity<AccountDTO> deleteAccountById(@PathVariable Integer id) {
        Optional<Account> maybeAccount = accountService.getById(id);

        if (!maybeAccount.isPresent())
            return ResponseEntity.notFound().build();

        accountRepository.delete(maybeAccount.get());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/api/account")
    public List<AccountDTO> getAllAccounts(@RequestParam(name = "includeBusyDays", required = false) boolean includeBusyDays) {
        return accountRepository.findAll().stream()
                .map(account -> {
                    AccountDTO dto = account.convertToDTO();
                    if(includeBusyDays)
                        dto.setCalendarEntries(accountService.getAccountCalendar(account));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/api/account/{id}/availability")
    public ResponseEntity accountAvailabilityInPeriod(@PathVariable(name = "id") Integer id,
                                                      @RequestParam(name = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                                                      @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd) {

        Optional<Account> accountResultById = accountService.getById(id);
        if (!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Account account = accountResultById.get();
        List<CalendarEntry> accountFreeDates;
        if(dateStart == null && dateEnd == null) {
            accountFreeDates = accountService.getAccountCalendar(account);
        }
        else if (dateStart != null && dateEnd != null && dateStart.before(dateEnd)){
            accountFreeDates = accountService.getAccountCalendar(account, dateStart, dateEnd);
        }
        else {
            return ResponseEntity.badRequest().body("Badly formed request, check dateStart and dateEnd params");
        }
        return new ResponseEntity<>(accountFreeDates, HttpStatus.OK);
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
