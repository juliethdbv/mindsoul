package com.sena.mindsoul.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.lowagie.text.DocumentException;
import com.sena.mindsoul.entity.Appointment;
import com.sena.mindsoul.entity.User;
import com.sena.mindsoul.repository.SpecialtyRepository;
import com.sena.mindsoul.repository.UserRepository;
import com.sena.mindsoul.security.SecurityUtils;
import com.sena.mindsoul.service.AppointmentService;
import com.sena.mindsoul.service.RecordService;
import com.sena.mindsoul.util.RecordPacExporterPdf;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RequestMapping("/paciente")
@SessionAttributes("paciente")
@Controller
public class PacienteController {

    @Autowired
    private RecordService recordRepository;

    @Autowired
    private AppointmentService appointmentRepository;

    @Autowired
    private UserRepository userRepository; 

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpecialtyRepository specialtyRepository;
    // MODULO DATOS PERSONALES

    // Inicio Paciente

    @GetMapping("")
    public String listar(Model m) {
        String currentUser = SecurityUtils.getUserName();
        User user = userRepository.findByEmail(currentUser);
        User users = new User();
        m.addAttribute("users", users);
        m.addAttribute("user", user);
        return "paciente/homePaciente";
    }

    // Cambiar Contraseña

    @PostMapping("/cambiarClave")
    public String cambiarClave(@Valid User user, SessionStatus status) {

        try {
            String emailCurrentUser = SecurityUtils.getUserName();
            User userEncontrado = userRepository.findByEmail(emailCurrentUser);
            userEncontrado.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userEncontrado);
            status.setComplete();
            return "redirect:/paciente";

        } catch (Exception e) {

            return "paciente/modulos/homePaciente";
        }
    }

    // MODULO MIS CITAS

   //ver citas
       

    @GetMapping("/formAgCita")
    public String formAgCita(Model m){
        m.addAttribute("appointment", appointmentRepository.findAll());
        m.addAttribute("specialities", specialtyRepository.findAll());
        return "paciente/modulos/formAgCita";
    }

    @GetMapping("/formAgCita2/{id}")
    public String formAgCita2(Model m, @PathVariable Long id, SessionStatus status){   
        Appointment  a = appointmentRepository.findOne(id);
        //Appointment appointment = new Appointment();
        //User au = appointment.getUsers();
        /* if (reslt.hasErrors()) {
            return "paciente/modulos/formAgCita";
        } */
        try{
            String emailCurrentUser = SecurityUtils.getUserName();
            User userEncontrado = userRepository.findByEmail(emailCurrentUser);
            a.setStateAppointment(true);
            a.setUsers(userEncontrado);
            appointmentRepository.save(a);
            status.setComplete();
            m.addAttribute("appointment", a);
            //m.addAttribute("specialities", au.getSpecialities());
            return "paciente/modulos/formAgCita2";
        }
        catch (Exception e) {
            // duplicate primary key
            m.addAttribute("appointments", a);
            m.addAttribute("error", "La cita no se ha podido agendar.");
            System.out.println("ya existe");
            return "psicologo/modulos/formcita";
        }
        
    }
    
    // Modificar Estado de Cita (Cancelado / Pendiente)
    
    @GetMapping("/modE/{id}")
    public String modE(Model m, @PathVariable Long id, SessionStatus status){   
        Appointment  a = appointmentRepository.findOne(id);
        try{
            a.setStateCancel(true);
            appointmentRepository.save(a);
            status.setComplete();
            m.addAttribute("appointments", a);
            return "redirect:/paciente/listarcita";
        }
        catch (Exception e) {
            // duplicate primary key
            m.addAttribute("appointments", a);
            m.addAttribute("error", "No se pudo cancelar la cita.");
            System.out.println("ya existe");
            return "redirect:/paciente/formMotivo";
        }
    }

    @GetMapping("/formMotivo/{id}")
    public String formMotivo(Model m, @PathVariable Long id){
        Appointment a = appointmentRepository.findOne(id);
        m.addAttribute("appointments", a);
        return "paciente/modulos/formMotivo";
    }

    // Listar Citas Paciente
    @GetMapping("/listarcita")
    public String listarMed(Model m) {
        m.addAttribute("citas", appointmentRepository.findAll());
        return "paciente/modulos/listarCita";
    }

    // MODULO VER HISTORIAL MÉDICO

    // Listar Historial Médico

    @GetMapping("/listarHist")
    public String listarHist(Model m) {
        
        m.addAttribute("historiales", recordRepository.findAll());
        return "paciente/modulos/listarHist";
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

        RecordPacExporterPdf exporterPDF = new RecordPacExporterPdf(recordRepository.findAll());
        exporterPDF.exportar(response);
    }

    // Ver Citas
    @GetMapping("/vercita/{id}")
    public String verCita(@PathVariable Long id, Model m) {
        m.addAttribute("citas", appointmentRepository.findOne(id));
        return "/paciente/modulos/verCita";
    }

}
