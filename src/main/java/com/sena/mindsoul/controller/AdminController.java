package com.sena.mindsoul.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sena.mindsoul.security.SecurityUtils;
import com.lowagie.text.DocumentException;
import com.sena.mindsoul.entity.Role;
import com.sena.mindsoul.entity.Specialty;
import com.sena.mindsoul.entity.User;

import com.sena.mindsoul.repository.RoleRepository;

import com.sena.mindsoul.service.SpecialtyService;
import com.sena.mindsoul.service.UserService;
import com.sena.mindsoul.util.UsersExporterPdf;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RequestMapping("/admin")
@SessionAttributes("admin")
@Controller
public class AdminController {

    @Autowired
    private UserService userRepository;

    @Autowired
    private SpecialtyService specialtyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // MODULO DATOS PERSONALES

    // Inicio Usuario

    @GetMapping("")
    public String adminView(Model m) {
        String currentUser = SecurityUtils.getUserName();
        m.addAttribute("username", currentUser);
        return "admin/homeAdmin";
    }

    // MODULO PSICÓLOGOS

    // Listar MPsicólogo

    @GetMapping("/listarUsuarios")
    public String listarMed(Model m) {

        m.addAttribute("users", userRepository.findAll());

        return "admin/modulos/listarMed";

    }

    // Generar PDF

    @GetMapping("/pdfgenerate")
    public void generatePDF(HttpServletResponse response) throws DocumentException,IOException{
        response.setContentType("application/pdf");	
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerkey = "content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime +  ".pdf";
        response.setHeader(headerkey,headerValue);

        List<User> users = userRepository.findAll();

        UsersExporterPdf exporterPDF = new UsersExporterPdf(users);
        exporterPDF.exportar(response);
    }

    // Formulario Psicólogo

    @GetMapping("/formMed")
    public String formMed(Model m) {
        User user = new User();
        List<Specialty> specialities = specialtyRepository.findAll();

        m.addAttribute("usuario", user);
        m.addAttribute("specialities", specialities);
        m.addAttribute("accion", "Agregar Usuario Psicólogo");

        return "admin/modulos/formMed";
    }

    // Agregar y registrar Psicólogo

    @PostMapping("/formMed/addMed")
    public String addMed(@Valid User user, BindingResult reslt, RedirectAttributes ra,  Model m, SessionStatus status) {

        if (reslt.hasErrors()) {
            return "admin/modulos/formMed";
        }
        try {
            Role role = roleRepository.findByName("ROLE_PSICOLOGO");
            if (role == null) {
                role = createRolePsicologo();
            }
            user.setPassword(passwordEncoder.encode(user.getEmail()));
            user.setRoles(Arrays.asList(role));
            userRepository.save(user);
            status.setComplete();
            ra.addFlashAttribute("msnExito", "El Psicologo ha sido registrado");
            return "redirect:/admin/listarUsuarios";

        } catch (Exception e) {
            // duplicate primary key
            m.addAttribute("usuario", user);
            m.addAttribute("error", "El usuario ya existe");
            System.out.println("ya existe");
            return "admin/modulos/formMed";
        }
    }

    // creamos el role si no existe se hace por defecto no borrarlo porque no se
    // puede crear desde la app
    public Role createRolePsicologo() {
        Role psicologo = new Role();
        psicologo.setName("ROLE_PSICOLOGO");
        return roleRepository.save(psicologo);
    }

    // Traer datos del Psicólogo por Id al formulario

    @GetMapping("/verMed/{id}")
    public String verMed(@PathVariable Long id, Model m) {
        User user = null;
        if (id > 0) {
            user = userRepository.findOne(id);
        } else {
            return "redirect:listarMed";
        }
        List<Specialty> specialities = specialtyRepository.findAll();
        List<Role> roles = roleRepository.findAll();
        m.addAttribute("usuario", user);
        m.addAttribute("specialities", specialities);
        m.addAttribute("roles", roles);
        m.addAttribute("accion", "Actualizar Usuario Psicologo");
        return "admin/modulos/formMed";
    }

    // MODULO ESPECIALIDAD

    // Listar Especialidad

    @GetMapping("/listarEsp")
    public String listarEsp(Model m) {
        m.addAttribute("especialidades", specialtyRepository.findAll());
        return "admin/modulos/listarEsp";
    }

    // Formulario Especialidad

    @GetMapping("/formEsp")
    public String formEsp(Model m) {

        Specialty specialty = new Specialty();

        m.addAttribute("especialidades", specialty);
        m.addAttribute("accion", "Agregar Especialidad");

        return "admin/modulos/formEsp";
    }

    // Registrar o agregar nueva Especialidad

    @PostMapping("/addEsp")
    public String addEsp(@Valid Specialty specialty, BindingResult reslt, Model m, SessionStatus status) {
        if (reslt.hasErrors()) {
            return "admin/modulos/formEsp";
        }
        try {
            specialtyRepository.save(specialty);
            status.setComplete();
            return "redirect:listarEsp";

        } catch (Exception e) {
            // duplicate primary key
            m.addAttribute("especialidades", specialty);
            m.addAttribute("error", "La especialidad ya existe en la base de datos");
            System.out.println("ya existe");
            return "admin/modulos/formEsp";
        }
    }

    // Cambiar estado 

    @GetMapping("/cambiarEstado/{id}")
        public String cambiarEstado(@PathVariable Long id){
            Specialty specialty = specialtyRepository.findOne(id);

            specialty.setStateSpecialty(!specialty.getStateSpecialty());

            specialtyRepository.save(specialty);
            return "redirect:/admin/listarEsp";
        }

    // Traer datos de especialidad al fomulario por Id

    @GetMapping("/verEsp/{id}")
    public String verEsp(@PathVariable Long id, Model m) {
        Specialty specialty = null;
        if (id > 0) {
            specialty = specialtyRepository.findOne(id);
        } else {
            return "redirect:listarEsp";
        }
        m.addAttribute("especialidades", specialty);
        m.addAttribute("accion", "Actualizar Especialidad");
        return "admin/modulos/formEsp";
    }

    // MODULO ROLES

    // Listar Roles

    @GetMapping("/listarRol")
    public String listarRol(Model m) {
        m.addAttribute("roles", roleRepository.findAll());
        return "admin/modulos/listarRol";

    }

}
