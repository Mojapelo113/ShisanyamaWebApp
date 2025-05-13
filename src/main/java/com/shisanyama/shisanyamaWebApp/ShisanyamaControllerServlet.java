package com.shisanyama.shisanyamaWebApp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@SessionAttributes("user")
public class ShisanyamaControllerServlet {

    @Autowired
    private MyEntityRepository mer;

    private List<Item> cartList = new ArrayList<>();
    private User user = new User();
    private Boolean validLogins=false;


    @GetMapping("/profile")
    public ModelAndView showProfile() {
        ModelAndView modelAndView = new ModelAndView();
        if(user != null){
            modelAndView.addObject("user",user);
        }

        modelAndView.addObject("loginSuccess","logout");

        modelAndView.setViewName("my_account");
        return modelAndView;
    }

    @GetMapping("/getProduct")
    public String getProfuct(){
      return "Full Chicken description";
    }

    @PostMapping("/validateLogins")
    public ModelAndView validateLogins(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        user = mer.findUserByEmail(username);



        if (user != null) {
            if (user.getEmail().equals(username)) {
                if (user.getPassword().equals(password)) {
                    validLogins = true;
                }
            }



       if(!validLogins){
         modelAndView.addObject("invalidLogins", "Username or password is incorrect");
         modelAndView.setViewName("login_page");
       }else{
           modelAndView.addObject("loginSuccess","logout");
           modelAndView.setViewName("index");
           modelAndView.addObject("user",user);
       }
        }else{
            modelAndView.addObject("userNotExist", "User does not exist");
            modelAndView.setViewName("login_page");
        }

       return modelAndView;
    }

        @GetMapping("/loginPage")
        public ModelAndView showLoginPage() {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("login_page");
            return modelAndView;
        }

    @PostMapping("/showCheckoutPage")
    public ModelAndView showCheckoutPage() {
        ModelAndView modelAndView = new ModelAndView();

        if(user.getFirstName()==null){
            modelAndView.setViewName("login_page");
        }else{
        modelAndView.setViewName("checkout_page");
        }
        return modelAndView;
    }

    @PostMapping("/addOrder")
    public ModelAndView addOrder(@RequestParam String address) {
        ModelAndView modelAndView = new ModelAndView();
        List<Order> orders = new ArrayList<>();


        for(Item item : cartList){
            Order order = new Order();
            order.setUser(user);
            order.setAmountDue(item.getPrice());
            order.setItemName(item.getTitle());
            order.setLocation(address);
            orders.add(order);
        }

        user.setListOrders(orders);
        mer.save(user);


        DecimalFormat ft = new DecimalFormat("R#,###,###.00");
        double totalPrice = 0;
        double vatAmount = 0;
        double amountDue =0;
        for (Item item : cartList) {
            totalPrice += item.getPrice();
        }
        vatAmount = totalPrice*0.15;
        amountDue = vatAmount+totalPrice;

        modelAndView.addObject("amountDue",ft.format(amountDue));
        modelAndView.addObject("vatAmount",ft.format(vatAmount));
        modelAndView.addObject("totalPrice",ft.format(totalPrice));
        modelAndView.addObject("user",user);
        modelAndView.addObject("cartList", cartList);
        modelAndView.addObject("location",address);
        modelAndView.setViewName("reciet_page");
        return modelAndView;
    }


    @GetMapping("/go_to_cart")
    public ModelAndView showCartPage() {
        ModelAndView modelAndView = new ModelAndView();
        DecimalFormat ft = new DecimalFormat("R#,###,###.00");
        double totalPrice = 0;
        double vatAmount = 0;
        double amountDue =0;
        for (Item item : cartList) {
            totalPrice += item.getPrice();
        }
        vatAmount = totalPrice*0.15;
        amountDue = vatAmount+totalPrice;

        modelAndView.addObject("amountDue",ft.format(amountDue));
        modelAndView.addObject("vatAmount",ft.format(vatAmount));
        modelAndView.addObject("totalPrice",ft.format(totalPrice));
        modelAndView.addObject("cartList", cartList);
        modelAndView.setViewName("my_cart_page");
        return modelAndView;
    }

    @GetMapping("/homePage")
    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user",user);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        ModelAndView modelAndView = new ModelAndView();
        cartList.clear();
        user = new User();
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @PostMapping("/addToCart")
    public ModelAndView addToCart(@RequestParam String image,@RequestParam String title,@RequestParam String price) {

        Double newprice = Double.parseDouble(price);
        Item item = new Item(newprice,title,image);

        cartList.add(item);
        Integer count= cartList.size();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("count",count);
        modelAndView.setViewName("index");
        for(Item i:cartList){
            System.out.println(i.toString());
        }

        return modelAndView;
    }

    @PostMapping("/createAccount")
    public ModelAndView createAccount(@RequestParam String name, @RequestParam String surname, @RequestParam String email, @RequestParam String phone_number, @RequestParam String pass, @RequestParam String confirm_pass) {
        ModelAndView modelAndView = new ModelAndView();

        if (!pass.equals(confirm_pass)) {
            modelAndView.addObject("passError", "Password does not match");
            modelAndView.setViewName("signup_outcome_page");
        } else {
            User exsistingUser =mer.findUserByEmail(email);

            if(exsistingUser==null){
                user = new User(name, surname, phone_number, email, pass, new Date());
            mer.save(user);
            modelAndView.addObject("user", user);
            modelAndView.setViewName("signup_outcome_page");
            }else{
                modelAndView.addObject("message", "User with this email address "+email+ " already exists");
                modelAndView.setViewName("signup_outcome_page");
            }
        }

        return modelAndView;
    }


}
