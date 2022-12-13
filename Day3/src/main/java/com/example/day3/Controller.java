package com.example.day3;


import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    ArrayList<Customer> customers = new ArrayList<>();
    static final Faker fakerData = new Faker();

    @GetMapping("/customers")
    public ArrayList<Customer> getCustomers(){
        return customers;
    }
    @GetMapping("/customer/{index}")
    public Customer getCustomer(@PathVariable int index){
        return customers.get(index);
    }

    @PostMapping ("/add")
    public ApiResponse addCustomer(@RequestBody Customer customer){
        customers.add(customer);
        return new ApiResponse("customer added!");
    }

    @PutMapping("/update/{index}")
    public ApiResponse updateCustomer(@PathVariable int index, @RequestBody Customer customer){

        customers.set(index,customer);
        return new ApiResponse("customer updated");
    }

    @DeleteMapping("/delete/all")
    public ApiResponse deleteCustomer(){
        customers = null;
        customers = new ArrayList<>();
        return new ApiResponse("All data deleted");
    }

    @DeleteMapping("/delete/{index}")
    public ApiResponse deleteCustomer(@PathVariable int index){
        customers.remove(index);
        return new ApiResponse("customer deleted!");
    }

    @PutMapping("/operation/deposit/{index}/{amount}")
    public ApiResponse depositMoney(@PathVariable BigDecimal amount,@PathVariable int index){

        BigDecimal amountBeforeDeposit = customers.get(index).getBalance();
        System.out.println(amountBeforeDeposit);

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            return new ApiResponse("Wrong amount!");
        }

        customers.get(index).setBalance(amountBeforeDeposit.add(amount));
        return new ApiResponse("ID : "+customers.get(index).getId()+" new amount "+customers.get(index).getBalance());
    }

    @PutMapping("/operation/withdraw/{index}/{amount}")
    public ApiResponse withdrawMoney(@PathVariable BigDecimal amount,@PathVariable int index){

        BigDecimal currentAmount = customers.get(index).getBalance();

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            return new ApiResponse("Please enter positive amount : "+amount);
        }

        if(currentAmount.subtract(amount).compareTo(BigDecimal.ZERO) <= 0){
            return new ApiResponse("You cant not withdraw! your amount : "+customers.get(index).getBalance());
        }

        customers.get(index).setBalance(currentAmount.subtract(amount));
        return new ApiResponse("ID : "+customers.get(index).getId()+" new amount "+customers.get(index).getBalance());
    }

    @GetMapping("/generate/customers")
    public ApiResponse generateCustomers(){

        for (int i = 0; i < 20; i++) {
            customers.add(new Customer(i+1,randomFullName(),new BigDecimal(1000)));
        }
        return new ApiResponse("customers generated");
    }
    private String randomFullName(){
        return fakerData.name().firstName() +" "+fakerData.name().lastName();
    }


}
