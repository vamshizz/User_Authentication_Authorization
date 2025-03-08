package com.example.RegisterandLogin.Controller;

import com.example.RegisterandLogin.Configuration.JwtUtil;
import com.example.RegisterandLogin.DTO.AmountDTO;
import com.example.RegisterandLogin.DTO.LoginDto;
import com.example.RegisterandLogin.DTO.SignupDTO;
import com.example.RegisterandLogin.Model.Account;
import com.example.RegisterandLogin.Model.User;
import com.example.RegisterandLogin.Repository.AccountRepository;
import com.example.RegisterandLogin.Repository.AuthenticationRepository;
import com.example.RegisterandLogin.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class authenticationcontroller {
    private   static final Logger log = LoggerFactory.getLogger(authenticationcontroller.class);

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> registeruser(@RequestBody SignupDTO signupDTO){
        String accountNumber=null;
                ;
           do {
                accountNumber = generateAccountNumber();
               System.out.println(accountNumber);
           }while (accountRepository.existsById(accountNumber));

        User user=new User();
        Account account=new Account();
        user.setEmail(signupDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(signupDTO.getPassword()));
        account.setAccountName(signupDTO.getAccountName());
        account.setAccountBalance(0);
        account.setAccountNumber(accountNumber);
        User savedUser = userRepository.save(user);
        account.setUser(savedUser);


        accountRepository.save(account);

        return  ResponseEntity.ok("successfull");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginuser(@RequestBody LoginDto loginDto) {

        System.out.println(loginDto);
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        if (user != null) {
            if (bCryptPasswordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
                String token = jwtUtil.generateToken(user.get().getEmail());
                return ResponseEntity.ok(token);
            }
        } else {
            return ResponseEntity.ok("user not found");
        }

        return ResponseEntity.ok("Internal Server Error");
    }

    @GetMapping("/user")
    public ResponseEntity<String> user() {

        return ResponseEntity.ok("successfull");

    }


    @GetMapping("/checkbalance")
    public ResponseEntity<Integer> getAccountBalance() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Account not found for user"));
        log.info(String.valueOf(account.getAccountBalance()));
        return ResponseEntity.ok(account.getAccountBalance());
    }

    @Transactional
    @PostMapping("/addbalance")
    public ResponseEntity<String> addMoney(@RequestBody AmountDTO amountDTO) {
        if (amountDTO.getAmount() > 0) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Account account = accountRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Account not found for user"));
            account.setAccountBalance(account.getAccountBalance() + amountDTO.getAmount());
            return ResponseEntity.ok("Money Added Successfully");
        } else {
            ResponseEntity.badRequest().body("Enter Amount Greater than Zero");
        }
        return ResponseEntity.internalServerError().body("Internal Server Error");
    }

    @Transactional
    @PostMapping("/withdrawl")
    public ResponseEntity<String> withdrawMoney(@RequestBody AmountDTO amountDTO) {
        if (amountDTO.getAmount() <= 0) {  // Fix: Check should be <= instead of <
            return ResponseEntity.badRequest().body("Enter an amount greater than zero");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Account not found for user"));

        if (amountDTO.getAmount() > account.getAccountBalance()) {
            return ResponseEntity.badRequest().body("Insufficient account balance");
        }

        account.setAccountBalance(account.getAccountBalance() - amountDTO.getAmount());

        return ResponseEntity.ok("Withdrawal successful");
    }

    public static String generateAccountNumber() {
        String uuid = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        System.out.println(uuid);// Keep only digits
        return uuid.substring(0, 10);
    }
}
