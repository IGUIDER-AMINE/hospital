package ma.enset.hospital.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.enset.hospital.entities.Patient;
import ma.enset.hospital.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor // L'injection via le constructeur
public class PatientController {
    private PatientRepository patientRepository;

    //http://localhost:8084/index?page=1&size=5
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "0") int page,
                        @RequestParam(name = "size",defaultValue = "4") int size,
                        @RequestParam(name = "keyword",defaultValue = "") String keyword){
        //Page<Patient> pagePatients = patientRepository.findAll(PageRequest.of(page,size));
        Page<Patient> pagePatients = patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("listPatients",pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "patients"; // la vue
    }
    @GetMapping("/delete")
    public String delete(Long id,
                         @RequestParam(name = "keyword",defaultValue = "") String keyword,
                         @RequestParam(name = "page",defaultValue = "0") int page){
        patientRepository.deleteById(id);
        return "redirect:/index?page=" + page + "&keyword=" + keyword; // la vue
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/index"; // la vue
    }

    @GetMapping("/patients")
    @ResponseBody
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }

    @GetMapping("/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatients"; // la vue
    }

    // insert | update
    @PostMapping(path = "/save")
    // @Valid => une fois que tu stocke les données dans patient tu fait la validation, stocke les erreur dans BindingResult
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0")  int page,
                       @RequestParam(defaultValue = "")  String keyword){
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient); // if id paitent == null => insert , else id !=null => update
        //return "redirect:/formPatients"; // nouveau model
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/editPatient")
    public String editPatient(Model model,Long id,String keyword, int page){
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient", patient);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "editPatient"; // la vue
    }

}
