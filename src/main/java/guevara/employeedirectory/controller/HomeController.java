package guevara.employeedirectory.controller;


import guevara.employeedirectory.models.Department;
import guevara.employeedirectory.models.Person;
import guevara.employeedirectory.repositories.DepartmentRepo;
import guevara.employeedirectory.repositories.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class HomeController {

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    PersonRepo personRepo;



    @GetMapping("/")
    public String sayHello() {

        return "menu";
    }



    @GetMapping("/adddepart")
    public String loadDepart(Model model){
        model.addAttribute("department", new Department());

        return"adddeparment";

    }


    @PostMapping("/adddepart")
    public String addDepart(@Valid @ModelAttribute("department") Department department, BindingResult Result)
    {


        if (Result.hasErrors()) {
            return "adddeparment";
        }

        departmentRepo.save(department);

        return "departresult";
    }

    @GetMapping("/assignhead{id}")
    public String addHead (@PathVariable("id") long id,Model model) {
        Person person=new Person();
        person.setDepartment(departmentRepo.findOne(id));
        System.out.println(person.getDepartment().getId());
        model.addAttribute("person", person);

        return"addhead";
    }
    @PostMapping("/assignhead")
    public String postHead(@Valid @ModelAttribute("person") Person person,
                           @ModelAttribute("department") Department department, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            return"addhead";
        }

        personRepo.save(person);
        department.setDeparthead(person.getFirstname());
        departmentRepo.save(department);
        System.out.println(person.getFirstname());
        System.out.println(person.getDepartment().getId());
        return "headresult";
    }

    @GetMapping("/addperson{id}")
    public String loadPerson(@PathVariable("id") long id, Model model) {
        Person person=new Person();
        person.setDepartment(departmentRepo.findOne(id));
        model.addAttribute("person", person);
        return "addperson";
    }
    @PostMapping("/addperson")
    public String addPerson(@Valid @ModelAttribute("person") Person person,@ModelAttribute("department") Department department
                              ,BindingResult Result) {


        System.out.println(Result.toString());
        if (Result.hasErrors()) {
            return "addperson";
        }

        System.out.println("no errors");

        // Save the person to the database
        personRepo.save(person);

        return "personresult";

    }






}
