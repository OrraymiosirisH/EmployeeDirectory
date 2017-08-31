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
    public String addDepart(@Valid @ModelAttribute("department") Department department, BindingResult Result, Model model)
    {

        if (Result.hasErrors()) {
            return "adddeparment";
        }
        Iterable<Department>testdepname=departmentRepo.findAllByDepartname(department.getDepartname());
        long count=testdepname.spliterator().getExactSizeIfKnown();
        model.addAttribute("count",count);
        if(count>1)
        {
            String depterror="That is an existing department";
            model.addAttribute("duplicteDpt",depterror);

            return "adddeparment";
        }
//        Iterable<Department>departlist=departmentRepo.findAllByDepartname(department.getDepartname());
//        long count=departlist.spliterator().getExactSizeIfKnown();
        System.out.println("****************"+count+"**************");
        departmentRepo.save(department);

        return "departresult";
    }

    @RequestMapping("/assignhead/{id}")
    public String addhead(@PathVariable("id") long id, Model model) {
        Person person=new Person();
        Department depart=departmentRepo.findOne(id);
        person.setDepartment(depart);
//        person.setDepartment(departmentRepo.findOne(id));
        model.addAttribute("person", person);
        return "addperson";
    }
    @PostMapping("/assignhead")
    public String addhead(@ModelAttribute("person") Person otherperson, Department department, Model model) {
        personRepo.save(otherperson);
        Department testhead= departmentRepo.findOne(otherperson.getId());
        testhead.setDeparthead(otherperson.getId());
        System.out.println(otherperson.getId());
        return "personresult";
    }

    @RequestMapping("/addperson/{id}")
    public String loadPerson(@PathVariable("id") long id, Model model) {
        Person person=new Person();
        person.setDepartment(departmentRepo.findOne(id));
        model.addAttribute("person", person);
        return "addperson";
    }
    @PostMapping("/addperson")
    public String addPerson(@ModelAttribute("person") Person otherperson, Model model) {

            // Save the person to the database
            personRepo.save(otherperson);
        return "personresult";
    }

    @GetMapping("/listdepartment")
    public String showdepartment(Model model){
        model.addAttribute("department", departmentRepo.findAll());
        return "listdepartment";
    }
    @GetMapping("/detaildepartment/{id}")
    public String showDirector(@PathVariable("id") long id, Model model){

        model.addAttribute("department", departmentRepo.findOne(id));
        model.addAttribute("person",personRepo.findOne(id));
        return "detaildepartment";
    }
    @GetMapping("/listperson")
    public String showDirector(Model model){
        model.addAttribute("person", personRepo.findAll());
        return "listperson";
    }
    @GetMapping("/detailperson/{id}")
    public String showPerson(@PathVariable("id") long id, Model model){

        model.addAttribute("deplist", departmentRepo.findDepartmentById(id));

        return "detailperson";
    }

    @GetMapping("/updateperson/{id}")
    public String updatePerson(@PathVariable("id") long id, Model model){
        model.addAttribute("person", personRepo.findOne(id));
        return "addperson";
    }

    @GetMapping("/deleteperson/{id}")
    public String deleteperson(@PathVariable("id") long id){
//        Person persondelete=personRepo.findOne(id);
//        Department test= new Department();
        personRepo.findOne(id).getDepartment().deletePerson(personRepo.findOne(id));
        personRepo.delete(id);
//        test.getPersons().remove(personRepo.findOne(id));
        return "redirect:/listperson";
    }


}
