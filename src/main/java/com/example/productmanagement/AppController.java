package com.example.productmanagement;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppController {
  Logger logger = LogManager.getLogger(AppController.class);
  @Autowired
  private ProductService service;

  @RequestMapping("/")
  public String viewHomePage(Model model) {
    List<Product> listProducts = service.listAll();
    model.addAttribute("listProducts", listProducts);

    return "index";
  }

  @RequestMapping("/new")
  public String showNewProductForm(Model model) {
    Product product = new Product();
    model.addAttribute("product", product);

    return "new_product";
  }

  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String saveProduct(@ModelAttribute("product") Product product) {
    service.save(product);

    return "redirect:/";
  }

  @RequestMapping("/edit/{id}")
  public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
    ModelAndView mav = new ModelAndView("edit_product");

    Product product = service.get(id);
    mav.addObject("product", product);

    return mav;
  }

  @RequestMapping("/delete/{id}")
  public String deleteProduct(@PathVariable(name = "id") Long id) {
    service.delete(id);

    return "redirect:/";
  }

  @GetMapping("/get")
  public List<Product> list() {
    return service.listAll();
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<Product> get(@PathVariable Long id) {
    try {
      Product product = service.get(id);
      return new ResponseEntity<Product>(product, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/post")
  public void add(@RequestBody Product product) {
    service.save(product);
  }

  @PutMapping("/put/{id}")
  public ResponseEntity<?> update(@RequestBody Product product, @PathVariable long id) {
    try {
      Product existProduct = service.get(id);
      service.save(existProduct);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/del/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

}